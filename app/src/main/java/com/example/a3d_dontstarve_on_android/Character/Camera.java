package com.example.a3d_dontstarve_on_android.Character;

import android.opengl.Matrix;

import com.example.a3d_dontstarve_on_android.Vector3f;

public class Camera {
    public Vector3f position;
    public Vector3f front;
    public Vector3f up;
    public Vector3f right;
    public Vector3f worldUp;
    public Vector3f dirXZ;

    private float yaw;
    private float pitch;
    private float velocity;
    private float sight;

    public Camera() {
        yaw = pitch = 0;
        velocity = 0.2f;
        sight = 10.0f;

        position = new Vector3f(40, 3, -45);
        worldUp = new Vector3f(0, 1, 0);

        front = new Vector3f();
        right = new Vector3f();
        up = new Vector3f();
        dirXZ = new Vector3f();

        updateVectors();
    }

    private void updateVectors() {
        front.x = (float)Math.cos(Math.toRadians(yaw)) * (float)Math.cos(Math.toRadians(pitch));
        front.y = (float)Math.sin(Math.toRadians(pitch));
        front.z = (float)Math.sin(Math.toRadians(yaw)) * (float)Math.cos(Math.toRadians(pitch));
        front.normalize();
        right = front.cross(worldUp).normalize();
        up = right.cross(front).normalize();

        dirXZ.x = front.x;
        dirXZ.z = front.z;
        dirXZ.normalize();
    }

    public void rotate(float dyaw, float dpitch) {
        yaw += dyaw;
        pitch += dpitch;

        if (pitch > 89.0f) {
            pitch = 89.0f;
        }
        if (pitch < -89.0f) {
            pitch = -89.0f;
        }

        updateVectors();
    }

    public void move(int direction, float dt) {
        float d = velocity * dt;
        if (direction==1) {
            position = position.plus(dirXZ.scale(d));
        }
        else if (direction==2) {
            Vector3f temp = dirXZ.cross(worldUp).normalize();
            position = position.plus(temp.scale(d));
        }
        else if (direction==3) {
            position = position.minus(dirXZ.scale(d));
        }
        else if (direction==4) {
            Vector3f temp = dirXZ.cross(worldUp).normalize();
            position = position.minus(temp.scale(d));
        }
    }

    public void moveToView() {
        position = position.plus(dirXZ.scale(sight));
    }

    public void backFromView() {
        position = position.minus(dirXZ.scale(sight));
    }

    public float[] getViewMatrix() {
        float[] matrix = new float[16];
        Vector3f view = position.plus(front.scale(sight));
        Matrix.setLookAtM(matrix, 0,
                this.position.x, this.position.y, this.position.z,
                view.x, view.y, view.z,
                this.up.x, this.up.y, this.up.z);
        return matrix;
    }

    public Vector3f getPikachuPos() {
        Vector3f pos = position.plus(dirXZ.scale(sight));
        pos.y = 0;
        return pos;
    }

    public float getYaw() {
        return yaw;
    }

}
