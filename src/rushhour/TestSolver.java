package rushhour;

import java.io.File;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TestSolver {
    public static void main(String[] args) {
        multiTest();

//        singleTest("A00");

//        System.exit(0);
    }

    private static void singleTest(String filename) {
        Solver.solveFromFile("puzzles/" + filename + ".txt", "generated_solutions/" + filename + ".txt");
    }

    private static void multiTest() {
        File[] puzzles = new File("puzzles/").listFiles();
        assert puzzles != null;

        Vector<String> failedPuzzles = new Vector<>();

        ExecutorService es = Executors.newCachedThreadPool();

        for (File puzzle : puzzles) {

            es.execute(() -> {
                failedPuzzles.add(puzzle.getName());
                Solver.solveFromFile(puzzle.getPath(), "generated_solutions/" + puzzle.getName());
//                    System.out.println("Solved " + puzzle);
//                } else {
//                    System.out.println("Couldn't solve " + puzzle);
//                }
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
        if (failedPuzzles.isEmpty()) {
            System.out.println("All puzzles were successfully solved!");
        }

    }

}
