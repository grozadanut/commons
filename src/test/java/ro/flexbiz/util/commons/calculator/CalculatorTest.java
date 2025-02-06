package ro.flexbiz.util.commons.calculator;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.jupiter.api.Test;

public class CalculatorTest {
	@Test
	public void parse_Test() {
		assertThat(Calculator.parse(null)).isEqualByComparingTo(new BigDecimal("0"));
		assertThat(Calculator.parse("s")).isEqualByComparingTo(new BigDecimal("0"));
		assertThat(Calculator.parse("adfa123")).isEqualByComparingTo(new BigDecimal("0"));
		assertThat(Calculator.parse("")).isEqualByComparingTo(new BigDecimal("0"));
		assertThat(Calculator.parse("0")).isEqualByComparingTo(new BigDecimal("0"));
		assertThat(Calculator.parse(".1")).isEqualByComparingTo(new BigDecimal("0.1"));
		assertThat(Calculator.parse("00.100")).isEqualByComparingTo(new BigDecimal("0.1"));
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
		assertThat(Calculator.parse("2 3")).isEqualByComparingTo(new BigDecimal("23"));
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
	public void parseImplicit_Test() {
		// NOT SUPPORTED
		assertThat(Calculator.parse("2 3")).isEqualByComparingTo(new BigDecimal("23"));
		assertThat(Calculator.parse("2(3)")).isEqualByComparingTo(BigDecimal.ZERO/* new BigDecimal("6") */);
		assertThat(Calculator.parse("2(3+1-2)")).isEqualByComparingTo(BigDecimal.ZERO/* new BigDecimal("4") */);
		assertThat(Calculator.parse("2(3+((4*4)/2))")).isEqualByComparingTo(BigDecimal.ZERO/* new BigDecimal("22") */);
		assertThat(Calculator.parse("2(-3)")).isEqualByComparingTo(BigDecimal.ZERO/* new BigDecimal("-6") */);
		assertThat(Calculator.parse("-2(3+1-2)")).isEqualByComparingTo(BigDecimal.ZERO/* new BigDecimal("-4") */);
		assertThat(Calculator.parse("2(3-((4*4)/2))")).isEqualByComparingTo(BigDecimal.ZERO/* new BigDecimal("-10") */);
	}
	
	@Test
	public void parseSpace_Test() {
		assertThat(Calculator.parse(null)).isEqualByComparingTo(new BigDecimal("0"));
		assertThat(Calculator.parse(" s ")).isEqualByComparingTo(new BigDecimal("0"));
		assertThat(Calculator.parse("  adfa  123")).isEqualByComparingTo(new BigDecimal("0"));
		assertThat(Calculator.parse("")).isEqualByComparingTo(new BigDecimal("0"));
		assertThat(Calculator.parse(" 0  ")).isEqualByComparingTo(new BigDecimal("0"));
		assertThat(Calculator.parse("  .1  ")).isEqualByComparingTo(new BigDecimal("0.1"));
		assertThat(Calculator.parse("  0 00.100 0  ")).isEqualByComparingTo(new BigDecimal("0.1"));
		assertThat(Calculator.parse(" 1 000 000  ")).isEqualByComparingTo(new BigDecimal("1000000"));
		assertThat(Calculator.parse(" 1 + 1 ")).isEqualByComparingTo(new BigDecimal("2"));
		assertThat(Calculator.parse(" 1 + .1 ")).isEqualByComparingTo(new BigDecimal("1.1"));
		assertThat(Calculator.parse(" 10 00 0.25 + 1.2 ")).isEqualByComparingTo(new BigDecimal("10001.45"));
		assertThat(Calculator.parse(" 1 + 1 + 2 + 3.5  ")).isEqualByComparingTo(new BigDecimal("7.5"));
		
		assertThat(Calculator.parse("3 - 1")).isEqualByComparingTo(new BigDecimal("2"));
		assertThat(Calculator.parse("  10  -  2  -  3.5 ")).isEqualByComparingTo(new BigDecimal("4.5"));
		
		assertThat(Calculator.parse(" 2 * 2 ")).isEqualByComparingTo(new BigDecimal("4"));
		assertThat(Calculator.parse(" 2    *    3.5 ")).isEqualByComparingTo(new BigDecimal("7"));
		assertThat(Calculator.parse("  2    3  ")).isEqualByComparingTo(new BigDecimal("23"));
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
	
	@Test
	public void parsePercentage_Test() {
		assertThat(Calculator.parse("10%")).isEqualByComparingTo(new BigDecimal("0.1"));
		assertThat(Calculator.parse(" 22 % ")).isEqualByComparingTo(new BigDecimal("0.22"));
		assertThat(Calculator.parse("10+10%")).isEqualByComparingTo(new BigDecimal("11"));
		assertThat(Calculator.parse("10 + 10%")).isEqualByComparingTo(new BigDecimal("11"));
		assertThat(Calculator.parse("10 * 10%")).isEqualByComparingTo(new BigDecimal("1"));
		assertThat(Calculator.parse("10 + 5%")).isEqualByComparingTo(new BigDecimal("10.5"));
		assertThat(Calculator.parse("10 * 5%")).isEqualByComparingTo(new BigDecimal("0.5"));
		assertThat(Calculator.parse("10 - 10%")).isEqualByComparingTo(new BigDecimal("9"));
		assertThat(Calculator.parse("10 - 5%")).isEqualByComparingTo(new BigDecimal("9.5"));
		assertThat(Calculator.parse("119 + 22%")).isEqualByComparingTo(new BigDecimal("145.18"));
		assertThat(Calculator.parse("119 - 22%")).isEqualByComparingTo(new BigDecimal("92.82"));
		assertThat(Calculator.parse("119 * 22%")).isEqualByComparingTo(new BigDecimal("26.18"));
		assertThat(Calculator.parse("119 / 22%").setScale(7, RoundingMode.HALF_EVEN)).isEqualByComparingTo(new BigDecimal("540.9090909"));
		
		assertThat(Calculator.parse("10%+10")).isEqualByComparingTo(new BigDecimal("10.1"));
		assertThat(Calculator.parse("10% + 10")).isEqualByComparingTo(new BigDecimal("10.1"));
		assertThat(Calculator.parse("10% * 10")).isEqualByComparingTo(new BigDecimal("1"));
		assertThat(Calculator.parse("10% + 5")).isEqualByComparingTo(new BigDecimal("5.1"));
		assertThat(Calculator.parse("10% * 5")).isEqualByComparingTo(new BigDecimal("0.5"));
		assertThat(Calculator.parse("10% - 10")).isEqualByComparingTo(new BigDecimal("-9.9"));
		assertThat(Calculator.parse("10% - 5")).isEqualByComparingTo(new BigDecimal("-4.9"));
		assertThat(Calculator.parse("119% + 22")).isEqualByComparingTo(new BigDecimal("23.19"));
		assertThat(Calculator.parse("119% - 22")).isEqualByComparingTo(new BigDecimal("-20.81"));
		assertThat(Calculator.parse("119% * 22")).isEqualByComparingTo(new BigDecimal("26.18"));
		assertThat(Calculator.parse("119% / 22").setScale(9, RoundingMode.HALF_EVEN)).isEqualByComparingTo(new BigDecimal("0.054090909"));
		
		assertThat(Calculator.parse("10%+10%")).isEqualByComparingTo(new BigDecimal("0.11"));
		assertThat(Calculator.parse("10% + 10%")).isEqualByComparingTo(new BigDecimal(".11"));
		assertThat(Calculator.parse("10% * 10%")).isEqualByComparingTo(new BigDecimal("0.01"));
		assertThat(Calculator.parse("10% + 5%")).isEqualByComparingTo(new BigDecimal("0.105"));
		assertThat(Calculator.parse("10% * 5%")).isEqualByComparingTo(new BigDecimal("0.005"));
		assertThat(Calculator.parse("10% - 10%")).isEqualByComparingTo(new BigDecimal("0.09"));
		assertThat(Calculator.parse("10% - 5%")).isEqualByComparingTo(new BigDecimal("0.095"));
		assertThat(Calculator.parse("119% + 22%")).isEqualByComparingTo(new BigDecimal("1.4518"));
		assertThat(Calculator.parse("119% - 22%")).isEqualByComparingTo(new BigDecimal("0.9282"));
		assertThat(Calculator.parse("119% * 22%")).isEqualByComparingTo(new BigDecimal("0.2618"));
		assertThat(Calculator.parse("119% / 22%").setScale(9, RoundingMode.HALF_EVEN)).isEqualByComparingTo(new BigDecimal("5.409090909"));
		assertThat(Calculator.parse("10+1+5%")).isEqualByComparingTo(new BigDecimal("11.55"));
		assertThat(Calculator.parse("2+3%-8*4%")).isEqualByComparingTo(new BigDecimal("1.74"));
		
		assertThat(Calculator.parse("2+15*22%")).isEqualByComparingTo(new BigDecimal("5.3"));
		assertThat(Calculator.parse("2-15*22%")).isEqualByComparingTo(new BigDecimal("-1.3"));
		assertThat(Calculator.parse("2*15+22%")).isEqualByComparingTo(new BigDecimal("36.6"));
		assertThat(Calculator.parse("2*15-22%")).isEqualByComparingTo(new BigDecimal("23.4"));
		assertThat(Calculator.parse("2+15+22%")).isEqualByComparingTo(new BigDecimal("20.74"));
		assertThat(Calculator.parse("2-15-22%")).isEqualByComparingTo(new BigDecimal("-10.14"));
	}
}
