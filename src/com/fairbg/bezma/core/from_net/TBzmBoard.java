package com.fairbg.bezma.core.from_net;

import java.util.ArrayList;

import com.fairbg.bezma.core.from_net.enums.GameStates;
import com.fairbg.bezma.core.from_net.enums.PlayerColors;
import com.fairbg.bezma.core.from_net.enums.PlayerStates;
import com.fairbg.bezma.core.from_net.enums.BezmaConstants;
import com.fairbg.bezma.core.from_net.enums.Rules;

public class TBzmBoard {
    // private 
    private class BoardState 
    {
        public IBzmBoardPosition PrevBoardPosition;
        public IBzmBoardPosition BoardPosition;
        public GameStates GameState;
        public PlayerColors ActivePlayerId;
        public boolean Paused;
        public byte MoveNo;
        public void CopyTo(BoardState aNewCopy) 
        {
            aNewCopy.BoardPosition = BoardPosition.Clone();
            aNewCopy.GameState = GameState;
            aNewCopy.ActivePlayerId = ActivePlayerId;
            aNewCopy.Paused = Paused;
            aNewCopy.MoveNo = MoveNo;
            ///@todo aNewCopy.CurrentSound = CurrentSound;
        }
    }

    // Неизяменяющиеся в течение игры свойства board
    private IBzmRules m_Rules;
    private TBzmRateDie m_RateDie;
    private boolean m_Reflected;
    private TBzmBoardPlayer m_PlayerWhite, m_PlayerBlack;
    private TBzmStateList m_BoardStateList;
    private short m_MaxSeconds;

    // Изяменяющиеся в течение игры свойства board
    private BoardState m_CurrentState = new BoardState();
    private BoardState m_PrevState = new BoardState();

