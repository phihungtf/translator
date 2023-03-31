package history;

public class LogRow {
	private String word;
	private String lang;
	private int frequency;

	public LogRow(String word, String lang, int frequency) {
		this.word = word;
		this.lang = lang;
		this.frequency = frequency;
	}

	public String getWord() {
		return word;
	}

	public String getLang() {
		return lang;
	}

	public int getFrequency() {
		return frequency;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	@Override
	public String toString() {
		return word + ": " + lang + ": " + frequency;
	}
}
