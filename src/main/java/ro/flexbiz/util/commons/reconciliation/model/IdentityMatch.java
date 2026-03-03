package ro.flexbiz.util.commons.reconciliation.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class IdentityMatch {
	List<NormalizedRecord> left; List<NormalizedRecord> right;
	BigDecimal confidence; IdentityStatus status; List<MatchSignal> signals;
	public IdentityMatch(List<NormalizedRecord> left, List<NormalizedRecord> right, BigDecimal confidence,
			IdentityStatus status, List<MatchSignal> signals) {
		super();
		this.left = left;
		this.right = right;
		this.confidence = confidence;
		this.status = status;
		this.signals = signals;
	}
	public List<NormalizedRecord> getLeft() {
		return left;
	}
	public void setLeft(List<NormalizedRecord> left) {
		this.left = left;
	}
	public List<NormalizedRecord> getRight() {
		return right;
	}
	public void setRight(List<NormalizedRecord> right) {
		this.right = right;
	}
	public BigDecimal getConfidence() {
		return confidence;
	}
	public void setConfidence(BigDecimal confidence) {
		this.confidence = confidence;
	}
	public IdentityStatus getStatus() {
		return status;
	}
	public void setStatus(IdentityStatus status) {
		this.status = status;
	}
	public List<MatchSignal> getSignals() {
		return signals;
	}
	public void setSignals(List<MatchSignal> signals) {
		this.signals = signals;
	}
	@Override
	public int hashCode() {
		return Objects.hash(confidence, left, right, signals, status);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final IdentityMatch other = (IdentityMatch) obj;
		return Objects.equals(confidence, other.confidence) && Objects.equals(left, other.left)
				&& Objects.equals(right, other.right) && Objects.equals(signals, other.signals)
				&& status == other.status;
	}
	@Override
	public String toString() {
		return "IdentityMatch [left=" + left + ", right=" + right + ", confidence=" + confidence + ", status=" + status
				+ ", signals=" + signals + "]";
	}
}
