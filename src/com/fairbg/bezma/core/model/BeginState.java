package com.fairbg.bezma.core.model;

import com.fairbg.bezma.log.BezmaLog;


public class BeginState implements IAutomatState {

	@Override
	public boolean processCommand(GameBox gameBox, ModelCommand command) {
		// Check if this is start position
		BezmaLog.i("AUTOMAT", "Process Begin State");
		if (gameBox.checkStartPosition(command.getPosition()) )
		{
			BezmaLog.i("AUTOMAT", "CHANGE STATE TO MOVE_STATE");
			// Change current state to MOVE		
			IAutomatState state = gameBox.selectAutomatState(AutomatStates.MOVE);
			gameBox.setCurrentAutomatState(state);
			
			ModelState modelState = new ModelState(command.getPosition(), "");
			gameBox.setModelState(modelState);
			return true;
		}
		else
		{
			BezmaLog.i("AUTOMAT", "STATE STILL  BEGIN_STATE");
			ModelState modelState = new ModelState(command.getPosition(), "");
			gameBox.setModelState(modelState);			
		}
		
		return false;
	}

}
