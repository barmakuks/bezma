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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.fairbg.bezma.R;
import com.fairbg.bezma.communication.IModelView;
import com.fairbg.bezma.communication.commands.ICommandObserver;
import com.fairbg.bezma.communication.commands.UserCommand;
import com.fairbg.bezma.core.MatchParameters;
import com.fairbg.bezma.core.Presenter;
import com.fairbg.bezma.core.model.ModelState;
import com.fairbg.bezma.core.model.Position;
import com.fairbg.bezma.version3.ConfigurationVer3;
import com.fairbg.bezma.version3.ConfiguratorVer3;

/**
 * Окно отображения состояния матча на экране должно уметь реагировать на
 * команды пользователя и отправлять их слушателю
 * */
public class PlayActivity extends Activity implements IModelView {

	private Presenter m_Presenter = null;

	private PlayView m_view;

	private ArrayList<ICommandObserver> m_observers = new ArrayList<ICommandObserver>();
	private Position m_current_position = null;

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

	}

	private void initView() {
		m_view = new PlayView(this);
		m_view.setKeepScreenOn(true);
		setContentView(m_view); // отображаем его в Activity
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

	private class BoardView
	{
		
		private Bitmap m_board_bmp;
		private Bitmap m_cube_bmp;
		private Bitmap m_checker_black_bmp;
		private Bitmap m_checker_white_bmp;
		private int m_Left, m_Top;
		
		
		
		
		private final int[] _ldpi_nests = { 43, 67, 91, 116, 140, 165, 212, 236, 261, 286, 310, 335,/* BAR */188, /* OUT */367 };
		
		private int[] 	m_Nests;
		private int 	m_CheckerSize;
		private int 	m_MinY;
		private int 	m_MaxY;
		private int 	m_MinBarY;
		private int 	m_MaxBarY;
		private int 	m_WhiteHome;
		private int		m_BlackHome;
		
		private int     m_CubeMiddleY;
		private int     m_CubeBlackY;
		private int     m_CubeWhiteY;
		
		private int     m_CubeMiddleX;
		
		private int     m_CubeBlackX;
		private int     m_CubeWhiteX;
		private int 	m_CubeRightX;
		private int		m_CubeLeftX;
		
				
 
		private void initBitmaps() 
		{
			m_checker_black_bmp = BitmapFactory.decodeResource(getResources(), R.drawable.checker_black);
			m_checker_white_bmp = BitmapFactory.decodeResource(getResources(), R.drawable.checker_white);
			m_board_bmp = BitmapFactory.decodeResource(getResources(), R.drawable.glass_board);
			m_cube_bmp = BitmapFactory.decodeResource(getResources(), R.drawable.cube);
		}

		private void LoadHdpiValues()
		{
			m_Nests = new int[] { 43, 67, 91, 116, 140, 165, 212, 236, 261, 286, 310, 335,/* BAR */188, /* OUT */367 };
			
			m_CheckerSize = 23;
			m_MinY = 47;
			m_MaxY = 352;
			m_MinBarY = 187;
			m_MaxBarY = 214;
			m_WhiteHome = 366;
			m_BlackHome = 33;
			m_CubeMiddleY = 187;
			m_CubeBlackY = 47;
			m_CubeWhiteY = 325;
			
			m_CubeMiddleX = m_Nests[12] - 3;
			m_CubeBlackX = 7;
			m_CubeWhiteX = 7;
			m_CubeRightX = m_Nests[9] - 15;
			m_CubeLeftX = m_Nests[2] + 12;			
		}

		private void LoadLdpiValues()
		{
			m_Nests = new int[] { 26,  41,  55,  70,  85, 100,
								 127, 142, 157, 172, 186, 201,
								 /* BAR */113, /* OUT */230 };
			
			m_CheckerSize = 14;
			m_MinY = 29;
			m_MaxY = 212;
			m_MinBarY = 111;
			m_MaxBarY = 130;
			m_WhiteHome = 240;
			m_BlackHome = 5;
			m_CubeMiddleY = 112;
			m_CubeBlackY = 29;
			m_CubeWhiteY = 220;
			
			m_CubeMiddleX = m_Nests[12] - 3;
			m_CubeBlackX = 7;
			m_CubeWhiteX = 7;
			m_CubeRightX = m_Nests[9] - 15;
			m_CubeLeftX = m_Nests[2] + 12;			
		}
		
		public BoardView(int left, int top)
		{
			DisplayMetrics dm = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);
			if (dm.heightPixels <= 240 || dm.widthPixels <= 240)
			{
				LoadLdpiValues();
			}
			else
			{
				LoadHdpiValues();				
			}
						
			m_Left = left; m_Top = top;
			initBitmaps();
		}
		
		public void drawBoard(Canvas canvas, int[] checkers) {
			if (m_current_position == null)
				return;
			
			//canvas.rotate(90, 200, 200);
			//canvas.translate(100, -200);
			
			canvas.translate(m_Left, m_Top);
			
			canvas.drawBitmap(m_board_bmp, 0, 0, null);
			
			int y = 0;
			int x = 0;
			
			// рисуем фишки на доске
			for (int i = 1; i <= 24; i++) {
				
				int k = (i >= 13) ? 1 : -1;
				
				y = (i >= 13) ? m_MinY : m_MaxY - m_CheckerSize;				
				
				x = (i < 13) ? m_Nests[(i - 1) % 12] : m_Nests[11 - (i - 1) % 12];
				
				for (int j = 0; j > checkers[i]; j--) {
					canvas.drawBitmap(m_checker_black_bmp, x, y - k * j * m_CheckerSize, null);
				}
				
				for (int j = 0; j < checkers[i]; j++) {
					canvas.drawBitmap(m_checker_white_bmp, x, y + k * j * m_CheckerSize, null);
				}
			}
			
			// рисуем фишки на баре
			x = m_Nests[12];
			for (int j = 0; j < checkers[25]; j++) {
				canvas.drawBitmap(m_checker_white_bmp, x, m_MinBarY - j * m_CheckerSize - m_CheckerSize, null);
			}
			for (int j = 0; j > checkers[0]; j--) {
				canvas.drawBitmap(m_checker_black_bmp, x, m_MaxBarY - j * m_CheckerSize, null);
			}
			
			// рисуем фишки на выбросе
			y = m_WhiteHome;
			for (int j = 0; j < checkers[26]; j++) {
				canvas.drawBitmap(m_checker_white_bmp, m_Nests[13] - j * m_CheckerSize, y, null);
			}
			y = m_BlackHome - m_CheckerSize;
			for (int j = 0; j > checkers[27]; j--) {
				canvas.drawBitmap(m_checker_black_bmp, m_Nests[13] + j * m_CheckerSize, y, null);
			}
			
			canvas.translate(-m_Left, -m_Top);

		}

		public void drawCube(Canvas canvas, int cubeValue, int cubePosition) {
			float angle = 0;
			if (m_current_position != null) {
				int x = 0;
				int y = 0;
				int cube_value = m_current_position.getCubeValue();
				switch (m_current_position.getCubePosition()) {
				case Position.CUBE_CENTER:
					x = m_CubeMiddleX;
					y = m_CubeMiddleY;
					angle = -90;
					break;
				case Position.CUBE_CRAWFORD:
					return;
				case Position.CUBE_BLACK:
					x = m_CubeBlackX;
					y = m_CubeBlackY;
					angle = 180;
					break;
				case Position.CUBE_WHITE:
					x = m_CubeWhiteX;
					y = m_CubeWhiteY;
					break;
				case Position.CUBE_RIGHT:
					x = m_CubeRightX;
					y = m_CubeMiddleY;
					angle = 180;
					break;
				case Position.CUBE_LEFT:
					x = m_CubeLeftX;
					y = m_CubeMiddleY;
					break;
				default:
					return;
				}
				canvas.translate(m_Left, m_Top);
				canvas.drawBitmap(m_cube_bmp, x, y, null);
				drawText(canvas, Integer.toString(cube_value), x + m_cube_bmp.getWidth() / 2 - 1,
						y + m_cube_bmp.getHeight() / 2 - 1, 13, angle);
				canvas.translate(-m_Left, -m_Top);
			}
		}

		/**
		 * Выводит текст
		 * @param canvas   канва, где будет выведен текст
		 * @param text     текст
		 * @param left       смещение на канве по оси Х
		 * @param top       смещение на канве по оси Y
		 * @param textSize размер шрифта
		 * @param angle    угол поворота относительно центра текста
		 */
		private void drawText(Canvas canvas, String text, int left, int top, float textSize, float angle) {
			TextPaint mTextPaint = new TextPaint();
			Rect text_bounds = new Rect();
			mTextPaint.setAntiAlias(true);
			mTextPaint.setTextSize(textSize);
			// mTextPaint.setTextSkewX(-0.15f);
			mTextPaint.setColor(0xFFFFFFFF);
			mTextPaint.setTextAlign(Align.CENTER);
			mTextPaint.getTextBounds(text, 0, text.length(), text_bounds);
			int y = top + text_bounds.height() / 2;
			canvas.rotate(angle, left, top);
			canvas.drawText(text, left, y, mTextPaint);
			canvas.rotate(-angle, left, top);
		}
		
	}
	private class PlayView extends View {
	
		private Bitmap m_Background;
		private BoardView m_BoardView;
		
		public PlayView(Context context) {
			super(context);
			initBitmaps();
			m_BoardView = new BoardView(0, 0);
		}
		private void initBitmaps() {
			m_Background = BitmapFactory.decodeResource(getResources(), R.drawable.gradient);
		}

		protected void onDraw(Canvas canvas) {

			canvas.drawBitmap(m_Background, 0, 0, null);
			
			if (m_current_position != null)
			{
				m_BoardView.drawBoard(canvas, m_current_position.getPosiiton());

				m_BoardView.drawCube(canvas, m_current_position.getCubeValue(), m_current_position.getCubePosition());				
			}
			

			/*Rect rect = new Rect(100, 100, 300, 150);
			Paint bar_paint = new Paint(); // параметры рисования основного
											// прямоугольника
			bar_paint.setShadowLayer(3, 3, 3, 0xBF000000);
			bar_paint.setColor(0xFFCAC8A9);
			canvas.drawRect(rect, bar_paint);*/
			/*
			 * Paint inner_light_paint = new Paint(); // параметры рисования
			 * основного прямоугольника inner_light_paint.setColor(0xFFFFFFBE);
			 * inner_light_paint.setStyle(Style.STROKE);
			 * inner_light_paint.setStrokeWidth(5);
			 */
			
			// canvas.drawRect(rect, inner_light_paint);

			// canvas.drawBitmap(_cube, 100, 100, paint);
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

	public void drawCube(Canvas canvas, int cubeValue, int cubePosition) {
		// TODO Auto-generated method stub
		
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
