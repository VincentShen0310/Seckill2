package main.java.dao.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

import main.java.entity.Seckill;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisDAO {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private final JedisPool jedisPool;
	
	public RedisDAO(String ip,int port){
		jedisPool=new JedisPool(ip,port);
	}

	private RuntimeSchema<Seckill> schema=RuntimeSchema.createFrom(Seckill.class);
	public Seckill getSeckill(long id){
		try {
			Jedis jedis=jedisPool.getResource();
			try {
				String key="seckill2:"+id;
				//protostuff自定义序列化
				//get->byte[]->反序列化->Object(Seckill)
				byte[] bytes=jedis.get(key.getBytes());
				
				//获取缓存
				if (bytes!=null) {
					//创建空对象
					Seckill seckill=schema.newMessage();
					//反序列化seckill
					ProtostuffIOUtil.mergeFrom(bytes, seckill, schema);
					return seckill;
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
			finally{
				jedis.close();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	public String putSeckill(Seckill seckill){
		try {
			Jedis jedis=jedisPool.getResource();
			try {
				String key="seckill2:"+seckill.getId();
				//set Object(Seckill)->序列化->byte[]
				byte[] bytes=ProtostuffIOUtil.toByteArray(seckill, schema, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));

				//超时缓存
				int timeout=60*60;//1小时
				String result=jedis.setex(key.getBytes(), timeout, bytes);
				return result;
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
			finally{
				jedis.close();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;

	}
}
