package com.cahoots.json.receive;

import com.cahoots.eclipse.op.OpTransformation;

public class OpReplaceMessage extends OpTransformation {
	private String user;
	private String opId;
	private String contents;
	private Integer start;
	private Integer end;

	public OpReplaceMessage() {
	}

	public String getUser() {
		return user;
	}

	public void setUser(final String user) {
		this.user = user;
	}

	public String getOpId() {
		return opId;
	}

	public void setOpId(final String opId) {
		this.opId = opId;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(final String content) {
		this.contents = content;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(final Integer start) {
		this.start = start;
	}

	public Integer getEnd() {
		return end;
	}

	public void setEnd(final Integer end) {
		this.end = end;
	}

}
