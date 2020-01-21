/**********************************************************************
 *
 * The class ArrayStack implements the Stack ADT using an array to hold the
 * stack items.
 *
 * It uses the circular implementation so that the time complexities of both
 * push and pop are O(1)
 *
 * @author Litha Thampan
 *************************************************************************/

class ArrayStack<T> implements StackInterface<T>
{
  private T[] stackItems;
  private int topIndex, stackSize;
  
  public ArrayStack(int capacity)
  {
    @SuppressWarnings("unchecked")
    T[] temp = (T[]) new Object[capacity];
    stackItems = temp;
    
    topIndex = -1;
    stackSize = 0;
  }
  
  // Adds item to back
  public void push(T item)
  {
    topIndex++;
    stackItems[topIndex] = item;
    stackSize++;
  }
  
  // Removes item from front
  public T pop()
  {
    T returnValue = stackItems[topIndex];
    topIndex--;
    stackSize--;
    return returnValue;
  }
  
  // Returns item at front
  public T peek()
  {
    return stackItems[topIndex];
  }
  
  // Returns true iff the stack is empty
  public boolean isEmpty()
  {
    return stackSize == 0;
  }
  
  // Removes all items from stack
  public void clear()
  {
    for (int i = 0; i < stackItems.length; i++)
      stackItems[i] = null;
    topIndex = -1;
    stackSize = 0;
  }
}
  
    