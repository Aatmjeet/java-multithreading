package Section3_Thread_coOrdination;


import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @important
 * The idea behind this code
 * If 2 processes A and B are scheduled, We don't
 * know in which order there would be executed.
 * If let's say B is dependent upon A's execution,
 * then We would really be wasting the CPU time on B.
 * So in the time B is being executed, we put B to sleep
 * and wake up once A has done execution
 *
 * This can be done using join method
 */

public class ThreadJoin {

    public static void main(String[] args) throws InterruptedException {
//        List<Long> inputNumbers = Arrays.asList(1000000L, 3434L, 35435L, 2324L, 4656L, 23L, 5556L);

        List<Long> inputNumbers = Arrays.asList(30000L, 2000L, 3000L);

        List<FactorialThread> threads = new ArrayList<>();

        for(long inputNumber: inputNumbers){
            threads.add(new FactorialThread(inputNumber));
        }

        for(Thread thread: threads){
            thread.setDaemon(true); // We are setting threads are daemons to kill it once we are done executing
            thread.start();
        }

        for(Thread thread: threads){
            thread.join(2000); // we are giving the thread a max time of 2 seconds before we terminate
        }

        /*
         * In this case,
         * Till the time the main thread is done processing,
         * only factorial calculation for 0 and 23 are done.
         * This is a race condition.
         *
         * We can use a join() method to force the main thread to wait until all the threads are done processing
         */
//        for(int iter = 0; iter < inputNumbers.size(); iter++){
//            FactorialThread factorialThread = threads.get(iter);
//            if(factorialThread.isFinished()){
//                System.out.println("Factorial of " + inputNumbers.get(iter) + " is "+ factorialThread.getResult());
//            }
//            else {
//                System.out.println("The calculation for " + inputNumbers.get(iter) + " is still in progress.");
//            }
//        }
    }

    private static class FactorialThread extends Thread{
        private final long inputNumber;
        private BigInteger result = BigInteger.ZERO;
        private boolean isFinished = false;

        public FactorialThread(long inputNumber){
            this.inputNumber = inputNumber;
        }

        @Override
        public void interrupt() {
            System.out.println("Thread id is: " + Thread.currentThread().getName()+ " calc num: " + this.inputNumber +
                    " state: " + this.isFinished);
            super.interrupt();
        }

        @Override
        public void run(){
            this.result = factorial(inputNumber);
            this.isFinished = true;
        }

        private BigInteger factorial (long input){
            BigInteger tempResult = BigInteger.ONE;


            for(long iter = input; iter > 0; iter--)
                tempResult = tempResult.multiply(new BigInteger(Long.toString(iter)));

            return tempResult;
        }

        public boolean isFinished(){
            return this.isFinished;
        }

        public BigInteger getResult(){
            return  result;
        }
    }
}