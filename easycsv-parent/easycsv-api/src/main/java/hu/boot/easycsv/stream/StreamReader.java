package hu.boot.easycsv.stream;

import java.io.IOException;

public interface StreamReader {

	public Boolean isEmpty() throws IOException;

	public String readNextRow();

	public void close();

}
