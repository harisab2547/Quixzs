package com.ha7m.quixzs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.AudienceNetworkAds;
import com.ha7m.quixzs.Model.Question;
import com.ha7m.quixzs.data.AnswerListAsyncResponse;
import com.ha7m.quixzs.data.QuestionBank;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.ha7m.quixzs.util.Prefs;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button trueButton, falseButton;
    private ImageButton next_button, previous_Button;
    private TextView questionTextView, counter_TextView, user_Score,highestScore;
    private int currentQuestionIndex = 0;
    List<Question> questionList;
    private AdView mAdView;
    Score score = new Score();
    private int count = 0;

    Prefs prefs ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = new Prefs(MainActivity.this);
        Log.d("Second","onclick"+ prefs.getHighestScore());

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        highestScore= findViewById(R.id.highest_Score);
        trueButton = findViewById(R.id.true_button);
        falseButton = findViewById(R.id.false_Button);
        next_button = findViewById(R.id.next_Button);
        previous_Button = findViewById(R.id.previous_Button);
        questionTextView = findViewById(R.id.question_TextView);
        counter_TextView = findViewById(R.id.score);
        user_Score = findViewById(R.id.user_Score);

        falseButton.setOnClickListener(this);
        trueButton.setOnClickListener(this);
        next_button.setOnClickListener(this);
        previous_Button.setOnClickListener(this);

        currentQuestionIndex = prefs.getState();

        highestScore.setText(MessageFormat.format("HS {0}", String.valueOf(prefs.getHighestScore())));

        questionList = new QuestionBank().getQuestion(new AnswerListAsyncResponse() {
            @Override
            public void processFinished(ArrayList<Question> questionArrayList) {

                questionTextView.setText(questionList.get(currentQuestionIndex).getAnswer());
                counter_TextView.setText(MessageFormat.format("{0} / {1}", currentQuestionIndex, questionList.size()));


                Log.i("My Json", "Response" + questionArrayList);

            }
        });
       AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-8717283154647554/5168280283");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.true_button:
                checkAnswer(true);
                updateQuestion();
                break;
            case R.id.false_Button:
                checkAnswer(false);
                updateQuestion();
                break;
            case R.id.next_Button:
                currentQuestionIndex = (currentQuestionIndex + 1) % questionList.size();
                updateQuestion();
                break;
            case R.id.previous_Button:
                if (currentQuestionIndex > 0) {
                    currentQuestionIndex = (currentQuestionIndex - 1) % questionList.size();
                    previousQuestion();
                }
                break;

        }

    }

    private boolean checkAnswer(boolean userChoice) {

        boolean answer = questionList.get(currentQuestionIndex).isAnswerTure();
        if (answer == userChoice) {
            score.setScore(count);
            addPoint();
            fadeAnimation();

            Toast.makeText(MainActivity.this, "Your Answer is Right", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            deductPoint();
            shakeAnimation();
            Toast.makeText(MainActivity.this, "Your Answer is Wrong", Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    private void addPoint(){
        count = count +1;
        score.setScore(count);
        user_Score.setText(MessageFormat.format("Score : {0}", score.getScore()));

    }
    private void deductPoint(){
        if (count>0){
            count = count-1;
            score.setScore(count);
            user_Score.setText(MessageFormat.format("Score : {0}", score.getScore()));
        }
    }
    private void updateQuestion() {
        String question = questionList.get(currentQuestionIndex).getAnswer();
        questionTextView.setText(question);
        counter_TextView.setText(MessageFormat.format("{0} / {1}", currentQuestionIndex, questionList.size()));


    }

    private void previousQuestion() {
        String question = questionList.get(currentQuestionIndex).getAnswer();
        questionTextView.setText(question);
        counter_TextView.setText(MessageFormat.format("{0} / {1}", currentQuestionIndex, questionList.size()));

    }

    private void shakeAnimation() {
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake_animation);
        final CardView cardView = findViewById(R.id.cardView);
        cardView.setAnimation(shake);
        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                cardView.setCardBackgroundColor(Color.WHITE);
                currentQuestionIndex = (currentQuestionIndex + 1) % questionList.size();
                updateQuestion();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

                cardView.setCardBackgroundColor(Color.BLACK);
            }
        });
    }

    private void fadeAnimation() {
        final CardView cardView = findViewById(R.id.cardView);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);
        alphaAnimation.setDuration(780);

        cardView.setAnimation(alphaAnimation);
        cardView.setLayoutAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                cardView.setCardBackgroundColor(Color.WHITE);
                currentQuestionIndex = (currentQuestionIndex + 1) % questionList.size();
                updateQuestion();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    protected void onPause() {
        prefs.saveHighestScore(score.getScore());
        prefs.setState(currentQuestionIndex);

        super.onPause();
    }
}
