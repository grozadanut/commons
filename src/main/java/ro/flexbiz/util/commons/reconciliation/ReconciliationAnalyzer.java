package ro.flexbiz.util.commons.reconciliation;

import ro.flexbiz.util.commons.reconciliation.model.IdentityMatch;
import ro.flexbiz.util.commons.reconciliation.model.ReconciliationResult;

public interface ReconciliationAnalyzer {
	/**
	 * This is the reconciliation step. Here we reconcile the matched records.
	 * For example, for the following records:<br/>
	 * - left(type: OutgointPayment, date: 2026-03-01, partner: SUPPLIER INC, total: 100)<br/>
	 * - right(type: OutgointPayment, date: 2026-03-03, partner: SUPPLIER INC, total: 100)<br/>
	 * we could generate the following result:<br/>
	 * - ReconciliationResult(ReconciliationStatus.PARTIALLY_RECONCILED, Discrepancy("date", 2, DiscrepancyType.TOLERATED))
	 * 
	 * @param match the matching records to be reconciled
	 * @return the reconciliation result
	 */
	ReconciliationResult analyze(IdentityMatch match);
}
