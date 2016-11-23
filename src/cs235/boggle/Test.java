package cs235.boggle;

public class Test {

	private static BogglePlayer player = BoggleFactory.createBogglePlayer();
	private final static int MINLEN = 4;

	public static void main(String[] args) {
		String[] board0 = {"a", "b", "d", "e"};
		String[] board1 = { "H", "E", "B", "E", "Z", "K", "T", "S", "T" };
		String[] board2 = { "U", "G", "I", "A", "O", "H", "S", "S", "T", "U",
				"E", "T", "Y", "N", "T", "W" };
		player.loadDictionary("dictionary.txt");
		testValidWord("apple");
		testValidWord("qeruy");
		testValidPrefix("pre");
		testValidPrefix("zes");
		testAllWords(board0);
		testAllWords(board1);
		testAllWords(board2);
		testOnBoard("behest");
		testOnBoard("skeet");
		testOnBoard("jim");
		testOnBoard("tzeb");

	}

	private static void testValidWord(String word) {
		System.out.println("Test the word: " + word);
		System.out.println(player.isValidWord(word));
	}

	private static void testValidPrefix(String prefix) {
		System.out.println("Test the prefix: " + prefix);
		System.out.println(player.isValidPrefix(prefix));
	}

	private static void testAllWords(String[] board) {
		player.setBoard(board);
		System.out.println("Finding all words for given board");
		System.out.println(player.getAllValidWords(MINLEN));

	}

	private static void testOnBoard(String word){
		player.setBoard(player.getCustomBoard());
		System.out.println("Test that " + word +" is on the board.");
		System.out.println(player.isOnBoard(word));
	}
}
