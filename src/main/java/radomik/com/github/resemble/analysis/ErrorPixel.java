package radomik.com.github.resemble.analysis;

import radomik.com.github.resemble.pixel.Pixel;
import radomik.com.github.resemble.analysis.ResembleAnalysisOptions;
import static radomik.com.github.resemble.pixel.utils.ColorUtils.*;
import java.awt.image.BufferedImage;

public enum ErrorPixel {

    FLAT((BufferedImage destImg, int x, int y, Pixel d1, Pixel d2, ResembleAnalysisOptions options) -> {
        setARGB(destImg, x, y, options.getErrorPixelColor());
    }),
    MOVEMENT((BufferedImage destImg, int x, int y, Pixel d1, Pixel d2, ResembleAnalysisOptions options) -> {
        Pixel e = options.getErrorPixelColor();
        setARGB(destImg, x, y,
                d2.getAlpha().getValue(),
                ((d2.getRed().getValue() * (e.getRed().getValue() / 255)) + e.getRed().getValue()) / 2,
                ((d2.getGreen().getValue() * (e.getGreen().getValue() / 255)) + e.getGreen().getValue()) / 2,
                ((d2.getBlue().getValue() * (e.getBlue().getValue() / 255)) + e.getBlue().getValue()) / 2
        );
    }),
    FLAT_DIFFERENCE_INTENSITY((BufferedImage destImg, int x, int y, Pixel d1, Pixel d2, ResembleAnalysisOptions options) -> {
        Pixel e = options.getErrorPixelColor();
        setARGB(destImg, x, y,
                colorsDistanceRGB(d1, d2), e.getRed().getValue(), e.getGreen().getValue(), e.getBlue().getValue());
    }),
    MOVEMENT_DIFFERENCE_INTENSITY((BufferedImage destImg, int x, int y, Pixel d1, Pixel d2, ResembleAnalysisOptions options) -> {
        Pixel e = options.getErrorPixelColor();

        double ratio = colorsDistanceRGB(d1, d2) / 255.0 * 0.8;
        double ratio1 = 1.0 - ratio;
        setARGB(destImg, x, y,
                d2.getAlpha().getValue(),
                (int) (ratio1 * ((d2.getRed().getValue() * (e.getRed().getValue() / 255)) + ratio * e.getRed().getValue())),
                (int) (ratio1 * ((d2.getGreen().getValue() * (e.getGreen().getValue() / 255)) + ratio * e.getGreen().getValue())),
                (int) (ratio1 * ((d2.getBlue().getValue() * (e.getBlue().getValue() / 255)) + ratio * e.getBlue().getValue())));
    });
    private final Transformer transformer;

    private ErrorPixel(Transformer transformer) {
        this.transformer = transformer;
    }

    public Transformer getTransformer() {
        return transformer;
    }

    public static interface Transformer {

        void transform(BufferedImage destImg, int x, int y, Pixel d1, Pixel d2, ResembleAnalysisOptions options);
    }
}
