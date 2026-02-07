package ro.flexbiz.util.commons.reconciliation;

import java.util.Set;

import ro.flexbiz.util.commons.reconciliation.model.BlockKey;
import ro.flexbiz.util.commons.reconciliation.model.NormalizedRecord;

public interface Indexer {
	Set<BlockKey> index(NormalizedRecord record);
}
