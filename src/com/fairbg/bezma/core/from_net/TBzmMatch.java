package com.fairbg.bezma.core.from_net;

import java.util.ArrayList;

import com.fairbg.bezma.core.MatchIdentifier;
import com.fairbg.bezma.core.MatchParameters;
import com.fairbg.bezma.core.from_net.enums.Actions;
import com.fairbg.bezma.core.from_net.enums.GameStates;
import com.fairbg.bezma.core.from_net.enums.VictoryTypes;
import com.fairbg.bezma.core.from_net.enums.MatchModes;
import com.fairbg.bezma.core.model.PlayerColors;

public class TBzmMatch {
    private TBzmDiceSetIterator FDiceSetIterator = null;// @todo TBzmDiceSetIterator.EmptyIterator;
    private ArrayList<TBzmGame> FGames = new ArrayList<TBzmGame>();
    private MatchModes FMatchMode;
    private MatchParameters FMatchParameters = new MatchParameters();
    private IBzmDiceSetManager FDiceSetManager;
    private TBzmTimer FTimer;

    private void NewGame(String aStartPosition)
    {
        TBzmGame game = new TBzmGame();
        FGames.add(game);
        game.NewGame((byte)(FGames.size()), aStartPosition, IsCrawfordGame(FGames.size()));
    }
    private void SetLastGameStartPosition(String aStartPosition) 
    {
        getGame(FGames.size() - 1).NewGame((byte)(FGames.size()), aStartPosition, IsCrawfordGame(FGames.size()));
    }
    private void ParseSgfString(String aStr)
    {
    	/** @todo
        try
        {
            TSgfMatchParser parser = new TSgfMatchParser();
            TSgfMatch sgf_match = parser.Parse(aStr);
            Clear();
            if (sgf_match == null || sgf_match.GameCount == 0 || sgf_match[0].MoveCount == 0)
                return;
            MatchParameters.ShowInInternet = sgf_match[0][0]["MI"]["inet"].Value == "1";
            if (sgf_match[0][0]["MI"]["rules"] != null)
            {
                if (sgf_match[0][0]["MI"]["rules"].Value == "longgammon")
                    MatchParameters.RulesType = RulesType.RussianBackgammon;
                else
                    MatchParameters.RulesType = RulesType.Backgammon;
            }
            TBzmConst.IndicatorsCount = Int32.Parse(sgf_match[0][0]["MI"]["pos"].Value);
            MatchParameters.MatchType = (TBzmMatchWinType)(Byte.Parse(sgf_match[0][0]["MI"]["matchtype"].Value));
            MatchParameters.WinCondition = Byte.Parse(sgf_match[0][0]["MI"]["wincond"].Value);

            MatchParameters.MatchTime = Byte.Parse(sgf_match[0][0]["MI"]["timelim"].Value);
            MatchParameters.White.Name = sgf_match[0][0]["PW"].DefaultValue;
            MatchParameters.Black.Name = sgf_match[0][0]["PB"].DefaultValue;
            MatchParameters.StartDateTime = sgf_match[0][0]["DT"].DefaultValue;
            MatchParameters.MatchGuid = new Guid(sgf_match[0][0]["MI"]["mGUID"].Value);
            MatchParameters.DiceSet.Guid = new Guid(sgf_match[0][0]["MI"]["dGUID"].Value);
            MatchParameters.DiceSet.DiceSetType = (TBzmDiceSetType)(Byte.Parse(sgf_match[0][0]["MI"]["dicesettype"].Value));

            TBzmPlayerColor LastMovePlayer = TBzmPlayerColor.None;
            TBzmPlayerColor CurPlayer = TBzmPlayerColor.None;

            string FCurPosition = "";
            byte aMoveNo = 0;
            short aSeconds = 0;
            byte aRate = 0;
            TBzmMoveBase aNewMove;                
            for (int i = 0; i < sgf_match.GameCount; i++)
            {
                string attrValue = "";
                TBzmGame aNewGame = new TBzmGame();
                aNewGame.NewGame((byte)(i + 1), sgf_match[i][0]["MI"]["startpos"].Value, IsCrawfordGame(i + 1));
                FCurPosition = aNewGame.StartPosition;
                aMoveNo = 1;
                CurPlayer = TBzmPlayerColor.None;
                aNewMove = null;
                aSeconds = 0;
                aRate = 1;
                for (int j = 0; j < sgf_match[i].MoveCount; j++)
                {
                    if (sgf_match[i][j]["B"] != null)
                    {
                        attrValue = sgf_match[i][j]["B"].DefaultValue;
                        CurPlayer = TBzmPlayerColor.Black;
                    }
                    if (sgf_match[i][j]["W"] != null)
                    {
                        attrValue = sgf_match[i][j]["W"].DefaultValue;
                        CurPlayer = TBzmPlayerColor.White;
                    }
                    if (ParseSgfMove(CurPlayer, attrValue, ref aNewMove, aMoveNo, aSeconds, ref aRate))
                    {
                        aNewMove.PositionString = TBzmBoardPositionFactory.NextPositionString(MatchParameters.RulesType, FCurPosition, aNewMove);
                        FCurPosition = aNewMove.PositionString;
                        aNewGame.AddMove(aNewMove);
                        // Если это не Double - Drop, то переключаем игрока
                        LastMovePlayer = aNewMove.PlayerId;
                        aNewMove = null;                            
                        aMoveNo++;
                    }

                }
                //aNewMove = new TBzmGameEndMove(LastMovePlayer, aMoveNo, aSeconds, aRate);
                //aNewGame.AddMove(aNewMove);
                IBzmBoardPosition pos = TBzmBoardPositionFactory.Create(MatchParameters.RulesType);
                pos.StateString = aNewGame[aNewGame.MoveCount - 1].PositionString.Substring(4);
                int checkers_left = 0;
                TBzmGameWinType win_type = TBzmGameWinType.Normal;
                
                switch (LastMovePlayer) 
                {
                    case TBzmPlayerColor.Black:
                        checkers_left = 15 - pos.OffCheckersCount(TBzmPlayerColor.White);                            
                        break;
                    case TBzmPlayerColor.White:
                        checkers_left = 15 - pos.OffCheckersCount(TBzmPlayerColor.Black);
                        break;
                }
                if (checkers_left == 15)
                    win_type = TBzmGameWinType.Gammon;
                if ((aNewGame[aNewGame.MoveCount - 1] is TBzmDoubleMove))
                {
                    win_type = TBzmGameWinType.DropDouble;
                    aRate /= 2;
                }
                else 
                {
                    // проверить счет следующей партии, найти разницу очков
                    // разделить ее на aRate и если это 
                    int ws = 0, bs = 0;
                    if(sgf_match.GameCount > i + 1)
                        ws = (Convert.ToInt32(sgf_match[i + 1][0]["MI"]["ws"].Value) - Convert.ToInt32(sgf_match[i][0]["MI"]["ws"].Value)) / aRate;
                    if (sgf_match.GameCount > i + 1)
                        bs = (Convert.ToInt32(sgf_match[i + 1][0]["MI"]["bs"].Value) - Convert.ToInt32(sgf_match[i][0]["MI"]["bs"].Value)) / aRate;
                    // 1 - Normal
                    // 2 - Gammon
                    // 3 - Backgammon
                    switch (ws + bs) 
                    {
                        case 1 :
                            win_type = TBzmGameWinType.Normal;
                            break;
                        case 2 :
                            win_type = TBzmGameWinType.Gammon;
                            break;
                        case 3:
                            win_type = TBzmGameWinType.Backgammon;
                            break;
                    }
                }    
                aNewGame.EndGame(LastMovePlayer, aRate, checkers_left, (aNewGame[aNewGame.MoveCount - 1] is TBzmDoubleMove), win_type);
                AddGame(aNewGame);
            }

            // Здесь разпарсить DiceSet
            //FDiceSetIterator = DiceSetManager.GetDiceSet(MatchParameters.DiceSet.DiceSetGuid, MatchParameters.MatchGuid);//.ClearSet();
            for (int i = 0; i < sgf_match.GameCount; i++)
            {
                FDiceSetIterator.AddSgfDiceSet(sgf_match[i][0]["MI"]["dice"].Value);
            }
        }
        catch (Exception)
        {
        }*/
    }
    private void ParseDiceSetFromSgfString(String aStr)
    {
    	/** @todo
        try
        {
            TSgfMatchParser parser = new TSgfMatchParser();
            TSgfMatch sgf_match = parser.Parse(aStr);
            // Здесь разпарсить DiceSet

            //FDiceSetIterator = DiceSetManager.GetDiceSet(MatchParameters.DiceSet.DiceSetGuid, MatchParameters.MatchGuid); //FDiceSet.ClearSet();
            for (int i = 0; i < sgf_match.GameCount; i++)
            {
                if (sgf_match[i][0]["MI"] != null && sgf_match[i][0]["MI"]["DICE"] != null)
                    FDiceSetIterator.AddSgfDiceSet(sgf_match[i][0]["MI"]["DICE"].Value);
                else
                {
                    //TBzmDiceSet diceSet = FDiceSet.AddSfgDice("");
                    for (int j = 1; j < sgf_match[i].MoveCount; j++)
                    {
                        if (sgf_match[i][j]["B"] != null)
                        {
                            char a1 = sgf_match[i][j]["B"].DefaultValue.Substring(0, 1)[0];
                            if (a1 >= '0' && a1 <= '9')
                                FDiceSetIterator.AddDiceToGame(i, new TBzmDice(Byte.Parse(sgf_match[i][j]["B"].DefaultValue.Substring(0, 1)),
                                            Byte.Parse(sgf_match[i][j]["B"].DefaultValue.Substring(1, 1))));

                        }
                        if (sgf_match[i][j]["W"] != null)
                        {
                            char a1 = sgf_match[i][j]["W"].DefaultValue.Substring(0, 1)[0];
                            if (a1 >= '0' && a1 <= '9')
                                FDiceSetIterator.AddDiceToGame(i, new TBzmDice(Byte.Parse(sgf_match[i][j]["W"].DefaultValue.Substring(0, 1)),
                                            Byte.Parse(sgf_match[i][j]["W"].DefaultValue.Substring(1, 1))));
                        }
                    }
                    FDiceSetIterator.NextGame();
                }

            }
        }
        catch (Exception)
        {
        }
        */
    }
    private int PlayerPoints(PlayerColors aPlayer, int aGameNo) 
    {
        int res = 0;
        TBzmGameResult[] aProtocols = MatchProtokol();
        for (int i = 0; i < aGameNo && i < aProtocols.length; i++) 
        {
            if (aPlayer == aProtocols[i].getWinner()) 
            {
                res += aProtocols[i].getWinnerPoints();
            }
        }
        return res;
    }