    public void RollbackGameState()
    {
        setGameState(m_PrevState.GameState);
    }
    private void SetState(GameStates value)
    {
        if (m_CurrentState.GameState != value)
            m_PrevState.GameState = m_CurrentState.GameState;
        m_CurrentState.GameState = value;
        switch (m_CurrentState.GameState)
        {
            case LookMode:
                getRateDie().InitRateDie();
                m_PlayerWhite.setPlayerState(PlayerStates.Look);
                m_PlayerBlack.setPlayerState(PlayerStates.Look);
                break;
            case None:
            case NextGame:
                getRateDie().InitRateDie();
                m_CurrentState.BoardPosition = m_Rules.StartPosition();
                //GetBeginState.Clone(FBoardPosition);
                m_PlayerWhite.setPlayerState(PlayerStates.None);
                m_PlayerBlack.setPlayerState(PlayerStates.None);
                m_CurrentState.MoveNo = 0;
                break;
            case PlayerSelecting:
                m_PlayerWhite.setPlayerState(PlayerStates.PlayerNotSelected);
                m_PlayerBlack.setPlayerState(PlayerStates.PlayerNotSelected);
                m_CurrentState.MoveNo = 0;
                break;
            case FirstDice:
                Player(BezmaConstants.ACTIVE).setPlayerState(PlayerStates.FirstDice);
                Player(BezmaConstants.PASSIVE).setPlayerState(PlayerStates.Inactive);
                break;
            case Dice:
                Player(BezmaConstants.ACTIVE).setPlayerState(PlayerStates.Dice);
                //Player(TBzmConst.PASSIVE).ClearDice();
                Player(BezmaConstants.PASSIVE).setPlayerState(PlayerStates.Inactive);
                break;
            case Roll:
                Player(BezmaConstants.ACTIVE).setPlayerState(PlayerStates.Roll);
                //Player(TBzmConst.PASSIVE).ClearDice();
                Player(BezmaConstants.PASSIVE).setPlayerState(PlayerStates.Inactive);
                break;
            case DoubleConfirm:
                Player(BezmaConstants.ACTIVE).ClearDice();
                Player(BezmaConstants.PASSIVE).setPlayerState(PlayerStates.Inactive);
                Player(BezmaConstants.PASSIVE).ClearDice();
                Player(BezmaConstants.ACTIVE).setPlayerState(PlayerStates.DoubleConfirm);
                break;
            case TakeConfirm:
                Player(BezmaConstants.ACTIVE).ClearDice();
                Player(BezmaConstants.PASSIVE).setPlayerState(PlayerStates.Inactive);
                Player(BezmaConstants.PASSIVE).ClearDice();
                Player(BezmaConstants.ACTIVE).setPlayerState(PlayerStates.TakeConfirm);
                break;
            case PassConfirm:
                Player(BezmaConstants.ACTIVE).ClearDice();
                Player(BezmaConstants.PASSIVE).setPlayerState(PlayerStates.Inactive);
                Player(BezmaConstants.PASSIVE).ClearDice();
                Player(BezmaConstants.ACTIVE).setPlayerState(PlayerStates.PassConfirm);
                break;
            case DoubleAcceptingBeaver:
                Player(BezmaConstants.ACTIVE).ClearDice();
                Player(BezmaConstants.PASSIVE).setPlayerState(PlayerStates.Inactive);
                Player(BezmaConstants.PASSIVE).ClearDice();
                Player(BezmaConstants.ACTIVE).setPlayerState(PlayerStates.DoubleAcceptingBeaver);
                m_CurrentState.MoveNo++;
                break;
            case DoubleAccepting:
                Player(BezmaConstants.ACTIVE).ClearDice();
                Player(BezmaConstants.PASSIVE).setPlayerState(PlayerStates.Inactive);
                Player(BezmaConstants.PASSIVE).ClearDice();
                Player(BezmaConstants.ACTIVE).setPlayerState(PlayerStates.DoubleAccepting);
                m_CurrentState.MoveNo++;
                break;
            case BeaverAccepting:
                Player(BezmaConstants.ACTIVE).ClearDice();
                Player(BezmaConstants.ACTIVE).setPlayerState(PlayerStates.BeaverAccepting);
                Player(BezmaConstants.PASSIVE).ClearDice();
                Player(BezmaConstants.PASSIVE).setPlayerState(PlayerStates.Inactive);
                break;
            case MoveMaking:
                Player(BezmaConstants.ACTIVE).setPlayerState(PlayerStates.MoveMaking);
                Player(BezmaConstants.PASSIVE).setPlayerState(PlayerStates.Inactive);
                break;
            case MoveAcceptingOk:
                Player(BezmaConstants.ACTIVE).ClearDice();
                Player(BezmaConstants.ACTIVE).setPlayerState(PlayerStates.MoveAcceptingOk);
                Player(BezmaConstants.PASSIVE).setPlayerState(PlayerStates.WaitForMoveAccept);
                break;
            case MoveAccepting:
                Player(BezmaConstants.ACTIVE).ClearDice();
                Player(BezmaConstants.ACTIVE).setPlayerState(PlayerStates.MoveAccepting);
                Player(BezmaConstants.PASSIVE).setPlayerState(PlayerStates.WaitForMoveAccept);
                break;
            case LastMoveAccepting:
                Player(BezmaConstants.ACTIVE).ClearDice();
                Player(BezmaConstants.ACTIVE).setPlayerState(PlayerStates.LastMoveAccepting);
                Player(BezmaConstants.PASSIVE).setPlayerState(PlayerStates.WaitForMoveAccept);
                break;
            case PassAccepting:
                Player(BezmaConstants.ACTIVE).ClearDice();
                Player(BezmaConstants.ACTIVE).setPlayerState(PlayerStates.PassAccept);
                Player(BezmaConstants.PASSIVE).setPlayerState(PlayerStates.WaitForPassAccept);
                break;
            case InitBoard:
                m_CurrentState.BoardPosition.ClearBoard();
                Player(BezmaConstants.ACTIVE).setPlayerState(PlayerStates.InitBoard);
                Player(BezmaConstants.PASSIVE).setPlayerState(PlayerStates.Inactive);
                break;
            case PassTypeSelecting:
                Player(BezmaConstants.ACTIVE).ClearDice();
                Player(BezmaConstants.ACTIVE).setPlayerState(PlayerStates.PassTypeSelect);
                Player(BezmaConstants.PASSIVE).setPlayerState(PlayerStates.Inactive);
                break;
            case PassGammonAccepting:
                Player(BezmaConstants.ACTIVE).ClearDice();
                Player(BezmaConstants.ACTIVE).setPlayerState(PlayerStates.PassGammonAccept);
                Player(BezmaConstants.PASSIVE).setPlayerState(PlayerStates.WaitForPassAccept);
                break;
            case PassNormalAccepting:
                Player(BezmaConstants.ACTIVE).ClearDice();
                Player(BezmaConstants.ACTIVE).setPlayerState(PlayerStates.PassNormalAccept);
                Player(BezmaConstants.PASSIVE).setPlayerState(PlayerStates.WaitForPassAccept);
                break;
            case SkipMoveAccepting:
                Player(BezmaConstants.ACTIVE).ClearDice();
                Player(BezmaConstants.ACTIVE).setPlayerState(PlayerStates.SkipMoving);
                Player(BezmaConstants.PASSIVE).setPlayerState(PlayerStates.WaitForMoveAccept);
                break;

        }
        m_PlayerWhite.setPips(m_CurrentState.BoardPosition.GetPips(m_PlayerWhite.getPlayerId()));
        m_PlayerBlack.setPips(m_CurrentState.BoardPosition.GetPips(m_PlayerBlack.getPlayerId()));
    }
    private TBzmMove FindMove(byte aDie1, byte aDie2, PlayerColors anActivePlayerId, IBzmBoardPosition aPositionFrom)
    {
        IBzmBoardPosition temp = TBzmBoardPositionFactory.Create(getRules().getRulesType(), m_Reflected);
        int stateCount = m_BoardStateList.getStateCount();
        TBzmMove move = null;
        for(int i = 0; i < stateCount;i++)
        {
            temp.ReadState(m_BoardStateList, i);
            move = m_Rules.FindMove(aDie1,aDie2,anActivePlayerId,aPositionFrom, temp);
            if (move != null)
            {
                return move;
            }
        }
        return null;
    }

