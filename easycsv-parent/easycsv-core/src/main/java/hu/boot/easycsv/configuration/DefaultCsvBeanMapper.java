package hu.boot.easycsv.configuration;

import hu.boot.easycsv.EasyCsvException;

import java.util.ArrayList;
import java.util.List;

public class DefaultCsvBeanMapper implements CsvBeanMapper {

	private AbstractCsvConfiguration configuration;

	public DefaultCsvBeanMapper(AbstractCsvConfiguration configuration) {
		this.configuration = configuration;
	}

	@Override
	public CsvBeanMapping createMapping() throws EasyCsvException {
		CsvBeanMapper mapper = null;
		mapper = getMapperByConfigurationType();
		CsvBeanMapping mapping = mapper.createMapping();
		setDefaultCsvHeaderConfigurationIfRequired(mapping);
		return mapping;
	}

	private CsvBeanMapper getMapperByConfigurationType() {
		CsvBeanMapper mapper;
		if (configuration.getXmlMappingConfiguration() == null) {
			mapper = new ReflectionCsvBeanMapper(configuration.getBeanType());
		} else {
			mapper = new XmlCsvBeanMapper(
					configuration.getXmlMappingConfiguration());
		}
		return mapper;
	}

	private void setDefaultCsvHeaderConfigurationIfRequired(
			CsvBeanMapping mapping) {
		if (configuration.getCsvHeaderColumns() != null) {
			return;
		}
		List<String> csvHeaderColumnNames = new ArrayList<String>();
		List<CsvColumnBeanFieldMapping> csvFieldMappings = mapping
				.getCsvColumnFieldMapping();
		for (CsvColumnBeanFieldMapping csvFieldMapping : csvFieldMappings) {
			csvHeaderColumnNames.add(csvFieldMapping.getColumnName());
		}
		configuration.setCsvHeaderColumns(csvHeaderColumnNames
				.toArray(new String[] {}));
	}

}