    private void AddGame(TBzmGame aNewGame)
    {
        FGames.add(aNewGame);
        if (aNewGame.getGameResult() != null)
        {/** @todo
            switch (aNewGame.getGameResult().getWinner())
            {
                case WHITE:
                    MatchParameters.White.GameWins++;
                    MatchParameters.White.Points += aNewGame.GameResult.WinnerPoints;
                    break;
                case BLACK:
                    MatchParameters.Black.GameWins++;
                    MatchParameters.Black.Points += aNewGame.GameResult.WinnerPoints;
                    break;
            }
            */
        }
    }

    //EVENTS
    /** @todo
    public event TBzmEvent OnGameBegin;
    public event TBzmEvent OnGameMoveAdd;
    public event TBzmEvent OnGameMoveCancel;
    public event TBzmEvent OnGameEnd;
    public event TBzmEvent OnMatchBegin;
    public event TBzmEvent OnMatchEnd;
	*/

    //PROPERTIES
    public MatchParameters getMatchParameters() 
    {
        return FMatchParameters; 
    }
    
    public boolean isMatchOver() 
    {
    	/** @todo
            switch (getMatchParameters().MatchType) 
            {
                case TBzmMatchWinType.Scores:
                    return Math.Max(FMatchParameters.Black.Points, FMatchParameters.WHITE.Points) >= MatchParameters.WinCondition;
                case TBzmMatchWinType.FixedGames:
                    return FMatchParameters.Black.GameWins + FMatchParameters.WHITE.GameWins >= FMatchParameters.WinCondition;
            }
            */
            return false;
    }
    public PlayerColors getMatchWinner() 
    {
    	/**@todo
            if (isMatchOver())                    
                switch (FMatchParameters.MatchType) 
                {
                    case TBzmMatchWinType.Scores:
                        if (FMatchParameters.WHITE.Points >= FMatchParameters.WinCondition)
                            return TBzmPlayerColor.WHITE;
                        if (FMatchParameters.Black.Points >= FMatchParameters.WinCondition)
                            return TBzmPlayerColor.Black;
                        break;
                    case TBzmMatchWinType.FixedGames:
                        break;
                }
                */
            return PlayerColors.NONE;
    }
    public int getGameCount()
    {
        return FGames.size();
    }
    public TBzmGame getCurrentGame() 
    {
            return (getGameCount() == 0) ? null : getGame(getGameCount() - 1);
    }
    public TBzmGame getGame(int index)
    {
            return (index >= 0 && index < FGames.size()) ? FGames.get(index) : null;
    }
    public String getSgfFilename() 
    {
    	return "";
            /// @todoreturn FMatchParameters.DefaultSgfFileName;
    }
    public MatchModes getMatchMode() 
    {
        return FMatchMode; 
    }
    public IBzmDiceSetManager getDiceSetManager()
    {
        return FDiceSetManager; 
    }
    public void setDiceSetManager(IBzmDiceSetManager value)
    {
        FDiceSetManager = value;
        /**@todo
        if (FDiceSetManager == null)
            FDiceSetIterator = null; @todo TBzmDiceSetIterator.EmptyIterator;
        else
            FDiceSetIterator = FDiceSetManager.GetDiceSetIterator(MatchParameters.DiceSet.Guid, MatchParameters.MatchGuid, MatchParameters.MatchType == TBzmMatchWinType.FixedGames, FDiceSetIterator);
            */
    }
    public TBzmTimer getTimer() 
    {
        return FTimer;
       
    }
    public void setTimer(TBzmTimer timer) 
    { 
    	FTimer = timer; 
    }
    public boolean HasMainWindow;

