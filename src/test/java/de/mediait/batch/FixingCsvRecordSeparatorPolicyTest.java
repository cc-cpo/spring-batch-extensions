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

import static org.junit.Assert.*;

import org.junit.Test;

import de.mediait.batch.FixingCsvRecordSeparatorPolicy;

public class FixingCsvRecordSeparatorPolicyTest {

	 @Test
	    public void quoteInsideString() throws Exception {

	    	assertEquals("'before';'Hawai''i - Teil 2 / CH-Version';'after'", fixLineSemikolon("'before';'Hawai'i - Teil 2 / CH-Version';'after'"));
	    	
//	    	tok.tokenize("'before';'Hawai'i - Teil 2 / CH-Version';'after'");
//	    	Mockito.verify(mock).tokenize("'before';'Hawai''i - Teil 2 / CH-Version';'after'");
	        
	    }
	    
	    @Test
	    public void endsWithQuote() throws Exception {
	    	
	    	assertEquals("'ends'''", fixLineSemikolon("'ends''"));
//	    	Mockito.verify(mock).tokenize("'ends'''");
	    	
	    }

	    @Test
	    public void quoteAndSemicolon() throws Exception {
	    	
	    	assertEquals("'tick';tack'", fixLineSemikolon("'tick';tack'"));
	    	// note: the openoffice returns: tick;'tack'''
	    	
	    }
	    
	    @Test
	    public void lastColumn() throws Exception {
	    	assertEquals("'some data';'last''column'", fixLineSemikolon("'some data';'last'column'"));
//	    	tok.tokenize("'some data';'last'column'");
//	    	Mockito.verify(mock).tokenize("'some data';'last''column'");
	        
	    }

	    @Test
	    public void blank() throws Exception {
	    	assertEquals("", fixLineSemikolon(""));
//	    	tok.tokenize("");
//	    	Mockito.verify(mock).tokenize("");
	        
	    }
	    
	    @Test
	    public void empty() throws Exception {
	    	assertEquals("''", fixLineSemikolon("''"));
//	    	tok.tokenize("''");
//	    	Mockito.verify(mock).tokenize("''");
	        
	    }
	    
	    
	    @Test
	    public void nullCall() throws Exception {
	    	assertEquals(null, fixLineSemikolon(null));
	        
	    }

	    @Test
	    public void correct() throws Exception {
	    	assertEquals("'foo';'bar'", fixLineSemikolon("'foo';'bar'"));
	        
	    }
	    
	    @Test
	    public void ralfUdo() throws Exception {
	    	
	    	// user 10560 from User_liste_2013_12_01_06_45_36.csv
	    	assertEquals("'Hindenburgstr. ';'Postfach 5\n71145 Bondorf'", fixLineSemikolon("'Hindenburgstr. ';'Postfach 5\n71145 Bondorf'"));
	    	
	    }
	    
	    
	    
    /**
     * see https://bugs.freedesktop.org/show_bug.cgi?id=48621
     * and https://bz.apache.org/ooo/show_bug.cgi?id=78926
     */
    @Test
    public void magicQuoteHandling() throws Exception {
//    Only quotes surrounding the full
//    string (i.e. preceded or followed by the FIELD-Delimiter) should be viewed as
//    ESCAPING-Quotes, everything else should be considered as an ordinary string.
    	assertEquals(";'abc'' d ''ef';", fixLineSemikolon(";'abc' d 'ef';"));
    	assertEquals(",\"abc\"\" d \"\"ef\",", fixLineComma(",\"abc\" d \"ef\","));
    	
    	assertEquals(";'a''b; ''a';", fixLineSemikolon(";'a'b; 'a';"));
    	assertEquals(",\"abc\"\" d \"\"ef\",", fixLineComma(",\"abc\" d \"ef\","));
    }
	
	
	private static String fixLineSemikolon(final String line) {
    	return FixingCsvRecordSeparatorPolicy.fixQuoteCharactersInsideFields(line,';','\'');
    }
	
	private static String fixLineComma(final String line) {
    	return FixingCsvRecordSeparatorPolicy.fixQuoteCharactersInsideFields(line,',','"');
    }

}
