package com.fairbg.bezma.qui;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.fairbg.bezma.R;
import com.fairbg.bezma.communication.IModelView;
import com.fairbg.bezma.communication.commands.ICommandObserver;
import com.fairbg.bezma.communication.commands.UserCommand;
import com.fairbg.bezma.core.MatchParameters;
import com.fairbg.bezma.core.Presenter;
import com.fairbg.bezma.version3.ConfigurationVer3;
import com.fairbg.bezma.version3.ConfiguratorVer3;
import com.fairbg.core.model.ModelState;
import com.fairbg.core.model.Position;

/**
 * Окно отображения состояния матча на экране должно уметь реагировать на
 * команды пользователя и отправлять их слушателю
 * */
public class PlayActivity extends Activity implements IModelView {

	private Presenter m_Presenter = null;

	private Bitmap m_Background;
	private Bitmap m_cube_bmp;
	private Bitmap m_board_bmp;
	private Bitmap m_checker_black_bmp;
	private Bitmap m_checker_white_bmp;
	private PlayView m_view;

	private ArrayList<ICommandObserver> m_observers = new ArrayList<ICommandObserver>();
	private Position m_current_position = null;

	// private MatchParameters _match_parameters = null;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);

		ConfigurationVer3 configuration = (ConfigurationVer3) (getIntent().getExtras()
				.getSerializable(ConfigurationVer3.class.getCanonicalName()));
		ConfiguratorVer3 configurator = new ConfiguratorVer3();

		m_Presenter = new Presenter(configurator, configuration);

		m_Presenter.addView(this);

		if (!m_Presenter.start()) {
			showErrorMessage("Can't start listen bluetooth device");
			this.finish();
		}
		
		initView();

		initBitmaps();

	}

	private void initView() {
		m_view = new PlayView(this);
		m_view.setKeepScreenOn(true);

		setContentView(m_view); // отображаем его в Activity

		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(300, 300);
		params.setMargins(100, 200, 100, 200);

	}

	private void initBitmaps() {
		// загружаем иконку из ресурсов в объект myBitmap
		m_Background = BitmapFactory.decodeResource(getResources(), R.drawable.gradient);
		/*
		 * _grid = BitmapFactory.decodeResource(getResources(),
		 * R.drawable.grid_red);
		 */
		m_checker_black_bmp = BitmapFactory.decodeResource(getResources(), R.drawable.checker_black);
		m_checker_white_bmp = BitmapFactory.decodeResource(getResources(), R.drawable.checker_white);
		m_board_bmp = BitmapFactory.decodeResource(getResources(), R.drawable.glass_board);
		m_cube_bmp = BitmapFactory.decodeResource(getResources(), R.drawable.cube);
	}

	private void showErrorMessage(String message) {
		Context context = getApplicationContext();
		CharSequence text = message;
		int duration = Toast.LENGTH_LONG;
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}

	@Override
	protected void onDestroy() {
		m_Presenter.stop();
		m_Presenter.removeView(this);
		super.onDestroy();
	}

	private final int[] _nests = { 43, 67, 91, 116, 140, 165, 212, 236, 261, 286, 310, 335,/* BAR */188, /* OUT */367 };

	public class PlayView extends View {
		public PlayView(Context context) {
			super(context);
		}

		private void drawBoard(Canvas canvas, int dX, int dY) {
			if (m_current_position == null)
				return;
			int[] checkers = m_current_position.getPosiiton();
			canvas.translate(dX, dY);
			canvas.drawBitmap(m_board_bmp, 0, 0, null);
			final int SIZE = 23;
			int y = 0;
			int x = 0;
			// рисуем фишки на доске
			for (int i = 1; i <= 24; i++) {
				int k = (i >= 13) ? 1 : -1;
				y = (i >= 13) ? 47 : 352 - SIZE;
				x = (i < 13) ? _nests[(i - 1) % 12] : _nests[11 - (i - 1) % 12];
				for (int j = 0; j > checkers[i]; j--) {
					canvas.drawBitmap(m_checker_black_bmp, x, y - k * j * SIZE, null);
				}
				for (int j = 0; j < checkers[i]; j++) {
					canvas.drawBitmap(m_checker_white_bmp, x, y + k * j * SIZE, null);
				}
			}
			// рисуем фишки на баре
			x = _nests[12];
			for (int j = 0; j < checkers[25]; j++) {
				canvas.drawBitmap(m_checker_white_bmp, x, 187 - j * SIZE - SIZE, null);
			}
			for (int j = 0; j > checkers[0]; j--) {
				canvas.drawBitmap(m_checker_black_bmp, x, 214 - j * SIZE, null);
			}
			// рисуем фишки на выбросе
			y = 366;
			for (int j = 0; j < checkers[26]; j++) {
				canvas.drawBitmap(m_checker_white_bmp, _nests[13] - j * SIZE, y, null);
			}
			y = 33 - SIZE;
			for (int j = 0; j > checkers[27]; j--) {
				canvas.drawBitmap(m_checker_black_bmp, _nests[13] + j * SIZE, y, null);
			}
			canvas.translate(-dX, -dY);

		}

		protected void onDraw(Canvas canvas) {
			canvas.drawBitmap(m_Background, 0, 0, null);
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
			if (m_current_position != null) {
				int x = 0;
				int y = 0;
				int cube_value = m_current_position.getCubeValue();
				switch (m_current_position.getCubePosition()) {
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
				canvas.drawBitmap(m_cube_bmp, x, y, null);
				drawText(canvas, Integer.toString(cube_value), x + m_cube_bmp.getWidth() / 2 - 1,
						y + m_cube_bmp.getHeight() / 2 - 1, 13, angle);
				canvas.translate(-dX, -dY);
			}
		}

		/**
		 * Выводит текст
		 * @param canvas   канва, где будет выведен текст
		 * @param text     текст
		 * @param dX       смещение на канве по оси Х
		 * @param dY       смещение на канве по оси Y
		 * @param textSize размер шрифта
		 * @param angle    угол поворота относительно центра текста
		 */
		private void drawText(Canvas canvas, String text, int dX, int dY, float textSize, float angle) {
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
		m_current_position = position;
		if (position == null)
			Log.i("POS", "EMPTY");
		else
			Log.i("POS", position.toString());
		invalidate();
		// view.invalidate();
	}

	public void setMatchParameters(MatchParameters params) {
		/* _match_parameters = params; */
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
				m_view.invalidate();
				/*
				 * AlphaAnimation anim = new AlphaAnimation(1, 0.2f);
				 * anim.setDuration(5000); view.startAnimation(anim);
				 */
			}
		});
	}

	@Override
	public boolean start() {
		return true;
	}

	@Override
	public void stop() {
	}

	@Override
	public void notifyObservers(UserCommand aCommand) {
		for (ICommandObserver observer : m_observers) {
			observer.handeEvent(aCommand);
		}
	}

	@Override
	public void addObserver(ICommandObserver aCommandObserver) {
		m_observers.add(aCommandObserver);
	}

	@Override
	public void removeObserver(ICommandObserver aCommandObserver) {
		m_observers.remove(aCommandObserver);
	}

	@Override
	public void setModelState(ModelState aModelState) {
		// TODO Auto-generated method stub
		if (aModelState != null && !aModelState.isErrorState()) {
			setPosition(aModelState.getCurrentPosition());
		}
	}

	@Override
	public void sendCommand(UserCommand command) {
		// do nothing to send datagram
	}

}
