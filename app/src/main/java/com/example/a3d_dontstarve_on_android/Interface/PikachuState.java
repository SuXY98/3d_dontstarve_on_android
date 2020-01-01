package com.example.a3d_dontstarve_on_android.Interface;

import android.content.Context;

import com.example.a3d_dontstarve_on_android.GlobalTimer;

public class PikachuState {
    private StateBar stateBar;
    private HPBar hpBar;
    private HungerDegreeBar hungerDegreeBar;
    private int hp;
    private int hungerDegree;

    public PikachuState(Context context) {
        stateBar = new StateBar(context);
        hpBar = new HPBar(context);
        hungerDegreeBar =new HungerDegreeBar(context);
        hp = 7;
        hungerDegree = 7;
    }

    public void draw(float[] mvpMatrix) {
        updateState();

        stateBar.draw(mvpMatrix);
        hpBar.draw(mvpMatrix, hp);
        hungerDegreeBar.draw(mvpMatrix, hungerDegree);
    }

    private void updateState() {
        if (!GlobalTimer.getTimeState()) {
            if (GlobalTimer.getHungryState() ) {
                if (hungerDegree>0) {
                    hungerDegree--;
                }
                else {
                    if (hp>0) {
                        hp--;
                    }
                }
                GlobalTimer.resetLastEatTime();
            }
        }
    }

    public void handleCollision(int state) {
        if (state==0) { // fruit
            if (hungerDegree<7) {
                hungerDegree++;
            }
            else if (hp<7) {
                hp++;
            }
        }
        else if (state==1) {    // monster
            if (hp>1) {
                hp -=2;
            }
            else {
                hp--;
            }
        }
    }

    public boolean isAlive() {
        return hp > 0;
    }
}
