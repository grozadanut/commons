package ro.flexbiz.util.commons.reconciliation.components;

import java.util.Set;

import ro.flexbiz.util.commons.reconciliation.Indexer;
import ro.flexbiz.util.commons.reconciliation.model.BlockKey;
import ro.flexbiz.util.commons.reconciliation.model.NormalizedRecord;

public class RowIndexIndexer implements Indexer {
	@Override
	public Set<BlockKey> index(NormalizedRecord record) {
		return Set.of(new BlockKey("rowIndex", record.fields().getLong("rowIndex")));
	}
}
