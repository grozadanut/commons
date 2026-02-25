package ro.flexbiz.util.commons.reconciliation;

import java.util.stream.Stream;

import ro.flexbiz.util.commons.reconciliation.model.NormalizedRecord;

public interface Normalizer {
	Stream<NormalizedRecord> normalize(Object original);
}
