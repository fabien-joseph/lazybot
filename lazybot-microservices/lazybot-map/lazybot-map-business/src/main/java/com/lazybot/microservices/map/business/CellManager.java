package com.lazybot.microservices.map.business;

import com.lazybot.microservices.commons.exceptions.EmptyMapException;
import com.lazybot.microservices.commons.model.Position;
import com.lazybot.microservices.map.model.Cell;
import com.lazybot.microservices.map.model.Range;
import com.sun.xml.internal.ws.runtime.config.Tubelines;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CellManager {
    public List<Position> findPath(List<ArrayList<Cell>> map, Position start, Position end) throws EmptyMapException {
        if (map == null || map.isEmpty())
            throw new EmptyMapException();

        map = calculMapCosts(map, start, end);

        List<Position> paths = new ArrayList<>();
        List<Position> squareCell = new ArrayList<>();

        return paths;
    }

    public Cell findBestCell(List<ArrayList<Cell>> map, Cell cell) {
        Cell bestCell = new Cell(9999.0, 9999.0, 9999.0, null, 0, 0);

        Range range = verifyRangeCell(map.get(0).size(), cell);
        for (int i = range.getZMin(); i <= range.getZMax(); i++) {
            for (int j = range.getXMin(); j <= range.getXMax(); j++ ) {
                if (map.get(i).get(j) != cell && map.get(i).get(j).getFCost() != -1.0) {
                    // On regarde si l'une dans elle a un FCost inférieur à la meilleure déjà trouvée, si elle en a un
                    // elle devient la bestCell
                    if (map.get(i).get(j).getFCost() < bestCell.getFCost()) {
                        bestCell = map.get(i).get(j);
                    }

                    // Si on en trouve une qui a un FCost égal, on compare le HCost (distance avec la fin), et si elle en a
                    // inférieur elle devient la bestCell.
                    else if (map.get(i).get(j).getFCost().equals(bestCell.getFCost()) &&
                            map.get(i).get(j).getHCost() < bestCell.getHCost()) {
                        bestCell = map.get(i).get(j);
                    }
                } else {
                }
            }
        }
        return bestCell;
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

    /*
    Calcul the costs. If the cell is the start or the end the costs must be equals to -1
     */
    public List<ArrayList<Cell>> calculMapCosts(List<ArrayList<Cell>> map, Position start, Position end) throws NullPointerException {
        if (map.isEmpty())
            return null;
        for (int i = 0; i < map.size(); i++) {
            for (int j = 0; j < map.get(i).size(); j++) {
                if (i == start.getZ() && j == start.getX()) {
                    Cell cell = map.get(i).get(j);
                    cell.setGCost(-1.0);
                    cell.setHCost(-1.0);
                    cell.setFCost(-1.0);
                    map.get(i).set(j, cell);
                } else if (i == end.getZ() && j == end.getX()) {
                    Cell cell = map.get(i).get(j);
                    cell.setGCost(-1.0);
                    cell.setHCost(-1.0);
                    cell.setFCost(-1.0);
                    map.get(i).set(j, cell);
                } else {
                    map.get(i).set(j, calculCosts(map.get(i).get(j), start, end));
                }
            }
        }
        return map;
    }

    /*
    Calcul the gCost, hCost and fCost of a cell.
    Params : cell to calcul the costs, the position of the tart, the position of the end.
     */
    public Cell calculCosts(Cell cell, Position start, Position end) {
        cell.setGCost(Math.sqrt(
                Math.pow(cell.getPosition().getX() - start.getX(), 2) +
                        Math.pow(cell.getPosition().getZ() - start.getZ(), 2)
        ));

        cell.setHCost(Math.sqrt(
                Math.pow(cell.getPosition().getX() - end.getX(), 2) +
                        Math.pow(cell.getPosition().getZ() - end.getZ(), 2)
        ));

        cell.setFCost(cell.getGCost() + cell.getHCost());

        return cell;
    }

    public Range verifyRangeCell(int mapSize, Cell cell) {
        Range range = new Range();

        if (cell.getPosition().getX() == 0) {
            range.setXMin(cell.getPosition().getX());
            range.setXMax(cell.getPosition().getX() + 1);
        } else if (cell.getPosition().getX() == mapSize - 1) {
            range.setXMin(cell.getPosition().getX() - 1);
            range.setXMax(cell.getPosition().getX());
        } else {
            range.setXMin(cell.getPosition().getX() - 1);
            range.setXMax(cell.getPosition().getX() + 1);
        }

        if (cell.getPosition().getZ() == 0) {
            range.setZMin(cell.getPosition().getZ());
            range.setZMax(cell.getPosition().getZ() + 1);
        } else if (cell.getPosition().getZ() == mapSize - 1) {
            range.setZMin(cell.getPosition().getZ() - 1);
            range.setZMax(cell.getPosition().getZ());
        } else {
            range.setZMin(cell.getPosition().getZ() - 1);
            range.setZMax(cell.getPosition().getZ() + 1);
        }

        return range;
    }
}
