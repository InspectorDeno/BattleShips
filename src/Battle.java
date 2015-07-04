import java.util.*;

public class Battle {
	public static Scanner scan = new Scanner(System.in);
	public Random random = new Random(System.currentTimeMillis());
	public static int turn;
	public static int gameMode;
	public static int boardSize;
	public static int numOfShips;
	public static boolean PvPvP;
	public static boolean gameOver;
	public static boolean autoPlace;
	public static boolean firstGame;
	public LinkedList<Player> turns;
	public Player p1, p2, p3;
	public Bot AI, AI2;
	public Player our = new Player();
	public Player their = new Player();

	public static void main(String[] args) {
		Battle game = new Battle();
		firstGame = true;
		String input = "";
		boolean again = true;
		boolean valid = false;

		System.out.println(
				"\n\t WELCOME TO BATTLESHIPS!\n\n"
						+ " Enter the corresponding number to make a selection\n");
		do {
			game.play();
			firstGame = false;
			System.out.println("\n Do you want to play again? (Y/N):");
			while (!valid) {
				valid = false;

				try {
					input = scan.nextLine();
					char c = input.charAt(0);
					switch (c) {
						case 'Y':
							valid = true;
							again = true;
							break;
						case 'N':
							valid = true;
							again = false;
							break;
						default:
							valid = false;
							System.out.println(" Enter something");
					}
				} catch (StringIndexOutOfBoundsException e) {
					System.out.println(" Enter something");
				}
			}
		} while (again);
		System.out.println("\n Thanks for playing! Good bye!");
		scan.close();
	}

	private void play() {
		init();
		switch (gameMode) {
			case 1:
				printBoard();
				p1.placeShips();
				p1.hideShips();
				p2.placeShips();
				p2.hideShips();
				System.out.println("\n Both players are ready, let the battle begin!"
						+ "\n\n Enter your moves in the form 'D2'"
						+ "\n\n At any time during the game,"
						+ "\n Type 'S' to see all remaining ships"
						+ "\n Type 'H' to get a hint");
				break;
			case 2:
				printBoard();
				p1.placeShips();
				autoPlace = true;
				p2.AIplaceShips();
				p2.hideShips();
				System.out.println("\n Your enemy has placed out their ships!"
						+ "\n Let the battle begin!"
						+ "\n\n Enter your moves in the form 'D2'"
						+ "\n\n At any time during the game,"
						+ "\n Type 'S' to see all remaining ships"
						+ "\n Type 'H' to get a hint");
				break;
			case 3:
				printBoard();
				p1.placeShips();
				if (!p2.bot || !p3.bot) { // Hide ships if another player
					p1.hideShips();
				}
				if (!p2.bot) {
					p2.placeShips();
				} else { // Autoplace if bot
					autoPlace = true;
					p2.AIplaceShips();
				}
				p2.hideShips();
				if (!p3.bot) {
					p3.placeShips();
				} else { // Autoplace if bot
					autoPlace = true;
					p3.AIplaceShips();
				}
				p3.hideShips();
				System.out.println("\n All players are ready,"
						+ " let the battle begin!"
						+ "\n You get 2 shots per round, 1 per opponent"
						+ "\n\n Enter your moves in the form 'D2'"
						+ "\n\n At any time during the game,"
						+ "\n Type 'S' to see all remaining ships"
						+ "\n Type 'H' to get a hint");
				break;
			case 4:
				printBoard();
				autoPlace = true;
				p1.AIplaceShips();
				autoPlace = true;
				p2.AIplaceShips();
				printBoard();
				break;
		}
		while (!gameOver) {
			if (gameMode == 1) {
				if (turn == 1) {
					p1.move();
					printBoard();
				} else {
					p2.move();
					printBoard();
				}
			} else if (gameMode == 2 || gameMode == 4) {
				if (turn == 1) {
					p1.move();
				} else {
					p2.move();
					printBoard();
				}
			} else if (gameMode == 3) { // 3 Player Game
				if (turn == 1) {
					p1.move();
					printBoard();
				} else if (turn == 2) {
					p2.move();
					if (!p2.bot) {
						printBoard();
					}
				} else {
					p3.move();
					// shotOnce is reset after two shots
					if (p3.bot && !p3.shotOnce) {
						printBoard();
					} else if (!p3.bot) {
						printBoard();
					}
				}
			}
		}
		someStats();
	}

