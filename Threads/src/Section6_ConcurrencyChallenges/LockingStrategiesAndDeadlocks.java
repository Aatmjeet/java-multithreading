package Section6_ConcurrencyChallenges;

import java.util.Random;

/***
 * Deadlock:
 * Following is an example of deadlock. Here after a lock is applied on A
 * and the thread2 locks B, We are stuck in a deadlock.
 *
 * Thread 1
 * lock(A)
 * lock(B)
 * delete(A, item)
 * add(B, item)
 * unlock(B)
 * unlock(A)
 *
 * Thread 2
 * lock(B)
 * lock(A)
 * delete(B, item)
 * add (A, item)
 * unlock(A)
 * unlock(B)
 *
 */


/***
 * Conditions for a Deadlock
 * • Mutual Exclusion - Only one thread can have exclusive access to a resource
 * • Hold and Wait - At least one thread is holding a resource and is waiting for another resource
 * • Non-preemptive allocation - A resource is released only after the thread is done using it.
 * • Circular wait - A chain of at least two threads each one is holding one resource and waiting for another resource
 */

/***
 * Solution:
 * - Enforcing a strict order on lock acquisition prevents deadlocks
 *      i.e. it can be achieved but applying the locks in same order in both the roads.
 * • Easy to do with a small number of locks
 * • Maybe hard to accomplish if there are many locks in different places
 */

public class LockingStrategiesAndDeadlocks {

    public static void main(String[] args) {
        Intersection intersection = new Intersection();
        Thread trainAThread = new Thread(new TrainA(intersection));
        Thread trainBThread = new Thread(new TrainB(intersection));

        trainAThread.start();
        trainBThread.start();
    }


    public static class TrainB implements Runnable{
        private Intersection intersection;
        private Random random = new Random();

        public TrainB(Intersection intersection){
            this.intersection = intersection;
        }

        @Override
        public void run() {
            while (true){
                long sleepTime = random.nextInt(5);

                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                intersection.takeRoadB();
            }
        }
    }

    public static class TrainA implements Runnable{
        private Intersection intersection;
        private Random random = new Random();

        public TrainA(Intersection intersection){
            this.intersection = intersection;
        }

        @Override
        public void run() {
            while (true){
                long sleepTime = random.nextInt(5);

                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                intersection.takeRoadA();
            }
        }
    }

    public static class Intersection{
        private Object roadA = new Object();
        private Object roadB = new Object();

        public void takeRoadA() {
            synchronized (roadA) {
                System.out.println("Road A is locked by thread " + Thread.currentThread().getName());

                synchronized (roadB) {
                    System.out.println("Train is passing through road A");
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
        public void takeRoadB(){
            synchronized (roadB) {
                System.out.println("Road B is locked by thread " + Thread.currentThread().getName());

                synchronized (roadA) {
                    System.out.println("Train is passing through road B");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
    }
}
