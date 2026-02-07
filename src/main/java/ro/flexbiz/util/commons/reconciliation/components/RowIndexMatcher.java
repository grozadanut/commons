package ro.flexbiz.util.commons.reconciliation.components;

import java.math.BigDecimal;
import java.util.List;

import ro.flexbiz.util.commons.reconciliation.IdentityMatcher;
import ro.flexbiz.util.commons.reconciliation.model.IdentityMatch;
import ro.flexbiz.util.commons.reconciliation.model.IdentityStatus;
import ro.flexbiz.util.commons.reconciliation.model.MatchSignal;
import ro.flexbiz.util.commons.reconciliation.model.NormalizedRecord;

public class RowIndexMatcher implements IdentityMatcher {
	@Override
	public IdentityMatch score(List<NormalizedRecord> left, List<NormalizedRecord> right) {
		final long ls = left.stream()
				.map(nr -> nr.fields().getLong("rowIndex"))
				.reduce(Math::addExact)
				.orElse(0L);
		final long rs = right.stream()
				.map(nr -> nr.fields().getLong("rowIndex"))
				.reduce(Math::addExact)
				.orElse(0L);
		final boolean equal = ls == rs;
		return new IdentityMatch(left, right, equal ? BigDecimal.ONE : BigDecimal.ZERO,
				equal ? IdentityStatus.CONFIRMED : IdentityStatus.NOT_FOUND,
						List.of(new MatchSignal("rowIndex", new BigDecimal(rs - ls), "Matching by line numbers")));
	}
}
