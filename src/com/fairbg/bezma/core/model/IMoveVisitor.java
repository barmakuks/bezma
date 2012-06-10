package com.fairbg.bezma.core.model;

public interface IMoveVisitor {

	void visit(Move move);

	void visit(Movement movement);
	
}
