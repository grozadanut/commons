package ro.flexbiz.util.commons.reconciliation.components;

import java.util.stream.Stream;

import ro.flexbiz.util.commons.PresentationUtils;
import ro.flexbiz.util.commons.model.GenericValue;
import ro.flexbiz.util.commons.reconciliation.Normalizer;
import ro.flexbiz.util.commons.reconciliation.model.NormalizedRecord;

public class StringNormalizer implements Normalizer {
	private long rowIndex = 0;

	@Override
	public Stream<NormalizedRecord> normalize(Object original) {
		return Stream.of(new NormalizedRecord(original, 
				GenericValue.of("row", PresentationUtils.safeString(original, Object::toString),
						"rowIndex", rowIndex++)));
	}
}
