# DOCUMENTATION

## Design decisions
### Classes
#### Car

#### RushHour
#### Solver
The Solver class, as one would assume, handles everything to do with actually solving the boards. The main function, solve(), uses a basic BFS algorithm to move through a graph of valid board states to find a solution, recording the steps along the way. The function writeToFile() then handles delivering the solution to the user, both through the console and in solution files.

### Data structures
The primary data structures used in our project have been Queues, Priority Queues, Stacks, and HashMaps. HashMaps were mostly used to keep track of the visited boards, which required us to implement hashcode() and equality comparison functions on the RushHour class. 

### Algorithms
Initially, a BFS algorithm was used to solve the boards while we worked out the rest of the program. Then, we implemented an A* heuristic-based algorithm (tested with a first-order blocking heuristic), but found that despite the theoretically improved efficiency it actually ran slower than our BFS algorithm, due to the extra time required to calculate the heuristic, and so we moved back to BFS.

## Project history and division of labour
We collaborated heavily on this project, utilizing features such as IntelliJ's "Code With Me" in order to work synchronously on the same file. For more asynchronous work, we used a shared private GitHub repository and merged our changes when needed. This workflow obfuscates who exactly wrote each line of code, but we feel that the workload was shared equitably between us.
### Planning (?)

### Determinism
Initially, we used HashSets to store the list of cars for each RushHour board. This worked well, and was performant, but it meant that the order of the moves generated were not consistent from run to run, and thus debugging was much more difficult. We solved this problem by switching to an ArrayList, which has constant order.

### Solution file formatting refactor
Near the end of the project, we became aware that our previous outputted solution files, which consisted of the final, solved, position of the boards, were not as desired. This refactor meant that we now had to keep track of which steps we had taken to reach the solved state. We found that the easiest way to integrate this with our current flow was to use maps, linking a board state with an ArrayList of the steps required to reach that state. The ArrayList is created by taking the parent's steps, and adding the steps to reach the current state from the parent. To save space, once all children have their ArrayLists finalized, the parents are removed from the map.

## Rust

We also wrote a version of this project in the Rust programming language, which is included (with its own documentation) in its own folder. This side project was an excellent way to learn Rust, and much unnecessary (yet enjoyable) optimization occurred there.

## Conclusions and lessons learned
