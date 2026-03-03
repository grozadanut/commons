package ro.flexbiz.util.commons.reconciliation.model;

import java.util.Objects;

public class Index {
	String strategy; Object value;
	public Index(String strategy, Object value) {
		super();
		this.strategy = strategy;
		this.value = value;
	}
	public String getStrategy() {
		return strategy;
	}
	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	@Override
	public int hashCode() {
		return Objects.hash(strategy, value);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Index other = (Index) obj;
		return Objects.equals(strategy, other.strategy) && Objects.equals(value, other.value);
	}
	@Override
	public String toString() {
		return "Index [strategy=" + strategy + ", value=" + value + "]";
	}
}
