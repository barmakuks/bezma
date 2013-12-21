package com.fairbg.bezma.core.backgammon;

import com.fairbg.bezma.core.model.IMoveVisitor;
import com.fairbg.bezma.core.model.MoveAbstract;

public class MoveCubeDouble extends MoveAbstract
{
    public void accept(IMoveVisitor visitor)
    {
        visitor.visit(this);
    }
}
