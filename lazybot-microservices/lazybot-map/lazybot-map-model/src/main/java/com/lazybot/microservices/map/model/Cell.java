package com.lazybot.microservices.map.model;
import com.lazybot.microservices.commons.model.Position;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Cell {
    // Distance from starting node
    Integer gCost;

    // Distance from end node
    Integer hCost;

    // gCost + hCost
    Integer fCost;

    Position position;

    int idBlock;

    int metaData;

    public Cell() {
    }

    public Cell(Integer gCost, Integer hCost, Integer fCost, Position position, int idBlock, int metaData) {
        this.gCost = gCost;
        this.hCost = hCost;
        this.fCost = fCost;
        this.position = position;
        this.idBlock = idBlock;
        this.metaData = metaData;
    }
}
