package ro.flexbiz.util.commons.reconciliation;

import java.util.stream.Stream;

import ro.flexbiz.util.commons.reconciliation.model.NormalizedRecord;

public interface Normalizer {
	/**
	 * Converts the reconciliation input into a unified data model so we can compare left with right.
	 * 
	 * @param original the original input
	 * @return the unified record
	 */
	Stream<NormalizedRecord> normalize(Object original);
}
