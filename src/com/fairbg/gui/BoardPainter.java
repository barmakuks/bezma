/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fairbg.gui;

import com.fairbg.core.Board;
import com.fairbg.core.Constants;
import com.fairbg.core.MatchParameters;
import com.fairbg.core.Position;
import com.sun.corba.se.impl.orbutil.closure.Constant;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import javax.swing.ImageIcon;

/**
 *
 * @author Vitalik
 */
public class BoardPainter {

    private static Image _img_checker_black = null;
    private static Image _img_checker_white = null;
    private static Image _img_cube = null;
    private static Image _img_board = null;
    private static Image _img_gradient = null;
    private static Board _board;

    private static void initGraphics() {
        _img_checker_black = new ImageIcon(BoardPainter.class.getResource("resources/checker_black.png")).getImage();
        _img_checker_white = new ImageIcon(BoardPainter.class.getResource("resources/checker_white.png")).getImage();
        _img_cube = new ImageIcon(BoardPainter.class.getResource("resources/cube.png")).getImage();
        _img_board = new ImageIcon(BoardPainter.class.getResource("resources/glass_board.png")).getImage();
        _img_gradient = new ImageIcon(BoardPainter.class.getResource("resources/gradient.png")).getImage();
    }
    private static final int[] _nests = {43, 67, 91, 116, 140, 165, 212, 236, 261,
        286, 310, 335,/* BAR */ 188, /* OUT */ 367};

    private static void drawBoard(Graphics canvas, int dX, int dY) {
        if (_board == null || _board.getCurrentPosition() == null) {
            return;
        }
        Position pos = _board.getCurrentPosition();
        int[] checkers = pos.getCheckers();
        int[] colors = pos.getColors();
        canvas.translate(dX, dY);
        canvas.drawImage(_img_board, 0, 0, null);
        final int SIZE = 23;
        int y = 0;
        int x = 0;
        // рисуем фишки на доске
        for (int i = 0; i < 24; i++) {
            int k = (i < 12) ? 1 : -1;
            y = (i < 12) ? 47 : 352 - SIZE;
            x = (i < 12) ? _nests[11 - i % 12] : _nests[i % 12];
            boolean is_white = colors[i] == Constants.PLAYER_WHITE;
            for (int j = 0; j < checkers[i]; j++) {
                canvas.drawImage(is_white ? _img_checker_white : _img_checker_black, x, y + k * j * SIZE, null);
            }
        }
        // рисуем фишки на баре
        x = _nests[12];
        for (int j = 0; j < checkers[24]; j++) {
            canvas.drawImage(_img_checker_white, x, 218 + j * SIZE, null);
        }
        for (int j = 0; j < checkers[25]; j++) {
            canvas.drawImage(_img_checker_black, x, 185 - j * SIZE - SIZE, null);
        }
        // рисуем фишки на выбросе
        y = 366;
        for (int j = 0; j < checkers[26]; j++) {
            canvas.drawImage(_img_checker_white, _nests[13] - j * SIZE, y, null);
        }
        y = 33 - SIZE;
        for (int j = 0; j < checkers[27]; j++) {
            canvas.drawImage(_img_checker_black, _nests[13] - j * SIZE, y, null);
        }
        canvas.translate(-dX, -dY);

    }

    public static void draw(Graphics canvas, Board board) {
        if (_img_board == null) {
            initGraphics();
        }
        _board = board;
        canvas.drawImage(_img_gradient, 0, 0, null);
        drawBoard(canvas, 40, 163);
        drawCube(canvas, 40, 163);
        drawMatchInfo(canvas, 40, 163);
    }

    private static void drawCube(Graphics canvas, int dX, int dY) {
        float angle = 0;
        if (_board != null && _board.getCurrentPosition() != null) {
            int x = 0;
            int y = 0;
            Position pos = _board.getCurrentPosition();
            int cube_value = pos.getCubeValue();
            switch (pos.getCubePosition()) {
                case Constants.CUBE_CENTER:
                    x = _nests[12] - 3;
                    y = 187;
                    angle = -90;
                    break;
                case Constants.CUBE_CRAWFORD:
                    return;
                case Constants.CUBE_WHITE:
                    x = 7;
                    y = 47;
                    angle = 180;
                    break;
                case Constants.CUBE_BLACK:
                    x = 7;
                    y = 325;
                    break;
                case Constants.CUBE_RIGHT:
                    x = _nests[9] - 15;
                    y = 187;
                    angle = 180;
                    break;
                case Constants.CUBE_LEFT:
                    x = _nests[2] + 12;
                    y = 187;
                    break;
                default:
                    return;
            }
            canvas.translate(dX, dY);
            canvas.drawImage(_img_cube, x, y, null);
            drawText(canvas, Integer.toString(cube_value), x + _img_cube.getWidth(null) / 2 - 1,
                    y + _img_cube.getHeight(null) / 2 - 1, 12, angle, Color.YELLOW);
            canvas.translate(-dX, -dY);
        }
    }

    /**
     * Выводит текст
     * 
     * @param canvas
     *            канва, где будет выведен текст
     * @param text
     *            текст
     * @param dX
     *            смещение на канве по оси Х
     * @param dY
     *            смещение на канве по оси Y
     * @param textSize
     *            размер шрифта
     * @param angle
     *            угол поворота относительно центра текста
     */
    private static void drawText(Graphics canvas, String text, int dX, int dY, int textSize, float angle, Color color) {
        //int y = dY + text_bounds.height() / 2;
        //canvas.drawString(text, dY, dY);
        Graphics2D g2 = (Graphics2D) canvas;
        Font f = g2.getFont();
        g2.setFont(new Font(f.getFontName(), Font.BOLD, textSize));
        Rectangle2D text_bounds = g2.getFontMetrics().getStringBounds(text, canvas);
        int x = (int) (dX - text_bounds.getWidth() / 2);
        int y = (int) (dY + text_bounds.getHeight() / 2 - 3);
        double radian = Math.PI * angle / 180.0;
        g2.rotate(radian, dX, dY);
        g2.setColor(color);
        g2.drawString(text, x, y);
        g2.rotate(-radian, dX, dY);
        /*TextPaint mTextPaint = new TextPaint();
        Rect text_bounds = new Rect();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(textSize);
        // mTextPaint.setTextSkewX(-0.15f);
        mTextPaint.setColor(0xFFFFFFFF);
        mTextPaint.setTextAlign(Align.CENTER);
        mTextPaint.getTextBounds(text, 0, text.length(), text_bounds);
        int y = dY + text_bounds.height() / 2;
        canvas.rotate(angle, dX, dY);
        canvas.drawText(text, dX, y, mTextPaint);
        canvas.rotate(-angle, dX, dY);*/
    }

    private static void drawMatchInfo(Graphics canvas, int dX, int dY) {
        if (_board == null || _board.getMatch() == null || _board.getMatch().getMatchParams() == null) {
            return;
        }
        MatchParameters params = _board.getMatch().getMatchParams();
        canvas.translate(dX, dY - 100);
        canvas.setColor(new Color(0xe0e0be));
        canvas.fillRect(0, 0, 198, 100);
        drawText(canvas, params.Player1, 98, 90, 13, 180, Color.BLUE);
        canvas.translate(-dX, -dY + 100);

        canvas.translate(dX, dY + 400);
        canvas.setColor(new Color(0xe0e0be));
        canvas.fillRect(0, 0, 198, 100);
        drawText(canvas, params.Player2, 98, 90, 13, 0, Color.BLUE);
        canvas.translate(-dX, dY - 400);
    }
}
