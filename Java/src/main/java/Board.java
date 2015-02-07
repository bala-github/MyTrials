import java.util.ArrayList;
import java.util.List;


public class Board {

	
	private final int [] [] blocks;
	
    public Board(int[][] blocks) {
    	// construct a board from an N-by-N array of blocks
    	// (where blocks[i][j] = block in row i, column j)
		if(blocks == null) {
			throw new NullPointerException();
		}

		this.blocks = blocks;
    }
    
    public int dimension() {
    	// board dimension N
    	return blocks.length;
    	
    }
    
    public int hamming() {
    	
    	int count = 0;
    	
    	for(int i = 0; i < blocks.length; i++ ) {
    		for(int j = 0; j < blocks[i].length; j++) {
    			
    			if(blocks[i][j] == 0) {
    				continue;
    			}
    			if(blocks[i][j] != (i * blocks[i].length + j + 1) % (dimension()*dimension())) {
    				count ++;
    			}
    		}
    	}	
    	
    	return count;
    }
    
    public int manhattan() {
    	// sum of Manhattan distances between blocks and goal
    	int count = 0;
    	
    	for(int i = 0; i < blocks.length; i++ ) {
    		for(int j = 0; j < blocks[i].length; j++) {
    			
    			int digit = blocks[i][j];
    			if(digit == 0) {
    				continue;
    			}
    			int horizontalDistance = Math.abs(((digit - 1) / dimension()) - i);
    			int verticalDistance = Math.abs(((digit - 1) % dimension()) - j);
    			//System.out.println("Digit" + digit + "horizontal" + horizontalDistance + "vertical" + verticalDistance);
    			count = count + horizontalDistance + verticalDistance;
    		}
    	}
    	
    	return count;
    }
    
    public boolean isGoal() {
    	
    	return hamming() == 0;
    }
    
    private int[][] copyBlocks() {
    	int[][] blockscopy= new int[dimension()][dimension()];
    	for(int i = 0; i < blocks.length; i++ ) {
    		for(int j = 0; j < blocks[i].length; j++) {
    				
    			blockscopy[i][j] = blocks[i][j];	
    		}
    	}	
    	
    	return blockscopy;
    }
    public Board twin() {
    	// a board that is obtained by exchanging two adjacent blocks in the same row
    	int[][] twinBlocks = copyBlocks();
    	for(int i = 0; i < blocks.length; i++ ) {
    		for(int j = 0; j < blocks[i].length - 1; j++) {
    			
    			if(twinBlocks[i][j] != 0 && twinBlocks[i][j+1] != 0) {
    				int temp = twinBlocks[i][j];
    				twinBlocks[i][j] = twinBlocks[i][j+1];
    				twinBlocks[i][j+1] = temp;
    				return new Board(twinBlocks);
    			}
    		}	
    	}	
    	
    	return null;
    }
    
    public boolean equals(Object y) {
    	// does this board equal y?
    	if(y == null) {
    		return false;
    	}
    	
    	if(this.getClass() != y.getClass()) {
    		return false;
    	}
    	
    	if(this == y) {
    		return true;
    	}
    	
    	int[][] compareblocks = ((Board)y).blocks;
    	if(blocks.length != compareblocks.length) {
    		return false;
    	}
    	
    	for(int i = 0; i < blocks.length; i++) {
    		if(blocks[i].length != compareblocks[i].length) {
    			return false;
    		}
    		for(int j = 0; j < blocks[i].length; j++) {
    			
    			if(blocks[i][j] != compareblocks[i][j]) {
    				return false;
    			}
    		}
    	}
    	
    	return true;
    }
    
    public Iterable<Board> neighbors() {
    	// all neighboring boards
    	List<Board> neighbours = new ArrayList<Board>();
    	int [][] neighborBlocks = null;

    	for(int i = 0; i < blocks.length; i++ ) {
    		for(int j = 0; j < blocks[i].length; j++) {
    			
    			if(blocks[i][j] == 0) {
    				//reached empyt block.
    				if(i != 0) {
    					//if not first row, get the neighbour by using the value from previous row.
    					neighborBlocks = copyBlocks();
    					
    					neighborBlocks[i][j] = neighborBlocks[i-1][j];
    					neighborBlocks[i-1][j] = 0;
    					neighbours.add(new Board(neighborBlocks));
    				}
    				
    				if(i != dimension() -1) {
    					//if not last row, get the neighbour by using the value from next row.
    					neighborBlocks = copyBlocks();
    					neighborBlocks[i][j] = neighborBlocks[i+1][j];
    					neighborBlocks[i+1][j] = 0;
    					
    					neighbours.add(new Board(neighborBlocks));
    				}
    				
    				if(j != 0) {
    					//if not first column, get the neighbour by using the value from column to left.
    					neighborBlocks = copyBlocks();
    					neighborBlocks[i][j] = neighborBlocks[i][j-1];
    					neighborBlocks[i][j-1] = 0;
    					neighbours.add(new Board(neighborBlocks));
    				}
    				
    				if(j != dimension()  - 1) {
    					//if not last column, get the neighbour by using the value from column to right.
    					neighborBlocks = copyBlocks();
    					neighborBlocks[i][j] = neighborBlocks[i][j+1];
    					neighborBlocks[i][j+1] = 0;
    					
    					neighbours.add(new Board(neighborBlocks));
    				
    					
    				}
    				
    				return neighbours;
    			}
    		}
    	}
    	
    	return neighbours;
    }
    
    public String toString() {
    	// string representation of this board (in the output format specified below)
    	StringBuilder boardstring = new StringBuilder(dimension() + "\n");
    	for(int i = 0; i < blocks.length; i++ ) {
    		for(int j = 0; j < blocks[i].length; j++) {
    			boardstring.append(" ");
    			boardstring.append(blocks[i][j]);
    		}
    		boardstring.append("\n");
    	}
    	
    	return boardstring.toString();
    }

    public static void main(String[] args) {
    	// create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        
        Board initial = new Board(blocks);
        
        System.out.println("Board:" + initial.toString());
        System.out.println("Hamming:" + initial.hamming());
        System.out.println("Manhattan:" + initial.manhattan());
        System.out.println("Twin:" + initial.twin());
        
        System.out.println("Neighbours:");
        for(Board neighbour : initial.neighbors()) {
        	
        	System.out.println(neighbour);
        }
    }
}
