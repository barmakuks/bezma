package com.fairbg.bezma.core.backgammon;

import com.fairbg.bezma.core.MatchParameters;
import com.fairbg.bezma.core.model.MoveAbstract;
import com.fairbg.bezma.core.model.IMoveVisitor;
import com.fairbg.bezma.core.model.PlayerId;

public class MovePrinter implements IMoveVisitor
{
    private StringBuilder m_builder = new StringBuilder();
    private String        m_moveString = null;
    private MatchParameters m_matchParameters = null;

    public MovePrinter(MatchParameters matchParameters)
    {
        m_matchParameters = matchParameters;
    }
    
    @Override
    public void visit(Move move)
    {
        m_builder = new StringBuilder();

        m_builder.append(move.getPlayer() == PlayerId.SILVER ? m_matchParameters.silverPlayerName : m_matchParameters.redPlayerName);
        m_builder.append(System.getProperty("line.separator"));
        
        m_builder.append("[" + move.getDie1() + ":" + move.getDie2() + "]");

        if (move.getMovements().length == 0)
        {
            m_builder.append(" skip move");
        }
        else
        {
            for (Movement movement : move.getMovements())
            {
                movement.accept(this);
            }
        }

        m_moveString = m_builder.toString();
    }

    @Override
    public void visit(Movement movement)
    {
        m_builder.append(" " + movement.MoveFrom + "/" + (movement.MoveTo > 0 ? movement.MoveTo : "off"));
        m_builder.append(movement.Strike ? "* " : " ");
    }

    String getMoveString()
    {
        return m_moveString;
    }

    public String printMove(MoveAbstract move)
    {
        m_moveString = null;
        
        move.accept(this);

        return getMoveString();
    }

    @Override
    public void visit(MoveCubeDouble move)
    {
        m_builder = new StringBuilder();
        m_builder.append(move.getPlayer() == PlayerId.SILVER ? m_matchParameters.silverPlayerName : m_matchParameters.redPlayerName);
        m_builder.append(": double " + move.getCubeValue());

        m_moveString = m_builder.toString();        
    }

    @Override
    public void visit(MoveCubeTake move)
    {
        m_builder = new StringBuilder();
        m_builder.append(move.getPlayer() == PlayerId.SILVER ? m_matchParameters.silverPlayerName : m_matchParameters.redPlayerName);
        m_builder.append(": take " + move.getCubeValue());

        m_moveString = m_builder.toString();        
    }

    @Override
    public void visit(MoveCubePass move)
    {
        m_builder = new StringBuilder();
        m_builder.append(move.getPlayer() == PlayerId.SILVER ? m_matchParameters.silverPlayerName : m_matchParameters.redPlayerName);
        m_builder.append(": pass " + move.getCubeValue());

        m_moveString = m_builder.toString();        
    }
    
    @Override
    public void visit(MoveFinishGame move)
    {
        m_builder = new StringBuilder();
        m_builder.append(move.getPlayer() == PlayerId.SILVER ? m_matchParameters.silverPlayerName : m_matchParameters.redPlayerName);
        m_builder.append(" won " + move.getPoints() + " point");

        if (move.getPoints() > 1)
        {
            m_builder.append("s");
        }

        m_moveString = m_builder.toString();        
    }
    
    @Override
    public void visit(MoveStartGame move)
    {
        m_moveString = "start game";
    }
}
