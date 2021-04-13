package rushhour;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

public class Solver {
    public static void solveFromFile(String input, String output) {
        RushHour a;
        try {
            a = new RushHour(input);
        } catch (Exception e) {
            return;
        }
        writeToFile(solve(a), output);
    }

    public static ArrayList<char[]> solve(RushHour board) {
        return aStar(board);
    }

    public static ArrayList<char[]> aStar(RushHour board) {
        var stepsArrayList = new ArrayList<char[]>();
        if (board.isSolved()) return stepsArrayList;    //if board is already solved, return an empty ArrayList

        HashSet<RushHour> visited = new HashSet<>();
        visited.add(board);

        var stack = new Stack<RushHour>();
        stack.add(board);

        var map = new HashMap<RushHour, ArrayList<char[]>>();   //create map to link a board with the steps leading to it
        map.put(board, stepsArrayList);                         //add board to map along with empty ArrayList

        while (!stack.isEmpty()){
            board = stack.pop();
            for (RushHour newBoard : board.moves()) {           //iterate through possible next moves
                if (!visited.contains(newBoard)){
                    visited.add(newBoard);
                    stack.add(newBoard);
                    stepsArrayList = map.get(board);                //load the steps to the board
                    stepsArrayList.add(board.boardDiff(newBoard));  //add steps to newBoard
                    map.put(newBoard, stepsArrayList);              //add full steps to map for newBoard
                }
                if (newBoard.isSolved()) { return map.get(newBoard); }
            }
            map.remove(board);      //remove parent board from the map once all children have been added

        }
        return null;
    }

    public static void writeToFile(ArrayList<char[]> steps, String filename) {
        try {
            var writer = new BufferedWriter(new FileWriter(filename));
            System.out.println(filename);
            for (char[] step : steps) {
                writer.write(String.valueOf(step));
                System.out.println(String.valueOf(step));
                writer.newLine();
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
