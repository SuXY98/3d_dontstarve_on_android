package com.example.a3d_dontstarve_on_android.Interface;

import android.content.Context;
import android.opengl.Matrix;

import com.example.a3d_dontstarve_on_android.GlobalTimer;

public class PikachuState {
    private StateBar stateBar;
    private HP hp;
    private HungerDegreeBar hungerDegreeBar;
    private int hpLeft;
    private int hungerDegree;

    public PikachuState(Context context) {
        stateBar = new StateBar(context);
        hp = new HP(context);
        hungerDegreeBar =new HungerDegreeBar(context);
        hpLeft = 7;
        hungerDegree = 7;
    }

    public void draw(float[] mvpMatrix) {
        updateState();

        stateBar.draw(mvpMatrix);
        hp.draw(mvpMatrix, hpLeft);
        hungerDegreeBar.draw(mvpMatrix, hungerDegree);
    }

    private void updateState() {
        if (!GlobalTimer.getTimeState()) {
            if (GlobalTimer.getHungryState() ) {
                if (hungerDegree>0) {
                    hungerDegree--;
                }
                else {
                    if (hpLeft>0) {
                        hpLeft--;
                    }
                }
                GlobalTimer.resetLastEatTime();
            }
        }
    }
}
