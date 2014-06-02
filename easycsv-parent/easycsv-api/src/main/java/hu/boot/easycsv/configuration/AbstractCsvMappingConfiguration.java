package hu.boot.easycsv.configuration;

import java.io.InputStream;

public abstract class AbstractCsvMappingConfiguration implements
		CsvMappingConfiguration {

	private String[] csvHeaderColumns;

	private InputStream xmlMappingConfiguration = null;

	private Class<?> beanType = null;

	private CsvBeanMapping csvBeanMapping = null;

	public AbstractCsvMappingConfiguration() {
	}

	public AbstractCsvMappingConfiguration(String[] csvHeaderColumns) {
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

}
