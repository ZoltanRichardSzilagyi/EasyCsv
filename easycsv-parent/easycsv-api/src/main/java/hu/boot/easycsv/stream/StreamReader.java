package hu.boot.easycsv.stream;

import java.io.IOException;

public interface StreamReader {

	public Boolean isEmpty();

	public String readNextRow() throws IOException;

	public void close();

}
