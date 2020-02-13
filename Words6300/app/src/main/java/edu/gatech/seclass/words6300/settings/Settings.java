package edu.gatech.seclass.words6300.settings;

import java.util.List;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;

public class Settings extends RealmObject {
    private boolean mainSettings = false;
    private int maxTurns;
    private static final int[][] DEFAULT_SETTING = { {9, 1}, {2, 3}, {2, 3}, {4, 2}, {12, 1}, {2, 4}, {3, 2}, {2, 4}, {9, 1}, {1, 8}, {1, 5}, {4, 1}, {2, 3}, {6, 1}, {8, 1}, {2, 3}, {1, 10},  {6, 1}, {4, 1}, {6, 1}, {4, 1}, {2, 4}, {2, 4}, {1, 8}, {2, 4}, {1, 10} };
    private static final int DEFAULT_MAXIMUM_TURNS = 8;
    private RealmList<LetterSetting> letterSettings = new RealmList<>();

    public Settings() {}

    public void initSettings(){
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();

        maxTurns = DEFAULT_MAXIMUM_TURNS;
        if (letterSettings.isEmpty()) {
            LetterSetting ls;
            for (int i = 0; i < DEFAULT_SETTING.length; i++) {
                ls = realm.createObject(LetterSetting.class);
                ls.setLetter(String.valueOf((char)('A' + i)));
                ls.setDistribution(DEFAULT_SETTING[i][0]);
                ls.setValue(DEFAULT_SETTING[i][1]);
                letterSettings.add(ls);
            }
        }

        realm.commitTransaction();
    }

    public void adjustMaxTurns(int maxTurns) {
        this.maxTurns = maxTurns;
    }

    public void adjustLetterSettings(List<LetterSetting> newSettings) {

        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();
        letterSettings.clear();
        letterSettings.addAll(newSettings);
        realm.commitTransaction();
    }

    public int getMaxTurns() {
      return maxTurns;
    }

    public List<LetterSetting> getLetterSettings() {
        return letterSettings;
    }

    void setMainSettings() {
        mainSettings = true;
    }
}
