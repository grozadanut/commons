package ro.flexbiz.util.commons.reconciliation.components;

import ro.flexbiz.util.commons.reconciliation.ActionPolicy;
import ro.flexbiz.util.commons.reconciliation.model.Action;
import ro.flexbiz.util.commons.reconciliation.model.ActionType;
import ro.flexbiz.util.commons.reconciliation.model.ReconciliationResult;

public class IgnorePolicy implements ActionPolicy {
	@Override
	public Action resolve(ReconciliationResult result) {
		return new Action(result, ActionType.IGNORE, null, null);
	}
}