    //PUBLIC METHODS
    public void Clear() 
    {
        FGames.clear();
        /** @todo
        FMatchParameters.White.ClearResuts();
        FMatchParameters.Black.ClearResuts();
        */
    }
    public void StartMatch() 
    {
        Clear();
        /** @todo
        if (OnMatchBegin != null)
            OnMatchBegin();
            */
    }
    public String GetSgfString() 
    {
    	return null;
    	/** @todo
        string sgf_string = "";
        string staticHeader = String.Format(";FF[4]GM[6]CA[UTF-8]AP[BEZMA]PW[{0}]PB[{1}]DT[{2}]", 
                                    MatchParameters.White.Name, 
                                    MatchParameters.Black.Name, 
                                    MatchParameters.StartDateTime);
        string miHeader = "MI[length:{0}][game:{1}][ws:{2}][bs:{3}][wtime:{4}][btime:{5}]";//[wtimeouts:{6}][btimeouts:{7}]"
        string bezmaHeader = String.Format("[inet:{0}][pos:{1}][dicesettype:{2}][matchtype:{3}][wincond:{4}][timelim:{5}][mGUID:{6}][dGUID:{7}]",
                                    (MatchParameters.ShowInInternet?"1":"0"),
                                    11,
                                    ((byte)(MatchParameters.DiceSet.DiceSetType)).ToString(),
                                    (int)MatchParameters.MatchType,
                                    MatchParameters.WinCondition,
                                    MatchParameters.MatchTime,
                                    MatchParameters.MatchGuid,
                                    MatchParameters.DiceSet.Guid);
        string header = "";
        TBzmGame game;
        int len = MatchParameters.WinCondition;
        int ws = 0, bs = 0;
        for (int i = 0; i < GameCount; i++) 
        {
            game = this[i];
            header = staticHeader + 
                     String.Format(miHeader, len, i , ws, bs, 0, 0) + 
                     bezmaHeader +
                     String.Format("[startpos:{0}]", game.StartPosition);
            sgf_string += game.ToSgfString(header);
            if (game.GameResult != null) 
            {
                switch (game.GameResult.Winner)
                {
                    case TBzmPlayerColor.Black:
                        bs += game.GameResult.WinnerPoints;
                        break;
                    case TBzmPlayerColor.White:
                        ws += game.GameResult.WinnerPoints;
                        break;
                }
            }
        }
        return sgf_string;
        */
    }
    public String GetJellyFishString()
    {
    	return null;
    	/** todo
        int max_points = 0;
        if (MatchParameters.MatchType == TBzmMatchWinType.Scores) 
            max_points = MatchParameters.WinCondition;

        Bezma.FileFormats.JellyFish.MatchWriter match_writer = new MatchWriter(max_points,MatchParameters.White.Name, MatchParameters.Black.Name);

        TBzmGame game;
        for (int i = 0; i < GameCount; i++)
        {
            game = this[i];
            match_writer.NewGame(game.GameNo);
            for(int j = 0; j< game.MoveCount;j++)
            {
                TBzmMoveBase move = game[j];
                // Добавить все хода
                switch (move.ActionType)
                {
                    case TBzmActionType.atMove:
                        match_writer.AddMove(move.PlayerId == TBzmPlayerColor.White, move.ToJFString());
                        break;
                    case TBzmActionType.atSkipMove:
                        match_writer.AddMove(move.PlayerId == TBzmPlayerColor.White, move.ToJFString());
                        break;
                    case TBzmActionType.atDouble:
                        TBzmDoubleMove double_move = move as TBzmDoubleMove;
                        match_writer.AddDouble(double_move.PlayerId == TBzmPlayerColor.White, double_move.RateLevel, double_move.Take);
                        break;
                }
            }
            if (game.GameResult != null)
            {
                switch (game.GameResult.Winner)
                {
                    case TBzmPlayerColor.White:
                        match_writer.FinishGame(true, game.GameResult.WinnerPoints);
                        break;
                    case TBzmPlayerColor.Black:
                        match_writer.FinishGame(false, game.GameResult.WinnerPoints);
                        break;
                }
            }
        }
        return match_writer.ToString();
        */
    }

