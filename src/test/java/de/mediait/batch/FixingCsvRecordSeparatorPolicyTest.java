package de.mediait.batch;

import static org.junit.Assert.*;

import org.junit.Test;

import de.mediait.batch.FixingCsvRecordSeparatorPolicy;

public class FixingCsvRecordSeparatorPolicyTest {

	 @Test
	    public void quoteInsideString() throws Exception {

	    	assertEquals("'before';'Hawai''i - Teil 2 / CH-Version';'after'", fixLine("'before';'Hawai'i - Teil 2 / CH-Version';'after'"));
	    	
//	    	tok.tokenize("'before';'Hawai'i - Teil 2 / CH-Version';'after'");
//	    	Mockito.verify(mock).tokenize("'before';'Hawai''i - Teil 2 / CH-Version';'after'");
	        
	    }
	    
	    @Test
	    public void endsWithQuote() throws Exception {
	    	
	    	assertEquals("'ends'''", fixLine("'ends''"));
//	    	Mockito.verify(mock).tokenize("'ends'''");
	    	
	    }

	    @Test
	    public void quoteAndSemicolon() throws Exception {
	    	
	    	assertEquals("'tick';tack'", fixLine("'tick';tack'"));
	    	// note: the openoffice returns: tick;'tack'''
	    	
	    }
	    
	    @Test
	    public void lastColumn() throws Exception {
	    	assertEquals("'some data';'last''column'", fixLine("'some data';'last'column'"));
//	    	tok.tokenize("'some data';'last'column'");
//	    	Mockito.verify(mock).tokenize("'some data';'last''column'");
	        
	    }

	    @Test
	    public void blank() throws Exception {
	    	assertEquals("", fixLine(""));
//	    	tok.tokenize("");
//	    	Mockito.verify(mock).tokenize("");
	        
	    }
	    
	    @Test
	    public void empty() throws Exception {
	    	assertEquals("''", fixLine("''"));
//	    	tok.tokenize("''");
//	    	Mockito.verify(mock).tokenize("''");
	        
	    }
	    
	    
	    @Test
	    public void nullCall() throws Exception {
	    	assertEquals(null, fixLine(null));
	        
	    }

	    @Test
	    public void correct() throws Exception {
	    	assertEquals("'foo';'bar'", fixLine("'foo';'bar'"));
	        
	    }
	    
	    @Test
	    public void ralfUdo() throws Exception {
	    	
	    	// user 10560 from User_liste_2013_12_01_06_45_36.csv
	    	assertEquals("'Hindenburgstr. ';'Postfach 5\n71145 Bondorf'", fixLine("'Hindenburgstr. ';'Postfach 5\n71145 Bondorf'"));
	    	
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
    	assertEquals(";'abc'' d ''ef';", fixLine(";'abc' d 'ef';"));
    	
    	assertEquals(";'a''b; ''a';", fixLine(";'a'b; 'a';"));
    }
	
	
	private static String fixLine(final String line) {
    	return FixingCsvRecordSeparatorPolicy.fixQuoteCharactersInsideFields(line,';','\'');
    }

}
