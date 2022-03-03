package com.sanderjochems.senet;

public class Board {

	private Square[] squares = new Square[30];

	/**
	 * Board constructor to prepare the board
	 * 
	 * @param senet
	 */
	public Board() {
		for (int i = 0; i < 30; i++) {
			squares[i] = Square.EMPTY;
		}
	}

	/**
	 * Setup the default locations of the pawns
	 */
	public void setupPawns(Gamemode gamemode) {
		switch (gamemode) {
		case TEST1:
			setSquare(1, Square.BLACK);
			setSquare(8, Square.BLACK);
			setSquare(16, Square.BLACK);
			setSquare(23, Square.BLACK);

			setSquare(2, Square.WHITE);
			setSquare(4, Square.WHITE);
			setSquare(6, Square.WHITE);
			setSquare(10, Square.WHITE);
			setSquare(12, Square.WHITE);
			setSquare(14, Square.WHITE);
			setSquare(17, Square.WHITE);
			setSquare(18, Square.WHITE);
			setSquare(20, Square.WHITE);
			setSquare(21, Square.WHITE);
			setSquare(24, Square.WHITE);
			setSquare(25, Square.WHITE);
			setSquare(26, Square.WHITE);
			break;
		case TEST2:
			setSquare(29, Square.BLACK);

			setSquare(22, Square.WHITE);
			setSquare(23, Square.WHITE);
			setSquare(24, Square.WHITE);
			break;
		case TEST3:
			setSquare(13, Square.BLACK);
			setSquare(25, Square.BLACK);
			setSquare(26, Square.BLACK);
			setSquare(28, Square.BLACK);
			setSquare(29, Square.BLACK);

			setSquare(6, Square.WHITE);
			setSquare(18, Square.WHITE);
			setSquare(22, Square.WHITE);
			break;

		default:
			setSquare(2, Square.BLACK);
			setSquare(4, Square.BLACK);
			setSquare(6, Square.BLACK);
			setSquare(8, Square.BLACK);
			setSquare(11, Square.BLACK);

			setSquare(1, Square.WHITE);
			setSquare(3, Square.WHITE);
			setSquare(5, Square.WHITE);
			setSquare(7, Square.WHITE);
			setSquare(9, Square.WHITE);
			break;
		}

	}

	/**
	 * Verify whether the player can move any of his pieces a given positions
	 * forward
	 * 
	 * @param player    the player you want to check for
	 * @param positions the amount of squares the pawn wants to move
	 * @return
	 */
	public boolean canPlay(Player player, int positions) {
		for (int i = 1; i <= 30; i++) {
			Square square = getSquare(i);

			if (player.getColor() == square && checkMove(player, i, positions, false))
				return true;
		}

		return false;
	}

	/**
	 * Verify whether it is possible to move the player from the current location to
	 * the next location
	 * 
	 * @param player    the player you want to check for
	 * @param location  the current location of the chosen pawn
	 * @param positions the amount of squares the pawn wants to move
	 * @return
	 */
	public boolean checkMove(Player player, int location, int positions) {
		return checkMove(player, location, positions, true);
	}

