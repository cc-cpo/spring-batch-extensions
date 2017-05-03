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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FixingCsvRecordSeparatorPolicyTest {

    @Test
    public void endsWithQuote() throws Exception {
        assertEquals("'ends'''", fixLineSemikolon("'ends''"));
    }

    @Test
    public void quoteAndSemicolon() throws Exception {
        assertEquals("'tick';tack'", fixLineSemikolon("'tick';tack'"));
        // note: the openoffice returns: tick;'tack'''
    }

    @Test
    public void lastColumn() throws Exception {
        assertEquals("'some data';'last''column'", fixLineSemikolon("'some data';'last'column'"));
    }

    @Test
    public void blank() throws Exception {
        assertEquals("", fixLineSemikolon(""));
    }

    @Test
    public void empty() throws Exception {
        assertEquals("''", fixLineSemikolon("''"));
    }

    @Test
    public void nullCall() throws Exception {
        assertEquals(null, fixLineSemikolon(null));
    }

    @Test
    public void correct() throws Exception {
        assertEquals("'foo';'bar'", fixLineSemikolon("'foo';'bar'"));
    }

    /**
     * See https://bugs.freedesktop.org/show_bug.cgi?id=48621.
     */
    @Test
    public void magicQuoteHandling() throws Exception {
        assertEquals(";'abc'' d ''ef';", fixLineSemikolon(";'abc' d 'ef';"));
        assertEquals(",\"abc\"\" d \"\"ef\",", fixLineComma(",\"abc\" d \"ef\","));

        assertEquals(";'a''b; ''a';", fixLineSemikolon(";'a'b; 'a';"));
        assertEquals(",\"abc\"\" d \"\"ef\",", fixLineComma(",\"abc\" d \"ef\","));
    }

    private static String fixLineSemikolon(final String line) {
        return FixingCsvRecordSeparatorPolicy.fixQuoteCharactersInsideFields(line, ';', '\'');
    }

    private static String fixLineComma(final String line) {
        return FixingCsvRecordSeparatorPolicy.fixQuoteCharactersInsideFields(line, ',', '"');
    }

}
