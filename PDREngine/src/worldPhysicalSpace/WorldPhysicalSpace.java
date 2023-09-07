package worldPhysicalSpace;

import entity.EntityInstance;
import pointCoord.PointCoord;
import utility.Utilities;

import java.util.*;

public class WorldPhysicalSpace {
    private EntityInstance[][] worldSpace;
    private PointCoord worldSize;
    private Set<PointCoord> freeSpaces;

    public WorldPhysicalSpace(PointCoord worldSize) {
        this.worldSpace = new EntityInstance[worldSize.getRow()][worldSize.getCol()];
        this.worldSize = worldSize;
        this.freeSpaces = new HashSet<>();
        for(int i = 0; i < worldSize.getRow(); i++){
            for(int j = 0; j < worldSize.getCol(); j++){
                this.freeSpaces.add(new PointCoord(i, j));
            }
        }
    }

    public void putEntityInWorld(EntityInstance entityInstance){
        List<PointCoord> coordList = new ArrayList<>(this.freeSpaces);
        int randomIndex = Utilities.initializeRandomInt(0, this.freeSpaces.size() - 1);
        PointCoord randomPos = coordList.get(randomIndex);
        this.freeSpaces.remove(randomPos);
        this.worldSpace[randomPos.getRow()][randomPos.getCol()] = entityInstance;
        entityInstance.setPositionInWorld(randomPos);
    }

    public EntityInstance[][] getWorldSpace() {
        return worldSpace;
    }

    public void setWorldSpace(EntityInstance[][] worldSpace) {
        this.worldSpace = worldSpace;
    }

    public PointCoord getWorldSize() {
        return worldSize;
    }

    public void setWorldSize(PointCoord worldSize) {
        this.worldSize = worldSize;
    }

    public void addFreeSpaceBack(PointCoord pc){
        this.freeSpaces.add(pc);
    }

    public void moveCurrentEntity(EntityInstance currentInstance){
        List<PointCoord> freeSpaces = getFreeSpacesFromCurrentPosition(currentInstance);
        if(freeSpaces.size() != 0){
            int randomMoveIndx = Utilities.initializeRandomInt(0, freeSpaces.size() - 1);
            PointCoord move = freeSpaces.get(randomMoveIndx);
            removeEntityFromWorld(currentInstance.getPositionInWorld());
            currentInstance.setPositionInWorld(move);
            worldSpace[move.getRow()][move.getCol()] = currentInstance;
            this.freeSpaces.remove(move);
        }

    }

    private List<PointCoord> getFreeSpacesFromCurrentPosition(EntityInstance currentInstance) {
        List<PointCoord> freeSpaces = new ArrayList<>();
        if(canMoveUp(currentInstance.getPositionInWorld())){
            if(currentInstance.getPositionInWorld().getRow() != 0) {
                freeSpaces.add(new PointCoord(currentInstance.getPositionInWorld().getRow() - 1, currentInstance.getPositionInWorld().getCol()));
            }
            else{
                freeSpaces.add(new PointCoord(this.worldSize.getRow() - 1, currentInstance.getPositionInWorld().getCol()));
            }
        }
        if(canMoveDown(currentInstance.getPositionInWorld())){
            if(currentInstance.getPositionInWorld().getRow() != this.worldSize.getRow() - 1) {
                freeSpaces.add(new PointCoord(currentInstance.getPositionInWorld().getRow() + 1, currentInstance.getPositionInWorld().getCol()));
            }
            else{
                freeSpaces.add(new PointCoord(0, currentInstance.getPositionInWorld().getCol()));
            }
        }
        if(canMoveLeft(currentInstance.getPositionInWorld())){
            if(currentInstance.getPositionInWorld().getCol() != 0) {
                freeSpaces.add(new PointCoord(currentInstance.getPositionInWorld().getRow(), currentInstance.getPositionInWorld().getCol() - 1));
            }
            else{
                freeSpaces.add(new PointCoord(currentInstance.getPositionInWorld().getRow(), this.worldSize.getCol() - 1));
            }
        }
        if(canMoveRight(currentInstance.getPositionInWorld())){
            if(currentInstance.getPositionInWorld().getCol() != this.worldSize.getCol() - 1){
                freeSpaces.add(new PointCoord(currentInstance.getPositionInWorld().getRow(), currentInstance.getPositionInWorld().getCol() + 1));
            }
            else{
                freeSpaces.add(new PointCoord(currentInstance.getPositionInWorld().getRow(), 0));
            }
        }

        return freeSpaces;
    }

    private boolean canMove(int currentX, int currentY, int deltaX, int deltaY) {
        int newX = (currentX + deltaX + worldSize.getRow()) % worldSize.getRow(); // Modulo ensures circular boundary on X-axis
        int newY = (currentY + deltaY + worldSize.getCol()) % worldSize.getCol();   // Modulo ensures circular boundary on Y-axis

        // Check if the new position is blocked by an entity or obstacle
        if (worldSpace[newX][newY] == null) {
            return true; // Move is valid
        } else {
            return false; // Move is blocked
        }
    }


    private boolean canMoveUp(PointCoord currentPlace){
        return canMove(currentPlace.getRow(), currentPlace.getCol(), -1, 0);
    }

    private boolean canMoveDown(PointCoord currentPlace){
        return canMove(currentPlace.getRow(), currentPlace.getCol(), 1, 0);
    }

    private boolean canMoveLeft(PointCoord currentPlace){
        return canMove(currentPlace.getRow(), currentPlace.getCol(), 0, -1);
    }

    private boolean canMoveRight(PointCoord currentPlace){
        return canMove(currentPlace.getRow(), currentPlace.getCol(), 0, 1);
    }

    public void removeEntityFromWorld(PointCoord positionInWorld) {
        this.worldSpace[positionInWorld.getRow()][positionInWorld.getCol()] = null;
        addFreeSpaceBack(positionInWorld);
    }

    public void replaceEntities(EntityInstance createdInstance, PointCoord positionInWorld) {
        this.worldSpace[positionInWorld.getRow()][positionInWorld.getCol()] = createdInstance;
    }

    // N = ROW
    // M = Col
    // x = row
    // y = col
    private boolean isValidCoordinate(PointCoord coord) {
        return coord.getRow() > 0 && coord.getRow() <= this.worldSize.getRow() && coord.getCol() > 0 && coord.getCol() <= this.worldSize.getCol();
    }

    private PointCoord adjustCoordinate(PointCoord coord) {
        int adjustedX = (coord.getRow() - 1) % this.worldSize.getRow() + 1;
        int adjustedY = (coord.getCol() - 1) % this.worldSize.getCol() + 1;
        return new PointCoord(adjustedX, adjustedY);
    }

    public Set<PointCoord> findEnvironmentCells(PointCoord source, int rank) {
        Set<PointCoord> result = new HashSet<>();
        result.add(adjustCoordinate(source));

        for (int currentRank = 1; currentRank < rank; currentRank++) {
            Set<PointCoord> newCoordinates = new HashSet<>();
            for (PointCoord coord : result) {
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        PointCoord newCoord = new PointCoord(coord.getRow() + dx, coord.getCol() + dy);
                        if (!newCoord.equals(coord) && isValidCoordinate(newCoord)) {
                            newCoordinates.add(adjustCoordinate(newCoord));
                        }
                    }
                }
            }
            result.addAll(newCoordinates);
        }

        return result;
    }
}
