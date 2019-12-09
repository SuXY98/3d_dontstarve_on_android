package com.example.a3d_dontstarve_on_android;

public class Vector3f {
    public float x;
    public float y;
    public float z;

    public Vector3f() {}

    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3f(Vector3f v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }

    public float length() {
        return (float)Math.sqrt(x*x+y*y+z*z);
    }

    public float dot(Vector3f v) {
        return this.x*v.x+this.y*v.y+this.z*v.z;
    }

    public float dot(float x, float y, float z) {
        return this.x*x+this.y*y+this.z*z;
    }

    public Vector3f cross(Vector3f v) {
        Vector3f ret = new Vector3f();
        float rx = this.y * v.z - this.z * v.y;
        float ry = this.z * v.x - this.x * v.z;
        float rz = this.x * v.y - this.y * v.x;
        ret.x = rx;
        ret.y = ry;
        ret.z = rz;
        return ret;
    }

    public Vector3f cross(float x, float y, float z) {
        Vector3f ret = new Vector3f();
        float rx = this.y * z - this.z * y;
        float ry = this.z * x - this.x * z;
        float rz = this.x * y - this.y * x;
        ret.x = rx;
        ret.y = ry;
        ret.z = rz;
        return ret;
    }

    public Vector3f minus(Vector3f v) {
        Vector3f ret = new Vector3f();
        float rx = this.x - v.x;
        float ry = this.y - v.y;
        float rz = this.z - v.z;
        ret.x = rx;
        ret.y = ry;
        ret.z = rz;
        return ret;
    }

    public Vector3f plus(Vector3f v) {
        Vector3f ret = new Vector3f();
        float rx = this.x + v.x;
        float ry = this.y + v.y;
        float rz = this.z + v.z;
        ret.x = rx;
        ret.y = ry;
        ret.z = rz;
        return ret;
    }

    // return self
    public Vector3f normalize() {
        float length = this.length();
        this.x /= length;
        this.y /= length;
        this.z /= length;
        return this;
    }

    public Vector3f scale(float scale) {
        Vector3f ret = new Vector3f();
        float rx = this.x * scale;
        float ry = this.y * scale;
        float rz = this.z * scale;
        ret.x = rx;
        ret.y = ry;
        ret.z = rz;
        return ret;
    }

//    @Override
//    public Vector3f clone() throws CloneNotSupportedException {
//        try {
//            return (Vector3f) super.clone();
//        } catch (CloneNotSupportedException e) {
//            throw new AssertionError();
//        }
//    }

    public void set(Vector3f v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }

    public void set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

}