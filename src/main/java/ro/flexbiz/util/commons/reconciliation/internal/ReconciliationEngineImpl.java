package ro.flexbiz.util.commons.reconciliation.internal;

import java.util.List;
import java.util.Objects;

import ro.flexbiz.util.commons.reconciliation.ActionPolicy;
import ro.flexbiz.util.commons.reconciliation.IdentityMatcher;
import ro.flexbiz.util.commons.reconciliation.Indexer;
import ro.flexbiz.util.commons.reconciliation.Normalizer;
import ro.flexbiz.util.commons.reconciliation.ReconciliationAnalyzer;
import ro.flexbiz.util.commons.reconciliation.ReconciliationEngine;
import ro.flexbiz.util.commons.reconciliation.components.IgnorePolicy;
import ro.flexbiz.util.commons.reconciliation.components.RowIndexIndexer;
import ro.flexbiz.util.commons.reconciliation.components.RowIndexMatcher;
import ro.flexbiz.util.commons.reconciliation.components.StringDiffAnalyzer;
import ro.flexbiz.util.commons.reconciliation.components.StringNormalizer;
import ro.flexbiz.util.commons.reconciliation.model.Action;

public class ReconciliationEngineImpl implements ReconciliationEngine {
	private Normalizer leftNormalizer;
	private Normalizer rightNormalizer;
	private Indexer indexer;
	private IdentityMatcher matcher;
	private ReconciliationAnalyzer analyzer;
	private ActionPolicy policy;

	public ReconciliationEngineImpl() {
		this.leftNormalizer = new StringNormalizer();
		this.rightNormalizer = new StringNormalizer();
		this.indexer = new RowIndexIndexer();
		this.matcher = new RowIndexMatcher();
		this.analyzer = new StringDiffAnalyzer();
		this.policy = new IgnorePolicy();
	}

	@Override
	public ReconciliationEngine leftNormalizer(Normalizer normalizer) {
		this.leftNormalizer = Objects.requireNonNull(normalizer);
		return this;
	}

	@Override
	public ReconciliationEngine rightNormalizer(Normalizer normalizer) {
		this.rightNormalizer = Objects.requireNonNull(normalizer);
		return this;
	}

	@Override
	public ReconciliationEngine indexer(Indexer indexer) {
		this.indexer = Objects.requireNonNull(indexer);
		return this;
	}

	@Override
	public ReconciliationEngine matcher(IdentityMatcher matcher) {
		this.matcher = Objects.requireNonNull(matcher);
		return this;
	}

	@Override
	public ReconciliationEngine analyzer(ReconciliationAnalyzer analyzer) {
		this.analyzer = Objects.requireNonNull(analyzer);
		return this;
	}

	@Override
	public ReconciliationEngine actionPolicy(ActionPolicy policy) {
		this.policy = Objects.requireNonNull(policy);
		return this;
	}

	@Override
	public List<Action> reconcile(List<Object> left, List<Object> right) {
		// TODO Auto-generated method stub
		return null;
	}
}
