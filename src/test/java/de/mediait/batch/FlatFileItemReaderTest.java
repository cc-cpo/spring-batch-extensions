package de.mediait.batch;

import org.junit.Test;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.ArrayFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.separator.RecordSeparatorPolicy;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.ClassPathResource;

import static org.junit.Assert.assertEquals;

public class FlatFileItemReaderTest {

	@Test
	public void testName() throws Exception {
		
		
		FlatFileItemReader<String[]> reader = new FlatFileItemReader<String[]>();
		DefaultLineMapper<String[]> lineMapper = new DefaultLineMapper<String[]>();
		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer(";");
		lineMapper.setLineTokenizer(tokenizer);
		RecordSeparatorPolicy recordSeparatorPolicy = new FixingCsvRecordSeparatorPolicy();
		reader.setRecordSeparatorPolicy(recordSeparatorPolicy);
		reader.setLineMapper(lineMapper);
		FieldSetMapper<String[]> fieldSetMapper = new ArrayFieldSetMapper();
		lineMapper.setFieldSetMapper(fieldSetMapper);
		
		reader.setResource(new ClassPathResource("foo.txt"));
		
		ExecutionContext executionContext = new ExecutionContext();
		reader.open(executionContext);
		String[] object = (String[]) reader.read();
		reader.close();


		assertEquals("'abc' d 'ef'", object[1]);
	}
	
}
