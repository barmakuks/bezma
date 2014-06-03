package com.fairbg.bezma.core.backgammon;

import java.util.ArrayList;
import java.io.File;

//import android.provider.MediaStore.Files;

import com.fairbg.bezma.core.MatchIdentifier;
import com.fairbg.bezma.core.MatchParameters;
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
import com.fairbg.bezma.store.IModelSerializer;

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
    private MovesList                          m_moves = new MovesList();
    private int                                m_gameNo;
    private IModelEventNotifier                m_modelNotifier;
    private MatchParameters                    m_matchParameters;
    private BgScore                            m_score = new BgScore();
    private boolean                            m_cubeInGame; 

    BgMatchController(MatchParameters matchParameters, IModelEventNotifier aModelNotifier)
    {
        m_matchParameters = matchParameters;
        m_gameNo = -1;
        m_moves.clear();
        nextGame();
        m_modelNotifier = aModelNotifier;
        m_cubeInGame = !matchParameters.useCrawfordRule || matchParameters.matchLength != 1;
    }

    public void appendMove(MoveAbstract move)
    {
        m_moves.get(m_gameNo).add(move);
        m_modelNotifier.notifyAll(new IModelObserver.MoveEvent(move));
    }

    @Override
    public void finishGame(PlayerId winner, int points)
    {
        MoveAbstract move = new MoveFinishGame(winner, points);
        appendMove(move);
        
        m_score.setPlayerScore(winner, m_score.getPlayerScore(winner) + points);
        
        m_cubeInGame = !m_matchParameters.useCrawfordRule || (m_score.getPlayerScore(winner) + 1 != m_matchParameters.matchLength);

        m_modelNotifier.notifyAll(new IModelObserver.ScoreChangedEvent(m_score));
        
        if (!isMatchFinished())
        {
            // start next game
            nextGame();
        }
        else
        {
            saveMatch();
            m_modelNotifier.notifyAll(new IModelObserver.MatchFinishEvent());
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
        m_moves.add(new ArrayList<MoveAbstract>());
        m_gameNo++;
    }
    
    private void saveMatch()
    {
        String filename = getUniqueFilename(m_matchParameters.defaultDir, SnowieGenerator.getDefaultFileName(m_matchParameters));
     
        IGenerator generator = new SnowieGenerator(filename); 

        generator.beginProccessing();
        generator.processMatchParameters(m_matchParameters);
        generator.processMoves(m_moves);
        generator.finishProcessing();
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

    @Override
    public boolean cubeInGame()
    {
        return m_cubeInGame;
    }

    @Override
    public void serialize(IModelSerializer serializer)
    {
        serializer.serialize(m_matchParameters, m_moves);
    }

    @Override
    public void deserialize(IModelSerializer serializer, MatchIdentifier matchId)
    {
        // TODO Auto-generated method stub
        
    }

}