	private void init() {
		boolean chosenMode = false;
		System.out.println(
				"\t_______Game_Mode______\n" +
						"\t 1: Player vs Player \n" +
						"\t 2: Player vs Bot\n" +
						"\t 3: 3 Player Game\n" +
						"\t______________________");
		p1 = new Player();
		p2 = new Player();
		p3 = new Player();
		turns = new LinkedList<Player>();

		while (!chosenMode) {
			try {
				char mode = scan.nextLine().charAt(0);
				switch (mode) {
					case '1': // Player vs Player
						gameMode = 1;
						addPlayer(p1, 1);
						addPlayer(p2, 2);
						turns.add(p1);
						turns.add(p2);
						chosenMode = true;
						break;
					case '2': // Player vs Bot
						gameMode = 2;
						addPlayer(p1, 1);
						addBot(AI, 2);
						turns.add(p1);
						turns.add(p2);
						chosenMode = true;
						break;
					case '3': // 3 Player Game
						gameMode = 3;
						PvPvP = true;
						System.out.println(" In this mode, everyone fights everyone\n\n"
								+ "\t_______Game_Mode______\n"
								+ "\t 1: 3 Players\n"
								+ "\t 2: 2 Players, 1 Bot\n"
								+ "\t 3: 1 Player, 2 Bots\n"
								+ "\t______________________");
						boolean chosen3 = false;
						while (!chosen3) {
							char mode3 = scan.nextLine().charAt(0);
							switch (mode3) { // PvPvP
								case '1':
									addPlayer(p1, 1);
									addPlayer(p2, 2);
									addPlayer(p3, 3);
									chosen3 = true;
									break;
								case '2': // PvPvAI
									addPlayer(p1, 1);
									addPlayer(p2, 2);
									addBot(AI, 3);
									chosen3 = true;
									break;
								case '3': // PvAIvAI
									addPlayer(p1, 1);
									addBot(AI, 2);
									addBot(AI2, 3);
									p3.name = "Amigo";
									chosen3 = true;
									break;
								default:
							}
						}
						turns.add(p1);
						turns.add(p2);
						turns.add(p3);
						chosenMode = true;
						break;

					case '4': // AI vs AI
						gameMode = 4;
						addBot(AI, 1);
						addBot(AI2, 2);
						p2.name = "R2D2";
						turns.add(p1);
						turns.add(p2);
						chosenMode = true;
						break;
					default:
						System.out.println(" Enter 1, 2, or 3");
				}
			} catch (StringIndexOutOfBoundsException e) {
				System.out.println(" Enter something");
			}
		}
		p1.number = 1;
		p2.number = 2;
		p3.number = 3;

		System.out.println(
				"\t______Board_Size______\n"
						+ "\t 1: Small\n"
						+ "\t 2: Medium\n"
						+ "\t 3: Large\n"
						+ "\t______________________\n");

		boolean chosenSize = false;
		while (!chosenSize) {
			try {
				char size = scan.nextLine().charAt(0);
				switch (size) {
					case '1':
						boardSize = 6;
						numOfShips = 3;
						p1.ships.add(new Ship(1));
						p1.ships.add(new Ship(3));
						p1.ships.add(new Ship(4));
						p2.ships.add(new Ship(1));
						p2.ships.add(new Ship(3));
						p2.ships.add(new Ship(4));
						p3.ships.add(new Ship(1));
						p3.ships.add(new Ship(3));
						p3.ships.add(new Ship(4));
						chosenSize = true;
						break;
					case '2':
						numOfShips = 4;
						boardSize = 8;
						p1.ships.add(new Ship(1));
						p1.ships.add(new Ship(2));
						p1.ships.add(new Ship(4));
						p1.ships.add(new Ship(5));
						p2.ships.add(new Ship(1));
						p2.ships.add(new Ship(2));
						p2.ships.add(new Ship(4));
						p2.ships.add(new Ship(5));
						p3.ships.add(new Ship(1));
						p3.ships.add(new Ship(2));
						p3.ships.add(new Ship(4));
						p3.ships.add(new Ship(5));
						chosenSize = true;
						break;
					case '3':
						numOfShips = 5;
						boardSize = 10;
						p1.ships.add(new Ship(1));
						p1.ships.add(new Ship(2));
						p1.ships.add(new Ship(3));
						p1.ships.add(new Ship(4));
						p1.ships.add(new Ship(5));
						p2.ships.add(new Ship(1));
						p2.ships.add(new Ship(2));
						p2.ships.add(new Ship(3));
						p2.ships.add(new Ship(4));
						p2.ships.add(new Ship(5));
						p3.ships.add(new Ship(1));
						p3.ships.add(new Ship(2));
						p3.ships.add(new Ship(3));
						p3.ships.add(new Ship(4));
						p3.ships.add(new Ship(5));
						chosenSize = true;
						break;
					case 'H': // IQTest
						numOfShips = 5;
						boardSize = 8;
						p1.ships.add(new Ship(6));
						p1.ships.add(new Ship(7));
						p1.ships.add(new Ship(8));
						p1.ships.add(new Ship(9));
						p1.ships.add(new Ship(10));
						p2.ships.add(new Ship(1));
						p2.ships.add(new Ship(2));
						p2.ships.add(new Ship(3));
						p2.ships.add(new Ship(4));
						p2.ships.add(new Ship(5));
						chosenSize = true;
					default:
						System.out.println(" Enter 1, 2 or 3");
				}
			} catch (StringIndexOutOfBoundsException e) {
				System.out.println(" Enter something");
			}
		}
		p1.board = new BoardPiece[boardSize][boardSize];
		p2.board = new BoardPiece[boardSize][boardSize];
		p3.board = new BoardPiece[boardSize][boardSize];

		for (int i = 0; i < p1.board.length; i++) {
			for (int j = 0; j < p1.board[0].length; j++) {
				p1.board[i][j] = new BoardPiece();
				p2.board[i][j] = new BoardPiece();
				p3.board[i][j] = new BoardPiece();
			}
		}
		// Reset these
		turn = 1;
		gameOver = false;
	}

	private void addPlayer(Player p, int i) {
		String name = "";
		String message = "Enter your name: ";
		if (gameMode != 2) {
			message = "Enter name of Player " + i + ": ";
		}
		System.out.println(message);
		boolean ready = false;
		while (!ready) {
			name = scan.nextLine();
			if (name.isEmpty() || name.length() > 7) {
				System.out.println(" Please enter a name between 1-7 characters");
			} else {
				p.name = name;
				ready = true;
			}
		}
	}

	private void addBot(Bot bot, int i) {
		if (PvPvP) {
			System.out.println(
					"\t____Computer_Level____\n"
							+ "\t 1: So Dumb\n"
							+ "\t 2: Clever\n"
							+ "\t______________________");
		} else {
			System.out.println(
					"\t____Computer_Level____\n"
							+ "\t 1: So Dumb\n"
							+ "\t 2: Clever\n"
							+ "\t 3: Worty Opponent\n"
							+ "\t 4: Deep Blue\n"
							+ "\t______________________");
		}
		boolean ready = false;
		while (!ready) {
			try {
				char iq = scan.nextLine().charAt(0);
				switch (iq) {
					case '1':
						bot = new Bot(1);
						ready = true;
						break;
					case '2':
						bot = new Bot(2);
						ready = true;
						break;
					case '3':
						bot = new Bot(3);
						if (PvPvP) {
							System.out.println(" Enter 1 or 2");
							;
						} else {
							ready = true;
						}
						break;
					case '4':
						bot = new Bot(4);
						if (PvPvP) {
							System.out.println(" Enter 1 or 2");
							;
						} else {
							ready = true;
						}
						break;
					default:
						System.out.println(" Enter a valid number");
				}
			} catch (StringIndexOutOfBoundsException e) {
				System.out.println(" Enter something");
			}
		}
		switch (i) {
			case 1:
				p1 = bot;
				p1.bot = true;
				break;
			case 2:
				p2 = bot;
				p2.bot = true;
				break;
			case 3:
				p3 = bot;
				p3.bot = true;
				break;
		}
	}

