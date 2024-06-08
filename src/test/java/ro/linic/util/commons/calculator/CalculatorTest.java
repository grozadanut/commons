package ro.linic.util.commons.calculator;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.jupiter.api.Test;

public class CalculatorTest {
	@Test
	public void parse_Test(){
		assertThat(Calculator.parse(null)).isEqualByComparingTo(new BigDecimal("0"));
		assertThat(Calculator.parse("s")).isEqualByComparingTo(new BigDecimal("0"));
		assertThat(Calculator.parse("adfa123")).isEqualByComparingTo(new BigDecimal("0"));
		assertThat(Calculator.parse("")).isEqualByComparingTo(new BigDecimal("0"));
		assertThat(Calculator.parse("0")).isEqualByComparingTo(new BigDecimal("0"));
		assertThat(Calculator.parse(".1")).isEqualByComparingTo(new BigDecimal("0.1"));
		assertThat(Calculator.parse("0.1")).isEqualByComparingTo(new BigDecimal("0.1"));
		assertThat(Calculator.parse("1")).isEqualByComparingTo(new BigDecimal("1"));
		assertThat(Calculator.parse("   1   ")).isEqualByComparingTo(new BigDecimal("1"));
		assertThat(Calculator.parse("1")).isEqualByComparingTo(new BigDecimal("1"));
		assertThat(Calculator.parse("1+1")).isEqualByComparingTo(new BigDecimal("2"));
		assertThat(Calculator.parse("1+.1")).isEqualByComparingTo(new BigDecimal("1.1"));
		assertThat(Calculator.parse("1 + 1")).isEqualByComparingTo(new BigDecimal("2"));
		assertThat(Calculator.parse("10+12")).isEqualByComparingTo(new BigDecimal("22"));
		assertThat(Calculator.parse("10.25+1.2")).isEqualByComparingTo(new BigDecimal("11.45"));
		assertThat(Calculator.parse("1+1+2+3.5")).isEqualByComparingTo(new BigDecimal("7.5"));
		
		assertThat(Calculator.parse("3-1")).isEqualByComparingTo(new BigDecimal("2"));
		assertThat(Calculator.parse("10-2-3.5")).isEqualByComparingTo(new BigDecimal("4.5"));
		assertThat(Calculator.parse("10+1-2")).isEqualByComparingTo(new BigDecimal("9"));
		
		assertThat(Calculator.parse("2*2")).isEqualByComparingTo(new BigDecimal("4"));
		assertThat(Calculator.parse("2*3.5")).isEqualByComparingTo(new BigDecimal("7"));
		assertThat(Calculator.parse("2 3")).isEqualByComparingTo(new BigDecimal("6"));
		assertThat(Calculator.parse("2*(3)")).isEqualByComparingTo(new BigDecimal("6"));
		assertThat(Calculator.parse("2*(3+1-2)")).isEqualByComparingTo(new BigDecimal("4"));
		assertThat(Calculator.parse("2/2")).isEqualByComparingTo(new BigDecimal("1"));
		assertThat(Calculator.parse("8/2")).isEqualByComparingTo(new BigDecimal("4"));
		assertThat(Calculator.parse("7/3").setScale(16, RoundingMode.HALF_EVEN)).isEqualByComparingTo(new BigDecimal("2.3333333333333333"));
		assertThat(Calculator.parse("2*3+3/3")).isEqualByComparingTo(new BigDecimal("7"));
		assertThat(Calculator.parse("2*3+8/4-.1")).isEqualByComparingTo(new BigDecimal("7.9"));
		assertThat(Calculator.parse("2*(3+((4*4)/2))")).isEqualByComparingTo(new BigDecimal("22"));
		
		assertThat(Calculator.parse("2*-2")).isEqualByComparingTo(new BigDecimal("-4"));
		assertThat(Calculator.parse("-2*-2")).isEqualByComparingTo(new BigDecimal("4"));
		assertThat(Calculator.parse("2*-3.5")).isEqualByComparingTo(new BigDecimal("-7"));
		assertThat(Calculator.parse("-2*3.5")).isEqualByComparingTo(new BigDecimal("-7"));
		assertThat(Calculator.parse("2*(-3)")).isEqualByComparingTo(new BigDecimal("-6"));
		assertThat(Calculator.parse("-2*(3+1-2)")).isEqualByComparingTo(new BigDecimal("-4"));
		assertThat(Calculator.parse("2/-2")).isEqualByComparingTo(new BigDecimal("-1"));
		assertThat(Calculator.parse("-8/2")).isEqualByComparingTo(new BigDecimal("-4"));
		assertThat(Calculator.parse("2*3-3/3")).isEqualByComparingTo(new BigDecimal("5"));
		assertThat(Calculator.parse("2*3-8/4-.1")).isEqualByComparingTo(new BigDecimal("3.9"));
		assertThat(Calculator.parse("2*(3-((4*4)/2))")).isEqualByComparingTo(new BigDecimal("-10"));
	}
	
