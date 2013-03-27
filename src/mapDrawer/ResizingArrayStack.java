package mapDrawer;

public class ResizingArrayStack<Item>
{
	@SuppressWarnings("unchecked")
	private Item[] a = (Item[]) new AreaToDraw[1]; // stack items
	private int N = 0; // number of items
	
	public boolean isEmpty() { return N == 0; }
	
	public int size() { return N; }
	
	private void resize(int max)
	{ // Move stack to a new array of size max.
		@SuppressWarnings("unchecked")
		Item[] temp = (Item[]) new AreaToDraw[max];
		for (int i = 0; i < N; i++)
		temp[i] = a[i];
		a = temp;
	}
	
	public void push(Item item)
	{ // Add item to top of stack.
		if (N == a.length) resize(2*a.length);
		a[N++] = item;
	}
	
	public Item pop()
	{ // Remove item from top of stack.
		if(isEmpty() != true){
		Item item = a[--N];
		a[N] = null; // Avoid loitering (see text).
		if (N > 0 && N == a.length/4) resize(a.length/2);
		return item;
		}
		else return null;
	}
}