package com.fairbg.core;

import com.fairbg.core.commands.Command;
import com.fairbg.core.commands.CommandPosition;
import java.awt.Toolkit;
import sun.security.util.Debug;

/** Текущее состояние доски, 
 * включает положение фишек и кубика стоимости, 
 * параметры матча, 
 * счет,
 * список партий и ходов в них
 * на основании текущей позиции и состояния автомата вычисляет ход и следующее состояние автомата
 * */
public class Board {

    /** текущая позиция на доске*/
    private Position _current_position = null;
    /** ссылка на текущий матч */
    private Match _match_ref = null;

    /**
     * Устанавливает текущую позицию фишек на доске
     * @param position новая позиция фишек на доске
     */
    public void setCurrentPosition(Position position) {
        _current_position = position;
    }

    /**
     * Возвращает текущую позицию фишек на доске
     * @return 
     */
    public Position getCurrentPosition() {
        return _current_position;
    }

    /**
     * Устанавливает связь с объектом match
     * @param match {@link Match} 
     */
    public void setMatch(Match match) {
        _match_ref = match;
    }

    /**
     * Возвращает ссылку на текущий матч
     * @return 
     */
    public Match getMatch() {
        return _match_ref;
    }

    public Position apply_moves(Move[] moves) {
        Position new_pos = (Position) _current_position.clone();        
        if(moves != null){
            
        }
        setCurrentPosition(new_pos);
        return new_pos;
    }
}
