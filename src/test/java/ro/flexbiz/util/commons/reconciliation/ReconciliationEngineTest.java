package ro.flexbiz.util.commons.reconciliation;

import static org.assertj.core.api.Assertions.assertThatList;

import java.util.List;

import org.junit.jupiter.api.Test;

import ro.flexbiz.util.commons.reconciliation.model.Action;
import ro.flexbiz.util.commons.reconciliation.model.IdentityMatch;
import ro.flexbiz.util.commons.reconciliation.model.IdentityStatus;
import ro.flexbiz.util.commons.reconciliation.model.ReconciliationResult;
import ro.flexbiz.util.commons.reconciliation.model.ReconciliationStatus;

public class ReconciliationEngineTest {
	@Test
	public void simpleCases() {
		// given
		final List<ReconciliationResult> result = ReconciliationEngine.defaults().reconcile(
				List.of("Left only", "", "Match", "Start diff", "End diff", "Just a string"),
				List.of("", "Right only", "Match", "Small diff", "End difference yea", "Another value"))
				.stream()
				.map(Action::result)
				.toList();
		
		// expect
		assertThatList(result.stream()
				.map(ReconciliationResult::match)
				.map(IdentityMatch::status)
				.toList())
		.containsOnly(IdentityStatus.CONFIRMED);
		
		assertThatList(result.stream()
				.map(ReconciliationResult::status)
				.toList())
		.containsExactlyInAnyOrder(ReconciliationStatus.LEFT_ONLY, ReconciliationStatus.RIGHT_ONLY,
				ReconciliationStatus.RECONCILED, ReconciliationStatus.PARTIALLY_RECONCILED, ReconciliationStatus.PARTIALLY_RECONCILED,
				ReconciliationStatus.MISMATCH);
	}
}
