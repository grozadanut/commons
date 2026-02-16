package ro.flexbiz.util.commons.reconciliation;

import java.util.Set;

import ro.flexbiz.util.commons.reconciliation.model.Index;
import ro.flexbiz.util.commons.reconciliation.model.NormalizedRecord;

public interface Indexer {
	Set<Index> index(NormalizedRecord record);
}
