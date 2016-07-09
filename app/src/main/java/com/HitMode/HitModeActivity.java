package com.HitMode;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.laserGun.R;
import com.uidata.CommonData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HitModeActivity extends Activity {

	private TextView tv_start;
	private TextView tv_stop;
	private TextView tv_continue;
	private TextView tv_return;
    private TextView tv_end;
    private TextView tv_mode;
    private GridLayoutManager gridLayoutManager;
    private RecyclerView recyclerView;

    public AdapterRecycler adapterRecycler;
    public String arrhitscores[] = new String[CommonData.TARGETNUM];
    public int arrhitscorenum[] = new int[CommonData.TARGETNUM];
    public MyBroadcastReceiver myBroadcastReceiver;
    final int STRAT = 1;
    final int STOP = 2;
    final int CONTINUE = 3;
    final int RETURN = 4;
    final int END = 5;
    int iTime = 0;
    boolean bStart = false;
    boolean bStop = false;


    Drawable dwPress;
    Drawable dwDisable;
    int Gray;
    int White;

    public static Map<Integer,ArrayList<String>> map = new HashMap<Integer, ArrayList<String>>();

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.model_main);
        dwPress = getResources().getDrawable(R.drawable.pressed);
        dwDisable= getResources().getDrawable(R.drawable.disabled);
        Gray= getResources().getColor(R.color.gray);
        White = getResources().getColor(R.color.white);

        initUI();
        Intent intent = getIntent();
        String strModeName = intent.getStringExtra("ModeName");
        iTime = intent.getIntExtra("Time", 20);
        tv_mode.setText(strModeName);

        myBroadcastReceiver = new MyBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("ReceiveData");
        registerReceiver(myBroadcastReceiver, intentFilter);

	}
	private void initUI()
	{
		tv_start = (TextView)this.findViewById(R.id.tv_compete_start);
        tv_stop = (TextView)this.findViewById(R.id.tv_stop);
        tv_continue = (TextView)this.findViewById(R.id.tv_tryagain);
        tv_return = (TextView)this.findViewById(R.id.tv_compete_return);
        tv_mode = (TextView)this.findViewById(R.id.tv_competemodename);
        tv_end = (TextView)this.findViewById(R.id.tv_end);
        recyclerView =(RecyclerView)this.findViewById(R.id.rv_show);
        gridLayoutManager = new GridLayoutManager(this, 4);
        recyclerView.setLayoutManager(gridLayoutManager);
        for(int i = 0; i < CommonData.TARGETNUM; i++)
        {
            arrhitscores[i] = "0";
        }
        adapterRecycler = new AdapterRecycler(this,arrhitscores);
        recyclerView.setAdapter(adapterRecycler);
        TouchListener starttouchListener = new TouchListener(STRAT);
        tv_start.setOnTouchListener(starttouchListener);
        TouchListener stoptouchListener = new TouchListener(STOP);
        tv_stop.setOnTouchListener(stoptouchListener);
        TouchListener continuetouchListener = new TouchListener(CONTINUE);
        tv_continue.setOnTouchListener(continuetouchListener);
        TouchListener returnTouchListener = new TouchListener(RETURN);
        tv_return.setOnTouchListener(returnTouchListener);
        TouchListener endListener = new TouchListener(END);
        tv_end.setOnTouchListener(endListener);
        tv_start.setBackground(dwPress);
        tv_start.setTextColor(White);
        tv_continue.setBackground(dwDisable);
        tv_continue.setTextColor(Gray);
        tv_stop.setBackground(dwDisable);
        tv_stop.setTextColor(Gray);
        tv_end.setBackground(dwDisable);
        tv_end.setTextColor(Gray);
	}
    public class TouchListener implements View.OnTouchListener
    {
        int iFunction;
        public TouchListener(int ifunction)
        {
            iFunction = ifunction;
        }
        public boolean onTouch(View v, MotionEvent event)
        {
            if(event.getAction() == MotionEvent.ACTION_DOWN)
            {
                switch (iFunction)
                {
                    case STRAT:
                        if(!bStart) {
                            for(int i = 0; i < CommonData.TARGETNUM; i++)
                            {
                                arrhitscores[i] = "0";
                            }
                            adapterRecycler.notifyDataSetChanged();
//                            CommonData.dataProcess.sendCmd(0x00, CommonData.HITCMD, CommonData.STARTSTT, iTime, 0x00);
                            CommonData.dataProcess.sendCmd(0x00, CommonData.MOD_HIT, CommonData.STT_START, iTime, 0x00);
                            tv_start.setBackground(dwDisable);
                            tv_start.setTextColor(Gray);

                            tv_continue.setBackground(dwPress);
                            tv_continue.setTextColor(White);

                            tv_stop.setBackground(dwDisable);
                            tv_stop.setTextColor(Gray);

                            tv_end.setBackground(dwPress);
                            tv_end.setTextColor(White);
                            bStart = true;
                            break;
                        }
                    case STOP:
                        if(bStart&&!bStop) {
//                            CommonData.dataProcess.sendCmd(0x00, CommonData.HITCMD, CommonData.PAUSESTT, 0x00, 0x00);
                            CommonData.dataProcess.sendCmd(0x00, CommonData.MOD_HIT, CommonData.STT_PAUSE, 0x00, 0x00);
                            tv_stop.setBackground(dwDisable);
                            tv_stop.setTextColor(Gray);
                            tv_continue.setBackground(dwPress);
                            tv_continue.setTextColor(White);
                            bStop = true;
                        }
                        break;
                    case CONTINUE:
                        if(bStop&&bStart)
                        {
                            tv_stop.setBackground(dwPress);
                            tv_stop.setTextColor(White);
                            tv_continue.setBackground(dwDisable);
                            tv_continue.setTextColor(Gray);
//                            CommonData.dataProcess.sendCmd(0x00, CommonData.HITCMD, CommonData.RESUMESTT, 0x00, 0x00);
                            CommonData.dataProcess.sendCmd(0x00, CommonData.MOD_HIT, CommonData.STT_RESUME, 0x00, 0x00);
                            bStop = false;
                        }
                        break;
                    case RETURN:
                        if(bStart)
                        {
//                            CommonData.dataProcess.sendCmd(0x00, CommonData.HITCMD, CommonData.STOPSTT, 0x00, 0x00);
                            CommonData.dataProcess.sendCmd(0x00, CommonData.MOD_HIT, CommonData.STT_STOP, 0x00, 0x00);
                        }
                        Intent intent = new Intent(HitModeActivity.this, Hit_Activity.class);
                        startActivity(intent);
                        HitModeActivity.this.finish();
                        break;
                    case END:
                        if(bStart)
                        {
//                            CommonData.dataProcess.sendCmd(0x00, CommonData.HITCMD, CommonData.STOPSTT, 0x00, 0x00);
                            CommonData.dataProcess.sendCmd(0x00, CommonData.MOD_HIT, CommonData.STT_STOP, 0x00, 0x00);
                            bStart = false;
                            tv_start.setBackground(dwPress);
                            tv_start.setTextColor(White);
                            tv_continue.setBackground(dwDisable);
                            tv_continue.setTextColor(Gray);
                            tv_stop.setBackground(dwDisable);
                            tv_stop.setTextColor(Gray);
                            tv_end.setBackground(dwDisable);
                            tv_end.setTextColor(Gray);
                        }
                        break;
                }
            }
            return false;
        }
    }
    public class MyBroadcastReceiver extends BroadcastReceiver
    {
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();
            if(action.equals("ReceiveData"))
            {
                int hitNum = intent.getIntExtra("HitNum", 0);
                int nRing = intent.getIntExtra("Ring", 0);
                if(bStart && !bStop &&  hitNum != 0 && hitNum < CommonData.TARGETNUM)
                {
                    arrhitscorenum[hitNum - 1]++;
                    arrhitscores[hitNum - 1] = ""+arrhitscorenum[hitNum - 1];

                    adapterRecycler.notifyItemChanged(hitNum - 1);
                    if(!map.containsKey(hitNum))
                    {
                        ArrayList<String> arrayList = new ArrayList<String>();
                        arrayList.add(nRing + "");
                        map.put(hitNum, arrayList);
                    }
                    else
                    {
                        map.get(hitNum).add(nRing + "");
                    }
                }
            }
        }
    }
    public void onBackPressed() {
        if(bStart)
        {
            CommonData.dataProcess.sendCmd(0x00, CommonData.MOD_HIT, CommonData.STT_STOP, 0x00, 0x00);
        }
        Intent intent = new Intent(HitModeActivity.this, Hit_Activity.class);
        startActivity(intent);
        HitModeActivity.this.finish();
    }
    protected void onDestroy()
    {
        super.onDestroy();
        unregisterReceiver(myBroadcastReceiver);
    }
    public Map<Integer, ArrayList<String>> getMap()
    {
        return map;
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == 1) {
            int  nHitnum = data.getExtras().getInt("HitPosition");
            ArrayList<String> arrRing = data.getExtras().getStringArrayList("gradeArrayList");
            map.remove(nHitnum);
            map.put(nHitnum, arrRing);
        }
    }
}
