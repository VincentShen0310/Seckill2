package main.java.dto;

public class Exposer {

	// 是否开启
	private boolean exposed;

	private long id;

	private String md5;

	private long now;

	private long start;

	private long end;

	public Exposer(boolean exposed, long id, String md5) {
		this.exposed = exposed;
		this.id = id;
		this.md5 = md5;
	}


	public Exposer(boolean exposed, long id, long now, long start, long end) {
		this.exposed = exposed;
		this.id = id;
		this.now = now;
		this.start = start;
		this.end = end;
	}


	public Exposer(boolean exposed, long id) {
		this.exposed = exposed;
		this.id = id;
	}

	public boolean isExposed() {
		return exposed;
	}

	public void setExposed(boolean exposed) {
		this.exposed = exposed;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public long getNow() {
		return now;
	}

	public void setNow(long now) {
		this.now = now;
	}

	public long getStart() {
		return start;
	}

	public void setStart(long start) {
		this.start = start;
	}

	public long getEnd() {
		return end;
	}

	public void setEnd(long end) {
		this.end = end;
	}


	@Override
	public String toString() {
		return "Exposer [exposed=" + exposed + ", id=" + id + ", md5=" + md5
				+ ", now=" + now + ", start=" + start + ", end=" + end + "]";
	}

}
