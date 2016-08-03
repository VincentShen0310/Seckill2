--购买执行存储过程
--;转化为$$
DELIMITER $$
--row_count()返回上一条修改类型sql(insert,update,delete)的影响行数


CREATE PROCEDURE execute_seckill 
(in v_id bigint, in v_phone bigint, in v_kill_time timestamp, out r_result int)
BEGIN
	DECLARE insert_count int DEFAULT 0;
	START TRANSACTION;
	INSERT ignore INTO success_killed
		(id,user_phone,create_time)
		VALUES
		(v_id, v_phone,v_kill_time);
	SELECT row_count() into insert_count;
	IF (insert_count=0) THEN
		ROLLBACK;
		set r_result=-1;
	ELSEIF (insert_count<0) THEN
		ROLLBACK;
		set r_result=-2;
	ELSE
		UPDATE seckill SET number = number-1
		WHERE id = v_id
		and start_time < v_kill_time
		and end_time > v_kill_time
		and number > 0;
		SELECT row_count() into insert_count;
		IF(insert_count=0) THEN
			ROLLBACK;
			set r_result=0;
		ELSEIF(insert_count<0) THEN
			ROLLBACK;
			set r_result=-2;
		ELSE
			COMMIT;
			set r_result=1;
		END IF;
	END IF;
END;
$$

--执行存储过程
DELIMITER ;
set @r_result=-3;
call execute_seckill(4,13088888888,now(),@r_result);





