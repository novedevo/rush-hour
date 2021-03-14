package rushhour;

import java.io.File;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TestSolver {
    public static void main(String[] args) {
        File[] puzzles = new File("puzzles/").listFiles();
        assert puzzles != null;

        Vector<String> failedPuzzles = new Vector<>();

        ExecutorService es = Executors.newCachedThreadPool();

        for (File puzzle : puzzles) {

            es.execute(new Runnable() {
                @Override
                public void run() {
                    failedPuzzles.add(puzzle.getName());
                    String placeholder = "";
                    Solver.solveFromFile(puzzle.getPath(), placeholder);
                    failedPuzzles.remove(puzzle.getName());
                }
            });
        }

        try {
            boolean finished = es.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (String puzzle : failedPuzzles) {
            System.out.println(puzzle + " has failed to complete within the maximum allotted time of 5 seconds.");
        }

        System.exit(0);
    }
}
