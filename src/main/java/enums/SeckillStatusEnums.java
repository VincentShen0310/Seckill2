package main.java.enums;

public enum SeckillStatusEnums {
	SUCCESS(1, "购买成功"), 
	END(2, "购买结束"), 
	REPEAT_KILL(-1, "重复购买"), 
	INNOR_ERROR(-2, "系统错误"), 
	MD5_ERROR(-3, "MD5错误");

	private int status;
	private String statusInfo;

	private SeckillStatusEnums(int status, String statusInfo) {
		this.status = status;
		this.statusInfo = statusInfo;
	}

	public int getStatus() {
		return status;
	}

	public String getStatusInfo() {
		return statusInfo;
	}

	public static SeckillStatusEnums statusOf(int index) {
		for (SeckillStatusEnums status : values()) {
			if (status.getStatus() == index) {
				return status;
			}
		}
		return null;
	}

}