	@Test
	public void parseSpace_Test(){
		assertThat(Calculator.parse(null)).isEqualByComparingTo(new BigDecimal("0"));
		assertThat(Calculator.parse(" s ")).isEqualByComparingTo(new BigDecimal("0"));
		assertThat(Calculator.parse("  adfa  123")).isEqualByComparingTo(new BigDecimal("0"));
		assertThat(Calculator.parse("")).isEqualByComparingTo(new BigDecimal("0"));
		assertThat(Calculator.parse(" 0  ")).isEqualByComparingTo(new BigDecimal("0"));
		assertThat(Calculator.parse("  .1  ")).isEqualByComparingTo(new BigDecimal("0.1"));
		assertThat(Calculator.parse(" 0000.1000  ")).isEqualByComparingTo(new BigDecimal("0.1"));
		assertThat(Calculator.parse(" 1000000  ")).isEqualByComparingTo(new BigDecimal("1000000"));
		assertThat(Calculator.parse(" 1 + 1 ")).isEqualByComparingTo(new BigDecimal("2"));
		assertThat(Calculator.parse(" 1 + .1 ")).isEqualByComparingTo(new BigDecimal("1.1"));
		assertThat(Calculator.parse(" 10000.25 + 1.2 ")).isEqualByComparingTo(new BigDecimal("10001.45"));
		assertThat(Calculator.parse(" 1 + 1 + 2 + 3.5  ")).isEqualByComparingTo(new BigDecimal("7.5"));
		
		assertThat(Calculator.parse("3 - 1")).isEqualByComparingTo(new BigDecimal("2"));
		assertThat(Calculator.parse("  10  -  2  -  3.5 ")).isEqualByComparingTo(new BigDecimal("4.5"));
		
		assertThat(Calculator.parse(" 2 * 2 ")).isEqualByComparingTo(new BigDecimal("4"));
		assertThat(Calculator.parse(" 2    *    3.5 ")).isEqualByComparingTo(new BigDecimal("7"));
		assertThat(Calculator.parse("  2    3  ")).isEqualByComparingTo(new BigDecimal("6"));
		assertThat(Calculator.parse(" 2 * ( 3 ) ")).isEqualByComparingTo(new BigDecimal("6"));
		assertThat(Calculator.parse("2 * ( 3 + 1 -  2 )")).isEqualByComparingTo(new BigDecimal("4"));
		assertThat(Calculator.parse(" 2 / 2 ")).isEqualByComparingTo(new BigDecimal("1"));
		assertThat(Calculator.parse(" 8 / 2 ")).isEqualByComparingTo(new BigDecimal("4"));
		assertThat(Calculator.parse(" 7 / 3 ").setScale(16, RoundingMode.HALF_EVEN)).isEqualByComparingTo(new BigDecimal("2.3333333333333333"));
		assertThat(Calculator.parse(" 2 * 3 + 3 / 3 ")).isEqualByComparingTo(new BigDecimal("7"));
		assertThat(Calculator.parse(" 2 * 3 + 8 / 4 - .1 ")).isEqualByComparingTo(new BigDecimal("7.9"));
		assertThat(Calculator.parse(" 2 * ( 3 + ( ( 4 * 4 ) / 2 ) ) ")).isEqualByComparingTo(new BigDecimal("22"));
		
		assertThat(Calculator.parse(" 2 * - 2 ")).isEqualByComparingTo(new BigDecimal("-4"));
		assertThat(Calculator.parse(" - 2  *   -  2")).isEqualByComparingTo(new BigDecimal("4"));
		assertThat(Calculator.parse(" 2 * -3.5 ")).isEqualByComparingTo(new BigDecimal("-7"));
		assertThat(Calculator.parse(" - 2 * 3.5 ")).isEqualByComparingTo(new BigDecimal("-7"));
		assertThat(Calculator.parse(" 2  * ( -3 ) ")).isEqualByComparingTo(new BigDecimal("-6"));
		assertThat(Calculator.parse(" -2 * ( 3 + 1 - 2)")).isEqualByComparingTo(new BigDecimal("-4"));
		assertThat(Calculator.parse(" 2 / -2 ")).isEqualByComparingTo(new BigDecimal("-1"));
		assertThat(Calculator.parse(" -8 / 2 ")).isEqualByComparingTo(new BigDecimal("-4"));
		assertThat(Calculator.parse(" 2 * 3 - 3 / 3")).isEqualByComparingTo(new BigDecimal("5"));
		assertThat(Calculator.parse(" 2 * 3 - 8 / 4 - .1")).isEqualByComparingTo(new BigDecimal("3.9"));
		assertThat(Calculator.parse(" 2 * ( 3 - ( ( 4 * 4 ) / 2 ) ) ")).isEqualByComparingTo(new BigDecimal("-10"));
	}
}
