package com.fairbg.bezma.core.model;

public class Move implements Cloneable {
    private Movement[] m_Movements = new Movement[4];
    private int[] m_Dice = new int[4];
    private PlayerColors m_Player;
    private int m_Die1, m_Die2;

    public PlayerColors getPlayer() {
		return m_Player;
	}

	public void setPlayer(PlayerColors player) {
		m_Player = player;
	}

	public Move()
    {
    }
    
    public Movement getMovement(int index)
    {
    	return m_Movements[index];
    }
    
    public void setMovement(int index, Movement movement)
    {
    	m_Movements[index] = movement; 
    }
    
    public int getDieValue(int index)
    {
    	return m_Dice[index];
    }
    public void setDieValue(int index, int value)
    {
    	m_Dice[index] = value;
    }
    
    @Override
    protected Object clone() throws CloneNotSupportedException {
    	
    	Move clone = new Move();
    	
    	clone.m_Player = this.m_Player;
    	
    	clone.m_Die1 = this.m_Die1;
    	clone.m_Die2 = this.m_Die2;
    	
    	for (int i = 0; i < this.m_Dice.length; ++i)
    	{
    		clone.m_Dice[i] = this.m_Dice[i];    		
    	}
    	
    	for (int i = 0; i < this.m_Movements.length; ++i)
    	{
    		clone.m_Movements[i] = this.m_Movements[i] != null ? (Movement) this.m_Movements[i].clone() : null;
    	}
    	return clone;
    }

    @Override
    public boolean equals(Object o) {
    	Move other = (Move)o;
    	
        for ( int i = 0; i <= 3; i++)
        {        	
            if (other.m_Dice[i] != this.m_Dice[i])
            	return false;
            
            if(!Movement.isEquals(m_Movements[i], other.m_Movements[i]))
            {
            	return false;
            }
        }
        return true;
    }
    
    public void updateDice()
    {
    	m_Dice[0] = m_Die1;
    	m_Dice[1] = m_Die2;
    	if (m_Die1 == m_Die2)
    	{
    		m_Dice[2] = m_Dice[3] = m_Die1;
    	}
    	else
    	{
    		m_Dice[2] = m_Dice[3] = 0;
    	}
    }
    
    public void setDice(int die1, int die2)
    {
    	m_Die1 = die1; m_Die2 = die2;
    	updateDice();
    }
    
	public boolean appendMovement(int from_index, int to_index, boolean isStrike) {
        for(int i = 0; i < 4; i++)
        {
            if(m_Movements[i] == null && (from_index - to_index == m_Dice[i]))
            {
            	Movement movement = new Movement();
            	movement.MoveFrom = from_index;
            	movement.MoveTo = to_index;
            	movement.Color = m_Player;
            	movement.Strike = isStrike;
                m_Movements[i] = movement;
                orderMovements();
                return true;
            }
        }
        return false;
	}

	private void orderMovements() {

		for(int i = 0; i <= 2; i++)
        {
            for(int j = i + 1; j <=3; j++)
            {
                if ((m_Movements[i]!= null) && (m_Movements[j]!= null) && 
                    (m_Movements[i].MoveFrom > m_Movements[j].MoveFrom))
                {
                    Movement temp_movement = m_Movements[i];
                    int temp_die = m_Dice[i];
                    m_Movements[i] = m_Movements[j];
                    m_Dice[i] = m_Dice[j];
                    m_Movements[j] = temp_movement;
                    m_Dice[j] = temp_die;
                }
            }
        }
	}
	
	public void clear()
	{
        for (int i = 0; i < 4; i++)
        {
        	m_Movements[i] = null;
        }            
        updateDice();		
	}
	
	public void accept(IMoveVisitor visitor)
	{
		visitor.visit(this);
	}

	public void setMovement(int i, int from_index, int dest_index, boolean isStrike) {
            if(m_Movements[i] == null && (from_index - dest_index == m_Dice[i]))
            {
            	Movement movement = new Movement();
            	movement.MoveFrom = from_index;
            	movement.MoveTo = dest_index;
            	movement.Color = m_Player;
            	movement.Strike = isStrike;
                m_Movements[i] = movement;
            }
	}
}
