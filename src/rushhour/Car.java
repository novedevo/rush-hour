package rushhour;

import java.awt.*;
import java.util.Objects;

public class Car {

    private final Point pos;
    private final int orientation;
    private final int length;
    private final char colour;

    /**
     * JBOD constructor for the Car class
     *
     * @param pos         a point denoting the top left corner of the car
     * @param orientation as specified by the constants in RushHour.java
     * @param length      either 2 or 3
     * @param colour      a char denoting the name/colour of the car
     */
    public Car(Point pos, int orientation, int length, char colour) {
        this.pos = pos;
        this.orientation = orientation;
        this.length = length;
        this.colour = colour;
    }


    //getters, no setters. this is an immutable class.
    //perhaps you also have some experience teaching classes which are overly loud!

    public final int getOrientation() {
        return orientation;
    }

    public final char getColour() {
        return colour;
    }

    public final int getLength() {
        return length;
    }

    public Point getPos() {
        return pos;
    }

    public boolean isVictorious() {
        return (colour == 'X' && pos.x >= 4);
    }

    /**
     * equality comparison is required for hashsets
     *
     * @param o the other object being compared
     * @return boolean denoting equality
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return orientation == car.orientation && length == car.length && colour == car.colour && pos.equals(car.pos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pos.x, pos.y, orientation, length, colour);
    }
}
