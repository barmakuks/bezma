package com.fairbg.bezma.core.from_net;

import com.fairbg.bezma.core.MatchIdentifier;
import com.fairbg.bezma.core.from_net.enums.RollTypes;

public class TBzmDiceSetParameters {
    public RollTypes DiceSetType = RollTypes.Generate;
    public MatchIdentifier Guid = new MatchIdentifier();
    public String Filename = "";
    public void Copy(TBzmDiceSetParameters aCopy) 
    {
        try {
            DiceSetType = aCopy.DiceSetType;
			Guid = (MatchIdentifier) aCopy.Guid.clone();
	        Filename = aCopy.Filename;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
    }

}
