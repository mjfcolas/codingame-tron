package fr.li212.codingame.tron.infrastructure.newvoronoi;

import fr.li212.codingame.tron.adapters.grid.SquareCoordinate;
import fr.li212.codingame.tron.domain.grid.port.Cell;
import fr.li212.codingame.tron.domain.grid.port.Coordinate;
import fr.li212.codingame.tron.domain.grid.port.Grid;

import java.util.*;
import java.util.stream.Collectors;

public class VoronoiSpaceProvider {

    private final Grid gridToCompute;
    private final int width;
    private final int height;

    final GermWithDistance[][] distancesByCell;

    public VoronoiSpaceProvider(final Grid gridToCompute) {
        this.gridToCompute = gridToCompute;
        this.width = gridToCompute.getWidth();
        this.height = gridToCompute.getHeight();
        this.distancesByCell = new GermWithDistance[width][height];
    }

    public static VoronoiSpaceProvider get(final Grid gridToCompute) {
        return new VoronoiSpaceProvider(gridToCompute);
    }

    public Map<Coordinate, Set<Coordinate>> get(final Collection<Coordinate> germs) {

        this.computeAllCells(germs);
        final Map<Coordinate, Set<Coordinate>> result = this.getVoronoiSpaceSizesByGerm(germs);
        //PrintVoronoiSpaces.print(gridToCompute, result);
        return result;
    }

    private void computeAllCells(final Collection<Coordinate> germs) {
        for (Coordinate currentGerm : germs) {
            VoronoiNode[][] copiedGrid = initialize(this.gridToCompute);
            VoronoiNode startNode = copiedGrid[currentGerm.getX()][currentGerm.getY()];
            startNode.setDistance(0);
            startNode.setClosed();
            final Queue<VoronoiNode> openSet = new PriorityQueue<>();
            openSet.add(startNode);
            while (!openSet.isEmpty()) {
                VoronoiNode currentNode = openSet.remove();
                final Collection<VoronoiNode> notClosedNeighbours = this.getNeighbours(currentNode, copiedGrid);
                for (VoronoiNode neighbour : notClosedNeighbours) {
                    neighbour.setDistance(currentNode.getDistance() + 1);
                    neighbour.setClosed();
                    openSet.add(neighbour);
                    final Coordinate neighbourCoordinates = neighbour.getUnderlyingCoordinates();
                    if (distancesByCell[neighbourCoordinates.getX()][neighbourCoordinates.getY()] == null
                            || distancesByCell[neighbourCoordinates.getX()][neighbourCoordinates.getY()].getDistance() > neighbour.getDistance()) {
                        distancesByCell[neighbourCoordinates.getX()][neighbourCoordinates.getY()] = new GermWithDistance(currentGerm, neighbour.getDistance());
                    } else if (distancesByCell[neighbourCoordinates.getX()][neighbourCoordinates.getY()].getDistance().equals(neighbour.getDistance())) {
                        distancesByCell[neighbourCoordinates.getX()][neighbourCoordinates.getY()].setGerm(null);
                    }
                }
            }
        }
    }

    private Map<Coordinate, Set<Coordinate>> getVoronoiSpaceSizesByGerm(final Collection<Coordinate> germs) {
        final Map<Coordinate, Set<Coordinate>> result = germs.stream().collect(Collectors.toMap(
                germCoordinate -> germCoordinate,
                coordinate -> new HashSet<>()
        ));

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                final GermWithDistance currentCell = distancesByCell[x][y];
                if (currentCell != null && currentCell.getGerm() != null) {
                    result.get(currentCell.getGerm()).add(new SquareCoordinate(x, y));
                }
            }
        }
        return result;
    }

    private List<VoronoiNode> getNeighbours(final VoronoiNode currentNode, final VoronoiNode[][] copiedGrid) {
        final List<Coordinate> neighbours = gridToCompute.getNeighbours(currentNode.getUnderlyingCoordinates());
        List<VoronoiNode> result = new ArrayList<>(4);
        for (Coordinate coordinate : neighbours) {
            final VoronoiNode potentialNeighbour = copiedGrid[coordinate.getX()][coordinate.getY()];
            if (!potentialNeighbour.isClosed()) {
                result.add(potentialNeighbour);
            }
        }
        return result;
    }


    private VoronoiNode[][] initialize(final Grid gridContainer) {
        final Cell[][] grid = gridContainer.getCells();
        final int width = gridContainer.getWidth();
        final int height = gridContainer.getHeight();
        final VoronoiNode[][] copiedGrid = new VoronoiNode[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                copiedGrid[x][y] = new VoronoiNode(grid[x][y].getCoordinate());
            }
        }
        return copiedGrid;
    }
}
