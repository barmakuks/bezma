package com.fairbg.core.model;

import com.fairbg.core.commands.UserCommand;

/**
 * This class contains all game logic and rules
 * To implement Rules for game inherit this abstract class
 * @author Vitalik
 *
 */
public abstract class GameBox {

	public static class Parameters{
		
	}

	public static GameBox createBox(Parameters gameParams) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setState(ModelState m_ModelState) {
		// TODO Auto-generated method stub
		
	}

	public ModelCommand processCommand(UserCommand command) {
		// TODO Auto-generated method stub
		return null;
	}

}
