import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class DisjointSet
{
   int[] _disjointSetArray;
   final static int ROOT_VALUE = -1;


   /**
    * Creates a new Disjoint Set of length size. Every value is set
    * as a root position within the set.
    * @param size The size to make the Disjoint set
    */
   public DisjointSet(int size)
   {
      _disjointSetArray = new int[size];
      Arrays.fill(_disjointSetArray, ROOT_VALUE);
   }// DisjointSet


   /**
    * Unions p and q into one set in such a way that no subtree will grow 
    * in height
    * @param p a set
    * @param a another set
    */
   public void union(int p, int a)
   {
      int rootP = find(p);
      int rootA = find(a);

      // if the root of a is deeper than the root of P
      if (_disjointSetArray[rootA] < _disjointSetArray[rootP])
      {
         // attach the root of p to the root of a
         _disjointSetArray[rootP] = rootA;
      }
      else // otherwise, if the root of p is deeper or equal to the root of a
      {
         // if the height of the trees would be equal, we are forced to 
         // add to the size of the tree.
         if (_disjointSetArray[rootP] == _disjointSetArray[rootA])
         {
            // increase the height of the tree.
            // since negative values indicate a root
            // subtract from the value to add 1 to the depth 
            _disjointSetArray[rootP]--;
         }

         // attach the root of a to the root of p
         _disjointSetArray[rootA] = rootP;
      }
   }// union


   /**
    * Finds the root of p in the disjoint set, compressing the path as 
    * we go through use of recursion
    * @param p a point in the disjoint set to find the root of
    * @return the root value of p
    */
   public int find(int p)
   {
      // any value less than or equal to the root value is a root
      // if this is the case, we have the root
      if (_disjointSetArray[p] <= ROOT_VALUE)
      {
         return p;
      }
      else
      {
         // compress the path by setting as we return
         return _disjointSetArray[p] = find(_disjointSetArray[p]);
      }
   }


   /**
    * Determines whether or not two items are contained within the same set
    * @param p one item to check
    * @param q another item to check
    * @return true if they are both contained within the same set
    */
   public boolean isConnected(int p, int q)
   {
      return find(p) == find(q);
   }


   /**
    * Runs a sample program to demonstrate the disjoint set in order to 
    * create a maze, where every cell is accessable from every other cell.
    * @param args A single int, representing the size, n by n, of the 
    * maze to generate.
    */
   public static void main(String[] args)
   {  
      // for this case, we will assume that we are being passed a valid
      // integer parameter
      int size = Integer.parseInt(args[0]);
      int count = size * size;
      int wallIndex;
      boolean currentIsVertical = true;

      // create a DisjointSet for a square of length: size
      DisjointSet mazeSet = new DisjointSet(count);

      // create a list of walls for the maze. Note that this list
      // will NOT contain the maze border walls, i.e. the outside
      ArrayList<Wall> mazeWalls = new ArrayList<Wall>();

      // Create a set to store a copy of the walls. Note we are only storing
      // references to the walls
      HashMap<Integer,  Wall> unseenWalls = new HashMap<Integer, Wall>();
      ArrayList<Wall> unseen2;

      // fill the list with walls alternating between vertical and horizontal
      // depending on the index
      for (int i = 0; i < count - 1; i++)
      {
         // if we have reached the end of one row, begin adding horizontal
         // walls
         if ((i + 1) % size == 0)
         {
            for (int j = (i - (size - 1)); j <= i; j++)
            {
               // add a horizontal wall for each cell
               mazeWalls.add(new Wall(j, j + size, false));
            }
         }
         else
         {
            // add a vertical wall for the current index and the cell 
            // following it
            mazeWalls.add(new Wall(i, i + 1, true));
         }
      }

      // add references from mazeWalls after it is populated
      for (int i = 0; i < mazeWalls.size(); i++)
      {
         unseenWalls.put(new Integer(i), mazeWalls.get(i));
      }

      unseen2 = new ArrayList<Wall>(mazeWalls);

      // while the disjoint set is not fully connected 
      while (count > 1)
      {
         // get a random wall from those we haven't seen yet
         wallIndex = (int)(Math.random() * unseen2.size());

         // check if this wall separates different sets within the Disjoint set
         if (!mazeSet.isConnected(unseen2.get(wallIndex).getCellOne(), 
               unseen2.get(wallIndex).getCellTwo()))
         {
            // update the wall to be empty in the set of all walls
            unseen2.get(wallIndex).setEmpty();

            // union the sets adjacent to the walls
            mazeSet.union(unseen2.get(wallIndex).getCellOne(), 
                  unseen2.get(wallIndex).getCellTwo());
            count--;
         }

         // remove the wall from the list of unseen walls
         unseen2.remove(wallIndex);

      }


      // print the maze result
      // print top border
      // top left will be empty
      System.out.print("+  ");
      for (int i = 0; i < size - 1; i++)
      {
         System.out.print("+--");
      }
      System.out.println("+");
      System.out.print("|");

      // print the body of the maze
      for (int i = 0; i < mazeWalls.size(); i++)
      {
         // if we have reached the end of a line, print a new 
         // line
         if (currentIsVertical != mazeWalls.get(i).isVertical())
         {
            if (currentIsVertical)
            {
               System.out.print("  |");
               System.out.println();
               System.out.print("+");
            }
            else
            {
               System.out.print("+");
               System.out.println();
               System.out.print("|");
            }
            currentIsVertical = mazeWalls.get(i).isVertical();
         }

         // format the printing with various separators
         // use spaces to separate vertical walls, and plus signs to 
         // separate horizontal walls
         if (mazeWalls.get(i).isVertical())
         {
            System.out.print("  ");
            System.out.print(mazeWalls.get(i).getRepresentation());
         }
         else
         {
            System.out.print(mazeWalls.get(i).getRepresentation());

            // print a horizontal Divider if the index isn't the last 
            // print before a new line
            if (mazeWalls.get(i).isVertical() == 
                  mazeWalls.get(i + 1).isVertical())
            {
               System.out.print("+");
            }
         }

      }

      // print the bottom level of the maze
      System.out.println("  |");
      for (int i = 0; i < size - 1; i++)
      {
         System.out.print("+--");
      }
      System.out.print("+");
   }
}// main