	/**
	 * Verify whether it is possible to move the player from the current location to
	 * the next location
	 * 
	 * @param player    the player you want to check for
	 * @param location  the current location of the chosen pawn
	 * @param positions the amount of squares the pawn wants to move
	 * @param messages  whether you want to get in the console
	 * @return
	 */
	public boolean checkMove(Player player, int location, int steps, boolean messages) {
		int nextLocation = location + steps;

		if (!(location >= 1 && location <= 30)) {
			if (messages)
				System.out.println(String.format("Illegal square: %d", location));
			return false;
		}

		if (!(nextLocation >= 1 && nextLocation <= 30)) {
			if (messages)
				System.out.println(String.format("Illegal destination: %d", nextLocation));
			return false;
		}

		Square squareNow = getSquare(location);
		Square squareNext = getSquare(nextLocation);

		if (squareNow != player.getColor()) {
			if (messages)
				System.out.println(String.format("You don't have a piece on square %d", location));
			return false;
		}

		if (squareNext == player.getColor()) {
			if (messages)
				System.out.println(String.format("One of your own pieces occupies square %d", nextLocation));
			return false;
		}

		Square otherPlayerSquare = player.getColor() == Square.BLACK ? Square.WHITE : Square.BLACK;

		if (nextLocation == 26 || nextLocation == 28 || nextLocation == 29) {
			if (squareNext == otherPlayerSquare) {
				if (messages)
					System.out.println(String.format("You can't attack the other player on square %d", nextLocation));
				return false;
			}
		}

		if (nextLocation == 30) {
			for (int i = 1; i <= 20; i++) {
				if (getSquare(i) == player.getColor()) {
					if (messages)
						System.out.println("Not all your pieces are on the final row");
					return false;
				}
			}
		}

		if (squareNext == otherPlayerSquare) {
			if (getSquare(nextLocation - 1) == otherPlayerSquare || getSquare(nextLocation + 1) == otherPlayerSquare) {
				if (messages)
					System.out.println(String.format("Attack on safe piece: %d", nextLocation));
				return false;
			}
		}

		if (steps > 3) {
			int otherPlayerPieces = 0;

			for (int i = (location + 1); i <= nextLocation; i++) {
				if (getSquare(i) == otherPlayerSquare) {
					otherPlayerPieces++;
				} else {
					otherPlayerPieces = 0;
				}
			}

			if (otherPlayerPieces >= 3) {
				if (messages)
					System.out.println("Attempt to jump over blockade");
				return false;
			}
		}

		return true;
	}

	/**
	 * Move the pawn on the location the amount of positions ahead
	 * 
	 * @param location  the location you want to move the pawn from
	 * @param positions the amount of squares the pawn has to move
	 */
	public void move(int location, int steps) {
		int nextLocation = location + steps;

		Square squareNow = getSquare(location);
		Square squareNext = getSquare(nextLocation);

		if (nextLocation == 27) {
			for (int i = 1; i <= 30; i++) {
				if (getSquare(i) == Square.EMPTY) {
					setSquare(i, squareNow);
					setSquare(location, Square.EMPTY);
					return;
				}
			}
		}

		if (nextLocation == 30) {
			setSquare(location, Square.EMPTY);
			return;
		}

		if (squareNext != Square.EMPTY)
			System.out.println(String.format("Attack on safe piece: %d", nextLocation));

		setSquare(location, squareNext);
		setSquare(nextLocation, squareNow);
	}

	/**
	 * Check if the given player has won the game
	 * 
	 * @param player the player to check for
	 * @return wether the given player has won the game
	 */
	public boolean checkWinner(Player player) {
		for (int i = 1; i <= 30; i++) {
			if (getSquare(i) == player.getColor()) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Print the board to the out
	 */
	public void print() {
		System.out.println("+----------+");

		System.out.print("|");
		for (int i = 1; i <= 10; i++) {
			System.out.print(getPrintChar(i));
		}
		System.out.println("|");

		System.out.print("|");
		for (int i = 20; i >= 11; i--) {
			System.out.print(getPrintChar(i));
		}
		System.out.println("|");

		System.out.print("|");
		for (int i = 21; i <= 30; i++) {
			System.out.print(getPrintChar(i));
		}
		System.out.println("|");

		System.out.println("+----------+");
	}

	/**
	 * Get the character for the given location
	 * 
	 * @param location the location you want the character for
	 * @return
	 */
	private String getPrintChar(int location) {
		switch (getSquare(location)) {
		case BLACK:
			return "x";
		case WHITE:
			return "o";
		default:
			return ".";
		}
	}

	/**
	 * Get the square by the location
	 * 
	 * @param location the location you want the square for
	 * @return
	 */
	private Square getSquare(int location) {
		if (location >= 1 && location <= 30)
			return squares[location - 1];
		return null;
	}

	/**
	 * Set the square by the location
	 * 
	 * @param location the location you want the square for
	 * @param square   the square you want set for the square
	 */
	private void setSquare(int location, Square square) {
		squares[location - 1] = square;
	}

}
