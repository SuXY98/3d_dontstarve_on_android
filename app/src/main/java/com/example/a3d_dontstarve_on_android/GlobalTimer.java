package com.example.a3d_dontstarve_on_android;

import java.util.Date;

//this is a global class, which must be initialized when system starts.
public final class GlobalTimer {
    private static boolean isStart;
    private static long startTime;
    private static final Date Timer = new Date();
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
        return Timer.getTime() - startTime;
    }

    public static boolean isStart(){
        return isStart;
    }
}
