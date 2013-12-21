package com.fairbg.bezma.core.model;

import com.fairbg.bezma.core.backgammon.Move;
import com.fairbg.bezma.core.backgammon.MoveCubeDouble;
import com.fairbg.bezma.core.backgammon.MoveCubeTake;
import com.fairbg.bezma.core.backgammon.Movement;

public interface IMoveVisitor
{
    void visit(Move move);
    void visit(MoveCubeDouble move);
    void visit(MoveCubeTake move);
    void visit(Movement movement);
}
