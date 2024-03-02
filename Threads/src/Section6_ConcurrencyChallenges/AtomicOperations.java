package Section6_ConcurrencyChallenges;

import java.util.Random;

/**
 * In this lecture we would cover atomic operations and the `volatile` keyword.
 *
 * NOT all operations are atomic. But following operations are atomic:
 * - All reference assignments are atomic
 * - All primitive types are atomic, with an exception to `long` and `double`.
 *      As they are 64 bit, java doesn't guarantee atomicity
 *
 * Fortunately Java provides us with the `volatile` keyword, which guarantees
 * that the read and writes on `long` and `double` are thread safe.
 * Example: volatile double x = 1.0, volatile double y = 9.0;
 */


/***
 * Summary
 * • Atomic operations
 *      - Assignments to primitive types (excluding double and long)
 *      - Assignments to references
 *      - Assignments to double and long using volatile keyword
 * • Metrics capturing Use Case
 * • Knowledge about atomic operations is key to high
 * performance
 */

public class AtomicOperations {

    public static void main(String[] args){
        Metrics metrics = new Metrics();

        BusinessLogic businessLogic1 = new BusinessLogic(metrics);
        BusinessLogic businessLogic2 = new BusinessLogic(metrics);

        MetricsPrinter metricsPrinter = new MetricsPrinter(metrics);

        businessLogic1.start();
        businessLogic2.start();
        metricsPrinter.start();
    }

    public static class MetricsPrinter extends Thread{
        private Metrics metrics;

        public MetricsPrinter(Metrics metrics){
            this.metrics = metrics;
        }

        @Override
        public void run(){
            while (true){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                double currentAverage = metrics.getAverage();

                System.out.println("Current average is " + currentAverage);
            }
        }
    }

    public static class BusinessLogic extends Thread{
        private Metrics metrics;
        private Random random = new Random();

        public BusinessLogic(Metrics metrics){
            this.metrics = metrics;
        }

        @Override
        public void run(){
            while (true) {
                long startTime = System.currentTimeMillis();

                try {
                    Thread.sleep(random.nextInt(10));
                } catch (InterruptedException e) {
                }

                long endTime = System.currentTimeMillis();

                metrics.addSample(endTime - startTime);
            }
        }
    }

    public static class Metrics{
        /**
         * In this example, we can see that since a lot of classes would be accessing same Metrics object,
         * and the addSample could get out of sync, So we add a `synchronize` keyword to make it atomic.
         *
         * And
         *
         * Since we are returning average, which is a double in the getAverage method, we want to make sure that
         * it is also atomic. Hence I've added a volatile keyword in definition to make it atomic.
         */

        private long count = 0;
        private volatile double average = 0.0;

        public void addSample(long sample){
            double currentSum = average * count;
            count++;
            average = (currentSum + sample) / count;
        }

        public double getAverage(){
            return average;
        }
    }
}
