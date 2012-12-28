package com.fairbg.bezma.core;

import com.fairbg.bezma.communication.ICommunicator;
import com.fairbg.bezma.communication.IModelView;
import com.fairbg.bezma.communication.commands.ICommandObserver;
import com.fairbg.bezma.communication.commands.CommunicationCommand;
import com.fairbg.bezma.communication.commands.CommunicationCommandRequest;
import com.fairbg.bezma.core.model.Model;
import com.fairbg.bezma.core.model.ModelCommand;
import com.fairbg.bezma.store.IDatabase;

public class Presenter implements ICommandObserver
{

    private ICommunicator m_Communicator = null;

    private IDatabase m_Storage = null;

    private RequestViewLoop m_RequestLoop = null;

    private Model m_Model = new Model();

    public Presenter(IConfigurator configurator, Configuration configuration)
    {
        try
        {
            m_Communicator = configurator.createCommunicator(configuration);
            m_Communicator.removeObserver(this);
            m_Communicator.addObserver(this);

            m_RequestLoop = new RequestViewLoop();

            m_Storage = configurator.createDatabase(configuration);

            m_Model.create(configuration.getMatchParameters(), m_Storage);
        }
        catch (WrongConfigurationException e)
        {
            e.printStackTrace();
        }
    }

    public void addView(IModelView view)
    {
        m_Communicator.addView(view);
    }

    public void removeView(IModelView view)
    {
        m_Communicator.removeView(view);
    }

    public boolean start()
    {

        boolean result = m_Communicator.start();

        /*
         * if (result) { m_RequestLoop.start(); }
         */
        return result;
    }

    public void stop()
    {

        m_RequestLoop.stopLoop();

        m_Communicator.stop();
    }

    @Override
    public void handeEvent(CommunicationCommand aCommand)
    {
        processCommand(aCommand);
        displayResults();
    }

    private void displayResults()
    {
        if (m_Communicator != null)
        {
            m_Communicator.setModelState(m_Model.getState());
        }
    }

    private void processCommand(CommunicationCommand command)
    {

        // TODO Преобразовать команду коммуникационную в команду
        // пользовательскую
        ModelCommand modelCommand = ModelCommand.createCommand(command);

        m_Model.processCommand(modelCommand);
        // m_state.processCommand(userCommand);
    }

    @Override
    public String toString()
    {
        return "ModelPresenter";
    }

    private class RequestViewLoop extends Thread
    {

        private boolean stopped = true;

        @Override
        public void run()
        {

            stopped = false;

            CommunicationCommandRequest command = new CommunicationCommandRequest();

            while (!stopped)
            {
                m_Communicator.sendCommand(command);

                try
                {
                    sleep(2000);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }

        public void stopLoop()
        {
            stopped = true;
        }
    }
}
