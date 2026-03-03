package ro.flexbiz.util.commons.reconciliation.model;

import java.util.List;
import java.util.Objects;

public class ReconciliationResult {
	IdentityMatch match; ReconciliationStatus status; List<Discrepancy> discrepancies;
	public ReconciliationResult(IdentityMatch match, ReconciliationStatus status, List<Discrepancy> discrepancies) {
		super();
		this.match = match;
		this.status = status;
		this.discrepancies = discrepancies;
	}
	public IdentityMatch getMatch() {
		return match;
	}
	public void setMatch(IdentityMatch match) {
		this.match = match;
	}
	public ReconciliationStatus getStatus() {
		return status;
	}
	public void setStatus(ReconciliationStatus status) {
		this.status = status;
	}
	public List<Discrepancy> getDiscrepancies() {
		return discrepancies;
	}
	public void setDiscrepancies(List<Discrepancy> discrepancies) {
		this.discrepancies = discrepancies;
	}
	@Override
	public int hashCode() {
		return Objects.hash(discrepancies, match, status);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ReconciliationResult other = (ReconciliationResult) obj;
		return Objects.equals(discrepancies, other.discrepancies) && Objects.equals(match, other.match)
				&& status == other.status;
	}
	@Override
	public String toString() {
		return "ReconciliationResult [match=" + match + ", status=" + status + ", discrepancies=" + discrepancies + "]";
	}
}
