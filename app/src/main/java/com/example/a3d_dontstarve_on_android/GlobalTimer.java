package com.example.a3d_dontstarve_on_android;

import java.util.Date;

//this is a global class, which must be initialized when system starts.
public final class GlobalTimer {
    private static long startTime;
    private static long currentTime;
    static final long DAYLENGTH = 120000;
    private static long lastEatTime;
    private static long lastUpdateTime;

    public static void initializeTimer() {
        startTime = currentTime = lastUpdateTime = (new Date()).getTime();
        lastEatTime = startTime + DAYLENGTH/2;
    }

    public static boolean getTimeState() {
        if (currentTime-startTime<=DAYLENGTH/2) {
            return true;
        }
        else {
            return false;
        }
    }

    public static long getCurrentMS(){
        return currentTime - startTime;
    }

    public static void updateTimer() {
        currentTime = (new Date()).getTime();
        if (currentTime - startTime>=DAYLENGTH) {
            startTime = currentTime;
            lastEatTime = startTime + DAYLENGTH/2;
        }
    }

    public static void resetLastEatTime() {
        if (currentTime - startTime>=DAYLENGTH/2) {
            lastEatTime = currentTime;
        }
    }

    public static boolean getHungryState() {
        if (currentTime-lastEatTime>DAYLENGTH/12) {
            return true;
        }
        else {
            return false;
        }
    }

    public static void resetLastUpdateTime() {
        lastUpdateTime = currentTime;
    }

    public static long getDeltaTime() {
        return currentTime-lastUpdateTime;
    }
}
