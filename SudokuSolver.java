import java.util.Scanner;

public class SudokuSolver
{
   /**
    * Uses a backtracking algorithm to solve the soduku puzzle
    * 
    * @param board
    *           the board with the values in the board
    * @param validCharacters
    *           a list of characters allowed on the board
    * @return true if we succeeded in filling the board
    */
   private static boolean solve(char[][] board, char[] validCharacters)
   {
      boolean successful = false;
      boolean emptyPositionFound = false;
      int row = -1;
      int col = -1;
      final char NULL_CHARACTER = '\0';

      // iterate over the board and find an empty location

      // iterate over columns
      for (int i = 0; i < board.length && !emptyPositionFound; i++)
      {
         // iterate over row
         for (int j = 0; j < board.length && !emptyPositionFound; j++)
         {
            if (board[i][j] == NULL_CHARACTER)
            {
               emptyPositionFound = true;
               col = i;
               row = j;
            }
         }
      }

      // if there are no empty positions, we can skip this, we successfully
      // filled the sudoku board and found a solution
      if (!emptyPositionFound)
      {
         successful = true;
      }
      else
      {
         for (int i = 0; i < validCharacters.length && !successful; i++)
         {
            // if this is a valid possibility, make a change to the board
            // then continue on. If later we discover that this is invalid
            // undo the operation
            if (isValid(board, row, col, validCharacters[i]))
            {
               board[col][row] = validCharacters[i];

               // if we succeeded we can return true, otherwise, undo the
               // change we made above and continue on
               if (solve(board, validCharacters))
               {
                  successful = true;
               }
               else
               {
                  board[col][row] = NULL_CHARACTER;
               }
            }
         }

      }

      return successful;
   }// solve
   

   /**
    * Determines if inserting the given value into the specified row is valid
    * 
    * @param board
    *           the sudoku matrix
    * @param row
    *           the row to insert into
    * @param value
    *           the value that would be added in
    * @return true if valid move
    */
   private static boolean validInRow(char[][] board, int row, char value)
   {
      boolean valid = true;

      // iterate over the values in the given row
      // if we match another value, this is not valid
      for (int i = 0; i < board.length && valid; i++)
      {
         valid = board[i][row] != value;
      }

      return valid;
   }// validInRow

   /**
    * Determines if inserting the given value into the specified column is valid
    * 
    * @param board
    *           the sudoku matrix
    * @param col
    *           the column to insert into
    * @param value
    *           the value that would be added in
    * @return true if valid move
    */
   private static boolean validInCol(char[][] board, int col, char value)
   {
      boolean valid = true;

      for (int i = 0; i < board.length && valid; i++)
      {
         valid = board[col][i] != value;
      }

      return valid;
   }// validInCol
   

   /**
    * determines if inserting the given value is valid within the inner box it
    * belongs to
    * 
    * @param board
    *           the sudoku matrix
    * @param row
    *           the row to insert into
    * @param col
    *           the column to insert into
    * @param value
    *           the value that would be added in
    * @return true if the box does not yet contain the value
    */
   private static boolean validInnerBox(char[][] board, int row, int col,
         char value)
   {
      int boxSize = (int) Math.sqrt(board.length);
      boolean valid = true;

      // get the numbers to add to the rows

      for (int i = 0; i < boxSize && valid; i++)
      {
         // gets the number to add to the columns
         for (int j = 0; j < boxSize && valid; j++)
         {
            valid = board[col + i][row + j] != value;
         }
      }

      return valid;
   }// validInnerBox
   

   /**
    * Determines if the state of the board thus far is valid. This does not
    * check for the board being completely full, rather, it checks to ensure an
    * illegal move has not been made
    * 
    * @param board
    *           a 2 dimensional array containing the character values
    *           representing the sudoku board
    * @param row
    *           the row in the sudoku matrix to add to
    * @param col
    *           the columb in the sudoku matrix to add to
    * @param value
    *           the character value to add
    * @return true if the board is in a valid state
    */
   private static boolean isValid(char[][] board, int row, int col, char value)
   {
      int innerBoxSize = (int) Math.sqrt(board.length);

      // use modulo to determine the offset of the row and column relative
      // to the inner box it belongs to
      return validInRow(board, row, value) && validInCol(board, col, value)
            && validInnerBox(board, row - (row % innerBoxSize),
                  col - (col % innerBoxSize), value);
   }// isValid

   /**
    * Prints out the result of the board in the current state this is done in a
    * formatted matrix so it is visually accurate
    * 
    * @param board
    *           the sudoku board containing the characters for each position
    */
   private static void printBoard(char[][] board)
   {
      // print a list of the columns
      System.out.println();
      System.out.print("  ");
      for (int i = 0; i < board[0].length; i++)
      {
         System.out.print(i + 1 + " ");
      }
      System.out.println();

      // iterate through the board and print it out
      for (int i = 0; i < board[0].length; i++)
      {
         // print the row number
         System.out.print(i + 1 + " ");

         // print out the row's contents aligned with the columns
         for (int j = 0; j < board[0].length; j++)
         {
            // print at least 2 characters wide
            // note this is primarily for debugging in the case
            // of a board with empty positions so as to get
            // an accurate visualization

            System.out.printf("%s ", board[j][i]);
         }
         System.out.println();
      }
   }// printBoard

   /**
    * Runs the program to solve a sodoku puzzle and prints the completed puzzle.
    * If there is no solution, an error is printed
    * We assume the user enters valid data
    * 
    * @param args No args
    */
   public static void main(String[] args)
   {
      char[][] board;
      char[] characterSet;
      int rowPosition;
      int colPosition;
      String characterString;
      Scanner inputScanner;
      
      inputScanner = new Scanner(System.in);
      
      // Get the character set from user assuming correct format.
      System.out.println("Enter the characters used in the "
            + "puzzle without any delimeter such as: 123456789");
      characterString = inputScanner.nextLine();

      // based on length of the first line from the input, construct the
      // board. It is a square requiring the length of the inputString in
      // both the vertical and horizontal lengths of our board array
      board = new char[characterString.length()][characterString.length()];

      // finish creating the characterSet now that we have the amount of
      // legal characters
      characterSet = new char[characterString.length()];

      // add legal characters to the array characterSet
      for (int i = 0; i < characterString.length(); i++)
      {
         characterSet[i] = characterString.charAt(i);
      }

      // set a delimiter for the Scanner to scan individual characters
      // through use of a regex allowing for spaces or new lines
      inputScanner.useDelimiter(",|\n");

      // begin reading the following input that would assign the starting
      // values of the puzzle.
      System.out.println("Enter positions of the puzzle's given characters");
      System.out.println("Press enter with no data when done");
      System.out.println("Format as: row,Column,character");
      System.out.println("Send EOF (End of File) when done");
      while (inputScanner.hasNext())
      {
         // get the position for the specified letter or number to go
         // within the board

         // NOTE: We subtract 1 here because we are using 1 based indexing
         rowPosition = Integer.parseInt(inputScanner.next()) - 1;
         colPosition = Integer.parseInt(inputScanner.next()) - 1;

         board[colPosition][rowPosition] = inputScanner.next().charAt(0);
      }

      inputScanner.close();


      if (solve(board, characterSet))

      {
         printBoard(board);
      }
      else
      {
         System.err.println("No found Solution");
      }

   }// main
}
