package com.lazybot.microservices.map.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class MapCells {
    List<List<Cell>> map;

    public MapCells() {
    }

    public MapCells(List<List<Cell>> map) {
        this.map = map;
    }
}
