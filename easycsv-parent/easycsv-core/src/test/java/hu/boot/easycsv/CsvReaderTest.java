package hu.boot.easycsv;

import hu.boot.easycsv.error.ErrorMessages;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.input.NullInputStream;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Assert;
import org.junit.Test;

public class CsvReaderTest {

	private final CsvReader<User> reader = new CsvReader<User>();

	@Test
	public void readerInputValidationTestFileNull() throws EasyCsvException {
		final CsvReadResult<User> result = reader.convert((File) null,
				User.class);
		Assert.assertEquals(2, result.getErrors().size());
		Assert.assertEquals(ErrorMessages.FILE_NULL, result.getErrors().get(1)
				.getMessage());
	}

	@Test
	public void readerInputValidationTestFileMissing() throws EasyCsvException {
		final File missingFile = new File("T:/random/folder/data.csv");

		final CsvReadResult<User> sampleResult = new CsvReadResult<User>();
		sampleResult.addError(ErrorMessages.FILE_MISSING,
				missingFile.getAbsolutePath());

		final CsvReadResult<User> testResult = reader.convert(missingFile,
				User.class);
		Assert.assertEquals(2, testResult.getErrors().size());

		Assert.assertEquals(sampleResult.getErrors().get(0).getMessage(),
				testResult.getErrors().get(1).getMessage());
	}

	@Test
	public void inputDataTest() throws EasyCsvException {
		final CsvReadResult<User> result = reader.convert(
				new NullInputStream(0), User.class);

		Assert.assertEquals(1, result.getErrors().size());
		Assert.assertEquals(ErrorMessages.EMPTY_CSV, result.getErrors().get(0)
				.getMessage());
		final List<User> users = result.getBeans();
		Assert.assertEquals(0, users.size());
	}

	@Test
	public void rowValidationTest() throws EasyCsvException {
		final CsvReadResult<User> sampleResult = new CsvReadResult<User>();
		sampleResult.addError(ErrorMessages.INVALID_CELLS_NUM, 1, 2, 3);

		final User user = new User();
		user.setName("john");
		user.setAge(28);

		final String data = "name,age" + "\r\n" + "john,28";
		final ByteArrayInputStream stream = new ByteArrayInputStream(
				data.getBytes());
		final CsvReadResult<User> result = reader.convert(stream, User.class);
		Assert.assertEquals(1, result.getErrors().size());
		Assert.assertEquals(sampleResult.getErrors().get(0).getMessage(),
				result.getErrors().get(0).getMessage());
	}

	@Test
	public void parsedDataTest() throws EasyCsvException {
		final CsvReadResult<User> sampleResult = new CsvReadResult<User>();
		sampleResult.addError(ErrorMessages.INVALID_CELLS_NUM, 1, 2, 3);

		final User sampleUser = new User();
		sampleUser.setName("john");
		sampleUser.setAge(28);
		final DateTime dateTime = new DateTime();
		sampleUser.setBirthDate(dateTime.toDate());

		final String data = "name,age" + "\r\n" + "\"john\",28,"
				+ dateTime.toString(ISODateTimeFormat.dateTime());

		final ByteArrayInputStream stream = new ByteArrayInputStream(
				data.getBytes());
		final CsvReadResult<User> result = reader.convert(stream, User.class);
		Assert.assertEquals(0, result.getErrors().size());
		Assert.assertEquals(1, result.getBeans().size());

		final User readedUser = result.getBeans().get(0);
		Assert.assertEquals(sampleUser.getName(), readedUser.getName());
		Assert.assertEquals(sampleUser.getAge(), readedUser.getAge());
		Assert.assertEquals(sampleUser.getBirthDate(),
				readedUser.getBirthDate());
	}

	public void performanceTest() {
		final List<Order> orders = createOrder(10000);
	}

	private List<Order> createOrder(int ordersNum) {
		final List<Order> orders = new ArrayList<Order>(ordersNum);
		for (int i = 0; i < ordersNum; i++) {
			orders.add(createOrder());
		}
		return orders;
	}

	private Order createOrder() {
		final Order order = new Order();
		order.setComment(getRandomString(120));
		order.setCustomerFirstName(getRandomString(20));
		order.setCustomerLastName(getRandomString(20));
		order.setDelivered(true);
		order.setDeliveryDate(new Date());
		order.setOrderDate(new Date());
		order.setOrderId(getRandomString(15));
		order.setPrice(RandomUtils.nextInt(500, 10000));
		order.setProduct(getRandomString(50));
		order.setProductSerial(getRandomString(50));
		return order;
	}

	private String getRandomString(int max) {
		final int value = RandomUtils.nextInt(5, max);
		return RandomStringUtils.randomAlphabetic(value);

	}
}
