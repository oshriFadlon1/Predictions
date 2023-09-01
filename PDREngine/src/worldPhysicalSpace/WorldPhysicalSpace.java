package worldPhysicalSpace;

import entity.EntityInstance;
import pointCoord.PointCoord;
import utility.Utilities;

import java.util.*;

public class WorldPhysicalSpace {
    private EntityInstance[][] worldSpace;
    private PointCoord worldSize;
    private Set<PointCoord> freeSpaces;

    static int[] moveInRow = {-1,0,1,0};
    static int[] moveInCol = {0,1,0,-1};

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
        int randomIndex = Utilities.initializeRandomInt(0, this.freeSpaces.size());
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

    private boolean canMoveUp(PointCoord currentPlace){

        return this.worldSpace[(currentPlace.getRow() + moveInRow[0]) % worldSize.getRow()][(currentPlace.getCol() + moveInCol[0]) % worldSize.getCol()] == null ;
    }

    private boolean canMoveDown(PointCoord currentPlace){
        return this.worldSpace[(currentPlace.getRow() + moveInRow[2]) % worldSize.getRow()][(currentPlace.getCol() + moveInCol[2]) % worldSize.getCol()] == null;
    }

    private boolean canMoveLeft(PointCoord currentPlace){
        return this.worldSpace[(currentPlace.getRow() + moveInRow[3]) % worldSize.getRow()][(currentPlace.getCol() + moveInCol[3]) % worldSize.getCol()] == null;
    }

    private boolean canMoveRight(PointCoord currentPlace){
        return this.worldSpace[(currentPlace.getRow() + moveInRow[1]) % worldSize.getRow()][(currentPlace.getRow() + moveInCol[1]) % worldSize.getCol()] == null ;
    }

    public void removeEntityFromWorld(PointCoord positionInWorld) {
        this.worldSpace[positionInWorld.getRow()][positionInWorld.getCol()] = null;
    }

    public void replaceEntities(EntityInstance createdInstance, PointCoord positionInWorld) {
        this.worldSpace[positionInWorld.getRow()][positionInWorld.getCol()] = createdInstance;
    }
}
