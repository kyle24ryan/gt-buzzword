package edu.gatech.seclass.words6300.settings;

import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class LetterSettingTest {
    private Random rand = new Random();
    private LetterSetting letterSetting;

    @Before
    public void setUp() {
        letterSetting = new LetterSetting();
    }

    @Test
    public void testSetLetter() {
        final String newLetter = Character.toString((char) (rand.nextInt(26) + 'A'));
        letterSetting.setLetter(newLetter);
        assertEquals(letterSetting.getLetter(), newLetter);
    }

    @Test
    public void testSetDistribution() {
        final int newDistribution = rand.nextInt();
        letterSetting.setDistribution(newDistribution);
        assertEquals(letterSetting.getDistribution(), newDistribution);
    }

    @Test
    public void testSetValue() {
        final int newValue = rand.nextInt();
        letterSetting.setValue(newValue);
        assertEquals(letterSetting.getValue(), newValue);
    }

}