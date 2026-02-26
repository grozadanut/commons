package ro.flexbiz.util.commons;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;

import org.junit.jupiter.api.Test;

public class PresentationUtilsTest {
	@Test
	public void displayBigDecimal_US_Tests() {
		Locale.setDefault(Locale.US);
		assertThat(PresentationUtils.displayBigDecimal(new BigDecimal("4"))).isEqualTo("4");
		assertThat(PresentationUtils.displayBigDecimal(new BigDecimal("4.2"))).isEqualTo("4.2");
		assertThat(PresentationUtils.displayBigDecimal(new BigDecimal("4.22"))).isEqualTo("4.22");
		assertThat(PresentationUtils.displayBigDecimal(new BigDecimal("-4"))).isEqualTo("-4");
		assertThat(PresentationUtils.displayBigDecimal(new BigDecimal("-4.2"))).isEqualTo("-4.2");
		assertThat(PresentationUtils.displayBigDecimal(new BigDecimal("-4.22"))).isEqualTo("-4.22");
		assertThat(PresentationUtils.displayBigDecimal(new BigDecimal("110998432.512362"))).isEqualTo("110 998 432.51");
		assertThat(PresentationUtils.displayBigDecimal(new BigDecimal("110998432.518523"))).isEqualTo("110 998 432.52");
	}
	
	@Test
	public void displayBigDecimalWithScale_US_Tests() {
		Locale.setDefault(Locale.US);
		assertThat(PresentationUtils.displayBigDecimal(new BigDecimal("4"), 2, RoundingMode.HALF_EVEN)).isEqualTo("4.00");
		assertThat(PresentationUtils.displayBigDecimal(new BigDecimal("4.2"), 2, RoundingMode.HALF_EVEN)).isEqualTo("4.20");
		assertThat(PresentationUtils.displayBigDecimal(new BigDecimal("4.22"), 2, RoundingMode.HALF_EVEN)).isEqualTo("4.22");
		assertThat(PresentationUtils.displayBigDecimal(new BigDecimal("-4"), 2, RoundingMode.HALF_EVEN)).isEqualTo("-4.00");
		assertThat(PresentationUtils.displayBigDecimal(new BigDecimal("-4.2"), 2, RoundingMode.HALF_EVEN)).isEqualTo("-4.20");
		assertThat(PresentationUtils.displayBigDecimal(new BigDecimal("-4.22"), 6, RoundingMode.HALF_EVEN)).isEqualTo("-4.220000");
		assertThat(PresentationUtils.displayBigDecimal(new BigDecimal("110998432.412362"), 0, RoundingMode.HALF_EVEN)).isEqualTo("110 998 432");
		assertThat(PresentationUtils.displayBigDecimal(new BigDecimal("110998432.512362"), 2, RoundingMode.HALF_EVEN)).isEqualTo("110 998 432.51");
		assertThat(PresentationUtils.displayBigDecimal(new BigDecimal("110998432.512362"), 6, RoundingMode.HALF_EVEN)).isEqualTo("110 998 432.512362");
		assertThat(PresentationUtils.displayBigDecimal(new BigDecimal("110998432.568523"), 0, RoundingMode.HALF_EVEN)).isEqualTo("110 998 433");
		assertThat(PresentationUtils.displayBigDecimal(new BigDecimal("110998432.518523"), 2, RoundingMode.HALF_EVEN)).isEqualTo("110 998 432.52");
		assertThat(PresentationUtils.displayBigDecimal(new BigDecimal("110998432.518523"), 6, RoundingMode.HALF_EVEN)).isEqualTo("110 998 432.518523");
	}
	
