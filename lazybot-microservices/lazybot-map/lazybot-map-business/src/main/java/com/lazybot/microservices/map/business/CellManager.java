package com.lazybot.microservices.map.business;

import com.lazybot.microservices.map.model.Cell;
import com.lazybot.microservices.map.model.Position;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class CellManager {
    /*
    Convertion array of int to list of cells
     */
    public List<Cell> convertBlocksToCellList(int[] blocks, int ray) {
        List<Cell> cells = new ArrayList<>();
        for (int i = 0; i < blocks.length; i++) {
            cells.add(convertBlockToCell(blocks[i], i, ray));
        }
        return cells;
    }

    /*
    Convertion block to cell
    call findPosition method to find the position of a cell
     */
    public Cell convertBlockToCell(int block, int position, int ray) {
        Cell cell = new Cell();
        cell.setIdBlock(block / 16);
        cell.setPosition(findPosition(position, ray));
        return cell;
    }

    /*
    Find the position of a cell
     */
    public Position findPosition(int positionBlock, int ray) {
        Position pos = new Position();
        int dimension = ray * 2 + 1;

        int x = 0;
        for (int i = 0; i < positionBlock; i++) {
            if (x == dimension - 1)
                x = 0;
            else {
                x++;
            }
        }
        pos.setX(x);

        for (int i = 1; i <= dimension; i++) {
            if (positionBlock >= dimension * i - dimension && positionBlock <= dimension * i - 1) {
                pos.setZ(i - 1);
                break;
            }
        }
        return pos;
    }

    /*
    Check if a path can be find to go to a target
     */
    public boolean isReachable(List<Cell> cells, Position start, Position target) {

        return false;
    }

    /*
    Calcul the gCost, hCost and fCost of a cell
     */
    public Cell calculCosts(Cell cell, Position start, Position target) {
        cell.setGCost((Math.abs(cell.getPosition().getX()) + Math.abs(cell.getPosition().getZ())) +
                (Math.abs(start.getX()) + Math.abs(start.getZ())));

        cell.setHCost((Math.abs(cell.getPosition().getX()) + Math.abs(cell.getPosition().getZ())) +
                (Math.abs(target.getX()) + Math.abs(target.getZ())));

        cell.setFCost(cell.getGCost() + cell.getHCost());

        return cell;
    }
}
