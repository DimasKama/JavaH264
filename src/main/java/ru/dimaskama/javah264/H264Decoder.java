package ru.dimaskama.javah264;

import org.jetbrains.annotations.Nullable;
import ru.dimaskama.javah264.exception.UnknownPlatformException;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class H264Decoder implements AutoCloseable {

    private final AtomicBoolean closed = new AtomicBoolean();
    private final long pointer;

    public H264Decoder() throws IOException, UnknownPlatformException {
        this(new Builder());
    }

    public H264Decoder(Builder builder) throws IOException, UnknownPlatformException {
        OpenH264Lib.load();
        this.pointer = createDecoder0(builder.flushBehavior.ordinal());
    }

    public static Builder builder() {
        return new Builder();
    }

    @Nullable
    public DecodeResult decodeRGBA(byte[] packet) {
        assertNotClosed();
        return decodeRGBA0(pointer, Objects.requireNonNull(packet, "packet"));
    }

    @Nullable
    public DecodeResult decodeRGB(byte[] packet) {
        assertNotClosed();
        return decodeRGB0(pointer, Objects.requireNonNull(packet, "packet"));
    }

    public DecodeResult[] flushRemainingRGBA() {
        assertNotClosed();
        return flushRemainingRGBA0(pointer);
    }

    public DecodeResult[] flushRemainingRGB() {
        assertNotClosed();
        return flushRemainingRGB0(pointer);
    }

    private void assertNotClosed() {
        if (closed.get()) {
            throw new IllegalStateException("This H264Decoder instance is closed!");
        }
    }

    @Override
    public void close() {
        if (closed.compareAndSet(false, true)) {
            destroyDecoder0(pointer);
        }
    }

    // Utility method to split the bitstream to an array of NAL-units
    public static byte[][] nalUnits(byte[] h264data) throws IOException, UnknownPlatformException {
        OpenH264Lib.load();
        return nalUnits0(h264data);
    }

    private static native long createDecoder0(int flushBehavior) throws IOException;

    private static native DecodeResult decodeRGBA0(long pointer, byte[] packet);

    private static native DecodeResult decodeRGB0(long pointer, byte[] packet);

    public static native DecodeResult[] flushRemainingRGBA0(long pointer);

    public static native DecodeResult[] flushRemainingRGB0(long pointer);

    private static native void destroyDecoder0(long pointer);

    private static native byte[][] nalUnits0(byte[] bitstream);

    public static class Builder {

        private FlushBehavior flushBehavior = FlushBehavior.Auto;

        private Builder() {

        }

        public Builder flushBehavior(FlushBehavior value) {
            this.flushBehavior = value;
            return this;
        }

        public H264Decoder build() throws IOException, UnknownPlatformException {
            return new H264Decoder(this);
        }

    }

    /// How the decoder should handle flushing.
    ///
    /// The behavior of flushing is somewhat unclear upstream. If you run into decoder errors,
    /// you should probably disable automatic flushing, and manually call flushRemaining()
    /// after all NAL units have been processed. It might be a good idea to do the latter regardless.
    ///
    /// If you have more info on flushing best practices, we'd greatly appreciate a PR to make our
    /// decoding pipeline more robust.
    public enum FlushBehavior {

        /// Uses the current currently configured decoder default (which is attempted flushing after each decode).
        Auto,
        /// Flushes after each decode operation.
        Flush,
        /// Do not flush after decode operations.
        NoFlush

    }

}
