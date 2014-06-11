package hu.boot.easycsv;

import hu.boot.easycsv.stream.StreamReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CsvStreamReader implements StreamReader {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(CsvStreamReader.class);

	private final InputStream data;
	private final InputStreamReader reader;

	private final BufferedReader bufferedReader;

	public CsvStreamReader(InputStream data, String charsetName) {
		this.data = data;
		reader = new InputStreamReader(data);
		bufferedReader = new BufferedReader(reader);
	}

	@Override
	public String readNextRow() throws IOException {
		return bufferedReader.readLine();
	}

	@Override
	public void close() {
		IOUtils.closeQuietly(bufferedReader);
		IOUtils.closeQuietly(reader);
		IOUtils.closeQuietly(data);
	}

	@Override
	public Boolean isEmpty() {
		int available = 0;
		try {
			available = data.available();
		} catch (final IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return (available > 0) ? false : true;
	}
}
