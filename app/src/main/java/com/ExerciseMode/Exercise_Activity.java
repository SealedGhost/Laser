package com.ExerciseMode;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.laserGun.MainActivity;
import com.laserGun.R;
import com.uidata.CommonData;

public class Exercise_Activity extends Activity {
    private TextView tv_exerciseReturn;
    private TextView tv_end;
    private TextView tv_start;
    boolean bStart = false;
    Drawable dwPress;
    Drawable dwDisable;
    int Gray;
    int White;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.practice_main);

        dwPress = getResources().getDrawable(R.drawable.pressed);
        dwDisable= getResources().getDrawable(R.drawable.disabled);
        Gray= getResources().getColor(R.color.gray);
        White = getResources().getColor(R.color.white);

        tv_exerciseReturn = (TextView)findViewById(R.id.tv_exercise_return);
        tv_end = (TextView)findViewById(R.id.tv_practiceend);
        tv_start = (TextView)findViewById(R.id.tv_practice_start);

        tv_start.setTextColor(White);
        tv_start.setBackground(dwPress);
        tv_end.setTextColor(White);
        tv_start.setBackground(dwPress);

        tv_exerciseReturn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    if(bStart) {
//                        CommonData.dataProcess.sendCmd(0x00, CommonData.EXERCISECMD, CommonData.STOPSTT, 0x00, 0x00);
                        CommonData.dataProcess.sendCmd(0x00, CommonData.MOD_EXERCISE, CommonData.STT_STOP, 0x00, 0x00);
                    }
                   startActivity(new Intent(Exercise_Activity.this, MainActivity.class));
                   Exercise_Activity.this.finish();
                }
                return false;
            }
        });
        tv_end.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            if(bStart) {
//                                CommonData.dataProcess.sendCmd(0x00, CommonData.EXERCISECMD, CommonData.STOPSTT, 0x00, 0x00);
                                CommonData.dataProcess.sendCmd(0x00, CommonData.MOD_EXERCISE, CommonData.STT_STOP, 0x00, 0x00);
                                tv_start.setTextColor(White);
                                tv_start.setBackground(dwPress);
                                tv_end.setTextColor(Gray);
                                tv_end.setBackground(dwDisable);
                                bStart = false;
                            }
                        }
                        return false;
                    }
                });

        tv_start.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                  if(!bStart)
                  {
                      bStart = true;
                      tv_start.setTextColor(Gray);
                      tv_start.setBackground(dwDisable);
                      tv_end.setTextColor(White);
                      tv_end.setBackground(dwPress);
                      CommonData.dataProcess.sendCmd(0x00, CommonData.MOD_EXERCISE, CommonData.STT_START,0x00, 0x00);
                  }
                }
                return false;
            }
        });
    }
    public void onBackPressed() {
        if(bStart) {
            CommonData.dataProcess.sendCmd(0x00, CommonData.MOD_EXERCISE, CommonData.STT_STOP, 0x00, 0x00);
        }
        Log.i("Exercsise", "back");
        startActivity(new Intent(Exercise_Activity.this, MainActivity.class));
        Exercise_Activity.this.finish();
    }
}
