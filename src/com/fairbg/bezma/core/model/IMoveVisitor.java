package com.fairbg.bezma.core.model;

import com.fairbg.bezma.core.backgammon.Move;
import com.fairbg.bezma.core.backgammon.Movement;

public interface IMoveVisitor {

	void visit(Move move);

	void visit(Movement movement);
	
}
