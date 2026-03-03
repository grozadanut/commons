package ro.flexbiz.util.commons.reconciliation.internal;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;

import ro.flexbiz.util.commons.ListUtils;
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
import ro.flexbiz.util.commons.reconciliation.model.IdentityMatch;
import ro.flexbiz.util.commons.reconciliation.model.IdentityStatus;
import ro.flexbiz.util.commons.reconciliation.model.Index;
import ro.flexbiz.util.commons.reconciliation.model.NormalizedRecord;
import ro.flexbiz.util.commons.reconciliation.model.ReconciliationResult;
import ro.flexbiz.util.commons.reconciliation.model.ReconciliationStatus;

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
	public <L, R> List<Action> reconcile(Collection<L> l, Collection<R> r) {
		final List<NormalizedRecord> left = l.stream().flatMap(leftNormalizer::normalize).collect(Collectors.toList());
		final List<NormalizedRecord> right = r.stream().flatMap(rightNormalizer::normalize).collect(Collectors.toList());
		final Map<Index, List<NormalizedRecord>> leftIndex = indexAll(left);
		final Map<Index, List<NormalizedRecord>> rightIndex = indexAll(right);
		
		final List<IdentityMatch> candidates = ListUtils.union(leftIndex.keySet(), rightIndex.keySet()).stream()
				.flatMap(key -> matcher.score(key, leftIndex.getOrDefault(key, List.of()), rightIndex.getOrDefault(key, List.of())))
				.sorted(Comparator.comparing(IdentityMatch::getConfidence).reversed())
				.collect(Collectors.toList());

		final List<NormalizedRecord> leftMatched = new ArrayList<>();
		final List<NormalizedRecord> rightMatched = new ArrayList<>();
		final List<IdentityMatch> matches = new ArrayList<>();
		
		candidates.stream()
		.filter(im -> Collections.disjoint(im.getLeft(), leftMatched) && Collections.disjoint(im.getRight(), rightMatched))
		.peek(im -> leftMatched.addAll(im.getLeft()))
		.peek(im -> rightMatched.addAll(im.getRight()))
		.forEach(matches::add);
		
		// add unmatched left&right to matches
		final List<NormalizedRecord> unmatchedLeft = left.stream()
				.filter(lnr -> !leftMatched.contains(lnr)).collect(Collectors.toList());
		if (!unmatchedLeft.isEmpty())
			matches.add(new IdentityMatch(unmatchedLeft, List.of(), BigDecimal.ZERO, IdentityStatus.NOT_FOUND, List.of()));
		
		final List<NormalizedRecord> unmatchedRight = right.stream()
				.filter(rnr -> !rightMatched.contains(rnr)).collect(Collectors.toList());
		if (!unmatchedRight.isEmpty())
			matches.add(new IdentityMatch(List.of(), unmatchedRight, BigDecimal.ZERO, IdentityStatus.NOT_FOUND, List.of()));

		return matches.stream().map(im -> {
			if (im.getStatus().equals(IdentityStatus.NOT_FOUND)) {
				if (ListUtils.notEmpty(im.getLeft()) && ListUtils.isEmpty(im.getRight()))
					return new ReconciliationResult(im, ReconciliationStatus.LEFT_ONLY, List.of());
				else if (ListUtils.isEmpty(im.getLeft()) && ListUtils.notEmpty(im.getRight()))
					return new ReconciliationResult(im, ReconciliationStatus.RIGHT_ONLY, List.of());
			}
			return analyzer.analyze(im);
		})
				.map(policy::resolve)
				.collect(Collectors.toList());
	}

	private Map<Index, List<NormalizedRecord>> indexAll(List<NormalizedRecord> records) {
		return records.stream()
				.flatMap(r -> indexer.index(r).stream().map(bk -> Map.entry(bk, r)))
				.collect(Collectors.groupingBy(Entry::getKey, Collectors.mapping(Entry::getValue, Collectors.toList())));
	}
}