    /// <summary>
    /// Функция работает только для последней в списке игры
    /// </summary>
    /// <param name="aGameNo"></param>
    /// <returns></returns>
    public boolean IsCrawfordGame(int aGameNo) 
    {
    	/** @todo
        if (FMatchParameters.Crawford) // Crawford rule
        {
            if (getGameCount() != 0 && aGameNo <= getGameCount() && aGameNo > 1)
            {
                switch (getGame(aGameNo - 2).GameResult.Winner)
                {                           
                    case TBzmPlayerColor.BLACK:
                        return PlayerPoints(TBzmPlayerColor.BLACK, aGameNo - 1) == MatchParameters.WinCondition - 1;
                    case TBzmPlayerColor.WHITE:
                        return PlayerPoints(TBzmPlayerColor.WHITE, aGameNo - 1) == MatchParameters.WinCondition - 1;
                }
            }
        }
        */
        return false;
    }

    /*public boolean AddGameString() 
    {
        throw new Exception("Is not SGF string");
    }*/
    public boolean LoadMatchFromFile(String aFileName) 
    {
    	return false;
    	/** @todo
        if (File.Exists(aFileName))
        {
            StreamReader reader = new StreamReader(aFileName);
            string aStr = reader.ReadToEnd();
            ParseSgfString(aStr);
            return true;
        }
        else 
        {
            return false;
        }
        */
    }
    
