package test.java.dao;

import main.java.dao.SuccessKilledDAO;
import main.java.entity.SuccessKilled;

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
public class SuccessKilledDAOTest {

	// 注入DAO实现类依赖
	@Autowired
	private SuccessKilledDAO successKilledDAO;

	@Test
	public void testInsertSuccessKilled() throws Exception {
		long id = 1;
		long userPhone = 13011113333L;
		int i = successKilledDAO.insertSuccessKilled(id, userPhone);
		System.out.println("最终结果为: "+i);
	}

	@Test
	public void testQueryByIDwithSeckilled() throws Exception {
		long id = 1;
		long userPhone = 13011113333L;
		SuccessKilled sk=successKilledDAO.queryByIDwithSeckilled(id, userPhone);
		System.out.println(sk);
		System.out.println(sk.getSeckill());
	}
}
