package com.polkapolka.bluetooth.le;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.polkapolka.bluetooth.le.DeviceControlActivity;


/**
 * Created by crypto_lab on 2017-06-10.
 */

public class CheckActivity extends Activity {

    protected int mCount;
    private TextView _text;
    private TextView _count;
    private CountDownTimer _timer;
    private DeviceControlActivity DCA = new DeviceControlActivity();
    private byte[] Val;
    private BluetoothLeService mBLES = new BluetoothLeService();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        _text = (TextView) findViewById(R.id.tvMsg);
        _count = (TextView) findViewById(R.id.countMsg);

        _timer = new CountDownTimer(60 * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                _text.setText("시간 = " + millisUntilFinished/1000);
                int lec = 0;
                boolean instance = false;
                int count = 0;

                while( millisUntilFinished < 60000){
                    if(lec > 250 && !instance ){
                        instance = true;
                        count ++;
                    }
                    else if(lec < 150){
                        instance = false;
                    }


                }
                //	if msg.obj > 250(top) && !instance(전역변수)
                //        instance == true;
                //		  count++;
                //  elsif msg.obj < 150(mid)
                //        instance == false;

                //count start
            }

            public void onFinish() {
                _text.setText("finshed");
                //count finish -> database에 넣기
            }
        };

        Button btnStart = (Button) findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                _timer.start();
                //_count,start();
                DCA.SitUpRequest();
            }
        });

        Button btnEnd = (Button) findViewById(R.id.btnStop);
        btnEnd.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                _timer.cancel();
            }
        });
    }

    private int count()
    {
        int count = 0;
        int i = 0;
        byte[] receive = mBLES.get_val();


        int rec; // 바꿨다
            // 알고리즘

        _count.setText("개수 = " + count);


        return count;

    }


}
