package com.fairbg.bezma.core.model;

import java.util.ArrayList;

public class Match
{
    ArrayList<MoveAbstract> m_Moves = new ArrayList<MoveAbstract>();

    public void appendMove(MoveAbstract move)
    {
        m_Moves.add(move);
    }
}