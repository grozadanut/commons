package ro.flexbiz.util.commons.reconciliation.components;

import ro.flexbiz.util.commons.PresentationUtils;
import ro.flexbiz.util.commons.model.GenericValue;
import ro.flexbiz.util.commons.reconciliation.Normalizer;
import ro.flexbiz.util.commons.reconciliation.model.NormalizedRecord;

public class StringNormalizer implements Normalizer {
	private long rowIndex = 0;

	@Override
	public NormalizedRecord normalize(Object original) {
		return new NormalizedRecord(original, 
				GenericValue.of("row", PresentationUtils.safeString(original, Object::toString),
						"rowIndex", rowIndex++));
	}
}
