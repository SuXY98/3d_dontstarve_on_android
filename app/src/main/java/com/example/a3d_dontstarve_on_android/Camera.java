package com.example.a3d_dontstarve_on_android;

import android.opengl.Matrix;

public class Camera {
    private Vector3f position;
    private Vector3f view;
    private Vector3f up;
    public Vector3f n;
    private Vector3f v;
    private Vector3f u;
    private float[] mViewMatrix;
//    private float speed;

    public Camera() {
        this.position = new Vector3f();
        this.view = new Vector3f();
        this.up = new Vector3f();
        this.n = new Vector3f();
        this.v = new Vector3f();
        this.u = new Vector3f();
        this.mViewMatrix = new float[16];
    }

    public Camera(Vector3f position, Vector3f view, Vector3f up) {
        this.position = new Vector3f(position);
        this.view = new Vector3f(view);
        this.up = new Vector3f(up);
        this.n = new Vector3f(view.minus(position));
        this.v = new Vector3f(up);
        this.u = new Vector3f(n.cross(v));
        this.mViewMatrix = new float[16];

        this.up.normalize();
        this.n.normalize();
        this.v.normalize();
        this.u.normalize();

        Matrix.setLookAtM(this.mViewMatrix, 0,
                this.position.x, this.position.y, this.position.z,
                this.view.x, this.view.y, this.view.z,
                this.up.x, this.up.y, this.up.z);

//        this.speed = 0.2f;
    }

//    public Camera(Vector3f position, Vector3f view, Vector3f up, float speed) throws CloneNotSupportedException {
//        this.position = position.clone();
//        this.view = view.clone();
//        this.up = up.clone().normalize();
//        n = new Vector3f(view.minus(position));
//        v = up.clone();
//        u = new Vector3f(n.cross(v));
//        n.normalize();
//        v.normalize();
//        u.normalize();
//
//        this.speed = speed;
//    }

    //    public void setSpeed(float speed) {
//        this.speed = speed;
//    }
    public void set(float px, float py, float pz, float vx, float vy, float vz, float ux, float uy, float uz) {
        this.position.set(px, py, pz);
        this.view.set(vx, vy, vz);
        this.up.set(ux, uy, uz);

        this.n = view.minus(position);
        this.v.set(up);
        this.u = n.cross(v);

        this.up.normalize();
        this.n.normalize();
        this.v.normalize();
        this.u.normalize();

        Matrix.setLookAtM(this.mViewMatrix, 0,
                this.position.x, this.position.y, this.position.z,
                this.view.x, this.view.y, this.view.z,
                this.up.x, this.up.y, this.up.z);
    }

    public float getDist() {
        return Math.abs(this.view.minus(this.position).length());
    }

    private void updateParas() {
        float dist = this.getDist();
        this.up.set(this.v);
        this.view.set(this.position.plus(this.n.scale(dist)));
        Matrix.setLookAtM(this.mViewMatrix, 0,
                this.position.x, this.position.y, this.position.z,
                this.view.x, this.view.y, this.view.z,
                this.up.x, this.up.y, this.up.z);
    }

    public void moveVector(float dx, float dy, float dz) {
        this.position.x += dx;
        this.position.y += dy;
        this.position.z += dz;

        this.view.x += dx;
        this.view.y += dy;
        this.view.z += dz;

        Matrix.setLookAtM(this.mViewMatrix, 0,
                this.position.x, this.position.y, this.position.z,
                this.view.x, this.view.y, this.view.z,
                this.up.x, this.up.y, this.up.z);
    }

    public void moveVector(Vector3f v) {
        this.position.x += v.x;
        this.position.y += v.y;
        this.position.z += v.z;

        this.view.x += v.x;
        this.view.y += v.y;
        this.view.z += v.z;

        Matrix.setLookAtM(this.mViewMatrix, 0,
                this.position.x, this.position.y, this.position.z,
                this.view.x, this.view.y, this.view.z,
                this.up.x, this.up.y, this.up.z);
    }

    public void yaw(float angle) {
        float cs = (float)Math.cos(angle*Math.PI/180);
        float sn = (float)Math.sin(angle*Math.PI/180);

        Vector3f t = new Vector3f(this.n);
        Vector3f s = new Vector3f(this.u);

        this.n = t.scale(cs).minus(s.scale(sn));
        this.u = t.scale(sn).plus(s.scale(cs));

        updateParas();
    }

    public void pitch(float angle) {
        float cs = (float)Math.cos(angle*Math.PI/180);
        float sn = (float)Math.sin(angle*Math.PI/180);

        Vector3f t = new Vector3f(this.v);
        Vector3f s = new Vector3f(this.n);

        this.v = t.scale(cs).minus(s.scale(sn));
        this.n = t.scale(sn).plus(s.scale(cs));

        updateParas();
    }

    public void rotateWorldY(float angle) {
        float cs = (float)Math.cos(angle*Math.PI/180);
        float sn = (float)Math.sin(angle*Math.PI/180);

        Vector3f t = new Vector3f(this.v);
        Vector3f s = new Vector3f(this.n);

        this.v.x = t.x*cs + t.z*sn;
        this.v.z = -t.x*sn + t.z*cs;

        this.n.x = s.x*cs + s.z*sn;
        this.n.z = -s.x*sn + s.z*cs;

        this.u = n.cross(v);

        updateParas();
    }


    public float[] getViewMatrix() {
        return this.mViewMatrix;
    }

    public Vector3f getPosition() {
        return this.position;
    }

    public Vector3f getView() {
        return this.view;
    }

    public Vector3f getUpVector() {
        return this.up;
    }
}
