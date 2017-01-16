package com.tomwen.tools;

/**
 * from https://github.com/OpenHFT/Java-Lang/blob/master/lang/src/main/java/net/openhft/lang/Maths.java
 * @author peter.lawrey
 */
public class Maths {
    /**
     * Numbers larger than this are whole numbers due to representation error.
     */
    private static final double WHOLE_NUMBER = 1L << 53;
    private static final long[] TENS = new long[19];

    static {
        TENS[0] = 1;
        for (int i = 1; i < TENS.length; i++)
            TENS[i] = TENS[i - 1] * 10;
    }

    /**
     * Performs a round which is accurate to within 1 ulp. i.e. for values very close to 0.5 it might be rounded up or
     * down. This is a pragmatic choice for performance reasons as it is assumed you are not working on the edge of the
     * precision of double.
     *
     * @param d value to round
     * @return rounded value
     */
    public static double round2(double d) {
        final double factor = 1e2;
        return d > WHOLE_NUMBER || d < -WHOLE_NUMBER ? d :
                (long) (d < 0 ? d * factor - 0.5 : d * factor + 0.5) / factor;
    }

    /**
     * Performs a round which is accurate to within 1 ulp. i.e. for values very close to 0.5 it might be rounded up or
     * down. This is a pragmatic choice for performance reasons as it is assumed you are not working on the edge of the
     * precision of double.
     *
     * @param d value to round
     * @return rounded value
     */
    public static double round4(double d) {
        final double factor = 1e4;
        return d > Long.MAX_VALUE / factor || d < -Long.MAX_VALUE / factor ? d :
                (long) (d < 0 ? d * factor - 0.5 : d * factor + 0.5) / factor;
    }

    /**
     * Performs a round which is accurate to within 1 ulp. i.e. for values very close to 0.5 it might be rounded up or
     * down. This is a pragmatic choice for performance reasons as it is assumed you are not working on the edge of the
     * precision of double.
     *
     * @param d value to round
     * @return rounded value
     */
    public static double round6(double d) {
        final double factor = 1e6;
        return d > Long.MAX_VALUE / factor || d < -Long.MAX_VALUE / factor ? d :
                (long) (d < 0 ? d * factor - 0.5 : d * factor + 0.5) / factor;
    }

    /**
     * Performs a round which is accurate to within 1 ulp. i.e. for values very close to 0.5 it might be rounded up or
     * down. This is a pragmatic choice for performance reasons as it is assumed you are not working on the edge of the
     * precision of double.
     *
     * @param d value to round
     * @return rounded value
     */
    public static double round8(double d) {
        final double factor = 1e8;
        return d > Long.MAX_VALUE / factor || d < -Long.MAX_VALUE / factor ? d :
                (long) (d < 0 ? d * factor - 0.5 : d * factor + 0.5) / factor;
    }

    public static long power10(int n) {
        if (n < 0 || n >= TENS.length) return -1;
        return TENS[n];
    }

    public static int nextPower2(int n, int min) {
        if (!isPowerOf2(min))
            throw new IllegalArgumentException();
        if (n < min) return min;
        if (isPowerOf2(n))
            return n;
        int i = min;
        while (i < n) {
            i *= 2;
            if (i <= 0) return 1 << 30;
        }
        return i;
    }

    public static long nextPower2(long n, long min) {
        if (!isPowerOf2(min))
            throw new IllegalArgumentException();
        if (n < min) return min;
        if (isPowerOf2(n))
            return n;
        long i = min;
        while (i < n) {
            i *= 2;
            if (i <= 0) return 1L << 62;
        }
        return i;
    }

    public static boolean isPowerOf2(int n) {
        return (n & (n - 1)) == 0;
    }

    public static boolean isPowerOf2(long n) {
        return (n & (n - 1L)) == 0L;
    }

    public static int hash(int n) {
        n ^= (n >>> 21) - (n >>> 11);
        n ^= (n >>> 7) + (n >>> 4);
        return n;
    }

    public static long hash(long n) {
        n ^= (n >>> 41) - (n >>> 21);
        n ^= (n >>> 15) + (n >>> 7);
        return n;
    }

    public static long hash(CharSequence cs) {
        long hash = 0;
        for (int i = 0; i < cs.length(); i++)
            hash = hash * 131 + cs.charAt(i);
        return hash;
    }

    /**
     * Compares two {@code long} values numerically. The value returned is identical to what would be returned by:
     * <pre>
     *    Long.valueOf(x).compareTo(Long.valueOf(y))
     * </pre>
     *
     * @param x the first {@code long} to compare
     * @param y the second {@code long} to compare
     * @return the value {@code 0} if {@code x == y}; a value less than {@code 0} if {@code x < y}; and a value greater
     * than {@code 0} if {@code x > y}
     */
    public static int compare(long x, long y) {
        return (x < y) ? -1 : ((x == y) ? 0 : 1);
    }

    public static int intLog2(long num) {
        long l = Double.doubleToRawLongBits(num);
        return (int) ((l >> 52) - 1023);
    }

    public static int toInt(long l, String error) {
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE)
            throw new IllegalStateException(String.format(error, l));
        return (int) l;
    }

    public static long agitate(long l) {
        l ^= l >> 23;
        l += Long.rotateRight(l, 18);
        return l;
    }
}
