DisjointSet takes an int command line argument and makes an x by x maze where each cell is accessable from any other cell. 
This acts as a demonstration for the Disjoint set's union / find

SudokuSolver does not take command line arguments. It instead queries the user for input through System.in
The first input asks for the alphanumeric characters to be used in the sudoku puzzle. 
The next lines of input will ask about the positions of these characters with the format row,column,character
Sudoku solver is NOT designed to trap malformed input. It is to demonstrate a backtracking algorithm.
Send an EOF (end of file) to System.in once finished entering the sudoku puzzle
EOF: Ctrl + Z On Windows, Ctrl + D on Mac
