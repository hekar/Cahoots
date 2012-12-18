package com.cahoots.json.receive;

public class OpReplaceMessage {
	private String user;
	private String opId;
	private String content;
	private Integer start;
	private Integer end;
	private Long tickStamp;

	public OpReplaceMessage() {
	}
	
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getOpId() {
		return opId;
	}

	public void setOpId(String opId) {
		this.opId = opId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getEnd() {
		return end;
	}

	public void setEnd(Integer end) {
		this.end = end;
	}

	public Long getTickStamp() {
		return tickStamp;
	}

	public void setTickStamp(Long tickStamp) {
		this.tickStamp = tickStamp;
	}

}
