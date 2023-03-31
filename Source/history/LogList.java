package history;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class LogList {
	private ArrayList<Log> list;

	public LogList() {
		list = new ArrayList<>();
	}

	public void add(File file, Log log) throws IOException {
		try {
			list.add(log);
			FileWriter fw = new FileWriter(file, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(log.getDate() + "," + log.getLang() + "," + log.getWord() + "\n");
			bw.close();
			fw.close();
		} catch (IOException e) {
			throw e;
		}
	}

	public void remove(int index) {
		list.remove(index);
	}

	public void remove(Log log) {
		list.remove(log);
	}

	public Log get(int index) {
		return list.get(index);
	}

	public String getValueAt(int row, int col) {
		Log log = list.get(row);
		switch (col) {
		case 0:
			return log.getWord();
		case 1:
			return log.getLang();
		case 2:
			return log.getDate().toString();
		}
		return null;
	}

	public void setValueAt(Object value, int row, int col) {
		Log log = list.get(row);
		switch (col) {
		case 0:
			log.setWord((String) value);
			break;
		case 1:
			log.setLang((String) value);
			break;
		case 2:
			log.setDate((LocalDate) value);
			break;
		}
	}

	public int size() {
		return list.size();
	}

	public ArrayList<LogRow> getLogRows(LocalDate fromDate, LocalDate toDate) {
		ArrayList<LogRow> logRows = new ArrayList<>();
		for (Log log : list) {
			if (!log.getDate().isBefore(fromDate) && !log.getDate().isAfter(toDate)) {
				if (logRows.size() == 0) {
					logRows.add(new LogRow(log.getWord(), log.getLang(), 1));
				} else {
					boolean found = false;
					for (LogRow logRow : logRows) {
						if (logRow.getWord().equals(log.getWord()) && logRow.getLang().equals(log.getLang())) {
							logRow.setFrequency(logRow.getFrequency() + 1);
							found = true;
							break;
						}
					}
					if (!found) {
						logRows.add(new LogRow(log.getWord(), log.getLang(), 1));
					}
				}
			}
		}
		return logRows;
	}
	
	public void readCSV(File file) throws IOException {
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] values = line.split(",", 3);
				list.add(new Log(values[2], values[1], LocalDate.parse(values[0])));
			}
			br.close();
		} catch (Exception e) {
			throw e;
		}
	}
}