    public boolean LoadDiceSetFromFile(String aFileName)
    {
    	return false;
    	/** @todo
        if (File.Exists(aFileName))
        {
            StreamReader reader = new StreamReader(aFileName);
            string aStr = reader.ReadToEnd();
            ParseDiceSetFromSgfString(aStr);
            return true;
        }
        else
        {
            return false;
        }
        */
    }
    /// <summary>
    ///  Функция ParseSfgMove должна разобрать параметр переданный в строке AStr и сформировать ход
    /// если ход сформирован полностью функция возвращает True, если нужны дополнительные сведения для формирования хода, то возвращается False
    /// дополнительные сведения можно передать следующим вызовом функции с тем же самым значением AMove
    /// </summary>
    /// <param name="aPlayer">Текущий игрок</param>
    /// <param name="aStr">Наименование атрибута</param>
    /// <param name="aMove">Текущий ход</param>
    /// <param name="aMoveNo">Номер хода</param>
    /// <param name="aSeconds">Количество прошедших секунд</param>
    /// <param name="aRate">Значение кубика стоимости</param>
    /// <returns></returns>
    private boolean ParseSgfMove(PlayerColors aPlayer, String aStr, TBzmMoveBase aMove, byte aMoveNo, short aSeconds, byte aRate)
    {
    	return false; /** @todo
        boolean res = false;
        switch (aStr.ToLower()) 
        {
            case "double" :
                aRate *= 2;
                aMove = new TBzmDoubleMove(aPlayer, aMoveNo, aSeconds, aRate, false, false);
                aStr = "";
                res = false;
                break;
            case "beaver" :
                if (aMove != null) 
                {
                    (aMove as TBzmDoubleMove).Beaver = true;
                    aRate *= 2;
                    aStr = "";
                    res = false;
                }
                break;
            case "take":
                if (aMove != null) 
                {
                    (aMove as TBzmDoubleMove).Take = true;
                    aStr = "";
                    res = true;
                }
                break;
            case "drop":
                if (aMove != null)
                {
                    (aMove as TBzmDoubleMove).Take = false;
                    aStr = "";
                    res = true;
                }
                break;
        }
        if (aStr != "") 
        {
            if (aStr.Length == 2)
            {
                aMove = new TBzmSkipMove(aPlayer, aMoveNo, aSeconds, null);
                (aMove as TBzmSkipMove).ParseSgfString(aStr);
                res = true;
            }
            else 
            {
                aMove = new TBzmMoveMove(aPlayer, aMoveNo, aSeconds, null);
                (aMove as TBzmMoveMove).ParseSgfString(aStr);
                res = true;
            }
        }
        return res;*/
    }
    public void GameStart(String aStartPosition) 
    {
        NewGame(aStartPosition);
        /**@todo
         * if (OnGameBegin != null)
         * OnGameBegin();
         */
            
    }
    public boolean GameAddMove(Actions aMoveType, PlayerColors aPlayerId,
        byte aMoveNo, short aSeconds, String aPositionString, Object ... aParams) 
    {
    	return false;
    	/** @todo
        TBzmMoveBase aNewMove = TBzmGameMoveFactory.CreateMove(aMoveType, aPlayerId, aMoveNo, aSeconds, aParams);
        if(aNewMove != null)            
        {
            aNewMove.PositionString = aPositionString;
            CurrentGame.AddMove(aNewMove);
            if (OnGameMoveAdd != null)
                OnGameMoveAdd();
            return true;
        }
        else            
        {
            return false;
        }        
        */    
    }
    public boolean GameCancelMove() 
    {
        boolean res = false;
        if (getCurrentGame() != null)
            res = getCurrentGame().CancelMove();
        
        return false;
        /**@todo
        if (OnGameMoveCancel != null)
            OnGameMoveCancel();
        return res;
        */
    }
    public GameStates GameEnd(PlayerColors aPlayerId, byte aRateLevel, int aCheckersLeft, boolean IsRatePass, VictoryTypes WinType) 
    {
    	return GameStates.None;
    	/**@todo
        TBzmMoveBase aMove = TBzmGameMoveFactory.CreateMove(TBzmActionType.atGameEnd, aPlayerId, (byte)(CurrentGame.MoveCount + 1), 0, new object[] { aRateLevel });
        if (aMove == null)
            throw new Exception("Can not create last move");
        CurrentGame.AddMove(aMove);
        byte rate_level = aRateLevel;
        switch (WinType) 
        {
            case TBzmGameWinType.Backgammon:
                rate_level = (byte)(rate_level * 3);
                break;
            case TBzmGameWinType.Gammon:
                rate_level = (byte)(rate_level * 2);
                break;
        }
        switch (aPlayerId) 
        {
            case TBzmPlayerColor.Black:
                FMatchParameters.Black.GameWins++;
                FMatchParameters.Black.Points += rate_level;
                break;
            case TBzmPlayerColor.White:
                FMatchParameters.White.GameWins++;
                FMatchParameters.White.Points += rate_level;
                break;
        }
        CurrentGame.EndGame(aPlayerId, aRateLevel, aCheckersLeft, IsRatePass, WinType);
        if (OnGameEnd != null)
            OnGameEnd();
        if (IsMatchOver)
        {
            if (OnMatchEnd != null)
                OnMatchEnd();
            //FDiceSet.DeleteDiceFile();
            return TBzmGameState.LookMode;
        }
        else 
        {
            return TBzmGameState.NextGame;
        }
	*/
    }
    public short getNextDiceFromSet()
    {
    	return 0x33;
    	/** @todo
        TBzmDice dice = FDiceSetIterator.GetDiceAndMoveNext();
        if (dice != null)
            return dice.ToInt16();
        else
            return 0;
            */
    }
    public short getCurrentDiceFromSet()
    {
    	return 0x44;
    	/** @todo
        TBzmDice dice = FDiceSetIterator.GetDice();
        if (dice != null)
            return dice.ToInt16();
        else
            return 0;
            */
    }
    public int NextDiceSet() 
    {
    	return 0x55;
    	/** @todo
        if (FMatchParameters.DiceSet.DiceSetType == TBzmDiceSetType.Manual)
            return 0;
        else
            return FDiceSetIterator.NextGame();
            */
    }
    public boolean RestoreDiceSet(MatchIdentifier aMatchGuid) 
    {
        //FDiceSetIterator = DiceSetManager.GetDiceSet(MatchParameters.DiceSet.DiceSetGuid, MatchParameters.MatchGuid);
        return true;
    }
    
