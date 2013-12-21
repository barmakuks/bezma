package com.fairbg.bezma.core.backgammon;

import com.fairbg.bezma.core.model.IMoveVisitor;
import com.fairbg.bezma.core.model.MoveAbstract;

public class MoveCubeTake extends MoveAbstract
{

    @Override
    public void accept(IMoveVisitor visitor)
    {
        visitor.visit(this);
    }

}
