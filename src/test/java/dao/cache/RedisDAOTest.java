package test.java.dao.cache;

import main.java.dao.SeckillDAO;
import main.java.dao.cache.RedisDAO;
import main.java.entity.Seckill;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:spring/spring-dao.xml" })
public class RedisDAOTest {
	private long id = 2;
	@Autowired
	private RedisDAO redisDAO;
	@Autowired
	private SeckillDAO seckillDAO;

	@Test
	public void testRedisDAO() {
		Seckill seckill = redisDAO.getSeckill(id);
		if (seckill == null) {
			seckill = seckillDAO.queryByID(id);
			if (seckill != null) {
				String result = redisDAO.putSeckill(seckill);
				System.out.println(result);
				seckill = redisDAO.getSeckill(id);
				System.out.println(seckill);
			}
		}

	}

}
