package com.fairbg.bezma.core.backgammon.generators;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Locale;

import com.fairbg.bezma.core.MatchIdentifier;
import com.fairbg.bezma.core.MatchParameters;
import com.fairbg.bezma.core.MatchParameters.GameType;
import com.fairbg.bezma.core.MatchParameters.MatchWinConditions;
import com.fairbg.bezma.core.MatchParameters.RollTypes;
import com.fairbg.bezma.core.backgammon.Move;
import com.fairbg.bezma.core.backgammon.MoveCubeDouble;
import com.fairbg.bezma.core.backgammon.MoveCubePass;
import com.fairbg.bezma.core.backgammon.MoveCubeTake;
import com.fairbg.bezma.core.backgammon.MoveFinishGame;
import com.fairbg.bezma.core.backgammon.MoveStartGame;
import com.fairbg.bezma.core.backgammon.Movement;
import com.fairbg.bezma.core.backgammon.Position;
import com.fairbg.bezma.core.model.IMoveVisitor;
import com.fairbg.bezma.core.model.MoveAbstract;
import com.fairbg.bezma.core.model.MovesList;
import com.fairbg.bezma.core.model.PlayerId;
import com.fairbg.bezma.log.BezmaLog;
import com.fairbg.bezma.store.IModelSerializer;

class MoveKeys
{
    public static final char Black = 'B';
    public static final char White = 'W';
    public static final String StartGame = "SG:";

    public static final char Double = '*';
    public static final char Take = '+';
    public static final char Pass = '-';
    public static final char Finish = '=';    
    
    public static char getPlayerKey(PlayerId player)
    {
        if (player == PlayerId.RED)
        {
            return Black;
        }
        else if (player == PlayerId.SILVER)
        {
            return White;
        }
        
        return 0;
    }
}

class MoveSerializerGenerator implements IMoveVisitor
{
    private StringBuilder m_builder = new StringBuilder();

    public void clear()
    {
        m_builder = new StringBuilder();
    }

    public String getData()
    {
        return m_builder.toString();
    }

    private void finishMove()
    {
        m_builder.append(";\n");
    }
    
    @Override
    public void visit(Movement movement)
    {
        m_builder.append(String.format("%c%c",
                (char) ('a' + movement.MoveFrom),
                (movement.Strike ? 'A' : 'a') + movement.MoveTo));
    }

    @Override
    public void visit(Move move)
    {
        m_builder.append(MoveKeys.getPlayerKey(move.getPlayer()));
        m_builder.append(move.getDie1());
        m_builder.append(move.getDie2());

        for (int i = 0; i < move.getMovements().length; ++i)
        {
            move.getMovements()[i].accept(this);
        }
        finishMove();
    }

    @Override
    public void visit(MoveCubeDouble move)
    {
        m_builder.append(MoveKeys.getPlayerKey(move.getPlayer()));
        m_builder.append(MoveKeys.Double);
        m_builder.append(move.getCubeValue());
        finishMove();        
    }

    @Override
    public void visit(MoveCubeTake move)
    {
        m_builder.append(MoveKeys.getPlayerKey(move.getPlayer()));
        m_builder.append(MoveKeys.Take);
        m_builder.append(move.getCubeValue());
        finishMove();
    }

    @Override
    public void visit(MoveCubePass move)
    {
        m_builder.append(MoveKeys.getPlayerKey(move.getPlayer()));
        m_builder.append(MoveKeys.Pass);
        m_builder.append(move.getCubeValue()); 
        finishMove();
    }

    @Override
    public void visit(MoveFinishGame move)
    {
        m_builder.append(MoveKeys.getPlayerKey(move.getPlayer()));
        m_builder.append(MoveKeys.Finish);
        m_builder.append(move.getPoints()); 
        finishMove();
    }

    @Override
    public void visit(MoveStartGame move)
    {
        m_builder.append(MoveKeys.StartGame + move.getDirection().name());
        finishMove();
    }
}

class ParamKeys
{
    public static final String MatchIdentifier   = "MatchIdentifier";
    public static final String BlackPlayerName   = "BlackPlayerName";
    public static final String WhitePlayerName   = "WhitePlayerName";
    public static final String GameType          = "GameType";
    public static final String MatchWinCondition = "MatchWinCondition";
    public static final String MatchLength       = "MatchLength";
    public static final String UseCrawfordRule   = "UseCrawfordRule";
    public static final String RollType          = "RollType";
    public static final String CalculateRolls    = "CalculateRolls";
    public static final String ExactRolls        = "ExactRolls";
    public static final String UseTimer          = "UseTimer";
    public static final String MatchTimeLimit    = "MatchTimeLimit";
    public static final String MoveTimeLimit     = "MoveTimeLimit";
    public static final String ConfirmDouble     = "ConfirmDouble";
    public static final String Date              = "Date";
    public static final String DefaultDir        = "DefaultDir";

    public static final String KeyPrefix         = ";";
    public static final String KeySuffix         = ":";
    public static final String DateFormat        = "yyyyMMddHHmm";

