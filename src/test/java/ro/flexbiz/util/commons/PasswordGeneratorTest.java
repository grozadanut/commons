package ro.flexbiz.util.commons;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class PasswordGeneratorTest {
	@Test
	public void givenNoCategories_whenBuild_thenThrowException() {
		assertThrows(IllegalArgumentException.class, () -> new PasswordGenerator.Builder().useDigits(false).useLower(false)
				.useUpper(false).usePunctuation(false).build(),
				"At least one category should be specified!");
	}

	@Test
	public void givenZeroLength_whenGenerate_thenThrowException() {
		assertThrows(IllegalArgumentException.class, () -> PasswordGenerator.withDefaults().generate(0),
				"Length should be greater than zero: 0");
	}

	@Test
	public void givenNegativeLength_whenGenerate_thenThrowException() {
		assertThrows(IllegalArgumentException.class, () -> PasswordGenerator.withDefaults().generate(-1),
				"Length should be greater than zero: -1");
	}
	
	@Test
	public void givenSpecifiedLength_whenGenerate_thenRespectLength() {
		final PasswordGenerator gen = PasswordGenerator.withDefaults();
		
		for (int length = 1; length <= 512; length++) {
			assertEquals(gen.generate(length).length(), length);
		}
	}
	
	@Test
	@Disabled
	public void uniquenessTest() {
		//Apr 22, 2024 10:25:00 AM
		//Generated 14 unique passwords with a length of 1
		//Generated 29 unique passwords with a length of 2
		//Generated 628 unique passwords with a length of 3
		//Generated 4,000 unique passwords with a length of 4
		//Generated 61,179 unique passwords with a length of 5
		//Generated 110,073 unique passwords with a length of 6
		//Generated 4,114,050 unique passwords with a length of 7
		//Generated 10,000,000 unique passwords with a length of 8
		//Generated 10,000,000 unique passwords with a length of 9
		final int capSize = 10000000;
		final PasswordGenerator gen = PasswordGenerator.withDefaults();
		
		for (int length = 1; length <= 64; length++) {
			final Set<String> generatedPass = new HashSet<>();
			int i = 0;
			String newPass = gen.generate(length);
			
			while (!generatedPass.contains(newPass)) {
				generatedPass.add(newPass);
				newPass = gen.generate(length);
				if (++i >= capSize)
					break;
			}
			
			System.out.println(MessageFormat.format("Generated {0} unique passwords with a length of {1}", i, length));
		}
	}
	
	@Test
	@Disabled
	public void speedTest() {
		//Apr 22, 2024 10:15:55 AM ro.linic.util.commons.Benchmarking printAndReset
		//INFO: Generated 100000 passwords with a length of 64: 10939 ms
		final PasswordGenerator gen = PasswordGenerator.withDefaults();
		final int size = 100000;
		final Benchmarking bm = Benchmarking.start();
		for (int i = 0; i < size ; i++) {
			gen.generate(64);
		}
		bm.printAndReset("Generated "+size+" passwords with a length of 64");
	}
}
