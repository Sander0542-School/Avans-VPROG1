package com.sanderjochems.senet;

import java.util.Random;

public class Dice {
	
	Random rnd = new Random();
	
	public int throwSticks()
	{
		int whiteSticks = 0;
		
		for (int i = 0; i < 4; i++) {
			whiteSticks += rnd.nextBoolean() ? 1 : 0;
		}
		
		return whiteSticks == 0 ? 6 : whiteSticks;
	}

}
