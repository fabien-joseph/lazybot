package com.lazybot.microservices.map.business;

import static org.junit.Assert.assertTrue;

import com.lazybot.microservices.map.model.Position;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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
                2, 2, 2, 2, 2,
                2, 2, 2, 2, 2,
                2, 1, 2, 2, 2,
                2, 1, 1, 2, 2,
                2, 2, 2, 2, 2};

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

}
