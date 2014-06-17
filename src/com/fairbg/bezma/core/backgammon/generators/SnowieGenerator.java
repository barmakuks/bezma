package com.fairbg.bezma.core.backgammon.generators;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Locale;

import android.annotation.SuppressLint;

import com.fairbg.bezma.core.MatchParameters;
import com.fairbg.bezma.core.backgammon.Move;
import com.fairbg.bezma.core.backgammon.MoveCubeDouble;
import com.fairbg.bezma.core.backgammon.MoveCubePass;
import com.fairbg.bezma.core.backgammon.MoveCubeTake;
import com.fairbg.bezma.core.backgammon.MoveFinishGame;
import com.fairbg.bezma.core.backgammon.MoveStartGame;
import com.fairbg.bezma.core.backgammon.Movement;
import com.fairbg.bezma.core.model.IGenerator;
import com.fairbg.bezma.core.model.IMoveVisitor;
import com.fairbg.bezma.core.model.MovesList;
import com.fairbg.bezma.core.model.PlayerId;
import com.fairbg.bezma.log.BezmaLog;


class MoveGenerator implements IMoveVisitor
{
    private StringBuilder m_builder = new StringBuilder();
    private int m_blackScore = 0;
    private int m_whiteScore = 0;
    private int m_moveNo = 1;
    
    private String whiteMove = "";
    private String blackMove = "";
    
    private PrintWriter m_out;

    public MoveGenerator(PrintWriter out)
    {
        m_out = out;
    }

    public int getWhiteScore()
    {
        return m_whiteScore;
    }
    
    public int getBlackScore()
    {
        return m_blackScore;
    }
    
    void printLine()
    {
        m_out.println(String.format("%3d) %-28s %s", m_moveNo++, blackMove, whiteMove));
        blackMove = "";
        whiteMove = "";
    }
    
    @Override
    public void visit(Move move)
    {
        m_builder = new StringBuilder();

        m_builder.append(move.getDie1());
        m_builder.append(move.getDie2() + ": ");

        if (move.getMovements().length == 0)
        {
            m_builder.append("");
        }
        else
        {
            for (Movement movement : move.getMovements())
            {
                movement.accept(this);
            }
        }

        if (move.getPlayer() == PlayerId.BLACK)
        {
            blackMove = m_builder.toString();            
        }
        else
        {
            whiteMove = m_builder.toString();
            printLine();
        }
    }

    @Override
    public void visit(Movement movement)
    {
        m_builder.append("" + movement.MoveFrom + "/" + (movement.MoveTo > 0 ? movement.MoveTo : "0"));
        m_builder.append(movement.Strike ? "* " : " ");
    }

    @Override
    public void visit(MoveCubeDouble move)
    {
        if (move.getPlayer() == PlayerId.BLACK)
        {
            blackMove = " Doubles => " + move.getCubeValue();            
        }
        else
        {
            whiteMove = " Doubles => " + move.getCubeValue();            
            printLine();
        }
    }

    @Override
    public void visit(MoveCubeTake move)
    {
        if (move.getPlayer() == PlayerId.BLACK)
        {
            blackMove = " Takes";            
        }
        else
        {
            whiteMove = " Takes";            
            printLine();
        }
    }

    @Override
    public void visit(MoveCubePass move)
    {
        if (move.getPlayer() == PlayerId.BLACK)
        {
            blackMove = " Drops";
        }
        else
        {
            whiteMove = " Drops";            
            printLine();
        }
    }
    
    @Override
    public void visit(MoveFinishGame move)
    {
        String pts = "" + move.getPoints() + " point";
        if (move.getPoints() > 1)
        {
            pts = pts + "s";
        }

        if (move.getPlayer() == PlayerId.WHITE)
        {
            whiteMove = " Wins " + pts;
            m_whiteScore += move.getPoints();
        }
        else
        {
            m_blackScore += move.getPoints();
        }
        
        if (blackMove != "")
        {
            printLine();
        }

        if (move.getPlayer() == PlayerId.BLACK)
        {
            m_out.println("      Wins " + pts);
        }
        
        m_moveNo = 1;
        blackMove = "";
        whiteMove = "";
    }

    @Override
    public void visit(MoveStartGame move)
    {
        // TODO Auto-generated method stub
        
    }
}

@SuppressLint("DefaultLocale")
public class SnowieGenerator implements IGenerator
{
    private String      m_filename;
    private PrintWriter m_out;
    
    private MatchParameters m_matchParameters;
    
    public static String getDefaultFileName(MatchParameters matchParameters)
    {
        return String.format("%s-%s-%dp-%s.txt", 
                matchParameters.bPlayerName,
                matchParameters.wPlayerName,
                matchParameters.matchLength,
                new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(matchParameters.eventDate)).replace(' ', '_');
    }
    
    public SnowieGenerator(String filename)
    {
        super();
        
        m_filename = filename;
    }

    @Override
    public void processMatchParameters(MatchParameters matchParameters)
    {
        m_matchParameters = matchParameters;
        
        if (m_out != null && matchParameters != null)
        {            
            m_out.println(String.format("; [eventDate \"%s\"]", new SimpleDateFormat("yyyy.MM.dd", Locale.US).format(matchParameters.eventDate)));

            m_out.println(String.format("; [Black \"%s\"]", matchParameters.bPlayerName));
            m_out.println(String.format("; [White \"%s\"]", matchParameters.wPlayerName));
            m_out.println();
            m_out.println(String.format("%2d point match", matchParameters.matchLength));
        }
    }

    @Override
    public void processMoves(MovesList moves)
    {
        MoveGenerator moveGenerator = new MoveGenerator(m_out);

        if (m_out !=  null && moves != null)
        {
            for (int i = 0; i < moves.size(); ++i)
            {
                m_out.println();
                m_out.println(" Game " + (i+1));
                m_out.println(String.format(" %-30s %-30s",
                                            m_matchParameters.bPlayerName + " : " + moveGenerator.getBlackScore(),
                                            m_matchParameters.wPlayerName + " : " + moveGenerator.getWhiteScore()));
            
                for (int j = 0; j < moves.get(i).size(); ++j)
                {
                    moves.get(i).get(j).accept(moveGenerator);
                }
            }
        }
    }

    @Override
    public void beginProccessing()
    {
        finishProcessing();
        BezmaLog.allowTag("Generator");
//        BezmaLog.i("Generator", "Start write to file:" + m_filename);
        try
        {
            File file = new File (m_filename);
            file.getParentFile().mkdirs();
            file.createNewFile();

            m_out = new PrintWriter(new PrintStream(file));
        } catch (FileNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void finishProcessing()
    {
        if (m_out != null)
        {
            BezmaLog.i("Generator", "Finish write to file:" + m_filename);
            m_out.close();
            m_out = null;
        }
    }

}
