package ro.flexbiz.util.commons.reconciliation.model;

import java.math.BigDecimal;
import java.util.Objects;

public class MatchSignal {
	String fieldKey; BigDecimal score; String explanation;
	public MatchSignal(String fieldKey, BigDecimal score, String explanation) {
		super();
		this.fieldKey = fieldKey;
		this.score = score;
		this.explanation = explanation;
	}
	public String getFieldKey() {
		return fieldKey;
	}
	public void setFieldKey(String fieldKey) {
		this.fieldKey = fieldKey;
	}
	public BigDecimal getScore() {
		return score;
	}
	public void setScore(BigDecimal score) {
		this.score = score;
	}
	public String getExplanation() {
		return explanation;
	}
	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}
	@Override
	public int hashCode() {
		return Objects.hash(explanation, fieldKey, score);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final MatchSignal other = (MatchSignal) obj;
		return Objects.equals(explanation, other.explanation) && Objects.equals(fieldKey, other.fieldKey)
				&& Objects.equals(score, other.score);
	}
	@Override
	public String toString() {
		return "MatchSignal [fieldKey=" + fieldKey + ", score=" + score + ", explanation=" + explanation + "]";
	}
}
