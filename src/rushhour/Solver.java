package rushhour;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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
        var stepsArrayList = new ArrayList<char[]>();
        if (board.isSolved()) return stepsArrayList;    //if board is already solved, return an empty ArrayList

        HashSet<RushHour> visited = new HashSet<>();
        visited.add(board);

        Queue<RushHour> queue = new LinkedList<>();
        queue.add(board);

        var map = new HashMap<RushHour, ArrayList<char[]>>();   //create map to link a board with the steps leading to it
        map.put(board, stepsArrayList);                         //add board to map along with empty ArrayList

        while (!queue.isEmpty()){
            board = queue.poll();
            for (RushHour newBoard : board.moves()) {           //iterate through possible next moves
                if (!visited.contains(newBoard)){
                    visited.add(newBoard);
                    queue.add(newBoard);
                    stepsArrayList = new ArrayList<>(map.get(board));   //make a deep clone of the steps to reach board
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
            if (steps == null) {
                writer.write("No steps required; board already solved.");
                writer.flush();
                writer.close();
                return;
            }
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
