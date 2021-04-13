package rushhour;

import java.awt.*;
import java.util.Objects;

public class Car {

    private final Point pos;
    private final int orientation;
    private final int length;
    private final char colour;

    public Car(Point pos, int orientation, int length, char colour) {
        this.pos = pos;
        this.orientation = orientation;
        this.length = length;
        this.colour = colour;
    }

    //TODO: comments throughout

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
