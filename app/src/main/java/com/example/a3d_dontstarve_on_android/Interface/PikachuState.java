package com.example.a3d_dontstarve_on_android.Interface;

import android.content.Context;
import android.opengl.Matrix;

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
        stateBar.draw(mvpMatrix);
        hp.draw(mvpMatrix, hpLeft);
        hungerDegreeBar.draw(mvpMatrix, hungerDegree);
    }
}
