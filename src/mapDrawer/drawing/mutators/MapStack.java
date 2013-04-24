package mapDrawer.drawing.mutators;

import mapDrawer.AreaToDraw;

/**
 * Stack that contains the different zooms.
 * @param <Item> Takes an item for the stack.
 */
public class MapStack<Item>
{
	@SuppressWarnings("unchecked")
	private Item[] a = (Item[]) new AreaToDraw[1]; // stack items
	private int N = 0; // number of items
	
	/**
	 * Checks wether or not the array is empty.
	 * @return Boolean for the array.
	 */
	public boolean isEmpty() { return N == 0; }
	
	/**
	 * Returns the size of the array.
	 * @return The size of the array.
	 */
	public int size() { return N; }
	
	/**
	 * Resizes the array to avoid loitering.
	 * @param max The maximum size of the array.
	 */
	private void resize(int max)
	{ 
		@SuppressWarnings("unchecked")
		Item[] temp = (Item[]) new AreaToDraw[max];
		for (int i = 0; i < N; i++)
		temp[i] = a[i];
		a = temp;
	}
	
	/**
	 * Adds the item to the top of stack.
	 * @param item The item to be pushed. 
	 */
	public void push(Item item)
	{ 
		if (N == a.length) resize(2*a.length);
		a[N++] = item;
	}
	
	/**
	 * Returns the top item from the stack and deletes it.
	 * @return The top item in the stack.
	 */
	public Item pop()
	{
		if(isEmpty() != true){
		Item item = a[--N];
		a[N] = null; 
		if (N > 0 && N == a.length/4) resize(a.length/2);
		return item;
		}
		else return null;
	}
}