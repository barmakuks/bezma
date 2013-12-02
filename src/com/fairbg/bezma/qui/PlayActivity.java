package com.fairbg.bezma.qui;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.fairbg.bezma.R;
import com.fairbg.bezma.communication.IModelView;
import com.fairbg.bezma.communication.commands.CommunicationCommand;
import com.fairbg.bezma.communication.commands.ICommandObserver;
import com.fairbg.bezma.core.MatchParameters;
import com.fairbg.bezma.core.Presenter;
import com.fairbg.bezma.core.backgammon.MovePrinter;
import com.fairbg.bezma.core.backgammon.Position;
import com.fairbg.bezma.core.model.IMove;
import com.fairbg.bezma.core.model.ModelSituation;
import com.fairbg.bezma.log.BezmaLog;
import com.fairbg.bezma.version3.ConfigurationVer3;
import com.fairbg.bezma.version3.ConfiguratorVer3;

/**
 * Окно отображения состояния матча на экране должно уметь реагировать на
 * команды пользователя и отправлять их слушателю
 * */
public class PlayActivity extends Activity implements IModelView
{

    private Presenter m_Presenter = null;

    private PlayView m_view;

    private ArrayList<ICommandObserver> m_observers = new ArrayList<ICommandObserver>();
    private Position m_current_position = null;
    private ProgressDialog pDialog;
    private String m_DeviceMAC = "";
    private MatchParameters m_MatchParameters = null;

    @Override
    protected void onCreate(Bundle bundle)
    {
	super.onCreate(bundle);

	requestWindowFeature(Window.FEATURE_NO_TITLE);
	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		WindowManager.LayoutParams.FLAG_FULLSCREEN);

	ConfigurationVer3 configuration = (ConfigurationVer3) (getIntent()
		.getExtras().getSerializable(ConfigurationVer3.class
		.getCanonicalName()));

	m_DeviceMAC = configuration.deviceMAC;
	m_MatchParameters = configuration.getMatchParameters();

	ConfiguratorVer3 configurator = new ConfiguratorVer3();

	m_Presenter = new Presenter(configurator, configuration);

	m_Presenter.addView(this);

	initView();

	DrawingUtils.setAssetManager(this.getAssets());

