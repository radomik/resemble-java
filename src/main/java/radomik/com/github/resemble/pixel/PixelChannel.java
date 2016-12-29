package radomik.com.github.resemble.pixel;

public interface PixelChannel<T extends Number> {

    public static enum Channel {
        ALPHA, RED, GREEN, BLUE, MIN_BRIGHTNESS, MAX_BRIGHTNESS, HUE;
    }

    boolean isSame(PixelChannel<T> b);

    boolean isColorSimilar(PixelChannel<T> b, Pixel tolerance);

    /**
     * Get channel value. NOTE: When value is not available then {@link PixelChannelNeededListener} is called which is
     * expected to call {@link #setValue(java.lang.Number) }. When listener did not set value or listener is not
     * available then exception is thrown.
     *
     * @return
     * @throws IllegalStateException when value is not available
     */
    T getValue() throws IllegalStateException;

    /**
     * Set channel value and mark it as available.
     *
     * @param value
     * @see PixelChannelChangeListener
     * @see PixelChannel#isAvailable()
     */
    void setValue(T value);

    Channel getChannel();

    /**
     * Set channel available.
     *
     * @param available
     */
    void setAvailable(boolean available);

    /**
     * Query if channel is available.
     *
     * @return
     */
    boolean isAvailable();

    PixelChannel setChangedListener(PixelChannelChangeListener<T> listener);

    PixelChannel setNeededListener(PixelChannelNeededListener<T> listener);
}