	private void printBoard() {
		// Prints game board corresponding to desired board size
		String space = "   ";
		String boardwidth = "";
		String topRow = "";
		String colEnum = " ";

		// Determines boardWidth
		for (int i = 0; i < boardSize - 1; i++) {
			boardwidth += space + " ";
		}

		for (int colCount = 0; colCount <= boardSize; colCount++) {
			// Row 1: Player names.
			if (colCount == 0) {
				// Top row
				topRow += " ›" + p1.name + "‹" + boardwidth + "\t ›" + p2.name + "‹";
				// Add if 3 players
				if (PvPvP) {
					topRow += boardwidth + "\t ›" + p3.name + "‹";
				}
				System.out.println("\n" + topRow);
				// Row 2: Player column enumeration
			} else {
				colEnum += space + (colCount - 1);
			}
		}
		// Space in between
		colEnum += space + "\t ";
		// Row 2: Player 2 Column numbers
		for (int colCount = 1; colCount <= boardSize; colCount++) {
			colEnum += space + (colCount - 1);
		}
		// Add if 3 players
		if (gameMode == 3) {
			// Space in between
			colEnum += space + "\t ";
			// Row 2: Player 3 Column numbers
			for (int colCount = 1; colCount <= boardSize; colCount++) {
				colEnum += space + (colCount - 1);
			}
		}
		// Print column numbers
		System.out.print(colEnum);

		char rowCount = 'A';
		for (int y = 0; y < boardSize; y++) {
			System.out.println();
			// Player 1 Board
			for (int x = 0; x < boardSize; x++) {
				if (x == 0) {
					System.out.print(" " + rowCount + " ");
				}
				System.out.print("_" + p1.board[y][x].piece + "_|");
			}
			// Space
			System.out.print(space + "\t");
			// Player 2 Board
			for (int x = 0; x < boardSize; x++) {
				if (x == 0) {
					System.out.print(" " + rowCount + " ");
				}
				System.out.print("_" + p2.board[y][x].piece + "_|");
			}
			// 3 Player game
			if (gameMode == 3) {
				// Space in between
				System.out.print(space + "\t");
				// Player 3 Board
				for (int x = 0; x < boardSize; x++) {
					if (x == 0) {
						System.out.print(" " + rowCount + " ");
					}
					System.out.print("_" + p3.board[y][x].piece + "_|");
				}
			}
			rowCount++; // A-B-C-D-E
		}
		System.out.println();
		System.out.print(colEnum);
		System.out.println();
	}

	public void someStats() {
		String table[][];
		int numOfShipSquares = 0;
		for (Ship ship : p1.ships) {
			numOfShipSquares += ship.size;
		}
		p1.hitPercent = (double) (p1.hits * 1000 / (p1.hits + p1.misses)) / 10;
		p1.damPercent = (double) (p1.hits * 1000 / numOfShipSquares) / 10;
		p2.hitPercent = (double) (p2.hits * 1000 / (p2.hits + p2.misses)) / 10;
		p2.damPercent = (double) (p2.hits * 1000 / numOfShipSquares) / 10;

		if (gameMode == 3) { // 3 Player game
			table = new String[9][5];
			p3.damPercent = (double) (p3.hits * 1000 / numOfShipSquares) / 10;
			p3.hitPercent = (double) (p3.hits * 1000 / (p3.hits + p3.misses)) / 10;
			table[7][0] = "Ships left: |";
			table[7][1] = "  " + (p1.ships.size() - p1.destroyedShips);
			table[7][2] = "  " + (p2.ships.size() - p2.destroyedShips);
			table[7][3] = "  " + (p3.ships.size() - p3.destroyedShips);
			table[7][4] = "|";
			table[8][0] = "Ships sunk: |";
			table[8][1] = "  " + p1.sunkCount;
			table[8][2] = "  " + p2.sunkCount;
			table[8][3] = "  " + p3.sunkCount;
			table[8][4] = "|";
		} else {
			table = new String[7][5];
		}

		String result = "\n";

		table[0][0] = "|";
		table[1][0] = "|";
		table[2][0] = "Shots: |";
		table[3][0] = "Hits: |";
		table[4][0] = "Hit %: |";
		table[5][0] = "Misses: |";
		table[6][0] = "Damage %: |";

		table[0][1] = " " + p1.name;
		table[1][1] = "  ";
		table[2][1] = "  " + (p1.hits + p1.misses);
		table[3][1] = "  " + p1.hits;
		table[4][1] = "  " + p1.hitPercent;
		table[5][1] = "  " + p1.misses;
		table[6][1] = "  " + p1.damPercent;

		table[0][2] = " " + p2.name;
		table[1][2] = "  ";
		table[2][2] = "  " + (p2.hits + p2.misses);
		table[3][2] = "  " + p2.hits;
		table[4][2] = "  " + p2.hitPercent;
		table[5][2] = "  " + p2.misses;
		table[6][2] = "  " + p2.damPercent;

		table[0][3] = " " + p3.name;
		table[1][3] = "  ";
		table[2][3] = "  " + (p3.hits + p3.misses);
		table[3][3] = "  " + p3.hits;
		table[4][3] = "  " + p3.hitPercent;
		table[5][3] = "  " + p3.misses;
		table[6][3] = "  " + p3.damPercent;
		// /
		table[0][4] = "|";
		table[1][4] = "|";
		table[2][4] = "|";
		table[3][4] = "|";
		table[4][4] = "|";
		table[5][4] = "|";
		table[6][4] = "|";
		// /

		if (gameMode == 3) {
			result += String.format("%13s%s %n", "", "_________Stats_Board_________");
		} else {
			result += String.format("%14s%s %n", "", "____Stats_Board____");
		}
		for (int i = 0; i < table.length; i++) {
			result += String.format("%14s%-9s%-9s", table[i][0], table[i][1], table[i][2]);
			if (gameMode == 3) {
				result += String.format("%-9s", table[i][3]);
			}
			result += String.format("%-1s %n", table[i][4]);
		}
		if (gameMode == 3) {
			result += String.format("%13s%s", "", "|___________________________|");
		} else {
			result += String.format("%13s%s", "", "|__________________|");
		}
		System.out.println(result);
	}

	public class Player {
		public String name;
		public BoardPiece[][] board;
		public List<Ship> ships;
		public LinkedHashMap<Integer, String> hitList;
		public HashMap<Integer, List<String>> hints;
		public boolean bot;
		public boolean ready;
		public boolean shotOnce;
		public boolean gameIsOver;
		public int number;
		public int hits;
		public int misses;
		public int destroyedShips; // the ones we've lost
		public int sunkCount; // the ones we've sunk
		public double hitPercent;
		public double damPercent;

		Player() {
			name = "";
			board = new BoardPiece[boardSize][boardSize];
			ships = new ArrayList<Ship>();
			hitList = new LinkedHashMap<Integer, String>();
			hints = new HashMap<Integer, List<String>>();
			bot = false;
			ready = false;
			shotOnce = false;
			gameIsOver = false;
			hits = 0;
			misses = 0;
			destroyedShips = 0;
			number = 0;
		}

