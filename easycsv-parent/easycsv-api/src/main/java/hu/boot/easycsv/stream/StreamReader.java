package hu.boot.easycsv.stream;

public interface StreamReader {

	public Boolean isEmpty();

	public String readNextRow();

	public void close();

}
