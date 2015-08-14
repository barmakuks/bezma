package com.fairbg.bezma.qui;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.fairbg.bezma.R;
import com.fairbg.bezma.bluetooth.StateDatagram;
import com.fairbg.bezma.communication.IModelView;
import com.fairbg.bezma.communication.IRawDataView;
import com.fairbg.bezma.communication.commands.CommunicationCommand;
import com.fairbg.bezma.communication.commands.ICommandObserver;
import com.fairbg.bezma.core.IConfigurator;
import com.fairbg.bezma.core.MatchParameters;
import com.fairbg.bezma.core.Presenter;
import com.fairbg.bezma.core.backgammon.MovePrinter;
import com.fairbg.bezma.core.backgammon.Position;
import com.fairbg.bezma.core.backgammon.Position.Direction;
import com.fairbg.bezma.core.errors.Error;
import com.fairbg.bezma.core.errors.ErrorWrongPosition;
import com.fairbg.bezma.core.model.MatchScore;
import com.fairbg.bezma.core.model.MoveAbstract;
import com.fairbg.bezma.core.model.BoardContext;
import com.fairbg.bezma.core.model.PlayerId;
import com.fairbg.bezma.log.BezmaLog;
import com.fairbg.bezma.unit_tests.Runner;
import com.fairbg.bezma.unit_tests.TestConfigurator;
import com.fairbg.bezma.unit_tests.TestModelCommandsProvider;
import com.fairbg.bezma.version3.ConfigurationVer3;
import com.fairbg.bezma.version3.ConfiguratorVer3;

/**
 * Окно отображения состояния матча на экране должно уметь реагировать на команды пользователя и отправлять их слушателю
 * */
public class PlayActivity extends Activity implements IModelView, IRawDataView
{

    private Presenter                   m_Presenter        = null;

    private PlayView                    m_view;

    private ArrayList<ICommandObserver> m_observers        = new ArrayList<ICommandObserver>();
    private Position                    m_current_position = null;
    private Object                      m_lastRawData      = null;
    private ProgressDialog              pDialog;
    private String                      m_DeviceMAC        = "";
    private MatchParameters             m_MatchParameters  = null;
    private MovePrinter                 m_movePrinter      = null;

    /** Initialize activity for receiving test commands from TestCommandProvider */
    protected void initDebug()
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ConfigurationVer3 configuration = (ConfigurationVer3)
                (getIntent().getExtras().getSerializable(ConfigurationVer3.class.getCanonicalName()));

        m_MatchParameters = configuration.getMatchParameters();
        m_movePrinter = new MovePrinter(m_MatchParameters);


        configuration.configureMatchParameters(m_MatchParameters);

        IConfigurator configurator = new TestConfigurator();

        m_Presenter = new Presenter(configurator, configuration);

        IModelView commandsProvider = new TestModelCommandsProvider(Runner.getDatagrams(), 2000);

        m_Presenter.addView(commandsProvider);
        m_Presenter.addView(this);

        initView();

        DrawingUtils.setAssetManager(this.getAssets());

        m_Presenter.start();

    }

    /** Initialize activity for receiving commands from Bluetooth device */
    protected void initRelease()
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ConfigurationVer3 configuration = (ConfigurationVer3)
                (getIntent().getExtras().getSerializable(ConfigurationVer3.class.getCanonicalName()));

        m_DeviceMAC = configuration.getUserSettings().boardMAC;
        m_MatchParameters = configuration.getMatchParameters();
        m_movePrinter = new MovePrinter(m_MatchParameters);

        ConfiguratorVer3 configurator = new ConfiguratorVer3();

        m_Presenter = new Presenter(configurator, configuration);

        initView();

        m_Presenter.addView(this);

        DrawingUtils.setAssetManager(this.getAssets());

        startPresenter();
    }

    @Override
    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);

        // Use only one of initDebug() or initRelease() functions
