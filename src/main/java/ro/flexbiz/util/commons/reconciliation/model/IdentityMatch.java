package ro.flexbiz.util.commons.reconciliation.model;

import java.math.BigDecimal;
import java.util.List;

public record IdentityMatch(List<NormalizedRecord> left, List<NormalizedRecord> right,
		BigDecimal confidence, IdentityStatus status, List<MatchSignal> signals) {
	
}
