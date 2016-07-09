package com.HitMode;

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

public class HitSet_Activity extends Activity {

    private Spinner spMode1;
    private Spinner spMode2;
    private Spinner spMode3;
    private TextView tvReturn;
    private TextView tvConfirm;
    ArrayAdapter arrayAdapter;
    String strTime;
    int arrTime[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_main);
        spMode1 = (Spinner)findViewById(R.id.sp_mode1);
        spMode2 = (Spinner)findViewById(R.id.sp_mode2);
        spMode3 = (Spinner)findViewById(R.id.sp_mode3);
        tvReturn = (TextView)findViewById(R.id.tv_setreturn);
        tvConfirm = (TextView)findViewById(R.id.tv_Confirm);
        arrayAdapter = ArrayAdapter.createFromResource(this, R.array.ModeTime, R.layout.spinner_item_white);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMode1.setAdapter(arrayAdapter);
        spMode2.setAdapter(arrayAdapter);
        spMode3.setAdapter(arrayAdapter);

        arrTime = new int[3];

        spMode1.setOnItemSelectedListener(new ItemListner(0));
        spMode2.setOnItemSelectedListener(new ItemListner(1));
        spMode3.setOnItemSelectedListener(new ItemListner(2));

        tvReturn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    startActivity(new Intent(HitSet_Activity.this, Hit_Activity.class));
                    HitSet_Activity.this.finish();
                }
                return false;
            }
        });
        tvConfirm.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    PreferenceUtils.setPrefInt(HitSet_Activity.this,PreferenceConstants.Mode1, arrTime[0]);
                    PreferenceUtils.setPrefInt(HitSet_Activity.this,PreferenceConstants.Mode2, arrTime[1]);
                    PreferenceUtils.setPrefInt(HitSet_Activity.this,PreferenceConstants.Mode3, arrTime[2]);
                    startActivity(new Intent(HitSet_Activity.this, Hit_Activity.class));
                    HitSet_Activity.this.finish();
                }
                return false;
            }
        });
    }
    public class ItemListner implements AdapterView.OnItemSelectedListener
    {
        int iMode;
        ItemListner(int imode)
        {
            iMode = imode;
        }
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            // TODO Auto-generated method stub
            strTime=(String) parent.getItemAtPosition(position);
            arrTime[iMode] = Integer.parseInt(strTime.substring(0,strTime.length() - 1));
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    }
    public void onBackPressed() {
        startActivity(new Intent(HitSet_Activity.this, Hit_Activity.class));
        HitSet_Activity.this.finish();
    }
}
