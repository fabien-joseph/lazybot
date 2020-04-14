package com.lazybot.microservices.map.business;

import static org.junit.Assert.assertTrue;

import com.lazybot.microservices.commons.exceptions.EmptyMapException;
import com.lazybot.microservices.commons.model.Position;
import com.lazybot.microservices.map.model.Cell;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    CellManager manager = null;
    int[] blocks;
    int ray;


    @Before
    public void setUp() {
        manager = new CellManager();

        this.blocks = new int[]{
                32, 32, 32, 32, 32,
                32, 32, 32, 32, 32,
                32, 48, 32, 32, 32,
                32, 16, 16, 32, 32,
                32, 32, 32, 32, 32};

        this.ray = 5;
    }

    @Test
    public void testFindPositionWithSmallMap() {
        //TEST X
        Assert.assertEquals(2, manager.findPosition(8, 1).getX());
        Assert.assertEquals(1, manager.findPosition(4, 1).getX());
        Assert.assertEquals(0, manager.findPosition(0, 1).getX());

        //TEST Z
        Assert.assertEquals(2, manager.findPosition(8, 1).getZ());
        Assert.assertEquals(1, manager.findPosition(5, 1).getZ());
        Assert.assertEquals(0, manager.findPosition(0, 1).getZ());
    }

    @Test
    public void testFindPositionWithMediumMap() {
        //TEST X
        Assert.assertEquals(4, manager.findPosition(24, 2).getX());
        Assert.assertEquals(2, manager.findPosition(22, 2).getX());
        Assert.assertEquals(0, manager.findPosition(20, 2).getX());
        Assert.assertEquals(0, manager.findPosition(10, 2).getX());
        Assert.assertEquals(4, manager.findPosition(4, 2).getX());
        Assert.assertEquals(0, manager.findPosition(0, 2).getX());

        //TEST Z
        Assert.assertEquals(4, manager.findPosition(24, 2).getZ());
        Assert.assertEquals(4, manager.findPosition(22, 2).getZ());
        Assert.assertEquals(4, manager.findPosition(20, 2).getZ());
        Assert.assertEquals(2, manager.findPosition(10, 2).getZ());
        Assert.assertEquals(0, manager.findPosition(4, 2).getZ());
        Assert.assertEquals(0, manager.findPosition(0, 2).getZ());
    }

    @Test
    public void testFindPositionWithBigMap() {
        //TEST X
        Assert.assertEquals(8, manager.findPosition(80, 4).getX());
        Assert.assertEquals(4, manager.findPosition(40, 4).getX());
        Assert.assertEquals(2, manager.findPosition(29, 4).getX());
        Assert.assertEquals(8, manager.findPosition(8, 4).getX());
        Assert.assertEquals(0, manager.findPosition(0, 4).getX());

        //TEST Z
        Assert.assertEquals(8, manager.findPosition(80, 4).getZ());
        Assert.assertEquals(8, manager.findPosition(72, 4).getZ());
        Assert.assertEquals(3, manager.findPosition(29, 4).getZ());
        Assert.assertEquals(0, manager.findPosition(8, 4).getZ());
        Assert.assertEquals(0, manager.findPosition(0, 4).getZ());
    }

    @Test
    public void testCalculCosts() {
        Cell cell = new Cell();
        Position start = new Position(0, 0);
        Position target = new Position(3, 4);

        cell.setPosition(new Position(-1, 0));
        cell = manager.calculCosts(cell, start, target);
        Assert.assertEquals(java.util.Optional.of(8).get(), cell.getHCost());
        Assert.assertEquals(java.util.Optional.of(1).get(), cell.getGCost());
        Assert.assertEquals(java.util.Optional.of(9).get(), cell.getFCost());

        cell.setPosition(new Position(0, 0));
        cell = manager.calculCosts(cell, start, target);
        Assert.assertEquals(java.util.Optional.of(7).get(), cell.getHCost());
        Assert.assertEquals(java.util.Optional.of(0).get(), cell.getGCost());
        Assert.assertEquals(java.util.Optional.of(7).get(), cell.getFCost());
    }

    @Test
    public void testConvertListTo2DMap() {
        List<Cell> cells = manager.convertBlocksToCellList(blocks, 2);
        List<ArrayList<Cell>> map = manager.convertListTo2DMap(cells, 2);

        Assert.assertEquals(2, map.get(0).get(0).getIdBlock());
        Assert.assertEquals(2, map.get(0).get(2).getIdBlock());
        Assert.assertEquals(2, map.get(1).get(0).getIdBlock());
        Assert.assertEquals(2, map.get(1).get(4).getIdBlock());
        Assert.assertEquals(2, map.get(2).get(0).getIdBlock());
        Assert.assertEquals(3, map.get(2).get(1).getIdBlock());
        Assert.assertEquals(1, map.get(3).get(1).getIdBlock());
        Assert.assertEquals(1, map.get(3).get(2).getIdBlock());
        Assert.assertEquals(2, map.get(3).get(3).getIdBlock());
    }

    @Test(expected = EmptyMapException.class)
    public void testFindPath() throws EmptyMapException {
        manager.findPath(null, new Position(0, 0), new Position(0, 0));
    }
}