    public void SaveSgfMatch(String filename) 
    {
    	/** @todo
        int pos = filename.LastIndexOf('/');
        if (pos == -1) 
        {
            pos = filename.LastIndexOf('\\');
        }
        if (pos != -1) 
        {
            string parentDirectory = filename.Substring(0, pos + 1);
            if (!Directory.Exists(parentDirectory))
                Directory.CreateDirectory(parentDirectory);
        }
        StreamWriter writer = new StreamWriter(File.Open(filename, FileMode.Create));
        try
        {
            writer.Write(GetSgfString());
        }
        finally 
        {
            writer.Close();
        }
        */
    }
    public void SaveJellyFish(String filename) 
    {
    	/**@todo
        int pos = filename.LastIndexOf('/');
        if (pos == -1)
        {
            pos = filename.LastIndexOf('\\');
        }
        if (pos != -1)
        {
            string parentDirectory = filename.Substring(0, pos + 1);
            if (!Directory.Exists(parentDirectory))
                Directory.CreateDirectory(parentDirectory);
        }
        StreamWriter writer = new StreamWriter(File.Open(filename, FileMode.Create));
        try
        {
            writer.Write(GetJellyFishString());
        }
        finally
        {
            writer.Close();
        }
		*/
    }

    public TBzmGameResult[] MatchProtokol()
    {
        ArrayList<TBzmGameResult> list = new ArrayList<TBzmGameResult>();
        for (int i = 0; i < getGameCount(); i++) 
        {
            if (getGame(i) != null && getGame(i).getGameResult() != null)
                list.add((TBzmGameResult) getGame(i).getGameResult().Clone());
        }
        return list.toArray(new TBzmGameResult[0]);
    }


    public void SetPositionString(String aPositionString)
    {
        SetLastGameStartPosition(aPositionString);
    }

}
