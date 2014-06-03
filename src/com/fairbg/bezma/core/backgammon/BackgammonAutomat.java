package com.fairbg.bezma.core.backgammon;

import com.fairbg.bezma.core.backgammon.IAutomatState.AutomatStates;
import com.fairbg.bezma.core.backgammon.Position.CubePosition;
import com.fairbg.bezma.core.backgammon.Position.Direction;
import com.fairbg.bezma.core.model.IGameController;
import com.fairbg.bezma.core.model.MoveAbstract;
import com.fairbg.bezma.core.model.ModelCommand;
import com.fairbg.bezma.core.model.PlayerId;
import com.fairbg.bezma.log.BezmaLog;

interface IBackgammonAutomat extends IGameWithCube
{
    public Position.Direction getStartPositionDirection(Position position, boolean withCube);

    public void startGame(Position.Direction direction, boolean withCube);

    public boolean isGameFinished();

    public void finishGame();

    public boolean findAndAcceptMove(Position position);

    public boolean processCube(Position position);

    public IAutomatState getAutomatState();

    public boolean setAutomatState(IAutomatState.AutomatStates state);

    public boolean isCurrentPlayer(PlayerId playerId);
}

interface IAutomatState
{
    public static enum AutomatStates
    {
        START, MOVE, DOUBLE, END
    }

    boolean processCommand(IBackgammonAutomat gameBox, ModelCommand command);
}

class AutomatStateDouble implements IAutomatState
{

    @Override
    public boolean processCommand(IBackgammonAutomat gameAutomat, ModelCommand command)
    {
        if (!gameAutomat.isCurrentPlayer(command.player))
        {
            return false;
        }

        // if (gameAutomat.isDoubleAccepted(command.getPosition()))
        {
            gameAutomat.take(command.player);
            gameAutomat.setAutomatState(AutomatStates.MOVE);

            return true;
        }

        // return false;
    }

}

class AutomatStateEnd implements IAutomatState
{

    @Override
    public boolean processCommand(IBackgammonAutomat gameAutomat, ModelCommand modelCommand)
    {
        // TODO Auto-generated method stub
        return false;
    }

}

class AutomatStateMove implements IAutomatState
{
    @Override
    public boolean processCommand(IBackgammonAutomat gameAutomat, ModelCommand command)
    {
        Position position = command.getPosition();

        boolean result = gameAutomat.processCube(position);
        if (!result) // Cube position is changed?
        {
            // Cube position is not changed
            result = gameAutomat.findAndAcceptMove(position);
            
            if (result)
            {
                if (gameAutomat.isGameFinished())
                {
                    gameAutomat.finishGame();
                }
                else
                {
                    gameAutomat.setAutomatState(AutomatStates.MOVE);
                }
            }

            /* Move move = gameBox.findMove(command.getPosition(), command.player);
             * if (move != null)
             * {
             * // Change current state to MOVE
             * gameBox.appendMove(move);
             * IAutomatState state = gameBox.selectAutomatState(AutomatStates.MOVE);
             * gameBox.setCurrentAutomatState(state);
             * currentPosition.applyMove(move);
             * ModelSituation modelState = new ModelSituation(currentPosition, "");
             * gameBox.setModelState(modelState);
             * return true;
             * } */

        }
        return result;
    }

}

class AutomatStateStart implements IAutomatState
{
    public AutomatStateStart(boolean useCube)
    {
        m_gameWithCube = useCube;
    }
    
    @Override
    public boolean processCommand(IBackgammonAutomat gameAutomat, ModelCommand command)
    {
        // Check if this is start position
        
        // TODO calculate if cube is needed in this game
        Position.Direction direction = gameAutomat.getStartPositionDirection(command.getPosition(), m_gameWithCube);

        if (direction != Position.Direction.None)
        {
            gameAutomat.startGame(direction, m_gameWithCube);
            // Change current state to MOVE
            gameAutomat.setAutomatState(AutomatStates.MOVE);

            return true;
        }

        return false;
    }
    
    private boolean m_gameWithCube;
}

public class BackgammonAutomat implements IBackgammonAutomat, IGameAutomat, IGameWithCube
{
    private Position.Direction m_StartPositionDirection = Position.Direction.None;
    private Position.Direction DefaultDirection         = Position.Direction.RedCCW;

