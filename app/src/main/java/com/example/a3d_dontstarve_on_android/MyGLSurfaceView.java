package com.example.a3d_dontstarve_on_android;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MyGLSurfaceView extends GLSurfaceView {
    public final MyRenderer mRenderer;
    private float mPreviousX;
    private float mPreviousY;
    private long lastTouch;


    public MyGLSurfaceView(Context context) {
        super(context);
        setEGLContextClientVersion(2);
        mRenderer = new MyRenderer(getContext());
        setRenderer(mRenderer);
    }

    public MyGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEGLContextClientVersion(2);
        mRenderer = new MyRenderer(getContext());
        setRenderer(mRenderer);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // getX/getY 触摸点相对于其所在view组件坐标系的坐标（以触摸点所在 view 的左上角为原点的坐标系）
        // getRawX/getRawY 触摸点相对于屏幕默认坐标系的坐标（以屏幕的左上角为原点的坐标系）
        float x = e.getX();
        float y = e.getY();
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if ((x-mRenderer.arrowButtonCentreX)*(x-mRenderer.arrowButtonCentreX)+(y-mRenderer.arrowButtonCentreY)*(y-mRenderer.arrowButtonCentreY) < mRenderer.arrowButtonR2) {
                    if (y-mRenderer.arrowButtonCentreY > Math.abs(x-mRenderer.arrowButtonCentreX)) {
                        mRenderer.moveDirection = 3;
                    }
                    else if (-y+mRenderer.arrowButtonCentreY > Math.abs(x-mRenderer.arrowButtonCentreX)) {
                        mRenderer.moveDirection = 1;
                    }
                    else if (x-mRenderer.arrowButtonCentreX > Math.abs(y-mRenderer.arrowButtonCentreY)) {
                        mRenderer.moveDirection = 2;
                    }
                    else if (-x+mRenderer.arrowButtonCentreX > Math.abs(y-mRenderer.arrowButtonCentreY)) {
                        mRenderer.moveDirection = 4;
                    }
                }
                requestRender();
                break;
            case MotionEvent.ACTION_MOVE:
                if (mRenderer.moveDirection != 0) {
                    break;
                }
                float dx = x - mPreviousX; // 从左往有滑动时: x 值增大，dx 为正；反之则否。
                float dy = y - mPreviousY; // 从上往下滑动时: y 值增大，dy 为正；反之则否。
                // OpenGL 绕 z 轴的旋转符合左手定则，即 z 轴朝屏幕里面为正。

                mRenderer.pikachu.mCamera.moveToView();
                mRenderer.pikachu.mCamera.rotate(dx/10, dy/10);
                mRenderer.pikachu.mCamera.backFromView();

                // 在计算旋转角度后，调用requestRender()来告诉渲染器现在可以进行渲染了
                requestRender();
                break;
            case MotionEvent.ACTION_UP:
                mRenderer.moveDirection = 0;
//                final long now = System.currentTimeMillis();
//                if (now-lastTouch<300) {
//                    mRenderer.changeProjection();
//                }
//                lastTouch = now;
                requestRender();
                break;
        }

        mPreviousX = x;
        mPreviousY = y;
        return true;
    }
}

