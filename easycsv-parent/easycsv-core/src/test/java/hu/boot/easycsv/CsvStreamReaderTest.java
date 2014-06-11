package hu.boot.easycsv;

import hu.boot.easycsv.stream.StreamReader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.input.NullInputStream;
import org.apache.commons.lang3.CharEncoding;
import org.junit.Assert;
import org.junit.Test;

public class CsvStreamReaderTest {

	@Test(expected = NullPointerException.class)
	public void streamReaderNullTest() {
		final StreamReader reader = new CsvStreamReader(null,
				CharEncoding.UTF_8);
		reader.isEmpty();
		reader.close();
	}

	@Test(expected = NullPointerException.class)
	public void streamReaderNullParameters() {
		final StreamReader reader = new CsvStreamReader(null, null);
		reader.isEmpty();
		reader.close();
	}

	@Test
	public void encodingNullTest() {
		final StreamReader reader = new CsvStreamReader(new NullInputStream(0),
				null);
		reader.isEmpty();
		reader.close();
	}

	@Test
	public void streamReaderEmptyFalseTest() {
		final String sampleText = "Easy Csv";
		final InputStream inputStream = new ByteArrayInputStream(
				sampleText.getBytes());
		final StreamReader reader = new CsvStreamReader(inputStream,
				CharEncoding.UTF_8);
		Assert.assertFalse(reader.isEmpty());
		reader.close();
	}

	@Test
	public void streamReaderEmptyTrueTest() {
		final InputStream inputStream = new NullInputStream(0);
		final StreamReader reader = new CsvStreamReader(inputStream,
				CharEncoding.UTF_8);

		Assert.assertTrue(reader.isEmpty());
		try {
			Assert.assertNull(reader.readNextRow());
		} catch (final IOException e) {

		}
		reader.close();
	}

	@Test
	public void streamReaderResultEqualsTest() {
		final String sampleText = "Easy Csv";
		final InputStream inputStream = new ByteArrayInputStream(
				sampleText.getBytes());
		final StreamReader reader = new CsvStreamReader(inputStream,
				CharEncoding.UTF_8);
		String readedText = null;
		try {
			readedText = reader.readNextRow();
		} catch (final IOException e) {
			Assert.fail();
		}
		Assert.assertEquals(sampleText, readedText);
		reader.close();
	}

}