//        initDebug();
        initRelease();
    }

    final int DEVICE_CONNECTION_OK    = 1;
    final int DEVICE_CONNECTION_ERROR = 0;

    public void startPresenter()
    {
        pDialog = ProgressDialog.show(this, "Connecting to " + m_DeviceMAC + "...", "Please wait", true, true);

        Thread thread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                int result = m_Presenter.start() ? DEVICE_CONNECTION_OK : DEVICE_CONNECTION_ERROR;

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
        int duration = Toast.LENGTH_SHORT;
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

    private class DrawPrimitives
    {

        public final Bitmap background;

        public final Bitmap cube_bmp_2;
        public final Bitmap cube_bmp_4;
        public final Bitmap cube_bmp_8;
        public final Bitmap cube_bmp_16;
        public final Bitmap cube_bmp_32;
        public final Bitmap cube_bmp_64;

        public final Bitmap checker_black_bmp;
        public final Bitmap checker_white_bmp;
        public final Bitmap checker_white_side_bmp;
        public final Bitmap checker_black_side_bmp;

        private DrawPrimitives(int screen_size)
        {
            if (screen_size >= 1024)
            {
                background = BitmapFactory.decodeResource(getResources(), R.drawable.bg_m);
                checker_black_bmp = BitmapFactory.decodeResource(getResources(), R.drawable.checker_black_m);
                checker_white_bmp = BitmapFactory.decodeResource(getResources(), R.drawable.checker_white_m);
                checker_black_side_bmp = BitmapFactory.decodeResource(getResources(), R.drawable.checker_black_side_m);
                checker_white_side_bmp = BitmapFactory.decodeResource(getResources(), R.drawable.checker_white_side_m);
                cube_bmp_2 = BitmapFactory.decodeResource(getResources(), R.drawable.cube_2_m);
                cube_bmp_4 = BitmapFactory.decodeResource(getResources(), R.drawable.cube_4_m);
                cube_bmp_8 = BitmapFactory.decodeResource(getResources(), R.drawable.cube_8_m);
                cube_bmp_16 = BitmapFactory.decodeResource(getResources(), R.drawable.cube_16_m);
                cube_bmp_32 = BitmapFactory.decodeResource(getResources(), R.drawable.cube_32_m);
                cube_bmp_64 = BitmapFactory.decodeResource(getResources(), R.drawable.cube_64_m);
            }
            else
            {
                background = BitmapFactory.decodeResource(getResources(), R.drawable.bg_m);
                checker_black_bmp = BitmapFactory.decodeResource(getResources(), R.drawable.checker_black_m);
                checker_white_bmp = BitmapFactory.decodeResource(getResources(), R.drawable.checker_white_m);
                checker_black_side_bmp = BitmapFactory.decodeResource(getResources(), R.drawable.checker_black_side_m);
                checker_white_side_bmp = BitmapFactory.decodeResource(getResources(), R.drawable.checker_white_side_m);
                cube_bmp_2 = BitmapFactory.decodeResource(getResources(), R.drawable.cube_2_m);
                cube_bmp_4 = BitmapFactory.decodeResource(getResources(), R.drawable.cube_4_m);
                cube_bmp_8 = BitmapFactory.decodeResource(getResources(), R.drawable.cube_8_m);
                cube_bmp_16 = BitmapFactory.decodeResource(getResources(), R.drawable.cube_16_m);
                cube_bmp_32 = BitmapFactory.decodeResource(getResources(), R.drawable.cube_32_m);
                cube_bmp_64 = BitmapFactory.decodeResource(getResources(), R.drawable.cube_64_m);
            }
        }
    };

    private class DrawDimensions
    {
        public final int    left;
        public final int    top;

        public final int[]  nestsY;
        public final int    checkerSize;
        public final int    checkerSideSize;
        public final int    minY;
        public final int    maxY;
        public final int    minBarX;
        public final int    maxBarX;
        public final int    cubeMiddleY;
        public final int    cubeMiddleX;

        public final int    cubeSouthX;
        public final int    cubeNorthX;
        public final int    cubeEastY;
        public final int    cubeWestY;

        private DrawDimensions(int screen_size)
        {
            top = 0;
            left = 250;
            nestsY = new int[] { 0, 36, 72, 108, 144, 180, 257, 293, 329, 365, 401, 437,
                    /* BAR */218,
                    /* OUT */-52 };
            checkerSize = 36;
            checkerSideSize = 11;
            minY = 53;
            maxY = 427;
            minBarX = 226;
            maxBarX = 257;

            cubeMiddleX = 230;
            cubeMiddleY = nestsY[12] + 5;

            cubeSouthX = 365;
            cubeNorthX = 90;

            cubeEastY = 90;
            cubeWestY = 350;
        }
    };

    private class BoardView
    {
        private final DrawPrimitives m_drawPrimitives;
        private final DrawDimensions m_drawDimensions;

        private Bitmap getCubeBitmap(int index)
        {
            switch (index)
            {
            case 2:
                return m_drawPrimitives.cube_bmp_2;
            case 4:
                return m_drawPrimitives.cube_bmp_4;
            case 8:
                return m_drawPrimitives.cube_bmp_8;
            case 16:
                return m_drawPrimitives.cube_bmp_16;
            case 32:
                return m_drawPrimitives.cube_bmp_32;
            case 1:
            case 64:
                return m_drawPrimitives.cube_bmp_64;
            }

            return null;
        }


        public BoardView(DrawPrimitives drawPrimitives, DrawDimensions drawDimensions)
        {
            m_drawPrimitives = drawPrimitives;
            m_drawDimensions = drawDimensions;
        }

        public void drawBoard(Canvas canvas, int[] checkers)
        {

            if (m_current_position == null)
            {
                return;
            }

            canvas.translate(m_drawDimensions.left, m_drawDimensions.top);

            int y = 0;
            int x = 0;

            // draw checkers on board
            for (int i = 1; i <= 24; i++)
            {
                x = (i < 13) ? m_drawDimensions.minY : m_drawDimensions.maxY - m_drawDimensions.checkerSize;
                y = (i >= 13) ? m_drawDimensions.nestsY[(i - 1) % 12] : m_drawDimensions.nestsY[11 - (i - 1) % 12];
                Bitmap checker = checkers[i] > 0 ? m_drawPrimitives.checker_black_bmp : m_drawPrimitives.checker_white_bmp;
                int checkersCount = Math.abs(checkers[i]);
                DrawCheckersNest(canvas, checker, checkersCount, x, y, i >= 13);
            }

            // draw checkers on bar
            y = m_drawDimensions.nestsY[12];

            for (int j = 0; j > checkers[25]; j--)
            {
                if (m_current_position.getDirection() == Direction.GrayCCW 
                    || m_current_position.getDirection() == Direction.GrayCW)
                {
                    canvas.drawBitmap(m_drawPrimitives.checker_white_bmp, m_drawDimensions.minBarX + j * m_drawDimensions.checkerSize - m_drawDimensions.checkerSize, y, null);
                }
                else
                {
                    canvas.drawBitmap(m_drawPrimitives.checker_white_bmp, m_drawDimensions.maxBarX - j * m_drawDimensions.checkerSize, y, null);
                }
                
            }

            for (int j = 0; j < checkers[0]; j++)
            {
                if (m_current_position.getDirection() == Direction.GrayCCW 
                        || m_current_position.getDirection() == Direction.GrayCW)
                {
                    canvas.drawBitmap(m_drawPrimitives.checker_black_bmp, m_drawDimensions.maxBarX + j * m_drawDimensions.checkerSize, y, null);
                }
                else
                {
                    canvas.drawBitmap(m_drawPrimitives.checker_black_bmp, m_drawDimensions.minBarX - j * m_drawDimensions.checkerSize - m_drawDimensions.checkerSize, y, null);
                }    
            }

            // draw checkers on off-side
            x = m_drawDimensions.maxY - m_drawDimensions.checkerSideSize;

            for (int j = 0; j < checkers[26]; j++)
            {
                canvas.drawBitmap(m_drawPrimitives.checker_white_side_bmp, x - j * m_drawDimensions.checkerSideSize - j / 5 * 5, m_drawDimensions.nestsY[13], null);
            }

            x = m_drawDimensions.minY;

            for (int j = 0; j > checkers[27]; j--)
            {
                canvas.drawBitmap(m_drawPrimitives.checker_black_side_bmp, x - j * m_drawDimensions.checkerSideSize - j / 5 * 5, m_drawDimensions.nestsY[13], null);
            }
            canvas.translate(-m_drawDimensions.left, -m_drawDimensions.top);
        }

        public void DrawCheckersNest(Canvas canvas, Bitmap checker, int checkersCount, int nestX, int nestY, boolean direction)
        {
            int max_checkers = 5;

            int checkers_on_line = 0;
            int shift = 0;
            final int mult = direction ? -1 : 1;

            for (int j = 0; j < checkersCount; j++)
            {
                if (checkers_on_line >= max_checkers)
                {
                    checkers_on_line = 0;
                    shift = m_drawDimensions.checkerSize / 2;
                    max_checkers--;
                }

                int posX = nestX + mult * (shift + checkers_on_line * m_drawDimensions.checkerSize);

                canvas.drawBitmap(checker, posX, nestY, null);
                checkers_on_line++;
            }
        }

        public void drawCube(Canvas canvas, int cubeValue, Position.CubePosition cubePosition)
        {
            if (m_current_position != null)
            {
                int x = 0;
                int y = 0;

                switch (cubePosition)
                {
                case None:
                    return;
                case Center:
                    x = m_drawDimensions.cubeMiddleX;
                    y = m_drawDimensions.cubeMiddleY;
                    break;
                case North:
                    x = m_drawDimensions.cubeNorthX;
                    y = m_drawDimensions.cubeMiddleY;
                    break;
                case South:
                    x = m_drawDimensions.cubeSouthX;
                    y = m_drawDimensions.cubeMiddleY;
                    break;
                case East:
                    x = m_drawDimensions.cubeMiddleX;
                    y = m_drawDimensions.cubeEastY;
                    break;
                case West:
                    x = m_drawDimensions.cubeMiddleX;
                    y = m_drawDimensions.cubeWestY;
                    break;
                default:
                    return;
                }

                canvas.translate(m_drawDimensions.left, m_drawDimensions.top);
                // canvas.rotate(angle);
                canvas.drawBitmap(getCubeBitmap(cubeValue), x, y, null);
                canvas.translate(-m_drawDimensions.left, -m_drawDimensions.top);
            }
        }

        public void drawRawData(Canvas canvas, Object m_lastRawData)
        {
            if (m_lastRawData instanceof StateDatagram)
            {
                StateDatagram stateDatagram = (StateDatagram) m_lastRawData;
                byte[] rawData = stateDatagram.getRawData();
                byte[] positions = new byte[150];
                
                for (int i = 7; i < rawData.length; i += 2)
                {
                    int index = rawData[i - 1] < 0 ? rawData[i - 1] + 256 : rawData[i - 1];
                    positions[index] = rawData[i];
                }
                
                final float size = 2f;
                final int gapX = 36;
                final int gapY = 36;
                int white = 0xFFFFFFFF;
                int black = 0XFF00FF00;
                
                Paint paint = new Paint();
               
                for (int i = 0; i < 30; ++i)
                {
                    if (positions[i] != 0)
                    {
                        paint.setColor((positions[i] == 2) ? white : black);
                        canvas.drawCircle(70 + (i % 5) * gapX, 740 - (i / 5) * gapY, size, paint);
                    }
                }
                
                for (int i = 32; i < 62; ++i)
                {
                    if (positions[i] != 0)
                    {
                        int index = i - 32;
                        paint.setColor((positions[i] == 2) ? white : black);
                        canvas.drawCircle(408 - (index % 5) * gapX, 740 - (index / 5) * gapY, size, paint);
                    }
                }

                for (int i = 64; i < 94; ++i)
                {
                    if (positions[i] != 0)
                    {
                        int index = i - 64;
                        paint.setColor((positions[i] == 2) ? white : black);
                        canvas.drawCircle(70 + (index % 5) * gapX, 303 + (index / 5) * gapY, size, paint);
                    }
                }

                for (int i = 96; i < 126; ++i)
                {
                    if (positions[i] != 0)
                    {
                        int index = i - 96;
                        paint.setColor((positions[i] == 2) ? white : black);
                        canvas.drawCircle(408 - (index % 5) * gapX, 303 + (index / 5) * gapY, size, paint);
                    }
                }
                
                if (positions[129] != 0)
                {
                    paint.setColor((positions[129] == 2) ? white : black);
                    canvas.drawCircle(134, 522, size, paint);
                }
                if (positions[130] != 0)
                {
                    paint.setColor((positions[130] == 2) ? white : black);
                    canvas.drawCircle(167, 522, size, paint);
                }
                if (positions[131] != 0)
                {
                    paint.setColor((positions[131] == 2) ? white : black);
                    canvas.drawCircle(200, 522, size, paint);
                }

                if (positions[132] != 0)
                {
                    paint.setColor((positions[132] == 2) ? white : black);
                    canvas.drawCircle(283, 522, size, paint);
                }
                if (positions[133] != 0)
                {
                    paint.setColor((positions[133] == 2) ? white : black);
                    canvas.drawCircle(316, 522, size, paint);
                }
                if (positions[134] != 0)
                {
                    paint.setColor((positions[134] == 2) ? white : black);
                    canvas.drawCircle(349, 522, size, paint);
                }

                if (positions[135] != 0)
                {
                    paint.setColor(0xFF00FFFF);
                    canvas.drawCircle(104, 522, size, paint);
                }
                if (positions[136] != 0)
                {
                    paint.setColor(0xFF00FFFF);
                    canvas.drawCircle(241, 522, size, paint);
                }
                if (positions[137] != 0)
                {
                    paint.setColor(0xFF00FFFF);
                    canvas.drawCircle(378, 522, size, paint);
                }
            }
        }

    }

    private class PlayView extends View
    {
        private final DrawPrimitives m_drawPrimitives;

        private BoardView m_BoardView;

        public int        m_silverScore = 0;
        public int        m_redScore = 0;

        public PlayView(Context context)
        {
            super(context);
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);

            m_drawPrimitives = new DrawPrimitives(dm.heightPixels);

            m_BoardView = new BoardView(m_drawPrimitives, new DrawDimensions(dm.heightPixels));
        }

        private Direction m_lastDirection = Direction.None;
        
        protected void onDraw(Canvas canvas)
        {
            canvas.drawBitmap(m_drawPrimitives.background, 0, 0, null);

            String redPips = "", silverPips = "";

            
            if (m_current_position != null
                    && m_current_position.getDirection() != Direction.None
                    && m_lastDirection != m_current_position.getDirection())
            {
                m_lastDirection = m_current_position.getDirection();
            }

            if (m_current_position != null)
            {
                redPips = " : " + m_current_position.getPips(PlayerId.RED);
                silverPips = " : " + m_current_position.getPips(PlayerId.SILVER);
            }

            String bottomPlayerName = m_MatchParameters.redPlayerName + redPips;
            String topPlayerName = m_MatchParameters.silverPlayerName + silverPips;
            String bottomScore = Integer.toString(m_redScore);
            String topScore = Integer.toString(m_silverScore);
            
            if (m_lastDirection == Direction.GrayCCW || m_lastDirection == Direction.GrayCW)
            {
                bottomPlayerName = m_MatchParameters.silverPlayerName + silverPips;
                topPlayerName = m_MatchParameters.redPlayerName + redPips;
                bottomScore = Integer.toString(m_silverScore);
                topScore = Integer.toString(m_redScore);

            }

            // player names
            DrawingUtils.drawText(canvas, bottomPlayerName, 32, 670, 22, 90, 0xFF5b2b0a, "fonts/OpiumB.TTF");
            DrawingUtils.drawText(canvas, topPlayerName, 448, 670, 22, 270, 0xFFA30101, "fonts/OpiumB.TTF");
            // Players scores
            DrawingUtils.drawText(canvas, bottomScore, 140, 78, 48, 0, 0xFF5b2b0a, "fonts/OpiumB.TTF");
            DrawingUtils.drawText(canvas, topScore, 340, 78, 48, 0, 0xFFA30101, "fonts/OpiumB.TTF");

            // Game conditions
            DrawingUtils.drawText(canvas, Integer.toString(m_MatchParameters.matchLength), 240, 78, 48, 0, 0xFF302f2f, "fonts/OpiumB.TTF");

            if (m_current_position != null)
            {
                m_BoardView.drawBoard(canvas, m_current_position.getCheckers());

                m_BoardView.drawCube(canvas, m_current_position.getCubeValue(), m_current_position.getCubePosition());
            }
            
            if (m_lastRawData != null)
            {
                m_BoardView.drawRawData(canvas, m_lastRawData);
            }
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
    }
    
    public void setRawData(Object rawData)
    {
        m_lastRawData = rawData;
        invalidate();
    }

    private void invalidate()
    {
        runOnUiThread(m_view::invalidate);
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
    public void setModelState(BoardContext aModelState)
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
    public void appendMove(final MoveAbstract move)
    {
        if (m_movePrinter != null)
        {
            runOnUiThread(() -> {
                String moveString = m_movePrinter.printMove(move);
                showErrorMessage(moveString);
            });
        }
    }

    @Override
    public void displayError(Error error)
    {
        if (error instanceof ErrorWrongPosition)
        {
            // ErrorWrongPosition position_error = (ErrorWrongPosition) error;
            // Position wrong_position = position_error.position;
        }
    }

    @Override
    public void changeScore(MatchScore score)
    {
        m_view.m_redScore = score.getPlayerScore(PlayerId.RED);
        m_view.m_silverScore = score.getPlayerScore(PlayerId.SILVER);
    }
}