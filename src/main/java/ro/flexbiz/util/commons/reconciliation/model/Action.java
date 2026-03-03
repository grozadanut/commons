package ro.flexbiz.util.commons.reconciliation.model;

import java.util.Objects;

import ro.flexbiz.util.commons.model.GenericValue;

public class Action {
	private ReconciliationResult result;
	private ActionType type;
	private Object endpoint;
	private GenericValue params;
	public Action(ReconciliationResult result, ActionType type, Object endpoint, GenericValue params) {
		super();
		this.result = result;
		this.type = type;
		this.endpoint = endpoint;
		this.params = params;
	}
	public ReconciliationResult getResult() {
		return result;
	}
	public void setResult(ReconciliationResult result) {
		this.result = result;
	}
	public ActionType getType() {
		return type;
	}
	public void setType(ActionType type) {
		this.type = type;
	}
	public Object getEndpoint() {
		return endpoint;
	}
	public void setEndpoint(Object endpoint) {
		this.endpoint = endpoint;
	}
	public GenericValue getParams() {
		return params;
	}
	public void setParams(GenericValue params) {
		this.params = params;
	}
	@Override
	public int hashCode() {
		return Objects.hash(endpoint, params, result, type);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Action other = (Action) obj;
		return Objects.equals(endpoint, other.endpoint) && Objects.equals(params, other.params)
				&& Objects.equals(result, other.result) && type == other.type;
	}
	@Override
	public String toString() {
		return "Action [result=" + result + ", type=" + type + ", endpoint=" + endpoint + ", params=" + params + "]";
	}
}