    private boolean BoardPositionChanged()
    {
        IBzmBoardPosition temp_position = TBzmBoardPositionFactory.Create(getRules().getRulesType(), m_Reflected);
        int stateCount = m_BoardStateList.getStateCount();
        for(int i = 0; i< stateCount; i++)
        {
            temp_position.ReadState(m_BoardStateList, i);
            if (!m_CurrentState.BoardPosition.Like(temp_position))
                return true;
        }
        return false;
    }

    //Constructors
    public TBzmBoard(TBzmStateList aBoardStateList) 
    {
        m_CurrentState.Paused = false;
        m_BoardStateList = aBoardStateList;
        m_RateDie = new TBzmRateDie();
        m_PlayerWhite = new TBzmBoardPlayer(PlayerColors.WHITE, m_RateDie);
        m_PlayerBlack = new TBzmBoardPlayer(PlayerColors.BLACK, m_RateDie);
        m_CurrentState.ActivePlayerId = PlayerColors.NONE;
        m_CurrentState.GameState = GameStates.None;
        m_CurrentState.MoveNo = 0;
        /// @todo CurrentState.CurrentSound = TBzmSound.None;
        setRulesType(Rules.Backgammon);
        m_CurrentState.BoardPosition = TBzmBoardPositionFactory.Create(getRules().getRulesType(), m_Reflected);
    }

    //Properties
    public PlayerColors getActivePlayerId() 
    {
        return m_CurrentState.ActivePlayerId; 
    }
    public void setActivePlayerId(PlayerColors player)
    {
        m_CurrentState.ActivePlayerId = player; 
    }
    
