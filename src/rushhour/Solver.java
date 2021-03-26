package rushhour;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Stack;

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

    public static RushHour solve(RushHour board) {
        return aStar(board);
    }

    public static RushHour aStar(RushHour board) {
        if (board.isSolved()) return board;

        HashSet<RushHour> visited = new HashSet<>();
        visited.add(board);

        var stack = new Stack<RushHour>();
        stack.add(board);
        while (!stack.isEmpty()){
            board = stack.pop();
            for (RushHour newBoard : board.moves()) {
                if (newBoard.isSolved()) {
                    return newBoard;
                }
                if (!visited.contains(newBoard)){
                    visited.add(newBoard);
                    stack.add(newBoard);
                }
            }

        }
        return null;
    }

    public static void writeToFile(RushHour board, String filename) {
        try {
            var writer = new BufferedWriter(new FileWriter(filename));
            writer.write(board.toString());
            System.out.println(filename);
            System.out.println(board.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
