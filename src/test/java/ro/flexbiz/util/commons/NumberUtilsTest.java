package ro.flexbiz.util.commons;

import static org.junit.jupiter.api.Assertions.fail;
import static ro.flexbiz.util.commons.NumberUtils.adjustPrice;
import static ro.flexbiz.util.commons.NumberUtils.greaterThan;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.jupiter.api.Test;

public class NumberUtilsTest
{
	@Test
	public void adjustPrice_Test()
	{
		adjustPrice(null, null);
		adjustPrice(BigDecimal.ZERO, null);
		adjustPrice(null, BigDecimal.ZERO);
		adjustPrice(BigDecimal.ZERO, BigDecimal.ZERO);
		for (float p = 0.01f; p < 100; p+=0.01)
		{
			for (float q = 0.01f; q < 10; q+=0.01)
			{
				final BigDecimal price = new BigDecimal(p).setScale(2, RoundingMode.HALF_EVEN);
				final BigDecimal quantity = new BigDecimal(q).setScale(2, RoundingMode.HALF_EVEN);
				
				final BigDecimal total = price.multiply(quantity);
				final BigDecimal newPrice = adjustPrice(price, quantity);
				final BigDecimal newTotal = newPrice.multiply(quantity);
				if (newTotal.setScale(4, RoundingMode.HALF_EVEN).toString().endsWith("50"))
					fail("oldPrice = "+price+" quantity = "+quantity+" oldTotal = "+total+" newPrice = "+newPrice+" newTotal = "+newTotal);
				if (greaterThan(total.subtract(newTotal).abs(), new BigDecimal("0.1")))
					fail("error_too_big oldPrice = "+price+" quantity = "+quantity+" oldTotal = "+total+" newPrice = "+newPrice+" newTotal = "+newTotal);
			}
		}
	}
}
