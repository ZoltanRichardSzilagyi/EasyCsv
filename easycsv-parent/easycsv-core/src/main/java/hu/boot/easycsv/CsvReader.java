package hu.boot.easycsv;

import hu.boot.easycsv.cellprocessor.CellProcessorFactory;
import hu.boot.easycsv.configuration.CsvBeanMapper;
import hu.boot.easycsv.configuration.CsvBeanMapping;
import hu.boot.easycsv.configuration.CsvReaderConfiguration;
import hu.boot.easycsv.configuration.DefaultCsvBeanMapper;
import hu.boot.easycsv.error.ErrorMessages;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.input.NullInputStream;

public class CsvReader<B> implements Reader<B> {

	private final CellProcessorFactory cellProcessorFactory = new CellProcessorFactory();

	@Override
	public CsvReadResult<B> convert(File file, Class<B> beanType)
			throws EasyCsvException {
		return convert(file, createDefaultConfiguration(beanType));
	}

	@Override
	public CsvReadResult<B> convert(List<File> files, Class<B> beanType)
			throws EasyCsvException {
		return convert(files, createDefaultConfiguration(beanType));
	}

	@Override
	public CsvReadResult<B> convert(InputStream data, Class<B> beanType)
			throws EasyCsvException {
		createDefaultConfiguration(beanType);
		return convert(data, createDefaultConfiguration(beanType));
	}

	private CsvReaderConfiguration createDefaultConfiguration(Class<B> beanType) {
		CsvReaderConfiguration configuration = new CsvReaderConfiguration();
		configuration.setBeanType(beanType);
		return configuration;
	}

	@Override
	public CsvReadResult<B> convert(File file,
			CsvReaderConfiguration configuration) throws EasyCsvException {
		CsvReadResult<B> result = null;
		InputStream fileInputStream = getFileInputStream(file);
		result = convert(fileInputStream, configuration);
		if (file == null) {
			result.addError(ErrorMessages.FILE_NULL);
		} else if (!file.exists()) {
			result.addError(ErrorMessages.FILE_MISSING, file.getAbsolutePath());
		}
		return result;
	}

	private InputStream getFileInputStream(File file) {
		InputStream fileInputStream = null;
		if (file == null) {
			return new NullInputStream(0);
		}
		try {
			fileInputStream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			fileInputStream = new NullInputStream(0);
		}
		return fileInputStream;
	}

	@Override
	public CsvReadResult<B> convert(List<File> files,
			CsvReaderConfiguration configuration) throws EasyCsvException {
		CsvReadResult<B> result = new CsvReadResult<B>();
		for (File file : files) {
			CsvReadResult<B> subResult = convert(file, configuration);
			result.addBeans(subResult.getBeans());
			result.addErrors(subResult.getErrors());
		}
		return result;
	}

	@Override
	public CsvReadResult<B> convert(InputStream data,
			CsvReaderConfiguration configuration) throws EasyCsvException {
		configuration.setBeanType(configuration.getBeanType());
		CsvBeanMapper beanMapper = new DefaultCsvBeanMapper(configuration);
		CsvBeanMapping mapping = beanMapper.createMapping();
		configuration.setCsvBeanMapping(mapping);

		return processInputData(data, configuration);
	}

	private CsvReadResult<B> processInputData(InputStream data,
			CsvReaderConfiguration configuration) throws EasyCsvException {
		CsvParser.CsvParserBuilder<B> csvParserBuilder = new CsvParser.CsvParserBuilder<B>();
		CsvParser<B> parser = csvParserBuilder
				.setCellProcessorFactory(cellProcessorFactory)
				.setConfiguration(configuration).setData(data).build();
		return parser.parse();
	}

}
