package hu.boot.easycsv;

import hu.boot.easycsv.bean.CsvColumn;

import java.util.Date;

public class User {

	@CsvColumn(name = "name")
	private String name;

	@CsvColumn(name = "age")
	private Integer age;

	@CsvColumn(name = "birth-date", required = false)
	private Date birthDate;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

}
