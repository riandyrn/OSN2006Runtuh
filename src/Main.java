import java.io.BufferedInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Scanner;

public class Main
{
	
	private Scanner stdin;
	ArrayList<ArrayList<Integer>> container;
	int R = 0;
	int C = 0;
	
	public static void main(String args[])
	{
		Main main = new Main();
		main.readInput();
		main.processInput();
		main.printOutput();
	}
	
	public Main()
	{
		container = new ArrayList<>();
	}
	
	public void readInput()
	{
		stdin = new Scanner(new BufferedInputStream(System.in));
		
		R = stdin.nextInt();
		C = stdin.nextInt();
		
		for(int i = 0; i < R; i++)
		{
			container.add(convertToArrayList(stdin.next()));
		}
		
	}
	
	private ArrayList<Integer> convertToArrayList(String next) {
		
		ArrayList<Integer> ret = new ArrayList<>();
		
		
		for(int j = 0; j < C; j++)
		{
			ret.add(Character.getNumericValue(next.charAt(j)));
		}
		
		return ret;
	}

	public void processInput()
	{
		while(hasDestructibleRow())
		{
			int lastDestructibleRowIdx = destroyDestructibleRows();
			placeFlyingTilesToBottom(lastDestructibleRowIdx);
		}
		
	}
	
	private void placeFlyingTilesToBottom(int lastDestructibleRowIdx) {
		
		for(int col = 0; col < C; col++)
		{
			int flyingTiles = countFlyingTiles(col, lastDestructibleRowIdx);
			placeFlyingTilesAtBottomColumn(col, lastDestructibleRowIdx, flyingTiles);
		}
		
	}

	private void placeFlyingTilesAtBottomColumn(int col, int lastDestructibleRowIdx, int flyingTiles) {
		
		/*
		 * Fungsi ini melakukan stack tiles pada bottom
		 * dari suatu kolom. Cara kerjanya:
		 * - ketahui dulu bottom kolomnya ada dimana
		 * - stack tiles mulai dari bottom kolom itu
		 */
		
		int bottomRowIdx = getBottomIndex(col, lastDestructibleRowIdx);
		nullifyFlyingTiles(col, lastDestructibleRowIdx);
		stackTiles(bottomRowIdx, col, flyingTiles);
	}

	private void nullifyFlyingTiles(int col, int lastDestructibleRowIdx) {
		
		for(int row = 0; row < lastDestructibleRowIdx; row++)
		{
			ArrayList<Integer> tmp = container.get(row);
			tmp.set(col, 0);
			container.set(row, tmp);
		}
		
	}

	private void stackTiles(int bottomRowIdx, int col, int numFlyingTiles) {
		
		int startStackingIdx = bottomRowIdx;
		int count = 0;
		
		for(int row = startStackingIdx; count < numFlyingTiles; row--)
		{
			ArrayList<Integer> tmp = container.get(row);
			tmp.set(col, 1);
			container.set(row, tmp);
			
			count++;
		}
		
	}

	private int getBottomIndex(int col, int lastDestructibleRowIdx) {
		
		int startIdx = lastDestructibleRowIdx;
		boolean foundBottom = false;
		
		for(int row = startIdx; row < R; row++)
		{
			if(container.get(row).get(col) == 1)
			{
				startIdx = row - 1;
				foundBottom = true;
				break;
			}
		}
		
				
		if(startIdx < 0)
		{
			startIdx = 0;
		}
		
		if(foundBottom == false)
		{
			startIdx = R - 1;
		}

		return startIdx;
	}

	private int countFlyingTiles(int j, int bottom) {
		
		int count = 0;
		
		for(int i = bottom; i >= 0; i--)
		{
			if(container.get(i).get(j) == 1)
			{
				count++;
			}
		}
		
		return count;
	}

	private int destroyDestructibleRows() {
		
		int lastDestructibleRowIdx = 0;
		
		ListIterator<ArrayList<Integer>> it = container.listIterator();
		
		while(it.hasNext())
		{
			int tmpIdx = it.nextIndex();
			ArrayList<Integer> tmp = it.next();
			
			if(isCompleteRow(tmp))
			{
				destroyRow(tmpIdx);
				lastDestructibleRowIdx = tmpIdx;
			}
		}
		
		return lastDestructibleRowIdx;
	}
	
	private ArrayList<Integer> createDestroyedRow()
	{
		ArrayList<Integer> destroyedRow = new ArrayList<>();
		
		for(int i = 0; i < C; i++)
		{
			destroyedRow.add(0);
		}
		
		return destroyedRow;
	}
	
	private void destroyRow(int row) {
		
		container.set(row, createDestroyedRow());
	}

	private boolean hasDestructibleRow() {
		
		boolean foundCompleteRow = false;
		Iterator<ArrayList<Integer>> it = container.listIterator();
		
		while(it.hasNext() && !foundCompleteRow)
		{
			if(isCompleteRow(it.next()))
			{
				foundCompleteRow = true;
			}
		}
		
		return foundCompleteRow;
	}

	private boolean isCompleteRow(ArrayList<Integer> al)
	{
		boolean foundZero = false;
		Iterator<Integer> it = al.listIterator();
		
		while(it.hasNext() && !foundZero)
		{
			if(it.next() == 0)
			{
				foundZero = true;
			}
		}
		
		
		return !foundZero;
	}
	
	public void printOutput()
	{
		for(ArrayList<Integer> row: container)
		{
			printArrayList(row);
			System.out.println();
		}
	}

	private void printArrayList(ArrayList<Integer> al) {
		
		for(int i = 0; i < al.size(); i++)
		{
			System.out.print(al.get(i));
		}
		
	}
	
}