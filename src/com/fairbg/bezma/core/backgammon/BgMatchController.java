package com.fairbg.bezma.core.backgammon;

import java.util.ArrayList;
import java.io.File;

//import android.provider.MediaStore.Files;

import com.fairbg.bezma.core.MatchParameters;
import com.fairbg.bezma.core.MatchParameters.MatchWinConditions;
import com.fairbg.bezma.core.backgammon.generators.SnowieGenerator;
import com.fairbg.bezma.core.model.IGenerator;
import com.fairbg.bezma.core.model.IMatchController;
import com.fairbg.bezma.core.model.IModelEventNotifier;
import com.fairbg.bezma.core.model.IModelObserver;
import com.fairbg.bezma.core.model.MatchScore;
import com.fairbg.bezma.core.model.MoveAbstract;
import com.fairbg.bezma.core.model.MovesList;
import com.fairbg.bezma.core.model.PlayerId;
import com.fairbg.bezma.log.BezmaLog;

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
    private MovesList                          m_Moves = new MovesList();
    private int                                m_gameNo;
    private IModelEventNotifier                m_ModelNotifier;
    private MatchParameters                    m_matchParameters;
    private BgScore                            m_score = new BgScore();
    private boolean                            m_isCrawford; 

    BgMatchController(MatchParameters matchParameters, IModelEventNotifier aModelNotifier)
    {
        m_matchParameters = matchParameters;
        m_gameNo = -1;
        m_Moves.clear();
        nextGame();
        m_ModelNotifier = aModelNotifier;
        m_isCrawford = false;
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
        
        m_isCrawford = m_score.getPlayerScore(winner) + points + 1 == m_matchParameters.matchLength;

        m_ModelNotifier.notifyAll(new IModelObserver.ScoreChangedEvent(m_score));
        
        if (!isMatchFinished())
        {
            // start next game
            nextGame();
        }
        else
        {
            saveMatch();
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
    
    private void saveMatch()
    {
        String filename = getUniqueFilename(m_matchParameters.defaultDir, SnowieGenerator.getDefaultFileName(m_matchParameters));
     
        processMoves(new SnowieGenerator(filename), m_matchParameters, m_Moves);
    }

    private String getUniqueFilename(String directory, String defaultFileName)
    {
        String filenameWithoutExt = defaultFileName;
        String ext = null;
        
        int extIndex = defaultFileName.lastIndexOf('.');
        
        if (extIndex != -1)
        {
            ext = defaultFileName.substring(extIndex);
            filenameWithoutExt = defaultFileName.substring(0, extIndex);
        }
        
        String filename = defaultFileName;
        
        int i = 1;
        String path = directory + "/" + filename;
        
        while ((new File(path)).exists())
        {
            filename = filenameWithoutExt + "-" + i + ext;
            i++;
            path = directory + "/" + filename;
        }
        BezmaLog.allowTag("BezmaPath");
        BezmaLog.i("BezmaPath", path);
        return path;//Paths.get(directory, filename).toString();
    }

    private void processMoves(IGenerator generator, MatchParameters matchParameters, MovesList moves)
    {
        generator.beginProccessing();
        
        generator.processMatchParameters(matchParameters);
        
        generator.processMoves(moves);
        
        generator.finishProcessing();
    }

    @Override
    public boolean isCrawford()
    {
        return m_matchParameters.winConditions == MatchWinConditions.Scores && (!m_matchParameters.useCrawfordRule || m_isCrawford);
    }

}