package hu.boot.easycsv.configuration;

import java.io.InputStream;

public interface CsvMappingConfiguration {

	public InputStream getXmlMappingConfiguration();

	@SuppressWarnings("rawtypes")
	public Class getBeanType();

	public CsvBeanMapping getCsvBeanMapping();

}
