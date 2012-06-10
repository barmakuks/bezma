package com.fairbg.bezma.core.model;

import com.fairbg.bezma.log.BezmaLog;

public class Match {

	public void appendMove(Move move) {
		MovePrinter printer = new MovePrinter();
		move.accept(printer);		
	}

}

class MovePrinter implements IMoveVisitor
{
	StringBuilder sb = null;	
	@Override
	public void visit(Move move) {
		sb = new StringBuilder();
		sb.append("("+ move.getDieValue(0) + ";" + move.getDieValue(1) + ") : ");
		for(int i = 0 ; i < 4 ; ++i)
		{
			Movement movement = move.getMovement(i);
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
	public void visit(Movement movement) {
		sb.append(movement.MoveFrom + " >> " + movement.MoveTo);
		if (movement.Strike)
		{
			sb.append("*");
		}		
	}
	
}