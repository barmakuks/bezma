package com.fairbg.core;

/** Описывает положение фишек и куба стоимости на доске */
public class Position {

    private int[] _checkers = new int[28]; // 0 - бар черных; 1-24 - позиции фишек; 25 - бар белых; 26,27 - выброс
    private int[] _colors = new int[28]; // 0 - бар черных; 1-24 - позиции фишек; 25 - бар белых; 26,27 - выброс
    private int _cube_value = 1; // текущее значение кубика стоимости
    private int _cube_position = Constants.CUBE_CENTER; // текущее положение кубика стоимости на столе

    /**
     * Возвращает массив содержащий количество фишек на доске если
     * 1 по 24 - количество фишек на игровых полях позиция 0 - бар черных фишек
     * позиция 25 - бар белых фишек 26 - зона выброса белых фишек 27 - зона
     * выброса черных фишек
     * */
    public int[] getCheckers() {
        return _checkers;
    }

    /**
     * Возвращает массив содержащий цвет фишек на доске если
     * 1 по 24 - количество фишек на игровых полях позиция 0 - бар черных фишек
     * позиция 25 - бар белых фишек 26 - зона выброса белых фишек 27 - зона
     * выброса черных фишек
     * */
    public int[] getColors() {
        return _colors;
    }

    /**
     * Устанавливает количество фишек в позиции
     * @param index позиция 0 по 23 - количество фишек на игровых полях позиция 24 - бар белых фишек
     * позиция 25 - бар черных фишек 26 - зона выброса белых фишек 27 - зона  выброса черных фишек
     * @param checkers_count, количество фишек
     * @param цвет фишек
     */
    public void setChecker(int index, int checkers_count, int color) {
        if (index >= 0 && index < _checkers.length) {
            _checkers[index] = checkers_count;
            _colors[index] = (color < 0) ? Constants.PLAYER_BLACK : color == 0 ? Constants.PLAYER_NONE : Constants.PLAYER_WHITE;
        }
    }

    /** Воззвращает цвет фишки на позиции     
     * @param index - индекс позиции в пределах от 0 до 27
     * @param direction - направлени нумерации на доске, для BLACK и NONE - прямая, для WHITE - обратная 
     * @return PLAYER_NONE - если цвет не определен или позиция пустая <br/>
     * PLAYER_WHITE - для фишек белого цвета<br/>
     * PLAYER_BLACK - для фишек черного цвета
     */
    public int getColor(int index, int direction) {
        if (index >= 0 && index <= 25) {
            return _colors[(direction == Constants.PLAYER_WHITE) ? 25 - index : index];
        }
        return Constants.PLAYER_NONE;
    }

    public int getCheckerCount(int index, int direction) {
        if (index >= 0 && index <= 25) {
            return _checkers[(direction == Constants.PLAYER_WHITE) ? 25 - index : index];
        } else {
            if (index == 26 || index == 27) {
                return _checkers[index];
            }
        }
        return -1;
    }

    /** Возвращает текущее значение кубика стоимости */
    public int getCubeValue() {
        return _cube_value;
    }

    /** Устанавливает текущее значение кубика стоимости 
     * @param new_value, новое значение из диапазона [1,2,4,8,16,32,64]
     */
    public void setCubeValue(int new_value) {
        _cube_value = new_value;
    }

    /** Возвращает текущее положение кубика стоимости на доске */
    public int getCubePosition() {
        return _cube_position;
    }

    /** Устанавливает положение кубика стоимости на доске
     * @param new_position новое положение кубика, может принимать одно из значений:
     * <DIV>CUBE_CRAWFORD - кубик отсутствует;<BR/> 
     * CUBE_CENTER - Кубик стоимости в центре доски;<BR/>
     * CUBE_WHITE - Кубик стоимости у белого игрока, в нижней части доски;<BR/>
     * CUBE_BLACK - Кубик стоимости у черного игрока, в верхней части доски;<BR/>
     * CUBE_RIGHT - Кубик стоимости предложен черному игроку и находится на правой стороне доски;<BR/>
     * CUBE_LEFT - Кубик стоимости предложен белому игроку и находится на левой стороне доски
     * <DIV>
     */
    public void setCubePosition(int new_position) {
        _cube_position = new_position;
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < _checkers.length; i++) {
            res.append(_checkers[i]);
            res.append(',');
        }
        return res.toString();
    }

    /** Сравнивает две позиции без учета кубика стоимости. 
     * При сравнении двух позиций с определенными цветами сравнивается полное соответсвие количесва фишек на позициях.
     * При сравнении позиций с неопределенными цветами сравнивается количесво в пределах 5-ти фишек (позиции с 5-ю и 
     * больше фишками объявляются идентичными).
     * @param other позиция для сравнения
     * @return true, если позиции идентичные
     */
    public boolean equal_checkers(Position other) {
        if (other == null) {
            return false;
        }
        for (int i = 0; i < _checkers.length; i++) {
            int chA = _checkers[i];
            int chB = other._checkers[i];
            if (_colors[i] == Constants.PLAYER_NONE || other._colors[i] == Constants.PLAYER_NONE) { // если цвета не определены
                chA = Math.abs(chA);
                chB = Math.abs(chB);
                if (!((chA >= 5 && chB >= 5) || (chA == chB))) {
                    return false;
                }
            } else { // если все цвета определены
                if (chA != chB || _colors[i] != other._colors[i]) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected Object clone() {
        Position new_pos = new Position();
        new_pos._cube_position = this._cube_position;
        new_pos._cube_value = this._cube_value;
        for (int i = 0; i < this._checkers.length; i++) {
            new_pos._checkers[i] = this._checkers[i];
            new_pos._colors[i] = this._colors[i];
        }
        return new_pos;
    }

    public void moveChecker(int from, int to, int direction) {
    }

    public int getPips(int player) {
        return 0;
    }

    boolean absolute_equal(Position otherPosition) {
        int count, other_count;
        int color, other_color;
        for (int i = 0; i <= 25; i++) {
            count = _checkers[i];
            color = _colors[i];
            if (color != Constants.PLAYER_NONE) {
                color = Constants.PLAYER_SOME;
            }
            other_count = otherPosition._checkers[i];
            other_color = otherPosition._checkers[i];
            if (other_color != Constants.PLAYER_NONE) {
                other_color = Constants.PLAYER_SOME;
            }
            if (count != other_count || color != other_color) {
                return false;
            }
        }
        return true;
    }
}
