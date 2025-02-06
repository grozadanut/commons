package ro.flexbiz.util.commons;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ro.flexbiz.util.commons.PresentationUtils.EMPTY_STRING;
import static ro.flexbiz.util.commons.PresentationUtils.toWords;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

public class RomanianNumberToWordsTest
{
	@Test
	public void test()
	{
		assertEquals(RomanianNumberToWords.convert(-1252), "minusunamiedouasutecincizecisidoua");
		assertEquals(RomanianNumberToWords.convert(0), "zero");
		assertEquals(RomanianNumberToWords.convert(1), "una");
		assertEquals(RomanianNumberToWords.convert(2), "doua");
		assertEquals(RomanianNumberToWords.convert(5), "cinci");
		assertEquals(RomanianNumberToWords.convert(10), "zece");
		assertEquals(RomanianNumberToWords.convert(16), "saisprezece");
		assertEquals(RomanianNumberToWords.convert(19), "nouasprezece");
		assertEquals(RomanianNumberToWords.convert(20), "douazeci");
		assertEquals(RomanianNumberToWords.convert(21), "douazecisiuna");
		assertEquals(RomanianNumberToWords.convert(22), "douazecisidoua");
		assertEquals(RomanianNumberToWords.convert(27), "douazecisisapte");
		assertEquals(RomanianNumberToWords.convert(64), "saizecisipatru");
		assertEquals(RomanianNumberToWords.convert(100), "unasuta");
		assertEquals(RomanianNumberToWords.convert(105), "unasutacinci");
		assertEquals(RomanianNumberToWords.convert(110), "unasutazece");
		assertEquals(RomanianNumberToWords.convert(115), "unasutacincisprezece");
		assertEquals(RomanianNumberToWords.convert(118), "unasutaoptsprezece");
		assertEquals(RomanianNumberToWords.convert(120), "unasutadouazeci");
		assertEquals(RomanianNumberToWords.convert(125), "unasutadouazecisicinci");
		assertEquals(RomanianNumberToWords.convert(200), "douasute");
		assertEquals(RomanianNumberToWords.convert(219), "douasutenouasprezece");
		assertEquals(RomanianNumberToWords.convert(300), "treisute");
		assertEquals(RomanianNumberToWords.convert(320), "treisutedouazeci");
		assertEquals(RomanianNumberToWords.convert(333), "treisutetreizecisitrei");
		assertEquals(RomanianNumberToWords.convert(380), "treisuteoptzeci");
		assertEquals(RomanianNumberToWords.convert(800), "optsute");
		assertEquals(RomanianNumberToWords.convert(801), "optsuteuna");
		assertEquals(RomanianNumberToWords.convert(1316), "unamietreisutesaisprezece");
		assertEquals(RomanianNumberToWords.convert(2316), "douamiitreisutesaisprezece");
		assertEquals(RomanianNumberToWords.convert(3316), "treimiitreisutesaisprezece");
		assertEquals(RomanianNumberToWords.convert(1000000), "unamilion");
		assertEquals(RomanianNumberToWords.convert(2000000), "douamilioane");
		assertEquals(RomanianNumberToWords.convert(3000200), "treimilioanedouasute");
		assertEquals(RomanianNumberToWords.convert(700000), "saptesutemii");
		assertEquals(RomanianNumberToWords.convert(9000000), "nouamilioane");
		assertEquals(RomanianNumberToWords.convert(9001000), "nouamilioaneunamie");
		assertEquals(RomanianNumberToWords.convert(123456789), "unasutadouazecisitreimilioanepatrusutecincizecisisasemiisaptesuteoptzecisinoua");
		assertEquals(RomanianNumberToWords.convert(2147483647), "douamiliardeunasutapatruzecisisaptemilioanepatrusuteoptzecisitreimiisasesutepatruzecisisapte");
		assertEquals(RomanianNumberToWords.convert(3000000010L), "treimiliardezece");
		
		assertEquals(toWords(null), EMPTY_STRING);
		assertEquals(toWords(new BigDecimal(-1252)), "minusunamiedouasutecincizecisidoualei");
		assertEquals(toWords(new BigDecimal(0)), "zerolei");
		assertEquals(toWords(new BigDecimal(1)), "unalei");
		assertEquals(toWords(new BigDecimal(2)), "doualei");
		assertEquals(toWords(new BigDecimal(5)), "cincilei");
		assertEquals(toWords(new BigDecimal(10)), "zecelei");
		assertEquals(toWords(new BigDecimal(16)), "saisprezecelei");
		assertEquals(toWords(new BigDecimal(19)), "nouasprezecelei");
		assertEquals(toWords(new BigDecimal(20)), "douazecilei");
		assertEquals(toWords(new BigDecimal(21)), "douazecisiunalei");
		assertEquals(toWords(new BigDecimal(22)), "douazecisidoualei");
		assertEquals(toWords(new BigDecimal(27)), "douazecisisaptelei");
		assertEquals(toWords(new BigDecimal(64)), "saizecisipatrulei");
		assertEquals(toWords(new BigDecimal(100)), "unasutalei");
		assertEquals(toWords(new BigDecimal(105)), "unasutacincilei");
		assertEquals(toWords(new BigDecimal(110)), "unasutazecelei");
		assertEquals(toWords(new BigDecimal(115)), "unasutacincisprezecelei");
		assertEquals(toWords(new BigDecimal(118)), "unasutaoptsprezecelei");
		assertEquals(toWords(new BigDecimal(120)), "unasutadouazecilei");
		assertEquals(toWords(new BigDecimal(125)), "unasutadouazecisicincilei");
		assertEquals(toWords(new BigDecimal(200)), "douasutelei");
		assertEquals(toWords(new BigDecimal(219)), "douasutenouasprezecelei");
		assertEquals(toWords(new BigDecimal(300)), "treisutelei");
		assertEquals(toWords(new BigDecimal(320)), "treisutedouazecilei");
		assertEquals(toWords(new BigDecimal(333)), "treisutetreizecisitreilei");
		assertEquals(toWords(new BigDecimal(380)), "treisuteoptzecilei");
		assertEquals(toWords(new BigDecimal(800)), "optsutelei");
		assertEquals(toWords(new BigDecimal(801)), "optsuteunalei");
		assertEquals(toWords(new BigDecimal(1316)), "unamietreisutesaisprezecelei");
		assertEquals(toWords(new BigDecimal(2316)), "douamiitreisutesaisprezecelei");
		assertEquals(toWords(new BigDecimal(3316)), "treimiitreisutesaisprezecelei");
		assertEquals(toWords(new BigDecimal(1000000)), "unamilionlei");
		assertEquals(toWords(new BigDecimal(2000000)), "douamilioanelei");
		assertEquals(toWords(new BigDecimal(3000200)), "treimilioanedouasutelei");
		assertEquals(toWords(new BigDecimal(700000)), "saptesutemiilei");
		assertEquals(toWords(new BigDecimal(9000000)), "nouamilioanelei");
		assertEquals(toWords(new BigDecimal(9001000)), "nouamilioaneunamielei");
		assertEquals(toWords(new BigDecimal(123456789)), "unasutadouazecisitreimilioanepatrusutecincizecisisasemiisaptesuteoptzecisinoualei");
		assertEquals(toWords(new BigDecimal(2147483647)), "douamiliardeunasutapatruzecisisaptemilioanepatrusuteoptzecisitreimiisasesutepatruzecisisaptelei");
		assertEquals(toWords(new BigDecimal(3000000010L)), "treimiliardezecelei");
		
		assertEquals(toWords(new BigDecimal(-1252.1)), "minusunamiedouasutecincizecisidoualeisizecebani");
		assertEquals(toWords(new BigDecimal(0.01)), "zeroleisiunabani");
		assertEquals(toWords(new BigDecimal(1.05)), "unaleisicincibani");
		assertEquals(toWords(new BigDecimal(2.10)), "doualeisizecebani");
		assertEquals(toWords(new BigDecimal(5.11)), "cincileisiunsprezecebani");
		assertEquals(toWords(new BigDecimal(10.12)), "zeceleisidouasprezecebani");
		assertEquals(toWords(new BigDecimal(16.15)), "saisprezeceleisicincisprezecebani");
		assertEquals(toWords(new BigDecimal(19.20)), "nouasprezeceleisidouazecibani");
		assertEquals(toWords(new BigDecimal(20.21)), "douazecileisidouazecisiunabani");
		assertEquals(toWords(new BigDecimal(21.22)), "douazecisiunaleisidouazecisidouabani");
		assertEquals(toWords(new BigDecimal(22.25)), "douazecisidoualeisidouazecisicincibani");
		assertEquals(toWords(new BigDecimal(27.30)), "douazecisisapteleisitreizecibani");
		assertEquals(toWords(new BigDecimal(64.31)), "saizecisipatruleisitreizecisiunabani");
		assertEquals(toWords(new BigDecimal(100.37)), "unasutaleisitreizecisisaptebani");
		assertEquals(toWords(new BigDecimal(105.84)), "unasutacincileisioptzecisipatrubani");
		assertEquals(toWords(new BigDecimal(110.153)), "unasutazeceleisicincisprezecebani");
		assertEquals(toWords(new BigDecimal(115.1547)), "unasutacincisprezeceleisicincisprezecebani");
		assertEquals(toWords(new BigDecimal(118.253)), "unasutaoptsprezeceleisidouazecisicincibani");
		assertEquals(toWords(new BigDecimal(120.822)), "unasutadouazecileisioptzecisidouabani");
		assertEquals(toWords(new BigDecimal(125.156248)), "unasutadouazecisicincileisisaisprezecebani");
		assertEquals(toWords(new BigDecimal(200.2546875)), "douasuteleisidouazecisicincibani");
		assertEquals(toWords(new BigDecimal(219.3858752)), "douasutenouasprezeceleisitreizecisinouabani");
	}
}