		private void placeShips() {

			System.out.println("\n Okay " + name + ", it´s time to place your ships on the board! ");
			if (gameMode == 1 && !p1.ready) { // PvP
				System.out.println(" " + p2.name + ", look away now until " + p1.name + " is done!");
			} else if (gameMode == 3) { // PvPvP
				if (!p1.ready && !p2.ready) {
					System.out.println(
							" " + p2.name + " and " + p3.name + ", look away now until " + p1.name
									+ " is done!");
				} else if (p1.ready) {
					System.out.println(" Scrolling up is cheating!");
				}
			}
			System.out.println("\n You have " + numOfShips + " ships to place:");
			System.out.println("\t_________________________");
			int i = 1;
			for (Ship ship : ships) {
				System.out.println("\t" + i + ": " + ship.name + "\t- " + ship.size + " spots");
				i++;
			}
			System.out.println("\t_________________________");
			if (firstGame) {
				instructions();
			} else {
				System.out.println("\n If you wish to read the instructions again, type 'I'");
			}

			boolean said = false;
			while (!ready) {
				try {
					String input = scan.nextLine();
					char choise = input.charAt(0);

					if (input.length() == 1) {
						switch (choise) {
							case 'C':
								System.out.println("\n You have " + numOfShips + " ships to place:");
								System.out.println("\t_________________________");
								i = 1;
								for (Ship ship : ships) {
									System.out.println("\t" + i + ": " + ship.name + "\t- " + ship.size + " spots");
									i++;
								}
								System.out.println("\t_________________________");
								clearBoard();
								printBoard();
								break;
							case 'D':
								if (allPlaced()) {
									ready = true;
								} else {
									System.out.println(" You need to place all of your ships.");
								}
								break;
							case 'I': // For instructions
								instructions();
								break;
							case 'R':
								autoPlace = true;
								clearBoard();
								AIplaceShips();
								printBoard();
								if (!said) {
									System.out.println("\n Nice, ships are placed!\n"
											+ " If you wish, you can now reposition your ships");
								}
								System.out.println(" Type 'D' if you are Done");
								said = true;
								break;
							default:
								System.out.println("Invalid input, try again");

						}
					} else if (input.length() == 6) {
						int n = input.charAt(0) - 49; // = 1,2,3,4,5
						int row = input.charAt(2) - 65; // A=0 ... J=9
						int col = input.charAt(3) - 48; // 0=0 ... 9=9
						char layout = input.charAt(5); // H or V

						if (n > (numOfShips - 1) || n < 0) { // Ship number between 1 and
																// numOfShips
							System.out.println(" Invalid ship type");
						} else if (row >= boardSize) {
							System.out.println(" Invalid row");
						} else if (col >= boardSize) {
							System.out.println(" Invalid Column");
						} else if ((int) input.charAt(5) != 'H'
								&& (int) input.charAt(5) != 'V') {
							System.out.println(" Invalid orientation");
							// Valid row and column - check if the ship fits
						} else {
							// Doesn't fit
							if (layout == 'H' && ships.get(n).size > (boardSize - col)) {
								System.out.println(" Invalid Horizontal Placement. Not enough room.");
								// Doesn't fit
							} else if (layout == 'V' && ships.get(n).size > (boardSize - row)) {
								System.out.println(" Invalid Vertical Placement. Not enough room.");
								// Valid placement! Except overlap
							} else {
								placeOnBoard(this, layout, ships.get(n), input.substring(2, 4));
							}
						}
						if (allPlaced()) {
							if (!said) {
								System.out.println("\n Nice, ships are placed!\n"
										+ " If you wish, you can now reposition your ships");
							}
							System.out.println(" Type 'D' if you are Done");
							said = true;
						}
					} else {
						System.out.println(" Invalid input, try again");
					}
				} catch (StringIndexOutOfBoundsException e) {
					System.out.println(" Invalid input, try again");
				}
			}
		}

		private void AIplaceShips() {
			for (Ship current : ships) {
				boolean placed = false;
				while (!placed) {
					int row = random.nextInt(boardSize); // [0,boardSize)
					int col = random.nextInt(boardSize);
					int layout = random.nextInt(2); // H or V, [0,1]

					if (layout == 0) {
						layout = 72; // H
					} else {
						layout = 86; // V
					}
					if (layout == 'H' && current.size > ((boardSize) - col))
						; // Try again
					else if (layout == 'V' && current.size > ((boardSize) - row))
						; // Try again
					else { // Valid placement
						row += 65; // 0=A ... 9=J
						col += 48; // 0=0 ... 9=9
						String location = String.valueOf((char) row) + String.valueOf((char) col);
						// Try to place the ship on the board.
						placeOnBoard(this, (char) layout, current, location);
						if (current.placed) {
							placed = true;
						}
					}
				}
			}
			autoPlace = false;
		}

		protected void placeOnBoard(Player p, char layout, Ship current, String location) {
			boolean overlap = false;
			int row = location.charAt(0) - 65;
			int col = location.charAt(1) - 48;

			// Checks overlap
			if (layout == 'H') {
				for (int x = col; x < col + current.size; x++) {
					if (p.board[row][x].type != current.type && p.board[row][x].used == true) {
						overlap = true;
					}
				}
			} else { // Layout == 'V'
				for (int y = row; y < row + current.size; y++) {
					if (p.board[y][col].type != current.type && p.board[y][col].used == true) {
						overlap = true;
					}
				}
			}
			// We don't overlap
			if (!overlap) {
				if (current.placed) { // Relocate ship, remove old one
					int oldRow = current.location.charAt(0) - 65;
					int oldCol = current.location.charAt(1) - 48;
					if (current.orientation == 'H') {
						for (int x = oldCol; x < oldCol + current.size; x++) {
							p.board[oldRow][x].piece = "_";
							p.board[oldRow][x].used = false;
							p.board[oldRow][x].type = 0;
						}
					} else {
						for (int y = oldRow; y < oldRow + current.size; y++) {
							p.board[y][oldCol].piece = "_";
							p.board[y][oldCol].used = false;
							p.board[y][oldCol].type = 0;
						}
					}
				}
				if (layout == 'H') { // Place horizontally
					for (int x = col; x < col + current.size; x++) {
						p.board[row][x].piece = "@";
						p.board[row][x].used = true;
						p.board[row][x].type = current.type;
					}
				} else { // Place vertically
					for (int y = row; y < row + current.size; y++) {
						p.board[y][col].piece = "@";
						p.board[y][col].used = true;
						p.board[y][col].type = current.type;
					}
				}
				current.placed = true;
				current.location = location;
				current.orientation = layout;
				// Print the board when successful
				if (!autoPlace) {
					printBoard();
				}

			} else { // Overlap
				if (!autoPlace) {
					System.out.println(" Invalid placement. Ships overlap");
				}
			}
			// Display what ships are left. Not when we autoplace the ships
			if (!allPlaced() && !autoPlace) {
				System.out.println("\n Ships left to place:");
				System.out.println("\t_________________________");
				for (Ship ship : p.ships) {
					if (!ship.placed) {
						System.out.println("\t" + (p.ships.indexOf(ship) + 1) + ": " + ship.name + "\t- "
								+ ship.size + " spots");
					}
				}
				System.out.println("\t_________________________");
			} else
				; // All ships are correctly placed on the board
		}