    public static String formatedKey(String key)
    {
        return KeyPrefix + key + KeySuffix;
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
            File file = new File(m_fileName);
            File parentFile = file.getParentFile();
            if (parentFile != null)
            {
                parentFile.mkdirs();
            }

            file.createNewFile();

            PrintWriter m_out = new PrintWriter(new PrintStream(file));

            m_out.println(ParamKeys.formatedKey(ParamKeys.MatchIdentifier) + matchParams.matchId.toString());
            m_out.println(ParamKeys.formatedKey(ParamKeys.BlackPlayerName) + matchParams.redPlayerName);
            m_out.println(ParamKeys.formatedKey(ParamKeys.WhitePlayerName) + matchParams.silverPlayerName);
            m_out.println(ParamKeys.formatedKey(ParamKeys.GameType) + matchParams.gameType.name());
            m_out.println(ParamKeys.formatedKey(ParamKeys.MatchWinCondition) + matchParams.winConditions.name());
            m_out.println(ParamKeys.formatedKey(ParamKeys.MatchLength) + matchParams.matchLength);
            m_out.println(ParamKeys.formatedKey(ParamKeys.UseCrawfordRule) + matchParams.useCrawfordRule);
            m_out.println(ParamKeys.formatedKey(ParamKeys.RollType) + matchParams.rollType.name());
            m_out.println(ParamKeys.formatedKey(ParamKeys.CalculateRolls) + matchParams.calculateRolls);
            m_out.println(ParamKeys.formatedKey(ParamKeys.ExactRolls) + matchParams.exactRolls);
            m_out.println(ParamKeys.formatedKey(ParamKeys.UseTimer) + matchParams.useTimer);
            m_out.println(ParamKeys.formatedKey(ParamKeys.MatchTimeLimit) + matchParams.matchTimeLimit);
            m_out.println(ParamKeys.formatedKey(ParamKeys.MoveTimeLimit) + matchParams.moveTimeLimit);
            m_out.println(ParamKeys.formatedKey(ParamKeys.ConfirmDouble) + matchParams.confirmDouble);
            DateFormat formatter = new SimpleDateFormat(ParamKeys.DateFormat, Locale.US);
            m_out.println(ParamKeys.formatedKey(ParamKeys.Date) + formatter.format(matchParams.eventDate));
            m_out.println(ParamKeys.formatedKey(ParamKeys.DefaultDir) + matchParams.defaultDir);
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
            for (; m_lastSavedMoveNo < moves.get(m_lastSavedGameNo).size(); ++m_lastSavedMoveNo)
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
            File file = new File(m_fileName);
            File parentFile = file.getParentFile();
            if (parentFile != null)
            {
                parentFile.mkdirs();
            }

            BezmaLog.i("STORAGE", "Write to file: " + m_fileName);
            FileWriter m_out = new FileWriter(file, true);

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
    public void deserialize(MatchParameters matchParameters, MovesList moves)
    {
        try
        {
            ArrayList<String> lines = new ArrayList<String>();

            File file = new File(m_fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

            String line = null;

            while ((line = reader.readLine()) != null)
            {
                lines.add(line);
            }

            reader.close();

            parse(lines, matchParameters, moves);
            m_matchId = matchParameters.matchId;
            
            if (moves.isEmpty())
            {
                m_lastSavedGameNo = 0;
                m_lastSavedMoveNo = 0;                
            }
            else
            {
                m_lastSavedGameNo = moves.size() - 1;
                m_lastSavedMoveNo = moves.get(moves.size() - 1).size();
            }

        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private static void parse(AbstractList<String> lines,
            MatchParameters matchParameters,
            MovesList moves)
    {
        moves.clear();

        for (String line : lines)
        {
            if (line.startsWith(ParamKeys.KeyPrefix))
            {
                parseMatchParameter(line, matchParameters);
            }
            else
            {
                parseMove(line, moves);
            }
        }
    }

    private static void parseMatchParameter(String line, MatchParameters matchParams)
    {
        String key = line.substring(line.indexOf(ParamKeys.KeyPrefix) + ParamKeys.KeyPrefix.length(),
                line.indexOf(ParamKeys.KeySuffix));
        String value = line.substring(line.indexOf(ParamKeys.KeySuffix) + ParamKeys.KeySuffix.length());

        if (key.equals(ParamKeys.MatchIdentifier))
        {
            matchParams.matchId = MatchIdentifier.fromString(value);
        } 
        else if (key.equals(ParamKeys.BlackPlayerName))
        {
            matchParams.redPlayerName = value;
        }
        else if (key.equals(ParamKeys.WhitePlayerName))
        {
            matchParams.silverPlayerName = value;
        }
        else if (key.equals(ParamKeys.GameType))
        {
            matchParams.gameType = GameType.valueOf(value);
        }
        else if (key.equals(ParamKeys.MatchWinCondition))
        {
            matchParams.winConditions = MatchWinConditions.valueOf(value);
        }
        else if (key.equals(ParamKeys.MatchLength))
        {
            matchParams.matchLength = Integer.parseInt(value);
        }
        else if (key.equals(ParamKeys.UseCrawfordRule))
        {
            matchParams.useCrawfordRule = Boolean.parseBoolean(value);
        }
        else if (key.equals(ParamKeys.RollType))
        {
            matchParams.rollType = RollTypes.valueOf(value);
        }
        else if (key.equals(ParamKeys.CalculateRolls))
        {
            matchParams.calculateRolls = Boolean.parseBoolean(value);
        }
        else if (key.equals(ParamKeys.ExactRolls))
        {
            matchParams.exactRolls = Boolean.parseBoolean(value);
        }
        else if (key.equals(ParamKeys.UseTimer))
        {
            matchParams.useTimer = Boolean.parseBoolean(value);
        }
        else if (key.equals(ParamKeys.MatchTimeLimit))
        {
            matchParams.matchTimeLimit = Integer.parseInt(value);
        }
        else if (key.equals(ParamKeys.MoveTimeLimit))
        {
            matchParams.moveTimeLimit = Integer.parseInt(value);
        }
        else if (key.equals(ParamKeys.ConfirmDouble))
        {
            matchParams.confirmDouble = Boolean.parseBoolean(value);
        }
        else if (key.equals(ParamKeys.Date))
        {
            DateFormat formatter = new SimpleDateFormat(ParamKeys.DateFormat, Locale.US);
            try
            {
                matchParams.eventDate = formatter.parse(value);
            } catch (ParseException e)
            {
                e.printStackTrace();
            }
        }
        else if (key.equals(ParamKeys.DefaultDir))
        {
            matchParams.defaultDir = value;
        }
    }

    private static void parseMove(String line, MovesList moves)
    {
        if (line == null || line.length() < 3)
        {
            return;            
        }
        final char pl = line.charAt(0); 

        MoveAbstract move = null;
        
        PlayerId player = PlayerId.NONE;

        if (pl == MoveKeys.Black)
        {
            player = PlayerId.RED;
        }
        else if (pl == MoveKeys.White)
        {
            player = PlayerId.SILVER;
        }
        else if (line.startsWith(MoveKeys.StartGame))
        {
            final Position.Direction direction = Position.Direction.valueOf(line.substring(MoveKeys.StartGame.length(), line.length() - 1));
            move = new MoveStartGame(direction);
        }
        else
        {
            return;
        }

        final char operation = line.charAt(1);

        if (move == null)
        {
            switch (operation)
            {
            case MoveKeys.Double:
            {
                final int cubeValue = Integer.parseInt(line.substring(2, line.length() - 1));            
                move = new MoveCubeDouble(player, cubeValue);
                break;
            }
            case MoveKeys.Take:
            {
                final int cubeValue = Integer.parseInt(line.substring(2, line.length() - 1));            
                move = new MoveCubeTake(player, cubeValue);
                break;
            }
            case MoveKeys.Pass:
            {
                final int cubeValue = Integer.parseInt(line.substring(2, line.length() - 1));            
                move = new MoveCubePass(player, cubeValue);
                break;
            }
            case MoveKeys.Finish:
            {
                final int points = Integer.parseInt(line.substring(2, line.length() - 1));            
                move = new MoveFinishGame(player, points);
                break;
            }
            default:
            {
                final char die1 = line.charAt(1);            
                final char die2 = line.charAt(2);
                Move mv = new Move();
                mv.setDice(die1 - '0', die2 - '0');
                mv.setPlayer(player);
                
                String movements = line.substring(3, line.length()-1);
                while (!movements.isEmpty())
                {
                    boolean strike = false;
                    int from_index = movements.charAt(0) - 'a';
                    int to_index = movements.charAt(1) - 'a';
                    if (movements.charAt(1) <= 'Z')
                    {
                        to_index = movements.charAt(1) - 'A';
                        strike = true;
                    }                
                    mv.appendMovement(from_index, to_index, strike);
                    movements = movements.substring(2);
                }
                move = mv;
                break;
            }
            }
        }

        if (move != null)
        {
            appendMove(moves, move);
        }
    }

    private static ArrayList<MoveAbstract> getLastGame(MovesList moves)
    {
        if (!moves.isEmpty())
        {
            return moves.get(moves.size() - 1);
        }
        
        return null;
    }
    
    private static MoveAbstract getLastMove(MovesList moves)
    {
        ArrayList<MoveAbstract> lastGame = getLastGame(moves);
        
        if (lastGame != null && !lastGame.isEmpty())
        {
            return lastGame.get(lastGame.size() - 1);
        }
        
        return null;
    }

    private static void appendMove(MovesList moves, MoveAbstract move)
    {        
        MoveAbstract lastMove = getLastMove(moves);
        
        if (lastMove == null || lastMove instanceof MoveFinishGame)
        {
            moves.add(new ArrayList<MoveAbstract>());
        }
     
        getLastGame(moves).add(move);
    } 
    
    @Override
    public void finishMatch()
    {
        File file = new File(m_fileName);
        System.out.println("Remove file: " + m_fileName + " with result" + file.delete());
    }

    @Override
    public boolean isLastMatchFinished()
    {
        File file = new File(m_fileName);
        return !file.exists();
    }
    // public static void main(String[] args)
    // {
    // String value = ";Key:value";
    // parseMatchParameter(value, null);
    // }

}
