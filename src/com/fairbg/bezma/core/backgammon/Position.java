package com.fairbg.bezma.core.backgammon;

import java.util.Random;

import com.fairbg.bezma.core.model.PlayerColors;
import com.fairbg.bezma.log.BezmaLog;

/** Describes checkers positions and cube state*/
public class Position implements Cloneable
{
    /**
     * Describes direction of checkers movement for bottom player*/
    public enum Direction{
        /** Board direction is not defined*/
        None, 
        /** Board for white player (player in the bottom place), movements counter clockwise*/
        WhiteCCW,
        /** Board for white player, movements clockwise*/
        WhiteCW,
        /** Board for black player, movements counter clockwise*/
        BlackCCW,
        /** Board for black player, movements clockwise*/
        BlackCW
    }

    /** No cube on board */
    public static final int CUBE_CRAWFORD = -1;
    /** Cube in the middle */
    public static final int CUBE_CENTER = 0;
    /** White player owns the cube in the bottom of the board */
    public static final int CUBE_WHITE = 1;
    /** Black player owns cube on the top of the board*/
    public static final int CUBE_BLACK = 2;
    /**
     * Cube is proposed to the black player and placed into the right part of the board
     */
    public static final int CUBE_RIGHT = 3;
    /**
     * Cube is proposed to the white player and placed into the left part of the board
     */
    public static final int CUBE_LEFT = 4;
    public static final int MAX_CHECKERS_IN_POSITION = 5;

    /**
     * Индекс бара. С него начинает ся поиск хода. Ход совершается от большего
     * индекса к меньшему
     */
    public static final int BAR_POSITION = 25;

    /**
     * Позиции фишек на доске
     * 
     * @details 1-24 - позиции фишек( >0 - белые, <0 - черные, 0 - пусто), 0 -
     *          бар белых, 25 - бар черных, 26,27 - выброс черных и белых
     *          соответсвенно направление движения для белых прямое, для черных
     *          - обратное
     * */
    private int[] m_Checkers = new int[28];

    /**
     * Преобразовывает положение фишки на доске в индекс массива в зависимости
     * от цвета игрока
     * 
     * @param position
     *            положение фишки на доске относительно игрока @param direction
     * @return индекс позиции в массиве m_Checkers
     */
    private int getIndex(int position, PlayerColors direction)
    {
        if (position <= 0)
        {
            return direction == PlayerColors.BLACK ? 26 : 27;
        }
        else
        {
            return direction == PlayerColors.BLACK ? position : BAR_POSITION - position;
        }
    }

    private int getPLayerSign(PlayerColors player)
    {
        return player == PlayerColors.WHITE ? 1 : player == PlayerColors.BLACK ? -1 : 0;
    }

    /** текущее значение кубика стоимости */
    private int m_CubeValue = 1;

    /** текущее положение кубика стоимости на столе */
    private int m_CubePosition = CUBE_CENTER;

    /**
     * Возвращает массив содержащий положение и цвет фишек на доске если
     * значение >0, то количество белых, если <0, то количество черных позиция с
     * 1 по 24 - количество фишек на игровых полях позиция 25 - бар белых фишек
     * позиция 0 - бар черных фишек 26 - зона выброса белых фишек 27 - зона
     * выброса черных фишек
     * */
    public int[] getCheckers()
    {
        return m_Checkers;
    }

    public void setCheckers(int[] checkers)
    {
        if (checkers.length != m_Checkers.length)
        {
            BezmaLog.e("SET CHECKERS", "Invalide size of incomming checkers array");
            Thread.currentThread().interrupt();
        }
        for (int i = 0; i < checkers.length; ++i)
        {
            m_Checkers[i] = checkers[i];
        }

    }

    /** Возвращает текущее значение кубика стоимости */
    public int getCubeValue()
    {
        return m_CubeValue;
    }

    /** Возвращает текущее положение кубика стоимости на доске */
    public int getCubePosition()
    {
        return m_CubePosition;
    }

    @Override
    public String toString()
    {
        StringBuilder res = new StringBuilder();
        res.append('\n');
        for (int i = 0; i < 5; ++i)
        {
            for (int j = 13; j <= 24; ++j)
            {
                if (Math.abs(m_Checkers[j]) > i)
                {
                    res.append(m_Checkers[j] > 0 ? '0' : 'X');
                }
                else
                {
                    res.append('-');
                }
            }
            res.append('\n');
        }

        for (int i = 4; i >= 0; --i)
        {
            for (int j = 12; j >= 1; --j)
            {
                if (Math.abs(m_Checkers[j]) > i)
                {
                    res.append(m_Checkers[j] > 0 ? '0' : 'X');
                }
                else
                {
                    res.append('-');
                }
            }
            res.append('\n');
        }
        res.append("Bar : ");

        int count = m_Checkers[0];
        for (int i = 0; i < Math.abs(count); i++)
        {
            res.append(count > 0 ? '0' : 'X');
        }

        res.append(" : ");
        count = m_Checkers[25];

        for (int i = 0; i < Math.abs(count); i++)
        {
            res.append(count > 0 ? '0' : 'X');
        }

        /*
         * for (int i = 0; i < m_Checkers.length; i++) {
         * res.append(m_Checkers[i]); res.append(','); }
         */
        return res.toString();
    }

