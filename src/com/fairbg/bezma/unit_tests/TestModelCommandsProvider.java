package com.fairbg.bezma.unit_tests;

import java.util.ArrayList;

import com.fairbg.bezma.bluetooth.StateDatagram;
import com.fairbg.bezma.communication.IModelView;
import com.fairbg.bezma.communication.commands.CommunicationCommand;
import com.fairbg.bezma.communication.commands.CommunicationCommandState;
import com.fairbg.bezma.communication.commands.ICommandObserver;
import com.fairbg.bezma.core.errors.Error;
import com.fairbg.bezma.core.model.MatchScore;
import com.fairbg.bezma.core.model.MoveAbstract;
import com.fairbg.bezma.core.model.BoardContext;

public class TestModelCommandsProvider implements IModelView
{
    private long m_delay;
    private boolean m_observersChanged = false;
    short[][]    m_datagrams;

    public TestModelCommandsProvider(short[][] datagrams, long delay)
    {
        m_delay = delay;
        m_datagrams = datagrams;
    }

    /** Datagram listeners list */
    private ArrayList<ICommandObserver> m_Observers = new ArrayList<ICommandObserver>();

    @Override
    public void notifyObservers(CommunicationCommand userCommand)
    {
        m_observersChanged = false;
        for (ICommandObserver observer : m_Observers)
        {
            observer.handeEvent(userCommand);
            if (m_observersChanged)
            {
                return;
            }
        }
    }

    @Override
    public void addObserver(ICommandObserver aCommandObserver)
    {
        m_observersChanged = true;
        m_Observers.add(aCommandObserver);
    }

    @Override
    public void removeObserver(ICommandObserver aCommandObserver)
    {
        m_observersChanged = true;
        m_Observers.remove(aCommandObserver);
    }


    private static StateDatagram.CubeState getCubeState(int value)
    {
        if (value == 0)
        {
            return StateDatagram.CubeState.Center;
        }
        if (value < 0)
        {
            return StateDatagram.CubeState.South;
        }
        if (value > 0)
        {
            return StateDatagram.CubeState.North;
        }

        return StateDatagram.CubeState.NoCube;
    }

    @Override
    public boolean start()
    {
        Thread thread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                for (int i = 0; i < m_datagrams.length; i++)
                {
                    CommunicationCommandState command = new CommunicationCommandState();
                    command.checkers = m_datagrams[i];
                    command.cubePosition = getCubeState(m_datagrams[i][m_datagrams[i].length - 1]);
                    command.playerId = -1;
                    notifyObservers(command);

                    try
                    {
                        Thread.sleep(m_delay);
                    } catch (InterruptedException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });

        thread.start();
        return true;
    }

    @Override
    public void stop()
    {
    }

    @Override
    public void sendCommand(CommunicationCommand command)
    {
    }

    @Override
    public void setModelState(BoardContext aModelState)
    {
    }

    @Override
    public void appendMove(MoveAbstract move)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void displayError(Error error)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void changeScore(MatchScore score)
    {
        // TODO Auto-generated method stub
        
    }

}
