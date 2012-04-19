package com.fairbg.bezma.core.from_net;

import com.fairbg.bezma.core.from_net.enums.Actions;
import com.fairbg.bezma.core.from_net.enums.Buttons;
import com.fairbg.bezma.core.from_net.enums.BezmaConstants;
import com.fairbg.bezma.core.from_net.enums.RollTypes;
import com.fairbg.bezma.core.from_net.enums.GameStates;
import com.fairbg.bezma.core.from_net.enums.VictoryTypes;
import com.fairbg.bezma.core.from_net.enums.MatchWinConditions;
import com.fairbg.bezma.core.from_net.enums.Messages;
import com.fairbg.bezma.core.from_net.enums.PlayerColors;
import com.fairbg.bezma.core.from_net.enums.PlayerStates;

public class BoardAutomat
{
    private TBzmMatch FMatch;
    //private TBzmGameState PrevGameState;

    private TBzmBoard FBoard;

    private void changeCurrentPlayer() 
    {
        FBoard.ChangeCurrentPlayer();
        FMatch.getTimer().Switch(FBoard.getActivePlayerId());
    }
    //private Timer FTimer;
    private void OnTimer(Object state)
    {
        if (FMatch.getMatchParameters().MatchLimit == 0)
            return;
        /** @todo
        if (FBoard.getPlayerBlack().OnTimer())
            ProcessMessage(TBzmMessages.TimeIsUp, TBzmPlayerColor.Black, 0, 0);
        if (FBoard.PlayerWhite.OnTimer())
            ProcessMessage(TBzmMessages.TimeIsUp, TBzmPlayerColor.White, 0, 0);
            */
    }
    public void TimeIsUp(PlayerColors winner) 
    {
        FBoard.setGameState(FMatch.GameEnd(winner, 
        		FBoard.getRateDie().getRateLevel(), 
        		FBoard.CheckersOnBoard(FBoard.getActivePlayerId()),
        		false, 
        		VictoryTypes.Normal));
    }
    /// <summary>
    /// Реализация конечного автомата
    /// </summary>
    /// <param name="msg">Тип полученного сообщения</param>
    /// <param name="aPlayerId">Игрок, пославший сообщение</param>
    /// <param name="aBtn">Какой кнопкой послано сообщение</param>
    /// <param name="aParam">Дополнительный внешний параметр</param>
    /// <returns>Истина, если автомат корректно обработал сообщение</returns>
    private boolean ProcessMessage(Messages msg, PlayerColors aPlayerId, Buttons aBtn, int aParam)

