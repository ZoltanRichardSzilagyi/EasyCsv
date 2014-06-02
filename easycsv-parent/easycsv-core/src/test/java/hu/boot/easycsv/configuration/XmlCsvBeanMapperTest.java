package hu.boot.easycsv.configuration;

import hu.boot.easycsv.EasyCsvException;
import hu.boot.easycsv.User;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.junit.Assert;
import org.junit.Test;

public class XmlCsvBeanMapperTest {

	@Test
	public void testMapping() throws Exception {
		ReflectionCsvBeanMapper reflectionCsvBeanMapper = new ReflectionCsvBeanMapper(
				User.class);

		CsvBeanMapping reflectionMapping = reflectionCsvBeanMapper
				.createMapping();
		XmlCsvBeanConfiguration xmlConfiguration = new XmlCsvBeanConfiguration();
		xmlConfiguration.setMappings(reflectionMapping
				.getCsvColumnFieldMapping());

		Marshaller marshaller = createJAXBMarshaller();
		OutputStream out = new ByteArrayOutputStream();
		marshaller.marshal(xmlConfiguration, out);

		InputStream config = new ByteArrayInputStream(out.toString().getBytes());
		XmlCsvBeanMapper xmlMapper = new XmlCsvBeanMapper(config);
		CsvBeanMapping xmlMapping = xmlMapper.createMapping();

		Assert.assertEquals(reflectionMapping.getColumnsNum(),
				xmlMapping.getColumnsNum());

	}

	private Marshaller createJAXBMarshaller() throws EasyCsvException {
		Marshaller marshaller = null;
		try {
			JAXBContext jaxbContext = JAXBContext
					.newInstance(XmlCsvBeanConfiguration.class);
			marshaller = jaxbContext.createMarshaller();
		} catch (JAXBException e) {
			throw new EasyCsvException("Unable to create xml configuration.", e);
		}
		return marshaller;
	}

}
