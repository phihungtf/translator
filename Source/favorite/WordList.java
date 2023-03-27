package favorite;

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
}
