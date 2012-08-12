package com.fairbg.bezma.core.model;


enum AutomatStates{BEGIN, MOVE, DICE};

public interface IAutomatState {
	boolean processCommand(GameBox gameBox, ModelCommand modelCommand);
}