    /**
     * Возвращает случайно сгенерированную позицию
     * */
    public static Position getRandomPosition()
    {
        Position pos = new Position();
        for (int i = 0; i < pos.m_Checkers.length; i++)
        {
            pos.m_Checkers[i] = 0;
        }
        Random rnd = new Random();
        int checkers_left = 15;
        while (checkers_left > 0)
        { // расставляем белые фишки
            int p = rnd.nextInt(28);
            if (p != 27 && p != 25 && pos.m_Checkers[p] >= 0)
            {
                pos.m_Checkers[p]++;
                checkers_left--;
            }
        }
        checkers_left = 15;
        while (checkers_left > 0)
        { // расставляем черные фишки
            int p = rnd.nextInt(28);
            if (p != 26 && p != 24 && pos.m_Checkers[p] <= 0)
            {
                pos.m_Checkers[p]--;
                checkers_left--;
            }
        }
        pos.m_CubeValue = (new int[] { 2, 4, 8, 16, 32, 64 })[rnd.nextInt(6)];
        pos.m_CubePosition = rnd.nextInt(6) - 1;
        return pos;
    }

    @Override
    public Object clone() throws CloneNotSupportedException
    {
        Position clone = new Position();
        for (int i = 0; i < this.m_Checkers.length; ++i)
        {
            clone.m_Checkers[i] = this.m_Checkers[i];
        }
        clone.m_CubePosition = this.m_CubePosition;
        clone.m_CubeValue = this.m_CubeValue;
        return clone;
    }

    public void applyMove(Move move)
    {
        for (Movement movement : move.getMovements())
        {
            applayMovement(movement.MoveFrom, movement.MoveTo, movement.Color);
        }
    }

    public void applayMovement(int moveFrom, int moveTo, PlayerColors player)
    {
        moveFrom = getIndex(moveFrom, player);
        moveTo = getIndex(moveTo, player);
        final int OPP_BAR_POSITION = getIndex(Position.BAR_POSITION, PlayerColors.getAltColor(player));

        final int ONE_CHECKER = getPLayerSign(player);

        m_Checkers[moveFrom] -= ONE_CHECKER; // убираем одну фишку с позиции

        if (moveTo < Position.BAR_POSITION)
        {
            if ((m_Checkers[moveTo] == 0) || // если пустая позиция
                    ONE_CHECKER * m_Checkers[moveTo] > 0) // или там стоят фишки
                                                          // того же цвета
            { // там стояли свои фишки
                m_Checkers[moveTo] += ONE_CHECKER;
            }
            else
            {
                if (ONE_CHECKER * m_Checkers[moveTo] == -1) // там стояла одна
                                                            // фишка противника
                {
                    m_Checkers[moveTo] = ONE_CHECKER;
                    m_Checkers[OPP_BAR_POSITION] += -ONE_CHECKER;
                }
            }
        }
    }

    public int getPips(PlayerColors forPlayer)
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public PlayerColors getCheckerColor(int position, PlayerColors player)
    {
        final int index = getIndex(position, player);
        int color = m_Checkers[index] * getPLayerSign(PlayerColors.WHITE);
        
        return color == 0 ? PlayerColors.NONE : color > 0 ? PlayerColors.WHITE : PlayerColors.BLACK;
    }

    public boolean absoluteEquals(Position other)
    {
        for (int i = 0; i <= 25; i++)
        {
            if (this.m_Checkers[i] != other.m_Checkers[i])
            {
                return false;
            }
        }
        return true;
    }

    public boolean approxEquals(Position other)
    {
	boolean equals = true;
        
	for (int i = 0; equals && i <= 25; i++)
        {
            int count = getCheckerCount(i, PlayerColors.BLACK);
            int other_count = other.getCheckerCount(i, PlayerColors.BLACK);
            PlayerColors color = getCheckerColor(i, PlayerColors.BLACK);
            PlayerColors other_color = other.getCheckerColor(i, PlayerColors.BLACK);

            equals = equals && (other_color == color || other_color == PlayerColors.NONE || color == PlayerColors.NONE);

            equals = equals && (count == other_count) || (count >= other_count && (other_count == MAX_CHECKERS_IN_POSITION));
        }

        return equals;
    }

    public int getCheckerCount(int position, PlayerColors player)
    {
        return Math.abs(m_Checkers[getIndex(position, player)]);
    }

    public boolean hasCheckerInBar(PlayerColors player)
    {
        return m_Checkers[getIndex(BAR_POSITION, player)] != 0;
    }
}
