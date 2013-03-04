
import edu.wpi.first.wpijavacv.WPIBinaryImage;
import edu.wpi.first.wpijavacv.WPIColor;
import edu.wpi.first.wpijavacv.WPIColorImage;
import edu.wpi.first.wpijavacv.WPIPoint;

public final class VisionUtils {

    public static WPIColorImage applyBGFilter(WPIColorImage img, int sqrsInX, int sqrsInY, double devFactor) {
        int colorMagSum;
        int colorMagAvg;
        int devMagSum;
        int dev;
        int squareWidth = img.getWidth() / sqrsInX;
        int squareHeight = img.getHeight() / sqrsInY;
        WPIBinaryImage toReturn;
        int[][] pixelMag = new int[squareWidth][squareHeight];

        for (int iterX = 0; iterX < sqrsInX; iterX++) {
            for (int iterY = 0; iterY < sqrsInY; iterY++) {
                colorMagSum = 0;
                //Find BK average
                for (int x = squareWidth * iterX;
                        x < squareWidth * (iterX + 1); x++) {
                    for (int y = squareHeight * iterY;
                            y < squareHeight * (iterY + 1); y++) {
                        pixelMag[x - squareWidth * iterX][y - squareHeight * iterY] = getMag(img, x, y);
                        colorMagSum += pixelMag[x - squareWidth * iterX][y - squareHeight * iterY];
                    }
                }
                colorMagAvg =
                        colorMagSum / ((img.getWidth() / sqrsInX) * (img.getWidth() / sqrsInY));
                System.out.println("Avg = " + colorMagAvg + " at (" + iterX + ", " + iterY + ")");

                devMagSum = 0;
                for (int x = squareWidth * iterX;
                        x < squareWidth * (iterX + 1); x++) {
                    for (int y = squareHeight * iterY;
                            y < squareHeight * (iterY + 1); y++) {
                        devMagSum += Math.abs(pixelMag[x - squareWidth * iterX][y - squareHeight * iterY] - colorMagAvg);
                    }
                }
                dev = devMagSum / (squareWidth * squareHeight);

                for (int x = squareWidth * iterX;
                        x < squareWidth * (iterX + 1); x++) {
                    for (int y = squareHeight * iterY;
                            y < squareHeight * (iterY + 1); y++) {
                        if (pixelMag[x - squareWidth * iterX][y - squareHeight * iterY] > colorMagAvg) {
                            img.drawPoint(new WPIPoint(x, y), WPIColor.WHITE, 0);
                        }
                    }
                }
            }
        }
        return img;

    }

    public static int getMag(WPIColorImage img, int x, int y) {
        int rgb = img.getBufferedImage().getRGB(x, y);
        return -1 * (rgb >> 16);
        /*int RGB = img.getBufferedImage().getRGB(x, y);
         int R = RGB >> 16;
         int G = (RGB >> 8) % (256);
         int B = (RGB) % 256;
         return (int) (Math.sqrt((B * B + R * R + G * G) / 3));*/
    }
}
