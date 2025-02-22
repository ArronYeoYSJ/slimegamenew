package com.arron_yeoman.maths;

public class VertPN {

    private float x, y, z, w;
    private float nx, ny, nz, nw;
    private float u, v;

    public VertPN(float x, float y, float z, float w, float nx, float ny, float nz, float nw) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
        this.nx = nx;
        this.ny = ny;
        this.nz = nz;
        this.nw = nw;
    }

    public VertPN(Vector4 p, Vector4 n, Vector4 uv) {
        this.x = p.getX();
        this.y = p.getY();
        this.z = p.getZ();
        this.w = p.getW();
        this.nx = n.getX();
        this.ny = n.getY();
        this.nz = n.getZ();
        this.nw = n.getW();
        this.u = uv.getX();
        this.v = uv.getY();
    }

    public VertPN() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
        this.w = 1.0f;
        this.nx = 0;
        this.ny = 0;
        this.nz = 0;
        this.nw = 1.0f;
        this.u = 0;
        this.v = 0;
    }

    public Vector4 getXYZW(){
        return new Vector4(x, y, z, w);
    }
    public Vector4 getNormal(){
        return new Vector4(nx, ny, nz, nw);
    }
    

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public float getW() {
        return w;
    }

    public void setW(float w) {
        this.w = w;
    }

    public float getNx() {
        return nx;
    }

    public void setNx(float nx) {
        this.nx = nx;
    }

    public float getNy() {
        return ny;
    }

    public void setNy(float ny) {
        this.ny = ny;
    }

    public float getNz() {
        return nz;
    }

    public void setNz(float nz) {
        this.nz = nz;
    }

    public float getNw() {
        return nw;
    }

    public void setNw(float nw) {
        this.nw = nw;
    }

    public float getU() {
        return u;
    }

    public void setU(float u) {
        this.u = u;
    }

    public float getV() {
        return v;
    }

    public void setV(float v) {
        this.v = v;
    }

    

}
