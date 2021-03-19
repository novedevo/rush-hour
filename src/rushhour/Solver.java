package rushhour;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

public class Solver {
    public static boolean solveFromFile(String input, String output) {
        RushHour a;
        try {
            a = new RushHour(input);
        } catch (Exception e) {
            return false;
        }
        return solve(a);
    }

    public static boolean solve(RushHour board) {
        return breadthFirstSearch(board);
    }

    public static boolean breadthFirstSearch(RushHour board) {
        if (board.isSolved()) return true;

        HashSet<RushHour> visited = new HashSet<>();
        visited.add(board);

        LinkedList<RushHour> queue = new LinkedList<>();
        queue.add(board);
        int i = 0;
        while (!queue.isEmpty()){
            board = queue.poll();
            for (RushHour newBoard : board.moves()) {
                if (newBoard.isSolved()) {
                    return true;
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
            i++;
        }
        return false;
    }
}
