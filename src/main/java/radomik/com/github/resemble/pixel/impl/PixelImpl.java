package radomik.com.github.resemble.pixel.impl;

import radomik.com.github.resemble.pixel.Pixel;
import radomik.com.github.resemble.pixel.PixelChannel;
import radomik.com.github.resemble.pixel.PixelChannel.Channel;
import radomik.com.github.resemble.pixel.PixelChannelChangeListener;
import radomik.com.github.resemble.pixel.PixelChannelNeededListener;
import radomik.com.github.resemble.pixel.utils.ColorUtils;
import static radomik.com.github.resemble.pixel.utils.ColorUtils.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class PixelImpl implements Pixel {

    private final PixelChannel[] values;
    private final PixelChannelChangeListener<Integer> rgbChanged = (PixelChannel<Integer> source, Integer oldValue) -> {
        getMinBrightness().setAvailable(false);
        getMaxBrightness().setAvailable(false);
        getHue().setAvailable(false);
    };
    private final PixelChannelNeededListener<Integer> brightnessNeeded = (PixelChannel<Integer> source) -> {
        addBrightnessInfo();
    };
    private final PixelChannelNeededListener<Double> hsvNeeded = (PixelChannel<Double> source) -> {
        addHueInfo();
    };

    public PixelImpl(int a, int r, int g, int b) {
        values = new PixelChannel[Channel.values().length];
        values[Channel.ALPHA.ordinal()] = new BytePixelChannelImpl(Channel.ALPHA);
        values[Channel.RED.ordinal()] = new BytePixelChannelImpl(Channel.RED).setChangedListener(rgbChanged);
        values[Channel.GREEN.ordinal()] = new BytePixelChannelImpl(Channel.GREEN).setChangedListener(rgbChanged);
        values[Channel.BLUE.ordinal()] = new BytePixelChannelImpl(Channel.BLUE).setChangedListener(rgbChanged);
        values[Channel.MIN_BRIGHTNESS.ordinal()] = new BytePixelChannelImpl(Channel.MIN_BRIGHTNESS).setNeededListener(brightnessNeeded);
        values[Channel.MAX_BRIGHTNESS.ordinal()] = new BytePixelChannelImpl(Channel.MAX_BRIGHTNESS).setNeededListener(brightnessNeeded);
        values[Channel.HUE.ordinal()] = new HuePixelChannelImpl(Channel.HUE).setNeededListener(hsvNeeded);
        setARGB(a, r, g, b);
    }

    public PixelImpl() {
        this(0, 0, 0, 0);
    }

    private PixelImpl(BufferedImage image, int x, int y) {
        this();
        setARGB(image, x, y);
    }

    @Override
    public String toString() {
        return Arrays.toString(values);
    }

    @Override
    public boolean isPixelBrightnessSimilar(Pixel d2, Pixel tolerance) {
        if (!getAlpha().isColorSimilar(d2.getAlpha(), tolerance)) {
            return false;
        }
        return getMinBrightness().isColorSimilar(d2.getMinBrightness(), tolerance);
    }

    @Override
    public boolean isRGBSame(Pixel d2) {
        return (getRed().isSame(d2.getRed()) &&
                getGreen().isSame(d2.getGreen()) &&
                getBlue().isSame(d2.getBlue()));
    }

    @Override
    public boolean isARGBSimilar(Pixel d2, Pixel tolerance) {
        return (getRed().isColorSimilar(d2.getRed(), tolerance) &&
                getGreen().isColorSimilar(d2.getGreen(), tolerance) &&
                getBlue().isColorSimilar(d2.getBlue(), tolerance) &&
                getAlpha().isColorSimilar(d2.getAlpha(), tolerance));
    }

    @Override
    public boolean isContrasting(Pixel d2, Pixel tolerance) {
        int thisValue = getMinBrightness().getValue();
        int d2Value = d2.getMinBrightness().getValue();
        int tolValue = tolerance.getMaxBrightness().getValue();
        return Math.abs(thisValue - d2Value) > tolValue;
    }

    @Override
    public PixelChannel<Integer> getAlpha() {
        return get(Channel.ALPHA);
    }

    @Override
    public PixelChannel<Integer> getRed() {
        return get(Channel.RED);
    }

    @Override
    public PixelChannel<Integer> getGreen() {
        return get(Channel.GREEN);
    }

    @Override
    public PixelChannel<Integer> getBlue() {
        return get(Channel.BLUE);
    }

    @Override
    public PixelChannel<Integer> getMinBrightness() {
        return get(Channel.MIN_BRIGHTNESS);
    }

    @Override
    public PixelChannel<Integer> getMaxBrightness() {
        return get(Channel.MAX_BRIGHTNESS);
    }

    @Override
    public PixelChannel<Double> getHue() {
        return get(Channel.HUE);
    }

    @Override
    public PixelChannel get(Channel channel) {
        return values[channel.ordinal()];
    }

    @Override
    public void setARGB(int argb) {
        //System.out.printf("setARGB: argb=%08X\n", argb);
        setARGB(getARGB_Alpha(argb), getARGB_Red(argb), getARGB_Green(argb), getARGB_Blue(argb));
    }

    @Override
    public final void setARGB(int a, int r, int g, int b) {
        //System.out.printf("setARGB: argb={%02X,%02X,%02X,%02X}\n", a, r, g, b);
        getAlpha().setValue(a);
        getRed().setValue(r);
        getGreen().setValue(g);
        getBlue().setValue(b);
    }

    @Override
    public final void setARGB(BufferedImage image, int x, int y) {
        setARGB(getARGB(image, x, y));
    }

    private void addBrightnessInfo() {
        int brightness = getBrightness(getRed().getValue(), getGreen().getValue(), getBlue().getValue());
        getMinBrightness().setValue(brightness);
        getMaxBrightness().setValue(brightness);
    }

    private void addHueInfo() {
        double hue = ColorUtils.getHue(getRed().getValue(), getGreen().getValue(), getBlue().getValue());
        getHue().setValue(hue);
    }
}
