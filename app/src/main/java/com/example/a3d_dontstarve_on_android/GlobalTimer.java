package com.example.a3d_dontstarve_on_android;

import java.util.Date;

//this is a global class, which must be initialized when system starts.
public final class GlobalTimer {
    private static boolean isStart;
    private static long startTime;
    public static final Date Timer = new Date();
    public static void start(){
        if(!isStart){
            isStart = true;
            startTime = Timer.getTime();
        }
    }

    public static void stop(){
        isStart = false;
    }

    public static long getCurrentMS(){
        Date T = new Date();
        return T.getTime() - startTime;
    }

    public static boolean isStart(){
        return isStart;
    }
}
