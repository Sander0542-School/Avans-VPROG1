package com.sanderjochems.senet;

public class Player {
	
	private String name;
	private Square color;
	
	/**
	 * Create a new object of the player
	 * 
	 * @param square
	 */
	public Player(Square square) 
	{
		this.color = square;
		this.name = square.toString().toLowerCase();
	}
	
	/**
	 * Create a new object of the player
	 * 
	 * @param square
	 */
	public Player(Square square, String name) 
	{
		this.color = square;
		this.name = name;
	}

	/**
	 * Get the name of the player
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Get the color of the player
	 * 
	 * @return
	 */
	public Square getColor() {
		return color;
	}
	
	/**
	 * Get the color as a X or O
	 * 
	 * @return
	 */
	public String getColorSign()
	{
		return this.color == Square.BLACK ? "x" : "o";
	}

}
