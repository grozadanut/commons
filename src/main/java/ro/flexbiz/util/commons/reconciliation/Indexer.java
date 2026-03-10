package ro.flexbiz.util.commons.reconciliation;

import java.util.Set;

import ro.flexbiz.util.commons.reconciliation.model.Index;
import ro.flexbiz.util.commons.reconciliation.model.NormalizedRecord;

public interface Indexer {
	/**
	 * This step generates candidate pairs that will be compared in the next step.
	 * <br/><br/>
	 * For example, for the following record:<br/>
	 * - type: OutgointPayment, date: 2026-03-01, partner: SUPPLIER INC, total: 200<br/>
	 * we could generate the following indexes:<br/>
	 * - strategy: byPartnerAndDate, value: OutgointPayment|SUPPLIER INC|2026-03-01<br/>
	 * - strategy: byTotal, value: OutgointPayment|200
	 * 
	 * @param record the record to index
	 * @return the indexes that will be used to generate candidate pairs that will be compared for matching
	 */
	Set<Index> index(NormalizedRecord record);
}