    private BackgammonRules    m_Rules                  = new BackgammonRules();
    private IGameController    m_GameController;

    private Position           m_LastPosition;
    private PlayerId           m_CurrentPlayer;

    private CubeAutomat        m_CubeAutomat;
    private int                m_cubeValue;

    IAutomatState              m_CurrentState;

    public BackgammonAutomat(IGameController gameController)
    {
        m_GameController = gameController;
        m_CubeAutomat = new CubeAutomat(this);

        setAutomatState(AutomatStates.START);
    }

    @Override
    public Position.Direction getStartPositionDirection(Position position, boolean withCube)
    {
        return m_Rules.getStartPositionDirection(position, withCube);
    }

    @Override
    public void startGame(Position.Direction direction, boolean withCube)
    {
        BezmaLog.i("BEZMA", "startGame " + (withCube ? "with" : "without") + " cube");

        m_LastPosition = new Position();
        m_LastPosition.setCheckers(BackgammonRules.getStartPosition(Direction.RedCCW));
        m_cubeValue = 1;

        if (withCube)
        {
            m_LastPosition.setCubePosition(CubePosition.Center);
        }
        else
        {
            m_LastPosition.setCubePosition(CubePosition.None);
        }

        m_StartPositionDirection = direction;
        m_GameController.startGame();
    }

    boolean canDouble(Position position)
    {
        return false;
    }

    void proposeDouble()
    {
        m_cubeValue = m_cubeValue * 2;
        MoveAbstract move = new MoveCubeDouble(m_CurrentPlayer, m_cubeValue);
        m_GameController.appendMove(move);
    }

    boolean isDoubleAccepted(Position position)
    {
        // TODO Auto-generated method stub
        return true;
    }

    void acceptDouble()
    {
        MoveAbstract move = new MoveCubeTake(m_CurrentPlayer, m_cubeValue);
        m_GameController.appendMove(move);
    }

    @Override
    public boolean isGameFinished()
    {
        // TODO Implement if continuing game become meaningless
        return m_LastPosition.getPips(PlayerId.BLACK) == 0 || m_LastPosition.getPips(PlayerId.WHITE) == 0;
    }

    @Override
    public void finishGame()
    {
        m_GameController.finishGame(m_CurrentPlayer, m_cubeValue);
        m_CurrentPlayer = null;

        setAutomatState(AutomatStates.START);
    }

    @Override
    public boolean processCube(Position position)
    {
        boolean result = false;
        if (m_LastPosition.getCubePosition() != position.getCubePosition())
        {            
            result = m_CubeAutomat.processNextState(position.getCubePosition());
            
            if (result)
            {
                // TODO Cube value should have right value
                m_LastPosition.setCubeValue(m_cubeValue);
            }
        }

        return result;
    }

