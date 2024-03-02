package Section3_Thread_coOrdination;

import java.math.BigInteger;

public class ThreadControl {

    public static void main(String[] args){
        // Long-running computation
        Thread longRunningThread = new Thread(new LongComputationTask(
                new BigInteger("200000000"), new BigInteger("100000")
        ));


        // To stop a long computation thread without checking after computations, we make that thread a 'Daemon'
        // Daemons: are low-priority background workers that are cleared after main thread is closed!
        // Example: Garbage collector
        longRunningThread.setDaemon(true);
        longRunningThread.start();

        // longRunningThread.interrupt(); // An interrupt can't stop a long computation thread without check!

        // Sleeping thread
        // Thread thread = new Thread(new BlockingTask());
        // thread.start();
        // thread.interrupt();
    }

    private static class BlockingTask implements Runnable {

        @Override
        public void run() {
            // do things
            try {
                Thread.sleep(500000);
            } catch (InterruptedException e) {
                System.out.println("Exiting blocking thread");
            }
        }
    }

    private static class LongComputationTask implements Runnable{
        private  BigInteger base;
        private  BigInteger power;

        public LongComputationTask(BigInteger base, BigInteger power){
            this.base = base;
            this.power = power;
        }
        @Override
        public void run() {
            System.out.println(base+"^"+power+" = "+pow(base, power));
        }

        private BigInteger pow(BigInteger base, BigInteger power){
            BigInteger result = BigInteger.ONE;

            for(BigInteger i = BigInteger.ZERO; i.compareTo(power) != 0; i = i.add(BigInteger.ONE)){
                if(Thread.currentThread().isInterrupted()){
                    System.out.println("Thread interrupted prematurely!");
                    return BigInteger.ZERO;
                }
                result = result.multiply(base);
            }

            return result;
        }
    }
}
