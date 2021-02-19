package com.dimitriadism.carombilliardcounter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NamesActivity extends AppCompatActivity {

    static boolean player1flag;
    static boolean player2flag;
    static boolean scoreFlag ;

    private EditText editTextWinningScore;
    private  EditText editTextPlayer1;
    private  EditText editTextPlayer2;
    Integer winningScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_names);

        //Validations to go to next Activity
        player1flag = false;
        player2flag = false;
        scoreFlag = false;

        editTextWinningScore = findViewById(R.id.winningScore);
        editTextPlayer1 = findViewById(R.id.player1);
        editTextPlayer2 = findViewById(R.id.player2);

        //When the Screen first opens, focus to the 1st Player Name input
        editTextPlayer1.requestFocus();

        Button letsPlay = findViewById(R.id.letsPlay);
        letsPlay.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openLetsPlay();
            }
        } );
    }

    public void openLetsPlay()
    {

        String player1 = editTextPlayer1.getText().toString().trim();

        //Check if input Player 1 is not empty
        if(!TextUtils.isEmpty(editTextPlayer1.getText()))
        {
            if(player1.length() < 3 )
            {
                Toast.makeText(this, "Πολύ μικρό όνομα για τον 1ο Παίκτη. Εισάγετε τουλάχιστον 3 γράμματα!", Toast.LENGTH_SHORT).show();
                player1flag = false;
            }
            else
            {
                player1flag = true;
            }
        }
        else //Player 1 name is empty
        {
            Toast.makeText(this, "Πρέπει να εισάγετε όνομα για τον 1ο Παίκτη!", Toast.LENGTH_SHORT).show();
            player1flag = false;
        }


        String player2 = editTextPlayer2.getText().toString().trim();

        //Check if input Player 2 is not empty
        if(!TextUtils.isEmpty(editTextPlayer2.getText()))
        {
            if(player2.length() < 3 )
            {
                Toast.makeText(this, "Πολύ μικρό όνομα για τον 2ο Παίκτη. Εισάγετε τουλάχιστον 3 γράμματα!", Toast.LENGTH_SHORT).show();
                player2flag = false;
            }
            else
            {
                player2flag = true;
            }
        }
        else //Player 2 name is empty
        {
            Toast.makeText(this, "Πρέπει να εισάγετε όνομα για τον 2ο Παίκτη!", Toast.LENGTH_SHORT).show();
            player2flag = false;
        }

        String winScore = editTextWinningScore.getText().toString().trim();

        //Check if input Winning score is not empty
        if(!TextUtils.isEmpty(editTextWinningScore.getText()))
        {
            //Parse it as Integer if the input is not empty, so I can perform arithmetic validations
            winningScore = Integer.parseInt(winScore);

            if(winningScore <= 0 )// If it's not empty, make some Validations
            {
                Toast.makeText(this, "Οι νικητήριες καραμπόλες δε γίνεται να είναι 0!", Toast.LENGTH_SHORT).show();
                scoreFlag = false;
            }
            else if (winningScore < 5) //Validation for low score
            {
                Toast.makeText(this, "Πολύ λίγες νικητήριες καραμπόλες. Εισάγετε περισσότερες από 5!", Toast.LENGTH_SHORT).show();
                scoreFlag = false;
            }
            else if (winningScore > 500)  //Validation for huge score
            {
                Toast.makeText(this, "Πολλές νικητήριες καραμπόλες. Εισάγετε λιγότερες από 500!", Toast.LENGTH_SHORT).show();
                scoreFlag = false;
            }
            else //All Validations OK for the winningScore
            {
                scoreFlag = true;
            }
        }
        else //Input winning Score is empty, throw Message
        {
            Toast.makeText(this, "Πρέπει να εισάγετε τις νικητήριες καραμπόλες!", Toast.LENGTH_SHORT).show();
            scoreFlag = false;

        }

        //ALL validations OK, go to next Activity
        if(player1flag && player2flag && scoreFlag)
        {
            Intent intent = new Intent(this, NewGameActivity.class);
            intent.putExtra("Player1", player1);
            intent.putExtra("Player2", player2);
            intent.putExtra("WinningScore", winningScore);

            intent.putExtra("Activity", "NEW"); //We want to know from which activity we came into the Game Activity. Was it New Game or Resume ?

            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            //We don't want to return to this activity with the Back Button again, so we call finish()
            finish();
        }
    }

    // ********************************************************
    //      Override  (Back Button pressed)
    // ********************************************************
    @Override
    public void onBackPressed()
    {
        Intent returnToMainActivity = new Intent(this, MainActivity.class);
        startActivity(returnToMainActivity);

        //We don't want to return to this activity with the Back Button again, so we call finish()
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        //overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

}