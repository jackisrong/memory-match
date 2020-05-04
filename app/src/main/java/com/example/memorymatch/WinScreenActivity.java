package com.example.memorymatch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class WinScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win_screen);

        Intent intent = getIntent();
        int numberOfMoves = intent.getIntExtra(GameScreenActivity.WIN_MOVES_COUNT, 0);
        ((TextView) findViewById(R.id.winMovesText)).setText(getString(R.string.win_moves_text, numberOfMoves));
    }

    public void playAgain(View view) {
        // Go to GameScreenActivity
        Intent intent = new Intent(this, GameScreenActivity.class);
        startActivity(intent);
    }
}
