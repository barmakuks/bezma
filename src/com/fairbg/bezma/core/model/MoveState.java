package com.fairbg.bezma.core.model;

import com.fairbg.bezma.log.BezmaLog;

public class MoveState implements IAutomatState {

	@Override
	public boolean processCommand(GameBox gameBox, ModelCommand command) {
		
		BezmaLog.i("AUTOMAT", "Find move from: " + gameBox.getModelState().getPosition().toString());
		BezmaLog.i("AUTOMAT", "Find move to: " + command.getPosition().toString());
		BezmaLog.i("AUTOMAT", "for player: " + command.player.toString());

	
		Move move = gameBox.findMove(command.getPosition(), command.player);
		
		if (move != null)
		{
			BezmaLog.i("AUTOMAT", "Move found");
			// Change current state to MOVE
			
			gameBox.appendMove(move);
			
			IAutomatState state = gameBox.selectAutomatState(AutomatStates.MOVE);
			gameBox.setCurrentAutomatState(state);
				
			ModelState modelState = new ModelState(command.getPosition(), "");
			gameBox.setModelState(modelState);
			
			return true;			
		}
		BezmaLog.i("AUTOMAT", "Move not found");
		
		return false;		
		
	}

}
