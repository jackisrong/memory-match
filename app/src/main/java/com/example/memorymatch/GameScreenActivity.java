package com.example.memorymatch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.util.Collections.shuffle;

public class GameScreenActivity extends AppCompatActivity {
    public static final String WIN_MOVES_COUNT = "com.example.memorymatch.WIN_MOVES_COUNT";
    private boolean[] isImageUsed = new boolean[MainActivity.images.size()];
    private Drawable[] buttonImages = new Drawable[20];
    int numberOfCardsCurrentlyFlipped = 0;
    int flippedCardID = 0;
    int flippedCardButtonNumber = 0;
    int numberOfPairsFound = 0;
    int numberOfMoves = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);

        // Set up 2 ArrayLists for image and button indices and shuffle them
        ArrayList<Integer> imageIndices = new ArrayList<Integer>();
        ArrayList<Integer> buttonIndices = new ArrayList<Integer>();

        for (int i = 0; i < MainActivity.images.size(); i++) {
            imageIndices.add(i);
        }
        shuffle(imageIndices);

        for (int i = 0; i < 20; i++) {
            buttonIndices.add(i);
        }
        shuffle(buttonIndices);

        // Get 10 random unused images and map them to 2 random unused buttons
        for (int i = 0; i < 10; i++) {
            int imageIndex = imageIndices.remove(0);
            int buttonOneIndex = buttonIndices.remove(0);
            int buttonTwoIndex = buttonIndices.remove(0);

            buttonImages[buttonOneIndex] = MainActivity.images.get(imageIndex);
            buttonImages[buttonTwoIndex] = MainActivity.images.get(imageIndex);
        }

        // Display number of pairs and moves as 0
        ((TextView) findViewById(R.id.pairsCounter)).setText(getString(R.string.number_of_pairs_text, 0));
        ((TextView) findViewById(R.id.movesCounter)).setText(getString(R.string.number_of_moves_text, 0));
    }

    public void buttonClick(final View v) {
        final String buttonName = getResources().getResourceEntryName(v.getId());
        final int buttonNumber = Integer.parseInt(buttonName.substring(11));

        if (numberOfCardsCurrentlyFlipped >= 2 || buttonNumber == flippedCardButtonNumber) {
            // If already 2 cards flipped or same card pressed twice, do nothing
            return;
        }

        // Display image on card
        ((ImageButton) findViewById(v.getId())).setImageDrawable(buttonImages[buttonNumber - 1]);
        numberOfCardsCurrentlyFlipped++;

        if (numberOfCardsCurrentlyFlipped == 1) {
            // If this is the first card flipped, keep track of ID and button number
            flippedCardID = v.getId();
            flippedCardButtonNumber = buttonNumber;
        } else if (numberOfCardsCurrentlyFlipped == 2) {
            // If this is the 2nd card flipped, increase number of moves
            numberOfMoves++;

            if (buttonImages[buttonNumber - 1] == buttonImages[flippedCardButtonNumber - 1]) {
                // If this and first card hold same image, set background to green and not clickable, increase number of pairs found
                findViewById(flippedCardID).setBackgroundColor(Color.GREEN);
                findViewById(flippedCardID).setClickable(false);
                findViewById(v.getId()).setBackgroundColor(Color.GREEN);
                findViewById(v.getId()).setClickable(false);
                numberOfPairsFound++;
                ((TextView) findViewById(R.id.pairsCounter)).setText(getString(R.string.number_of_pairs_text, numberOfPairsFound));
            }

            // Wait half a second to display cards before continuing
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    if (buttonImages[buttonNumber - 1] != buttonImages[flippedCardButtonNumber - 1]) {
                        // If this and first card aren't same image, hide pictures
                        ((ImageButton) findViewById(flippedCardID)).setImageResource(android.R.color.transparent);
                        ((ImageButton) findViewById(v.getId())).setImageResource(android.R.color.transparent);
                    }

                    // Display new number of moves used
                    ((TextView) findViewById(R.id.movesCounter)).setText(getString(R.string.number_of_moves_text, numberOfMoves));

                    numberOfCardsCurrentlyFlipped = 0;
                    flippedCardID = 0;
                    flippedCardButtonNumber = 0;
                }
            }, 500);

            // If 10 pairs found, win game!
            if (numberOfPairsFound == 10) {
                Intent intent = new Intent(this, WinScreenActivity.class);
                intent.putExtra(WIN_MOVES_COUNT, numberOfMoves);
                startActivity(intent);
            }
        }
    }
}
