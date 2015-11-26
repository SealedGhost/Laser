package com.uidata;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.util.Log;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import android.os.Handler;

import com.infraredgun.MyApplication;

public class DataProcess {

    private byte[] sData = new byte[100];

    public Socket socket;
    private boolean onListening = false;

    private Thread receiveThread;

    public Handler handler;
    public BroadcastReceiver broadcastReceiver;
    public DataProcess()
    {
        socket  = new Socket();
    }

    public boolean sendData(byte[] data) throws IOException {
        OutputStream out=socket.getOutputStream();
        if(out==null) return false;
        out.write(data);
        return true;
    }

    public boolean stopConn() {
        if (socket == null) return false;
        onListening = false;
        try {
            socket.shutdownInput();
            socket.shutdownOutput();
            socket.close();
            socket = null;
        } catch (IOException e) {
            e.printStackTrace();
            socket = null;
        }
        return true;
    }

    public void startConn() {
        new Thread(new Runnable()
        {
            @Override
            public void run() {
                try
                {
                    Thread.sleep(1000);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
                 try
                 {
                     socket = new Socket(CommonData.IP, CommonData.PORT);
                     Log.e("DataProcess", "socket connected");
                     onListening = true;
                     acceptMsg();
                }
                catch(UnknownHostException e)
                {
                    e.printStackTrace();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    public void acceptMsg()
    {
        if(receiveThread == null)
        {
            receiveThread = new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    int rlRead;
                    try {
                        while (onListening) {
                            if(!socket.isClosed()) {
                                if(socket.isConnected()) {
                                    try {
                                        rlRead = socket.getInputStream().read(sData);
                                        if (rlRead > 0) {
                                            Log.e("receiveData",""+rlRead);
                                            receiveFrame();
                                            for (int i = 0; i < 9; i++) {
                                                sData[i] = 0;
                                            }
                                        }
                                    }catch(IOException e)
                                    {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            receiveThread.start();
        }
        if(!onListening)
        {
            onListening = true;
        }
    }


    public int sendCmd(int address,int mode, int stt, int value, int dataz)
    {
        int sum =0;
        sum=stt + value + dataz;
        sum=sum&0xff;
        int size = CommonData.ARRAYSIZE;
        byte[] frame =new byte[size] ;
        frame[0] =(byte)CommonData.STARTBYTE;
        frame[1] =(byte)CommonData.STARTBYTE;
        frame[2] =(byte)address;
        frame[3] =(byte)mode;
        frame[4] =(byte)stt;
        frame[5] = (byte)value;
        frame[6] = (byte)dataz;
        frame[7] =(byte)sum;
        frame[8] =(byte)CommonData.STOPBYTE;
        if(!socket.isConnected())
        {
            return 0;
        }
        try {
            sendData(frame);
        } catch (IOException e) {

        }
        return size;
    }

    public void receiveFrame()
    {
        int first = byteTurnInt(sData[0]);
        int second = byteTurnInt(sData[1]);
        int last = byteTurnInt(sData[8]);
        int iFunc = byteTurnInt(sData[3]);
        int iAdress = byteTurnInt(sData[2]);
        if(first == CommonData.STARTBYTE && last == CommonData.STOPBYTE && second == CommonData.STARTBYTE)
        {
            if(sData[7]==sData[4]+sData[5]+sData[6])
            {
                Intent intent;
                if(iFunc == 0x09 && byteTurnInt(sData[4])== CommonData.ACKSTT)//competeMode Reply
                {
                    intent = new Intent("CompeteACK");
                    intent.putExtra("TargetExist",iAdress);
                }
                else
                {
                    intent = new Intent("ReceiveData");
                    intent.putExtra("HitNum", iAdress);
                    intent.putExtra("Mode", iFunc);
                }
                MyApplication.getAppContext().sendBroadcast(intent);
            }
        }
    }
    public int byteTurnInt(byte a)
    {
        int i = a;
        i = a&0xff;
        return i;
    }
}

