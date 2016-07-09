package com.uidata;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.util.Log;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.cert.CertificateParsingException;

import android.os.Handler;

import com.laserGun.MyApplication;

public class DataProcess {
    private static final String TAG = "DataProcess";

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

    public boolean sendData(byte[] data)  {
        try {
            OutputStream out = socket.getOutputStream();
            if (out == null) {
                Log.e(TAG, "outputstream null");
                return false;
            }
            Log.e(TAG, "put in out stream");
            out.write(data);
            return true;
        }catch (IOException e)
        {
            e.printStackTrace();
            stopConn();
            return false;
        }
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
                                        Log.e("DataProcess", "socket read");
                                        rlRead = socket.getInputStream().read(sData);

                                        if (rlRead > 0) {
                                            Log.e("receiveData",""+rlRead);
                                            receiveFrame();
                                            for (int i = 0; i < 8; i++) {
                                                sData[i] = 0;
                                            }
                                        }
                                        else{
                                            Log.e("receiveData", "read nothing");
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
//        sum=stt + value + dataz;
//        sum  =
//        sum=sum&0xff;
        int size = CommonData.ARRAYSIZE;
        byte[] frame  = new byte[size];
        frame[0]  = (byte)CommonData.STARTBYTE;  /// Header of laser if 0x55
        frame[1]  = (byte)((CommonData.DEV_TGT_S << 5) | address);  /// ID of destination
        frame[2]  = (byte)( (CommonData.DEV_PAD << 5) | 1); /// ID of src
        frame[3]  = (byte)(mode<<5 | stt );   /// cmd
        frame[4]  = (byte)value;   /// para
        frame[5]  = (byte)dataz;   /// reserved
        frame[6]  = (byte)(frame[3]+frame[4]+frame[5]); /// sum
        frame[7]  = (byte)CommonData.STOPBYTE;   /// Tail
 //        byte[] frame =new byte[size] ;
//        frame[0] =(byte)CommonData.STARTBYTE;
//        frame[1] =(byte)CommonData.STARTBYTE;
//        frame[2] =(byte)address;
//        frame[3] =(byte)mode;
//        frame[4] =(byte)stt;
//        frame[5] = (byte)value;
//        frame[6] = (byte)dataz;
//        frame[7] =(byte)sum;
//        frame[8] =(byte)CommonData.STOPBYTE;

        if(!socket.isConnected())
        {
            return 0;
        }

        Log.e("senddata", "senddata");
        sendData(frame);
        return size;
    }

    public void receiveFrame()
    {
        if(byteTurnInt(sData[0]) == CommonData.STARTBYTE  &&  byteTurnInt(sData[7]) == CommonData.STOPBYTE){
            int dst  = byteTurnInt(sData[1]);
            int src  = byteTurnInt(sData[2]);
            int cmd  = byteTurnInt(sData[3]);
            int prm  = byteTurnInt(sData[4]);
            int rsr  = byteTurnInt(sData[5]);
            int sum  = byteTurnInt(sData[6]);

            if( (src>>5) != CommonData.DEV_TGT_S) {
                Log.e(TAG, "src dev type err");
                return ;
            }

            if(sum != ((cmd+prm+rsr)&0xff)){
Log.e(TAG,"sum error");
                return;
            }
            else{
//               if( (cmd>>5) == CommonData.RECEIVECMD){
                   int addr  = src & 0x1f;

                   if(addr > 0){
                       Log.e(TAG, "Snipper:" + addr +" hit " +prm +" ring" );
                       Intent toIntent  = new Intent("ReceiveData");
                       toIntent.putExtra("HitNum", addr);
                       toIntent.putExtra("Ring", prm);
                       MyApplication.getAppContext().sendBroadcast(toIntent);
                   }
                   else{
                       Log.e(TAG, "addr err");
                   }
//               }
//                else{
//                   Log.e(TAG, "cmd err");
//               }
            }
        }
        else{
Log.e(TAG, "error header or tail");
        }


//        int first = byteTurnInt(sData[0]);
////        int second = byteTurnInt(sData[1]);
//        int last = byteTurnInt(sData[8]);
//        int iMode = byteTurnInt((byte)(sData[3] >> 5));
//        int iStt  = byteTurnInt((byte)(sData[3] & 0x1f));
//        int iAdress = byteTurnInt((byte)(sData[1] & 0x1f));
////        if(first == CommonData.STARTBYTE && last == CommonData.STOPBYTE && second == CommonData.STARTBYTE)
//        if(first == CommonData.STARTBYTE  &&  last == CommonData.STOPBYTE)
//        {
////            if(sData[7]==sData[4]+sData[5]+sData[6])
//            if(sData[6] == sData[4]+sData[3]+sData[5])
//            {
//                Intent intent;
//                if(iMode == CommonData.COMPETECMD && iStt== CommonData.ACKSTT)//competeMode Reply
//                {
//                    Log.e("ack","ack" + " " +iAdress);
//                    intent = new Intent("CompeteACK");
//                    intent.putExtra("TargetExist",iAdress);
//                    MyApplication.getAppContext().sendBroadcast(intent);
//                }
//                else
//                {
//                    if(iMode == CommonData.RECEIVECMD) {
//                        iAdress = byteTurnInt(sData[5]);
//                        int nRing = byteTurnInt(sData[6]);
//                        if(iAdress != 0) {
//                            intent = new Intent("ReceiveData");
//                            intent.putExtra("HitNum", iAdress);
//                            Log.e("Hirtnum", " " + iAdress);
//                            Log.e("Ring", "" + nRing);
//
//                            intent.putExtra("Ring", nRing);
//                            MyApplication.getAppContext().sendBroadcast(intent);
//                        }
//                    }
//                }
//            }
//        }
    }
    public int byteTurnInt(byte a)
    {
        int i = a;
        i = a&0xff;
        return i;
    }
}

