package com.fairbg.core;

import com.fairbg.core.commands.Command;
import com.fairbg.core.commands.CommandPosition;
import com.fairbg.core.commands.ICommandListener;
import com.fairbg.core.commands.ICommander;
import sun.security.util.Debug;

/**
 * Основной класс программы 
 * Отвечает за взаимодействие всех объектов программы
 * @author Vitalik
 */
public class Corel implements ICommandListener {

    private Board _board = new Board();
    /** текущий матч */
    private Match _match = new Match();
    private IBoardDisplay _display = null;
    private ICommander _commander = null;
    private Automat _automat = new Automat();
    private IGameRules _rules = new BackgammonRules();

    public Corel() {
        _board.setMatch(_match);
        _board.setCurrentPosition(_rules.getStartPosition());
    }

    /** Устанавливает окно для отображения матча
     * @param window окно отображения матча
     */
    public void setBoardDisplay(IBoardDisplay display) {
        _display = display;
        _display.addListener(this);
        _display.setBoardObject(_board);
    }

    /**
     * Устанавливает связь с объектом управления устройством доски
     * @param commander 
     */
    public void setCommander(ICommander commander) {
        _commander = commander;
        if (_commander instanceof ICommander) {
            _commander.addListener(this);
        }
    }

    @Override
    public void onCommand(ICommander commander, Command command) {
        if (command instanceof CommandPosition) {
            processCommand(command);
            //_board.setCurrentPosition(new_position);
            _display.updateDisplay();
        }
    }

    /**
     * Стартует цикл опроса в объекте управления устройством доски
     */
    public void startListenCommander() {
        if (_commander instanceof ICommander) {
            _commander.start();
        }
    }

    /**
     * Останавливает цикл опроса в объекте управления устройством доски
     */
    public void stopListenCommander() {
        if (_commander instanceof ICommander) {
            _commander.stop();
        }
    }

    /** 
     * Получает команду, обрабатывает ее и изменяет текущее состояние автомата
     * @param command полученная команда
     */
    void processCommand(Command command) {
        Debug.println("PROCESS COMMAND", command.toString());
        Position cur_pos = _board.getCurrentPosition();        
        if (command instanceof CommandPosition && _automat.allowPlayerCommand(command.player)) {
            CommandPosition cmd = (CommandPosition) command;
            Debug.println("PROCESS COMMAND POSITION", cmd.toString());
            switch (_automat.currentState) {
                case Constants.STATE_BEGIN:
                    if (cur_pos.equal_checkers(cmd.getPosition())) {
                        _automat.currentState = Constants.STATE_DICE;
                        //Toolkit.getDefaultToolkit().beep();
                    }

                    break;
                case Constants.STATE_DICE:
                    // если положение кубика стоимости не изменилось, то просто вычисляем ход
                    // если положение кубика стоимости менялось, то вычисляем double-take/pass
                    // если поменялось и то и другое, то вычисляем double-take и ход
                    Move[] moves = _rules.findMove(_board, cmd.getPosition(), cmd.player);
                    if (moves != null) {
                        _match.addMoves(moves);
                        _board.apply_moves(moves);
                        _display.updateDisplay();
                    }
                    /*if(new_pos != null)
                    cur_pos = new_pos;*/
                    break;
                case Constants.STATE_ACCEPT_MOVE:
                    break;
                case Constants.STATE_ACCEPT_DOUBLE:
                    break;
            }
        } else {
            return;
        }
    }
}
