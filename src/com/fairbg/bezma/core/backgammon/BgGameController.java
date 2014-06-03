package com.fairbg.bezma.core.backgammon;

import java.lang.ref.WeakReference;
import com.fairbg.bezma.core.model.IGameController;
import com.fairbg.bezma.core.model.IMatchController;
import com.fairbg.bezma.core.model.ModelCommand;
import com.fairbg.bezma.core.model.BoardContext;
import com.fairbg.bezma.core.model.MoveAbstract;
import com.fairbg.bezma.core.model.PlayerId;
import com.fairbg.bezma.log.BezmaDebug;
import com.fairbg.bezma.store.IModelSerializer;

/**
 * This class contains all game logic and rules To implement Rules for game
 * inherit this abstract class
 * 
 */
public class BgGameController implements IGameController
{
    /** Current game state */
    private BoardContext                    m_BoardContext;

    private IGameAutomat                    m_GameAutomat;
    
    /** Current model */
    private WeakReference<IMatchController> m_MatchController;

    public BgGameController(IMatchController aMatchController)
    {
        m_BoardContext = null;
        m_MatchController = new WeakReference<IMatchController>(aMatchController);

        m_GameAutomat = new BackgammonAutomat(this);
    }

    public void setModelState(BoardContext state)
    {
        m_BoardContext = state;
    }

    public boolean processCommand(ModelCommand modelCommand)
    {
        modelCommand.getRawData();
        
//        m_BoardContext = new BoardContext(null, "", m_lastRawData);

        if (BezmaDebug.checkPositionMode)
        {
            Position position = modelCommand.getPosition();
            m_BoardContext = new BoardContext(position, "");
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
        m_MatchController.get().appendMove(move);
        m_BoardContext = new BoardContext(m_GameAutomat.getCurrentPosition(), "");
    }

    public void writeCurrentState()
    {
        // TODO Auto-generated method stub
    }

    public void restore(IModelSerializer m_Storage)
    {
        // TODO Auto-generated method stub
    }

    @Override
    public void startGame()
    {
        m_BoardContext = new BoardContext(m_GameAutomat.getCurrentPosition(), "");
    }

    @Override
    public void finishGame(PlayerId winner, int m_cubeValue)
    {
        m_BoardContext = new BoardContext(new Position(), "");

        // TODO calculate winner points
        int points = m_cubeValue;

        m_MatchController.get().finishGame(winner, points);
    }

    @Override
    public BoardContext getModelSituation()
    {
        return m_BoardContext;
    }

    @Override
    public boolean cubeInGame()
    {
        return m_MatchController.get().cubeInGame();
    }
}