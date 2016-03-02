package twg2.text.stringEscape;

import java.io.IOException;
import java.io.UncheckedIOException;

import twg2.text.stringUtils.StringIndex;

/**
 * @author TeamworkGuy2
 * @since 2016-2-28
 */
public class StringEscapePartial {

	private StringEscapePartial() { throw new AssertionError("cannot instantiate static class StringEscapePartial"); }


	// ==== String ====
	/**
	 * @see #unescapePartialQuoted(String, int, int, char, char, char, char, boolean, Appendable)
	 */
	public static final int unescapePartialQuoted(String src, int offset, char escapeChar, char quote, char endCh1, Appendable dst) {
		return unescapePartialQuoted(src, offset, src.length() - offset, escapeChar, quote, endCh1, endCh1, false, dst);
	}


	/**
	 * @see #unescapePartialQuoted(String, int, int, char, char, char, char, boolean, Appendable)
	 */
	public static final int unescapePartialQuoted(String src, int offset, char escapeChar, char quote, char endCh1, char endCh2, Appendable dst) {
		return unescapePartialQuoted(src, offset, src.length() - offset, escapeChar, quote, endCh1, endCh2, false, dst);
	}


	/** Parse a sub-string until an ending character is reached, unless the ending
	 * character appears in a quoted section.
	 * For example, if the ending characters are {@code ','} and {@code ']'}, and the {@code str} is:<br>
	 * {@code "a string containing "quotes, [], and commas", further string"}<br>
	 * the result is:<br>
	 * {@code "a string containing "quotes, [], and commas"}
	 * 
	 * @param src
	 * @param offset
	 * @param length
	 * @param escapeChar
	 * @param quote
	 * @param endCh1
	 * @param endCh2
	 * @param throwIfNoEndChar true to throw an error if the string ends without an end char,
	 * false read until the end of the string
	 * @param dst
	 * @return the index of the {@code endCh1/endCh2} that parsing stopped at, or the length
	 * of the {@code src} string if no {@code endCh1/endCh2} character was encountered 
	 */
	public static final int unescapePartialQuoted(String src, int offset, int length, char escapeChar, char quote,
			char endCh1, char endCh2, boolean throwIfNoEndChar, Appendable dst) {
		final int offLen = offset + length;
		boolean added = false;
		int endIndex = StringIndex.indexOf(src, offset, length, endCh1);
		if(endIndex == -1 && endCh2 != endCh1) {
			endIndex = StringIndex.indexOf(src, offset, length, endCh2);
		}
		if(!throwIfNoEndChar && endIndex == -1) {
			endIndex = offLen;
		}
		int quoteIndex = StringIndex.indexOf(src, offset, endIndex - offset, quote);
		if(quoteIndex > -1 && quoteIndex < endIndex) {
			// append the portion of the string up to the quote
			if(offset < quoteIndex) {
				try {
					dst.append(src, offset, quoteIndex + 1);
				} catch (IOException e) {
					throw new UncheckedIOException(e);
				}
			}
			// unescape the quoted portion of the string
			endIndex = StringEscape.unescape(src, quoteIndex + 1, offLen - (quoteIndex + 1), escapeChar, quote, dst);
			if(endIndex > offLen) {
				endIndex = -1;
			}
			// if the quoted portion ends with a closing quote, increment the end index past it since the closing quote is part of the substring we're parsing
			else if(endIndex > offset && src.charAt(endIndex) == quote && src.charAt(endIndex - 1) != escapeChar) {
				endIndex++;
			}
			// if the string ended without a closing quote for the quoted portion, add a closing quote
			if(offset < quoteIndex) {
				try {
					dst.append(quote);
				} catch (IOException e) {
					throw new UncheckedIOException(e);
				}
			}
			added = true;
		}

		if(endIndex == -1) {
			if(throwIfNoEndChar) {
				throw new IllegalArgumentException("string does not end properly, unquoted value did not end with '" + endCh1 + "' or '" + endCh2 + "'");
			}
			else {
				endIndex = offLen;
			}
		}
		else if(!added) {
			try {
				dst.append(src, offset, endIndex);
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
		}

		return endIndex;
	}

}
