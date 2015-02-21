package test;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import stringUtils.StringIndex;
import checks.Check;

/**
 * @author TeamworkGuy2
 * @since 2015-2-21
 */
public class StringIndexTest {

	@Test
	public void testCharOccurenceN() {
		String[] inputs = {
				"val val val", "acac", "aaa",
				"a", "a", "",
				"uuv", ""
		};
		int[] matchChars = {
				'v', 'c', 'a',
				'a', 'b', 't',
				'u', 'v'
				
		};
		int[] occurenceN = {
				3, 0, 2,
				0, 1, 0,
				0, 1
		};
		Integer[] expectIndices = {
				8, 3, 1,
				0, -1, -1,
				1, -1
		};

		Check.assertTests(inputs, expectIndices, null, null, (str, i) -> {
			return StringIndex.indexOfOccurrenceN(str, occurenceN[i], matchChars[i]);
		});

	}


	@Test
	public void testStringOccurenceN() {
		String[] inputs = {
				"val val val", "acac", "aaaaa",
				"a", "a", "",
				"uuuuu", ""
		};
		String[] matchStrs = {
				"val", "c", "aa",
				"a", "b", "t",
				"uu", "v"
				
		};
		int[] occurenceN = {
				3, 0, 2,
				0, 1, 0,
				0, 1
		};
		Integer[] expectIndices = {
				8, 3, 2,
				0, -1, -1,
				2, -1
		};

		Check.assertTests(inputs, expectIndices, null, null, (str, i) -> {
			return StringIndex.indexOfOccurrenceN(str, occurenceN[i], matchStrs[i]);
		});

	}


	@Test
	public void indexOfNotPrefixedByTest() {
		{
			String[] strs = new String[] {	"string //!#with !#text", "!#", "//!#!#", "and //!#///!#", "/!#//!#"};
			Integer[] expect = new Integer[] {16, 0, 4, -1, 1};

			String str = "!#";
			String prefix = "//";
			int strOff = 0;
			int prefixOff = 0;
			char[] strC = str.toCharArray();
			char[] prefixC = prefix.toCharArray();

			Check.assertTests(strs, expect, "index of " + str + " without " + prefix + " prefix ", "",
					(s) -> StringIndex.indexOfNotPrefixedBy(s.toCharArray(), 0, strC, strOff, prefixC, prefixOff));
		}

		{
			String[] strs2 = new String[] {	"string //!#with !#text", "!#", "//!#!#", "and //!#///!#", "/!#//!#"};
			Integer[] expect2 = new Integer[] {16, 0, 4, -1, 1};

			String str2 = "!#";
			String prefix2 = "//";
			int strOff2 = 0;
			int prefixOff2 = 0;
			char[] strC2 = str2.toCharArray();
			char[] prefixC2 = prefix2.toCharArray();

			Check.assertTests(strs2, expect2, "index of " + str2 + " without " + prefix2 + " prefix ", "",
					(s) -> StringIndex.indexOfNotPrefixedBy(s.toCharArray(), 0, strC2, strOff2, prefixC2, prefixOff2));
		}

		{
			String[] strs2 = new String[] {	"string <%-with text-%>", "<%-a", "-%>", "and <!--<%-%>", "<!--%>"};
			Integer[] expect2 = new Integer[] {7, 0, 0, 10, 3};

			String str2 = "!#";
			String prefix = "<!--";
			List<String> matchStrs = Arrays.asList("<%-", "-%>");
			int prefixOff = 0;
			char[] prefixC = prefix.toCharArray();

			Check.assertTests(strs2, expect2, "index of " + str2 + " without " + prefix + " prefix ", "",
					(s) -> {
						int index = StringIndex.indexOfMatchNotPrefixedBy(s.toCharArray(), 0, matchStrs, prefixC, prefixOff);
						if(index > -1) {
							index = StringIndex.indexOfNotPrefixedBy(s.toCharArray(), 0, matchStrs.get(index), 0, prefixC, prefixOff);
						}
						return index;
					});
		}
	}

}