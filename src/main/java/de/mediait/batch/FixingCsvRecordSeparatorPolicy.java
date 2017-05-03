/*
~ Copyright 2015 the original author or authors.
~
~ Licensed under the Apache License, Version 2.0 (the "License");
~ you may not use this file except in compliance with the License.
~ You may obtain a copy of the License at
~
~       http://www.apache.org/licenses/LICENSE-2.0
~
~ Unless required by applicable law or agreed to in writing, software
~ distributed under the License is distributed on an "AS IS" BASIS,
~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
~ See the License for the specific language governing permissions and
~ limitations under the License.
*/

package de.mediait.batch;

import java.util.HashSet;
import java.util.Set;

import org.springframework.batch.item.file.separator.SimpleRecordSeparatorPolicy;
import org.springframework.util.StringUtils;

public class FixingCsvRecordSeparatorPolicy extends SimpleRecordSeparatorPolicy {

    public static final String DELIMITER_COMMA = ",";

    public static final char DEFAULT_QUOTE_CHARACTER = '"';

    private char delimiter;

    private char quoteCharacter = DEFAULT_QUOTE_CHARACTER;

    public void setDelimiter(final char delimiter) {
        this.delimiter = delimiter;
    }

    public void setQuoteCharacter(final char quoteCharacter) {
        this.quoteCharacter = quoteCharacter;
    }

    @Override
    public boolean isEndOfRecord(final String line) {
        return !isQuoteUnterminated(line);
    }

    private boolean isQuoteUnterminated(final String line) {
        return StringUtils.countOccurrencesOf(fixQuoteCharactersInsideFields(line),
            String.valueOf(quoteCharacter)) % 2 != 0;
    }

    @Override
    public String preProcess(final String line) {
        if (isQuoteUnterminated(line)) {
            return fixQuoteCharactersInsideFields(line) + "\n";
        }
        return fixQuoteCharactersInsideFields(line);
    }

    private String fixQuoteCharactersInsideFields(final String line) {
        return fixQuoteCharactersInsideFields(line, delimiter, quoteCharacter);
    }

    /**
     *
     * Only quotes surrounding the full string (i.e. preceded or followed by the FIELD-Delimiter)
     * should be viewed as ESCAPING-Quotes, everything else should be considered as an ordinary
     * string.
     */
    @SuppressWarnings({"checkstyle:npathcomplexity", "checkstyle:cyclomaticcomplexity"})
    public static String fixQuoteCharactersInsideFields(final String line,
        final char seperatorCharacter, final char quoteCharacter) {
        if (line == null) {
            return null;
        }

        /**
         * positions of unquoted (single) quotes
         */
        final Set<Integer> indizesUnquoted = new HashSet<Integer>();

        int quoteCount = 0;
        for (int i = 0; i < line.length(); i++) {
            final char ch = line.charAt(i);
            final char l = i > 0 ? line.charAt(i - 1) : 0;
            final char r = i + 1 < line.length() ? line.charAt(i + 1) : 0;

            if (ch != quoteCharacter) {
                continue;
            }

            if (i == 0 || i == line.length() - 1) {
                quoteCount++;
                continue;
            }

            if (l == seperatorCharacter || r == seperatorCharacter) {
                quoteCount++;
             // if neighbour is a seperator, treat the quote as field quote
                continue;
            }
            indizesUnquoted.add(i);
        }

        final StringBuffer out = new StringBuffer();
        for (int i = 0; i < line.length(); i++) {
            if (indizesUnquoted.contains(i)) {
             // add a second quote
                out.append(quoteCharacter);
            }
            out.append(line.charAt(i));
        }

        return out.toString();
    }

}
