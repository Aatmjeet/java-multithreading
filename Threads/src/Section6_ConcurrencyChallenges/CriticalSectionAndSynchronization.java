package Section6_ConcurrencyChallenges;

import Section5_Data_sharing_between_threads.ResourceSharing;

/***
 * Critical section: This is a part of code that has to be executed at once,
 * without the intervention of another thread. Critical section helps in achieving atomicity
 *
 * JVM with support of OS and Hardware provides us with the tools to guard
 * the critical section from access by concurrent threads.
 *
 * Options to use that:
 * 1. The 'synchronized' keyword:
 *      There are 2 ways to use this keyword,
 *      - The first is to use the keyword in the functional definition of the method.
 *          This approach will make all the methods in that class atomic as this applies to whole object
 *      - The second approach is to use `synchronized()` and passing the lockingObject as a parameter.
 *          This way the whole object is not locked but only the critical section.
 *
 *      Note: the first method is nothing but `synchronized(this)` where, we are locking the whole object
 */

public class CriticalSectionAndSynchronization {


    public static void main(String [] args) throws InterruptedException {
        InventoryCounterWithSynchronization inventoryCounter = new InventoryCounterWithSynchronization();
        IncrementThread incrementThread = new IncrementThread(inventoryCounter);
        DecrementThread decrementThread = new DecrementThread(inventoryCounter);


        incrementThread.start();
        decrementThread.start();

        incrementThread.join();
        decrementThread.join();

        System.out.println("We currently have " + inventoryCounter.getItems() + " items");
    }



    public static class IncrementThread extends Thread{
        private InventoryCounterWithSynchronization inventoryCounter;

        public IncrementThread(InventoryCounterWithSynchronization inventoryCounter){
            this.inventoryCounter = inventoryCounter;
        }

        @Override
        public void run(){
            for (int i = 0; i < 10000; i++) {
                inventoryCounter.increment();
            }
        }
    }

    public static class DecrementThread extends Thread{
        private InventoryCounterWithSynchronization inventoryCounter;

        public DecrementThread(InventoryCounterWithSynchronization inventoryCounter){
            this.inventoryCounter = inventoryCounter;
        }

        @Override
        public void run(){
            for (int i = 0; i < 10000; i++) {
                inventoryCounter.decrement();
            }
        }
    }

    private static class InventoryCounterWithSynchronization{
        private int items = 0;

        /**
         * By adding `synchronized` keyword, we were able to make critical section atomic
         */

        // Synchronization type 1:
        /*
        public synchronized void increment(){
            this.items++;
        }
        public synchronized void decrement(){
            this.items--;
        }
        public synchronized int getItems(){
            return this.items;
        }
         */

        // Synchronization method 2:

        Object lock = new Object();
        public void increment(){
            synchronized (this.lock)
            {
                this.items++;

            }
        }
        public void decrement(){
            synchronized (this.lock){
                this.items--;
            }
        }
        public int getItems(){
            synchronized (this.lock){
                return this.items;
            }
        }
    }
}
