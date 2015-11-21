package com.uidata;
public class CommonData  {
    public static DataProcess dataProcess = new DataProcess();
    public static String IP = "10.10.100.254";
    public static int  PORT =  8899;
    public static  int TARGETNUM = 50;
    public static boolean isRunning = true;//to control detectThread
    public static WifiAdmin wifiAdmin ;
    public static String SSID = "For Cs Laser";
    public static String PSWD = "1357924680";

    public static int NETTYPE = 3;
    public static int ARRAYSIZE = 9;

    public static int EXERCISECMD = 0x01;
    public static int HITCMD = 0x02;
    public static int COMPETECMD = 0x03;
    public static int STARTBYTE = 0xdf;
    public static int STOPBYTE = 0xfd;

    public static int STOPSTT = 0x00;
    public static int ACKSTT = 0x01;
    public static int PAUSESTT = 0x02;
    public static int RESUMESTT = 0x03;
    public static int STARTSTT = 0x04;


    public static int COMMONTIME = 30;
    public static int PROGRESSTIME = 15;
    public static int CHALLENGETIME = 10;

    public static int EXERCISE_TIME = 30;
    public static int GAMETIME = 180;
}
