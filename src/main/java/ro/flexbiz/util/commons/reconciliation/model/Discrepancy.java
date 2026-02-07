package ro.flexbiz.util.commons.reconciliation.model;

public record Discrepancy(String fieldKey, Object diff, DiscrepancyType type) {

}
