package rushhour;

import java.awt.*;
import java.io.File;
import java.util.ArrayList; //storing the cars
import java.util.HashSet; //storing the colours
import java.util.Scanner; //reading from files

public class RushHour {
    // constants for cardinal directions
    //this could also be accomplished with a boolean for verticality checking
    //or an enum, for that matter
    public final static int UP = 0;
    public final static int DOWN = 2;
    public final static int LEFT = 1;
    public final static int RIGHT = 3;

    //technically, this could probably work on boards of any size.
    //functionality not guaranteed!
    public final static int SIZE = 6;

    //stores the SIZE*SIZE char array representation of the board
    private final char[][] boardChars;
    //keeps track of the canonical list of cars composing the board
    private final ArrayList<Car> cars;

    /**
     * for internal generation of boards during neighbour state iteration
     *
     * @param newCars list of cars to generate the board from
     */
    private RushHour(ArrayList<Car> newCars) {
        this.cars = newCars;
        this.boardChars = generateBoardChars(newCars);
    }

    /**
     * @param fileName Reads a board from file and creates the board
     * @throws Exception if the file not found or the board is bad
     *                   Initial constructor for RushHour boards
     */
    public RushHour(String fileName) throws Exception {

        Scanner boardScanner = new Scanner(new File(fileName));
        boardChars = new char[SIZE][SIZE]; //allocate / initialize array
        cars = new ArrayList<>();

        HashSet<Character> colours = new HashSet<>();
        colours.add('.'); //to avoid making a fake car from the pavement

        //copy each line of the puzzle file into our char array
        int lineIndex = 0;
        do {
            boardChars[lineIndex] = (boardScanner.nextLine().toCharArray()); //this could fail if the board is
            // improperly formatted, in which case it would throw an exception
            lineIndex++;
        } while (boardScanner.hasNext());

        //generate our list of cars from the char array
        for (int i = 0; i < SIZE; i++) {
            char[] line = boardChars[i];
            for (int j = 0; j < SIZE; j++) {
                char colour = line[j];

                //if this is a new car we have not yet seen
                if (!colours.contains(colour)) {
                    colours.add(colour);
                    Point pos = new Point(j, i); //seems backwards, but row:column <=> y:x

                    int orientation = determineOrientation(pos, boardChars);
                    int length = determineLength(pos, orientation, boardChars);

                    cars.add(new Car(pos, orientation, length, colour));
                }
            }
        }
    }

    /**
     * @param pt     Name of point to be moved
     * @param dir    Direction to move the point
     * @param length Distance to move the point
     *               Modifies the given point to be *length* units further in the given direction
     */
    public static void movePoint(Point pt, int dir, int length) {
        //this utility helper function is a glorified switch
        switch (dir) {
            case UP: {
                pt.translate(0, -length);
                break;
            }
            case DOWN: {
                pt.translate(0, length);
                break;
            }
            case LEFT: {
                pt.translate(-length, 0);
                break;
            }
            case RIGHT: {
                pt.translate(length, 0);
                break;
            }
            default:
                throw new IllegalStateException("Unexpected value in movePoint: " + dir);
        }
    }

    /**
     * Helper function for RushHour.moves() above
     * creates new car, from which it creates a list of cars, from which it creates a RushHour board and
     * adds it to the provided ArrayList
     *
     * @param newPoint new position of the moved car
     * @param car      the car to be modified
     * @param cars     list of cars from the original board
     * @param moves    list of moves to which the newly constructed board is added
     */
    public static void addToMoves(Point newPoint, Car car, ArrayList<Car> cars, ArrayList<RushHour> moves) {
        Car newCar = new Car(newPoint, car.getOrientation(), car.getLength(), car.getColour()); //create the new car
        ArrayList<Car> newCars = new ArrayList<>();
        for (Car oldCar : cars) {
            if (newCar.getColour() == oldCar.getColour()) {
                newCars.add(newCar); //swaps the car in place for the new one
            } else {
                newCars.add(oldCar);
            }
        }
        moves.add(new RushHour(newCars));
    }

    /**
     * @return true if and only if the board is solved,
     * i.e., the XX car is touching the right edge of the board
     */
    public boolean isSolved() {
        for (Car car : cars) {
            if (car.isVictorious()) {
                return true;
            }
        }
        return false;
    }

