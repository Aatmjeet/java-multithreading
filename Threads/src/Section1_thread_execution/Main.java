package Section1_thread_execution;

import Section1_thread_execution.threadExtension;

public class Main {
    public static void main(String[] args) {

//        Method 1: To create a new thread constructor and then using start method
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                // Code that will run in  a new thread
                System.out.println("we are now in thread "+Thread.currentThread().getName());
                System.out.println("Current thread priority is " + Thread.currentThread().getPriority());
            }
        });

        thread.setName("New Worker Thread");

        thread.setPriority(Thread.MAX_PRIORITY);

        System.out.println("We are in thread: " + Thread.currentThread().getName()+ " before starting a new thread");
        thread.start();
        System.out.println("We are in thread: " + Thread.currentThread().getName()+ " after starting a new thread");

//        Method 2: Extend the 'Thread' class and then define the runnable part in the new class's run function i.e.
//        Thread thread = new threadExtension();

        thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();

//        Method 3:
        Thread thread2 = new Thread(new Task2());
        thread2.start();
    }

    public static class Task2 implements Runnable {
        @Override
        public void run(){
            System.out.println("Hello from new thread");
        }
    }
}