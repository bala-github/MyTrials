import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class Solver {

	private final boolean isSolvable;
	
	private final int moves;
	
	private final List<Board> solution;
	
	public Solver(Board initial)      {
		// find a solution to the initial board (using the A* algorithm)
		
		if(initial == null) {
			throw new NullPointerException();
		}
		
		Comparator<BoardState> boardSateComparator = new Comparator<BoardState>() {

			@Override
			public int compare(BoardState arg0, BoardState arg1) {
				
				if((arg0.blocks.manhattan() + arg0.moves) < (arg1.blocks.manhattan() + arg1.moves)) {
					return -1;
				}
				
				if((arg0.blocks.manhattan() + arg0.moves) > (arg1.blocks.manhattan() + arg1.moves)) {
					return 1;
				}

				return 0;
			}
			
		};
		
		MinPQ<BoardState> pqoriginal = new MinPQ<BoardState>(1, boardSateComparator);
		MinPQ<BoardState> pqtwin = new MinPQ<BoardState>(1, boardSateComparator);
		solution = new ArrayList<Board>();
			
		pqoriginal.insert(new BoardState(initial, null, 0));
		
		pqtwin.insert(new BoardState(initial.twin(), null, 0));
		
		BoardState original, twin;
		
		do {
			
			original = pqoriginal.delMin();
			twin = pqtwin.delMin();
			
			//System.out.println("Original:" + original.blocks);
			//System.out.println("Twin:" + twin.blocks);
			
			
			if(twin.blocks.isGoal()) {
				isSolvable = false;
				moves = -1;
				break;
				
			}
			
			if(original.blocks.isGoal()) {
				isSolvable = true;
				moves = original.moves;
				solution.add(original.blocks);
				break;
			} else {
				//get neighbours of orignal and twin and add it to priority queues.
				for(Board board : original.blocks.neighbors()) {
					
					if(!board.equals(original.previous)) { 
						pqoriginal.insert(new BoardState(board, original.blocks, original.moves + 1));
					}
				}
				
				for(Board board : twin.blocks.neighbors()) {
					
					if(!board.equals(twin.previous)) {
						pqtwin.insert(new BoardState(board, twin.blocks, original.moves + 1));
					}
				}
				solution.add(original.blocks);
			}
			
		}while(true);
		
		
	}
	
    public boolean isSolvable() {     
    	// is the initial board solvable?
    	return isSolvable;
    }	
    
    public int moves() {
    	// min number of moves to solve initial board; -1 if unsolvable
    	return moves;
    }
    
    public Iterable<Board> solution() {
    	// sequence of boards in a shortest solution; null if unsolvable
    	if(isSolvable()) {
    		return solution;
    	} else {
    		return null;
    	}
    }
    
    public static void main(String[] args)  {
    	// solve a slider puzzle (given below)
    	// create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);    	
        }
    }
    
    private class BoardState {
    	
    	private Board blocks;

		private Board previous;
    	private int moves;
    	
    	BoardState(Board blocks, Board previous, int moves) {
    		this.blocks = blocks;
    		this.previous = previous;
    		this.moves = moves;
    	}
    	
    	
    }
}
