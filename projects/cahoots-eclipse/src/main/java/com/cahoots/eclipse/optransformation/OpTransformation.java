package com.cahoots.eclipse.optransformation;

import java.beans.Transient;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.eclipse.ecf.internal.provisional.docshare.cola.UpdateMessage;

public class OpTransformation implements Comparable<OpTransformation> {
	private Long tickStamp;
	private String content;
	private Integer start;
	private Integer replacementLength;
	private String oldContent;
	private Integer moved = 0;
	private String user;
	private Integer localCount;
	private Integer remoteCount;
	private transient Boolean applied = false;
	private transient Set<OpTransformation> offsetAppliedTo = new HashSet<OpTransformation>();

	public boolean isInitialReplace() {
		return tickStamp.equals(0L);
	}
	
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

	public Integer getStart() {
		return start;
	}

	public void setStart(final Integer start) {
		this.start = start;
	}

	public String getContent() {
		return content == null ? "" : content;
	}

	public void setContent(final String contents) {
		this.content = contents;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof OpTransformation) {
			final OpTransformation opTransformation = (OpTransformation) obj;
			return Objects.equals(tickStamp, opTransformation.tickStamp)
					&& Objects.equals(start, opTransformation.start);
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(tickStamp, start);
	}

	@Override
	public int compareTo(final OpTransformation o) {
		final int compareTo = tickStamp.compareTo(o.tickStamp);
		if (compareTo == 0) {
			return start.compareTo(o.start);
		}
		return compareTo;
	}

	public Integer getMoved() {
		return moved;
	}

	public void setMoved(final Integer moved) {
		this.moved = moved;
	}

	public String getUser() {
		return user;
	}

	public void setUser(final String user) {
		this.user = user;
	}

	@Transient
	public Collection<OpTransformation> getOffsetAppliedTo() {
		return offsetAppliedTo;
	}

	public UpdateMessage toUpdateMessage() {
		return new UpdateMessage(getIndex(), getReplacementLength(),
				(getContent() == null) ? "" : getContent());
	}

	public Integer getReplacementLength() {
		return replacementLength;
	}

	public void setReplacementLength(final Integer length) {
		this.replacementLength = length;
	}

	public String getOldContent() {
		return oldContent;
	}

	public void setOldContent(final String oldContent) {
		this.oldContent = oldContent;
	}

	@Transient
	public int getLength() {
		return 0;
	}

	public int getIndex() {
		return getStart() + getMoved();
	}

	public void setIndex(final Integer index) {
		setMoved(index - getStart());
	}

	public Integer getLocalCount() {
		return localCount;
	}

	public void setLocalCount(final Integer localCount) {
		this.localCount = localCount;
	}

	public Integer getRemoteCount() {
		return remoteCount;
	}

	public void setRemoteCount(final Integer remoteCount) {
		this.remoteCount = remoteCount;
	}

	
}
