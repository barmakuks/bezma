package com.fairbg.bezma.core.backgammon.generators;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import com.fairbg.bezma.core.MatchIdentifier;
import com.fairbg.bezma.core.MatchParameters;
import com.fairbg.bezma.core.backgammon.Move;
import com.fairbg.bezma.core.backgammon.MoveCubeDouble;
import com.fairbg.bezma.core.backgammon.MoveCubePass;
import com.fairbg.bezma.core.backgammon.MoveCubeTake;
import com.fairbg.bezma.core.backgammon.MoveFinishGame;
import com.fairbg.bezma.core.backgammon.Movement;
import com.fairbg.bezma.core.model.IMoveVisitor;
import com.fairbg.bezma.core.model.MovesList;
import com.fairbg.bezma.core.model.PlayerId;
import com.fairbg.bezma.store.IModelSerializer;

class MoveSerializerGenerator implements IMoveVisitor
{
    private StringBuilder   m_builder = new StringBuilder();

    public void clear()
    {
        m_builder = new StringBuilder();
    }
    
    public String getData()
    {
        return m_builder.toString();
    }
    
    @Override
    public void visit(Movement movement)
    {
        m_builder.append(String.format("%c%c", 
                                       (char)('a' + movement.MoveFrom),
                                       (movement.Strike ? 'A' : 'a') + movement.MoveTo));
    }

    @Override
    public void visit(Move move)
    {
        final char player = move.getPlayer() == PlayerId.BLACK ? 'B' : 'W';
        m_builder.append(player);
        m_builder.append(move.getDie1());
        m_builder.append(move.getDie2());
        
        for (int i = 0; i < move.getMovements().length; ++i)
        {
            move.getMovements()[i].accept(this);
        }
        m_builder.append(";\n");
    }

    @Override
    public void visit(MoveCubeDouble move)
    {
        m_builder.append(String.format("%c*%d;\n",
                                        move.getPlayer() == PlayerId.BLACK ? 'B' : 'W',
                                        move.getCubeValue()));
    }

    @Override
    public void visit(MoveCubeTake move)
    {
        m_builder.append(String.format("%c+%d;\n",
                move.getPlayer() == PlayerId.BLACK ? 'B' : 'W',
                move.getCubeValue()));
    }

    @Override
    public void visit(MoveCubePass move)
    {
        m_builder.append(String.format("%c-%d;\n",
                move.getPlayer() == PlayerId.BLACK ? 'B' : 'W',
                move.getCubeValue()));
    }

    @Override
    public void visit(MoveFinishGame move)
    {
        m_builder.append(String.format("%c=%d;\n",
                move.getPlayer() == PlayerId.BLACK ? 'B' : 'W',
                move.getPoints()));
    }
}

public class MatchSerializer implements IModelSerializer
{
    private String                  m_fileName;
    private MatchIdentifier         m_matchId;
    private int                     m_lastSavedGameNo;
    private int                     m_lastSavedMoveNo;
    private MoveSerializerGenerator m_generator;
    
    public MatchSerializer(String fileName)
    {
        m_fileName = fileName;
        m_generator = new MoveSerializerGenerator();
    }

    private void startNewMatch(MatchParameters matchParams)
    {
        m_matchId = matchParams.matchId;
        m_lastSavedGameNo = 0;
        m_lastSavedMoveNo = 0;

        try
        {
            File file = new File (m_fileName);
            File parentFile = file.getParentFile(); 
            if (parentFile != null)
            {
                parentFile.mkdirs();
            }

            file.createNewFile();

            PrintWriter m_out = new PrintWriter(new PrintStream(file));

            m_out.println(";MatchIdentifier: " + matchParams.matchId.toString());
            m_out.println(";BlackPlayerName: " + matchParams.bPlayerName);
            m_out.println(";WhitePlayerName: " + matchParams.wPlayerName);
            m_out.println(";GameType: " + matchParams.gameType.name());
            m_out.println(";MatchWinCondition: " + matchParams.winConditions.name());
            m_out.println(";MatchLength: " + matchParams.matchLength);            
            m_out.println(";UseCrawfordRule: " + matchParams.useCrawfordRule);            
            m_out.println(";RollType: " + matchParams.rollType.name());
            m_out.println(";CalculateRolls: " + matchParams.calculateRolls);
            m_out.println(";ExactRolls: " + matchParams.exactRolls);
            m_out.println(";UseTimer: " + matchParams.useTimer);
            m_out.println(";MatchTimeLimit: " + matchParams.matchTimeLimit);
            m_out.println(";MoveTimeLimit: " + matchParams.moveTimeLimit);
            m_out.println(";ConfirmDouble: " + matchParams.confirmDouble);
            DateFormat formatter = new SimpleDateFormat("yyyyMMddHHmm", Locale.US);
            m_out.println(";Date: " + formatter.format(matchParams.eventDate));
            m_out.println(";DefaultDir: " + matchParams.defaultDir); 
            m_out.close();

        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    @Override
    public void serialize(MatchParameters matchParameters, MovesList moves)
    {
        if (matchParameters.matchId != m_matchId)
        {
            startNewMatch(matchParameters);            
        }
        
        m_generator.clear();

        boolean hasNextGame = false;
        do
        {
            hasNextGame = false;
            for (;m_lastSavedMoveNo < moves.get(m_lastSavedGameNo).size(); ++m_lastSavedMoveNo)
            {
                moves.get(m_lastSavedGameNo).get(m_lastSavedMoveNo).accept(m_generator);
            }
            if (m_lastSavedGameNo < moves.size() - 1)
            {
                m_lastSavedGameNo++;
                m_lastSavedMoveNo = 0;
                hasNextGame = true;
            }
        } while (hasNextGame);
        
        try
        {
            File file = new File (m_fileName);
            File parentFile = file.getParentFile();
            if (parentFile != null)
            {
                parentFile.mkdirs();                
            }            

            FileWriter m_out = new FileWriter(file, true);
            
            System.out.println("Ser:" + m_generator.getData());            
            m_out.write(m_generator.getData());
            m_out.close();
            
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void deserialize(MatchIdentifier matchId, MatchParameters matchParameters, MovesList moves)
    {
        // TODO Implement deserialization        
    }

    @Override
    public void finishMatch()
    {
        File file = new File (m_fileName);
        System.out.println("Remove file: " + m_fileName + " with result" + file.delete());            
    }    
}
