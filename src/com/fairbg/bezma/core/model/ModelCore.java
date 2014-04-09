package com.fairbg.bezma.core.model;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import com.fairbg.bezma.core.MatchIdentifier;
import com.fairbg.bezma.core.MatchParameters;
import com.fairbg.bezma.store.IDatabase;

/**
 * Main class for model in Model-View-Controller architecture
 */
public final class ModelCore implements IModelEventNotifier
{
    private MatchParameters                          m_MatchParameters;
    private IDatabase                                m_Storage;

    private IMatchController                         m_MatchController;
    private IGameController                          m_GameController;

    private ArrayList<WeakReference<IModelObserver>> m_Observers = new ArrayList<WeakReference<IModelObserver>>();

    /**
     * Creates new match model
     * @param parameters Match parameters
     * @param storage
     */
    public void create(MatchParameters parameters, IDatabase storage, IControllersFactory factory)
    {
        m_MatchParameters = parameters;
        m_Storage = storage;
        m_MatchController = factory.createMatchController(parameters, this);
        m_GameController = factory.createGameController(m_MatchController);
        initNotStoredObjects();
        store();
    }

    /**
     * Opens model saved in storage
     * @param matchId Match identifier
     * @param storage storage
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
     * @param command
     * @return true if command is accepted and model state was changed
     */
    public boolean acceptCommand(ModelCommand command)
    {
        boolean result = processCommand(command);
        if (result && m_Storage != null)
        {
            m_Storage.writeCurrentState(m_MatchParameters.matchId, m_GameController.getModelSituation());
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

    @Override
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
            m_Storage.writeMatch(m_MatchParameters.matchId, m_MatchController);
        }
    }

    /**
     * Restores all model from m_Storage
     */
    private void restore()
    {
        if (m_Storage != null)
        {
            // m_GameBox.restore(m_Storage);
        }
    }

    /**
     * Converts user command to model command, processes it and changes model state
     * @param modelCommand user command to process
     * @return true, if command is valid and model was changed
     */
    private boolean processCommand(ModelCommand modelCommand)
    {
        return m_GameController.processCommand(modelCommand);
    }

    /**
     * Initializes all non stored objects
     */
    private void initNotStoredObjects()
    {
    }

    public BoardContext getBoardContext()
    {
        return m_GameController.getModelSituation();
    }
}