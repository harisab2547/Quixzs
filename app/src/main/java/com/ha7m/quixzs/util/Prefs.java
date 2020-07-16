package com.ha7m.quixzs.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {

    private SharedPreferences sharedPreferences;

    public Prefs(Activity activity) {
        this.sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE);

    }
    public void saveHighestScore(int score){
        int currentScore = score;
        int lastScore = sharedPreferences.getInt("highest_score",0);
        if (currentScore > lastScore){
            sharedPreferences.edit().putInt("highest_score",currentScore).apply();
        }
    }
    public int getHighestScore(){
        return sharedPreferences.getInt("highest_score",0);
    }
    public void setState(int index){
        sharedPreferences.edit().putInt("save_state",index).apply();

    }
    public int getState(){
        return sharedPreferences.getInt("save_state",0);
    }


}
