package com.fairbg.core.model;

import java.util.ArrayList;

import com.fairbg.core.commands.UserCommand;

/**
 * Main class for model in Model-View-Controller architecture
 *  
 * @author Vitalii Misiura
 */
public final class Model {
	private MatchParameters m_MatchParameters;
	private IStorage m_Storage;	
	private GameBox m_GameBox;
	private ArrayList<IModelObserver> m_Observers = new ArrayList<IModelObserver>(); 
	
	
	/************************* PUBLIC FUNCTIONS *****************************************/
	/** Creates new match model
	 * @param parameters Match parameters
	 * @param storage
	 */
	public void create(MatchParameters parameters, IStorage storage){
		m_MatchParameters = parameters;
		m_Storage = storage;
		initNotStoredObjects();
		store();
	}

	/** Opens model saved in storage
	 * @param matchId match identifier
	 * @param storage storage
	 */
	public void open(MatchId matchId, IStorage storage){
		m_MatchParameters = storage.readMatchParameters(matchId);
		m_Storage = storage;
		restore();
		initNotStoredObjects();
	}
	
	/** Accepts user command
	 * @param command
	 * @return true if command is accepted and model state was changed
	 */
	public boolean acceptUserCommand(UserCommand command){
		boolean result = processUserCommand(command);
		if (result)
		{			
			m_GameBox.writeCurrentState();
			m_Storage.writeCurrentState(m_MatchParameters.matchId, m_GameBox.getState());
		}
		return result;
	} 

	/** Subscribes object to listen Model notifications
	 * @param listener 
	 */
	public void addListener(IModelObserver listener){
		if(!m_Observers.contains(listener))
			m_Observers.add(listener);
	} 
	
	/** Unsibscribes object from listening Model notifications
	 * @param listener
	 */
	public void removeListener(IModelObserver listener){
		if(m_Observers.contains(listener))
			m_Observers.remove(listener);
	}
	/************************* PRIVATE FUNCTIONS *****************************************/
	
	/** Stores all model in m_Storage
	 */
	private void store(){
		if(m_MatchParameters.matchId == null || m_MatchParameters.matchId.isEmpty()){
			m_MatchParameters.matchId = MatchId.generateNew();			
		}
		m_Storage.writeMatchParameters(m_MatchParameters);
		m_Storage.writeMatch(m_MatchParameters.matchId, m_Match);
		m_Storage.writeCurrentState(m_MatchParameters.matchId, m_ModelState);
		
	}
	
	/** Restores all model from m_Storage
	 */
	private void restore(){
		m_GameBox.restore(m_Storage);
	}

	/** Converts user command to model command, processes it and changes model state
	 * @param command user command to process
	 * @return true, if command is valid and model was changed 
	 */
	private boolean processUserCommand(UserCommand command) {
		ModelCommand model_command = m_GameBox.processCommand(command);
		if(model_command == null)
			return processModelCommand(model_command);
		return false;
	}

	/** Process model command, changes model state
	 * @param model_command
	 * @return
	 */
	private boolean processModelCommand(ModelCommand model_command) {
		notifyObservers();
		return false;
	}
	
	/** Initializes all non stored objects
	 */
	private void initNotStoredObjects() {
	}

	/** Sends notifies to all observers
	 */
	private void notifyObservers(){
		for(IModelObserver observer : m_Observers){
			observer.updateModel(this);
		}
	}
}
