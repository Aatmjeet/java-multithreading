package Section5_Data_sharing_between_threads;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * Quiz Notes:
 *
 * 1) Regarding line 2:
 * `idToNameMap` is a reference allocated on the heap,
 * and therefore can be shared among threads
 *
 * 2) Regarding line 4:
 * `numberOfInstances` is a static primitive type variable, and as all static
 * variables defined on the class level, is allocated on the heap, which
 * makes it accessible to all threads.
 *
 * 3) Regarding line 12:
 * The `count` variable is a primitive type allocated on the stack, and is
 * therefore only accessible by the thread that is currently executing the
 * `getAllNames()` method.
 * If 2 threads are executing the `getAllNames()` simultaneously, they will have
 * 2 different versions of the `count` variable on the stack.
 *
 * 4) Regarding line 13:
 * The reference `allNames` is a local variable and is allocated on the stack.
 * The reference refers to an object of type ArrayList<String>, and that object
 * is allocated on the heap
 */

public class Example {

    private Map<Integer, String> idToNameMap;
    private static long numberOfInstances = 0;
    public Example() {
        this.idToNameMap = new HashMap<>();
        numberOfInstances++;
    }
    public List<String> getAllNames() {
        int count = idToNameMap.size();
        List<String> allNames = new ArrayList<>();

        allNames.addAll(idToNameMap.values());
        return allNames;
    }
}