		protected void move() {
			String input = "";
			String valid = "";
			String message = "";

			our = turns.peekFirst();
			their = turns.peekLast();

			if (PvPvP) { // 3 Player game, all players left
				message += "\n ›" + name + "'s turn to target ";
				turns.pop();
				if (turn == 1) {
					if (shotOnce) {
						message += turns.peekLast().name + "‹";
					} else {
						message += turns.peekFirst().name + "‹";
					}
				} else {
					if (shotOnce) {
						message += turns.peekFirst().name + "‹";
					} else {
						message += turns.peekLast().name + "‹";
					}
				}
				turns.addFirst(this);
			} else { // 2 Players
				// Print this for player only games
				if (gameMode != 2) {
					message = "\n ›" + name + "'s turn";
					if (gameMode == 3) {
						message += " to target " + their.name;
					}
				}
			}
			System.out.println(message);

			do {
				System.out.println(" Enter move: ");
				input = scan.nextLine();
				if (input.length() == 1) {
					char choise = input.charAt(0);
					switch (choise) {
						case 'H':
							System.out.println(" HINT: " + getHint() + " looks good");
							break;
						case 'S':
							shipsLeft();
							break;
						default:
					}
				} else {
					valid = checkMove(input);
				}

				if (valid != "ok") {
					System.out.println(valid);
				}
				// Until valid input
			} while (valid != "ok");

			processMove(input);

			if (PvPvP) {
				// Players get two shots. If they've shot once it's the next player's turn
				if (shotOnce) {
					turns.pop();
					turns.addLast(this);
					// Next person in line
					turn = turns.peek().number;
					shotOnce = false;
					// Else they've now shot once
				} else {
					shotOnce = true;
				}
			} else {
				// Check if game is over only when PvP
				gameOver = checkWin();
				// Pop and put last in line
				turns.pop();
				turns.addLast(this);
				turn = turns.peek().number;
			}
		}

		protected String getHint() {

			if (PvPvP) {
				our = turns.pop();
				if (turn == 1) {
					if (shotOnce) {
						their = turns.peekLast();
					} else {
						their = turns.peekFirst();
					}
				} else {
					if (shotOnce) {
						their = turns.peekFirst();
					} else {
						their = turns.peekLast();
					}
				}
				turns.addFirst(this);
			} else { // PvP
				our = turns.peekFirst();
				their = turns.peekLast();
			}
			// reset probabilities
			for (int row = 0; row < boardSize; row++) {
				for (int col = 0; col < boardSize; col++) {
					their.board[row][col].prob = 0;
				}
			}

			boolean overlap;
			Iterator<Ship> it = their.ships.iterator();
			// Evaluate whole board for all ships left
			while (it.hasNext()) {
				Ship current = it.next();
				if (!current.destroyed) {
					// System.out.println("\n" + current.name);
					for (int row = 0; row < boardSize; row++) {
						for (int col = 0; col < boardSize; col++) {
							overlap = false;
							// Horizontal placement
							if ((col + current.size) > boardSize) {
								overlap = true;
							} else {
								// Checks overlap, ignore ships on hit List
								for (int x = col; x < (current.size + col); x++) {
									if (their.board[row][x].selected &&
											!our.hitList.containsKey(their.board[row][x].type)) {
										overlap = true;
									}
								}
							}
							if (!overlap) {
								// Add probability to these squares
								for (int x = col; x < (current.size + col); x++) {
									their.board[row][x].prob++;
								}
							}
							// Reset
							overlap = false;
							// Vertical placement
							if ((row + current.size) > boardSize) {
								overlap = true;
							} else {
								// Checks overlap
								for (int y = row; y < (current.size + row); y++) {
									if (their.board[y][col].selected &&
											!our.hitList.containsKey(their.board[y][col].type)) {
										overlap = true;
									}
								}
							}
							if (!overlap) {
								// Add probability to these squares
								for (int y = row; y < (current.size + row); y++) {
									their.board[y][col].prob++;
								}
							}
						}
					}
				}
			}

			int i = 0;
			String max = "";
			hints.clear();
			String hint = "";
			List<String> coords = new ArrayList<String>();
			// Collect the highest valued squares
			for (int row = 0; row < boardSize; row++) {
				for (int col = 0; col < boardSize; col++) {
					if (their.board[row][col].selected) {
						their.board[row][col].prob = 0;
					}
					if (their.board[row][col].prob > i) {
						coords.clear();
						i = their.board[row][col].prob;

						max = String.valueOf((char) (row + 65)) + String.valueOf((char) (col + 48));
						coords.add(max);
					} else if (their.board[row][col].prob == i) {
						i = their.board[row][col].prob;
						max = String.valueOf((char) (row + 65)) + String.valueOf((char) (col + 48));
						coords.add(max);
					}
					// System.out.printf("%5s", their.board[row][col].prob);
				}
				// System.out.println();
			}

			int index = random.nextInt(coords.size());
			hint = coords.get(index);
			return hint;
		}

		protected String checkMove(String move) {
			// Won't process until ok
			if (move.length() != 2) {
				return " Invalid input.";
			}
			int row = (move.charAt(0)) - 65;
			int col = (move.charAt(1)) - 48;

			our = turns.pop();
			if (turn == 1) {
				if (our.shotOnce) {
					their = turns.peekLast();
				} else {
					their = turns.peekFirst();
				}
			} else { // P1 or P3
				if (our.shotOnce) {
					their = turns.peekFirst();
				} else {
					their = turns.peekLast();
				}
			}
			turns.addFirst(our);

			if (row >= boardSize || row < 0) {
				return " Invalid row.";
			}
			if (col >= boardSize || col < 0) {
				return " Invalid column.";
			}
			if (their.board[row][col].selected) {
				return " Already chosen.";
			}
			return "ok";
		}

