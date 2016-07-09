package com.CompeteMode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.laserGun.R;
import com.uidata.PreferenceConstants;
import com.uidata.PreferenceUtils;

public class CompeteSet extends Activity {
    private Spinner spTimeSet;
    ArrayAdapter arrayAdapter;
    int nTime;
    private TextView tv_confirm;
    private TextView tv_retrun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compete_set);

        spTimeSet = (Spinner)findViewById(R.id.sp_timeset);
        tv_confirm = (TextView)findViewById(R.id.tv_gameconfirm);
        tv_retrun = (TextView)findViewById(R.id.tv_gamereturn);


        arrayAdapter = ArrayAdapter.createFromResource(this, R.array.CompeteTime, R.layout.spinner_item_white);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTimeSet.setAdapter(arrayAdapter);
        spTimeSet.setOnItemSelectedListener(new ItemListner());

        nTime = PreferenceUtils.getPrefInt(this, PreferenceConstants.GameTime, 0);
        if(nTime != 0)
        {
            String []arrGameTime = getResources().getStringArray(R.array.CompeteTime);
            String strTime = nTime + "s";
            for(int i = 0; i < arrGameTime.length; i++ )
            {
                if(arrGameTime[i].equals(strTime))
                {
                    spTimeSet.setSelection(i);
                    break;
                }
            }
        }
        else
        {
            spTimeSet.setSelection(2);
        }
        tv_retrun.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    startActivity(new Intent(CompeteSet.this, Compete_Activity.class));
                    CompeteSet.this.finish();
                }
                return false;
            }
        });
        tv_confirm.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                {
                    if(event.getAction() == MotionEvent.ACTION_DOWN)
                    {
                        PreferenceUtils.setPrefInt(CompeteSet.this, PreferenceConstants.GameTime, nTime);
                        startActivity(new Intent(CompeteSet.this, Compete_Activity.class));
                        CompeteSet.this.finish();
                    }
                }
                return false;
            }
        });
    }
    public class ItemListner implements AdapterView.OnItemSelectedListener
    {

        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            // TODO Auto-generated method stub
            String strTime;
            strTime=(String) parent.getItemAtPosition(position);
            nTime = Integer.parseInt(strTime.substring(0,strTime.length() - 1));
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    }
    public void onBackPressed()
    {
        startActivity(new Intent(CompeteSet.this, Compete_Activity.class));
        CompeteSet.this.finish();
    }
}
