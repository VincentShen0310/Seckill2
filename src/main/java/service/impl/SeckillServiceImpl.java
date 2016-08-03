package main.java.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import main.java.dao.SeckillDAO;
import main.java.dao.SuccessKilledDAO;
import main.java.dao.cache.RedisDAO;
import main.java.dto.ExecuteSeckill;
import main.java.dto.Exposer;
import main.java.entity.Seckill;
import main.java.entity.SuccessKilled;
import main.java.enums.SeckillStatusEnums;
import main.java.execption.SeckillClosedExecption;
import main.java.execption.SeckillExecption;
import main.java.execption.SeckillRepeatExecption;
import main.java.service.SeckillService;

@Service
public class SeckillServiceImpl implements SeckillService {

	@Autowired
	private SeckillDAO seckillDAO;
	
	@Autowired
	private SuccessKilledDAO successKilledDAO;
	
	@Autowired
	private RedisDAO redisDAO;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private final String salt = "abcd1234";

	public Seckill getProductByID(long id) {
		return seckillDAO.queryByID(id);
	}


	public List<Seckill> getAllProduct() {
		return seckillDAO.queryAll(0, 100);
	}


	public Exposer exportSeckillUrl(long id) {
		//访问redis
		Seckill seckill = redisDAO.getSeckill(id);
		if (seckill == null) {
			//访问数据库
			seckill = seckillDAO.queryByID(id);
			if (seckill == null) {
				return new Exposer(false, id);
			}
			else {
				//更新redis
				redisDAO.putSeckill(seckill);
			}
		}

		Date startTime = seckill.getStartTime();
		Date endTime = seckill.getEndTime();
		Date nowTime = new Date();
		if (nowTime.getTime() < startTime.getTime()
				|| nowTime.getTime() > endTime.getTime()) {
			return new Exposer(false, id, nowTime.getTime(),
					startTime.getTime(), endTime.getTime());
		}
		String md5 = getMD5(id);
		return new Exposer(true, id, md5);
	}


	@Transactional
	public ExecuteSeckill executeSeckill(long id, long userPhone, String md5)
			throws SeckillExecption, SeckillRepeatExecption,
			SeckillClosedExecption {
		if (md5 == null || !md5.equals(getMD5(id))) {
			return new ExecuteSeckill(id,SeckillStatusEnums.MD5_ERROR);
		}
		Date nowTime = new Date();

		//插入购买记录调整到减库存操作之前，减少网络延迟（insert操作无行级锁，update操作有行级锁）
		try {
			// 插入购买记录
			int insertCount = successKilledDAO.insertSuccessKilled(id,userPhone);
			if (insertCount <= 0) {
				//重复购买
				throw new SeckillRepeatExecption("Seckill repeat!!!");
			} else {
				// 减库存
				int updateCount = seckillDAO.reduceNumber(id, nowTime);
				if (updateCount <= 0) {
					//购买结束
					throw new SeckillClosedExecption("Seckill is closed!!!");
				} else {
					//购买成功
					SuccessKilled successKilled = successKilledDAO
							.queryByIDwithSeckilled(id, userPhone);
					return new ExecuteSeckill(id, SeckillStatusEnums.SUCCESS, successKilled);
				}
			}
			
		} catch (SeckillClosedExecption e1) {
			throw e1;
		} catch (SeckillRepeatExecption e2) {
			throw e2;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			// 编译期异常转换为运行期异常
			throw new SeckillExecption("Seckill error happened"
					+ e.getMessage());
		}

	}

	private String getMD5(long id) {
		String base = id + salt;
		String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
		return md5;
	}
	
	//使用存储过程
	public ExecuteSeckill executeSeckillProcedure(long id,long userPhone,String md5){
		if (md5 == null || !md5.equals(getMD5(id))) {
			return new ExecuteSeckill(id,SeckillStatusEnums.MD5_ERROR);			
		}
		Date killTime = new Date();
		Map<String, Object> map=new HashMap<String,Object>();
		map.put("id", id);
		map.put("userPhone", userPhone);
		map.put("killTime", killTime);
		map.put("result", null);
		try {
			seckillDAO.executeByProcedure(map);
			int result=MapUtils.getInteger(map, "result", -2);
			if (result==1) {
				SuccessKilled successKilled = successKilledDAO
						.queryByIDwithSeckilled(id, userPhone);
				return new ExecuteSeckill(id, SeckillStatusEnums.SUCCESS, successKilled);
			}
			else {
				return new ExecuteSeckill(id, SeckillStatusEnums.statusOf(result));
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return new ExecuteSeckill(id, SeckillStatusEnums.INNOR_ERROR);
		}

	}
	
}
