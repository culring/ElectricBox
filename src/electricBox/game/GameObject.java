package electricBox.game;

import com.sun.prism.shader.DrawRoundRect_Color_AlphaTest_Loader;

/**
 * Created by Culring on 2017-08-22.
 */

public abstract class GameObject {
    protected enum Rotation{
        UP(0),
        LEFT(1),
        DOWN(2),
        RIGHT(3);

        private final int rotation;

        Rotation(int rot){
            rotation = rot;
        }

        public int getRotation(){
            return rotation;
        }
    }
    protected Rotation rotation;
    protected boolean isRotable;
    protected boolean isMovable;
    protected boolean isActivated;
    protected int collisionWidth, collisionHeight;

    {
        rotation = Rotation.RIGHT;
        isRotable = false;
        isMovable = false;
        isActivated = false;
        collisionWidth = collisionHeight = 1;
        System.out.println("cos sie stalo");
    }

    public abstract void activate();

    public boolean isRotable(){
        return isRotable;
    }
    public void setRotable(){
        isRotable = true;
    }
    public void rotateLeft(){
        System.out.println(rotation);
        //int rot = rotation.getRotation();
        //rotation = Rotation.valueOf((rot+1)%4);
    }
    public boolean isMovable(){
        return isMovable;
    }
    public void setMovable(){
        isMovable = true;
    }
    public int getCollisionWidth(){
        return collisionWidth;
    }
    public void setCollisionWidth(int width){
        collisionWidth = width;
    }
    public int getCollisionHeight(){
        return collisionHeight;
    }
    public void setCollisionHeight(int height){
        collisionHeight = height;
    }
}


