package asia.takezoo.layoutlessons;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.Date;
import java.util.TimerTask;

//timer

public class MainActivity extends ActionBarActivity {
    int num = 0;
    int heighest=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //timer
        //super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_my);

        timerText = (TextView) findViewById(R.id.timerText);
        startButton = (Button) findViewById(R.id.startButton);
        stopButton = (Button) findViewById(R.id.stopButton);
       resetButton = (Button) findViewById(R.id.resetButton);

        setButtonState(true, false,false);
    }

    public void getCount(View view) {
        TextView tv = (TextView) findViewById(R.id.num);
        if (num % 10 == 9 && num != 1) {
            String Message = "10の倍数!!";
            tv.setText(Message);
            num += 1;
        } else {
            num += 1;
            String numString = String.valueOf(num);
            tv.setText(numString);
        }
    }

    public void clearCount(View view) {
        TextView tv = (TextView) findViewById(R.id.num);

        num = 0;
        String numString = String.valueOf(num);
        tv.setText(numString);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //timer
    private long startTime;
    private long stopTime;
    private long timelimit;

    private Timer timer = null;
    private Handler handler = new Handler();
    private boolean isRunning = false;
    private long delta = 0l;

    private TextView timerText;
    private Button startButton;
    private Button stopButton;
    private Button resetButton;

    private void setButtonState(boolean start, boolean stop,boolean reset) {
        startButton.setEnabled(start);
        stopButton.setEnabled(stop);
        resetButton.setEnabled(reset);
    }

    public void startTimer(View v) {

        if (!isRunning) {
            startTime = SystemClock.elapsedRealtime();
            isRunning = true;
        } else {
            delta += SystemClock.elapsedRealtime() - stopTime;
        }

        // startボタンをおした時の時刻を取得 - startTime
        // startTime = SystemClock.elapsedRealtime(); // 起動してからの経過ミリ秒: タイマー
        // startTime = System.currentTimeMillis(); // 現在時刻を取得: カレンダー/日付

        // 一定時間ごとに現在時刻を取得してstartTimeとの差分を表示
        /*
        Timerクラス
        - scheduleAtFixedRate()
        -- TimerTask 抽象クラス
        --- run()

        UI描画=シングルスレッド(CPUの処理単位)

        Handler
        - post()
        -- Runnable Interface
        --- run()
         */
        timer = new Timer(true); // ユーザースレッド/デーモンスレッド
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //SimpleDateFormat sdf = new SimpleDateFormat("mm:ss.SSS");
                        SimpleDateFormat sdf = new SimpleDateFormat("ss");
                        String result = sdf.format(new Date(SystemClock.elapsedRealtime() - startTime - delta));

                        int i = -Integer.parseInt(result)+10;
                        if(i<0) {
                            if (heighest >num) {
                                timerText.setText("最高記録ならず");
                            }else {
                                timerText.setText("最高記録");
                            }

                        }else {
                            timerText.setText(String.valueOf(i));
                        }
                    }
                });
            }
        }, 0, 10);

        // ボタンの有効・無効化
        setButtonState(false, true,false);
    }
    public void stopTimer(View v) {
        stopTime = SystemClock.elapsedRealtime();
        timer.cancel(); // Timerのインスタンスは破棄される
        setButtonState(true, false,true);
    }

    public void resetTimer(View v) {
        isRunning = false;
        delta = 0l;
        timerText.setText("制限時間");
        setButtonState(true, false, false);
    }



}





