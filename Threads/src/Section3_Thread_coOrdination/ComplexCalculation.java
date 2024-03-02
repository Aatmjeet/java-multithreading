package Section3_Thread_coOrdination;

import java.math.BigInteger;

public class ComplexCalculation {
    public BigInteger calculateResult(BigInteger base1, BigInteger power1, BigInteger base2, BigInteger power2) throws InterruptedException {
        BigInteger result = BigInteger.ZERO;
        /*
            Calculate result = ( base1 ^ power1 ) + (base2 ^ power2).
            Where each calculation in (..) is calculated on a different thread
        */
        PowerCalculatingThread calcThread1 = new PowerCalculatingThread(base1, power1);

        PowerCalculatingThread calcThread2 = new PowerCalculatingThread(base2, power2);


        calcThread1.start();
        calcThread2.start();

        calcThread1.join();
        calcThread2.join();

        return result.add(calcThread1.getResult().add(calcThread2.getResult()));
    }

    private static class PowerCalculatingThread extends Thread {
        private BigInteger result = BigInteger.ONE;
        private BigInteger base;
        private BigInteger power;

        public PowerCalculatingThread(BigInteger base, BigInteger power) {
            this.base = base;
            this.power = power;
        }

        @Override
        public void run() {
           /*
           Implement the calculation of result = base ^ power
           */

            for(BigInteger iter = BigInteger.ZERO; iter.compareTo(power) != 0; iter = iter.add(BigInteger.ONE)){
                this.result = this.result.multiply(base);
            }
        }

        public BigInteger getResult() {
            return this.result;
        }
    }
}