    /**
     * calculates all possible moves from this position
     * akin to getting neighbours in a traditional graph
     *
     * @return list of all adjacent (neighbouring) boards
     */
    public ArrayList<RushHour> moves() {
        ArrayList<RushHour> moves = new ArrayList<>();
        for (Car car : cars) { //we need to check all possible moves for each individual car
            var pos = car.getPos();
            if (car.getOrientation() == RIGHT) {  //a.k.a. horizontal
                for (int i = 1; i <= 4; i++) {
                    if (pos.x - i >= 0 && boardChars[pos.y][pos.x - i] == '.') { //if there is room to move in this direction
                        addToMoves(new Point(pos.x - i, pos.y), car, cars, moves);
                    } else { //there is a car or wall in the way, this direction is finished
                        break;
                    }
                }
                for (int i = 1; i <= 4; i++) {
                    if (pos.x + car.getLength() + i <= 5 && boardChars[pos.y][pos.x + car.getLength() + i] == '.') {
                        addToMoves(new Point(pos.x + i, pos.y), car, cars, moves);
                    } else {
                        break;
                    }
                }
            } else { //a.k.a. vertical
                for (int i = 1; i <= 4; i++) {
                    if (pos.y - i >= 0 && boardChars[pos.y - i][pos.x] == '.') {
                        addToMoves(new Point(pos.x, pos.y - i), car, cars, moves);
                    } else {
                        break;
                    }
                }
                for (int i = 1; i <= 4; i++) {
                    if (pos.y + car.getLength() + i <= 5 && boardChars[pos.y + car.getLength() + i][pos.x] == '.') {
                        addToMoves(new Point(pos.x, pos.y + i), car, cars, moves);
                    } else {
                        break;
                    }
                }
            }
        }
        return moves;
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
        RushHour rushHour = (RushHour) o;
        return cars.equals(rushHour.cars);
    }

    @Override
    public int hashCode() {
        return cars.hashCode();
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        for (var line : boardChars) {
            out.append(new String(line));
            out.append("\n");
        }
        return out.toString();
    }

    /**
     * Regenerates the array of characters symbolizing the playing board from positions, directions,
     * and orientations of the cars in the list of cars.
     *
     * @return SIZE*SIZE char array representing the board
     */
    private char[][] generateBoardChars(ArrayList<Car> cars) {
        var tempBoard = new char[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            tempBoard[i] = "......".toCharArray(); //initialize as empty board
        }
        for (Car car : cars) {
            Point pos = new Point(car.getPos()); //copying to avoid changing the position of the actual car
            char colour = car.getColour();
            int length = car.getLength();
            int dir = car.getOrientation();

            for (int i = 0; i <= length; i++) { //fill in (length + 1) cells in boardChars with the colour of the car
                tempBoard[pos.y][pos.x] = colour;
                movePoint(pos, dir, 1); //prep for next square
            }
        }
        return tempBoard;
    }

    /**
     * @param pos Position of car whose orientation is to be determined
     *            Checks the surrounding grid of chars to determine which direction the car is facing
     *            Can only return DOWN or RIGHT as it assumes that cars are being checked left to right, top to bottom.
     *            Only called in constructor
     */
    private int determineOrientation(Point pos, char[][] tempChars) {
        char colour = tempChars[pos.y][pos.x];

        if (pos.y == SIZE - 1) {
            return RIGHT; //at bottom layer; guards against index out of bounds exceptions
        } else if (tempChars[pos.y + 1][pos.x] == colour) { //if the cell directly below us is the same colour, we are vertical
            return DOWN;
        } else return RIGHT;

    }

    /**
     * @param pos         Origin point of the car
     * @param orientation Direction that the car is facing
     *                    Determines the length of a contiguous car in the given board
     *                    Only called in the constructor
     */
    private int determineLength(Point pos, int orientation, char[][] tempChars) {
        char colour = tempChars[pos.y][pos.x];
        Point pt = new Point(pos);

        int length = 0;

        //since we only deal with 2,3 length cars, this could be simplified
        //but this implementation is more general

        while (true) {

            movePoint(pt, orientation, 1);
            char squareColour;

            try {
                squareColour = tempChars[pt.y][pt.x];
            } catch (IndexOutOfBoundsException e) { //hit the edge of the board
                break;
            }

            if (squareColour != colour) break;//hit another car or empty space

            length++;
        }
        return length;
    }

    /**
     * @param endBoard Board to end at
     * @return the step taken between this board and the end board
     * Ex: ['X','R','1'] would mean X was moved to the right by 1
     */
    public char[] boardDiff(RushHour endBoard) {
        char[] step = new char[3];
        if (this.equals(endBoard)) throw new IllegalArgumentException();
        for (int i = 0; i < this.cars.size(); i++) {         //iterate through cars
            Point startCarPos = this.cars.get(i).getPos();
            Point endCarPos = endBoard.cars.get(i).getPos();
            if (startCarPos != endCarPos) {                 //when the car that moved is found
                step[0] = this.cars.get(i).getColour();     //determine which colour it is
                if (endCarPos.x - startCarPos.x > 0) {      //determine by how much it moved and in which direction
                    step[1] = 'R';
                    step[2] = (char) (Math.abs(endCarPos.x - startCarPos.x) + '0');
                } else if (endCarPos.x - startCarPos.x < 0) {
                    step[1] = 'L';
                    step[2] = (char) (Math.abs(endCarPos.x - startCarPos.x) + '0');
                } else if (endCarPos.y - startCarPos.y > 0) {
                    step[1] = 'D';
                    step[2] = (char) (Math.abs(endCarPos.y - startCarPos.y) + '0');
                } else if (endCarPos.y - startCarPos.y < 0) {
                    step[1] = 'U';
                    step[2] = (char) (Math.abs(endCarPos.y - startCarPos.y) + '0');
                }
            }
        }
        return step;
    }
}
