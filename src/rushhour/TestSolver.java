package rushhour;

import java.io.File;
import java.util.Vector; // like an arraylist, but threadsafe!
import java.util.concurrent.ExecutorService; //for the threadpool
import java.util.concurrent.Executors; //same
import java.util.concurrent.TimeUnit; //to limit the threadpool to 10 seconds of execution

public class TestSolver {
    public static void main(String[] args) {
        multiTest();
    }

    // useful for debugging, to not deal with concurrency
    private static void singleTest(String filename) {
        Solver.solveFromFile("puzzles/" + filename + ".txt", "generated_solutions/" + filename + ".txt");
    }

    //executes every puzzle in the directory concurrently
    private static void multiTest() {
        File[] puzzles = new File("puzzles/").listFiles(); //enumerate all files in the directory
        assert puzzles != null; //if there are no puzzles in the directory, it has all gone wrong

        Vector<String> failedPuzzles = new Vector<>(); //like an arraylist, but threadsafe

        ExecutorService es = Executors.newCachedThreadPool();

        for (File puzzle : puzzles) {
            failedPuzzles.add(puzzle.getName());
            //there's a lambda function here that gets passed into the executor for it to complete on its own time, using any thread it wants to
            //since each puzzle is independent, we don't need any fancy concurrency ideas besides the threadsafe vector.
            es.execute(() -> {
                Solver.solveFromFile(puzzle.getPath(), "generated_solutions/" + puzzle.getName());
                failedPuzzles.remove(puzzle.getName()); //since the threadpool could get killed at any moment, this line
                //ensures that if that happens, the puzzle stays in the failed vector.
            });
        }

        boolean finished = false;
        try {
            es.shutdown(); //declare that it can no longer accept new tasks
            finished = es.awaitTermination(10, TimeUnit.SECONDS); //10 seconds for all 35 puzzles seems reasonable
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //for debugging purposes, we print any puzzles that failed to solve or timed out
        for (String puzzle : failedPuzzles) {
            System.out.println(puzzle + " was never solved.");
        }
        if (finished && failedPuzzles.isEmpty()) {
            System.out.println("All puzzles were successfully solved!");
        }

    }

}
