package com.fairbg.bezma.core.model;

import java.lang.ref.WeakReference;

import com.fairbg.bezma.core.backgammon.BackgammonAutomat;
import com.fairbg.bezma.core.backgammon.BackgammonRules;
import com.fairbg.bezma.core.backgammon.Position;
import com.fairbg.bezma.log.BezmaDebug;
import com.fairbg.bezma.store.IDatabase;

/**
 * This class contains all game logic and rules To implement Rules for game
 * inherit this abstract class
 * 
 */
public class GameBox implements IGameBox
{
    /** Current game state */
    private ModelSituation       m_ModelState;

    private IGameAutomat         m_GameAutomat;

    /** Current model */
    private WeakReference<Model> m_Model;

    public GameBox(Model aModel)
    {
        m_ModelState = null;
        m_Model = new WeakReference<Model>(aModel);

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
        if (BezmaDebug.checkPositionMode)
        {
            Position position = modelCommand.getPosition();
            m_ModelState = new ModelSituation(position, "");
            return true;
        }
        else
        {
            return m_GameAutomat.processCommand(this, modelCommand);
        }
    }

    @Override
    public void appendMove(MoveAbstract move)
    {
        m_Model.get().appendMove(move);
        m_ModelState = new ModelSituation(m_GameAutomat.getCurrentPosition(), "");
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
    public void startGame(Position.Direction direction)
    {
        Position pos = new Position();
        pos.setCheckers(BackgammonRules.getStartPosition(direction));

        m_ModelState = new ModelSituation(pos, "");
    }

    @Override
    public void finishGame()
    {
        m_ModelState = new ModelSituation(new Position(), "");;
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