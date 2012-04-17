package com.fairbg.bezma.core.from_net;

import com.fairbg.bezma.core.from_net.enums.PlayerColors;

public class TBzmBackgammonBoardPosition implements IBzmBoardPosition {

	public TBzmBackgammonBoardPosition(boolean reflect) {
		// TODO Auto-generated constructor stub
	}

	public TBzmBackgammonBoardPosition() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean getReflect() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setReflect(boolean reflcet) {
		// TODO Auto-generated method stub

	}


	@Override
	public boolean ApplayMovement(int aMoveFrom, int aMoveTo, PlayerColors aPlayerId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void ClearBoard() {
		// TODO Auto-generated method stub

	}

	@Override
	public IBzmBoardPosition Clone() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void Clone(IBzmBoardPosition aCopy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void ReadState(TBzmStateList fStates, int anIndex) {
		// TODO Auto-generated method stub

	}

	@Override
	public int GetPips(PlayerColors aPlayerId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int OffCheckersCount(PlayerColors aPlayerId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean Like(IBzmBoardPosition anOtherBoard) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean Equal(IBzmBoardPosition anOtherBoard) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean AbsEqual(IBzmBoardPosition anOtherBoard) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getStateString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setStateString(String stateString) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean HasCheckerInBar(PlayerColors aPlayerId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public TBzmCheckerPosition getCheckersPosition(int aPos, PlayerColors aPlayerId) {
		// TODO Auto-generated method stub
		return null;
	}

}
