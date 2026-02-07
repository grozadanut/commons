package ro.flexbiz.util.commons.reconciliation.model;

import java.util.List;

public record ReconciliationResult(IdentityMatch match, ReconciliationStatus status, List<Discrepancy> discrepancies) {

}
