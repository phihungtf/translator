package record;

public class RecordItem {
	private String word;
	private String meaning;

	public RecordItem() {
		this.word = "";
		this.meaning = "";
	}

	public RecordItem(String word, String meaning) {
		this.word = word;
		this.meaning = meaning;
	}

	public String getWord() {
		return word;
	}

	public String getMeaning() {
		return meaning;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public void setMeaning(String meaning) {
		this.meaning = meaning;
	}

	public String toString() {
		return word + ": " + meaning;
	}
}
