package main.java.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import main.java.entity.Seckill;

public interface SeckillDAO {

	// 减少库存
	int reduceNumber(@Param("id") long id, @Param("killTime") Date killTime);

	// 根据id查询商品
	Seckill queryByID(@Param("id") long id);

	// 根据偏移值查询列表
	List<Seckill> queryAll(@Param("offset") int offset, @Param("limit") int limit);
	
	// 根据存储过程购买商品
	void executeByProcedure(Map<String, Object> paramMap);
}
