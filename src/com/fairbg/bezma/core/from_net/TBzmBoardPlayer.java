package com.fairbg.bezma.core.from_net;

import com.fairbg.bezma.core.from_net.enums.Buttons;
import com.fairbg.bezma.core.from_net.enums.PlayerColors;
import com.fairbg.bezma.core.from_net.enums.PlayerStates;

public class TBzmBoardPlayer {
    private PlayerColors FPlayerId;
    private String FPlayerName = "";
    private TBzmRateDie FRateDie;
    private PlayerStates FPlayerState;
    /*private TBzmButtonsSet FHighlightedButtons = new TBzmButtonsSet();
    private TBzmButtonsSet FEnabledButtons = new TBzmButtonsSet();*/
    private String FDisplayText;
    private boolean FPaused;
    private short FMaxSeconds;
    private short FSeconds;
    private int FPips;
    /*private TBzmButtons AllDiceButtons 
    {
        get 
        {
            return UseRollDice ? TBzmButtons.Roll : TBzmButtons.None;// bcAllDice;
        }
    }*/
    public boolean UseRollDice;
    public String getPlayerName() 
    {
         return FPlayerName; 
    }
    
    public void setPlayerName(String playerName)
    {
        FPlayerName = playerName; 
    }
    private void SetPlayerState(PlayerStates aPlayerState) 
    {
        FPlayerState = aPlayerState;
        /** @todo
        switch(FPlayerState)
        {
            case TBzmPlayerState.Look:
                FEnabledButtons.Clear();
                FHighlightedButtons.Clear();
                FSeconds = FMaxSeconds;
                FDisplayText = TBzmConst.cstrNone;
                break;
            case TBzmPlayerState.None:
                FEnabledButtons.Set(TBzmButtons.Ok,TBzmButtons.Position);
                FHighlightedButtons.Clear();
                FSeconds = FMaxSeconds;
                FDisplayText = PlayerName + ": " + TBzmConst.cstrStart;
                break;

            case TBzmPlayerState.PlayerNotSelected:
                FEnabledButtons.Set(AllDiceButtons);
                FSeconds = FMaxSeconds;
                FDisplayText = TBzmConst.cstrPlayerNotSelected;
                break;
            case TBzmPlayerState.WaitForMoveAccept:
                FEnabledButtons.Clear();
                FDisplayText = TBzmConst.cstrWaitForMoveAccept;
                break;
            case TBzmPlayerState.Inactive: 
                FEnabledButtons.Clear();
                FDisplayText = String.Format(TBzmConst.cstrInactive,Pips);
                break;
            case TBzmPlayerState.DiceOrDouble:
                FEnabledButtons.Set(AllDiceButtons);
                if (FRateDie.CanDouble(FPlayerId))
                    FEnabledButtons.Add(TBzmButtons.Double);
                FDisplayText = TBzmConst.cstrDice;
                break;
            case TBzmPlayerState.Dice:
                FEnabledButtons.Set(AllDiceButtons);
                FDisplayText = TBzmConst.cstrDice;
                break;
            case TBzmPlayerState.FirstDice:
                FEnabledButtons.Set(AllDiceButtons, TBzmButtons.Clear);
                FDisplayText = TBzmConst.cstrDice;
                break;
            case TBzmPlayerState.Roll:
                FEnabledButtons.Set(TBzmButtons.Roll, TBzmButtons.Ok, TBzmButtons.Pass, TBzmButtons.Clear);
                FDisplayText = TBzmConst.cstrRoll;
                break;
            case TBzmPlayerState.MoveMaking:
            case TBzmPlayerState.WrongMoveMade:
                if (AllDiceButtons != TBzmButtons.Roll)
                    FEnabledButtons.Set(AllDiceButtons, TBzmButtons.Ok);
                else
                    FEnabledButtons.Set(TBzmButtons.Ok);
                FDisplayText = TBzmConst.cstrMoveMaking;
                break;
            case TBzmPlayerState.DoubleConfirm:
                FEnabledButtons.Set(TBzmButtons.Ok, TBzmButtons.Clear);
                FDisplayText = TBzmConst.cstrDoubleConfirm;
                break;
            case TBzmPlayerState.TakeConfirm:
                FEnabledButtons.Set(TBzmButtons.Ok, TBzmButtons.Clear);
                FDisplayText = TBzmConst.cstrTakeConfirm;
                break;
            case TBzmPlayerState.PassConfirm:
                FEnabledButtons.Set(TBzmButtons.Ok, TBzmButtons.Clear);
                FDisplayText = TBzmConst.cstrPassConfirm;
                break;
            case TBzmPlayerState.DoubleAccepting:
                FEnabledButtons.Set(TBzmButtons.Take, TBzmButtons.Pass);
                FDisplayText = TBzmConst.cstrDoubleAccepting;
                break;
            case TBzmPlayerState.DoubleAcceptingBeaver:
                FEnabledButtons.Set(TBzmButtons.Take, TBzmButtons.Pass);
                if (FRateDie.RateLevel != 64)
                    FEnabledButtons.Add(TBzmButtons.Beaver);
                FDisplayText = TBzmConst.cstrDoubleAccepting;
                break;
            case TBzmPlayerState.BeaverAccepting:
                FEnabledButtons.Set(TBzmButtons.Take, TBzmButtons.Pass, TBzmButtons.Clear);
                FDisplayText = TBzmConst.cstrBeaverAccepting;
                break;
            case TBzmPlayerState.SkipMoving:
                FEnabledButtons.Set(TBzmButtons.Ok, TBzmButtons.Clear, TBzmButtons.Pass);
                if (FRateDie.CanDouble(FPlayerId))
                    FEnabledButtons.Add(TBzmButtons.Double);
                FDisplayText = TBzmConst.cstrSkipMove;
                break;
            case TBzmPlayerState.MoveAccepting:
                FEnabledButtons.Set(AllDiceButtons, TBzmButtons.Clear, TBzmButtons.Pass);
                if (UseRollDice)
                    FEnabledButtons.Add(TBzmButtons.Ok);
                if (FRateDie.CanDouble(FPlayerId))
                    FEnabledButtons.Add(TBzmButtons.Double);
                FDisplayText = TBzmConst.cstrMoveAccepting;
                break;
            case TBzmPlayerState.MoveAcceptingOk:
                FEnabledButtons.Set(AllDiceButtons, TBzmButtons.Clear, TBzmButtons.Pass, TBzmButtons.Ok);
                if (FRateDie.CanDouble(FPlayerId))
                    FEnabledButtons.Add(TBzmButtons.Double);
                FDisplayText = TBzmConst.cstrMoveAcceptingOk;
                break;
            case TBzmPlayerState.LastMoveAccepting:
                FEnabledButtons.Set(TBzmButtons.Ok, TBzmButtons.Clear);
                FDisplayText = TBzmConst.cstrLastMoveAccepting;
                break;
            case TBzmPlayerState.InitBoard :
                FEnabledButtons.Set(TBzmButtons.Ok );
                FDisplayText = TBzmConst.cstrInitBoard;
                break;
            case TBzmPlayerState.PassAccept :
                FEnabledButtons.Set(TBzmButtons.Ok, TBzmButtons.Clear);
                FDisplayText = TBzmConst.cstrPassAccepting;
                break;
            case TBzmPlayerState.WaitForPassAccept:                  
                FEnabledButtons.Clear();
                FDisplayText = TBzmConst.cstrWaitForPassAccept;
                break;
            case TBzmPlayerState.PassGammonAccept:
                FEnabledButtons.Set(TBzmButtons.Ok, TBzmButtons.Clear);
                FDisplayText = TBzmConst.cstrPassGammonAccepting;
                break;
            case TBzmPlayerState.PassNormalAccept:
                FEnabledButtons.Set(TBzmButtons.Ok, TBzmButtons.Clear);
                FDisplayText = TBzmConst.cstrPassNormalAccepting;
                break;
            case TBzmPlayerState.PassTypeSelect:
                FEnabledButtons.Set(TBzmButtons.Die1, TBzmButtons.Die2, TBzmButtons.Clear);
                FDisplayText = TBzmConst.cstrPassTypeSelect;
                break;
        }
*/
    }
    private Buttons DieCode(byte aDieCode)
    {
    	/** @todo
        switch (aDieCode)
        {
            case 1: return TBzmButtons.Die1;
            case 2: return TBzmButtons.Die2;
            case 3: return TBzmButtons.Die3;
            case 4: return TBzmButtons.Die4;
            case 5: return TBzmButtons.Die5;
            case 6: return TBzmButtons.Die6;
        }
        */
        return Buttons.None;
    }

