package edu.gatech.seclass.words6300.utilities;

import java.util.Objects;
import java.util.Random;

public class RandomString {
    /**
     * Generate a random string with capital letters only
     * Following method provides here https://stackoverflow.com/questions/41107/how-to-generate-a-random-alpha-numeric-string
     */
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final Random rand;
    private final char[] candidate;
    private final int maxLength;

    public RandomString(int maxLength, Random rand) {
        this.maxLength = maxLength;
        if (maxLength < 1) throw new IllegalArgumentException();
        this.rand = Objects.requireNonNull(rand);
        this.candidate = UPPER.toCharArray();
    }

    public String nextString() {
        final int stringLength = rand.nextInt(this.maxLength);
        final char[] buffer = new char[stringLength];
        for (int idx = 0; idx < stringLength; idx++) {
            buffer[idx] = candidate[rand.nextInt(candidate.length)];
        }
        return new String(buffer);
    }
}
