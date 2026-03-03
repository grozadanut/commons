package ro.flexbiz.util.commons.reconciliation.model;

import java.util.Objects;

public class Discrepancy {
	String fieldKey; Object diff; DiscrepancyType type;
	public Discrepancy(String fieldKey, Object diff, DiscrepancyType type) {
		super();
		this.fieldKey = fieldKey;
		this.diff = diff;
		this.type = type;
	}
	public String getFieldKey() {
		return fieldKey;
	}
	public void setFieldKey(String fieldKey) {
		this.fieldKey = fieldKey;
	}
	public Object getDiff() {
		return diff;
	}
	public void setDiff(Object diff) {
		this.diff = diff;
	}
	public DiscrepancyType getType() {
		return type;
	}
	public void setType(DiscrepancyType type) {
		this.type = type;
	}
	@Override
	public int hashCode() {
		return Objects.hash(diff, fieldKey, type);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Discrepancy other = (Discrepancy) obj;
		return Objects.equals(diff, other.diff) && Objects.equals(fieldKey, other.fieldKey) && type == other.type;
	}
	@Override
	public String toString() {
		return "Discrepancy [fieldKey=" + fieldKey + ", diff=" + diff + ", type=" + type + "]";
	}
}
