package ro.flexbiz.util.commons.reconciliation;

import ro.flexbiz.util.commons.reconciliation.model.Action;
import ro.flexbiz.util.commons.reconciliation.model.ReconciliationResult;

public interface ActionPolicy {
	Action resolve(ReconciliationResult result);
}