    public PlayerColors getPassivePlayerId() 
    {
            switch (m_CurrentState.ActivePlayerId) 
            {
                case BLACK:
                    return PlayerColors.WHITE;
                case WHITE:
                    return PlayerColors.BLACK;
                default:
                    return PlayerColors.NONE;
            }
    }
    public boolean isPaused() 
    {
        return m_CurrentState.Paused;        
    }
    
    public void setPaused(boolean paused)
    {
            m_CurrentState.Paused = paused;
            m_PlayerBlack.setPaused(paused);
            m_PlayerWhite.setPaused(paused);
    }
    public TBzmRateDie getRateDie() 
    {
        return m_RateDie; 
    }
    public GameStates getGameState() 
    {
        return m_CurrentState.GameState; 
    }
    public void setGameState(GameStates state)
    {
        SetState(state); 
    }
    public TBzmBoardPlayer getPlayerWhite() 
    {
        return m_PlayerWhite; 
    }
    public TBzmBoardPlayer getPlayerBlack()
    {
        return m_PlayerBlack; 
    }
    public boolean isReflected() 
    {
        return m_Reflected; 
    }
    public void setReflected(boolean reflected)
    {
    	m_Reflected = reflected;
    	m_CurrentState.BoardPosition.setReflect(reflected);
    }

    
    public byte getCurrentMoveNo() 
    {
        return m_CurrentState.MoveNo; 
    }
    public String getPositionString() 
    {
        return m_RateDie.getStateString() + m_CurrentState.BoardPosition.getStateString(); 
    }
    public void setPositionString(String value)
    {
            if (value.length() < 4) return;
            m_RateDie.setStateString(value.substring(0,4));
            m_CurrentState.BoardPosition.setStateString(value.substring(4));
    }
    public String getShortPositionString() 
    {
        String sRes = "";
    	/** @todo
            for (int i = 0; i < 26; i++) 
            {
                switch (CurrentState.BoardPosition[i, TBzmPlayerColor.Black].Color)
                {
                    case TBzmPlayerColor.Black:
                        sRes = sRes + (char)((byte)'A' + CurrentState.BoardPosition[i, TBzmPlayerColor.Black].Count);
                        break;
                    case TBzmPlayerColor.White:
                        sRes = sRes + (char)((byte)'a' + CurrentState.BoardPosition[i, TBzmPlayerColor.Black].Count);
                        break;
                    default :
                        sRes = sRes + 'A';
                        break;
                }
            }
            sRes = sRes + "AA";
            */
            return sRes;
    }
    public short getMaxSeconds() 
    {
        return m_MaxSeconds; 
    }
    
    public void setMaxSeconds(short maxSeconds) { 
            m_MaxSeconds = maxSeconds;
            m_PlayerBlack.setMaxSeconds(maxSeconds);
            m_PlayerWhite.setMaxSeconds(maxSeconds);
    }
    /**
    public TBzmSound CurrentSound 
    {
        get { return CurrentState.CurrentSound; }
        set { CurrentState.CurrentSound = value; }
    }*/
    public TBzmStateList getBoardStateList() 
    {
        return m_BoardStateList; 
    }
    public Rules getRulesType() 
    {
            return (m_Rules instanceof TBzmBackgammonRules) ? 
            		Rules.Backgammon : Rules.RussianBackgammon; 
    }
    public void setRulesType(Rules rulesType)
    {
        switch (rulesType) 
        {
            case Backgammon:
                m_Rules = new TBzmBackgammonRules();
                break;
            case RussianBackgammon:
                m_Rules = new TBzmLongBackgammonRules();
                break;
        }
        m_CurrentState.BoardPosition = TBzmBoardPositionFactory.Create(getRules().getRulesType(), m_Reflected);    	
    }
    public IBzmRules getRules() 
    {
        return m_Rules;
    }

    //Methods
    public int CheckersOff(PlayerColors aPlayerId) 
    {
        return m_CurrentState.BoardPosition.OffCheckersCount(aPlayerId);
    }
    public int CheckersOnBoard(PlayerColors aPlayerId)
    {
        return 15 - m_CurrentState.BoardPosition.OffCheckersCount(aPlayerId);
    }

