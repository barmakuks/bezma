package com.fairbg.bezma.core.from_net;

import com.fairbg.bezma.core.model.PlayerColors;

public class TBzmRateDie {
	
    private PlayerColors FLastOwner; // предыдущий владелец кубика, если 0 - то кубик был ничей
    private byte FRateLevel; // уровень стоимости партии
    private PlayerColors FPlayerOwner ; // текущий владелец кубика, если 0 - то кубик был ничей
    private boolean FRateInTheGame; // если true, то кубик в игре, иначе кубик не участвет в игре
    private byte FMaxRateForWhite = 64; // Максимально возможная ставка для игрока White
    private byte FMaxRateForBlack = 64; // Максимально возможная ставка для игрока Black

    
    /// @todo public event TBzmEvent OnRateDieChanged; 
    
    public TBzmRateDie()
    {
        FRateInTheGame = true; // Изначально куб находится в игре
        InitRateDie();
    }
    
    public void InitRateDie() 
    {
        FLastOwner = PlayerColors.NONE; // кубик никому не принадлежит
        FPlayerOwner = PlayerColors.NONE; // кубиком до этого никто не владел
        FRateLevel = 1; // начальная стоимость 1            
    }

    public boolean getRateInTheGame() 
    {
            return FRateInTheGame;
    }
      
    public void setRateInTheGame(boolean value)
    {
            FRateInTheGame = value;
    }
    public byte getRateLevel()
    {
            return FRateLevel;
    }
    public void setRateLevel(byte rate){
            FRateLevel = rate;
            
    /** @todo if (OnRateDieChanged != null)
                OnRateDieChanged();
                */
    }
    public PlayerColors getPlayerOwner()
    {
        return FPlayerOwner; 
    }
    public void setPlayerOwner(PlayerColors player)
        {
            FPlayerOwner = player;
            FLastOwner = FPlayerOwner;
            /** @todo
            if (OnRateDieChanged != null)
                OnRateDieChanged();
                */
    }
    public PlayerColors getLastOwner()
    {
        return FLastOwner; 
    }
    public void setLastOwner(PlayerColors player)
    {
    	FLastOwner = player;
    }
    public String getStateString() 
    {
    	return "";
    	/** @todo
            if (getRateInTheGame())
                return TBzmStringUtils.StrPrint((short)(((int)FLastOwner << 12) + ((int)FPlayerOwner << 8) + FRateLevel));
            else
                return TBzmStringUtils.StrPrint((short)0);
                */
    }
    public void setStateString(String stateString) {
    	/** @todo
            int temp = Int16.Parse(value.Substring(0,4), System.Globalization.NumberStyles.HexNumber);
            FLastOwner = (TBzmPlayerColor)((temp & 0xF000) >> 12);
            FPlayerOwner = (TBzmPlayerColor)((temp & 0x0F00) >> 8);
            FRateLevel = (byte)(temp & 0x00FF);
            if (FRateLevel == 0)
            {
                FRateInTheGame = false;
                FRateLevel = 1;
            }
            else
                FRateInTheGame = true;
                */
    }
    public byte getMaxRateForBlack() 
    {
        return FMaxRateForBlack;
    }
    
    public void setMaxRateForBlack(byte maxRate)
    { 
    	FMaxRateForBlack = maxRate;
    }
    public byte getMaxRateForWhite()
    {
        return FMaxRateForWhite; 
    }

    public void setMaxRateForWhite(byte maxRate) 
    { 
    	FMaxRateForWhite = maxRate; 
    }

    public boolean CanDouble(PlayerColors aPlayerId) 
    {
        byte max_rate = 64;
        switch (aPlayerId)
        { 
            case BLACK:
                max_rate = getMaxRateForBlack();
                break;
            case WHITE:
                max_rate = getMaxRateForWhite();
                break;
        }
        return (FRateInTheGame && (FPlayerOwner == PlayerColors.NONE) || (FPlayerOwner == aPlayerId)) && (FRateLevel < max_rate);
    }
    public boolean Equal(TBzmRateDie aCopy) 
    {
        return  (FPlayerOwner == aCopy.FPlayerOwner) && 
                (FRateLevel == aCopy.FRateLevel) &&
                (FLastOwner == aCopy.FLastOwner);
    }
    public void Clone(TBzmRateDie aDest) 
    {
        aDest.FRateInTheGame = FRateInTheGame;
        aDest.FLastOwner = FLastOwner;
        aDest.FRateLevel = FRateLevel;
        aDest.FPlayerOwner = FPlayerOwner;
    }
    public void Double(PlayerColors aPlayerId) 
    {
        setPlayerOwner(PlayerColors.NONE); // ставим кубик на стол, пока его игрок не принял, им никто не владеет
      setLastOwner(aPlayerId); // запоминаем кто владел кубиком
      setRateLevel((byte)(FRateLevel << 1)); // увеличиваем стоимость
    }
    public void Beaver() 
    {

        setPlayerOwner(TBzmPlayerColorUtils.OtherPlayer(FLastOwner)); // устанавливаем нового владельца кубика
        setRateLevel((byte)(FRateLevel << 1)); // еще раз удваиваем
    }
    public void Pass() 
    {
        setRateLevel((byte)(getRateLevel() >> 1)); // возвращаем предыдущую стоимость партии
    }
    public void Take() 
    {
        if (FPlayerOwner == PlayerColors.NONE)
            FPlayerOwner = TBzmPlayerColorUtils.OtherPlayer(FLastOwner);
        FLastOwner = PlayerColors.NONE;
    }


    public void ResetBeaver()
    {
        FPlayerOwner = PlayerColors.NONE;
        FLastOwner = TBzmPlayerColorUtils.OtherPlayer(FLastOwner);
        setRateLevel((byte)(FRateLevel >> 1));
    }

}
