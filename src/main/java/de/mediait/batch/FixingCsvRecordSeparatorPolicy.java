/*
~ Copyright 2006-2014 the original author or authors.
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
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.springframework.batch.item.file.separator.SimpleRecordSeparatorPolicy;
import org.springframework.util.StringUtils;


public class FixingCsvRecordSeparatorPolicy extends
		SimpleRecordSeparatorPolicy {

	
	@Override
	public boolean isEndOfRecord(String line) {
		return !isQuoteUnterminated(line);
	}

	private boolean isQuoteUnterminated(String line) {
		return StringUtils.countOccurrencesOf(fixQuoteCharactersInsideFields(line), "'") % 2 != 0;
	}

    @Override
	public String preProcess(String line) {
		if (isQuoteUnterminated(line)) {
			return fixQuoteCharactersInsideFields(line) + "\n";
		}
		return fixQuoteCharactersInsideFields(line);
    }

    
    @Deprecated
    public static String fixQuoteCharactersInsideFields(final String line) {
    	return fixQuoteCharactersInsideFields(line, ';', '\'');
    }
    
    public static String fixQuoteCharactersInsideFields(final String line, final char seperatorCharacter, final char quoteCharacter) {
		if( line == null ) {
			return null;
		}
		
		/**
		 * positions of unquoted (single) quotes
		 */
		final Set<Integer> indizesUnquoted = new HashSet<Integer>();
		
		int quoteCount = 0;
		for(int i=0;i<line.length();i++) {
			final char ch = line.charAt(i);
			final char l = i>0 ? line.charAt(i-1) : 0;
			final char r = i+1<line.length() ? line.charAt(i+1) : 0;
			
			if( ch!=quoteCharacter ) {
				continue;
			}
			
			
			if( i==0 || i==line.length()-1 ) {
				quoteCount++;
				continue;
			}
			
			if( l==seperatorCharacter || r==seperatorCharacter ) {
				quoteCount++;
				continue; // if neighbour is a seperator, treat the quote as field quote
			}
			indizesUnquoted.add(i);
		}
		
		if( quoteCount % 2 != 0 ) {
//			System.out.println(""+quoteCount+" "+line);
//			indizesUnquoted.add(line.lastIndexOf(quoteCharacter));
		}
		
		final StringBuffer out = new StringBuffer();
		for(int i=0;i<line.length();i++) {
			if( indizesUnquoted.contains(i) ) {
				out.append(quoteCharacter); // add a second quote
			}
			out.append(line.charAt(i));
		}
		
		
		return out.toString();
    }
    
    @Deprecated
	// implementation idea: blank out any "';'" as well as the first and last quote char in line. search for remaining quote chars and expand them
	public static String fixQuoteCharactersInsideFieldsStefan(final String line) {
		if( line == null ) {
			return null;
		}
		final String masked = line.replace("';'", "___").replaceFirst("^'", "_").replaceFirst("'$", "_");
		
		final List<Integer> indizes = new LinkedList<Integer>();

		int indexFrom = 0;
		while(true) {
			int match = masked.indexOf('\'', indexFrom);
			if( match == -1) {
				break;
			}
			indizes.add(match);
			indexFrom = match+1;
		}
		
		final StringBuffer out = new StringBuffer();
		for(int i=0;i<line.length();i++) {
			if( indizes.contains(i) ) {
				out.append("'");
			}
			out.append(line.charAt(i));
		}
		
		
		return out.toString();
	}
}
