package ro.flexbiz.util.commons.reconciliation;

import java.util.Collection;
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
	<L, R> List<Action> reconcile(Collection<L> left, Collection<R> right);
	
	public static ReconciliationEngine defaults() {
        return new ReconciliationEngineImpl();
    }
}
