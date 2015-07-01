import java.util.*;

public class Battle {
	public static Scanner scan = new Scanner(System.in);
	public Random random = new Random(System.currentTimeMillis());
	public static int turn;
	public static int gameMode;
	public static int boardSize;
	public static int numOfShips;
	public static int nextDirection;
	public static boolean PvPvP;
	public static boolean gameOver;
	public static boolean autoPlace;
	public static boolean firstGame;
	public LinkedList<Player> turns;
	public Player p1 = new Player();
	public Player p2 = new Player();
	public Player p3 = new Player();
	public Bot AI, AI2;

	public static void main(String[] args) {
		Battle game = new Battle();
		firstGame = true;
		String again = "";

		System.out.println(
				"\n\t WELCOME TO BATTLESHIPS!\n\n" +
						" ›Enter the corresponding number to make a selection‹\n");
		do {
			game.play();
			System.out.println("\n Do you want to play again? (Y/N):");
			again = scan.nextLine();
			firstGame = false;
		} while (again.charAt(0) == 'Y');

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
				System.out.println("\n Both players are ready!\n" +
						" Let the battle begin!"
						+ "\n\n Enter your moves in the form 'D2'");
				break;
			case 2:
				printBoard();
				p1.placeShips();
				autoPlace = true;
				p2.AIplaceShips();
				p2.hideShips();
				System.out.println("\n Your enemy has placed out their ships!"
						+ "\n Let the battle begin!"
						+ "\n\n Enter your moves in the form 'D2'");
				break;
			case 3:
				printBoard();
				p1.placeShips();
				p1.hideShips();
				p2.placeShips();
				p2.hideShips();
				p3.placeShips();
				p3.hideShips();
				System.out.println("\n All players are ready,"
						+ " let the battle begin!\n\n"
						+ " You get 2 shots per round, 1 per opponent\n"
						+ " At any time during the game, type 'S' to see all remaining ships\n"
						+ " Enter your moves in the form 'B3'");
				break;
			case 4:
				printBoard();
				autoPlace = true;
				p1.AIplaceShips();
				p2.AIplaceShips();
				printBoard();
				System.out.println(" A good old Bot Battle");
				break;
		}
		while (!gameOver) {
			if (gameMode == 1 || gameMode == 2) {
				if (turn == 1) {
					p1.move();
				} else {
					p2.move();
				}
			} else if (gameMode == 3) { // 3 Player Game
				if (turn == 1) {
					p1.move();
				} else if (turn == 2) {
					p2.move();
				} else {
					p3.move();
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
						"\t 2: Player vs Computer\n" +
						"\t 3: 3 Player Game\n" +
						// "\t 4: Computer vs Computer\n" +
						"\t______________________");
		p1 = new Player();
		p2 = new Player();
		p3 = new Player();
		p1.number = 1;
		p2.number = 2;
		p3.number = 3;
		turns = new LinkedList<Player>();

		while (!chosenMode) {
			try {
				char mode = scan.nextLine().charAt(0);
				switch (mode) {
					case '1': // Player vs Player
						gameMode = 1;
						PvPvP = false;
						String name = "";
						System.out.println("Enter name of Player 1: ");
						// boolean invalid = true;
						while (!p1.ready) {
							name = scan.nextLine();
							if (name.isEmpty() || name.length() > 7) {
								System.out.println(" Please enter a name between 1-7 characters");
							} else {
								p1.name = name;
								p1.ready = true;
							}
							System.out.println("Enter name of Player 2: ");
							while (!p2.ready) {
								name = scan.nextLine();
								if (name.isEmpty() || name.length() > 7) {
									System.out.println(" Please enter a name between 1-7 characters");
								} else {
									p2.name = name;
									p2.ready = true;
								}
							}
						}
						turns.add(p1);
						turns.add(p2);
						chosenMode = true;
						break;
					case '2': // Player vs Bot
						PvPvP = false;
						gameMode = 2;
						System.out.println(" Enter your name: ");
						while (!p1.ready) {
							name = scan.nextLine();
							if (name.isEmpty() || name.length() > 7) {
								System.out.println(" Please enter a name between 1-7 characters");
							} else {
								p1.name = name;
								p1.ready = true;
							}
						}
						System.out.println(
								"\t____Computer_Level____\n" +
										"\t 1: So Dumb\n\t 2: Worthy Opponent\n" +
										"\t______________________");
						while (!p2.ready) {
							char iq = scan.nextLine().charAt(0);
							switch (iq) {
								case '1':
									AI = new Bot(1);
									p2.ready = true;
									break;
								case '2':
									AI = new Bot(2);
									p2.ready = true;
									break;
								default:
									System.out.println("Enter 1 or 2");
							}
						}
						p2 = AI;
						p2.number = 2;
						turns.add(p1);
						turns.add(p2);
						chosenMode = true;
						break;
					case '3': // 3 Player Game
						gameMode = 3;
						PvPvP = true;
						name = "";
						System.out.println(" Enter name of Player 1: ");
						while (!p1.ready) {
							name = scan.nextLine();
							if (name.isEmpty() || name.length() > 7) {
								System.out.println(" Please enter a name between 1-7 characters");
							} else {
								p1.name = name;
								p1.ready = true;
							}
						}
						System.out.println(" Enter name of Player 2: ");
						while (!p2.ready) {
							name = scan.nextLine();
							if (name.isEmpty() || name.length() > 7) {
								System.out.println(" Please enter a name between 1-7 characters");
							} else {
								p2.name = name;
								p2.ready = true;
							}
						}
						System.out.println(" Enter name of Player 3: ");
						while (!p3.ready) {
							name = scan.nextLine();
							if (name.isEmpty() || name.length() > 7) {
								System.out.println(" Please enter a name between 1-7 characters");
							} else {
								p3.name = name;
								p3.ready = true;
							}
						}
						turns.add(p1);
						turns.add(p2);
						turns.add(p3);
						chosenMode = true;
						break;

					case '4': // Bot vs Bot, not fully implemented
						gameMode = 4;
						AI = new Bot(2);
						AI2 = new Bot(2);
						AI2.name = "DeepRed";
						p1 = AI;
						p2 = AI2;
						chosenMode = true;
						break;
					default:
						System.out.println(" Enter 1,2 or 3");
				}
			} catch (StringIndexOutOfBoundsException e) {
				System.out.println(" Enter something");
			}
		}
		System.out.println(
				"\t______Board_Size______\n" +
						"\t 1: Small\n\t 2: Medium\n\t 3: Large\n" +
						"\t______________________\n");
		boolean chosenSize = false;
		while (!chosenSize) {
			char size = scan.nextLine().charAt(0);
			switch (size) {
				case '1':
					numOfShips = 3;
					boardSize = 6;
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
		p1.ready = false;
		p2.ready = false;
		p3.ready = false;
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
				if (gameMode == 3) {
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

	class Player {
		public String name;
		public BoardPiece[][] board;
		public List<Ship> ships;
		public LinkedHashMap<Integer, String> hitList;
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
			name = "PLAYER";
			board = new BoardPiece[boardSize][boardSize];
			ships = new ArrayList<Ship>();
			hitList = new LinkedHashMap<Integer, String>();
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
								printBoard();
								break;
							case 'R':
								autoPlace = true;
								clearBoard();
								AIplaceShips();
								printBoard();
								autoPlace = false;
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
						int number = input.charAt(0) - 49; // = 1,2,3,4,5
						int row = input.charAt(2) - 65; // A=0 ... J=9
						int col = input.charAt(3) - 48; // 0=0 ... 9=9
						char layout = input.charAt(5); // H or V

						if (number > (numOfShips - 1) || number < 0) { // Ship number between 1 and
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
							if (layout == 'H' && ships.get(number).size > (boardSize - col)) {
								System.out.println(" Invalid Horizontal Placement. Not enough room.");
								// Doesn't fit
							} else if (layout == 'V' && ships.get(number).size > (boardSize - row)) {
								System.out.println(" Invalid Vertical Placement. Not enough room.");
								// Valid placement! Except overlap
							} else {
								placeOnBoard(this, layout, ships.get(number), input.substring(2, 4));
								printBoard();
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
					}
					else {
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

		private void move() {
			String input = "";
			String valid = "";
			String message = "";

			// Player only game
			if (gameMode == 1 || gameMode == 3) {

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

					System.out.println(message);
					do {
						input = "";
						valid = "";
						System.out.println(" Enter move: ");
						input = scan.nextLine();
						if (input.charAt(0) == 'S') {
							shipsLeft();
						} else {
							valid = checkMove(input);
						}
						if (valid != "ok") {
							System.out.println(valid);
						}
						// Until valid input
					} while (valid != "ok");

					processMove(input);
					printBoard();

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

				} else { // PvP
					message = "\n ›" + name + "'s turn";
					if (gameMode == 3) {
						message += " to target " + turns.peekLast().name;
					}
					System.out.println(message);
					do {
						input = "";
						valid = "";
						System.out.println(" Enter move: ");
						input = scan.nextLine();
						if (input.charAt(0) == 'S') {
							shipsLeft();
						} else {
							valid = checkMove(input);
						}
						if (valid != "ok") {
							System.out.println(valid);
						}
						// Until valid input
					} while (valid != "ok");

					processMove(input);
					gameOver = checkWin();
					printBoard();

					// Pop and put last in line
					turns.pop();
					turns.addLast(this);
					turn = turns.peek().number;
				}
				// Player vs Bot
			} else if (gameMode == 2) {

				do {
					if (turn == 1) {
						System.out.println(" Enter move: ");
						input = scan.nextLine();
					} else {
						input = AImove();
					}
					// displays shipsLeft
					if (input.charAt(0) == 'S') {
						shipsLeft();
					} else {
						valid = checkMove(input);
					}
					// We don't want to se AI errors
					if (turn == 1 && valid != "ok") {
						System.out.println(valid);
					}
					// Until valid input
				} while (valid != "ok");

				processMove(input);
				gameOver = checkWin();

				if (turn != 1) {
					printBoard();
				}
				turns.pop();
				turns.addLast(this);
				turn = turns.peek().number;
			}
		}

		private String AImove() {
			// IQ 1 = fire randomly
			if (AI.IQ == 1) {
				int row = 0;
				int col = 0;
				row = random.nextInt(boardSize) + 65; // [0,boardSize)
				col = random.nextInt(boardSize) + 48;
				// row += 65; // 0=A ... 9=J
				// col += 48; // 0=0 ... 1=1
				String move = String.valueOf((char) row) + String.valueOf((char) col);
				return move;
			} else { // IQ 2
				int nextRow = 0;
				int nextCol = 0;
				String nextMove = "";

				if (p2.hitList.isEmpty()) { // go random
					nextRow = random.nextInt(boardSize); // [0,boardSize)
					nextCol = random.nextInt(boardSize);
					nextRow += 65; // 0=A ... 9=J
					nextCol += 48; // 0=0 ... 1=1
					nextMove = String.valueOf((char) nextRow) + String.valueOf((char) nextCol);
					// Hitlist isn't empty
				} else {
					String first = p2.hitList.get(AI.targetType);
					// First hit position of active ship
					int firstRow = first.charAt(0);
					int firstCol = first.charAt(1);
					// Latest move
					String last = AI.lastMove;
					int lastRow = last.charAt(0);
					int lastCol = last.charAt(1);
					Ship hitShip = new Ship();

					// Find the active ship
					for (Ship ship : p1.ships) {
						if (ship.type == AI.targetType) {
							hitShip = ship;
						}
						// Got it
					}
					// IF AI hit their previous shot
					if (AI.justHit) {
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
							if (AI.reset) {
								nextDirection = random.nextInt(4);
								// We hit a wall or a used square, turn around
							} else {
								nextDirection = AI.change(AI.direction);
							}
							// It's been hit multiple times, turn around.
						} else {
							nextDirection = AI.change(AI.direction);
						}
					}
					nextMove = AI.traverse(nextRow, nextCol, nextDirection);
				}
				return nextMove;
			}
		}

		private String checkMove(String move) {
			// Won't process until ok
			if (move.length() != 2) {
				return " Invalid input.";
			}
			int row = (move.charAt(0)) - 65;
			int col = (move.charAt(1)) - 48;

			// Player vs Player
			if (gameMode == 1 || gameMode == 3) {
				Player our = new Player();
				Player their = new Player();

				our = turns.pop();
				if (turn == 1) { // To avoid multiple hits
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
				// Player vs Bot
			} else if (gameMode == 2) {

				if (turn == 1) {
					if (row >= boardSize || row < 0) {
						return " Invalid row. ";
					}
					if (col >= boardSize || col < 0) {
						return " Invalid column.";
					}
					if (p2.board[row][col].selected) {
						return " Already chosen.";
					}
					// AI makes a mistake
				} else {
					// AI is out of bounds
					if (row >= boardSize || row < 0 || col >= boardSize ||
							col < 0) {
						// If AI just hit, turn around before next move
						if (AI.justHit) {
							AI.turnAround = true;
						}
						AI.reset = true;
						AI.justHit = false;
						// As if we didnt hit
						return "Bad AI, bad";
					}
					// If the active ship is on the hitlist we know that AI wasn't
					// done and needs to reset
					// might end up here if we hit a missed square during twirl
					// but AI.justHit = false fixes that
					if (p1.board[row][col].selected && p2.hitList.containsKey(AI.targetType)) {
						// We're gonna send error message anyways
						// but next time AI moves we will have reset
						// and changed direction
						AI.justHit = false;
						AI.reset = true;
						return "AI no!";
					}
					else if (p1.board[row][col].selected) {
						return "AI stop.";
					}
				}
			}
			return "ok";
		}

		private void processMove(String move) {
			int row = move.charAt(0) - 65;
			int col = move.charAt(1) - 48;
			char marker = ' ';
			String message = "";
			Player our = new Player();
			Player their = new Player();
			Ship hitShip = new Ship();
			int shipType = 0;

			// Players only Game
			if (gameMode == 1 || gameMode == 3) {

				if (gameMode == 1) {
					our = turns.peek();
					their = turns.peekLast();
					shipType = their.board[row][col].type;
				}
				else if (gameMode == 3) {

					our = turns.pop();
					if (turn == 1) { // To avoid multiple hits
						if (our.shotOnce) {
							their = turns.peekLast();
							shipType = their.board[row][col].type;
						} else {
							their = turns.peekFirst();
							shipType = their.board[row][col].type;
						}
					} else { // P1 or P3
						if (our.shotOnce) {
							their = turns.peekFirst();
							shipType = their.board[row][col].type;
						} else {
							their = turns.peekLast();
							shipType = their.board[row][col].type;
						}
					}
					turns.addFirst(our);
				}

				if (their.board[row][col].used) {
					marker = 'X';
					message = "Hit!";
					our.hits++;
					// Find the hit ship
					for (Ship ship : their.ships) {
						if (ship.type == shipType) {
							hitShip = ship;
						}
					}
					hitShip.hitCount++;

					if (hitShip.hitCount == hitShip.size) {
						hitShip.destroyed = true;
						their.destroyedShips++;
						our.sunkCount++;
						message += "\n " + our.name + " destroyed " + their.name + "'s " + hitShip.name + "!";

						// Displays Ships left, not if all are sunk
						if (their.destroyedShips != numOfShips) {
							message += "\n\t_______Ships left_______\n";
							for (Ship left : their.ships) {
								if (!left.destroyed) {
									message += "\t " + left.name + "\t- " + left.size + " spots\n";
								}
							}
						} else {
							their.gameIsOver = true;
							shotOnce = true;
							PvPvP = false;
							// PvPvP to PvP
							if (gameMode == 3 && turns.size() == 3) {
								turns.remove(their);
								message += "\n " + their.name + " has no ships left!";
							}
						}
					}
					their.board[row][col].hidden = false;
				} else {
					marker = 'o';
					message = "Miss!";
					our.misses++;
				}
				their.board[row][col].piece = Character.toString(marker);
				their.board[row][col].selected = true;

				message = " ›" + name + "'s move:\t" + move + " - " + message;
				System.out.println(message);
			}

			// Player vs Bot
			else if (gameMode == 2) {
				our = turns.peekFirst();
				their = turns.peekLast();
				shipType = their.board[row][col].type;

				// Last move to pass on
				String lastMove = move;
				// if (turn == 1) {
				if (their.board[row][col].used) {
					marker = 'X';
					message = "Hit!";
					our.hits++;
					// //
					if (turn == 2) {
						AI.justHit = true;
						AI.reset = false;
						// If we hit a new ship, add it
						if (!our.hitList.containsKey(shipType)) {
							if (our.hitList.isEmpty()) {
								our.hitList.put(shipType, move);
								// Start targeting the first ship
								AI.targetType = shipType;
							} else { // We hit a new ship
								our.hitList.put(shipType, move);
							}
						} else
							; // We hit an already hit ship
						// Remember what direction we went
						AI.direction = nextDirection;
					}
					// //////

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
						if (turn == 1) {
							message += "\n You destroyed their " + hitShip.name + "!";

							// Displays Ships left, don't do if all are sunk
							if (their.destroyedShips != numOfShips) {
								message += "\n\t_______Ships left_______\n";
								for (Ship left : their.ships) {
									if (!left.destroyed) {
										message += "\t " + left.name + "\t- " + left.size + " spots\n";
									}
								}
							}
						} else {
							message += "\n They destroyed your " + hitShip.name + "!";
							// Remove fron HitList
							AI.hitList.remove(shipType);
							// If more targets, target next
							if (!AI.hitList.isEmpty()) {
								Iterator<Integer> it = AI.hitList.keySet().iterator();
								AI.targetType = it.next();
								// Our "last" move is where we first hit the next target
								// this will be the Bot's next starting point
								lastMove = hitList.get(AI.targetType);
								// The ship will only have been hit once, so twirl
								AI.direction = random.nextInt(4);
								AI.justHit = false;
								AI.reset = true;
							}
						} // Checks if game is over
						if (their.destroyedShips == numOfShips) {
							their.gameIsOver = true;
						}
					}
				} else {
					marker = 'o';
					message = "Miss!";
					our.misses++;
					if (turn == 2) {
						if (!AI.justHit) {
							AI.reset = true;
						}
						AI.justHit = false;
					}
				}
				their.board[row][col].piece = Character.toString(marker);
				their.board[row][col].selected = true;
				their.board[row][col].hidden = false;

				if (turn == 2) {
					AI.lastMove = lastMove;
					// Reset turnAround trigger
					AI.turnAround = false;
					message = " ›Enemy's move:\t" + move + " - " + message;
				} else {
					message = " ›Your move:\t" + move + " - " + message;
				}
				System.out.println(message);
			}
		}

		public boolean checkWin() {
			Player our = new Player();
			Player their = new Player();

			our = turns.peekFirst();
			their = turns.peekLast();

			if (their.gameIsOver) {
				p1.revealShips();
				p2.revealShips();

				if (gameMode == 1) {
					System.out.println(
							"\n Congratulations " + our.name + "! You've destroyed all of " + their.name
									+ "'s ships!");
				} else if (gameMode == 2) {
					if (turn == 1) {
						printBoard();
						System.out.println(
								"\n Congratulations " + our.name + "! You've destroyed all enemy ships!");
					} else {
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

	class Bot extends Player {
		public int IQ;
		public boolean justHit;
		public boolean reset;
		public boolean turnAround;
		public String lastMove;
		public int targetType;
		public int direction;

		Bot(int level) {
			name = "Anna";
			IQ = level;
			justHit = false;
			reset = false;
			turnAround = false;
			lastMove = "";
			targetType = 0;
			direction = 0;
		}

		public String traverse(int row, int col, int direction) {
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
		public int type;
		public boolean hidden;

		BoardPiece() {
			piece = "_";
			used = false;
			selected = false;
			type = 0;
			hidden = false;
		}
	}

}
