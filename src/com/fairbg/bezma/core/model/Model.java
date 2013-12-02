package com.fairbg.bezma.core.model;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import com.fairbg.bezma.core.MatchIdentifier;
import com.fairbg.bezma.core.MatchParameters;
import com.fairbg.bezma.store.IDatabase;

/**
 * Main class for model in Model-View-Controller architecture
 */
public final class Model
{
    private MatchParameters m_MatchParameters;
    private IDatabase m_Storage;
    private GameBox m_GameBox;

    private ArrayList<WeakReference<IModelObserver>> m_Observers = new ArrayList<WeakReference<IModelObserver>>();

    private Match m_Match = new Match();

    // private ModelState m_ModelState = new ModelState();

    /************************* PUBLIC FUNCTIONS *****************************************/
    /**
     * Creates new match model
     * 
     * @param parameters
     *            Match parameters
     * @param storage
     */
    public void create(MatchParameters parameters, IDatabase storage)
    {
	m_MatchParameters = parameters;
	m_Storage = storage;
	m_GameBox = new GameBox(this);
	initNotStoredObjects();
	store();
    }

    /**
     * Opens model saved in storage
     * 
     * @param matchId
     *            match identifier
     * @param storage
     *            storage
     */
    public void open(MatchIdentifier matchId, IDatabase storage)
    {
	m_MatchParameters = storage.readMatchParameters(matchId);
	m_Storage = storage;
	restore();
	initNotStoredObjects();
    }

    /**
     * Accepts user command
     * 
     * @param command
     * @return true if command is accepted and model state was changed
     */
    public boolean acceptCommand(ModelCommand command)
    {
	boolean result = processCommand(command);
	if (result)
	{
	    m_GameBox.writeCurrentState();
	    if (m_Storage != null)
	    {
		m_Storage.writeCurrentState(m_MatchParameters.matchId,
			m_GameBox.getModelState());
	    }
	}
	return result;
    }

    public void addObserver(IModelObserver observer)
    {
	if (!m_Observers.contains(observer))
	{
	    m_Observers.add(new WeakReference<IModelObserver>(observer));
	}
    }

    public void removeObserver(IModelObserver observer)
    {
	m_Observers.remove(observer);
    }

    public void notifyAll(IModelObserver.Event event)
    {
	for (WeakReference<IModelObserver> observer : m_Observers)
	{
	    if (!observer.isEnqueued())
	    {
		observer.get().onModelEvent(event);
	    }
	}
    }

    /************************* PRIVATE FUNCTIONS *****************************************/

    /**
     * Stores all model in m_Storage
     */
    private void store()
    {
	if (m_Storage != null)
	{
	    if (m_MatchParameters.matchId == null
		    || m_MatchParameters.matchId.isEmpty())
	    {
		m_MatchParameters.matchId = MatchIdentifier.generateNew();
	    }
	    m_Storage.writeMatchParameters(m_MatchParameters);
	    m_Storage.writeMatch(m_MatchParameters.matchId, m_Match);
	}
    }

    /**
     * Restores all model from m_Storage
     */
    private void restore()
    {
	if (m_Storage != null)
	{
	    m_GameBox.restore(m_Storage);
	}
    }

    /**
     * Converts user command to model command, processes it and changes model
     * state
     * 
     * @param modelCommand
     *            user command to process
     * @return true, if command is valid and model was changed
     */
    private boolean processCommand(ModelCommand modelCommand)
    {
	return m_GameBox.processCommand(modelCommand);
    }

    /**
     * Initializes all non stored objects
     */
    private void initNotStoredObjects()
    {
    }

    public ModelSituation getState()
    {
	return m_GameBox.getModelState();
    }

    public void appendMove(IMove move)
    {
	m_Match.appendMove(move);
	notifyAll(new IModelObserver.MoveEvent(move));
    }
}