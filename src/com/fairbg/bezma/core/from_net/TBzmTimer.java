package com.fairbg.bezma.core.from_net;

import java.util.Timer;

import com.fairbg.bezma.core.model.PlayerColors;

public class TBzmTimer {
    private Timer FTimer = null;
    private PlayerColors FActivePlayer = PlayerColors.NONE;
    private int FMatchSeconds = 0;
    private int FMoveSeconds = 0;
    private int FWhiteSecondsLeft = 0;
    private int FBlackSecondsLeft = 0;
    private int FWhiteMoveSeconds = 0;
    private int FBlackMoveSeconds = 0;

    private void DoOnTimer(Object stateInfo)
    {
    	/** @todo
        if (FMatchSeconds == 0)
            return;
        switch(FActivePlayer)
        {
            case NONE:
                break;
            case BLACK:
                if (FBlackMoveSeconds <= 0) FBlackSecondsLeft--;
                else
                {
                    FBlackMoveSeconds--;
                    if (FBlackMoveSeconds <= 0 && OnMatchTimerStarted != null)
                        OnMatchTimerStarted(TBzmPlayerColor.Black);
                }
                if (OnTimer != null)
                    OnTimer(TBzmPlayerColor.Black);
                if (FBlackSecondsLeft <= 0 && OnTimeIsUp != null)
                    OnTimeIsUp(TBzmPlayerColor.Black);
                break;
            case TBzmPlayerColor.White:
                if (FWhiteMoveSeconds <= 0) FWhiteSecondsLeft--;
                else
                {
                    FWhiteMoveSeconds--;
                    if (FWhiteMoveSeconds <= 0 && OnMatchTimerStarted != null)
                        OnMatchTimerStarted(TBzmPlayerColor.White);
                }
                if (OnTimer != null)
                    OnTimer(TBzmPlayerColor.White);
                if (FWhiteSecondsLeft <= 0 && OnTimeIsUp != null)
                    OnTimeIsUp(TBzmPlayerColor.White);
                break;                
        }
        if (FWhiteSecondsLeft * FBlackSecondsLeft == 0) 
        {
            Stop();
        }
        */
    }

    public TBzmTimer()
    {
    	/** @todo
        TimerCallback callback = new TimerCallback(DoOnTimer);
        FTimer = new System.Threading.Timer(callback, null , Timeout.Infinite, 1000);
        */
    }
    public int getMatchTime()
    {
    	return FMatchSeconds / 60; 
    }
        public void setMatchTime(int matchTime) {
            FMatchSeconds = matchTime * 60;
            FWhiteSecondsLeft = FMatchSeconds;
            FBlackSecondsLeft = FMatchSeconds;
            FActivePlayer = PlayerColors.NONE;
    }
    public int getMoveTime() 
    {
    	return FMoveSeconds; 
    }
    
    public void setMoveTime(int moveTime) {
            FMoveSeconds = moveTime;
            FWhiteMoveSeconds = FMoveSeconds;
            FBlackMoveSeconds = FMoveSeconds;
    }
    private int SecondsLeft(PlayerColors aPlayer)
    {
        switch(aPlayer)
        {
            case BLACK:
                return FBlackSecondsLeft;
            case WHITE:
                return FWhiteSecondsLeft;
            default: return FMatchSeconds;
        }
    }
    private int MoveSecondsLeft(PlayerColors aPlayer) 
    {
        switch (aPlayer)
        {
            case BLACK:
                return FBlackMoveSeconds;
            case WHITE:
                return FWhiteMoveSeconds;
            default: 
            	return FMoveSeconds;
        }
    }
    public String TimeLeft(PlayerColors aPlayer)
    {
    	/** @todo
        int seconds = SecondsLeft(aPlayer);
        int hh, mm, ss;
        hh = seconds / 3600;
        mm = (seconds % 3600) / 60;
        ss = seconds % 60;
        string res = "";
        if (hh != 0)
            res = String.Format("{0}:", hh);
        res = String.Format("{0}{1:D2}:{2:D2}", res, mm, ss);
        return res;
        */ return "";
    }
    public String MoveTimeLeft(PlayerColors aPlayer) 
    {
    	/** @todo
        return String.Format("{0:D2}", MoveSecondsLeft(aPlayer));
        */ return "";
    }
    
    public void Continue() 
    {
    	/** @todo
        FTimer.Change(1000, 1000);
        */ 
    }
    public void Stop() 
    {
    	/** @todo
        FTimer.Change(Timeout.Infinite, Timeout.Infinite);
        FActivePlayer = TBzmPlayerColor.None;
        */
    }
    public void Pause() 
    {
    	/** @todo
        FTimer.Change(Timeout.Infinite, Timeout.Infinite);
        */ 
    }
    public void Switch() 
    {
        switch(FActivePlayer)
        {
            case BLACK:
                Switch(PlayerColors.WHITE);
                break;
            case WHITE:
                Switch(PlayerColors.BLACK);
                break;
        }
    }
    public void Switch(PlayerColors anActivePlayer) 
    {
        if (FActivePlayer != anActivePlayer)
        {
            FActivePlayer = anActivePlayer;
            FWhiteMoveSeconds = FMoveSeconds;
            FBlackMoveSeconds = FMoveSeconds;
        }            
        Continue();
    }

    /** @todo
    public event TBzmTimerEvent OnTimer;
    public event TBzmTimerEvent OnTimeIsUp;
    public event TBzmTimerEvent OnMatchTimerStarted;
     */
}
