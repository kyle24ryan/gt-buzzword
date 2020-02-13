//package edu.gatech.seclass.words6300.settings;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.Mock;
//
//import java.util.List;
//import java.util.Random;
//
//import static org.junit.Assert.*;
//
//public class SettingsTest {
//    private Settings settings;
//    private int maxTurns;
//    private static final int[][] DEFAULT_SETTING = { {9, 1}, {2, 3}, {2, 3}, {4, 2}, {12, 1}, {2, 4}, {3, 2}, {2, 4}, {9, 1}, {1, 8}, {1, 5}, {4, 1}, {2, 3}, {6, 1}, {8, 1}, {2, 3}, {1, 10},  {6, 1}, {4, 1}, {6, 1}, {4, 1}, {2, 4}, {2, 4}, {1, 8}, {2, 4}, {1, 10} };
//    private static final int DEFAULT_MAXIMUM_TURNS = 8;
//    private List<LetterSetting> letterSettings;
//    private final Random rand = new Random();
//    @Mock
//    List<LetterSetting> mockLetterSettings;
//
//    @Before
//    public void setUp() {
//        settings = new Settings();
//    }
//
//    @Test
//    public void testInitSettings() {
//        settings.initSettings();
//        assertEquals(settings.getLetterSettings().size(), DEFAULT_SETTING.length);
//        assertEquals(settings.getMaxTurns(), DEFAULT_MAXIMUM_TURNS);
//    }
//
//    @Test
//    public void testAdjustMaxTurns() {
//        final int newMaxTurns = rand.nextInt();
//        settings.adjustMaxTurns(newMaxTurns);
//        assertEquals(settings.getMaxTurns(), newMaxTurns);
//    }
//
//    @Test
//    public void testAdjustLetterSettings() {
//        settings.adjustLetterSettings(mockLetterSettings);
//        assertEquals(settings.getLetterSettings(), mockLetterSettings);
//    }
//
//}