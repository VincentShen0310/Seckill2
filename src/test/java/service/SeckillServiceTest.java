package test.java.service;

import java.util.List;

import main.java.dto.ExecuteSeckill;
import main.java.dto.Exposer;
import main.java.entity.Seckill;
import main.java.execption.SeckillClosedExecption;
import main.java.execption.SeckillRepeatExecption;
import main.java.service.SeckillService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:spring/spring-dao.xml","classpath:spring/spring-service.xml" })
public class SeckillServiceTest {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private SeckillService seckillService;
	
	@Test
	public void testGetAllProduct(){
		List<Seckill> seckills=seckillService.getAllProduct();
		logger.info("list={}",seckills);
	}

	@Test
	public void testGetProductByID(){
		long id=3;
		Seckill seckill=seckillService.getProductByID(id);
		logger.info("seckill={}",seckill);
	}
	
	@Test
	public void testExportSeckillUrl(){
		long id=2;
		Exposer exposer=seckillService.exportSeckillUrl(id);
		logger.info("seckill={}",exposer);
	}
	
	@Test
	public void testExecuteSeckill() {
		long id = 3;
		long userPhone = 13055550000L;
		String md5 = "a980d6f052b99e5675e6fff68669ccde";
		try {
			ExecuteSeckill executeSeckill = seckillService.executeSeckill(id,
					userPhone, md5);
			logger.info("The test result={}", executeSeckill);
		} catch (SeckillRepeatExecption e1) {
			logger.error(e1.getMessage());
		} catch (SeckillClosedExecption e2) {
			logger.error(e2.getMessage());
		}
	}

	@Test
	//testExportSeckillUrl和testExecuteSeckill结合的测试用例，可重复执行
	public void testSeckillLogic(){
		long id=3;
		Exposer exposer=seckillService.exportSeckillUrl(id);
		if (exposer.isExposed()) {
			logger.info("seckill={}",exposer);
			long userPhone = 13055550000L;
			String md5 = exposer.getMd5();
			try {
				ExecuteSeckill executeSeckill = seckillService.executeSeckill(id,
						userPhone, md5);
				logger.info("The test result={}", executeSeckill);
			} catch (SeckillRepeatExecption e1) {
				logger.error(e1.getMessage());
			} catch (SeckillClosedExecption e2) {
				logger.error(e2.getMessage());
			}
		}
		else {
			//购买未开启
			logger.info("seckill={}",exposer);
		}

	}
	
	@Test
	public void testExecuteSeckillProcedure(){
		long id=4;
		long userPhone=13066666666L;
		Exposer exposer=seckillService.exportSeckillUrl(id);
		if (exposer.isExposed()) {
			String md5 = exposer.getMd5();
			ExecuteSeckill executeSeckill = seckillService.executeSeckillProcedure(id,userPhone, md5);
			logger.info(executeSeckill.getStatusInfo());
		}
	}
}
