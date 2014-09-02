package com.fairbg.bezma.core.backgammon;

import java.util.ArrayList;

import com.fairbg.bezma.core.model.MoveAbstract;
import com.fairbg.bezma.core.model.IMoveVisitor;

public class Move extends MoveAbstract implements Cloneable 
{
    private ArrayList<Movement> m_Movements = new ArrayList<Movement>();
    private ArrayList<Integer> m_Dice = new ArrayList<Integer>();
    private int m_Die1, m_Die2;

    public Move()
    {
    }

    public Movement[] getMovements()
    {
        return m_Movements.toArray(new Movement[0]);
    }

    public Integer[] getPossibleDies()
    {
        return m_Dice.toArray(new Integer[0]);
    }

    public int getDie1()
    {
        return m_Die1;
    }

    public int getDie2()
    {
        return m_Die2;
    }

    public int possibleMovementsLeft()
    {
        return m_Dice.size();
    }

    @Override
    public Object clone() throws CloneNotSupportedException
    {

        Move clone = new Move();

        clone.m_Player = this.m_Player;

        clone.m_Die1 = this.m_Die1;
        clone.m_Die2 = this.m_Die2;

        for (int i = 0; i < this.m_Dice.size(); ++i)
        {
            clone.m_Dice.add(i, this.m_Dice.get(i));
        }

        for (int i = 0; i < this.m_Movements.size(); ++i)
        {
            clone.m_Movements.add(i, (Movement) this.m_Movements.get(i).clone());
        }
        return clone;
    }

    @Override
    public boolean equals(Object o)
    {

        Move other = (Move) o;

        // compare dice

        boolean result = (other.m_Die1 == this.m_Die1 && other.m_Die2 == this.m_Die2) || (other.m_Die1 == this.m_Die2 && other.m_Die2 == this.m_Die1);

        if (!result)
        {
            return false;
        }

        if (m_Movements.size() == other.m_Movements.size())
        {
            for (int i = 0; i < m_Movements.size(); i++)
            {
                if (!Movement.isEquals(m_Movements.get(i), other.m_Movements.get(i)))
                {
                    return false;
                }
            }
            return true;
        }

        return false;
    }

    public void updateDice()
    {
        m_Movements.clear();
        m_Dice.clear();

        m_Dice.add(0, m_Die1);
        m_Dice.add(1, m_Die2);

        if (m_Die1 == m_Die2)
        {
            m_Dice.add(2, m_Die1);
            m_Dice.add(3, m_Die2);
        }
    }

    public void setDice(int die1, int die2)
    {
        m_Die1 = die1;
        m_Die2 = die2;
        updateDice();
    }

    public boolean appendMovement(int from_index, int to_index, boolean isStrike)
    {
        for (int i = 0; i < m_Dice.size(); i++)
        {
            if (m_Dice.get(i) == from_index - to_index)
            {
                Movement movement = new Movement();
                movement.MoveFrom = from_index;
                movement.MoveTo = to_index;
                movement.Color = m_Player;
                movement.Strike = isStrike;
                m_Movements.add(movement);
                m_Dice.remove(i);
                return true;
            }
        }

        return false;
    }

    /*
     * private void orderMovements() {
     * 
     * for(int i = 0; i <= 2; i++) { for(int j = i + 1; j <=3; j++) { if
     * ((m_Movements[i]!= null) && (m_Movements[j]!= null) &&
     * (m_Movements[i].MoveFrom > m_Movements[j].MoveFrom)) { Movement
     * temp_movement = m_Movements[i]; int temp_die = m_Dice[i]; m_Movements[i]
     * = m_Movements[j]; m_Dice[i] = m_Dice[j]; m_Movements[j] = temp_movement;
     * m_Dice[j] = temp_die; } } } }
     */
    public void clear()
    {
        m_Movements.clear();
        m_Dice.clear();
        updateDice();
    }

    public void accept(IMoveVisitor visitor)
    {
        visitor.visit(this);
    }

}