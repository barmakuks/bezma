package com.fairbg.bezma.core.model;

import com.fairbg.bezma.core.backgammon.BackgammonAutomat;
import com.fairbg.bezma.store.IDatabase;

/**
 * This class contains all game logic and rules To implement Rules for game
 * inherit this abstract class
 * 
 */
public class GameBox implements IGameBox
{

    // / Отображает текущее состояние игры
    private ModelSituation m_ModelState;

    private IGameAutomat  m_GameAutomat;

    // / Текущий матч
    private Match m_Match;

    public GameBox(Match aMatch)
    {
        m_ModelState = null;
        m_Match = aMatch;
        
        m_GameAutomat = new BackgammonAutomat(this);
    }

    public ModelSituation getModelState()
    {
        return m_ModelState;
    }

    public void setModelState(ModelSituation state)
    {
        m_ModelState = state;
    }

    public boolean processCommand(ModelCommand modelCommand)
    {
        return m_GameAutomat.processCommand(this, modelCommand);
    }

    public Match getMatch()
    {
        return m_Match;
    }

    @Override
    public void appendMove(IMove move)
    {
        m_Match.appendMove(move);
        // TODO Auto-generated method stub
    }

    public void writeCurrentState()
    {
        // TODO Auto-generated method stub
        
    }

    public void restore(IDatabase m_Storage)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void nextGame()
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void finishGame()
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean isMatchFinished()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void finishMatch()
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void nextMatch()
    {
        // TODO Auto-generated method stub
        
    }
}