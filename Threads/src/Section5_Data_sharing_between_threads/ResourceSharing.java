package Section5_Data_sharing_between_threads;

/***
 * Resource sharing is very important, and could a lot of great problem
 * some examples are:
 * 1. Multiple threads in ms-Word, one is the UI and the other is auto-saving the changes.
 * They both access the same share resource of text value.
 *
 * 2. Database, MVCC
 *
 * But then there are clear problems as well. An example is shown below,
 * Here we have a warehouse that maintains the inventory of items.
 */

public class ResourceSharing {

    public static void main(String [] args) throws InterruptedException {
        InventoryCounter inventoryCounter = new InventoryCounter();
        IncrementThread incrementThread = new IncrementThread(inventoryCounter);
        DecrementThread decrementThread = new DecrementThread(inventoryCounter);

        /***
         * When we are stating both the applications at the same time,
         * We will run into problem more than likely.
         * This is because we don't have the control over the processing of the threads.
         * Or in technical words, the operations are not "Atomic"
         */
        incrementThread.start();
        decrementThread.start();

        incrementThread.join();
        decrementThread.join();

        System.out.println("We currently have " + inventoryCounter.getItems() + " items");
    }



    public static class IncrementThread extends Thread{
        private InventoryCounter inventoryCounter;

        public IncrementThread(InventoryCounter inventoryCounter){
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
        private InventoryCounter inventoryCounter;

        public DecrementThread(InventoryCounter inventoryCounter){
            this.inventoryCounter = inventoryCounter;
        }

        @Override
        public void run(){
            for (int i = 0; i < 10000; i++) {
                inventoryCounter.decrement();
            }
        }
    }

    private static class InventoryCounter{
        private int items = 0;

        public void increment(){
            this.items++;
        }
        public void decrement(){
            this.items--;
        }
        public int getItems(){
            return this.items;
        }
    }
}