    {            
        byte aDie1 = 0;
        byte aDie2 = 0;
        int aDice = 0;
        /// @todo /// @todo FBoard.CurrentSound = TBzmSound.None;
        switch(msg)
        {
            case Info:
                if (aPlayerId == FBoard.getActivePlayerId()) 
                {
                	/** @todo
                    switch(FMatch.getMatchParameters().PipsInfo)
                    {
                        case TBzmPipsInfo.None:
                            //FBoard.Player(TBzmConst.ACTIVE).DisplayText = String.Format("Your PIPs {0}   Opp  PIPs {1}", FBoard.Pips(FBoard.ActivePlayerId), FBoard.Pips(FBoard.PassivePlayerId));
                            break;
                        case TBzmPipsInfo.Your:
                            FBoard.Player(TBzmConst.ACTIVE).DisplayText = String.Format("Your PIPs {0}", FBoard.Pips(FBoard.ActivePlayerId));
                            break;
                        case TBzmPipsInfo.Both:
                            FBoard.Player(TBzmConst.ACTIVE).DisplayText = String.Format("Your PIPs {0}   Opp  PIPs {1}", FBoard.Pips(FBoard.ActivePlayerId), FBoard.Pips(FBoard.PassivePlayerId));
                            break;
                    }
                    */
                }
                return true;
            case TimeIsUp:
                FBoard.setGameState(FMatch.GameEnd(FBoard.getPassivePlayerId(),
                		FBoard.getRateDie().getRateLevel(),
                		FBoard.CheckersOnBoard(FBoard.getActivePlayerId()),
                		false, 
                		VictoryTypes.Normal));
                return true;
            case Start:
                switch(FBoard.getGameState())
                {
                    case LookMode:
                    case None:
                    case NextGame:
                    case InitBoard:
                        // Обработаем дальше
                        break;
                    default:
                        FBoard.setPaused(!FBoard.isPaused());
                        return true;
                }
                break;
        }
        switch (FBoard.getGameState())
        {
            case None:
            case NextGame:
                switch (msg) 
                {
                    case Ok: //TBzmMessages.msgStart:
                        if (FBoard.CheckBoardPositionConfirmity())
                        {
                            FBoard.getRateDie().InitRateDie();
                            FBoard.setGameState(GameStates.PlayerSelecting);
                            FMatch.GameStart(FBoard.getPositionString());
                            /// @todo /// @todo FBoard.CurrentSound = TBzmSound.GameStart;
                            FMatch.NextDiceSet();
                        }
                        else 
                        {
                            FBoard.getPlayerBlack().setDisplayText(BezmaConstants.cstrWrongPosition);
                            FBoard.getPlayerWhite().setDisplayText(BezmaConstants.cstrWrongPosition);
                            /// @todo FBoard.CurrentSound = TBzmSound.Error;
                        }
                        break;
                    case Position:
                        FBoard.setActivePlayerId(aPlayerId);
                        FBoard.setGameState(GameStates.InitBoard);
                        /// @todo FBoard.CurrentSound = TBzmSound.InitBoard;
                        FMatch.GameStart("");
                        break;
                }
                break;
            case InitBoard:
                switch (msg) 
                {
                    case Ok: //TBzmMessages.msgStart:
                        int black_checkers = FBoard.CheckersOnBoard(PlayerColors.BLACK);
                        int white_checkers = FBoard.CheckersOnBoard(PlayerColors.WHITE);
                        if (black_checkers > 15 || black_checkers <= 0 || white_checkers > 15 || white_checkers <= 0)
                        {
                            /// @todo FBoard.CurrentSound = TBzmSound.Error;
                        }
                        else 
                        {
                            FBoard.getRateDie().InitRateDie();
                            FBoard.setGameState(GameStates.PlayerSelecting);
                            //FMatch.GameStart(FBoard.PositionString);
                            FMatch.SetPositionString(FBoard.getPositionString());
                            /// @todo FBoard.CurrentSound = TBzmSound.GameStart;
                            FMatch.NextDiceSet();
                        }

                        break;
                    case Position: //TBzmMessages.msgOk:
                        FBoard.ReadBoardPosition();
                        changeCurrentPlayer();                            
                        FBoard.getPlayerBlack().setPips(FBoard.Pips(FBoard.getPlayerBlack().getPlayerId()));
                        FBoard.getPlayerWhite().setPips(FBoard.Pips(FBoard.getPlayerWhite().getPlayerId()));

                        FBoard.Player(BezmaConstants.ACTIVE).setPlayerState(PlayerStates.InitBoard);
                        FBoard.Player(BezmaConstants.PASSIVE).setPlayerState(PlayerStates.Inactive);
                        /// @todo FBoard.CurrentSound = TBzmSound.InitBoard;
                        break;
                }
                break;
            case PlayerSelecting:
                switch (msg) 
                {
                    case Dice:
                        FBoard.setActivePlayerId(aPlayerId);
                        /// @todo FBoard.Player(TBzmConst.ACTIVE).OnPressButton(aBtn);
                        /// @todo FBoard.Player(TBzmConst.PASSIVE).HighlightedDice = FBoard.Player(TBzmConst.ACTIVE).HighlightedDice; 
                        FBoard.setGameState(GameStates.FirstDice);
                        /// @todo FBoard.CurrentSound = TBzmSound.Roll;
                        FMatch.getTimer().Switch(FBoard.getActivePlayerId());
                        break;
                    case Ok:
                    case Roll:
                        if (aParam != 0) // Если значения кубиков получены из DiceManager
                        {
                            do
                            {
                                aDice = aParam;
                                aDie1 = (byte)(aDice / 10);
                                aDie2 = (byte)(aDice % 10);
                                aParam = FMatch.getNextDiceFromSet();
                            } while (aDie1 * aDie2 == 0 || aDie1 == aDie2);
                            FBoard.setActivePlayerId((aDie1 > aDie2) ? PlayerColors.WHITE : PlayerColors.BLACK);
                            /// @todo FBoard.Player(TBzmConst.ACTIVE).setDice(aDie1, aDie2);
                            /// @todo FBoard.Player(TBzmConst.PASSIVE).setDice(aDie1, aDie2);
                            FMatch.getCurrentGame().AddDice(aDie1, aDie2);
                            FBoard.setGameState(GameStates.MoveMaking);
                            /// @todo FBoard.CurrentSound = TBzmSound.Roll;
                            FMatch.getTimer().Switch(FBoard.getActivePlayerId());
                        }
                        // Если активен ручной режим ввода кубиков, пытаемся вычислить значения кубиков из начального хода
                        if (aParam == 0 && FMatch.getMatchParameters().CalculateRolls)
                        {
                            FBoard.setActivePlayerId(PlayerColors.WHITE);
                            TBzmMove move = FBoard.FindAndApplyNextMoveAndDice(FMatch.getMatchParameters().ExactRolls);
                            if (move == null)
                            {
                                FBoard.setActivePlayerId(PlayerColors.BLACK);
                                move = FBoard.FindAndApplyNextMoveAndDice(FMatch.getMatchParameters().ExactRolls);
                            }
                            if (move != null)
                            {
                                    FMatch.GameAddMove(Actions.Move,
                                                        FBoard.getActivePlayerId(),
                                                        FBoard.getCurrentMoveNo(),
                                                        FBoard.Player(BezmaConstants.ACTIVE).getSeconds(),
                                                        FBoard.getPositionString(),
                                                        move);
                                    /// @todo FBoard.CurrentSound = TBzmSound.Move;
                                    /// @todo FBoard.Player(TBzmConst.ACTIVE).SetDice(move.GetDie(1), move.GetDie(2)); 
                                    changeCurrentPlayer();
                                    FBoard.setGameState(GameStates.MoveAcceptingOk);
                            }
                            else
                            {
                                FBoard.setActivePlayerId(PlayerColors.NONE);
                            }
                        }

                        break;
                }
                break;
            case FirstDice:
            case Dice:
                if (aPlayerId != FBoard.getActivePlayerId()) return false;
                switch(msg)
                {
                    case Dice: // Если нажата кнопка кубиков
                        if (FMatch.getMatchParameters().RollType == RollTypes.Manual) 
                        {                                
                            /// @todo FBoard.Player(TBzmConst.ACTIVE).OnPressButton(aBtn);
                            /// @todo FBoard.Player(TBzmConst.PASSIVE).HighlightedDice = FBoard.Player(TBzmConst.ACTIVE).HighlightedDice;
                            int diceCount = 2; /// @todo FBoard.getPlayer(TBzmConst.ACTIVE).GetDice(out aDie1, out aDie2);
                            if (diceCount == 2)
                                FBoard.setGameState(GameStates.MoveMaking);
                            if (diceCount == 0)
                                FBoard.setGameState(GameStates.MoveAccepting);
                            /// @todo FBoard.CurrentSound = TBzmSound.Die;
                        }
                        break;
                    case Ok:  // Если нажата кнопка ОК или Roll
                    case Roll:
                        if (FMatch.getMatchParameters().RollType != RollTypes.Manual) 
                        {
                            aDice = aParam;
                            FMatch.getNextDiceFromSet();
                            aDie1 = (byte)(aDice / 10);
                            aDie2 = (byte)(aDice % 10);
                            FBoard.Player(BezmaConstants.ACTIVE).SetDice(aDie1, aDie2);
                            FBoard.Player(BezmaConstants.PASSIVE).SetDice(aDie1, aDie2);
                            FMatch.getCurrentGame().AddDice(aDie1, aDie2);
                            FBoard.setGameState(GameStates.MoveMaking);
                            /// @todo FBoard.CurrentSound = TBzmSound.Roll;
                        }
                        break;
                    case Reset: // Если нажата кнопка Reset
                        if (FMatch.getCurrentGame().getMoveCount() == 0) // Только в случае первого хода
                        {
                            // Возвращаем автомат в сосотояние выбора игрока
                            FBoard.setGameState(GameStates.PlayerSelecting);
                            FBoard.Player(BezmaConstants.ACTIVE).ClearDice();
                            FBoard.Player(BezmaConstants.PASSIVE).ClearDice();
                            FMatch.getTimer().Pause();
                        }
                        break;
                }
                break;
            case MoveMaking:
                if (FBoard.getActivePlayerId() != aPlayerId)
                    return false;
                switch(msg)
                {
                    case Dice:
                        if (FMatch.getMatchParameters().RollType == RollTypes.Manual) 
                        {
                            /// @todo FBoard.Player(TBzmConst.ACTIVE).OnPressButton(aBtn);
                            if(FMatch.getCurrentGame().getMoveCount() == 0)
                                FBoard.setGameState(GameStates.FirstDice);
                            else
                                FBoard.setGameState(GameStates.Dice);
                            /// @todo FBoard.Player(TBzmConst.PASSIVE).HighlightedDice = FBoard.Player(TBzmConst.ACTIVE).HighlightedDice; 
                            /// @todo FBoard.CurrentSound = TBzmSound.Die;
                        }
                        break;
                    /*
                    case Pass:
                        FBoard.Player(TBzmConst.PASSIVE).GetDice(out aDie1, out aDie2);
                        FMatch.getCurrentGame().AddDice(aDie1, aDie2);
                        if (FMatch.getMatchParameters().MatchType == TBzmMatchWinType.Scores)
                        {
                            FBoard.setGameState(TBzmGameState.PassTypeSelecting;
                        }
                        else
                        {
                            changeCurrentPlayer();
                            FBoard.setGameState(TBzmGameState.PassAccepting;
                            /// @todo FBoard.CurrentSound = TBzmSound.Resign;
                        }
                        //PrevGameState = TBzmGameState.MoveMaking;
                        break;
                     */
                    case Ok:
                        TBzmMove move = FBoard.FindAndApplyNextMove();
                        if (move != null)
                        {
                            if (move.getMovementCount() == 0)
                            {
                                FMatch.GameAddMove(Actions.SkipMove,
                                                    FBoard.getActivePlayerId(),
                                                    FBoard.getCurrentMoveNo(),
                                                    FBoard.Player(BezmaConstants.ACTIVE).getSeconds(),
                                                    FBoard.getPositionString(),
                                                    move);
                                /// @todo FBoard.CurrentSound = TBzmSound.SkipMove;
                            }
                            else
                            {
                                FMatch.GameAddMove(Actions.Move,
                                                    FBoard.getActivePlayerId(),
                                                    FBoard.getCurrentMoveNo(),
                                                    FBoard.Player(BezmaConstants.ACTIVE).getSeconds(),
                                                    FBoard.getPositionString(),
                                                    move);
                                /// @todo FBoard.CurrentSound = TBzmSound.Move;
                            }
                            if (FBoard.Pips(FBoard.getActivePlayerId()) == 0)
                            {
                                changeCurrentPlayer();                                    
                                FBoard.setGameState(GameStates.LastMoveAccepting);
                            }
                            else
                            {
                                changeCurrentPlayer();                                    
                                // Если игрок застрял на баре, он автоматически пропускает ход
                                if (FBoard.CannotMovingFromBar())                   
                                    FBoard.setGameState(GameStates.SkipMoveAccepting);
                                // конец изменений
                                else 
                                {
                                    if (FMatch.getMatchParameters().CalculateRolls)
                                        FBoard.setGameState(GameStates.MoveAcceptingOk);
                                    else
                                        FBoard.setGameState(GameStates.MoveAccepting);
                                }
                            }                                
                        }
                        else 
                        {
                            /// @todo FBoard.Player(TBzmConst.ACTIVE).GetDice(out aDie1, out aDie2);                                
                            FBoard.Player(BezmaConstants.ACTIVE).setPlayerState(PlayerStates.WrongMoveMade);
                            FBoard.Player(BezmaConstants.ACTIVE).setDisplayText( ""/** @todo String.Format(TBzmConst.cstrWrongMove, aDie1, aDie2)*/) ;
                            /// @todo FBoard.CurrentSound = TBzmSound.Error;
                        }
                        break;
                }
                break;
            case SkipMoveAccepting:
                switch (msg) 
                {
                    case Ok:
                        if (FMatch.getMatchParameters().RollType != RollTypes.Manual)
                        {
                            aDice = aParam;
                            FMatch.getNextDiceFromSet();
                            aDie1 = (byte)(aDice / 10);
                            aDie2 = (byte)(aDice % 10);
                        }
                        else 
                        {   
                            /// @todo Bezma.Random.IBzmRandom rnd = FMatch.getMatchParameters().Randomiser;
                            /// @todo aDie1 = (byte)(rnd.Next(1,6));
                        	/// @todo aDie2 = (byte)(rnd.Next(1,6));
                        }
                        FBoard.Player(BezmaConstants.ACTIVE).SetDice(aDie1, aDie2);
                        FBoard.Player(BezmaConstants.PASSIVE).ClearDice();
                        FMatch.getCurrentGame().AddDice(aDie1, aDie2);
                        TBzmMove move = new TBzmMove(aDie1, aDie2, aPlayerId);
                        FMatch.GameAddMove(Actions.SkipMove,
                                            FBoard.getActivePlayerId(),
                                            FBoard.getCurrentMoveNo(),
                                            FBoard.Player(BezmaConstants.ACTIVE).getSeconds(),
                                            FBoard.getPositionString(),
                                            move);
                        changeCurrentPlayer();
                        
                        if (FMatch.getMatchParameters().CalculateRolls)
                            FBoard.setGameState(GameStates.MoveAcceptingOk);
                        else
                            FBoard.setGameState(GameStates.MoveAccepting);
                        /// @todo FBoard.CurrentSound = TBzmSound.SkipMove;

                        break;
                    case Reset:
                        FBoard.ResetMove();
                        changeCurrentPlayer();                            
                        FBoard.setGameState(GameStates.MoveMaking);
                        FMatch.GameCancelMove();
                        /// @todo FBoard.CurrentSound = TBzmSound.Reset;
                        break;
                    case Pass:
                        /// @todo FBoard.Player(TBzmConst.PASSIVE).GetDice(out aDie1, out aDie2);
                        FMatch.getCurrentGame().AddDice(aDie1, aDie2);
                        if (FMatch.getMatchParameters().MatchType == MatchWinConditions.Scores)
                        {
                            FBoard.setGameState(GameStates.PassTypeSelecting);
                        }
                        else
                        {
                            changeCurrentPlayer();                                
                            FBoard.setGameState(GameStates.PassAccepting);
                            /// @todo FBoard.CurrentSound = TBzmSound.Resign;
                        }
                        break;
                    case Double:
                        if (FBoard.getRateDie().CanDouble(aPlayerId))
                        {
                            FBoard.setGameState(GameStates.DoubleConfirm);
                            /*FBoard.getRateDie().Double(aPlayerId);
                            changeCurrentPlayer();
                            FBoard.setGameState(TBzmGameState.DoubleAcceptingBeaver;
                            /// @todo FBoard.CurrentSound = TBzmSound.Double;*/
                        }
                        break;

                }
                break;
            case MoveAcceptingOk:
            case MoveAccepting:
                if (FBoard.getActivePlayerId() != aPlayerId) return false;
                switch (msg) 
                {
                        //добавлено для возможности поиска хода
                    case Ok:
                        if (FMatch.getMatchParameters().RollType != RollTypes.Manual)
                        {
                            aDice = aParam;
                            FMatch.getNextDiceFromSet();
                            aDie1 = (byte)(aDice / 10);
                            aDie2 = (byte)(aDice % 10);
                            FBoard.Player(BezmaConstants.ACTIVE).SetDice(aDie1, aDie2);
                            FBoard.Player(BezmaConstants.PASSIVE).SetDice(aDie1, aDie2);
                            FMatch.getCurrentGame().AddDice(aDie1, aDie2);
                            FBoard.setGameState(GameStates.MoveMaking);
                            /// @todo FBoard.CurrentSound = TBzmSound.Roll;
                        }
                        else 
                        {
                            if (FBoard.getGameState() != GameStates.MoveAcceptingOk)
                                return false;
                            /// @todo FBoard.Player(TBzmConst.PASSIVE).GetDice(out aDie1, out aDie2);
                            FMatch.getCurrentGame().AddDice(aDie1, aDie2);

                            TBzmMove move = FBoard.FindAndApplyNextMoveAndDice(FMatch.getMatchParameters().CalculateRolls);
                            if (move != null)
                            {
                                FBoard.Player(BezmaConstants.ACTIVE).SetDice(move.GetDie(1), move.GetDie(2));
                                if (move.getMovementCount() == 0)
                                {
                                    FMatch.GameAddMove(Actions.SkipMove,
                                                        FBoard.getActivePlayerId(),
                                                        FBoard.getCurrentMoveNo(),
                                                        FBoard.Player(BezmaConstants.ACTIVE).getSeconds(),
                                                        FBoard.getPositionString(),
                                                        move);
                                    /// @todo FBoard.CurrentSound = TBzmSound.SkipMove;
                                }
                                else
                                {
                                    FMatch.GameAddMove(Actions.Move,
                                                        FBoard.getActivePlayerId(),
                                                        FBoard.getCurrentMoveNo(),
                                                        FBoard.Player(BezmaConstants.ACTIVE).getSeconds(),
                                                        FBoard.getPositionString(),
                                                        move);
                                    /// @todo FBoard.CurrentSound = TBzmSound.Move;
                                }
                                if (FBoard.Pips(FBoard.getActivePlayerId()) == 0)
                                {
                                    changeCurrentPlayer();
                                    FBoard.setGameState(GameStates.LastMoveAccepting);
                                }
                                else
                                {
                                    changeCurrentPlayer();
                                    // Если игрок застрял на баре, он автоматически пропускает ход
                                    if (FBoard.CannotMovingFromBar())
                                        FBoard.setGameState(GameStates.SkipMoveAccepting);
                                    // конец изменений
                                    else
                                    {

                                        if (FMatch.getMatchParameters().CalculateRolls)
                                            FBoard.setGameState(GameStates.MoveAcceptingOk);
                                        else
                                            FBoard.setGameState(GameStates.MoveAccepting);
                                    }
                                }
                            }
                            else
                            {
                                // FBoard.setGameState(TBzmGameState.Dice;
                                // FBoard.Player(TBzmConst.ACTIVE).PlayerState = TBzmPlayerState.Dice;
                                // FBoard.Player(TBzmConst.ACTIVE).DisplayText = TBzmConst.cstrDice;
                                /// @todo FBoard.CurrentSound = TBzmSound.Error;
                            }
                        }

                        break;
                        // конец

                    case Dice:
                        if (FMatch.getMatchParameters().RollType == RollTypes.Manual) 
                        {                                
                            /// @todo FBoard.Player(TBzmConst.PASSIVE).GetDice(out aDie1, out aDie2);
                            FMatch.getCurrentGame().AddDice(aDie1, aDie2);
                            FBoard.setGameState(GameStates.Dice);
                            /// @todo FBoard.Player(TBzmConst.ACTIVE).OnPressButton(aBtn);
                            /// @todo FBoard.Player(TBzmConst.PASSIVE).HighlightedDice = FBoard.Player(TBzmConst.ACTIVE).HighlightedDice; 
                            /// @todo FBoard.CurrentSound = TBzmSound.Roll;
                        }
                        break;
                    case Roll: // Отсавлено для возможности ROLL вместо ОК
                        if (FMatch.getMatchParameters().RollType != RollTypes.Manual) 
                        {
                            aDice = aParam;
                            FMatch.getNextDiceFromSet();
                            aDie1 = (byte)(aDice / 10);
                            aDie2 = (byte)(aDice % 10);
                            FBoard.Player(BezmaConstants.ACTIVE).SetDice(aDie1, aDie2);
                            FBoard.Player(BezmaConstants.PASSIVE).SetDice(aDie1, aDie2);
                            FMatch.getCurrentGame().AddDice(aDie1, aDie2);
                            FBoard.setGameState(GameStates.MoveMaking);
                            /// @todo FBoard.CurrentSound = TBzmSound.Roll;
                        }
                        break;
                    case Reset:
                        FBoard.ResetMove();
                        FMatch.GameCancelMove();
                        /// @todo FBoard.CurrentSound = TBzmSound.Reset;
                        if (FMatch.getCurrentGame().getMoveCount() == 0)
                        { // отмена первого хода. возвращаем игру в позицию выбора игрока
                            FBoard.setActivePlayerId(PlayerColors.NONE);
                            FBoard.setGameState(GameStates.PlayerSelecting);
                            /// @todo FBoard.Player(TBzmConst.ACTIVE).HighlightedDice = 0; 
                            /// @todo FBoard.Player(TBzmConst.PASSIVE).HighlightedDice = 0; 
                        }
                        else 
                        { // отмена не первого хода, как обычно
                            changeCurrentPlayer();
                            FBoard.setGameState(GameStates.MoveMaking);
                        }
                        break;
                    case Double:
                        if(FBoard.getRateDie().CanDouble(aPlayerId))
                        {
                            /** @todo if (FMatch.getMatchParameters().ConfirmCube)
                                FBoard.setGameState(TBzmGameState.DoubleConfirm;
                            else*/ 
                            {                                    
                                FBoard.getRateDie().Double(aPlayerId);
                                changeCurrentPlayer();                            
                                if(FMatch.getMatchParameters().MatchType == MatchWinConditions.MoneyGame)
                                    FBoard.setGameState(GameStates.DoubleAcceptingBeaver);
                                else
                                    FBoard.setGameState(GameStates.DoubleAccepting);
                                /// @todo FBoard.CurrentSound = TBzmSound.Double;
                            }
                        }
                        break;
                    case Pass:                            
                        /// @todo FBoard.Player(TBzmConst.PASSIVE).GetDice(out aDie1, out aDie2);
                        FMatch.getCurrentGame().AddDice(aDie1, aDie2);
                        if (FMatch.getMatchParameters().MatchType == MatchWinConditions.Scores)
                        {
                            FBoard.setGameState(GameStates.PassTypeSelecting);
                        }
                        else 
                        {
                            changeCurrentPlayer();                                
                            FBoard.setGameState(GameStates.PassAccepting);
                            /// @todo FBoard.CurrentSound = TBzmSound.Resign;
                        }
                        //PrevGameState = TBzmGameState.MoveAccepting;
                        break;
                }
                break;
            case DoubleConfirm:
                if (FBoard.getActivePlayerId() != aPlayerId) return false;
                switch (msg) 
                {
                    case Ok:
                        FBoard.getRateDie().Double(aPlayerId);
                        changeCurrentPlayer();
                        if (FMatch.getMatchParameters().MatchType == MatchWinConditions.MoneyGame)
                            FBoard.setGameState(GameStates.DoubleAcceptingBeaver);
                        else
                            FBoard.setGameState(GameStates.DoubleAccepting);
                        /// @todo FBoard.CurrentSound = TBzmSound.Double;
                        break;
                    case Reset:
                        FBoard.RollbackGameState();                            
                        break;
                }
                break;
            case TakeConfirm:
                if (FBoard.getActivePlayerId() != aPlayerId) return false;
                switch (msg)
                {
                    case Ok:
                        FBoard.getRateDie().Take();
                        changeCurrentPlayer();
                        // Если игрок застрял на баре, он автоматически пропускает ход
                        if (FBoard.CannotMovingFromBar())
                            FBoard.setGameState(GameStates.SkipMoveAccepting);
                        else
                            FBoard.setGameState(GameStates.MoveAcceptingOk); // Before 17.04.2009 it was FBoard.setGameState(TBzmGameState.Dice;
                        FMatch.GameAddMove(Actions.Double,
                                    FBoard.getActivePlayerId(),
                                    FBoard.getCurrentMoveNo(),
                                    FBoard.Player(BezmaConstants.ACTIVE).getSeconds(),
                                    FBoard.getPositionString(),                                        
                                    FBoard.getRateDie().getRateLevel(), true, false);
                        /// @todo FBoard.CurrentSound = TBzmSound.Take;

                        break;
                    case Reset:
                        FBoard.RollbackGameState();
                        break;
                }
                break;
            case PassConfirm:
                if (FBoard.getActivePlayerId() != aPlayerId) return false;
                switch (msg)
                {
                    case Ok:
                        FBoard.getRateDie().Pass();
                        changeCurrentPlayer();                            
                        FMatch.GameAddMove(Actions.Double,
                                    FBoard.getActivePlayerId(),
                                    FBoard.getCurrentMoveNo(),
                                    FBoard.Player(BezmaConstants.ACTIVE).getSeconds(),
                                    FBoard.getPositionString(),
                                    (byte)(FBoard.getRateDie().getRateLevel() * 2), false, false);
                        FBoard.setGameState(FMatch.GameEnd(FBoard.getActivePlayerId(), FBoard.getRateDie().getRateLevel(), FBoard.CheckersOnBoard(FBoard.getPassivePlayerId()), true, VictoryTypes.Normal));
                        /// @todo FBoard.CurrentSound = TBzmSound.Pass;
                        break;
                    case Reset:
                        FBoard.RollbackGameState();
                        break;
                }
                break;
            case DoubleAccepting:
            case DoubleAcceptingBeaver:
                if (FBoard.getActivePlayerId() != aPlayerId) return false;
                switch (msg) 
                {
                    case Take:
                        if (FMatch.getMatchParameters().ConfirmDouble)
                        {
                            FBoard.setGameState(GameStates.TakeConfirm);
                        }
                        else 
                        {
                            /* то же самое сделать в TakeConfirm - ОК*/
                             FBoard.getRateDie().Take();
                            changeCurrentPlayer();
                            // Если игрок застрял на баре, он автоматически пропускает ход
                            if (FBoard.CannotMovingFromBar())
                                FBoard.setGameState(GameStates.SkipMoveAccepting);
                            else
                                FBoard.setGameState(GameStates.Dice);
                            FMatch.GameAddMove(Actions.Double,
                                        FBoard.getActivePlayerId(),
                                        FBoard.getCurrentMoveNo(),
                                        FBoard.Player(BezmaConstants.ACTIVE).getSeconds(),
                                        FBoard.getPositionString(),                                        
                                        FBoard.getRateDie().getRateLevel(), true, false);
                            /// @todo FBoard.CurrentSound = TBzmSound.Take;
                        }
                        
                        break;
                    case Beaver:
                        if (FMatch.getMatchParameters().MatchType == MatchWinConditions.MoneyGame) 
                        {
                            FBoard.getRateDie().Beaver();
                            changeCurrentPlayer();
                            FBoard.setGameState(GameStates.BeaverAccepting);
                            /// @todo FBoard.CurrentSound = TBzmSound.Beaver;
                        }
                        break;
                    case Pass:
                        if (FMatch.getMatchParameters().ConfirmDouble)
                        {
                            FBoard.setGameState(GameStates.PassConfirm);
                        }
                        else 
                        {
                            /* продублировать в PassConfirm */
                            FBoard.getRateDie().Pass();
                            changeCurrentPlayer();                            
                            FMatch.GameAddMove(Actions.Double,
                                        FBoard.getActivePlayerId(),
                                        FBoard.getCurrentMoveNo(),
                                        FBoard.Player(BezmaConstants.ACTIVE).getSeconds(),
                                        FBoard.getPositionString(),
                                        (byte)(FBoard.getRateDie().getRateLevel() * 2), false, false);
                            FBoard.setGameState(FMatch.GameEnd(FBoard.getActivePlayerId(), FBoard.getRateDie().getRateLevel(), FBoard.CheckersOnBoard(FBoard.getPassivePlayerId()), true, VictoryTypes.Normal));
                            /// @todo FBoard.CurrentSound = TBzmSound.Pass;
                        }                            
                        break;
                }
                break;
            case BeaverAccepting:
                if (FBoard.getActivePlayerId() != aPlayerId) return false;
                switch (msg) 
                {
                    case Reset:
                        FBoard.setGameState(GameStates.DoubleAcceptingBeaver);
                        FBoard.getRateDie().ResetBeaver();
                        /// @todo FBoard.CurrentSound = TBzmSound.Reset;
                        break;
                    case Take:
                        FBoard.setGameState(GameStates.Dice);
                        FMatch.GameAddMove(Actions.Double,
                                        FBoard.getActivePlayerId(),
                                        FBoard.getCurrentMoveNo(),
                                        FBoard.Player(BezmaConstants.ACTIVE).getSeconds(),
                                        FBoard.getPositionString(),
                                        (byte)(FBoard.getRateDie().getRateLevel() / 2), true, true);
                        /// @todo FBoard.CurrentSound = TBzmSound.Take;
                        break;
                    case Pass:
                        FBoard.getRateDie().Pass();
                        FMatch.GameAddMove(Actions.Double,
                                    FBoard.getActivePlayerId(),
                                    //FBoard.PassivePlayerId,
                                    FBoard.getCurrentMoveNo(),
                                    FBoard.Player(BezmaConstants.ACTIVE).getSeconds(),
                                    FBoard.getPositionString(),
                                    FBoard.getRateDie().getRateLevel(), false, true);
                        //FBoard.setGameState(FMatch.GameEnd(FBoard.getActivePlayerId(), FBoard.getRateDie().RateLevel, FBoard.CheckersOnBoard(FBoard.PassivePlayerId), true, TBzmGameWinType.Normal);
                        FBoard.setGameState(FMatch.GameEnd(FBoard.getPassivePlayerId(), FBoard.getRateDie().getRateLevel(), FBoard.CheckersOnBoard(FBoard.getActivePlayerId()), true, VictoryTypes.Normal));
                        /// @todo FBoard.CurrentSound = TBzmSound.Pass;
                        break;
                }
                break;
            case LastMoveAccepting:
                if (FBoard.getActivePlayerId() != aPlayerId) return false;
                switch (msg) 
                {
                    case Ok:
                        if (FMatch.getMatchParameters().RollType == RollTypes.Manual) 
                        {                                
                            /// @todo FBoard.Player(TBzmConst.PASSIVE).GetDice(out aDie1, out aDie2);
                            FMatch.getCurrentGame().AddDice(aDie1, aDie2);
                        }
                        VictoryTypes WinType = VictoryTypes.Normal;
                        if (FBoard.IsGammon(FBoard.getActivePlayerId()))
                        {
                            WinType = VictoryTypes.Gammon;
                            if (FBoard.IsBackgammon(FBoard.getActivePlayerId()))
                                WinType = VictoryTypes.Backgammon;
                        }
                        FBoard.setGameState(FMatch.GameEnd(FBoard.getPassivePlayerId(), FBoard.getRateDie().getRateLevel(), FBoard.CheckersOnBoard(FBoard.getActivePlayerId()), false, WinType));
                        /// @todo FBoard.CurrentSound = TBzmSound.GameOver;                            
                        break;
                    case Reset:
                        FBoard.ResetMove();
                        changeCurrentPlayer();                            
                        FBoard.setGameState(GameStates.MoveMaking);
                        FMatch.GameCancelMove();
                        /// @todo FBoard.CurrentSound = TBzmSound.Reset;
                        break;
                }
                break;
            case PassAccepting:
                if (FBoard.getActivePlayerId() != aPlayerId)
                    return false;
                switch (msg) 
                {
                    case Ok:
                        FBoard.setGameState(FMatch.GameEnd(FBoard.getActivePlayerId(), FBoard.getRateDie().getRateLevel(), FBoard.CheckersOnBoard(FBoard.getPassivePlayerId()), false, VictoryTypes.Normal));
                        /// @todo FBoard.CurrentSound = TBzmSound.GameOver;
                        break;
                    case Reset:
                        changeCurrentPlayer();                            
                        FBoard.setGameState(GameStates.Dice);
                        /// @todo FBoard.CurrentSound = TBzmSound.Reset;
                        break;
                }
                break;
            case PassTypeSelecting:
                if (FBoard.getActivePlayerId() != aPlayerId)
                    return false;
                switch (msg)
                {
                    case Dice:
                        if (aBtn == Buttons.Die1) 
                        {
                            changeCurrentPlayer();                                
                            FBoard.setGameState(GameStates.PassNormalAccepting);
                            /// @todo FBoard.CurrentSound = TBzmSound.Pass;
                        }
                        if (aBtn == Buttons.Die2)
                        {
                            changeCurrentPlayer();                                
                            FBoard.setGameState(GameStates.PassGammonAccepting);
                            /// @todo FBoard.CurrentSound = TBzmSound.Pass;
                        }                            
                        break;
                    case Reset:
                        FBoard.setGameState(GameStates.Dice);// PrevGameState = TBzmGameState.MoveMaking; 

                        break;
                }
                break;
            case PassGammonAccepting:
                if (FBoard.getActivePlayerId() != aPlayerId)
                    return false;
                switch (msg)
                {
                    case Ok:
                        FBoard.setGameState(FMatch.GameEnd(FBoard.getActivePlayerId(), FBoard.getRateDie().getRateLevel(), FBoard.CheckersOnBoard(FBoard.getPassivePlayerId()), false, VictoryTypes.Gammon));
                        /// @todo FBoard.CurrentSound = TBzmSound.GameOver;
                        break;
                    case Reset:
                        changeCurrentPlayer();                            
                        FBoard.setGameState(GameStates.Dice);
                        /// @todo FBoard.CurrentSound = TBzmSound.Reset;
                        break;
                }
                break;
            case PassNormalAccepting:
                if (FBoard.getActivePlayerId() != aPlayerId)
                    return false;
                switch (msg)
                {
                    case Ok:
                        FBoard.setGameState(FMatch.GameEnd(FBoard.getActivePlayerId(), FBoard.getRateDie().getRateLevel(), FBoard.CheckersOnBoard(FBoard.getPassivePlayerId()), false, VictoryTypes.Normal));
                        /// @todo FBoard.CurrentSound = TBzmSound.GameOver;
                        break;
                    case Reset:
                        changeCurrentPlayer();                            
                        FBoard.setGameState(GameStates.Dice);
                        /// @todo FBoard.CurrentSound = TBzmSound.Reset;
                        break;
                }
                break;
        }
        return true;
    }

