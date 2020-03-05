package com.lazybot.microservices.map.model;

import java.util.List;

public class MapCells {
    List<List<Cell>> map;

    public MapCells() {
    }

    public MapCells(List<List<Cell>> map) {
        this.map = map;
    }

    public List<List<Cell>> getMap() {
        return map;
    }

    public void setMap(List<List<Cell>> map) {
        this.map = map;
    }

    @Override
    public String toString() {
        return "Map{" +
                "map=" + map +
                '}';
    }
}
