package ro.flexbiz.util.commons.reconciliation;

import java.util.List;

import ro.flexbiz.util.commons.reconciliation.model.IdentityMatch;
import ro.flexbiz.util.commons.reconciliation.model.NormalizedRecord;

public interface IdentityMatcher {
	IdentityMatch score(List<NormalizedRecord> left, List<NormalizedRecord> right);
}
