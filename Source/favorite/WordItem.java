package favorite;

public class WordItem {
	private String word;
	private String lang;

	public WordItem(String word, String lang) {
		this.word = word;
		this.lang = lang;
	}

	public String getWord() {
		return word;
	}

	public String getLang() {
		return lang;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	@Override
	public String toString() {
		return word;
	}
}