    public BoardAutomat(TBzmStateList aStateList) 
    {
        FBoard = new TBzmBoard(aStateList);
        //FTimer = new Timer(new TimerCallback(OnTimer));
        //FTimer.Change(Timeout.Infinite, 1000);
    }
    public  boolean PostMessage(Buttons aBtn, PlayerColors aPlayerId, int aParam)
    {
        switch (aBtn)
        {
        /** @todo
            case Die1:
            case Die2:
            case Die3:
            case Die4:
            case Die5:
            case Die6:
            case Doublet:
                return ProcessMessage(TBzmMessages.Dice, aPlayerId, aBtn, aParam);
            case TBzmButtons.Take:
                return ProcessMessage(TBzmMessages.Take, aPlayerId, aBtn, aParam);
            case TBzmButtons.Double:
                return ProcessMessage(TBzmMessages.Double, aPlayerId, aBtn, aParam);
            case TBzmButtons.Pass:
                return ProcessMessage(TBzmMessages.Pass, aPlayerId, aBtn, aParam);
            case TBzmButtons.Clear:
                return ProcessMessage(TBzmMessages.Reset, aPlayerId, aBtn, aParam);
            case TBzmButtons.Ok:
                return ProcessMessage(TBzmMessages.Ok, aPlayerId, aBtn, aParam);
            case TBzmButtons.Beaver:
                return ProcessMessage(TBzmMessages.Beaver, aPlayerId, aBtn, aParam);
            case TBzmButtons.Info:
                return ProcessMessage(TBzmMessages.Info, aPlayerId, aBtn, aParam);
            case TBzmButtons.Position:
                return ProcessMessage(TBzmMessages.Position, aPlayerId, aBtn, aParam);
            case TBzmButtons.Roll:
                return ProcessMessage(TBzmMessages.Roll, aPlayerId, aBtn, aParam);*/
            default:
                return false;
        }
    }
    public TBzmBoard getBoard() {
        return FBoard; 
    }
    public TBzmMatch getMatch() 
    {
        return FMatch; 
    }
    public void setMatch(TBzmMatch match)
    {
        FMatch = match; 
    }

}
