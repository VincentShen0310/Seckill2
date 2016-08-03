package main.java.dto;

import main.java.entity.SuccessKilled;
import main.java.enums.SeckillStatusEnums;

public class ExecuteSeckill {
	
	private long id;
	
	private int status;
	
	private String statusInfo;
	
	private SuccessKilled successKilled;

	public ExecuteSeckill(long id, SeckillStatusEnums statusEnums,
			SuccessKilled successKilled) {
		this.id = id;
		this.status = statusEnums.getStatus();
		this.statusInfo = statusEnums.getStatusInfo();
		this.successKilled = successKilled;
	}

	public ExecuteSeckill(long id, SeckillStatusEnums statusEnums) {
		this.id = id;
		this.status = statusEnums.getStatus();
		this.statusInfo = statusEnums.getStatusInfo();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(short status) {
		this.status = status;
	}

	public String getStatusInfo() {
		return statusInfo;
	}

	public void setStatusInfo(String statusInfo) {
		this.statusInfo = statusInfo;
	}

	public SuccessKilled getSuccessKilled() {
		return successKilled;
	}

	public void setSuccessKilled(SuccessKilled successKilled) {
		this.successKilled = successKilled;
	}

	@Override
	public String toString() {
		return "ExecuteSeckill [id=" + id + ", status=" + status
				+ ", statusInfo=" + statusInfo + ", successKilled="
				+ successKilled + "]";
	}
	

}
