package ro.flexbiz.util.commons.reconciliation;

import ro.flexbiz.util.commons.reconciliation.model.Action;
import ro.flexbiz.util.commons.reconciliation.model.ReconciliationResult;

public interface ActionPolicy {
	/**
	 * If you want in this last step you can generate actions that need to be taken in order to fix 
	 * the reconciliation result. Note that these actions will NOT actually be performed. The client app 
	 * that reads the reconciliation result should be able to read the actions and run them if the user 
	 * demands it.
	 * 
	 * @param result the result of the reconciliation
	 * @return the action that needs to be taken for this result
	 */
	Action resolve(ReconciliationResult result);
}
