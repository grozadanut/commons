package ro.flexbiz.util.commons.reconciliation.components;

import java.util.Set;

import ro.flexbiz.util.commons.reconciliation.Indexer;
import ro.flexbiz.util.commons.reconciliation.model.Index;
import ro.flexbiz.util.commons.reconciliation.model.NormalizedRecord;

public class RowIndexIndexer implements Indexer {
	@Override
	public Set<Index> index(NormalizedRecord nr) {
		return Set.of(new Index("rowIndex", nr.fields().getLong("rowIndex")));
	}
}
