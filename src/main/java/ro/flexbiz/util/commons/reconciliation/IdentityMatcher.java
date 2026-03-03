package ro.flexbiz.util.commons.reconciliation;

import java.util.List;
import java.util.stream.Stream;

import ro.flexbiz.util.commons.reconciliation.model.IdentityMatch;
import ro.flexbiz.util.commons.reconciliation.model.Index;
import ro.flexbiz.util.commons.reconciliation.model.NormalizedRecord;

public interface IdentityMatcher {
	Stream<IdentityMatch> score(Index index, List<NormalizedRecord> left, List<NormalizedRecord> right);
}
