package com.fairbg.bezma.core.backgammon;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import com.fairbg.bezma.core.model.IGameController;
import com.fairbg.bezma.core.model.IMatchController;
import com.fairbg.bezma.core.model.ModelCommand;
import com.fairbg.bezma.core.model.BoardContext;
import com.fairbg.bezma.core.model.MoveAbstract;
import com.fairbg.bezma.core.model.MovesList;
import com.fairbg.bezma.core.model.PlayerId;
import com.fairbg.bezma.log.BezmaDebug;

/**
 * This class contains all game logic and rules To implement Rules for game
 * inherit this abstract class
 * 
 */
public class BgGameController implements IGameController
{
    /** Current game state */
    private BoardContext                    m_boardContext;

    private IGameAutomat                    m_gameAutomat;
    
    /** Current model */
    private WeakReference<IMatchController> m_matchController;

    public BgGameController(IMatchController aMatchController)
    {
        m_boardContext = null;
        m_matchController = new WeakReference<IMatchController>(aMatchController);

        m_gameAutomat = new BackgammonAutomat(this);
    }

    public void setModelState(BoardContext state)
    {
        m_boardContext = state;
    }

    public boolean processCommand(ModelCommand modelCommand)
    {
        modelCommand.getRawData();
        
//        m_BoardContext = new BoardContext(null, "", m_lastRawData);

        if (BezmaDebug.checkPositionMode)
        {
            Position position = modelCommand.getPosition();
            m_boardContext = new BoardContext(position, "");
            return true;
        }
        else
        {
            return m_gameAutomat.processCommand(this, modelCommand);
        }
    }

    @Override
    public void appendMove(MoveAbstract move)
    {
        m_matchController.get().appendMove(move);
        m_boardContext = new BoardContext(m_gameAutomat.getCurrentPosition(), "");
    }

    @Override
    public void startGame()
    {
        m_boardContext = new BoardContext(m_gameAutomat.getCurrentPosition(), "");
    }

    @Override
    public void finishGame(PlayerId winner, int cubeValue)
    {
        m_boardContext = new BoardContext(new Position(), "");

        // TODO calculate winner points
        int points = cubeValue;

        m_matchController.get().finishGame(winner, points);
    }

    @Override
    public BoardContext getModelSituation()
    {
        return m_boardContext;
    }

    @Override
    public boolean cubeInGame()
    {
        return m_matchController.get().cubeInGame();
    }
    
    @Override
    public void restoreLastGame()
    {
        // reset game automate
        m_gameAutomat.init();

        // replay last game
        final MovesList moves = m_matchController.get().getMoves();
        
        if (moves != null && moves.size() > 0)
        {
            ArrayList<MoveAbstract> lastGame = moves.get(moves.size() - 1); 
            
            if (lastGame.isEmpty() || lastGame.get(lastGame.size() - 1) instanceof MoveFinishGame)
            {
                return;
            }

            for (MoveAbstract move: lastGame)
            {
                m_gameAutomat.playMove(move);                
            }
        }
        m_boardContext = new BoardContext(m_gameAutomat.getCurrentPosition(), "");
    }
}