public class GenericStack<E> {
  private java.util.ArrayList<E> list = new java.util.ArrayList<>();

  public int getSize() {
    return list.size();
  }

  public E peek() {
    return list.get(getSize() - 1);
  }

  public void push(E o) { // puts parameter at the end of the ArrayList
    list.add(o);
  }

  public E pop() { // removes the last item in the ArrayList
    E o = list.get(getSize() - 1);
    list.remove(getSize() - 1);
    return o;
  }

  public boolean isEmpty() {
    return list.isEmpty();
  }
  
  @Override
  public String toString() {
    return "stack: " + list.toString();
  }

  public static void main(String [] args) {
    GenericStack<String> stringStack = new GenericStack<>();
    System.out.println("GenericStack of Strings were created and named stringStack.\n");
    
    String s1 = "Hello there";
    System.out.println(String.format("String s1 is:  \"%s\"", s1));
    String s2 = "Class 22C";
    System.out.println(String.format("String s2 is:  \"%s\"", s2));
    String s3 = "This is exercise 1";
    System.out.println(String.format("String s3 is:  \"%s\"", s3));
    String s4 = "Using the GenericStack Class.";
    System.out.println(String.format("String s4 is:  \"%s\"\n", s4));
    
    stringStack.push(s1);
    System.out.println(String.format("String s1 was pushed onto stringStack: \"%s\"", s1));
    stringStack.push(s2);
    System.out.println(String.format("String s2 was pushed onto stringStack: \"%s\"", s2));
    stringStack.push(s3);
    System.out.println(String.format("String s3 was pushed onto stringStack: \"%s\"", s3));
    stringStack.push(s4);
    System.out.println(String.format("String s4 was pushed onto stringStack: \"%s\"\n", s4));
    
    System.out.println(String.format("Curent contents of string%s\n\n", stringStack.toString()));

    while(!stringStack.isEmpty()) {
      System.out.println(String.format("The string \"%s\" was popped off.", stringStack.pop()));
       System.out.println(String.format("Curent contents of string%s\n", stringStack.toString()));
    }

  }
}