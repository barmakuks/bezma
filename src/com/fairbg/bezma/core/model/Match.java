package com.fairbg.bezma.core.model;

import java.util.ArrayList;

public class Match
{    
    ArrayList<IMove> m_Moves = new ArrayList<IMove>();  
    
    public void appendMove(IMove move)
    {
	m_Moves.add(move);
    }
}