    public int Checkers(int aPos) 
    {
        return m_CurrentState.BoardPosition.getCheckersPosition(aPos, PlayerColors.BLACK).Count;
    }
    public PlayerColors Colors(int aPos)
    {
        return m_CurrentState.BoardPosition.getCheckersPosition(aPos, PlayerColors.BLACK).Color;
    }
    public void SetBoardPosition(IBzmBoardPosition aPosition) 
    {
        aPosition.Clone(m_CurrentState.BoardPosition);
    }
    public void ReadBoardPosition() 
    {
        IBzmBoardPosition temp = TBzmBoardPositionFactory.Create(getRules().getRulesType(), m_Reflected);
        temp.ReadState(m_BoardStateList, 0);
        int cnt;
        for(int i = 0; i< 26; i++)
        {
            cnt = temp.getCheckersPosition(i,PlayerColors.WHITE).Count;
            if (cnt != m_CurrentState.BoardPosition.getCheckersPosition(i, PlayerColors.WHITE).Count)
            {
                m_CurrentState.BoardPosition.getCheckersPosition(i, PlayerColors.WHITE).Count = cnt;
                if (cnt !=0)
                    m_CurrentState.BoardPosition.getCheckersPosition(i, PlayerColors.WHITE).Color = m_CurrentState.ActivePlayerId;
                else
                    m_CurrentState.BoardPosition.getCheckersPosition(i, PlayerColors.WHITE).Color = PlayerColors.NONE;

                
            }
        }
        if (m_CurrentState.BoardPosition.getCheckersPosition(25, PlayerColors.WHITE).Count != 0)
            m_CurrentState.BoardPosition.getCheckersPosition(25, PlayerColors.WHITE).Color = PlayerColors.BLACK;
        if (m_CurrentState.BoardPosition.getCheckersPosition(0, PlayerColors.WHITE).Count != 0)
            m_CurrentState.BoardPosition.getCheckersPosition(0, PlayerColors.WHITE).Color = PlayerColors.WHITE;
    }
    public void Clone(TBzmBoard aDest) 
    {
        aDest.m_CurrentState.Paused = isPaused();
        aDest.m_Reflected = isReflected();
        m_RateDie.Clone(aDest.m_RateDie);
        m_CurrentState.BoardPosition.Clone(aDest.m_CurrentState.BoardPosition);
        m_PlayerWhite.Clone(aDest.m_PlayerWhite);
        m_PlayerBlack.Clone(aDest.m_PlayerBlack);
        aDest.m_CurrentState.ActivePlayerId = m_CurrentState.ActivePlayerId;
        aDest.m_CurrentState.GameState = m_CurrentState.GameState;
        /// @todo aDest.CurrentState.CurrentSound = CurrentState.CurrentSound;
        /** @todo if (aDest.OnBoardChange != null)
            aDest.OnBoardChange(); */
    }
    public void ChangeCurrentPlayer() 
    {
        setActivePlayerId((m_CurrentState.ActivePlayerId == PlayerColors.WHITE) ? PlayerColors.BLACK : PlayerColors.WHITE);
    }
    public TBzmBoardPlayer Player(boolean ReturnActivePlayer) 
    {
        if (ReturnActivePlayer)
            return (m_CurrentState.ActivePlayerId == PlayerColors.WHITE) ? m_PlayerWhite : m_PlayerBlack;
        else
            return (m_CurrentState.ActivePlayerId == PlayerColors.WHITE) ? m_PlayerBlack : m_PlayerWhite;
    }
    public boolean Equal(TBzmBoard aCopy) 
    {
      return
        (m_CurrentState.Paused == aCopy.m_CurrentState.Paused) &&
         m_RateDie.Equal(aCopy.m_RateDie) &&
         m_CurrentState.BoardPosition.Like(aCopy.m_CurrentState.BoardPosition) &&
        (m_CurrentState.GameState == aCopy.m_CurrentState.GameState) &&
        (m_CurrentState.ActivePlayerId == aCopy.m_CurrentState.ActivePlayerId) &&
         m_PlayerWhite.equals(aCopy.m_PlayerWhite) &&
         m_PlayerBlack.equals(aCopy.m_PlayerBlack);
    }
    public int Pips(PlayerColors aPlayerId) 
    {
        return m_CurrentState.BoardPosition.GetPips(aPlayerId);
    }
    public TBzmMove FindAndApplyNextMoveAndDice(boolean aStrongDice) 
    {
        IBzmBoardPosition after_prev_find_move_position = null;
        TBzmMove prev_find_move = null;
        byte aDie1, aDie2;
        for (aDie1 = 6; aDie1 >= 1; aDie1--) 
        {
            for (aDie2 = aDie1; aDie2 >= 1; aDie2--) 
            {
                TBzmMove temp_move = FindMove(aDie1, aDie2, m_CurrentState.ActivePlayerId, m_CurrentState.BoardPosition);
                if (temp_move != null) 
                {
                    temp_move.PositionBeforeMove = m_CurrentState.BoardPosition.Clone();
                    IBzmBoardPosition temp_position = m_CurrentState.BoardPosition.Clone();
                    temp_move.Applay(temp_position);
                    temp_move.PositionAfterMove = temp_position.Clone();
                    if (prev_find_move != null) // уже был найден один возможный ход
                    {
                        prev_find_move.HasAlternativeDice = true;
                        temp_move.HasAlternativeDice = true;
                        if (aStrongDice) // если требуется найти однозначные кубики и позиции получаются разные
                            return null;
                        else
                        {
                            if(!after_prev_find_move_position.Equal(temp_position)) // если получились разные позиции
                            {
                                return null;
                                /* Убрано 03/07/2008
                                if (Rules.CanOffChecker(temp_position, FActivePlayerId)) // Если начался выброс фишек
                                {
                                    // определить какая из позиций лучше, ту и оставить
                                    if (Better(temp_position, after_prev_find_move_position, FActivePlayerId))
                                        prev_find_move = temp_move;
                                }
                                else prev_find_move = temp_move;*/
                            }

                        }
                    }
                    else
                    {
                        after_prev_find_move_position = temp_position.Clone();
                        prev_find_move = temp_move;
                    }
                    
                }
            }
        }
        if (prev_find_move != null) 
        {
            prev_find_move.MoveNo = m_CurrentState.MoveNo;
            m_CurrentState.MoveNo++;
            m_CurrentState.PrevBoardPosition = m_CurrentState.BoardPosition.Clone();
            prev_find_move.Applay(m_CurrentState.BoardPosition);
        }
        return prev_find_move;
    }
    public static TBzmMove[] FindPossibleMoves(Rules aRulesType, String sBeginPosition, String sEndPosition, PlayerColors anActivePlayerId) 
    {
        IBzmRules rules;
        if(aRulesType == Rules.Backgammon)
            rules = new TBzmBackgammonRules();
        else
            rules = new TBzmLongBackgammonRules();

        IBzmBoardPosition beginPos = TBzmBoardPositionFactory.Create(aRulesType);
        IBzmBoardPosition endPos = TBzmBoardPositionFactory.Create(aRulesType);
        beginPos.setStateString(sBeginPosition);
        endPos.setStateString(sEndPosition);
        ArrayList<TBzmMove> moves = new ArrayList<TBzmMove>();
        
        byte aDie1, aDie2;
        for (aDie1 = 6; aDie1 >= 1; aDie1--)
            for (aDie2 = aDie1; aDie2 >= 1; aDie2--) 
            {
                TBzmMove move = rules.FindMove(aDie1, aDie2, anActivePlayerId, beginPos, endPos);
                if (move != null)
                    moves.add(move);
            }                
        return moves.toArray(new TBzmMove[0]);
    }

