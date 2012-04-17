package com.fairbg.core.model;

import com.fairbg.bezma.communication.commands.UserCommand;
import com.fairbg.bezma.store.IDatabase;

/**
 * This class contains all game logic and rules
 * To implement Rules for game inherit this abstract class
 *
 */
public class GameBox {

	ModelState m_State;
	Match m_Match;
	
	public GameBox(Match aMatch)
	{
		m_State = new ModelState();
		m_Match = aMatch;
	}
	
	public ModelState getState() {
		return m_State;
	}

	public void writeCurrentState() {
		// TODO Auto-generated method stub
		
	}

	public void restore(IDatabase m_Storage) {
		// TODO Auto-generated method stub
		
	}

	public boolean processCommand(UserCommand command) {
		m_State.processCommand(command);
		return true;
	}

}