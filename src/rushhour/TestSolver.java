package rushhour;

import java.io.File;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TestSolver {
    public static void main(String[] args) {
        multiTest();

        //singleTest();

        System.exit(0);
    }

    private static void singleTest() {
        System.out.println(Solver.solveFromFile("puzzles/INSTASOLVE.txt", ""));
    }

    private static void multiTest() {
        File[] puzzles = new File("puzzles/").listFiles();
        assert puzzles != null;

        Vector<String> failedPuzzles = new Vector<>();

        ExecutorService es = Executors.newCachedThreadPool();

        for (File puzzle : puzzles) {

            es.execute(() -> {
                failedPuzzles.add(puzzle.getName());
                String placeholder = "";
                if (Solver.solveFromFile(puzzle.getPath(), placeholder)) {
                    System.out.println("Solved " + puzzle);
                } else {
                    System.out.println("Couldn't solve " + puzzle);
                }
                failedPuzzles.remove(puzzle.getName());
            });
        }

        try {
            es.shutdown();
            boolean finished = es.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (String puzzle : failedPuzzles) {
            System.out.println(puzzle + " was never solved.");
        }

    }

}
