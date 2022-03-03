package com.sanderjochems.senet;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Senet {

	private Player player1 = new Player(Square.BLACK);
	private Player player2 = new Player(Square.WHITE);

	private Dice dice = new Dice();

	private Board board = new Board();

	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

	/**
	 * Display a welcome message
	 */
	public Senet() {
		System.out.println("Welcome to Senet!");
	}

	/**
	 * Start the game
	 */
	public void play() {

		Gamemode gamemode = gamemodeSetup();

		if (gamemode == Gamemode.NORMAL)
			playerSetup();

		board.setupPawns(gamemode);

		board.print();

		if (gamemode == Gamemode.NORMAL)
			turn(player2, 9);

		while (true) {
			turn(player1);
			if (board.checkWinner(player1)) {
				finish(player1);
				break;
			}

			turn(player2);
			if (board.checkWinner(player2)) {
				finish(player2);
				break;
			}
		}
	}

	/**
	 * Let the given player make a turn
	 * 
	 * @param player the player that is going to make a turn
	 */
	private void turn(Player player) {
		turn(player, null);
	}

	/**
	 * Let the given player make a turn
	 * 
	 * @param player   the player that is going to make a turn
	 * @param location the location the player needs to move his piece from if not
	 *                 null
	 */
	private void turn(Player player, Integer location) {
		System.out.print(String.format("%s (%s), press <ENTER> to throw the dice", player.getName(), player.getColorSign()));
		try {
			br.readLine();
		} catch (Exception e) {
		}
		int diceThrow = dice.throwSticks();
		System.out.println(String.format("%s (%s), you have thrown %d", player.getName(), player.getColorSign(), diceThrow));

		if (location != null) {
			board.move(location, diceThrow);
			board.print();
		} else if (!board.canPlay(player, diceThrow)) {
			System.out.println("You can't move anything, your turn is over");
		} else {
			while (true) {

				while (location == null) {
					try {
						System.out.print(String.format("%s (%s), which piece do you want to move? -> ", player.getName(), player.getColorSign()));
						location = Integer.parseInt(br.readLine());
					} catch (Exception e) {
						System.out.println("Could not select the piece, please try again");
					}
				}

				if (board.checkMove(player, location, diceThrow)) {
					board.move(location, diceThrow);
					break;
				}

				location = null;
			}

			board.print();
		}

		if ((diceThrow == 1 || diceThrow == 4 || diceThrow == 6) && !board.checkWinner(player)) {
			System.out.println(String.format("%s (%s), you have thrown %d, so you can make another turn", player.getName(), player.getColorSign(), diceThrow));
			turn(player);
		}

	}

	/**
	 * Finish the game by letting the player know
	 * 
	 * @param player the player that has won the game
	 */
	private void finish(Player player) {
		System.out.println(String.format("%s (%s), has won the game!", player.getName(), player.getColorSign()));
	}

	/**
	 * Ask the player which gamemode they to use
	 * 
	 * @return the selected gamemode
	 */
	private Gamemode gamemodeSetup() {
		while (true) {
			try {
				System.out.print("Would you like to start a normal game (0) or a test position (1-3)? -> ");

				int result = Integer.parseInt(br.readLine());

				switch (result) {
				case 0:
					return Gamemode.NORMAL;
				case 1:
					return Gamemode.TEST1;
				case 2:
					return Gamemode.TEST2;
				case 3:
					return Gamemode.TEST3;
				}
			} catch (Exception e) {
			}

			System.out.println("Please select a valid gamemode");
		}
	}

	/**
	 * Setup the names of the players
	 */
	private void playerSetup() {

		String player1 = requestName("first");
		String player2 = requestName("second");

		while (true) {
			if (playerStart(player1, player2))
				break;

			if (playerStart(player2, player1))
				break;
		}

		System.out.println(String.format("%s starts the game", this.player1.getName()));
	}

	/**
	 * Request the name of the given player
	 * 
	 * @param type the type of player
	 * @return
	 */
	private String requestName(String type) {
		String name = null;

		while (name == null) {
			try {
				System.out.print(String.format("Enter the name of the %s player: ", type));
				name = br.readLine();
			} catch (Exception e) {
				System.out.println("There was an error entering your name, please try again");
			}
		}

		return name;
	}

	/**
	 * Determine which player can start the game
	 * 
	 * @param thrower
	 * @param other
	 * @return
	 */
	private boolean playerStart(String thrower, String other) {
		int diceThrow = dice.throwSticks();
		System.out.println(String.format("%s has trown %d", thrower, diceThrow));

		if (diceThrow == 1) {
			player1 = new Player(Square.BLACK, thrower);
			player2 = new Player(Square.WHITE, other);

			return true;
		}

		return false;
	}

}
