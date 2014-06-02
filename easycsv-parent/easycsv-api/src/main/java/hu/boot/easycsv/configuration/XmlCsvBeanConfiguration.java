package hu.boot.easycsv.configuration;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "easy-csv-configuration")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlCsvBeanConfiguration {

	@XmlElementWrapper(name = "mappings")
	private List<CsvColumnBeanFieldMapping> mappings;

	public List<CsvColumnBeanFieldMapping> getMappings() {
		return mappings;
	}

	public void setMappings(List<CsvColumnBeanFieldMapping> mappings) {
		this.mappings = mappings;
	}

}