    private boolean Better(IBzmBoardPosition first_position, IBzmBoardPosition second_position, PlayerColors AnActivePlayerId)
    {
            // 1 - Проверить есть ли фишки соперника между фишками игрока
            // 2 - Если есть, проверить, что бы количество фишек после хода оставалось четным
            // 3 - Если нет, то максимизировать выброшенные фишки
            return first_position.OffCheckersCount(AnActivePlayerId) > second_position.OffCheckersCount(AnActivePlayerId);
    }

    public TBzmMove FindAndApplyNextMove() 
    {
        byte aDie1 = 0, aDie2 = 0;
        /** @todo
        if (Player(TBzmConst.ACTIVE).GetDice(out aDie1, out aDie2) != 2)
            return null;
            */
        TBzmMove move = FindMove(aDie1, aDie2, m_CurrentState.ActivePlayerId, m_CurrentState.BoardPosition);
        if (move != null)
        {
            move.MoveNo = m_CurrentState.MoveNo;
            m_CurrentState.MoveNo++;
            m_CurrentState.PrevBoardPosition = m_CurrentState.BoardPosition.Clone();
            move.PositionBeforeMove = m_CurrentState.BoardPosition.Clone();
            move.Applay(m_CurrentState.BoardPosition);
            move.PositionAfterMove = m_CurrentState.BoardPosition.Clone();
            return move;
        }
        else 
        {
            move = new TBzmMove(aDie1, aDie2, m_CurrentState.ActivePlayerId);
            if (m_Rules.StillCanDoMovement(move, m_CurrentState.BoardPosition))
            {
                return null;
            }
            else 
            {
                if (BoardPositionChanged())
                {
                    return null;
                }
                else 
                {
                    move.MoveNo = m_CurrentState.MoveNo;
                    m_CurrentState.MoveNo++;
                    m_CurrentState.PrevBoardPosition = m_CurrentState.BoardPosition.Clone();
                    return move;
                }
            }
        }
    }
    public boolean CheckBoardPositionConfirmity() 
    {
        IBzmBoardPosition temp = TBzmBoardPositionFactory.Create(getRules().getRulesType(), m_Reflected);
        temp.ReadState(m_BoardStateList, 0);
        boolean res = m_CurrentState.BoardPosition.Like(temp);
        if (!res)
        {
            setReflected(!isReflected());
            temp.setReflect(isReflected());
            temp.ReadState(m_BoardStateList, 0);
            res = m_CurrentState.BoardPosition.Like(temp);
            if (!res)
            {
                setReflected(!isReflected());
            }
        }
        return res;
      }
    public boolean IsGammon(PlayerColors forPlayerId) 
    {
        return m_CurrentState.BoardPosition.OffCheckersCount(forPlayerId) == 0;
    }
    public boolean IsBackgammon(PlayerColors forPlayerId) 
    {
        if (!IsGammon(forPlayerId))
            return false;
        for (int i = 0; i < 7; i++)
            if (m_CurrentState.BoardPosition.getCheckersPosition(i, forPlayerId).Count != 0)
                return true;
        return false;
    }
    
    public void ResetMove() 
    {
        if (m_CurrentState.PrevBoardPosition != null) 
        {
            m_CurrentState.BoardPosition = m_CurrentState.PrevBoardPosition;
            m_CurrentState.PrevBoardPosition = null;
            m_CurrentState.MoveNo--;
        }
    }
    //Events
    /// @todo public event TBzmEvent OnBoardChange;
    /// <summary>
    /// Возвращает true, если игрок не может вводить фишки с бара
    /// </summary>
    /// <returns></returns>
    boolean CannotMovingFromBar()
    {
        if (m_CurrentState.BoardPosition.getCheckersPosition(0, getActivePlayerId()).Count == 0) // фишек на баре нет
            return false;
        for (int i = 19; i <= 24; i++) 
        {
            if (m_CurrentState.BoardPosition.getCheckersPosition(i, getPassivePlayerId()).Color != getPassivePlayerId() 
            		|| m_CurrentState.BoardPosition.getCheckersPosition(i, getPassivePlayerId()).Count < 2)
                return false;
        }
        return true;
    }

}
