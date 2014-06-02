package hu.boot.easycsv.configuration;

import java.io.InputStream;

public interface CsvMappingConfiguration {

	public InputStream getXmlMappingConfiguration();

	public Class<?> getBeanType();

	public CsvBeanMapping getCsvBeanMapping();

}
