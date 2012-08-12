package com.fairbg.bezma.core.from_net;

import com.fairbg.bezma.core.model.PlayerColors;
/** 
 * Класс для сохранения одного хода
 * */
public class TBzmMove
{
    //PRIVATE 
    private TBzmMovement[] m_Movements = new TBzmMovement[4];
    public int[] FDice = new int[4];
    private void OrderMovements()
    {
        for(int i = 0; i<=2 ; i++)
        {
            for(int j = i + 1; j <=3; j++)
            {
                if ((!m_Movements[i].isEmpty()) && (!m_Movements[j].isEmpty()) && 
                    (m_Movements[i].MoveFrom > m_Movements[j].MoveFrom))
                {
                    TBzmMovement temp_movement = m_Movements[i];
                    int temp_die = FDice[i];
                    m_Movements[i] = m_Movements[j];
                    FDice[i] = FDice[j];
                    m_Movements[j] = temp_movement;
                    FDice[j] = temp_die;
                }
            }
        }
    }
    //PROTECTED
    protected byte Die1, Die2;
    protected PlayerColors FPlayerColor;
    protected void InitDice()
    {
        FDice[0] = Die1;
        FDice[1] = Die2;
        if (Die1 == Die2)
            {FDice[2] = Die1; FDice[3] = Die2;}
        else
            {FDice[2] = 0; FDice[3] = 0;}
    }

    //CONSTRUCTOR
    public TBzmMove(byte aDie1, byte aDie2, PlayerColors aColor) 
    {
        Die1 = aDie1; Die2 = aDie2; FPlayerColor = aColor;
        for (int i = 0; i < 4; i++)
            m_Movements[i] = new TBzmMovement();
            InitDice();
    }
    //PUBLIC
    public int MoveNo;
    public IBzmBoardPosition PositionBeforeMove = null;
    public IBzmBoardPosition PositionAfterMove = null;
    public boolean HasAlternativeDice = false;

    public int getMovementCount() 
    {
            int res = 0;
            for(int i = 0; i < 4; i++)
                if (!m_Movements[i].isEmpty())
                    res++;
            return res;
    }
    public TBzmMovement Movement(int anIndex) 
    {
        if (anIndex >= 0 && anIndex <= 3 && m_Movements[anIndex] != null)
            return m_Movements[anIndex];
        else
            return null;
    }
    public byte GetDie(int aNo)
    {
        switch (aNo)
        {
            case 1: return Die1;
            case 2: return Die2;
            default : return 0;
        }
    }
    public boolean AddMovement(int aFrom , int aTo, boolean aStrike)
    {
        for(int i = 0; i < 4 ;i++)
        {
            if(m_Movements[i].isEmpty() && (aTo - aFrom == FDice[i]))
            {
                m_Movements[i].MoveFrom = aFrom;
                m_Movements[i].MoveTo = aTo;
                m_Movements[i].Color = FPlayerColor;
                m_Movements[i].Strike = aStrike;
                OrderMovements();
                return true;
            }
        }
        return false;

    }
    public boolean CanAddMovement(int aFrom, int aTo) 
    {
      for(int i =0; i<=3;i ++)
          if(FDice[i] == (aTo-aFrom) && (m_Movements[i].isEmpty()))
              return true;
      return false;
    }
    public void Clear() 
    {
        for (int i = 0; i < 4; i++)
            m_Movements[i].Clear();
        InitDice();
    }
    public TBzmMove Clone() 
    {
        TBzmMove aCopy = new TBzmMove(Die1,Die2,FPlayerColor);
        for (int i = 0; i < 4; i++)
            aCopy.m_Movements[i] = new TBzmMovement(m_Movements[i]);
        aCopy.MoveNo = MoveNo;
        return aCopy;
    }
    public boolean EqualDice(byte aDie1, byte aDie2) 
    {
        return (Die1 == aDie1 && Die2 == aDie2) || (Die1 == aDie2 && Die2 == aDie1);
    }
    public boolean Equal(TBzmMove aCopy) 
    {
        for ( int i = 0; i<=3;i++)
            if(aCopy.FDice[i] != FDice[i] || 
                !TBzmMovement.Equal(m_Movements[i],aCopy.m_Movements[i]))
                return false;
        return true;
    }
    public void InitDice(byte aDie1, byte aDie2, PlayerColors aColor) 
    {
        Die1 = aDie1; Die2 = aDie2; FPlayerColor = aColor;
        InitDice();
    }
    @Override public String toString() 
    {
    	return "";/*
        char separator = ' ';
        string format_string = "{0}({1},{2})";
        string res = String.Format(format_string, TBzmPlayerColorUtils.GetPlayerShortColorString(FPlayerColor), Die1, Die2);
        for (int i = 0; i < 4; i++)
        {
            if (!FMovements[i].IsEmpty)
            {
                if (FMovements[i].MoveFrom == 0)
                    res = res + String.Format("{0}bar/{1}{2}", separator, 25 - FMovements[i].MoveTo, FMovements[i].Strike ? "*" : "");
                else
                    if (FMovements[i].MoveTo > 24)
                        res = res + String.Format("{0}{1}/off", separator, 25 - FMovements[i].MoveFrom);
                    else
                        res = res + String.Format("{0}{1}/{2}{3}", separator, 25 - FMovements[i].MoveFrom, 25 - FMovements[i].MoveTo, FMovements[i].Strike ? "*" : "");
                separator = ';';
            }
        }
        return res;*/
    }
    public void Applay(IBzmBoardPosition aBoardPosition) 
    {
        for(int i=0;i<4;i++)
        {
            if(!m_Movements[i].isEmpty())
            {
                aBoardPosition.ApplayMovement(m_Movements[i].MoveFrom,m_Movements[i].MoveTo,m_Movements[i].Color);
            }
        }
    }

    //PROPERTIES
    public PlayerColors getColor() 
    {
        return FPlayerColor; 
    }
    
    public TBzmMovement get(int index) 
    {
        return m_Movements[index];
    }
    public void set(int index, TBzmMovement mvmnt)
    {
    	m_Movements[index] = mvmnt;
    }
}
