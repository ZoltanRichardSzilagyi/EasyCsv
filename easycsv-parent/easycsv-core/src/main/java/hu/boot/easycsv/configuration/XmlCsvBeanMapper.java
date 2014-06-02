package hu.boot.easycsv.configuration;

import hu.boot.easycsv.EasyCsvException;

import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class XmlCsvBeanMapper implements CsvBeanMapper {

	private InputStream configurationData = null;

	public XmlCsvBeanMapper(InputStream configurationData) {
		this.configurationData = configurationData;
	}

	@Override
	public CsvBeanMapping createMapping() throws EasyCsvException {
		XmlCsvBeanConfiguration configuration = unmarshallConfiguration();
		CsvBeanMapping beanMapping = new CsvBeanMapping(
				configuration.getMappings());
		return beanMapping;
	}

	private XmlCsvBeanConfiguration unmarshallConfiguration()
			throws EasyCsvException {
		Unmarshaller unmarshaller = createJAXBUnmarshaller();
		XmlCsvBeanConfiguration configuration = null;
		try {
			configuration = (XmlCsvBeanConfiguration) unmarshaller
					.unmarshal(configurationData);
		} catch (JAXBException e) {
			throw new EasyCsvException("Unable to read xml configuration.", e);
		}
		return configuration;
	}

	private Unmarshaller createJAXBUnmarshaller() throws EasyCsvException {
		Unmarshaller unmarshaller = null;
		try {
			JAXBContext jaxbContext = JAXBContext
					.newInstance(XmlCsvBeanConfiguration.class);
			unmarshaller = jaxbContext.createUnmarshaller();
		} catch (JAXBException e) {
			throw new EasyCsvException("Unable to read xml configuration.", e);
		}
		return unmarshaller;
	}
}