    @Override
    public boolean findAndAcceptMove(Position position)
    {
        BezmaLog.i("BEZMA", "findAndAcceptMove");
        int die1 = 0;
        int die2 = 0;

        Move move = null;
        if (m_CurrentPlayer == null || m_CurrentPlayer == PlayerId.NONE)
        {
            m_CurrentPlayer = PlayerId.BLACK;

            move = m_Rules.findMove(die1, die2, m_LastPosition, position, m_CurrentPlayer);

            if (move == null)
            {
                m_CurrentPlayer = PlayerId.WHITE;
                move = m_Rules.findMove(die1, die2, m_LastPosition, position, PlayerId.WHITE);
            }

            if (move == null)
            {
                m_CurrentPlayer = PlayerId.NONE;
            }
        }
        else
        {
            move = m_Rules.findMove(die1, die2, m_LastPosition, position, m_CurrentPlayer);
        }

        if (move != null)
        {
            BezmaLog.i("BEZMA", "found move");
            m_LastPosition.applyMove(move);
            m_GameController.appendMove(move);

            if (!isGameFinished())
            {
                m_CurrentPlayer = PlayerId.getOppositeId(m_CurrentPlayer);
            }
            BezmaLog.i("BEZMA", "move accepted");
            return true;
        }
        else
        {
            BezmaLog.i("BEZMA", "move not found");
        }
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public IAutomatState getAutomatState()
    {
        return m_CurrentState;
    }

    @Override
    public boolean setAutomatState(AutomatStates state)
    {
        BezmaLog.i("BEZMA", "set Automat State: " + state.toString());
        switch (state)
        {
        case DOUBLE:
            m_CurrentState = new AutomatStateDouble();
            break;
        case MOVE:
            m_CurrentState = new AutomatStateMove();
            break;
        case START:
            m_CurrentState = new AutomatStateStart(m_GameController.cubeInGame());
            break;
        case END:
            m_CurrentState = new AutomatStateEnd();
            break;
        }

        return true;
    }

    @Override
    public boolean isCurrentPlayer(PlayerId player)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean processCommand(IGameController gameBox, ModelCommand modelCommand)
    {
        boolean res = false;

        if (m_CurrentState != null)
        {
            ModelCommand normalizedCommand = NormalizePosition(modelCommand);
            BezmaLog.i("BEZMA", "processCommand in BackgammonAutomat" + modelCommand.getPosition().toString());
            res = m_CurrentState.processCommand(this, normalizedCommand);
        }

        return res;
    }

    /** Returns Model Command's position in Black Counterclockwise direction */
    private ModelCommand NormalizePosition(ModelCommand modelCommand)
    {
        ModelCommand normalizedCommand = modelCommand;

        try
        {
            normalizedCommand = (ModelCommand) modelCommand.clone();
            normalizedCommand.getPosition().ChangeDirection(m_StartPositionDirection, DefaultDirection);
        } catch (CloneNotSupportedException e)
        {
            e.printStackTrace();
        }

        return normalizedCommand;
    }

    @Override
    public Position getCurrentPosition()
    {
        Position position = null;
        try
        {
            position = (Position)m_LastPosition.clone();
            position.ChangeDirection(DefaultDirection, m_StartPositionDirection);
        } 
        catch (CloneNotSupportedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return position;
    }

    // IGameWithCube functions
    @Override
    public boolean canProposeDouble(PlayerId player)
    {
        return m_LastPosition != null && m_LastPosition.getCubePosition() == CubePosition.Center && m_LastPosition.getCubeValue() != 64;
    }

    @Override
    public boolean proposeDouble(PlayerId player)
    {
        boolean result = false;

        if (player == this.getNorthPlayer())
        {
            m_LastPosition.setCubePosition(CubePosition.West);
            result = true;
        }
        else if (player == this.getSouthPlayer())
        {
            m_LastPosition.setCubePosition(CubePosition.East);
            result = true;
        }

        if (result)
        {
            m_CurrentPlayer = PlayerId.getOppositeId(player);
            m_cubeValue = m_cubeValue * 2;
            m_LastPosition.setCubeValue(m_cubeValue);
            MoveCubeDouble move = new MoveCubeDouble(m_CurrentPlayer, m_cubeValue);
            move.setPlayer(player);
            m_GameController.appendMove(move);
        }

        return result;
    }

    @Override
    public boolean take(PlayerId player)
    {
        boolean result = false;
        if (player == this.getNorthPlayer())
        {
            
            m_LastPosition.setCubePosition(CubePosition.North);
            result = true;
        }
        else if (player == this.getSouthPlayer())
        {
            m_LastPosition.setCubePosition(CubePosition.South);
            result = true;
        }

        if (result)
        {
            m_CurrentPlayer = PlayerId.getOppositeId(player);
            MoveCubeTake move = new MoveCubeTake(m_CurrentPlayer, m_cubeValue);
            move.setPlayer(player);
            m_GameController.appendMove(move);
        }

        return result;
    }

    @Override
    public boolean pass(PlayerId player)
    {
        m_cubeValue = m_cubeValue / 2;
        MoveCubePass move = new MoveCubePass(player, m_cubeValue);
        move.setPlayer(player);
        m_GameController.appendMove(move);
        m_CurrentPlayer = PlayerId.getOppositeId(player);

        finishGame();

        return true;
    }

    @Override
    public PlayerId getCurrentPlayer()
    {
        return m_CurrentPlayer;
    }

    @Override
    public int getCubeValue()
    {
        return m_cubeValue;
    }

    @Override
    public PlayerId getSouthPlayer()
    {
        if (m_StartPositionDirection == Direction.RedCW || m_StartPositionDirection == Direction.RedCCW)
        {
            return PlayerId.WHITE;
        }
        else
        {
            return PlayerId.BLACK;
        }
    }

    @Override
    public PlayerId getNorthPlayer()
    {
        return PlayerId.getOppositeId(getSouthPlayer());
    }

}
