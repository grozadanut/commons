package ro.flexbiz.util.commons;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.jupiter.api.Test;

public class HeterogeneousDataComparatorTest {
	@Test
	public void heterogenous_Test()
	{
		assertThat(HeterogeneousDataComparator.INSTANCE.compare("String", "String")).isEqualTo(0);
		assertThat(HeterogeneousDataComparator.INSTANCE.compare("String", "")).isGreaterThan(0);
		assertThat(HeterogeneousDataComparator.INSTANCE.compare(null, null)).isEqualTo(0);
		assertThat(HeterogeneousDataComparator.INSTANCE.compare(null, "String")).isEqualTo(1);
		assertThat(HeterogeneousDataComparator.INSTANCE.compare("String", null)).isEqualTo(-1);
		assertThat(HeterogeneousDataComparator.INSTANCE.compare("String", 1)).isGreaterThan(0);
		assertThat(HeterogeneousDataComparator.INSTANCE.compare(1, "String")).isLessThan(0);
		assertThat(HeterogeneousDataComparator.INSTANCE.compare(1, 1)).isEqualTo(0);
		assertThat(HeterogeneousDataComparator.INSTANCE.compare("A", "B")).isEqualTo(-1);
		assertThat(HeterogeneousDataComparator.INSTANCE.compare("1", 1)).isGreaterThan(0);
		assertThat(HeterogeneousDataComparator.INSTANCE.compare(new BigDecimal("1"), "1")).isEqualTo(1);
		assertThat(HeterogeneousDataComparator.INSTANCE.compare(new BigDecimal("1"), 1)).isEqualTo(1);
		assertThat(HeterogeneousDataComparator.INSTANCE.compare(Long.valueOf(1), Integer.valueOf(1))).isGreaterThan(0);
		assertThat(HeterogeneousDataComparator.INSTANCE.compare(new BigDecimal("1"), new BigInteger("1"))).isLessThan(0);
	}
}
