package Section1_thread_execution;

public class threadExtension extends Thread {
    @Override
    public void run() {
        System.out.println("we are now in thread " + this.getName());
        System.out.println("Current thread priority is " + this.getPriority());
        super.run(); // Thread execution instanciated
    }
}