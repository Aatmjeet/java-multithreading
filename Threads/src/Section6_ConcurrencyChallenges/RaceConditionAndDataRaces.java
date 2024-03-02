package Section6_ConcurrencyChallenges;

/**
 * Race Condition
 * • Condition when multiple threads are accessing a shared resource.
 * • At least one thread is modifying the resource
 * • The timing of threads' scheduling may cause incorrect results
 * • The core of the problem is non-atomic operations performed on the shared resource
 */

/**
 * Data Race - Solutions
 * # Establish a Happens - Before semantics by one of these methods:
 * • Synchronization of methods which modify shared variables
 * • Declaration of shared variables with the volatile keyword
 */

/**
 * Rule of Thumb
 * • Every shared variable (modified by at least one thread) should be either
 *      - Guarded by a synchronized block (or any type of lock)
 *      Or
 *      - Declared volatile
 */

/**
 * Here in this example, I have solved the race condition just by adding `volatile` keyword
 * in the declaration of the variables in the sharedClass.
 * Brief: In this case, we were facing race condition issue because the variables
 * were not being fed into the CPU in-order. With adding volatile keyword, guarantees that the
 * code that comes before the read-write(sharedVar) would be executed first, only then  the read-write happens.
 * Similarly for the of the code that comes after the shared variable.
 */




public class RaceConditionAndDataRaces {

    public static void main(String[] args){
        SharedClass sharedClass = new SharedClass();
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                sharedClass.increment();
            }
        });
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                sharedClass.checkForDataRace();
            }
        });

        thread1.start();
        thread2.start();
    }

    public static class SharedClass {
        private volatile int x = 0;
        private volatile int y = 0;
        public void increment() {
            x++;
            y++;
        }
        public void checkForDataRace () {
            if (y > x) {
                System.out.println("y > x - Data Race is detected");
            }
        }
    }
}
