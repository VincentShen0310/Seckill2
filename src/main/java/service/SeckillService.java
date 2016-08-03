package main.java.service;

import java.util.List;

import main.java.dto.ExecuteSeckill;
import main.java.dto.Exposer;
import main.java.entity.Seckill;
import main.java.execption.SeckillClosedExecption;
import main.java.execption.SeckillExecption;
import main.java.execption.SeckillRepeatExecption;

public interface SeckillService {

	Seckill getProductByID(long id);

	List<Seckill> getAllProduct();

	// 开启则输出地址，否则输出系统时间和开始时间
	Exposer exportSeckillUrl(long id);

	// 购买操作
	ExecuteSeckill executeSeckill(long id, long userPhone, String md5)
			throws SeckillExecption, SeckillRepeatExecption, SeckillClosedExecption;

	//购买操作（使用存储过程）
	ExecuteSeckill executeSeckillProcedure(long id, long userPhone, String md5);
}
