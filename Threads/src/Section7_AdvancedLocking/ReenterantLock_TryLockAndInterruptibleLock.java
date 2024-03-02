package Section7_AdvancedLocking;


import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/***
 * In this example we are building a financial asset dashboard
 * using the Re-enterant lock
 *
 * There are going to be 2 threads, 1 UI thread and the other which makes the network calls
 *
 * For UI work, we are using JavaFX
 */
public class ReenterantLock_TryLockAndInterruptibleLock {

    public static void main(String[] args) {

    }

    public static class PricesContainer{
        private Lock lockObject = new ReentrantLock();
        private double bitcoinPrice;
        private double etherPrice;
        private double litecoinPrice;
        private double bitcoinCashPrice;
        private double ripplePrice;

        public Lock getLockObject() {
            return lockObject;
        }

        public void setLockObject(Lock lockObject) {
            this.lockObject = lockObject;
        }

        public double getBitcoinPrice() {
            return bitcoinPrice;
        }

        public void setBitcoinPrice(double bitcoinPrice) {
            this.bitcoinPrice = bitcoinPrice;
        }

        public double getEtherPrice() {
            return etherPrice;
        }

        public void setEtherPrice(double etherPrice) {
            this.etherPrice = etherPrice;
        }

        public double getLitecoinPrice() {
            return litecoinPrice;
        }

        public void setLitecoinPrice(double litecoinPrice) {
            this.litecoinPrice = litecoinPrice;
        }

        public double getBitcoinCashPrice() {
            return bitcoinCashPrice;
        }

        public void setBitcoinCashPrice(double bitcoinCashPrice) {
            this.bitcoinCashPrice = bitcoinCashPrice;
        }

        public double getRipplePrice() {
            return ripplePrice;
        }

        public void setRipplePrice(double ripplePrice) {
            this.ripplePrice = ripplePrice;
        }
    }

    public static class PricesUpdater extends Thread{
        private PricesContainer pricesContainer;
        private Random random = new Random();

        public PricesUpdater(PricesContainer pricesContainer){
            this.pricesContainer = pricesContainer;
        }

        @Override
        public void run(){
            while (true){
                pricesContainer.getLockObject().lock();

                try{
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    pricesContainer.setBitcoinPrice(random.nextDouble(2000));
                    pricesContainer.setEtherPrice(random.nextDouble(2000));
                    pricesContainer.setLitecoinPrice(random.nextDouble(500));
                    pricesContainer.setBitcoinCashPrice(random.nextDouble(5000));
                    pricesContainer.setRipplePrice(random.nextDouble());
                }
                finally {
                    pricesContainer.getLockObject().unlock();
                }

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
