package com.lazybot.microservices.map.business;

import com.lazybot.microservices.map.model.Cell;

import java.util.ArrayList;
import java.util.List;

public class PathFind {
    public PathFind() {
    }

    public boolean canGoTo (List<Cell> cells) {
        return false;
    }

    public int calculGCost(int x, int z) {
        return 0;
    }

    public int calculHCost(int x, int z) {
        return 0;
    }

    public int calculFCost(int gCost, int hCost) {
        return gCost + hCost;
    }


}
