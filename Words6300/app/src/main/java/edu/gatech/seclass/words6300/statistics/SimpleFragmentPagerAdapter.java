package edu.gatech.seclass.words6300.statistics;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import edu.gatech.seclass.words6300.statistics.GameScoreStatsFragment;
import edu.gatech.seclass.words6300.statistics.LetterStatsFragment;
import edu.gatech.seclass.words6300.statistics.WordBankStatsFragment;

public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {

    SimpleFragmentPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new GameScoreStatsFragment();
        } else if (position == 1){
            return new LetterStatsFragment();
        } else {
            return new WordBankStatsFragment();
        }
    }


    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Game Score Stats";
            case 1:
                return "Letter\nStats";
            case 2:
                return "Word Bank Stats";
            default:
                return null;
        }
    }
}