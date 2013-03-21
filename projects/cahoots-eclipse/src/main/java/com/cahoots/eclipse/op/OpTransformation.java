package com.cahoots.eclipse.op;

import java.util.Objects;

public class OpTransformation implements Comparable<OpTransformation> {
	private Long tickStamp;
	private Boolean applied = false;

	public Long getTickStamp() {
		return tickStamp;
	}

	public void setTickStamp(final Long tickStamp) {
		this.tickStamp = tickStamp;
	}

	public Boolean getApplied() {
		return applied;
	}

	public void setApplied(final Boolean applied) {
		this.applied = applied;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof OpTransformation) {
			final OpTransformation opTransformation = (OpTransformation) obj;
			return Objects.equals(tickStamp, opTransformation.tickStamp);
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(tickStamp);
	}

	@Override
	public int compareTo(final OpTransformation o) {
		return tickStamp.compareTo(o.tickStamp);
	}

}