		protected void processMove(String move) {
			int row = move.charAt(0) - 65;
			int col = move.charAt(1) - 48;
			char marker = ' ';
			String message = "";
			Ship hitShip = new Ship();
			int shipType = 0;

			if (PvPvP) {
				// This is to balance who gets hit
				our = turns.pop();
				if (turn == 1) {
					if (our.shotOnce) {
						their = turns.peekLast(); // p1>p3
					} else {
						their = turns.peekFirst(); // p1>p2
					}
				} else { // P2 or P3
					if (our.shotOnce) { // p2>p1 or p3>p2
						their = turns.peekFirst();
					} else {
						their = turns.peekLast(); // p2>p3 or p3>p1
					}
				}
				turns.addFirst(our);
			} else { // PvP
				our = turns.peekFirst();
				their = turns.peekLast();
			}
			shipType = their.board[row][col].type;

			// We hit
			if (their.board[row][col].used) {
				marker = 'X';
				message = "Hit!";
				our.hits++;
				// For when we reveal the board
				their.board[row][col].hidden = false;

				// If it's a new ship, add to HitList (hints)
				if (!our.hitList.containsKey(shipType)) {
					our.hitList.put(shipType, move);
				}
				// Find the hit ship
				for (Ship ship : their.ships) {
					if (ship.type == shipType) {
						hitShip = ship;
					}
				}
				hitShip.hitCount++;

				if (hitShip.hitCount == hitShip.size) { // Destroyed
					hitShip.destroyed = true;
					their.destroyedShips++;
					our.sunkCount++;
					// Remove it from our hitList
					our.hitList.remove(shipType);
					if (gameMode == 2) {
						message += "\n You ";
					} else {
						message += "\n " + our.name;
					}
					message += " destroyed " + their.name + "'s " + hitShip.name + "!";
					// Displays Ships left, not if all are sunk
					if (their.destroyedShips != numOfShips) {
						message += "\n\t_______Ships_left_______\n";
						for (Ship left : their.ships) {
							if (!left.destroyed) {
								message += "\t " + left.name + "\t- " + left.size + " spots\n";
							}
						}
					} else { // All ships are sunk
						if (PvPvP) {
							// Remove first loser from turns list
							turns.remove(their);
							message += "\n " + their.name + " has no ships left!";
							// PvPvP to PvP
							PvPvP = false;
						} else { // PvP
							their.gameIsOver = true;
						}
					}
				}
			} else { // We missed
				marker = 'o';
				message = "Miss!";
				our.misses++;
			}
			their.board[row][col].piece = Character.toString(marker);
			their.board[row][col].selected = true;

			message = String.format("%-20s%s", " ›" + our.name + "'s move:", move + " - " + message);
			System.out.println(message);

		}

		protected boolean checkWin() {

			if (their.gameIsOver) {
				p1.revealShips();
				p2.revealShips();

				if (gameMode == 1) {
					System.out.println(
							"\n Congratulations " + our.name + "! You've destroyed all of " + their.name
									+ "'s ships!");
				} else if (gameMode == 2) {
					if (our.bot = false) {
						printBoard();
						System.out.println(
								"\n Congratulations " + our.name + "! You've destroyed all enemy ships!");
					} else { // The AI won
						System.out.println("\n Defeat! You've lost your fleet!");
					}
				} else { // gameMode == 3
					p3.revealShips();
					System.out.println(
							"\n Congratulations " + our.name + "! You have the strongest fleet!");
				}
				return true;
			}
			return false;
		}

		private void shipsLeft() {
			String list = "";
			Iterator<Ship> it = p1.ships.iterator();
			Iterator<Ship> it2 = p2.ships.iterator();
			Iterator<Ship> it3 = p3.ships.iterator();
			if (gameMode == 1 || gameMode == 2) {
				list += String.format("%-10s%-30s %n", "", "________Ships_Left________");
			} else {
				list += String.format("%-10s%-40s %n", "", "_______________Ships_Left________________");
			}
			list += String.format("%-10s%-15s%-15s", "", "  " + p1.name, "  " + p2.name);
			if (gameMode == 3) {
				list += String.format("%-15s", "  " + p3.name);
			}
			list += "\n\n";
			for (int i = 0; i < ships.size(); i++) {
				Ship p1Ship = it.next();
				Ship p2Ship = it2.next();
				Ship p3Ship = it3.next();
				list += String.format("%10s", "› ");
				if (p1Ship.destroyed) {
					list += String.format("%-15s", "");
				} else {
					list += String.format("%-15s", p1Ship.name);
				}
				if (p2Ship.destroyed) {
					list += String.format("%-15s", "");
				} else {
					list += String.format("%-15s", p2Ship.name);
				}
				if (gameMode == 3) {
					if (p3Ship.destroyed) {
						list += String.format("%-15s", "");
					} else {
						list += String.format("%-15s", p3Ship.name);
					}
				}
				list += "\n";
			}
			if (gameMode == 1 || gameMode == 2) {
				list += String.format("%-10s%-30s", "", "__________________________");
			} else {
				list += String.format("%-10s%-40s %n", "", "_________________________________________");
			}
			System.out.println(list);
		}

		private void clearBoard() {
			for (int x = 0; x < boardSize; x++) {
				for (int y = 0; y < boardSize; y++) {
					board[x][y].piece = "_";
					board[x][y].used = false;
					board[x][y].type = 0;
				}
			}
			for (Ship ship : ships) {
				ship.placed = false;
				ship.location = "";
				ship.orientation = ' ';
			}
		}

		private boolean allPlaced() {
			for (Ship ship : ships) {
				if (!ship.placed) {
					return false;
				}
			}
			return true;
		}

		private void hideShips() {
			for (int x = 0; x < boardSize; x++) {
				for (int y = 0; y < boardSize; y++) {
					if (board[x][y].piece == "@") {
						board[x][y].piece = "_";
						board[x][y].hidden = true;

					}

				}
			}
			for (int i = 0; i < 30; i++) {
				System.out.println("\n");
			}
			printBoard();
		}

