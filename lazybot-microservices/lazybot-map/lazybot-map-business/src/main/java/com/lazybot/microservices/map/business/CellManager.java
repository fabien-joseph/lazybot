package com.lazybot.microservices.map.business;

import com.lazybot.microservices.commons.exceptions.EmptyMapException;
import com.lazybot.microservices.commons.model.Position;
import com.lazybot.microservices.map.model.Cell;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

@Component
public class CellManager {
    public List<Position> findPath(List<ArrayList<Cell>> map, Position start, Position end) throws EmptyMapException {
        if (map == null || map.isEmpty())
            throw new EmptyMapException();

        map = calculListCosts(map, start, end);

        return null;
    }

    /*
    Convert a list of cells to a map with two lists to represent X and Z;
     */
    public List<ArrayList<Cell>> convertListTo2DMap(List<Cell> cells, int ray) {
        List<ArrayList<Cell>> map = new ArrayList<>();
        int dimension = ray * 2 + 1;

        int cursor = 0;
        for (int i = 0; i < dimension; i++) {
            map.add(new ArrayList<>());
            for (int j = 0; j < dimension; j++) {
                map.get(i).add(cells.get(cursor));
                cursor++;
            }
        }
        return map;
    }

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
    Check if a path can be find to go to a end
     */
    public boolean isReachable(List<Cell> cells, Position start, Position end) {

        return false;
    }

    public List<ArrayList<Cell>> calculListCosts(List<ArrayList<Cell>> map, Position start, Position end) throws NullPointerException {
        if (map.isEmpty())
            return null;
        for (int i = 0; i < map.size(); i++) {
            for (int j = 0; j < map.get(i).size(); j++) {
                map.get(i).set(j, calculCosts(map.get(i).get(j), start, end));
            }
        }
        return map;
    }

    /*
    Calcul the gCost, hCost and fCost of a cell
     */
    public Cell calculCosts(Cell cell, Position start, Position end) {
        cell.setGCost((Math.abs(cell.getPosition().getX()) + Math.abs(cell.getPosition().getZ())) +
                (Math.abs(start.getX()) + Math.abs(start.getZ())));

        cell.setHCost(
                (Math.abs(cell.getPosition().getX()) + Math.abs(cell.getPosition().getZ())) +
                        (Math.abs(end.getX()) + Math.abs(end.getZ())));

        cell.setFCost(cell.getGCost() + cell.getHCost());

        return cell;
    }
}