//Create an inner class to represent a wall which can be used to
// target adjacent cells to union using the Disjoint set
class Wall
{
   private int _adjacentCellOne;
   private int _adjacentCellTwo;
   private boolean _isVertical;
   private boolean _isEmpty;

   /**
    * Creates an object representing a wall between Two adjacent cells
    * @param cellOne First cell next to the wall
    * @param cellTwo the second cell adjacent to the wall
    * @param isVertical true if the wall is vertical, else Vertical
    */
   public Wall(int cellOne, int cellTwo, boolean isVertical)
   {
      _adjacentCellOne = cellOne;
      _adjacentCellTwo = cellTwo;
      _isVertical = isVertical;
      _isEmpty = false;
   }


   /**
    * Gets the cell designated as the first cell adjacent to this wall
    * @return First adjacent cell
    */
   public int getCellOne()
   {
      return _adjacentCellOne;
   }


   /**
    * Gets the cell designated as the second cell adjacent to this wall
    * @return Second adjacent cell
    */
   public int getCellTwo()
   {
      return _adjacentCellTwo;
   }


   /**
    * Determines orientation of this wall
    * @return True if the wall is vertical
    */
   public boolean isVertical()
   {
      return _isVertical;
   }


   /**
    * Gets a String representation of the wall based on its status
    * as vertical or horizontal, and whether or not it is erased
    * @return A string representation of the wall
    */
   public String getRepresentation()
   {
      final String VERTICAL_WALL = "|";
      final String HORIZONTAL_WALL = "--";
      final String EMPTY_VERTICAL = " ";
      final String EMPTY_HORIZONTAL = "  ";

      String representation;

      // if the wall is not erased, return a wall based on its type,
      // vertical or horizontal. Else, return empty space based on its
      // character length
      if (!_isEmpty)
      {
         if (_isVertical)
         {
            representation = VERTICAL_WALL;
         }
         else
         {
            representation = HORIZONTAL_WALL;
         }
      }
      else
      {
         if (_isVertical)
         {
            representation = EMPTY_VERTICAL;
         }
         else
         {
            representation = EMPTY_HORIZONTAL;
         }
      }

      return representation;
   }


   /**
    * Sets this wall to be erased 
    */
   public void setEmpty()
   {
      _isEmpty = true;
   }
}// Wall