		private void revealShips() {
			for (int x = 0; x < boardSize; x++) {
				for (int y = 0; y < boardSize; y++) {
					if (board[x][y].hidden == true) {
						board[x][y].piece = "@";
						board[x][y].hidden = false;
					}
				}
			}
		}

		private void instructions() {
			System.out.println("\n First, Enter the number of the ship, "
					+ "\n Then enter the first square where you want the ship to be placed,"
					+ "\n Then select the orientation, H = horizontal, V = vertical."
					+ "\n\n Example: [1 B2 V] places a Patrol Boat starting on B2 going vertically\n"
					+ "\n NOTE: Ships CAN be placed adjacent to one another, but not overlap!"
					+ "\n\n Type 'R' to randomize the board"
					+ "\n Type 'C' to clear the board");
		}
	}

	public class Bot extends Player {
		public int IQ;
		public int targetType;
		public int direction;
		public int nextDirection;
		public String lastMove;
		public boolean justHit;
		public boolean reset;
		public boolean turnAround;

		Bot(int level) {
			switch (level) {
				case 1:
					name = "Hodor";
					break;
				case 2:
					name = "Wall-E";
					break;
				case 3:
					name = "C-3PO";
					break;
				case 4:
					name = "Mozart";
					break;
			}
			IQ = level;
			targetType = 0;
			direction = 0;
			nextDirection = 0;
			lastMove = "";
			justHit = false;
			reset = false;
			turnAround = false;
		}

		@Override
		protected void move() {
			String input = "";
			String valid = "";
			String message = "";

			our = turns.peekFirst();
			their = turns.peekLast();

			if (PvPvP) { // 3 Player game, all players left
				message += "\n ›" + name + "'s turn to target ";
				turns.pop();
				if (turn == 1) {
					if (shotOnce) {
						message += turns.peekLast().name + "‹";
					} else {
						message += turns.peekFirst().name + "‹";
					}
				} else {
					if (shotOnce) {
						message += turns.peekFirst().name + "‹";
					} else {
						message += turns.peekLast().name + "‹";
					}
				}
				turns.addFirst(this);
			} else { // 2 Players
				// Print this for player only games
				if (gameMode != 2) {
					message = "\n ›" + name + "'s turn";
					if (gameMode == 3) {
						message += " to target " + their.name;
					}
				}
			}
			System.out.println(message);

			do {
				input = calcMove();
				valid = checkMove(input);
			} while (valid != "ok");

			processMove(input);

			if (PvPvP) {
				// Players get two shots. If they've shot once it's the next player's turn
				if (shotOnce) {
					turns.pop();
					turns.addLast(this);
					// Next person in line
					turn = turns.peek().number;
					shotOnce = false;
					// Else they've now shot once
				} else {
					shotOnce = true;
				}
			} else {
				// Check if game is over only when PvP
				gameOver = checkWin();
				// Pop and put last in line
				turns.pop();
				turns.addLast(this);
				turn = turns.peek().number;
			}
		}

		protected String calcMove() {
			int nextRow = 0;
			int nextCol = 0;
			String nextMove = "";
			// IQ 1 = fire randomly
			if (IQ == 1) {
				int row = 0;
				int col = 0;
				row = random.nextInt(boardSize) + 65; // [0,boardSize)
				col = random.nextInt(boardSize) + 48;
				// row += 65; // 0=A ... 9=J
				// col += 48; // 0=0 ... 1=1
				String move = String.valueOf((char) row) + String.valueOf((char) col);
				return move;
			} else if (IQ == 2) {

				return getHint();

			} else if (IQ == 3) { // IQ 3

				if (hitList.isEmpty()) { // go random
					nextRow = random.nextInt(boardSize); // [0,boardSize)
					nextCol = random.nextInt(boardSize);
					nextRow += 65; // 0=A ... 9=J
					nextCol += 48; // 0=0 ... 1=1
					nextMove = String.valueOf((char) nextRow) + String.valueOf((char) nextCol);
					// Hitlist isn't empty
				} else {
					String first = hitList.get(targetType);
					// First hit position of active ship
					int firstRow = first.charAt(0);
					int firstCol = first.charAt(1);
					// Latest move
					String last = lastMove;
					int lastRow = last.charAt(0);
					int lastCol = last.charAt(1);
					Ship hitShip = new Ship();

					// Find the active ship
					for (Ship ship : their.ships) {
						if (ship.type == targetType) {
							hitShip = ship;
						}
						// Got it
					}
					// IF AI hit their previous shot
					if (justHit) {
						// Keep going from where they hit
						nextRow = lastRow;
						nextCol = lastCol;
						// else go to where they first did
					} else {
						nextRow = firstRow;
						nextCol = firstCol;
						// If targetted ship was only hit once
						if (hitShip.hitCount == 1) {
							// Keep resetting if we missed a target ship more than twice in a row
							if (reset) {
								nextDirection = random.nextInt(4);
								// We hit a wall or a used square, turn around
							} else {
								nextDirection = change(direction);
							}
							// It's been hit multiple times, turn around.
						} else {
							nextDirection = change(direction);
						}
					}
					nextMove = trace(nextRow, nextCol, nextDirection);
				}
				return nextMove;
			} else if (IQ == 4) {
				if (hitList.isEmpty()) {
					// Calculate Best location
					return getHint();
				} else {
					String first = hitList.get(targetType);
					// First hit position of active ship
					int firstRow = first.charAt(0);
					int firstCol = first.charAt(1);
					// Latest move
					String last = lastMove;
					int lastRow = last.charAt(0);
					int lastCol = last.charAt(1);
					Ship hitShip = new Ship();

					// Find the active ship
					for (Ship ship : their.ships) {
						if (ship.type == targetType) {
							hitShip = ship;
						}
						// Got it
					}
					// IF AI hit their previous shot
					if (justHit) {
						// Keep going from where they hit
						nextRow = lastRow;
						nextCol = lastCol;
						// else go to where they first did
					} else {
						nextRow = firstRow;
						nextCol = firstCol;
						// If targetted ship was only hit once
						if (hitShip.hitCount == 1) {
							// Keep resetting if we missed a target ship more than twice in a row
							if (reset) {
								nextDirection = random.nextInt(4);
								// We hit a wall or a used square, turn around
							} else {
								nextDirection = change(direction);
							}
							// It's been hit multiple times, turn around.
						} else {
							nextDirection = change(direction);
						}
					}
					nextMove = trace(nextRow, nextCol, nextDirection);
				}
			}
			return nextMove;
		}

