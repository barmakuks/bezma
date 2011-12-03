package com.fairbg.gui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Color;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.Canvas;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.fairbg.R;
import com.fairbg.core.Corel;
import com.fairbg.core.MatchParameters;
import com.fairbg.core.Position;
import com.fairbg.core.Board;
import com.fairbg.core.commands.UserCommand;
import com.fairbg.core.commands.CommanderImpl;
import com.fairbg.core.commands.ICommandListener;

/**
 * Окно отображения состояния матча на экране должно уметь реагировать на
 * команды пользователя и отправлять их слушателю
 * */
public class BoardWindow extends Activity implements
		com.fairbg.core.IBoardWindow {

	private Bitmap _background;
	private Bitmap _grid;
	private Bitmap _cube;
	private Bitmap _board;
	private Bitmap _checker_black;
	private Bitmap _checker_white;

	private GraphicsView view;
	// private LinearLayout line;
	private GridView buttonsView;

	private CommanderImpl _commander_impl = null;
	private Position _current_position = null;
	private MatchParameters _match_parameters = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		_commander_impl = new CommanderImpl(this);
		Corel.corel.setBoardWindow(this);

		view = new GraphicsView(this);

		/*
		 * line = new LinearLayout(this); line.addView(new Button(this));
		 * line.addView(new Button(this)); line.addView(new Button(this));
		 * line.addView(new Button(this)); line.addView(new Button(this));
		 */
		/*
		 * buttonsView = new GridView(this);
		 * buttonsView.setVisibility(GridView.VISIBLE);
		 */

		setContentView(view); // отображаем его в Activity

		/*
		 * ArrayList<View> btns = new ArrayList<View>(); btns.add(new
		 * Button(this)); btns.add(new Button(this)); btns.add(new
		 * Button(this)); btns.add(new Button(this)); buttonsView.addView(new
		 * Button(this));
		 */
		android.widget.FrameLayout.LayoutParams params = new android.widget.FrameLayout.LayoutParams(
				300, 300);
		params.setMargins(100, 200, 100, 200);

		// addContentView(line, params);

		// загружаем иконку из ресурсов в объект myBitmap
		_background = BitmapFactory.decodeResource(getResources(),
				R.drawable.gradient);
		_grid = BitmapFactory.decodeResource(getResources(),
				R.drawable.grid_red);
		_checker_black = BitmapFactory.decodeResource(getResources(),
				R.drawable.checker_black);
		_checker_white = BitmapFactory.decodeResource(getResources(),
				R.drawable.checker_white);
		_board = BitmapFactory.decodeResource(getResources(),
				R.drawable.glass_board);
		_cube = BitmapFactory.decodeResource(getResources(), R.drawable.cube);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	private final int[] _nests = { 43, 67, 91, 116, 140, 165, 212, 236, 261,
			286, 310, 335,/* BAR */188, /* OUT */367 };

	public class GraphicsView extends View {
		public GraphicsView(Context context) {
			super(context);
		}

		private void drawBoard(Canvas canvas, int dX, int dY) {
			if (_current_position == null)
				return;
			int[] checkers = _current_position.getPosiiton();
			canvas.translate(dX, dY);
			canvas.drawBitmap(_board, 0, 0, null);
			final int SIZE = 23;
			int y = 0;
			int x = 0;
			// рисуем фишки на доске
			for (int i = 0; i < 24; i++) {
				int k = (i < 12) ? 1 : -1;
				y = (i < 12) ? 47 : 352 - SIZE;
				x = _nests[i % 12];
				for (int j = 0; j > checkers[i]; j--) {
					canvas.drawBitmap(_checker_black, x, y - k * j * SIZE, null);
				}
				for (int j = 0; j < checkers[i]; j++) {
					canvas.drawBitmap(_checker_white, x, y + k * j * SIZE, null);
				}
			}
			// рисуем фишки на баре
			x = _nests[12];
			for (int j = 0; j < checkers[24]; j++) {
				canvas.drawBitmap(_checker_white, x, 187 - j * SIZE - SIZE,
						null);
			}
			for (int j = 0; j > checkers[25]; j--) {
				canvas.drawBitmap(_checker_black, x, 214 - j * SIZE, null);
			}
			// рисуем фишки на выбросе
			y = 366;
			for (int j = 0; j < checkers[26]; j++) {
				canvas.drawBitmap(_checker_white, _nests[13] - j * SIZE, y,
						null);
			}
			y = 33 - SIZE;
			for (int j = 0; j > checkers[27]; j--) {
				canvas.drawBitmap(_checker_black, _nests[13] + j * SIZE, y,
						null);
			}
			canvas.translate(-dX, -dY);

		}

		protected void onDraw(Canvas canvas) {
			canvas.drawBitmap(_background, 0, 0, null);
			canvas.rotate(90, 200, 200);
			canvas.translate(100, -200);
			drawBoard(canvas, 40, 163);
			drawCube(canvas, 40, 163);

			Rect rect = new Rect(100, 100, 300, 150);
			Paint bar_paint = new Paint(); // параметры рисования основного
											// прямоугольника
			bar_paint.setShadowLayer(3, 3, 3, 0xBF000000);
			bar_paint.setColor(0xFFCAC8A9);
			/*
			 * Paint inner_light_paint = new Paint(); // параметры рисования
			 * основного прямоугольника inner_light_paint.setColor(0xFFFFFFBE);
			 * inner_light_paint.setStyle(Style.STROKE);
			 * inner_light_paint.setStrokeWidth(5);
			 */
			canvas.drawRect(rect, bar_paint);
			// canvas.drawRect(rect, inner_light_paint);

			// canvas.drawBitmap(_cube, 100, 100, paint);
		}

		private void drawCube(Canvas canvas, int dX, int dY) {
			float angle = 0;
			if (_current_position != null) {
				int x = 0;
				int y = 0;
				int cube_value = _current_position.getCubeValue();
				switch (_current_position.getCubePosition()) {
				case Position.CUBE_CENTER:
					x = _nests[12] - 3;
					y = 187;
					angle = -90;
					break;
				case Position.CUBE_CRAWFORD:
					return;
				case Position.CUBE_BLACK:
					x = 7;
					y = 47;
					angle = 180;
					break;
				case Position.CUBE_WHITE:
					x = 7;
					y = 325;
					break;
				case Position.CUBE_RIGHT:
					x = _nests[9] - 15;
					y = 187;
					angle = 180;
					break;
				case Position.CUBE_LEFT:
					x = _nests[2] + 12;
					y = 187;
					break;
				default:
					return;
				}
				canvas.translate(dX, dY);
				canvas.drawBitmap(_cube, x, y, null);
				drawText(canvas, Integer.toString(cube_value),
						x + _cube.getWidth() / 2 - 1, y + _cube.getHeight() / 2
								- 1, 13, angle);
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
		private void drawText(Canvas canvas, String text, int dX, int dY,
				float textSize, float angle) {
			TextPaint mTextPaint = new TextPaint();
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
			canvas.rotate(-angle, dX, dY);
		}
	}

	public void setPosition(Position position) {
		_current_position = position;
		if (position == null)
			Log.i("POS", "EMPTY");
		else
			Log.i("POS", position.toString());
		invalidate();
		// view.invalidate();
	}

	public void setMatchParameters(MatchParameters params) {
		_match_parameters = params;
	}

	private void invalidate() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				/*
				 * if (line.getVisibility() == View.VISIBLE)
				 * line.setVisibility(View.GONE); else
				 * line.setVisibility(View.VISIBLE);
				 */
				view.invalidate();
				/*
				 * AlphaAnimation anim = new AlphaAnimation(1, 0.2f);
				 * anim.setDuration(5000); view.startAnimation(anim);
				 */
			}
		});
	}

	@Override
	public void addListener(ICommandListener listener) {
		_commander_impl.addListener(listener);
	}

	@Override
	public void sendCommand(UserCommand command) {
		_commander_impl.sendCommand(command);
	}

	@Override
	public void start() {
	}

	@Override
	public void stop() {
	}

}
