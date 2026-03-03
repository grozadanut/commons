package ro.flexbiz.util.commons.reconciliation;

import static org.assertj.core.api.Assertions.assertThatList;

import java.util.List;
import java.util.stream.Collectors;

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
				List.of("Left only", "", "Match", "Start diff", "End diff yea", "Just a string"),
				List.of("", "Right only", "Match", "Small diff", "End difference yea", "Another value"))
				.stream()
				.map(Action::getResult)
				.collect(Collectors.toList());
		
		// expect
		assertThatList(result.stream()
				.map(ReconciliationResult::getMatch)
				.map(IdentityMatch::getStatus)
				.collect(Collectors.toList()))
		.containsOnly(IdentityStatus.CONFIRMED);
		
		assertThatList(result.stream()
				.map(ReconciliationResult::getStatus)
				.collect(Collectors.toList()))
		.containsExactlyInAnyOrder(ReconciliationStatus.MISMATCH, ReconciliationStatus.MISMATCH,
				ReconciliationStatus.RECONCILED, ReconciliationStatus.PARTIALLY_RECONCILED, ReconciliationStatus.PARTIALLY_RECONCILED,
				ReconciliationStatus.MISMATCH);
	}
	
	@Test
	public void leftOnlyMatch() {
		// given
		final List<ReconciliationResult> result = ReconciliationEngine.defaults().reconcile(
				List.of("Match", "Left only"),
				List.of("Match"))
				.stream()
				.map(Action::getResult)
				.collect(Collectors.toList());
		
		// expect
		assertThatList(result.stream()
				.map(ReconciliationResult::getMatch)
				.map(IdentityMatch::getStatus)
				.collect(Collectors.toList()))
		.containsExactlyInAnyOrder(IdentityStatus.CONFIRMED, IdentityStatus.NOT_FOUND);
		
		assertThatList(result.stream()
				.map(ReconciliationResult::getStatus)
				.collect(Collectors.toList()))
		.containsExactlyInAnyOrder(ReconciliationStatus.RECONCILED, ReconciliationStatus.LEFT_ONLY);
	}
	
	@Test
	public void rightOnlyMatch() {
		// given
		final List<ReconciliationResult> result = ReconciliationEngine.defaults().reconcile(
				List.of("Match"),
				List.of("Match", "Right only"))
				.stream()
				.map(Action::getResult)
				.collect(Collectors.toList());
		
		// expect
		assertThatList(result.stream()
				.map(ReconciliationResult::getMatch)
				.map(IdentityMatch::getStatus)
				.collect(Collectors.toList()))
		.containsExactlyInAnyOrder(IdentityStatus.CONFIRMED, IdentityStatus.NOT_FOUND);
		
		assertThatList(result.stream()
				.map(ReconciliationResult::getStatus)
				.collect(Collectors.toList()))
		.containsExactlyInAnyOrder(ReconciliationStatus.RECONCILED, ReconciliationStatus.RIGHT_ONLY);
	}
}
