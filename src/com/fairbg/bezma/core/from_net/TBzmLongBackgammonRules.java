package com.fairbg.bezma.core.from_net;

import com.fairbg.bezma.core.from_net.enums.Rules;
import com.fairbg.bezma.core.from_net.enums.PlayerColors;

public class TBzmLongBackgammonRules implements IBzmRules {

	@Override
	public Rules getRulesType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IBzmBoardPosition StartPosition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TBzmMove FindMove(byte aDie1, byte aDie2, PlayerColors anActivePlayerId, IBzmBoardPosition aPositionFrom,
			IBzmBoardPosition aPositionTo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean StillCanDoMovement(TBzmMove move, IBzmBoardPosition aBoardPosition) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean CanOffChecker(IBzmBoardPosition aPosition, PlayerColors aPlayerId) {
		// TODO Auto-generated method stub
		return false;
	}

}
