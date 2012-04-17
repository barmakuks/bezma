package com.fairbg.bezma.core.from_net;

import com.fairbg.bezma.core.MatchIdentifier;
import com.fairbg.bezma.core.from_net.enums.RandomType;
import com.fairbg.bezma.core.from_net.enums.Rules;
import com.fairbg.bezma.core.from_net.enums.RollTypes;
import com.fairbg.bezma.core.from_net.enums.MatchWinConditions;
import com.fairbg.bezma.core.from_net.enums.ShowPipsInfo;

public class TBzmMatchParameters {
    public MatchIdentifier MatchGuid = new MatchIdentifier();
    public ShowPipsInfo PipsInfo = ShowPipsInfo.Your;
    public boolean ConfirmCube = true;
    public MatchWinConditions MatchType = MatchWinConditions.Scores;
    public int MatchId = 0;
    public int BoardId = 0;
    public byte WinCondition;
    public int TeamMatchId = 0;
    
    public TBzmPlayerParameters White = new TBzmPlayerParameters();
    public TBzmPlayerParameters Black = new TBzmPlayerParameters();
    public TBzmDiceSetParameters DiceSet = new TBzmDiceSetParameters();
    public String StartDateTime = "";
    private boolean FUseCrawfordRule = true;
    private boolean FCalculateDice = true;
    private boolean FCalculateStrongDice = true;
    
    public boolean isUseCrawfordRule()
    {
            if (MatchType == MatchWinConditions.Scores)
                return FUseCrawfordRule;
            else
                return false;
    }
    public void setUseCrawfordRule(boolean value) 
    { 
    	FUseCrawfordRule = value; 
    }
    
    public boolean ShowInInternet = false;
    public boolean IsEmpty = true;
    public boolean isCalculateDice() 
    {
        return FCalculateDice && DiceSet.DiceSetType == RollTypes.Manual; 
    }
    public void setCalculateDice(boolean value)
    { 
    	FCalculateDice = value; 
    }
    public boolean isCalculateStrongDice() 
    {
        return FCalculateStrongDice && isCalculateDice(); 
        }
    public void setCalculateStrongDice(boolean value) 
    { 
    	FCalculateStrongDice = value; 
    }
    public short MatchTime = 0;
    public short MoveTime = 0;
    public RandomType getRandomType()
    {
        return FRandomType;
    }
    public void setRandomType(RandomType randomType){
            FRandomType = randomType;
            FRandomiser = TBzmRandomizerFactory.GetRandomizer(FRandomType);
        }
    public RandomType FRandomType = RandomType.ANSI;
    public IBzmRandom FRandomiser = null; 
    public IBzmRandom getRandomiser() 
    {
            if (FRandomiser == null)
            {
                FRandomiser = TBzmRandomizerFactory.GetRandomizer(FRandomType);
            }
            return FRandomiser;
    }
    public String Event = "";
    public String MatchLevel = "";
    public String Sponsor = "";
    public String Organizer = "";


    public boolean IdenticalOpenRoll = true;
    public boolean MatchOver = false;
    @Override
    public String toString() {
        String res = "";
        /** @todo
        if (IsEmpty)
            res = "New match";
        else
        {
            switch (MatchType)
            {
                case Scores:
                    res = res + String.Format("{0} scores to win", WinCondition);
                    break;
                case FixedGames:
                    res = res + String.Format("best of {0} games", WinCondition);
                    break;
            }
            res = res + String.Format(res + "\nWHITE : {0}\n wins {1} games, {2} scores\nBLACK : {3}\n wins {4} games, {5} scores",
                White.Name, White.GameWins, White.Points, Black.Name, Black.GameWins, Black.Points);
                
        }*/
        return res;
    }
    public String getDefaultSgfFileName() 
    {
    	
            String res = "";
            /** @todo
            if (StartDateTime.Length > 10)
            {
                res = StartDateTime.Substring(6, 4) + "-" +
                    StartDateTime.Substring(3, 2) + "-" +
                    StartDateTime.Substring(0, 2) + "-" +
                    StartDateTime.Substring(11, 2) +
                    StartDateTime.Substring(14, 2) + "-" +
                    Black.Name + "-" + White.Name + "-" +
                    WinCondition.ToString();
            }
            else
            { 
                res = Black.Name + "-" + White.Name;
            }
            */
            return res;
    }
    public void New() 
    {
        MatchGuid = MatchIdentifier.generateNew();            
        MatchType = MatchWinConditions.Scores;
        MatchId = 0;
        BoardId = 0;
        WinCondition = 1;
        White.Clear();
        White.Name = "White";
        Black.Clear();
        Black.Name = "Black";
        /** @todo StartDateTime = DateTime.Now.ToString("dd.MM.yyyy HH:mm"); */
        ShowInInternet = false;
        IsEmpty = true;
        MatchTime = 0;
        DiceSet.DiceSetType = RollTypes.Generate;
        DiceSet.Guid = MatchGuid;
        DiceSet.Filename = "";
        Event = "";
        MatchLevel = "";
        Sponsor = "";
        Organizer = "";

    }
    public Rules RulesType;
    public void Copy(TBzmMatchParameters aCopy) 
    {
        PipsInfo = aCopy.PipsInfo;
        TeamMatchId = aCopy.TeamMatchId;
        FUseCrawfordRule = aCopy.FUseCrawfordRule;
        FCalculateDice = aCopy.FCalculateDice;
        FCalculateStrongDice = aCopy.FCalculateStrongDice;

        MatchGuid = aCopy.MatchGuid;//MatchGuid
        MatchType = aCopy.MatchType;//MatchType
        MatchId = aCopy.MatchId;//MatchId
        BoardId = aCopy.BoardId;//BoardId
        WinCondition = aCopy.WinCondition;//WinCondition
        White.Copy(aCopy.White);//
        Black.Copy(aCopy.Black);//
        DiceSet.Copy(aCopy.DiceSet);//
        StartDateTime = aCopy.StartDateTime;//StartDateTime
        ShowInInternet = aCopy.ShowInInternet;//ShowInInternet
        IsEmpty = aCopy.IsEmpty;//IsEmpty
        MatchTime = aCopy.MatchTime;//MatchTime
        MoveTime = aCopy.MoveTime;//MoveTime
        IdenticalOpenRoll = aCopy.IdenticalOpenRoll;//IdenticalOpenRoll
        MatchOver = aCopy.MatchOver;//MatchOver
        RulesType = aCopy.RulesType;//RulesType
        Event = aCopy.Event;
        MatchLevel = aCopy.MatchLevel;
        Sponsor = aCopy.Sponsor;
        Organizer = aCopy.Organizer;

    }

}
