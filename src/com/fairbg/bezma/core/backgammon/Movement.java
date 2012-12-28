package com.fairbg.bezma.core.backgammon;

import com.fairbg.bezma.core.model.IMoveVisitor;
import com.fairbg.bezma.core.model.PlayerColors;

public class Movement implements Cloneable{
    public int MoveFrom = 0, MoveTo = 0;
    public boolean Strike = false;
    public PlayerColors Color = PlayerColors.NONE;

    @Override
    protected Object clone() throws CloneNotSupportedException {
    	Movement clone = new Movement();
    	clone.MoveFrom = this.MoveFrom;
    	clone.MoveTo = this.MoveTo;
    	clone.Strike = this.Strike;
    	clone.Color = this.Color;
    	return clone;
    }

	public static boolean isEquals(Movement first, Movement second) {
		
		if (first == null && second == null)
		{
			return true;
		}
		
		if (first != null)
		{
			return first.equals(second);
		}
		
		return false;
	}
	
	@Override
	public boolean equals(Object o) {
		
		Movement other = (Movement) o;
		
		if (other == null)
		{
			return false;
		}
		
		return this.MoveFrom == other.MoveFrom && 
				this.MoveTo == other.MoveTo && 
				this.Strike == other.Strike &&
				this.Color == other.Color;
	}
	
	public void accept(IMoveVisitor visitor)
	{
		visitor.visit(this);
	}
}
