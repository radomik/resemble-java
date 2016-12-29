package radomik.com.github.resemble.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public abstract class ImageUtils {

    public static final int IMAGE_TYPE = BufferedImage.TYPE_INT_ARGB;

    /**
     * Check image type.
     *
     * @param img
     * @param name
     * @throws IllegalArgumentException when image type is invalid
     */
    public static void checkImageType(BufferedImage img, String name) throws IllegalArgumentException {
        if (img.getType() != IMAGE_TYPE) {
            throw new IllegalArgumentException(String.format("Invalid %s.type=%s, expected %s",
                    name, img.getType(), IMAGE_TYPE));
        }
    }

    /**
     * Check image dimensions are matching.
     *
     * @param img1
     * @param imgName1
     * @param img2
     * @param imgName2
     * @throws IllegalArgumentException when image dimensions are not matching
     */
    public static void checkDimensionsMatch(BufferedImage img1, String imgName1, BufferedImage img2, String imgName2)
            throws IllegalArgumentException {
        if ((img1.getWidth() != img2.getWidth()) || (img1.getHeight() != img2.getHeight())) {
            throw new IllegalArgumentException(String.format("Size mismatch (%s.{w,h}={%d,%d} and %s.{w,h}={%d,%d})",
                    imgName1, img1.getWidth(), img1.getHeight(), imgName2, img2.getWidth(), img2.getHeight()));
        }
    }

    /**
     * Create new empty buffered image of the same size and type as <code>img</code>.
     *
     * @param img
     * @return new empty buffered image
     */
    public static BufferedImage createEmptyImage(BufferedImage img) {
        return new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
    }

    /**
     * Read buffered image from file and convert it to desired format if needed.
     *
     * @param file image file
     * @return buffered image read from file
     * @throws java.io.IOException on error reading image file
     */
    public static BufferedImage readImage(File file) throws IOException {
        BufferedImage bufImg;
        try {
            bufImg = ImageIO.read(file);
        } catch (IOException ex) {
            throw new IOException("Could not read image file '" + file + "'", ex);
        }
        BufferedImage convertedImg = new BufferedImage(bufImg.getWidth(), bufImg.getHeight(), IMAGE_TYPE);
        convertedImg.getGraphics().drawImage(bufImg, 0, 0, null);
        return convertedImg;
    }
}
