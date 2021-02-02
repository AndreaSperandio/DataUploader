package model;

public enum Language {
	IT(0), EN(1), DE(2), FR(3);

	public static final int LANGUAGES = 4;
	private int index;

	private Language(final int index) {
		this.index = index;
	}

	public int getIndex() {
		return this.index;
	}
}
