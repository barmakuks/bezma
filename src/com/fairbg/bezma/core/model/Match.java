package com.fairbg.bezma.core.model;

import com.fairbg.bezma.core.backgammon.Move;
import com.fairbg.bezma.core.backgammon.Movement;
import com.fairbg.bezma.log.BezmaLog;

public class Match
{

    public void appendMove(IMove move)
    {
        MovePrinter printer = new MovePrinter();
        move.accept(printer);
    }

}

class MovePrinter implements IMoveVisitor
{
    StringBuilder sb = null;

    @Override
    public void visit(Move move)
    {
        sb = new StringBuilder();
        sb.append("(" + move.getDie1() + ";" + move.getDie2() + ") : ");

        for (Movement movement : move.getMovements())
        {
            if (movement != null)
            {
                movement.accept(this);
                sb.append(", ");
            }
        }

        BezmaLog.i("MOVE", sb.toString());

        sb = null;
    }

    @Override
    public void visit(Movement movement)
    {
        sb.append(movement.MoveFrom + " >> " + movement.MoveTo);
        if (movement.Strike)
        {
            sb.append("*");
        }
    }

}