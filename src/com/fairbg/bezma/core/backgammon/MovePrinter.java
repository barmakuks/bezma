package com.fairbg.bezma.core.backgammon;

import com.fairbg.bezma.core.model.MoveAbstract;
import com.fairbg.bezma.core.model.IMoveVisitor;
import com.fairbg.bezma.core.model.PlayerId;

public class MovePrinter implements IMoveVisitor
{
    private StringBuilder m_builder = new StringBuilder();
    private String        m_moveString;

    @Override
    public void visit(Move move)
    {
        m_builder = new StringBuilder();

        m_builder.append(move.getPlayer() == PlayerId.WHITE ? "W: " : "B: ");
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

    public static String printMove(MoveAbstract move)
    {
        MovePrinter printer = new MovePrinter();

        move.accept(printer);

        return printer.getMoveString();
    }

    @Override
    public void visit(MoveCubeDouble move)
    {
        m_builder = new StringBuilder();
        m_builder.append(move.getPlayer() == PlayerId.WHITE ? "W: " : "B: ");
        m_builder.append(" double");

        m_moveString = m_builder.toString();        
    }

    @Override
    public void visit(MoveCubeTake move)
    {
        m_builder = new StringBuilder();
        m_builder.append(move.getPlayer() == PlayerId.WHITE ? "W: " : "B: ");
        m_builder.append(" take");

        m_moveString = m_builder.toString();        
    }

    @Override
    public void visit(MoveCubePass move)
    {
        m_builder = new StringBuilder();
        m_builder.append(move.getPlayer() == PlayerId.WHITE ? "W: " : "B: ");
        m_builder.append(" pass");

        m_moveString = m_builder.toString();        
    }
}
