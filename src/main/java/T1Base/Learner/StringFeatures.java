package T1Base.Learner;

public class StringFeatures {

	private String phrase;
	private int index;

	public StringFeatures(String phrase, int index) {
		this.phrase = phrase;
		this.index = index;
	}

	public float firstCapital() {
		//Beginning of sentence - ignore
		if (index == 0) {
			return 0;
		}
		if (phrase.charAt(0) >= 'A' && phrase.charAt(0) <= 'Z') {
			return 1;
		}
		return 0;
	}

	public float countCapitals() {
		//The "I" case
		if (phrase.equals("I")) {
			return 0;
		}
		int capitals = 0;
		for (int i = 0; i < phrase.length(); i++) {
			if (phrase.charAt(i) >= 'A' && phrase.charAt(i) <= 'Z') {
				capitals += 1;
			}
		}
		return capitals;
	}
	
	public float countPunctuation() {
		int punctuations = 0;
		for (int i = 0; i < phrase.length(); i++) {
			if (!Character.isLetterOrDigit(phrase.charAt(i)) && phrase.charAt(i)=='/') {
				punctuations += 1;
			}
		}
		return punctuations;
	}

	public float numLetters() {
		int letters = 0;
		for (int i = 0; i < phrase.length(); i++) {
			if (phrase.charAt(i) >= 'A' && phrase.charAt(i) <= 'Z') {
				letters += 1;
			}
			if (phrase.charAt(i) >= 'a' && phrase.charAt(i) <= 'z') {
				letters += 1;
			}
		}
		return letters;
	}

	public float numNumbers() {
		int numbers = 0;
		for (int i = 0; i < phrase.length(); i++) {
			if (phrase.charAt(i) >= '0' && phrase.charAt(i) <= '9') {
				numbers += 1;
			}
		}
		return numbers;
	}

	public float numWords() {
		int words = 1;
		for (int i = 0; i < phrase.length(); i++) {
			if (phrase.charAt(i) == '/') {
				words += 1;
			}
		}
		return words;
	}

}