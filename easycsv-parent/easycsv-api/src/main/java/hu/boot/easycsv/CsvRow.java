package hu.boot.easycsv;

public class CsvRow {

	private String[] row;

	public CsvRow(String[] row) {
		this.row = row;
	}

	public String getField(int index) {
		return row[0];
	}

}
