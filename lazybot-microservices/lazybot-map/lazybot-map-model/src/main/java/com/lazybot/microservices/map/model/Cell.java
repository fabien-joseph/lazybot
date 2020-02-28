package com.lazybot.microservices.map.model;

public class Cell {
    Integer fCost;
    Integer gCost;
    Integer hCost;
    int x;
    int z;

    public Cell() {
    }

    public Cell(Integer fCost, Integer gCost, Integer hCost, int x, int z) {
        this.fCost = fCost;
        this.gCost = gCost;
        this.hCost = hCost;
        this.x = x;
        this.z = z;
    }

    public Integer getfCost() {
        return fCost;
    }

    public void setfCost(Integer fCost) {
        this.fCost = fCost;
    }

    public Integer getgCost() {
        return gCost;
    }

    public void setgCost(Integer gCost) {
        this.gCost = gCost;
    }

    public Integer gethCost() {
        return hCost;
    }

    public void sethCost(Integer hCost) {
        this.hCost = hCost;
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
        return "Cell{" +
                "fCost=" + fCost +
                ", gCost=" + gCost +
                ", hCost=" + hCost +
                ", x=" + x +
                ", z=" + z +
                '}';
    }
}
