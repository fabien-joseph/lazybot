package com.lazybot.microservices.map.model;

public class Position {
    int x;
    int z;

    public Position() {
    }

    public Position(int x, int z) {
        this.x = x;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    @Override
    public String toString() {
        return "Position{" +
                "x=" + x +
                ", z=" + z +
                '}';
    }
}