    // Constructors
    public TBzmBoardPlayer(PlayerColors aPlayerId, TBzmRateDie aRateDie) 
    {
        FPlayerState = PlayerStates.None;
        /// @todo FHighlightedButtons.Clear();
        FPlayerId = aPlayerId;
        FRateDie = aRateDie;
    }

    // Properties
    public PlayerStates getPlayerState() 
    {
        return FPlayerState; 
    }
    
    public void setPlayerState(PlayerStates state) 
    { 
    	SetPlayerState(state);
    }
    public boolean isPaused() 
    {
        return FPaused;
        }
    public void setPaused(boolean paused) 
    { 
    	FPaused = paused; 
    	SetPlayerState(FPlayerState);
    }
    public short getSeconds() 
    {
    	return FSeconds;
    }
    public PlayerColors getPlayerId() 
    {
    	return FPlayerId;
    }
    public int getPips() 
    { 
        return FPips;}
    public void setPips(int pips) 
    { 
    	FPips = pips;
    }
    public short getMaxSeconds() 
    {
        return FMaxSeconds;
        }
    public void setMaxSeconds(short maxSeconds) 
    { 
    	FMaxSeconds = maxSeconds; 
    	if (FSeconds == 0) FSeconds = maxSeconds; 
    }
    

    // Methods
    /** @todo
    public void OnPressButton(TBzmButtons aBtnCode) 
    {
        switch (aBtnCode) 
        {
            case TBzmButtons.Die1:
            case TBzmButtons.Die2:
            case TBzmButtons.Die3:
            case TBzmButtons.Die4:
            case TBzmButtons.Die5:
            case TBzmButtons.Die6:
            case TBzmButtons.Doublet:
                FHighlightedButtons.Invert(aBtnCode);
                break;
        }
    }
    */
    public void ClearDice() 
    {
    	/** @todo
        FHighlightedButtons.Delete(TBzmButtons.Die1,
                                    TBzmButtons.Die2,
                                    TBzmButtons.Die3,
                                    TBzmButtons.Die4,
                                    TBzmButtons.Die5,
                                    TBzmButtons.Die6,
                                    TBzmButtons.Doublet);
                                    */
    }
    public void Clone(TBzmBoardPlayer ADest) 
    {
        ADest.FPips = FPips;
        ///@todo FEnabledButtons.Clone(ADest.FEnabledButtons);
        /// @todo FHighlightedButtons.Clone(ADest.FHighlightedButtons);
        ADest.FDisplayText = FDisplayText;
        ADest.FPlayerState = FPlayerState;
        ADest.FPlayerId = FPlayerId;
        ADest.FSeconds = FSeconds;
    }