	@Test
	public void displayBigDecimal_RO_Tests() {
		Locale.setDefault(new Locale("ro"));
		assertThat(PresentationUtils.displayBigDecimal(new BigDecimal("4"))).isEqualTo("4");
		assertThat(PresentationUtils.displayBigDecimal(new BigDecimal("4.2"))).isEqualTo("4.2");
		assertThat(PresentationUtils.displayBigDecimal(new BigDecimal("4.22"))).isEqualTo("4.22");
		assertThat(PresentationUtils.displayBigDecimal(new BigDecimal("-4"))).isEqualTo("-4");
		assertThat(PresentationUtils.displayBigDecimal(new BigDecimal("-4.2"))).isEqualTo("-4.2");
		assertThat(PresentationUtils.displayBigDecimal(new BigDecimal("-4.22"))).isEqualTo("-4.22");
		assertThat(PresentationUtils.displayBigDecimal(new BigDecimal("110998432.512362"))).isEqualTo("110 998 432.51");
		assertThat(PresentationUtils.displayBigDecimal(new BigDecimal("110998432.512362"), 2, RoundingMode.HALF_EVEN)).isEqualTo("110 998 432.51");
		assertThat(PresentationUtils.displayBigDecimal(new BigDecimal("110998432.512362"), 6, RoundingMode.HALF_EVEN)).isEqualTo("110 998 432.512362");
		assertThat(PresentationUtils.displayBigDecimal(new BigDecimal("110998432.518523"))).isEqualTo("110 998 432.52");
		assertThat(PresentationUtils.displayBigDecimal(new BigDecimal("110998432.518523"), 2, RoundingMode.HALF_EVEN)).isEqualTo("110 998 432.52");
		assertThat(PresentationUtils.displayBigDecimal(new BigDecimal("110998432.518523"), 6, RoundingMode.HALF_EVEN)).isEqualTo("110 998 432.518523");
	}
	
	@Test
	public void displayBigDecimalWithScale_RO_Tests() {
		Locale.setDefault(new Locale("ro"));
		assertThat(PresentationUtils.displayBigDecimal(new BigDecimal("4"), 2, RoundingMode.HALF_EVEN)).isEqualTo("4.00");
		assertThat(PresentationUtils.displayBigDecimal(new BigDecimal("4.2"), 2, RoundingMode.HALF_EVEN)).isEqualTo("4.20");
		assertThat(PresentationUtils.displayBigDecimal(new BigDecimal("4.22"), 2, RoundingMode.HALF_EVEN)).isEqualTo("4.22");
		assertThat(PresentationUtils.displayBigDecimal(new BigDecimal("-4"), 2, RoundingMode.HALF_EVEN)).isEqualTo("-4.00");
		assertThat(PresentationUtils.displayBigDecimal(new BigDecimal("-4.2"), 2, RoundingMode.HALF_EVEN)).isEqualTo("-4.20");
		assertThat(PresentationUtils.displayBigDecimal(new BigDecimal("-4.22"), 6, RoundingMode.HALF_EVEN)).isEqualTo("-4.220000");
		assertThat(PresentationUtils.displayBigDecimal(new BigDecimal("110998432.412362"), 0, RoundingMode.HALF_EVEN)).isEqualTo("110 998 432");
		assertThat(PresentationUtils.displayBigDecimal(new BigDecimal("110998432.512362"), 2, RoundingMode.HALF_EVEN)).isEqualTo("110 998 432.51");
		assertThat(PresentationUtils.displayBigDecimal(new BigDecimal("110998432.512362"), 6, RoundingMode.HALF_EVEN)).isEqualTo("110 998 432.512362");
		assertThat(PresentationUtils.displayBigDecimal(new BigDecimal("110998432.568523"), 0, RoundingMode.HALF_EVEN)).isEqualTo("110 998 433");
		assertThat(PresentationUtils.displayBigDecimal(new BigDecimal("110998432.518523"), 2, RoundingMode.HALF_EVEN)).isEqualTo("110 998 432.52");
		assertThat(PresentationUtils.displayBigDecimal(new BigDecimal("110998432.518523"), 6, RoundingMode.HALF_EVEN)).isEqualTo("110 998 432.518523");
	}
}
