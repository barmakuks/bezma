package com.fairbg.bezma.core.from_net;

import java.util.ArrayList;

import com.fairbg.bezma.core.from_net.enums.VictoryTypes;
import com.fairbg.bezma.core.from_net.enums.PlayerColors;

public class TBzmGame {
        private ArrayList<TBzmMoveBase> m_Moves = new ArrayList<TBzmMoveBase>();
        private String m_StartPosition;
        private String m_DiceString;
        //private TBzmPlayerColor FWinnerId;
        private byte m_RateLevel;
        private byte m_GameNo;
        private boolean m_IsOver;
        private boolean m_IsCrawford;
        //private int FWinnerSMP;
        private TBzmGameResult m_GameResult;
        
        public TBzmGameResult getGameResult()
        {
        	 return m_GameResult; 
        }

        private void Clear() 
        {
            m_DiceString = "";
            m_StartPosition = "";
            m_GameResult = null;
            //FWinnerId = TBzmPlayerColor.pclrNone;
            m_IsOver = false;
            m_RateLevel = 1;
            m_IsCrawford = false;
            m_GameNo = 0;
            m_Moves.clear();
        }

        //PROPERTIES
        public int getMoveCount() 
        {
            return m_Moves.size();
        }

        public byte getGameNo() 
        {
            return m_GameNo;
        }
        public String getStartPosition() 
        {
            return m_StartPosition;            
        }
        public String getDiceString() 
        {
            return m_DiceString;
        }
        public boolean isCrawford() 
        {
            return m_IsCrawford;
        }

        // PUBLIC METHODS
        public TBzmGame() 
        {
            Clear();
        }
        public void AddMove(TBzmMoveBase aMove)
        {
            m_Moves.add(aMove);
        }
        public TBzmMoveBase getMove(int index) 
        {
            return m_Moves.get(index);
        }
        public TBzmDice GetDice(int index) 
        {
        	/// @todo
            /*if (index < 0 || index * 2 >= FDiceString.length())
            return null;
        else
            return new TBzmDice(Convert.ToByte(FDiceString[index * 2] - '0'), Convert.ToByte(FDiceString[index * 2 + 1] - '0'));*/
        	return null;
        }
        public void AddDice(byte aDie1, byte aDie2)
        {
            m_DiceString = m_DiceString + aDie1 + aDie2;
        }
        public boolean CancelMove() 
        {
            if (m_Moves.size() > 0)
            {
                m_Moves.remove(m_Moves.size() - 1);
                return true;
            }
            else return false;
        }
        public void NewGame(byte aGameNo, String aStartPosition, boolean isCrawford) 
        {
            Clear();
            m_GameNo = aGameNo;
            m_StartPosition = aStartPosition;
            m_IsOver = false;
            m_IsCrawford = isCrawford;
        }
        public void EndGame(PlayerColors aWinnerId, byte aRateLevel, int CheckersLeft, boolean IsRatePass, VictoryTypes WinType) 
        {
            m_RateLevel = aRateLevel;
            switch (WinType) 
            {
                case Gammon:
                    m_RateLevel *= 2; break;
                case Backgammon:
                    m_RateLevel *= 3; break;
            }
            if (IsRatePass) WinType = VictoryTypes.DropDouble;

            m_GameResult = new TBzmGameResult(aWinnerId, CheckersLeft, aRateLevel, WinType);

            m_IsOver = true;
        }

        @Override public String toString() 
        {
        	/** @todo
            if (FGameResult == null)
            {
                return String.Format("game #{0}", FGameNo);
            }
            else 
            {
                return String.Format("game #{0} {1} wins {2}",
                    FGameNo, TBzmPlayerColorUtils.GetPlayerColorString(FGameResult.Winner), FGameResult.WinnerPoints);
            }
            */
        	return "";
        }
        public String ToSgfString(String aMatchHead) 
        {
        	/**@todo
        	
            if (isCrawford())
                aMatchHead = aMatchHead + "RU[Crawford:CrawfordGame]";
            String res = String.Format("({0}[dice:{1}]\n", aMatchHead, FDiceString);
            //res = res + Sgf.TSgfUtils.PositionToSgfString(StartPosition);
            for (int i = 0; i < moves.Count; i++) 
            {
                foreach(string sgf  in (moves[i] as TBzmMoveBase).ToSgfString())
                {
                    res = res + sgf + '\n';
                }
                
            }
            res = res + ")";
            return res;
            */
        	return "";
        }
        public String MoveString(int index) 
        {
            if (index == 0)
                return "Start position";
            else 
                return m_Moves.get(index - 1).toString();
        }
        public String PositionString(int index) 
        {
            if (index == 0)
                return m_StartPosition;
            else
                return m_Moves.get(index - 1).getPositionString();
        }

}
