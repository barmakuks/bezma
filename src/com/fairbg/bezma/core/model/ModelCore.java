package com.fairbg.bezma.core.model;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import com.fairbg.bezma.core.MatchIdentifier;
import com.fairbg.bezma.core.MatchParameters;
import com.fairbg.bezma.store.IModelSerializer;

/**
 * Main class for model in Model-View-Controller architecture
 */
public final class ModelCore implements IModelEventNotifier
{
    private MatchParameters                          m_matchParameters;
    private IModelSerializer                         m_storage;

    private IMatchController                         m_matchController;
    private IGameController                          m_gameController;

    private ArrayList<WeakReference<IModelObserver>> m_observers = new ArrayList<WeakReference<IModelObserver>>();

    /**
     * Creates new match model
     * @param parameters Match parameters
     * @param storage
     */
    public void create(MatchParameters parameters, IModelSerializer storage, IControllersFactory factory)
    {
        m_matchParameters = parameters;
        m_storage = storage;
        m_matchController = factory.createMatchController(parameters, this);
        m_gameController = factory.createGameController(m_matchController);
        initNotStoredObjects();
        store();
    }

    /**
     * Opens model saved in storage
     * @param matchId Match identifier
     * @param storage storage
     */
    public void open(MatchIdentifier matchId, IModelSerializer storage)
    {
        m_storage = storage;
        m_matchController.deserialize(storage, matchId);
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
        if (result && m_storage != null)
        {
            if (m_matchController.isMatchFinished())
            {
                m_storage.finishMatch();
            }
            else
            {
                m_matchController.serialize(m_storage);
            }
        }
        return result;
    }

    public void addObserver(IModelObserver observer)
    {
        if (!m_observers.contains(observer))
        {
            m_observers.add(new WeakReference<IModelObserver>(observer));
        }
    }

    public void removeObserver(IModelObserver observer)
    {
        m_observers.remove(observer);
    }

    @Override
    public void notifyAll(IModelObserver.Event event)
    {
        for (WeakReference<IModelObserver> observer : m_observers)
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
        if (m_storage != null)
        {
            if (m_matchParameters.matchId == null
                    || m_matchParameters.matchId.isEmpty())
            {
                m_matchParameters.matchId = MatchIdentifier.generateNew();
            }
            m_matchController.serialize(m_storage);
        }
    }

    /**
     * Converts user command to model command, processes it and changes model state
     * @param modelCommand user command to process
     * @return true, if command is valid and model was changed
     */
    private boolean processCommand(ModelCommand modelCommand)
    {
        return m_gameController.processCommand(modelCommand);
    }

    /**
     * Initializes all non stored objects
     */
    private void initNotStoredObjects()
    {
    }

    public BoardContext getBoardContext()
    {
        return m_gameController.getModelSituation();
    }
}