	startPresenter();

    }

    final int DEVICE_CONNECTION_OK = 1;
    final int DEVICE_CONNECTION_ERROR = 0;

    public void startPresenter()
    {
	pDialog = ProgressDialog.show(this, "Connecting to " + m_DeviceMAC
		+ "...", "Please wait", true, true);

	Thread thread = new Thread(new Runnable()
	{
	    @Override
	    public void run()
	    {
		int result = m_Presenter.start() ? DEVICE_CONNECTION_OK
			: DEVICE_CONNECTION_ERROR;

		handler.sendEmptyMessage(result);
	    }
	});

	thread.start();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler()
    {
	@Override
	public void handleMessage(Message msg)
	{
	    pDialog.dismiss();
	    // handle the result here
	    if (msg.what == DEVICE_CONNECTION_ERROR)
	    {
		presenterStartFailed();
	    }
	}
    };

    private void presenterStartFailed()
    {
	showErrorMessage("Can't start listen bluetooth device");
	this.finish();
    }

    private void initView()
    {
	m_view = new PlayView(this);
	m_view.setKeepScreenOn(true);
	setContentView(m_view); // отображаем его в Activity
    }

    private void showErrorMessage(String message)
    {
	Context context = getApplicationContext();
	CharSequence text = message;
	int duration = Toast.LENGTH_LONG;
	Toast toast = Toast.makeText(context, text, duration);
	toast.show();
    }

    @Override
    protected void onDestroy()
    {
	m_Presenter.stop();
	m_Presenter.removeView(this);
	super.onDestroy();
    }

    private class BoardView
    {

	private Bitmap m_cube_bmp_2;
	private Bitmap m_cube_bmp_4;
	private Bitmap m_cube_bmp_8;
	private Bitmap m_cube_bmp_16;
	private Bitmap m_cube_bmp_32;
	private Bitmap m_cube_bmp_64;

	private Bitmap m_checker_black_bmp;
	private Bitmap m_checker_white_bmp;
	private Bitmap m_checker_white_side_bmp;
	private Bitmap m_checker_black_side_bmp;
	private int m_Left, m_Top;

	private int[] m_NestsY;
	private int m_CheckerSize;
	private int m_CheckerSideSize;
	private int m_MinY;
	private int m_MaxY;
	private int m_MinBarX;
	private int m_MaxBarX;
	private int m_CubeMiddleY;
	private int m_CubeBlackY;
	private int m_CubeWhiteY;

	private int m_CubeMiddleX;

	private int m_CubeBlackX;
	private int m_CubeWhiteX;
	private int m_CubeRightX;
	private int m_CubeLeftX;

	private void initBitmaps()
	{
	    m_checker_black_bmp = BitmapFactory.decodeResource(getResources(),
		    R.drawable.checker_black);
	    m_checker_white_bmp = BitmapFactory.decodeResource(getResources(),
		    R.drawable.checker_white);
	    m_checker_black_side_bmp = BitmapFactory.decodeResource(
		    getResources(), R.drawable.checker_black_side);
	    m_checker_white_side_bmp = BitmapFactory.decodeResource(
		    getResources(), R.drawable.checker_white_side);
	    m_cube_bmp_2 = BitmapFactory.decodeResource(getResources(),
		    R.drawable.cube_2);
	    m_cube_bmp_4 = BitmapFactory.decodeResource(getResources(),
		    R.drawable.cube_4);
	    m_cube_bmp_8 = BitmapFactory.decodeResource(getResources(),
		    R.drawable.cube_8);
	    m_cube_bmp_16 = BitmapFactory.decodeResource(getResources(),
		    R.drawable.cube_16);
	    m_cube_bmp_32 = BitmapFactory.decodeResource(getResources(),
		    R.drawable.cube_32);
	    m_cube_bmp_64 = BitmapFactory.decodeResource(getResources(),
		    R.drawable.cube_64);
	}

	private Bitmap getCubeBitmap(int index)
	{
	    switch (index)
	    {
	    case 2:
		return m_cube_bmp_2;
	    case 4:
		return m_cube_bmp_4;
	    case 8:
		return m_cube_bmp_8;
	    case 16:
		return m_cube_bmp_16;
	    case 32:
		return m_cube_bmp_32;
	    case 1:
	    case 64:
		return m_cube_bmp_64;
	    }

	    return null;
	}

	private void LoadHdpiValues()
	{
	    m_NestsY = new int[] { 0, 36, 72, 108, 144, 180, 257, 293, 329,
		    365, 401, 437,
		    /* BAR */218,
		    /* OUT */-52 };
	    m_CheckerSize = 36;
	    m_CheckerSideSize = 11;
	    m_MinY = 53;
	    m_MaxY = 427;
	    m_MinBarX = 226;
	    m_MaxBarX = 257;
	    m_CubeMiddleY = 187;
	    m_CubeBlackY = 47;
	    m_CubeWhiteY = 325;

	    m_CubeMiddleY = m_NestsY[12] + 5;
	    m_CubeMiddleX = 230;
	    m_CubeBlackX = 7;
	    m_CubeWhiteX = 7;
	    m_CubeRightX = m_NestsY[9] - 15;
	    m_CubeLeftX = m_NestsY[2] + 12;
	}

	private void LoadLdpiValues()
	{
	    m_NestsY = new int[] { 0, 41, 82, 123, 164, 205, 287, 328, 369,
		    410, 451,
		    /* BAR */246, /* OUT */630 };

	    m_CheckerSize = 14;
	    m_MinY = 29;
	    m_MaxY = 212;
	    m_MinBarX = 111;
	    m_MaxBarX = 130;
	    m_CubeMiddleY = 112;
	    m_CubeBlackY = 29;
	    m_CubeWhiteY = 220;

	    m_CubeMiddleX = m_NestsY[12] - 3;
	    m_CubeBlackX = 7;
	    m_CubeWhiteX = 7;
	    m_CubeRightX = m_NestsY[9] - 15;
	    m_CubeLeftX = m_NestsY[2] + 12;
	}

	public BoardView(int left, int top, AssetManager assets)
	{
	    DisplayMetrics dm = new DisplayMetrics();
	    getWindowManager().getDefaultDisplay().getMetrics(dm);

	    if (dm.heightPixels <= 240 || dm.widthPixels <= 240)
	    {
		LoadLdpiValues();
	    } else
	    {
		LoadHdpiValues();
	    }

	    m_Left = left;
	    m_Top = top;
	    initBitmaps();
	}

	public void drawBoard(Canvas canvas, int[] checkers)
	{

	    if (m_current_position == null)
		return;

	    // canvas.rotate(90, 200, 200);
	    // canvas.translate(100, -200);

	    canvas.translate(m_Left, m_Top);

	    // for (int i = 1; i < 10; i++)
	    // {
	    // canvas.drawBitmap(m_checker_white_bmp, i * m_CheckerSize, 0,
	    // null);
	    // canvas.drawBitmap(m_checker_black_bmp, 1, i * m_CheckerSize,
	    // null);
	    // }
	    int y = 0;
	    int x = 0;

	    // рисуем фишки на доске
	    for (int i = 1; i <= 24; i++)
	    {

		x = (i >= 13) ? m_MinY : m_MaxY - m_CheckerSize;

		y = (i < 13) ? m_NestsY[(i - 1) % 12]
			: m_NestsY[11 - (i - 1) % 12];

		Bitmap checker = checkers[i] > 0 ? m_checker_black_bmp
			: m_checker_white_bmp;

		int checkersCount = Math.abs(checkers[i]);

		DrawCheckersNest(canvas, checker, checkersCount, x, y, i < 13);

	    }

	    // рисуем фишки на баре
	    y = m_NestsY[12];

	    for (int j = 0; j < checkers[25]; j++)
	    {
		canvas.drawBitmap(m_checker_white_bmp, m_MinBarX - j
			* m_CheckerSize - m_CheckerSize, y, null);
	    }

	    for (int j = 0; j > checkers[0]; j--)
	    {
		canvas.drawBitmap(m_checker_black_bmp, m_MaxBarX - j
			* m_CheckerSize, y, null);
	    }

	    // рисуем фишки на выбросе
	    x = m_MaxY - m_CheckerSideSize;

	    for (int j = 0; j < checkers[26]; j++)
	    {
		canvas.drawBitmap(m_checker_white_side_bmp, x - j
			* m_CheckerSideSize - j / 5 * 5, m_NestsY[13], null);
	    }

	    x = m_MinY;

	    for (int j = 0; j > checkers[27]; j--)
	    {
		canvas.drawBitmap(m_checker_black_side_bmp, x - j
			* m_CheckerSideSize - j / 5 * 5, m_NestsY[13], null);
	    }
	    canvas.translate(-m_Left, -m_Top);

	}

	public void DrawCheckersNest(Canvas canvas, Bitmap checker,
		int checkersCount, int nestX, int nestY, boolean direction)
	{
	    int max_checkers = 5;

	    int checkers_on_line = 0;
	    int shift = 0;
	    int mult = direction ? -1 : 1;

	    for (int j = 0; j < checkersCount; j++)
	    {
		if (checkers_on_line >= max_checkers)
		{
		    checkers_on_line = 0;
		    shift = m_CheckerSize / 2;
		    max_checkers--;
		}

		int posX = nestX + mult
			* (shift + checkers_on_line * m_CheckerSize);

		canvas.drawBitmap(checker, posX, nestY, null);
		checkers_on_line++;
	    }
	}

	public void drawCube(Canvas canvas, int cubeValue, int cubePosition)
	{
	    if (m_current_position != null)
	    {
		int x = 0;
		int y = 0;
		int cube_value = m_current_position.getCubeValue();
		switch (m_current_position.getCubePosition())
		{
		case Position.CUBE_CENTER:
		    x = m_CubeMiddleX;
		    y = m_CubeMiddleY;
		    break;
		case Position.CUBE_CRAWFORD:
		    return;
		case Position.CUBE_BLACK:
		    x = m_CubeBlackX;
		    y = m_CubeBlackY;
		    break;
		case Position.CUBE_WHITE:
		    x = m_CubeWhiteX;
		    y = m_CubeWhiteY;
		    break;
		case Position.CUBE_RIGHT:
		    x = m_CubeRightX;
		    y = m_CubeMiddleY;
		    break;
		case Position.CUBE_LEFT:
		    x = m_CubeLeftX;
		    y = m_CubeMiddleY;
		    break;
		default:
		    return;
		}
		canvas.translate(m_Left, m_Top);
		// canvas.rotate(angle);
		canvas.drawBitmap(getCubeBitmap(cube_value), x, y, null);
		canvas.translate(-m_Left, -m_Top);
	    }
	}

    }

    private class PlayView extends View
    {

	private Bitmap m_Background;
	private BoardView m_BoardView;

	public PlayView(Context context)
	{
	    super(context);
	    initBitmaps();
	    m_BoardView = new BoardView(0, 286, context.getAssets());
	}

	private void initBitmaps()
	{
	    m_Background = BitmapFactory.decodeResource(getResources(),
		    R.drawable.bckg3bg_800x480);
	}

	protected void onDraw(Canvas canvas)
	{

	    canvas.drawBitmap(m_Background, 0, 0, null);

	    // Players names
	    DrawingUtils.drawText(canvas, m_MatchParameters.Player1, 32, 670,
		    22, 90, 0xFFA30101, "fonts/OpiumB.TTF");
	    DrawingUtils.drawText(canvas, m_MatchParameters.Player2, 448, 670,
		    22, 270, 0xFF5b2b0a, "fonts/OpiumB.TTF");

	    // Game conditions
	    DrawingUtils.drawText(canvas,
		    Integer.toString(m_MatchParameters.MatchLength), 240, 98,
		    48, 0, 0xFF302f2f, "fonts/OpiumB.TTF");
	    // Players scores
	    DrawingUtils.drawText(canvas, "0", 140, 98, 48, 0, 0xFFA30101,
		    "fonts/OpiumB.TTF");
	    DrawingUtils.drawText(canvas, "0", 340, 98, 48, 0, 0xFF5b2b0a,
		    "fonts/OpiumB.TTF");

	    // DEBUG version
	    // int[] _checkers = new int[] {-3,
	    // 5, 6, 7, 8, 9, 10, //
	    // 11, 12, 13, 14, 15, 0, //
	    // -4, -5, -6, -7, -8, -9, //
	    // -10,-11,-12,-13,-14,-15, //
	    // 3,
	    // 15, -1 };
	    // m_BoardView.drawBoard(canvas, _checkers);

	    if (m_current_position != null)
	    {
		// release version
		m_BoardView.drawBoard(canvas, m_current_position.getCheckers());

		m_BoardView.drawCube(canvas, m_current_position.getCubeValue(),
			m_current_position.getCubePosition());
	    }

	    /*
	     * Rect rect = new Rect(100, 100, 300, 150); Paint bar_paint = new
	     * Paint(); // параметры рисования основного // прямоугольника
	     * bar_paint.setShadowLayer(3, 3, 3, 0xBF000000);
	     * bar_paint.setColor(0xFFCAC8A9); canvas.drawRect(rect, bar_paint);
	     */
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

    public void setPosition(Position position)
    {
	m_current_position = position;
	if (position == null)
	    BezmaLog.i("POS", "EMPTY");
	else
	    BezmaLog.i("POS", position.toString());
	invalidate();
	// view.invalidate();
    }

    public void drawCube(Canvas canvas, int cubeValue, int cubePosition)
    {
	// TODO Auto-generated method stub

    }

    public void setMatchParameters(MatchParameters params)
    {
	/* _match_parameters = params; */
    }

    private void invalidate()
    {
	runOnUiThread(new Runnable()
	{

	    @Override
	    public void run()
	    {
		m_view.invalidate();
	    }
	});
    }

    @Override
    public boolean start()
    {
	return true;
    }

    @Override
    public void stop()
    {
    }

    @Override
    public void notifyObservers(CommunicationCommand aCommand)
    {
	for (ICommandObserver observer : m_observers)
	{
	    observer.handeEvent(aCommand);
	}
    }

    @Override
    public void addObserver(ICommandObserver aCommandObserver)
    {
	m_observers.add(aCommandObserver);
    }

    @Override
    public void removeObserver(ICommandObserver aCommandObserver)
    {
	m_observers.remove(aCommandObserver);
    }

    @Override
    public void setModelState(ModelSituation aModelState)
    {

	if (aModelState != null && !aModelState.isErrorState())
	{
	    BezmaLog.i("SET MODEL STATE", aModelState.getPosition().toString());
	    setPosition(aModelState.getPosition());
	}
    }

    @Override
    public void sendCommand(CommunicationCommand command)
    {
	// do nothing to send datagram
    }

    @Override
    public void appendMove(IMove move)
    {
	String moveString = MovePrinter.printMove(move);
	
	showErrorMessage(moveString);
    }
}