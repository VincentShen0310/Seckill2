package main.java.dao;

import org.apache.ibatis.annotations.Param;

import main.java.entity.SuccessKilled;

public interface SuccessKilledDAO {

	// 插入成功明细，并过滤重复数据
	int insertSuccessKilled(@Param("id") long id, @Param("userPhone") long userPhone);

	// 根据id查询成功明细
	SuccessKilled queryByIDwithSeckilled(@Param("id") long id, @Param("userPhone") long userPhone);
}
