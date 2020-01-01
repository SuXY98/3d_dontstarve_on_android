package com.example.a3d_dontstarve_on_android;

import java.util.Date;

//this is a global class, which must be initialized when system starts.
public final class GlobalTimer {
    private static long startTime;
    private static long currentTime;
    static final long DAYLENGTH = 120000;
    private static long lastEatTime;
    private static long lastUpdateTime;
    private static long lastMonsterCollision;

    public static float getMixFactors(){
        float res = 1.0f;
        float pass = currentTime - startTime;
        if(pass< (float)(DAYLENGTH * 4 / 10)){
            res = 1;
        }else if((pass < (float)(DAYLENGTH / 2) )&& (pass> (float)(DAYLENGTH * 4 / 10))){
            res = 1 - (pass*10 - (float)(DAYLENGTH * 4))/(DAYLENGTH );
        }else if((pass < (float)(DAYLENGTH *9/ 10) )){
            res  = 0;
        }else{
            res = (pass * 10 - DAYLENGTH*9)/DAYLENGTH;
        }
        return res;
    }

    public static void initializeTimer() {
        startTime = currentTime = lastUpdateTime = lastMonsterCollision = (new Date()).getTime();
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

    public static float getProcess(){
        return (currentTime - startTime) / (float)DAYLENGTH;
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

    public static boolean justCollided() {
        if (currentTime-lastMonsterCollision<=1000) {
            return true;
        }
        else {
            lastMonsterCollision = currentTime;
            return false;
        }
    }
}
