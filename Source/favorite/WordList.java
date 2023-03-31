package favorite;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class WordList {
	private ArrayList<WordItem> list;

	public WordList() {
		list = new ArrayList<>();
	}

	public void add(WordItem word) {
		list.add(word);
	}

	public void remove(int index) {
		list.remove(index);
	}

	public void remove(WordItem word) {
		list.remove(word);
	}

	public WordItem get(int index) {
		return list.get(index);
	}

	public String getValueAt(int row, int col) {
		WordItem word = list.get(row);
		switch (col) {
		case 0:
			return word.getWord();
		case 1:
			return word.getLang();
		}
		return null;
	}

	public void setValueAt(Object value, int row, int col) {
		WordItem word = list.get(row);
		switch (col) {
		case 0:
			word.setWord((String) value);
			break;
		case 1:
			word.setLang((String) value);
			break;
		}
	}

	public int size() {
		return list.size();
	}

	@Override
	public String toString() {
		String result = "";
		for (WordItem word : list) {
			result += word.toString() + "\n";
		}
		return result;
	}

	public void clear() {
		list.clear();
	}

	public boolean isEmpty() {
		return list.isEmpty();
	}

	public WordItem search(String word) {
		for (WordItem wordItem : list) {
			if (wordItem.getWord().equals(word)) {
				return wordItem;
			}
		}
		return null;
	}

	public ArrayList<WordItem> getWordList() {
		return list;
	}

	public void readCSV(File file) throws IOException {
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] values = line.split(",", 2);
				list.add(new WordItem(values[0], values[1]));
			}
			br.close();
		} catch (Exception e) {
			throw e;
		}
	}

	public void writeCSV(File file) throws IOException {
		try {
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
			for (WordItem word : list) {
				bw.write(word.getWord() + "," + word.getLang());
				bw.newLine();
			}
			bw.close();
			fw.close();
		} catch (IOException e) {
			throw e;
		}
	}
}
