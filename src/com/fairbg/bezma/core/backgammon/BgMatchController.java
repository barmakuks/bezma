package com.fairbg.bezma.core.backgammon;

import java.util.ArrayList;

import com.fairbg.bezma.core.MatchParameters;
import com.fairbg.bezma.core.model.IMatchController;
import com.fairbg.bezma.core.model.IModelEventNotifier;
import com.fairbg.bezma.core.model.IModelObserver;
import com.fairbg.bezma.core.model.MatchScore;
import com.fairbg.bezma.core.model.MoveAbstract;
import com.fairbg.bezma.core.model.PlayerId;

class BgScore implements MatchScore
{
    int m_whiteScore = 0;
    int m_blackScore = 0;

    @Override
    public int getPlayerScore(PlayerId playerId)
    {
        return playerId == PlayerId.BLACK ? m_blackScore : m_whiteScore;
    }

    @Override
    public void setPlayerScore(PlayerId playerId, int score)
    {
        switch (playerId)
        {
        case WHITE:
            m_whiteScore = score;
            break;
        case BLACK:
            m_blackScore = score;
            break;
        default:
            break;
        }
    }
}

public class BgMatchController implements IMatchController
{
    private ArrayList<ArrayList<MoveAbstract>> m_Moves = new ArrayList<ArrayList<MoveAbstract>>();
    private int                                m_gameNo;
    private IModelEventNotifier                m_ModelNotifier;
    private MatchParameters                    m_matchParameters;
    private BgScore                            m_score = new BgScore();

    BgMatchController(MatchParameters matchParameters, IModelEventNotifier aModelNotifier)
    {
        m_matchParameters = matchParameters;
        m_gameNo = -1;
        m_Moves.clear();
        nextGame();
        m_ModelNotifier = aModelNotifier;
    }

    public void appendMove(MoveAbstract move)
    {
        m_Moves.get(m_gameNo).add(move);
        m_ModelNotifier.notifyAll(new IModelObserver.MoveEvent(move));
    }

    @Override
    public void finishGame(PlayerId winner, int points)
    {
        MoveAbstract move = new MoveFinishGame(winner, points);
        appendMove(move);
        
        m_score.setPlayerScore(winner, m_score.getPlayerScore(winner) + points);

        m_ModelNotifier.notifyAll(new IModelObserver.ScoreChangedEvent(m_score));
        
        if (!isMatchFinished())
        {
            // start next game
            nextGame();
        }
        else
        {
            m_ModelNotifier.notifyAll(new IModelObserver.MatchFinishEvent());
        }
    }

    @Override
    public boolean isMatchFinished()
    {
        switch (m_matchParameters.winConditions)
        {
        case FixedGames:
            return m_gameNo + 1 >= m_matchParameters.matchLength;
        case Scores:
            return m_score.getPlayerScore(PlayerId.BLACK) >= m_matchParameters.matchLength
                    || m_score.getPlayerScore(PlayerId.WHITE) >= m_matchParameters.matchLength;
        default:
            return false;
        }
    }

    @Override
    public MatchParameters getMatchParameters()
    {
        return m_matchParameters;
    }

    @Override
    public MatchScore getScore()
    {
        return m_score;
    }

    private void nextGame()
    {
        m_Moves.add(new ArrayList<MoveAbstract>());
        m_gameNo++;
    }
}