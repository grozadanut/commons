package ro.flexbiz.util.commons.reconciliation.model;

import ro.flexbiz.util.commons.model.GenericValue;

public record Action(ReconciliationResult result, ActionType type, Object endpoint, GenericValue params) {

}
