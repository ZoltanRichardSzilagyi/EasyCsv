package hu.boot.easycsv;

import hu.boot.easycsv.stream.StreamReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.io.IOUtils;

public class CsvStreamReader implements StreamReader {

	private final InputStream data;
	private final InputStreamReader reader;

	private final BufferedReader bufferedReader;

	public CsvStreamReader(InputStream data) {
		this.data = data;
		reader = new InputStreamReader(data);
		bufferedReader = new BufferedReader(reader);
	}

	@Override
	public String readNextRow() {
		try {
			return bufferedReader.readLine();
		} catch (IOException e) {
			// TODO logger
			return null;
		}
	}

	@Override
	public void close() {
		IOUtils.closeQuietly(bufferedReader);
		IOUtils.closeQuietly(reader);
		IOUtils.closeQuietly(data);
	}

	@Override
	public Boolean isEmpty() {
		int available = data.available();
		(available > 0) ? true : false; 
	}
}
