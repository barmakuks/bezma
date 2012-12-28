package com.fairbg.bezma.core.backgammon;

import com.fairbg.bezma.core.backgammon.IAutomatState.AutomatStates;
import com.fairbg.bezma.core.model.IGameAutomat;
import com.fairbg.bezma.core.model.IGameBox;
import com.fairbg.bezma.core.model.ModelCommand;
import com.fairbg.bezma.core.model.PlayerColors;

public class BackgammonAutomat implements IBackgammonAutomat, IGameAutomat
{
    private BackgammonRules m_Rules = new BackgammonRules();
    private IGameBox m_GameBox;
    
    private Position m_LastPosition;
    private PlayerColors m_CurrentPlayer;

    public BackgammonAutomat(IGameBox gameBox)
    {
        m_GameBox = gameBox;
    }
    
    @Override
    public boolean isStartPosition(Position position)
    {
        return m_Rules.checkStartPosition(position);
    }

    @Override
    public void nextGame()
    {
        m_GameBox.nextGame();
    }

    @Override
    public boolean canDouble(Position position)
    {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public void proposeDouble()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isDoubleAccepted(Position position)
    {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public void acceptDouble()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isGameFinished()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void finishGame()
    {
        // TODO Auto-generated method stub
        m_GameBox.finishGame();
    }

    @Override
    public boolean isMatchFinished()
    {
        // TODO Auto-generated method stub
        return m_GameBox.isMatchFinished();
    }

    @Override
    public void finishMatch()
    {
        m_GameBox.finishMatch();
    }

    @Override
    public boolean findAndAcceptMove(Position position)
    {
        int die1 = 0;
        int die2 = 0;
        m_Rules.findMove(die1, die2, m_LastPosition, position, m_CurrentPlayer);
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public IAutomatState getAutomatState()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean setAutomatState(AutomatStates state)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isCurrentPlayer(PlayerColors player)
    {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean processCommand(IGameBox gameBox, ModelCommand modelCommand)
    {
        // TODO Auto-generated method stub
        return false;
    }

}
