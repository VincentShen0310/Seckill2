package test.java.dao;

import java.util.Date;
import java.util.List;

import main.java.dao.SeckillDAO;
import main.java.entity.Seckill;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/*
 * 配置spring和junit整合，junit启动时加载springIOC容器
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:spring/spring-dao.xml" })
public class SeckillDAOTest {

	// 注入DAO实现类依赖
	@Autowired
	private SeckillDAO seckillDAO;

	@Test
	public void testQueryByID() throws Exception {
		long id = 2;
		Seckill seckill = seckillDAO.queryByID(id);
		System.out.println(seckill.getName());
		System.out.println(seckill);

	}

	@Test
	public void testQueryAll() throws Exception {
		List<Seckill> listSeckills = seckillDAO.queryAll(0, 10);
		for (Seckill seckill : listSeckills) {
			System.out.println(seckill);
		}

	}

	@Test
	public void testReduceNumber() throws Exception {
		Date killtimeDate = new Date();
		int updateCount = seckillDAO.reduceNumber(1, killtimeDate);
		System.out.println("updateCount=" + updateCount);

	}
}
