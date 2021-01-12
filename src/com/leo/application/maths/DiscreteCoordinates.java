/*
 *	Author:      Leonard Cseres
 *	Date:        27.12.20
 *	Time:        19:49
 */

package com.leo.application.maths;

import java.util.ArrayList;
import java.util.List;

public final class DiscreteCoordinates {

    public static DiscreteCoordinates ORIGIN = new DiscreteCoordinates(0, 0);
    public final int x;
    public final int y;

    public DiscreteCoordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public DiscreteCoordinates left(){
        return new DiscreteCoordinates(x-1, y);
    }

    public DiscreteCoordinates right(){
        return new DiscreteCoordinates(x+1, y);
    }

    public DiscreteCoordinates up(){
        return new DiscreteCoordinates(x, y+1);
    }

    public DiscreteCoordinates down(){
        return new DiscreteCoordinates(x, y-1);
    }
    
    public List<DiscreteCoordinates> getNeighbours(){
        List<DiscreteCoordinates> result = new ArrayList<>();
        result.add(left());
        result.add(up());
        result.add(right());
        result.add(down());
        return result;
    }

    public DiscreteCoordinates opposite(){
        return new DiscreteCoordinates(x*(-1), y*(-1));
    }

    public DiscreteCoordinates jump(int dx, int dy){
        return new DiscreteCoordinates(x+dx, y+dy);
    }

    public float manhattanDist(DiscreteCoordinates other){
        return (float) Math.sqrt((this.x-other.x)*(this.x-other.x) + (float) (this.y-other.y)*(this.y-other.y));
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(y) ^ Integer.hashCode(x);
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || !(object instanceof DiscreteCoordinates))
            return false;
        else {
            DiscreteCoordinates other = (DiscreteCoordinates)object;
            return x == other.x && y == other.y;
        }
    }

    @Override
    public String toString() {
        return "(x: "+ x +", y: "+ y +")";
    }
}
