package ro.flexbiz.util.commons.reconciliation.model;

import java.util.Objects;

import ro.flexbiz.util.commons.model.GenericValue;

public class NormalizedRecord {
	Object original; GenericValue fields;
	public NormalizedRecord(Object original, GenericValue fields) {
		super();
		this.original = original;
		this.fields = fields;
	}
	@Override
	public int hashCode() {
		return Objects.hash(fields, original);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final NormalizedRecord other = (NormalizedRecord) obj;
		return Objects.equals(fields, other.fields) && Objects.equals(original, other.original);
	}
	public Object getOriginal() {
		return original;
	}
	public void setOriginal(Object original) {
		this.original = original;
	}
	public GenericValue getFields() {
		return fields;
	}
	public void setFields(GenericValue fields) {
		this.fields = fields;
	}
	public GenericValue f() {
		return fields;
	}
	@Override
	public String toString() {
		return "NormalizedRecord [original=" + original + ", fields=" + fields + "]";
	}
}
