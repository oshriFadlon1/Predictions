package worldPhysicalSpace;

import entity.EntityInstance;
import pointCoord.PointCoord;

public class WorldPhysicalSpace {
    private EntityInstance[][] worldSpace;
    private PointCoord worldSize;

    static int[] moveInRow = {-1,0,1,0};
    static int[] moveInCol = {0,1,0,-1};

    public WorldPhysicalSpace(EntityInstance[][] worldSpace, PointCoord worldSize) {
        this.worldSpace = worldSpace;
        this.worldSize = worldSize;
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
}
