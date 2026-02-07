package ro.flexbiz.util.commons.reconciliation.components;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import ro.flexbiz.util.commons.ListUtils;
import ro.flexbiz.util.commons.PresentationUtils;
import ro.flexbiz.util.commons.reconciliation.ReconciliationAnalyzer;
import ro.flexbiz.util.commons.reconciliation.model.IdentityMatch;
import ro.flexbiz.util.commons.reconciliation.model.ReconciliationResult;
import ro.flexbiz.util.commons.reconciliation.model.ReconciliationStatus;

public class StringDiffAnalyzer implements ReconciliationAnalyzer {
	@Override
	public ReconciliationResult analyze(IdentityMatch match) {
		switch (match.status()) {
		case CONFIRMED:
			final String ls = match.left().stream()
					.map(nr -> nr.fields().getString("row"))
					.collect(Collectors.joining(PresentationUtils.NEWLINE));
			final String rs = match.right().stream()
					.map(nr -> nr.fields().getString("row"))
					.collect(Collectors.joining(PresentationUtils.NEWLINE));
			if (ls.equalsIgnoreCase(rs))
				return new ReconciliationResult(match, ReconciliationStatus.RECONCILED, List.of());
			if (levenshteinDistance(ls, rs) <= (ls.length() + rs.length()) / 4)
				return new ReconciliationResult(match, ReconciliationStatus.PARTIALLY_RECONCILED, List.of());
			return new ReconciliationResult(match, ReconciliationStatus.MISMATCH, List.of());
		default:
			if (ListUtils.isEmpty(match.left()) && ListUtils.notEmpty(match.right()))
				return new ReconciliationResult(match, ReconciliationStatus.RIGHT_ONLY, List.of());
			else if (ListUtils.notEmpty(match.left()) && ListUtils.isEmpty(match.right()))
				return new ReconciliationResult(match, ReconciliationStatus.LEFT_ONLY, List.of());
			return new ReconciliationResult(match, ReconciliationStatus.MISMATCH, List.of());
		}
	}
	
	private static int levenshteinDistance(String x, String y) {
		final int[][] dp = new int[x.length() + 1][y.length() + 1];

		for (int i = 0; i <= x.length(); i++) {
			for (int j = 0; j <= y.length(); j++) {
				if (i == 0) {
					dp[i][j] = j;
				} else if (j == 0) {
					dp[i][j] = i;
				} else {
					dp[i][j] = min(dp[i - 1][j - 1] + costOfSubstitution(x.charAt(i - 1), y.charAt(j - 1)),
							dp[i - 1][j] + 1, dp[i][j - 1] + 1);
				}
			}
		}

		return dp[x.length()][y.length()];
	}

	private static int costOfSubstitution(char a, char b) {
		return a == b ? 0 : 1;
	}

	private static int min(int... numbers) {
		return Arrays.stream(numbers).min().orElse(Integer.MAX_VALUE);
	}
}
