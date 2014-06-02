package hu.boot.easycsv.configuration;

import java.io.InputStream;

import org.apache.commons.lang3.CharEncoding;

public abstract class AbstractCsvConfiguration implements
		CsvMappingConfiguration {

	private String[] csvHeaderColumns;

	private InputStream xmlMappingConfiguration = null;

	private Class<?> beanType = null;

	private CsvBeanMapping csvBeanMapping = null;

	private String charset = CharEncoding.UTF_8;

	public AbstractCsvConfiguration() {
	}

	public AbstractCsvConfiguration(String[] csvHeaderColumns) {
		this.csvHeaderColumns = csvHeaderColumns;
	}

	@Override
	public InputStream getXmlMappingConfiguration() {
		return xmlMappingConfiguration;
	}

	public void setXmlMappingConfiguration(InputStream xmlMappingConfiguration) {
		this.xmlMappingConfiguration = xmlMappingConfiguration;
	}

	@Override
	public Class<?> getBeanType() {
		return beanType;
	}

	public void setBeanType(Class<?> beanType) {
		this.beanType = beanType;
	}

	public CsvBeanMapping getCsvBeanMapping() {
		return csvBeanMapping;
	}

	public void setCsvBeanMapping(CsvBeanMapping csvBeanMapping) {
		this.csvBeanMapping = csvBeanMapping;
	}

	public String[] getCsvHeaderColumns() {
		return csvHeaderColumns;
	}

	public void setCsvHeaderColumns(String[] csvHeaderColumns) {
		this.csvHeaderColumns = csvHeaderColumns;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

}
