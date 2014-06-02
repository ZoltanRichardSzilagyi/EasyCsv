package hu.boot.easycsv;

import hu.boot.easycsv.configuration.CsvReaderConfiguration;

import java.io.File;
import java.io.InputStream;
import java.util.List;

public interface Reader<B> {

	public CsvReadResult<B> convert(File file, Class<B> beanType)
			throws EasyCsvException;

	public CsvReadResult<B> convert(List<File> file, Class<B> beanType)
			throws EasyCsvException;

	public CsvReadResult<B> convert(InputStream data, Class<B> beanType)
			throws EasyCsvException;

	public CsvReadResult<B> convert(File file,
			CsvReaderConfiguration configuration) throws EasyCsvException;

	public CsvReadResult<B> convert(List<File> file,
			CsvReaderConfiguration configuration) throws EasyCsvException;

	public CsvReadResult<B> convert(InputStream data,
			CsvReaderConfiguration configuration) throws EasyCsvException;

}
