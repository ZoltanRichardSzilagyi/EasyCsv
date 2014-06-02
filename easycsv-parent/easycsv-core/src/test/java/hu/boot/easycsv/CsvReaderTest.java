package hu.boot.easycsv;

import hu.boot.easycsv.error.ErrorMessages;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.List;

import org.apache.commons.io.input.NullInputStream;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Assert;
import org.junit.Test;

public class CsvReaderTest {

	private CsvReader<User> reader = new CsvReader<User>();

	@Test
	public void readerInputValidationTestFileNull() throws EasyCsvException {
		CsvReadResult<User> result = reader.convert((File) null, User.class);
		Assert.assertEquals(2, result.getErrors().size());
		Assert.assertEquals(ErrorMessages.FILE_NULL, result.getErrors().get(1)
				.getMessage());
	}

	@Test
	public void readerInputValidationTestFileMissing() throws EasyCsvException {
		File missingFile = new File("T:/random/folder/data.csv");

		CsvReadResult<User> sampleResult = new CsvReadResult<User>();
		sampleResult.addError(ErrorMessages.FILE_MISSING,
				missingFile.getAbsolutePath());

		CsvReadResult<User> testResult = reader
				.convert(missingFile, User.class);
		Assert.assertEquals(2, testResult.getErrors().size());

		Assert.assertEquals(sampleResult.getErrors().get(0).getMessage(),
				testResult.getErrors().get(1).getMessage());
	}

	@Test
	public void inputDataTest() throws EasyCsvException {
		CsvReadResult<User> result = reader.convert(new NullInputStream(0),
				User.class);

		Assert.assertEquals(1, result.getErrors().size());
		Assert.assertEquals(ErrorMessages.EMPTY_CSV, result.getErrors().get(0)
				.getMessage());
		List<User> users = result.getBeans();
		Assert.assertEquals(0, users.size());
	}

	@Test
	public void rowValidationTest() throws EasyCsvException {
		CsvReadResult<User> sampleResult = new CsvReadResult<User>();
		sampleResult.addError(ErrorMessages.INVALID_CELLS_NUM, 1, 2, 3);

		User user = new User();
		user.setName("john");
		user.setAge(28);

		String data = "name,age" + "\r\n" + "john,28";
		ByteArrayInputStream stream = new ByteArrayInputStream(data.getBytes());
		CsvReadResult<User> result = reader.convert(stream, User.class);
		Assert.assertEquals(1, result.getErrors().size());
		Assert.assertEquals(sampleResult.getErrors().get(0).getMessage(),
				result.getErrors().get(0).getMessage());
	}

	@Test
	public void parsedDataTest() throws EasyCsvException {
		CsvReadResult<User> sampleResult = new CsvReadResult<User>();
		sampleResult.addError(ErrorMessages.INVALID_CELLS_NUM, 1, 2, 3);

		User sampleUser = new User();
		sampleUser.setName("john");
		sampleUser.setAge(28);
		DateTime dateTime = new DateTime();
		sampleUser.setBirthDate(dateTime.toDate());

		String data = "name,age" + "\r\n" + "john,28,"
				+ dateTime.toString(ISODateTimeFormat.dateTime());

		ByteArrayInputStream stream = new ByteArrayInputStream(data.getBytes());
		CsvReadResult<User> result = reader.convert(stream, User.class);
		Assert.assertEquals(1, result.getBeans().size());

		User readedUser = result.getBeans().get(0);
		Assert.assertEquals(sampleUser.getName(), readedUser.getName());
		Assert.assertEquals(sampleUser.getAge(), readedUser.getAge());
		Assert.assertEquals(sampleUser.getBirthDate(),
				readedUser.getBirthDate());
	}

}
