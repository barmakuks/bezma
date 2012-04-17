package com.fairbg.bezma.core.from_net;

import com.fairbg.bezma.core.from_net.enums.Rules;
import com.fairbg.bezma.core.from_net.enums.PlayerColors;

/**Интерфейс для правил игры*/
public interface IBzmRules {
    Rules getRulesType();
    IBzmBoardPosition StartPosition();
    TBzmMove FindMove(byte aDie1, byte aDie2, PlayerColors anActivePlayerId, IBzmBoardPosition aPositionFrom, IBzmBoardPosition aPositionTo);
    boolean StillCanDoMovement(TBzmMove move, IBzmBoardPosition aBoardPosition);
    boolean CanOffChecker(IBzmBoardPosition aPosition, PlayerColors aPlayerId);

}
