package Section4_Performace_Optimization;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageProcessing {
    private static final String SOURCE_FILE = "./resources/many-flowers.jpg";
    private static final String DESTINATION_FILE = "./resources/out-flowers.jpg";
    public static void main(String[] args) throws IOException {

        BufferedImage originalImage = ImageIO.read(new File(SOURCE_FILE));

        BufferedImage resultImage = new BufferedImage(originalImage.getWidth(),
                originalImage.getHeight(), BufferedImage.TYPE_INT_RGB);

        int numberOfThreads = 2;

        /*
         * For metric calculation, we define time components
         */
        long startTime = System.currentTimeMillis();

        // recolorSingleThreaded(originalImage, resultImage); // This is a single threaded approach
        // VS
        recolorMultiThreaded(originalImage, resultImage, numberOfThreads); // This is a single threaded approach

        long endTime = System.currentTimeMillis();

        long duration = endTime - startTime;

        System.out.println(String.valueOf(duration));

        File outputFile = new File(DESTINATION_FILE);
        ImageIO.write(resultImage, "jpg", outputFile);
    }

    public static void recolorMultiThreaded(BufferedImage originalImage, BufferedImage resultImage, int numberOfThreads){
        List<Thread> threads = new ArrayList<>();

        int width = originalImage.getWidth();
        int height = originalImage.getHeight() / numberOfThreads;

        for (int i = 0; i < numberOfThreads; i++) {
            final int threadMultiplier = i;

            Thread thread = new Thread(() -> {
                int leftCorner = 0;
                int  topCorner = height * threadMultiplier;

                recolorImage(originalImage, resultImage, leftCorner, topCorner, width, height);
            });

            threads.add(thread);
        }
        for(Thread thread: threads){
            thread.start();
        }

        for(Thread thread: threads){
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public static void recolorSingleThreaded(BufferedImage originalImage, BufferedImage resultImage){
        recolorImage(originalImage, resultImage, 0, 0, originalImage.getWidth(), originalImage.getHeight());
    }

    public static void recolorImage(BufferedImage originalImage, BufferedImage resultImage, int leftCorner,
                                    int topCorner, int width, int height){
        for(int x = leftCorner; x < leftCorner + width && x < originalImage.getWidth(); x++){
            for(int y = topCorner; y < topCorner + height && y < originalImage.getHeight(); y++){
                recolorPixes(originalImage, resultImage, x, y);
            }
        }
    }

    public static void recolorPixes(BufferedImage originalImage, BufferedImage resultImage, int x, int y){
        int rgb = originalImage.getRGB(x, y);

        int red = getRed(rgb);
        int green = getGreen(rgb);
        int blue = getBlue(rgb);

        int newRed, newGreen, newBlue;

        if(isShadeOfGray(red, green, blue)){
            newRed = Math.min(255, red + 10);
            newGreen = Math.max(0, green - 80);
            newBlue = Math.max(0, blue - 20);
        } else {
            newRed = red;
            newGreen = green;
            newBlue = blue;
        }

        int newRGB = createRGBColors(newRed, newGreen, newBlue);
        setRBG(resultImage, x, y, newRGB);
    }

    public static void setRBG (BufferedImage image, int x, int y, int rgb){
        image.getRaster().setDataElements(x, y, image.getColorModel().getDataElements(rgb, null));
    }

    public static boolean isShadeOfGray(int red, int green, int blue){
        return Math.abs(red - green) < 30 && Math.abs(red - blue) < 30 && Math.abs(green - blue) < 30;
    }

    public static int createRGBColors(int red, int green, int blue){
        int rgb = 0;


        rgb |= blue;
        rgb |= green << 8;
        rgb |= red << 16;

        rgb |= 0xFF000000;

        return rgb;
    }

    public static int getRed(int rgb){
        return (rgb & 0x00FF0000) >> 16; // this a byte map that gives back only red
    }

    public static int getGreen(int rgb){
        return (rgb & 0x0000FF00) >> 8; // this a byte map that gives back only green
    }

    public static int getBlue(int rgb){
        return rgb & 0x000000FF; // this a byte map that gives back only blue
    }
}
