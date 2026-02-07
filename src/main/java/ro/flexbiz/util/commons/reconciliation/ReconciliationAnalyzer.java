package ro.flexbiz.util.commons.reconciliation;

import ro.flexbiz.util.commons.reconciliation.model.IdentityMatch;
import ro.flexbiz.util.commons.reconciliation.model.ReconciliationResult;

public interface ReconciliationAnalyzer {
	ReconciliationResult analyze(IdentityMatch match);
}
