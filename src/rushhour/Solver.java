package rushhour;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

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
        return breadthFirstSearch(board);
    }

    public static RushHour breadthFirstSearch(RushHour board) {
        if (board.isSolved()) return board;

        HashSet<RushHour> visited = new HashSet<>();
        visited.add(board);

        LinkedList<RushHour> queue = new LinkedList<>();
        queue.add(board);
//        int i = 0;
        while (!queue.isEmpty()){
            board = queue.poll();
            for (RushHour newBoard : board.moves()) {
                if (newBoard.isSolved()) {
                    return newBoard;
                }
//                newBoard.printRedCar();
                if (!visited.contains(newBoard)){
                    visited.add(newBoard);
                    queue.add(newBoard);
                }
            }
//            if (i%100 == 0) {
//                System.out.println(i);
//            }
//            i++;
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