    public String getDisplayText() 
    {
    	/** @todo
            switch(PlayerState)
            {
                case TBzmPlayerState.Inactive: 
                    return String.Format(TBzmConst.cstrInactive,Pips);
                case TBzmPlayerState.MoveMaking:
                    byte aDie1, aDie2;
                    GetDice(out aDie1, out aDie2);
                    return String.Format(TBzmConst.cstrMoveMaking, aDie1, aDie2);
                default:
                    return FDisplayText;
            }
            */
    	return "";
    }

    public void setDisplayText(String text) 
    { 
    	FDisplayText = text; 
    }

    public void SetDice(byte aDie1, byte aDie2) 
    {
        ClearDice();
        if ((aDie1 > 6) || (aDie1 < 1) || (aDie2 > 6) || (aDie2 < 1))
        return;
      /// @todo FHighlightedButtons.Add(DieCode(aDie1));
      /// @todo FHighlightedButtons.Add(DieCode(aDie2));
      /// @todo if (aDie1 == aDie2)
      /// @todo FHighlightedButtons.Add(TBzmButtons.Doublet);

    }

    /** @todo public int GetDice(byte aDie1, byte aDie2) 
    {
        int res = 0;
        aDie1 = 0; aDie2 = 0;
        for(byte i = 1, mask = 1; i <= 6; i++)
        {
            if((mask & FHighlightedButtons.Mask) != 0)
            {
                res++;
                if(aDie1 == 0)
                    aDie1 = i;
                else
                    aDie2 = i;
            }
            mask <<= 1;
        }
        if (FHighlightedButtons.Contains(TBzmButtons.Doublet))
        {
            res++;
            aDie2 = aDie1;
        }
        return res;
    }
    public int HighlightedDice 
    {
        get { return FHighlightedButtons.Mask & Convert.ToInt32(TBzmButtons.AllDice); }
        set { FHighlightedButtons.Mask = FHighlightedButtons.Mask & ~(Convert.ToInt32(TBzmButtons.AllDice)) | value; }
    }
    public bool IsBtnActive(TBzmButtons aBtnCode) 
    {
        return FEnabledButtons.Contains(aBtnCode);
    }
    public bool IsBtnHighlighted(TBzmButtons aBtnCode)
    {
        return FHighlightedButtons.Contains(aBtnCode);
    }
    public bool Equal(TBzmBoardPlayer aCopy) 
    {
        return
            (FHighlightedButtons == aCopy.FHighlightedButtons) &&
            (FPlayerState == aCopy.FPlayerState) &&
            (FPaused == aCopy.FPaused) &&
            (FDisplayText == aCopy.FDisplayText);
    }
    public int HighlightedMask
    {
        get{
            int diceMask = FHighlightedButtons.Mask & 0x007F;
            if ((diceMask == 0) && ((FEnabledButtons.Mask & 0x007F) == 0x007F))
                diceMask = 0x007F;
            else diceMask = FHighlightedButtons.Mask;

            return diceMask +
            (FEnabledButtons.Mask & (int)TBzmButtons.Double) +
            (FEnabledButtons.Mask & (int)TBzmButtons.Take) +
            (FEnabledButtons.Mask & (int)TBzmButtons.Pass) +
            (FEnabledButtons.Mask & (int)TBzmButtons.Clear) +
            (FEnabledButtons.Mask & (int)TBzmButtons.Ok) +
            (FEnabledButtons.Mask & (int)TBzmButtons.Beaver) +
            (FEnabledButtons.Mask & (int)TBzmButtons.Roll) +
            (FEnabledButtons.Mask & (int)TBzmButtons.Die1) +
            (FEnabledButtons.Mask & (int)TBzmButtons.Die2);
        }            
    }
    public bool OnTimer() 
    {
        switch(FPlayerState)
        {
            case TBzmPlayerState.DiceOrDouble: 
            case TBzmPlayerState.Dice:
            case TBzmPlayerState.DoubleAcceptingBeaver:
            case TBzmPlayerState.BeaverAccepting: 
            case TBzmPlayerState.MoveMaking:
            case TBzmPlayerState.WrongMoveMade:
            case TBzmPlayerState.MoveAccepting:
                if (FSeconds != 0) 
                    FSeconds--;
                else
                    return true;
                break;
        }
        return false;
    }

    #region IBzmSerializableObject Members

    public void Serialize(System.IO.BinaryWriter writer)
    {
        writer.Write((byte)FPlayerId);
        writer.Write((byte)FPlayerState);
        writer.Write(FDisplayText);
        writer.Write(FPaused);
        writer.Write(FMaxSeconds);
        writer.Write(FSeconds);
        writer.Write(FPips);
        writer.Write(UseRollDice);
        writer.Write(FPlayerName);
        FHighlightedButtons.Serialize(writer);
        FEnabledButtons.Serialize(writer);
    }

    public void Deserialize(System.IO.BinaryReader reader)
    {
        FPlayerId = (TBzmPlayerColor)(reader.ReadByte());
        FPlayerState = (TBzmPlayerState)(reader.ReadByte());
        FDisplayText = reader.ReadString();
        FPaused = reader.ReadBoolean();
        FMaxSeconds = reader.ReadInt16();
        FSeconds = reader.ReadInt16();
        FPips = reader.ReadInt32();
        UseRollDice = reader.ReadBoolean();
        FPlayerName = reader.ReadString();
        FHighlightedButtons.Deserialize(reader);
        FEnabledButtons.Deserialize(reader);
    }

    #endregion
*/
}
