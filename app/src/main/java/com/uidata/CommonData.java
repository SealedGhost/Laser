package com.uidata;
public class CommonData  {
    public static DataProcess dataProcess = new DataProcess();
    public static String IP = "10.10.100.254";
    public static int  PORT =  8899;
   // public static String IP = "192.168.43.1";
    //public static int  PORT =  12345;
    public static  int TARGETNUM = 25;
    public static boolean isRunning = true;//to control detectThread
    public static WifiAdmin wifiAdmin ;
    public static String SSID = "For Cs Laser";
    public static String PSWD = "1357924680";

   //public static String SSID = "a-b";
   // public static String PSWD = "123456789";

    /**@brief Define device type
     *
     * Device       val
     * Pad         0x01
     * Tgt(small)  0x05
     */
    public static int DEV_PAD     = 1;
    public static int DEV_TGT_B   = 4;
    public static int DEV_TGT_S   = 5;

    public static int NETTYPE = 3;
    public static int ARRAYSIZE = 8;

    public static int EXERCISECMD = 0x01;
    public static final int MOD_EXERCISE  = 0x01;
    public static final int MOD_HIT        = 0x02;
    public static final int MOD_COMPETE   = 0x03;

    public static final int CMD_READ       = 0x01;


    public static int STARTBYTE = 0x55;
    public static int STOPBYTE = 0xfd;


    /**
     *
     */
    public static final int STT_STOP  = 0x5;
    public static final int STT_ACK    = 0x03;
    public static final int STT_PAUSE  = 0x04;
    public static final int STT_RESUME  = 0x02;
    public static final int STT_START    = 0x01;



    public static int RECEIVECMD = 0x01;


    public static final int COMMONTIME = 15 * 1000;
    public static final int PROGRESSTIME = 15 * 1000;
    public static final int CHALLENGETIME = 10 *1000;

    public static int EXERCISE_TIME = 30*1000;

    public static int DetectTime = 10 * 1000;

    public static int GameTime = 180;

}
