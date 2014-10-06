package com.fairbg.bezma.unit_tests;

import java.util.ArrayList;

import com.fairbg.bezma.communication.IModelView;
import com.fairbg.bezma.communication.commands.CommunicationCommand;
import com.fairbg.bezma.communication.commands.ICommandObserver;
import com.fairbg.bezma.core.MatchParameters;
import com.fairbg.bezma.core.backgammon.MovePrinter;
import com.fairbg.bezma.core.errors.Error;
import com.fairbg.bezma.core.model.MatchScore;
import com.fairbg.bezma.core.model.MoveAbstract;
import com.fairbg.bezma.core.model.BoardContext;
import com.fairbg.bezma.core.model.PlayerId;

public class TestModelOut implements IModelView
{
    /** Datagram listeners list */
    private ArrayList<ICommandObserver> m_Observers = new ArrayList<ICommandObserver>();
    private MovePrinter movePrinter = null;

    public TestModelOut(MatchParameters params)
    {
        movePrinter = new MovePrinter(params);
    }

    @Override
    public void notifyObservers(CommunicationCommand userCommand)
    {
        for (ICommandObserver observer : m_Observers)
        {
            observer.handeEvent(userCommand);
        }
    }

    @Override
    public void addObserver(ICommandObserver aCommandObserver)
    {
        m_Observers.add(aCommandObserver);
    }

    @Override
    public void removeObserver(ICommandObserver aCommandObserver)
    {
        m_Observers.remove(aCommandObserver);
    }

    @Override
    public boolean start()
    {
        return true;
    }

    @Override
    public void stop()
    {
        // TODO Auto-generated method stub
    }

    @Override
    public void sendCommand(CommunicationCommand command)
    {
//        System.out.println(command.toString());
    }

    @Override
    public void setModelState(BoardContext aModelState)
    {
        if (aModelState != null)
        {
            System.out.println(aModelState.getPosition());
        }
        else
        {
            System.out.println("empty state");
        }
    }

    @Override
    public void appendMove(MoveAbstract move)
    {
        System.out.println("----------------------------------------------");
        System.out.println("Move accepted: " + movePrinter.printMove(move));
    }

    @Override
    public void displayError(Error error)
    {
        System.out.println("Error received");
    }

    @Override
    public void changeScore(MatchScore score)
    {
        System.out.println("Change score. Black [" + score.getPlayerScore(PlayerId.RED) + " : " + score.getPlayerScore(PlayerId.SILVER) + "] White");
    }

}