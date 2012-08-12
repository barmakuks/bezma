package com.fairbg.bezma.core.model;

import java.util.HashMap;
import java.util.Map;

import com.fairbg.bezma.store.IDatabase;

/**
 * This class contains all game logic and rules
 * To implement Rules for game inherit this abstract class
 *
 */
public class GameBox {

	/// Отображает текущее состояние игры
	private ModelState m_ModelState;
	
	private BackgammonRules m_GameRules;
	
	private Map<AutomatStates, IAutomatState> m_AutomatStates = new HashMap<AutomatStates, IAutomatState>();
	private IAutomatState m_AutomatState;

	
	/// Текущий матч
	private Match m_Match;
	
	public GameBox(Match aMatch)
	{
		m_ModelState = null;
		m_Match = aMatch;
		m_GameRules = new BackgammonRules();
		
		m_AutomatStates.put(AutomatStates.BEGIN, new BeginState());
		m_AutomatStates.put(AutomatStates.MOVE, new MoveState());
		m_AutomatStates.put(AutomatStates.DICE, new DiceState());
		
		m_AutomatState = selectAutomatState(AutomatStates.BEGIN);
		
	}
	
	public BackgammonRules getGameRules() {
		return m_GameRules;
	}

	public ModelState getModelState() {
		return m_ModelState;
	}
	
	public void setModelState(ModelState state)
	{
		m_ModelState = state;		
	}

	public void writeCurrentState() {
		// TODO Auto-generated method stub
		
	}

	public void restore(IDatabase m_Storage) {
		// TODO Auto-generated method stub
		
	}

	public boolean processCommand(ModelCommand modelCommand) {
		return m_AutomatState.processCommand(this, modelCommand);
	}

	public Match getMatch(){
		return m_Match;
	}

	public boolean checkStartPosition(Position position) {
		return m_GameRules.checkStartPosition(position);		
	}
	
	public IAutomatState selectAutomatState(AutomatStates state)
	{
		return m_AutomatStates.get(state);
	}
	

	public IAutomatState getCurrentAutomatState()
	{
		return m_AutomatState;
	}
	
	public void setCurrentAutomatState(IAutomatState state)
	{
		m_AutomatState = state;
	}

	public Move findMove(Position destPosition, PlayerColors player) 
	{
		int die1 = 0;
		int die2 = 0;
		return m_GameRules.findMove(die1, die2, m_ModelState.getPosition(), destPosition, player);
	}

	public void appendMove(Move move) {
		m_Match.appendMove(move);
		// TODO Auto-generated method stub
		
	}
	
}