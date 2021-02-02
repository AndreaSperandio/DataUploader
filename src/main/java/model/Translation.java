package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Translation {
	private static final Map<String, String[]> translations = new HashMap<>();
	static {
		Translation.translations.put("tutto l'anno",
				new String[] { "open all year round", "ganzjährig geöffnet", "ouvert toute l’année" });
		Translation.translations.put("cancellazione gratuita fino a %d giorni",
				new String[] { "free cancellation up to %d days before your arrival",
						"kostenlose Stornierung bis %d Tage vor der Anreise", "annulation gratuite jusqu’à %d jours" });
		Translation.translations.put("a %d b %d", new String[] { "c %d d %d", "e %d f %d", "g %d h %d" });
		Translation.translations.put("dal", new String[] { "from", "vom", "du" });
		Translation.translations.put("da", new String[] { "from", "vom", "du" });
		Translation.translations.put("al", new String[] { "to", "bis", "au" });
		Translation.translations.put("a", new String[] { "to", "bis", "au" });
		Translation.translations.put("ai", new String[] { "to", "bis", "au" });
		Translation.translations.put("ad", new String[] { "to", "bis", "au" });
		Translation.translations.put("metà", new String[] { "mid", "mitte", "mi" });
		Translation.translations.put("fine", new String[] { "end", "ende", "fin" });
		Translation.translations.put("e", new String[] { "and", "und", "et" });
		Translation.translations.put("tutto", new String[] { "all", "ganze", "toute" });
		Translation.translations.put("fino", new String[] { "", "", "" });
		Translation.translations.put("gennaio", new String[] { "January", "Januar", "Janvier" });
		Translation.translations.put("febbraio", new String[] { "February", "Februar", "Février" });
		Translation.translations.put("marzo", new String[] { "March", "März", "Mars" });
		Translation.translations.put("aprile", new String[] { "April", "April", "Avril" });
		Translation.translations.put("maggio", new String[] { "May", "Mai", "Mai" });
		Translation.translations.put("giugno", new String[] { "June", "Juni", "Juin" });
		Translation.translations.put("luglio", new String[] { "July", "Juli", "Juillet" });
		Translation.translations.put("agosto", new String[] { "August", "August", "Août" });
		Translation.translations.put("settembre", new String[] { "September", "September", "Septembre" });
		Translation.translations.put("ottobre", new String[] { "October", "Oktober", "Octobre" });
		Translation.translations.put("novembre", new String[] { "November", "November", "Novembre" });
		Translation.translations.put("dicembre", new String[] { "December", "Dezember", "Décembre" });

	}

	public static String[] translate(final String itString) {
		if (itString == null || "".equals(itString)) {
			return null;
		}

		String checkString = itString.toLowerCase();

		// Check for exact matches
		if (Translation.translations.containsKey(checkString)) {
			return Translation.translations.get(checkString);
		}

		// Check for integer composed Strings
		final Integer[] numbers = Translation.getNumbers(itString);
		if (numbers != null) {
			checkString = checkString.replaceAll("[0-9]+", "%d");
		}
		if (Translation.translations.containsKey(checkString)) {
			String[] transl = Translation.translations.get(checkString);
			if (numbers != null) {
				transl = Translation.substituteNumbers(transl, numbers);
			}
			return transl;
		}

		// Translates 1 word at a time, only if all words are present
		checkString = itString.toLowerCase();
		final String[] words = checkString.split(" ");
		final String[] wordsNoNumbers = checkString.replaceAll("[^A-Za-zà]", " ").replaceAll("  ", " ").split(" ");
		for (final String word : wordsNoNumbers) {
			if (!"".equals(word) && !Translation.translations.containsKey(word)) {
				return null;

			}
		}
		return Translation.createTranslation(words);
	}

	private static Integer[] getNumbers(final String string) {
		final String[] sNumbers = string.split("\\D");
		final List<Integer> numbers = new ArrayList<>();

		for (final String sN : sNumbers) {
			if (sN != "") {
				try {
					numbers.add(Integer.parseInt(sN));
				} catch (final NumberFormatException e) {
					System.out.println(e);
				}
			}
		}

		if (numbers.isEmpty()) {
			return null;
		}
		return numbers.toArray(new Integer[numbers.size()]);
	}

	private static String[] substituteNumbers(final String[] strings, final Integer[] numbers) {
		final String[] retStrings = strings.clone();

		for (final Integer n : numbers) {
			for (int i = 0; i < retStrings.length; i++) {
				retStrings[i] = retStrings[i].replaceFirst("%d", n.toString());
			}
		}

		return retStrings;
	}

	private static String[] createTranslation(final String[] words) {
		final String[] retStrings = new String[Language.LANGUAGES - 1];
		for (int i = 0; i < retStrings.length; i++) {
			retStrings[i] = "";
		}
		for (final String word : words) {
			if (Translation.translations.containsKey(word)) {
				final String[] transl = Translation.translations.get(word);

				for (int i = 0; i < retStrings.length; i++) {
					retStrings[i] += transl[i] + " ";
				}
			} else {
				for (int i = 0; i < retStrings.length; i++) {
					retStrings[i] += word + " ";
				}
			}
		}

		for (int i = 0; i < retStrings.length; i++) {
			if ("".equals(retStrings[i])) {
				// Shouldn't happen
				return null;
			}
			retStrings[i] = retStrings[i].trim();
		}
		return retStrings;
	}
}
