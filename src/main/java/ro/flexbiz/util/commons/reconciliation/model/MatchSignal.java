package ro.flexbiz.util.commons.reconciliation.model;

import java.math.BigDecimal;

public record MatchSignal(String fieldKey, BigDecimal score, String explanation) {

}
