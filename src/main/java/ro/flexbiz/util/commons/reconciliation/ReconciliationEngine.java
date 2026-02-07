package ro.flexbiz.util.commons.reconciliation;

import java.util.List;

import ro.flexbiz.util.commons.reconciliation.internal.ReconciliationEngineImpl;
import ro.flexbiz.util.commons.reconciliation.model.Action;

public interface ReconciliationEngine {
	ReconciliationEngine leftNormalizer(Normalizer normalizer);
	ReconciliationEngine rightNormalizer(Normalizer normalizer);
	ReconciliationEngine indexer(Indexer indexer);
	ReconciliationEngine matcher(IdentityMatcher matcher);
	ReconciliationEngine analyzer(ReconciliationAnalyzer analyzer);
	ReconciliationEngine actionPolicy(ActionPolicy policy);
	List<Action> reconcile(List<Object> left, List<Object> right);
	
	public static ReconciliationEngine defaults() {
        return new ReconciliationEngineImpl();
    }
}
