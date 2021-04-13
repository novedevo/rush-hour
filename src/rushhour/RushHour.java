package rushhour;

import java.awt.*;
import java.io.File;
import java.util.*;

public class RushHour {
    public final static int UP = 0;
    public final static int DOWN = 2;
    public final static int LEFT = 1;
    public final static int RIGHT = 3;

    public final static int SIZE = 6;

    private final char[][] boardChars;
    private final ArrayList<Car> cars;

//    public int distanceFromRoot;
//    public final int blockingHeuristic;

//    private final Car VICTORY_CAR = new Car(new Point(4, 2), RIGHT, 3, 'X');

    public RushHour(ArrayList<Car> newCars) {
        this.cars = newCars;
        this.boardChars = generateBoardChars();
//        this.distanceFromRoot = distanceFromRoot;
//        blockingHeuristic = generateBlockingHeuristic();
    }

    /**
     * @param fileName Reads a board from file and creates the board
     * @throws Exception if the file not found or the board is bad
     */
    public RushHour(String fileName) throws Exception {

        Scanner boardScanner = new Scanner(new File(fileName));
        var tempChars = new char[SIZE][SIZE];
        cars = new ArrayList<>();

        HashSet<Character> colours = new HashSet<>();
        colours.add('.');

        int lineIndex = 0;
        do {
            tempChars[lineIndex] = (boardScanner.nextLine().toCharArray());
            lineIndex++;
        } while (boardScanner.hasNext());

        for (int i = 0; i < SIZE; i++) {
            char[] line = tempChars[i];
            for (int j = 0; j < SIZE; j++) {
                char colour = line[j];

                if (!colours.contains(colour)) {
                    colours.add(colour);
                    Point pos = new Point(j, i);

                    int orientation = determineOrientation(pos, tempChars);
                    int length = determineLength(pos, orientation, tempChars);

                    cars.add(new Car(pos, orientation, length, colour));

                }
            }
        }
        boardChars = generateBoardChars();
//        distanceFromRoot = 0;

//        blockingHeuristic = generateBlockingHeuristic();

    }

    /**
     * @param pt     Name of point to be moved
     * @param dir    Direction to move the point
     * @param length Distance to move the point
     *               Modifies the given point to be *length* units further in the given direction
     */
    public static void movePoint(Point pt, int dir, int length) {
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
     * @return true if and only if the board is solved,
     * i.e., the XX car is touching the right edge of the board
     */
    public boolean isSolved() {
        for (Car car: cars) {
            if (car.isVictorious()){
                return true;
            }
        }
        return false;
    }

    public ArrayList<RushHour> moves() {
        ArrayList<RushHour> moves = new ArrayList<>();
        for (Car car: cars) {
            var pos = car.getPos();
            if (car.getOrientation() == RIGHT) {
                for (int i = 1; i <= 4; i++) {
                    if (pos.x - i >= 0 && boardChars[pos.y][pos.x-i] == '.') {
                        addToMoves(new Point(pos.x-i, pos.y), car, cars, moves);
                    }
                    else {
                        break;
                    }
                }
                for (int i = 1; i <= 4; i++) {
                    if (pos.x + car.getLength() + i <= 5 && boardChars[pos.y][pos.x + car.getLength() + i ] == '.'){
                        addToMoves(new Point(pos.x+i, pos.y), car, cars, moves);
                    }
                    else {
                        break;
                    }
                }
            }
            else {
                for (int i = 1; i <= 4; i++) {
                    if (pos.y - i >= 0 && boardChars[pos.y - i][pos.x] == '.') {
                        addToMoves(new Point(pos.x, pos.y - i), car, cars, moves);
                    }
                    else {
                        break;
                    }
                }
                for (int i = 1; i <= 4; i++) {
                    if (pos.y + car.getLength() + i <= 5 && boardChars[pos.y + car.getLength() + i ][pos.x] == '.'){
                        addToMoves(new Point(pos.x, pos.y+i), car, cars, moves);
                    }
                    else {
                        break;
                    }
                }
            }
        }
        return moves;
    }

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
        for (var line: boardChars) {
            out.append(new String(line));
            out.append("\n");
        }
        return out.toString();
    }

//    public void printRedCar() {
//        for (Car car: cars) {
//            if (car.getColour() == 'X') {
//                System.out.println(car.getPos());
//            }
//        }
//    }

    public static void addToMoves(Point newPoint, Car car, ArrayList<Car> cars, ArrayList<RushHour> moves) {
        Car newCar = new Car(newPoint, car.getOrientation(), car.getLength(), car.getColour());
        ArrayList<Car> newCars = new ArrayList<>();
        for (Car oldCar: cars) {
            if (newCar.getColour() == oldCar.getColour()){
                newCars.add(newCar);
            }
            else {
                newCars.add(oldCar);
            }
        }
        moves.add(new RushHour(newCars));
    }

    /**
     * Regenerates the array of characters symbolizing the playing board from positions, directions,
     * and orientations of the cars in the list of cars.
     */
    private char[][] generateBoardChars() {
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

    private int generateBlockingHeuristic() {
        int h = 0;
        for (int i = 2; i < 6; i++) {
            if (boardChars[2][i] != 'X' && boardChars[2][i] != '.'){
                h++;
            }
        }
        return h;
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
        } else if (tempChars[pos.y + 1][pos.x] == colour) {
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

        while (true) {

            movePoint(pt, orientation, 1);
            char squareColour;

            try {
                squareColour = tempChars[pt.y][pt.x];
            } catch (IndexOutOfBoundsException e) { //hit the edge of the board
                break;
            }

            if (squareColour != colour) //hit another car or empty space
            {
                break;
            }

            length++;
        }
        return length;
    }

    /**
     * @param endBoard      Board to end at
     *                      Returns the step taken between this board and the end board
     *                      Ex: ['X','R','1'] would mean X was moved to the right by 1
     */
    public char[] boardDiff(RushHour endBoard) {
        var step = new char[3];
        if (this.equals(endBoard)) throw new IllegalArgumentException();
        for (int i = 0; i < this.cars.size(); i++){         //iterate through cars
            Point startCarPos = this.cars.get(i).getPos();
            Point endCarPos = endBoard.cars.get(i).getPos();
            if (startCarPos != endCarPos) {                 //when the car that moved is found
                step[0] = this.cars.get(i).getColour();     //determine which colour it is
                if (endCarPos.x - startCarPos.x > 0) {      //determine by how much it moved and in which direction
                    step[1] = 'R';
                    step[2] = (char)(Math.abs(endCarPos.x - startCarPos.x)+'0');
                }
                else if (endCarPos.x - startCarPos.x < 0) {
                    step[1] = 'L';
                    step[2] = (char)(Math.abs(endCarPos.x - startCarPos.x)+'0');
                }
                else if (endCarPos.y - startCarPos.y > 0) {
                    step[1] = 'D';
                    step[2] = (char)(Math.abs(endCarPos.y - startCarPos.y)+'0');
                }
                else if (endCarPos.y - startCarPos.y < 0) {
                    step[1] = 'U';
                    step[2] = (char)(Math.abs(endCarPos.y - startCarPos.y)+'0');
                }
            }
        }
        return step;
    }
}
