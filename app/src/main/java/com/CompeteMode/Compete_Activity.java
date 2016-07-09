package com.CompeteMode;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.laserGun.MainActivity;
import com.laserGun.R;
import com.uidata.CommonData;

public class Compete_Activity extends Activity {
    private TextView tv_commonmode;
    private TextView tv_progressmode;
    private TextView tv_challengemode;
    private TextView tv_return;
    private TextView tv_set;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.compete_main);
        tv_commonmode = (TextView)findViewById(R.id.tv_common);
        tv_progressmode = (TextView)findViewById(R.id.tv_progress);
        tv_challengemode = (TextView)findViewById(R.id.tv_challenge);
        tv_return = (TextView)findViewById(R.id.tv_compete_return);
        tv_set = (TextView)findViewById(R.id.tv_competeset);
        tv_commonmode.setOnTouchListener(new TouchListener(1));
        tv_progressmode.setOnTouchListener(new TouchListener(2));
        tv_challengemode.setOnTouchListener(new TouchListener(3));

        /**
         *  返回按键
         */
        tv_return.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                startActivity(new Intent(Compete_Activity.this, MainActivity.class));
                Compete_Activity.this.finish();
                return false;
            }
        });

        tv_set.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    startActivity(new Intent(Compete_Activity.this, CompeteSet.class));
                }
                return false;
            }
        });

	}
    private class TouchListener implements View.OnTouchListener
    {
        int iDifficult;
        public TouchListener(int idifficult)
        {
            iDifficult = idifficult;
        }
        public boolean onTouch(View v, MotionEvent event)
        {
            if(event.getAction() == MotionEvent.ACTION_DOWN)
            {
                int iTime = 0;
                switch(iDifficult)
                {
                    case 1:
                        iTime = CommonData.COMMONTIME;
                        break;
                    case 2:
                        iTime = CommonData.PROGRESSTIME;
                        break;
                    case 3:
                        iTime = CommonData.CHALLENGETIME;
                        break;
                }
                Intent intent = new Intent(Compete_Activity.this, Compete_Mode_Activity.class);
                TextView tvView = (TextView)v;
                String strModeName = tvView.getText().toString();
                intent.putExtra("ModeName", strModeName);
                intent.putExtra("Time", iTime);
                intent.putExtra("Difficult", iDifficult);
                startActivity(intent);
            }
            return false;
        }
    }
    public void onBackPressed() {
        startActivity(new Intent(Compete_Activity.this, MainActivity.class));
        Log.i("competeActivity", "back");
        Compete_Activity.this.finish();
    }

}
