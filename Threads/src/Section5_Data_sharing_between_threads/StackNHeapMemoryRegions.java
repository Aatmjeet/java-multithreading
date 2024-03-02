package Section5_Data_sharing_between_threads;


/***
 * Stack: is the memory where functions are loaded and members are declared
 * While Heap: is the shared memory for all the threads inside a process.
 *
 *
 * @imp:
 * references =! objects
 *
 * Objects, class members (along with their class object), static variables
 * are stored in Heap(shared)
 * While
 * Local primitive types, local references
 * are allocated on a stack.
 */


public class StackNHeapMemoryRegions {

    public static void main(String[] args){
        // This is an example that contains all the information over stack vs heap and quiz notes
        Example example = new Example();
    }
}

