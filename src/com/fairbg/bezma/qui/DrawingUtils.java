package com.fairbg.bezma.qui;

import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.text.TextPaint;

class DrawingUtils
{

    private static AssetManager m_assets = null;

    public static void setAssetManager(AssetManager assets)
    {
        m_assets = assets;
    }

    public static void drawText(Canvas canvas, String text, int left, int top, float textSize, float angle, int color)
    {
        drawText(canvas, text, left, top, textSize, angle, color, "");
    }

    /**
     * Выводит текст
     * 
     * @param canvas
     *            канва, где будет выведен текст
     * @param text
     *            текст
     * @param left
     *            смещение на канве по оси Х
     * @param top
     *            смещение на канве по оси Y
     * @param textSize
     *            размер шрифта
     * @param angle
     *            угол поворота относительно центра текста
     * @param color
     *            цвет текста
     * @param fontName
     *            файл шрифта
     */
    public static void drawText(Canvas canvas, String text, int left, int top, float textSize, float angle, int color, String fontName)
    {
        TextPaint mTextPaint = new TextPaint();

        if (m_assets != null && fontName != "")
        {
            Typeface typeface = Typeface.createFromAsset(m_assets, fontName);
            mTextPaint.setTypeface(typeface);
        }

        Rect text_bounds = new Rect();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(textSize);
        // mTextPaint.setTextSkewX(-0.15f);
        mTextPaint.setColor(color);
        mTextPaint.setTextAlign(Align.CENTER);
        mTextPaint.getTextBounds(text, 0, text.length(), text_bounds);
        int y = top + text_bounds.height() / 2;
        canvas.rotate(angle, left, top);
        canvas.drawText(text, left, y, mTextPaint);
        canvas.rotate(-angle, left, top);
    }
}
