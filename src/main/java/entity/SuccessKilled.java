package main.java.entity;

import java.util.Date;

public class SuccessKilled {
	private long id;
	private long userPhone;
	private short status;
	private Date createTime;

	private Seckill seckill;

	public Seckill getSeckill() {
		return seckill;
	}

	public void setSeckill(Seckill seckill) {
		this.seckill = seckill;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(long userPhone) {
		this.userPhone = userPhone;
	}

	public short getStatus() {
		return status;
	}

	public void setStatus(short status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "SuccessKilled [id=" + id + ", userPhone=" + userPhone
				+ ", status=" + status + ", createTime=" + createTime
				+ ", seckill=" + seckill + "]";
	}



}
