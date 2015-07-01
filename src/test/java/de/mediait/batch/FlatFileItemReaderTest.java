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

import org.junit.Test;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.ArrayFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.ClassPathResource;

import static org.junit.Assert.assertArrayEquals;

public class FlatFileItemReaderTest {

	
	@Test
	public void testFlatFileReader() throws Exception {
		
		final FlatFileItemReader<String[]> reader = createFlatFileReader(';','\'');
		
		reader.setResource(new ClassPathResource("csv-fix-samples/fixsemicolon.txt"));
		
		final ExecutionContext executionContext = new ExecutionContext();
		reader.open(executionContext);
		final String[] object = (String[]) reader.read();
		reader.close();


		assertArrayEquals(new String[]{"begin","abc' \"d\" 'ef","end"}, object);
	}
	
	
	@Test
	public void testFlatFileReader2() throws Exception {
		
		final FlatFileItemReader<String[]> reader = createFlatFileReader(',','\'');
		
		reader.setResource(new ClassPathResource("csv-fix-samples/fixcomma.txt"));
		
		final ExecutionContext executionContext = new ExecutionContext();
		reader.open(executionContext);
		final String[] object = (String[]) reader.read();
		reader.close();


		assertArrayEquals(new String[]{"begin","abc' \"d\" 'ef","end"}, object);
	}

	

	
	private FlatFileItemReader<String[]> createFlatFileReader(final char seperatorCharacter, final char quoteCharacter) {
		final FlatFileItemReader<String[]> reader = new FlatFileItemReader<String[]>();
		final DefaultLineMapper<String[]> lineMapper = new DefaultLineMapper<String[]>();
		final DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer(String.valueOf(seperatorCharacter));
		tokenizer.setQuoteCharacter(quoteCharacter);
		lineMapper.setLineTokenizer(tokenizer);
		final FixingCsvRecordSeparatorPolicy recordSeparatorPolicy = new FixingCsvRecordSeparatorPolicy();
		recordSeparatorPolicy.setQuoteCharacter(quoteCharacter);
		reader.setRecordSeparatorPolicy(recordSeparatorPolicy);
		reader.setLineMapper(lineMapper);
		final FieldSetMapper<String[]> fieldSetMapper = new ArrayFieldSetMapper();
		lineMapper.setFieldSetMapper(fieldSetMapper);
		return reader;
	}
	
}
