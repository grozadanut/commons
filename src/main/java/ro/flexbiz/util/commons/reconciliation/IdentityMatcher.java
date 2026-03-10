package ro.flexbiz.util.commons.reconciliation;

import java.util.List;
import java.util.stream.Stream;

import ro.flexbiz.util.commons.reconciliation.model.IdentityMatch;
import ro.flexbiz.util.commons.reconciliation.model.Index;
import ro.flexbiz.util.commons.reconciliation.model.NormalizedRecord;

public interface IdentityMatcher {
	/**
	 * This step performs the actual matching between left and right records. Note that you can return 
	 * a confidence level for each matching and the records will be matched on the descending confidence level.
	 * <br/><br/>
	 * For example, for the following index and records:<br/>
	 * - Index(strategy: byPartnerAndDate, value: OutgointPayment|SUPPLIER INC|2026-03-01)<br/>
	 * - left_1(type: OutgointPayment, date: 2026-03-01, partner: SUPPLIER INC, total: 100)<br/>
	 * - left_2(type: OutgointPayment, date: 2026-03-01, partner: SUPPLIER INC, total: 50)<br/>
	 * - right_1(type: OutgointPayment, date: 2026-03-01, partner: SUPPLIER INC, total: 100)<br/>
	 * - right_2(type: OutgointPayment, date: 2026-03-01, partner: SUPPLIER INC, total: 150)<br/>
	 * we could generate the following matches:<br/>
	 * - IdentityMatch_1(left_1, right_1, confidence: 1, IdentityStatus.CONFIRMED, MatchSignal("byTotals", BigDecimal.ONE, "matched by total"))<br/>
	 * - IdentityMatch_2((left_1, left_2), right_2, confidence: 0.9, IdentityStatus.PROBABLE, MatchSignal("bySummingTotals", BigDecimal.ONE, "matched by summing totals"))<br/>
	 * 
	 * @param index the index that matched for both records
	 * @param left the left record/s
	 * @param right the right record/s
	 * @return the matches for these records, if any
	 */
	Stream<IdentityMatch> score(Index index, List<NormalizedRecord> left, List<NormalizedRecord> right);
}
