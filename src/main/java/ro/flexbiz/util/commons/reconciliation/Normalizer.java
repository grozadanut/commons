package ro.flexbiz.util.commons.reconciliation;

import ro.flexbiz.util.commons.reconciliation.model.NormalizedRecord;

public interface Normalizer {
	NormalizedRecord normalize(Object original);
}
