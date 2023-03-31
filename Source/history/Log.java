package history;

import java.time.LocalDate;

public class Log {
	private String word;
	private String lang;
	private LocalDate date;

	public Log(String word, String lang) {
		this.word = word;
		this.lang = lang;
		this.date = LocalDate.now();
	}

	public Log(String word, String lang, LocalDate date) {
		this.word = word;
		this.lang = lang;
		this.date = date;
	}

	public String getWord() {
		return word;
	}

	public String getLang() {
		return lang;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return word + ": " + lang + ": " + date;
	}
}