		@Override
		protected String checkMove(String move) {
			if (move.length() != 2) {
				return " Invalid input.";
			}
			int row = (move.charAt(0)) - 65;
			int col = (move.charAt(1)) - 48;

			our = turns.pop();
			if (turn == 1) {
				if (our.shotOnce) {
					their = turns.peekLast();
				} else {
					their = turns.peekFirst();
				}
			} else { // P1 or P3
				if (our.shotOnce) {
					their = turns.peekFirst();
				} else {
					their = turns.peekLast();
				}
			}
			turns.addFirst(our);

			// AI is out of bounds
			if (row >= boardSize || row < 0 || col >= boardSize ||
					col < 0) {
				// If AI just hit, turn around before next move
				if (justHit) {
					turnAround = true;
				}
				reset = true;
				justHit = false;
				// As if we didnt hit
				return "Bad AI, bad";
			}
			// If the active ship is on the hitlist we know that AI wasn't
			// done and needs to reset
			// might end up here if we hit a missed square during twirl
			// but justHit = false fixes that
			if (their.board[row][col].selected && our.hitList.containsKey(targetType)) {
				// We're gonna send error message anyways
				// but next time AI moves we will have reset
				// and changed direction
				justHit = false;
				reset = true;
				return "AI no!";
			} else if (their.board[row][col].selected) {
				return "AI stop.";
			}
			return "ok";
		}

		@Override
		protected void processMove(String move) {
			int row = move.charAt(0) - 65;
			int col = move.charAt(1) - 48;
			char marker = ' ';
			String message = "";
			Ship hitShip = new Ship();
			int shipType = 0;

			if (PvPvP) {
				// This is to balance who gets hit
				our = turns.pop();
				if (turn == 1) {
					if (our.shotOnce) {
						their = turns.peekLast(); // p1>p3
					} else {
						their = turns.peekFirst(); // p1>p2
					}
				} else { // P2 or P3
					if (our.shotOnce) { // p2>p1 or p3>p2
						their = turns.peekFirst();
					} else {
						their = turns.peekLast(); // p2>p3 or p3>p1
					}
				}
				turns.addFirst(our);
			} else { // PvP
				our = turns.peekFirst();
				their = turns.peekLast();
			}
			shipType = their.board[row][col].type;

			// Last move to pass on
			String last = move;
			if (their.board[row][col].used) {
				marker = 'X';
				message = "Hit!";
				hits++;
				justHit = true;
				reset = false;

				// Add the ship to our hitList
				// If we hit a new ship, add it
				if (!our.hitList.containsKey(shipType)) {
					if (our.hitList.isEmpty()) {
						our.hitList.put(shipType, move);
						// Start targeting the first ship
						targetType = shipType;
					} else { // We hit a new ship
						our.hitList.put(shipType, move);
					}
				} else
					; // We hit an already hit ship

				// Remember what direction we went
				direction = nextDirection;

				// Find the hit ship
				for (Ship ship : their.ships) {
					if (ship.type == shipType) {
						hitShip = ship;
					}
				}
				// Check if enemy ship is sunk
				hitShip.hitCount++;
				if (hitShip.hitCount == hitShip.size) {
					hitShip.destroyed = true;
					their.destroyedShips++;
					our.sunkCount++;
					// Remove ship from hitList
					our.hitList.remove(shipType);

					// If more targets, target next
					if (!our.hitList.isEmpty()) {

						Iterator<Integer> it = our.hitList.keySet().iterator();
						// This is to ensure we sunk the correct ship
						if (targetType == shipType) {
							targetType = it.next();
							System.out.println("Now I target " + targetType);
						} else {// we rarely sink another ship
							System.out.println("next on list is " + targetType);
							;
						}
						// Our "last" move is where we first hit the next target
						// this will be the Bot's next starting point
						last = hitList.get(targetType);
						System.out.println("last: " + last);
						// The ship will only have been hit once, so twirl
						justHit = false;
						reset = true;
						// direction = random.nextInt(4);
					}
					// Checks if game is over
					if (their.destroyedShips == numOfShips) {
						their.gameIsOver = true;
					}
					message += "\n\n " + our.name + " destroyed ";
					if (gameMode == 2) {
						message += "your " + hitShip.name + "!";
					} else {
						message += their.name + "'s " + hitShip.name + "!";
					}
				}
			} else {
				marker = 'o';
				message = "Miss!";
				our.misses++;
				if (!justHit) {
					reset = true;
				}
				justHit = false;
			}
			their.board[row][col].piece = Character.toString(marker);
			their.board[row][col].selected = true;
			their.board[row][col].hidden = false;

			lastMove = last;
			// Reset turnAround trigger
			turnAround = false;
			// Don't print this if AI vs AI
			message = String.format("%-20s%s", " ›" + our.name + "'s move:", move + " - " + message);
			System.out.println(message);
		}

		private String trace(int row, int col, int direction) {
			String nextMove;
			switch (direction) {
				case 0:
					row++;
					break;
				case 1:
					row--;
					break;
				case 2:
					col++;
					break;
				case 3:
					col--;
					break;
			}
			nextMove = String.valueOf((char) row) + String.valueOf((char) col);
			return nextMove;
		}

		private int change(int dir) {
			if (dir % 2 == 0) { // 1 or 3, Up or Right
				dir++;
			} else { // 0 or 2, Down or Left
				dir--;
			}
			return dir;
		}
	}

	class Ship {
		public int size;
		public int type;
		public boolean placed;
		public boolean destroyed;
		public String location;
		public String name;
		public char orientation;
		public int hitCount;

		Ship(int t) {
			switch (t) {
				case 1:
					name = "Patrol Boat";
					size = 2;
					break;
				case 2:
					name = "Destroyer";
					size = 3;
					break;
				case 3:
					name = "Submarine";
					size = 3;
					break;
				case 4:
					name = "Battleship";
					size = 4;
					break;
				case 5:
					name = "Carrier";
					size = 5;
					break;
				default:
					name = "Hodor";
					size = 5;
			}
			type = t;
			placed = false;
			destroyed = false;
			location = "";
			orientation = ' ';
			hitCount = 0;
		}

		Ship() {
			placed = false;
			destroyed = false;
			location = "";
			orientation = ' ';
			hitCount = 0;
		}
	}

	class BoardPiece {
		public String piece;
		public boolean used;
		public boolean selected;
		public boolean hidden;
		public int type;
		public int prob;

		BoardPiece() {
			piece = "_";
			used = false;
			selected = false;
			hidden = false;
			type = 0;
			prob = 0;
		}
	}

}
