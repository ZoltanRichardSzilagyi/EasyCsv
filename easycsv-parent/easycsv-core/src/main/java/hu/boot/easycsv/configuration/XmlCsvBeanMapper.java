package hu.boot.easycsv.configuration;

import hu.boot.easycsv.EasyCsvException;

import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class XmlCsvBeanMapper implements CsvBeanMapper {

	private static final String UNABLE_TO_READ_XML_CONFIGURATION = "Unable to read xml configuration.";
	private InputStream configurationData = null;

	public XmlCsvBeanMapper(InputStream configurationData) {
		this.configurationData = configurationData;
	}

	@Override
	public CsvBeanMapping createMapping() throws EasyCsvException {
		final XmlCsvBeanConfiguration configuration = unmarshallConfiguration();
		return new CsvBeanMapping(configuration.getMappings());
	}

	private XmlCsvBeanConfiguration unmarshallConfiguration()
			throws EasyCsvException {
		final Unmarshaller unmarshaller = createJAXBUnmarshaller();
		XmlCsvBeanConfiguration configuration = null;
		try {
			configuration = (XmlCsvBeanConfiguration) unmarshaller
					.unmarshal(configurationData);
		} catch (final JAXBException e) {
			throw new EasyCsvException(UNABLE_TO_READ_XML_CONFIGURATION, e);
		}
		return configuration;
	}

	private Unmarshaller createJAXBUnmarshaller() throws EasyCsvException {
		Unmarshaller unmarshaller = null;
		try {
			final JAXBContext jaxbContext = JAXBContext
					.newInstance(XmlCsvBeanConfiguration.class);
			unmarshaller = jaxbContext.createUnmarshaller();
		} catch (final JAXBException e) {
			throw new EasyCsvException(UNABLE_TO_READ_XML_CONFIGURATION, e);
		}
		return unmarshaller;
	}
}
