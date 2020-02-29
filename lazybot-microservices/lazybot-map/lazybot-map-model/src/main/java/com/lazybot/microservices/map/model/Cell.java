package com.lazybot.microservices.map.model;

public class Cell {

    // Distance from starting node
    Integer gCost;

    // Distance from end node
    Integer hCost;

    // gCost + hCost
    Integer fCost;

    Position position;

    int idBlock;

    public Cell() {
    }

    public Cell(Integer gCost, Integer hCost, Integer fCost, Position position, int idBlock) {
        this.gCost = gCost;
        this.hCost = hCost;
        this.fCost = fCost;
        this.position = position;
        this.idBlock = idBlock;
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

    public Integer getfCost() {
        return fCost;
    }

    public void setfCost(Integer fCost) {
        this.fCost = fCost;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public int getIdBlock() {
        return idBlock;
    }

    public void setIdBlock(int idBlock) {
        this.idBlock = idBlock;
    }

    @Override
    public String toString() {
        return "Cell{" +
                "gCost=" + gCost +
                ", hCost=" + hCost +
                ", fCost=" + fCost +
                ", position=" + position +
                ", idBlock=" + idBlock +
                '}';
    }
}
