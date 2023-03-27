package record;

public class Word {
	private String word;
	private int id;

	public Word(String word, int id) {
		this.word = word;
		this.id = id;
	}

	public String getWord() {
		return word;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return word;
	}
}
