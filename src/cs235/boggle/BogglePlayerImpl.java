package cs235.boggle;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeSet;

public class BogglePlayerImpl implements BogglePlayer {
	private Object[] dictionary;
	private String[][] board;
	private int[][] used;
	private int size;
	private SortedSet<String> validWords;
	private List<Integer> path;

	public BogglePlayerImpl() {
		dictionary = null;
		board = null;
		used = null;
		path = null;
		size = 0;
	}

	public SortedSet getAllValidWords(int minimumWordLength)
			throws IllegalArgumentException, IllegalStateException {
		validWords = new TreeSet<String>();
		if (minimumWordLength < 1) {
			throw new IllegalArgumentException();
		}
		if (dictionary == null || board == null) {
			throw new IllegalStateException();
		}
		searchBoard(minimumWordLength);
		return validWords;
	}

	private void searchBoard(int minLen) {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				String word = "";
				findWord(word, minLen, i, j);
			}
		}
	}

	private void findWord(String word, int minLen, int i, int j) {
		if (i < 0 || i >= size) {
			return;
		}
		if (j < 0 || j >= size) {
			return;
		}
		if (used[i][j] > 0) {
			return;
		}
		used[i][j] = 1;

		word += board[i][j].toLowerCase();
		if (isValidPrefix(word)) {
			if (word.length() >= minLen && isValidWord(word)) {
				validWords.add(word);
			}
			findWord(word, minLen, i - 1, j - 1);
			findWord(word, minLen, i - 1, j);
			findWord(word, minLen, i - 1, j + 1);
			findWord(word, minLen, i, j - 1);
			findWord(word, minLen, i, j + 1);
			findWord(word, minLen, i + 1, j - 1);
			findWord(word, minLen, i + 1, j);
			findWord(word, minLen, i + 1, j + 1);
		}
		used[i][j] = 0;
	}

	public String[] getCustomBoard() {
		String[] board = { "H", "E", "B", "E", "Z", "K", "T", "S", "T" };
		return board;
	}

	public List isOnBoard(String wordToCheck) throws IllegalArgumentException,
			IllegalStateException {
		String word = "";
		path = new LinkedList<Integer>();
		if (wordToCheck == null) {
			throw new IllegalArgumentException();
		}
		if (board == null) {
			throw new IllegalStateException();
		}
		wordToCheck = wordToCheck.toLowerCase();

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (wordToCheck.startsWith(board[i][j].toLowerCase())) {
					//path = new LinkedList<Integer>();
					containsWord(wordToCheck, word, i, j);
				}
			}
		}

		int max = 0;
		try{
		while (!wordToCheck.equals(word)) {
			int i = path.get(max) / size;
			int j = path.get(max) % size;
			word += board[i][j].toLowerCase();
			max++;
		}
		}
		catch(IndexOutOfBoundsException e){
			return null;
		}
		return path.subList(0, max);
	}

	private int containsWord(String word, String subWord, int i, int j) {
		int n = 0;
		if (i < 0 || i >= size) {
			return 0;
		}
		if (j < 0 || j >= size) {
			return 0;
		}
		if (used[i][j] > 0) {
			return 0;
		}
		used[i][j] = 1;

		subWord += board[i][j].toLowerCase();
		if (word.startsWith(subWord)) {
			path.add(i * size + j);
			if (!word.equals(subWord)) {
				n += containsWord(word, subWord, i - 1, j - 1);
				n += containsWord(word, subWord, i - 1, j);
				n += containsWord(word, subWord, i - 1, j + 1);
				n += containsWord(word, subWord, i, j - 1);
				n += containsWord(word, subWord, i, j + 1);
				n += containsWord(word, subWord, i + 1, j - 1);
				n += containsWord(word, subWord, i + 1, j);
				n += containsWord(word, subWord, i + 1, j + 1);
			} else {
				n = 1;
			}
			if (n == 0){
				path.remove(path.size()-1);
			}

		}

		used[i][j] = 0;
		return n;
	}

	public boolean isValidPrefix(String prefixToCheck)
			throws IllegalArgumentException, IllegalStateException {
		int index;
		if (prefixToCheck == null) {
			throw new IllegalArgumentException();
		}
		if (dictionary == null) {
			throw new IllegalStateException();
		}
		index = java.util.Arrays.binarySearch(dictionary, prefixToCheck);
		if (index > -1) {
			return true;
		}
		index = -index;
		if (index >= dictionary.length) {
			return false;
		}
		if (((String) dictionary[index]).startsWith(prefixToCheck)
				|| ((String) dictionary[index - 1]).startsWith(prefixToCheck)) {
			return true;
		}
		return false;
	}

	public boolean isValidWord(String wordToCheck)
			throws IllegalArgumentException, IllegalStateException {

		if (wordToCheck == null) {
			throw new IllegalArgumentException();
		}
		if (dictionary == null) {
			throw new IllegalStateException();
		}
		if (java.util.Arrays.binarySearch(dictionary, wordToCheck) > -1) {
			return true;
		}

		return false;
	}

	public void loadDictionary(String fileName) throws IllegalArgumentException {
		Scanner cin;
		List<String> words = new LinkedList<String>();
		if (fileName == null) {
			throw new IllegalArgumentException();
		}

		try {
			cin = new Scanner(new File(fileName));
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException();
		}

		while (cin.hasNext()) {
			words.add(cin.next());
		}

		dictionary = new String[words.size()];
		dictionary = words.toArray();
	}

	public void setBoard(String[] letterArray) throws IllegalArgumentException {
		double root;
		if (letterArray == null) {
			throw new IllegalArgumentException();
		}
		root = java.lang.Math.sqrt(letterArray.length);
		size = (int) root;
		if (root - size != 0) {
			throw new IllegalArgumentException();
		}
		board = new String[size][size];
		used = new int[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				board[i][j] = letterArray[i * size + j];
				used[i][j] = 0;
			}
		}
	}

}
