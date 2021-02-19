package com.dimitriadism.carombilliardcounter;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PopUpWinner extends AppCompatActivity {

    public PopUpWinner()
    {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // 18-10-2020 Bug Fix for API 26 -> Eskage otan pigaine na anoiksei to Pop-Up, mono stis suskeues me 26ari logismiko
        if (android.os.Build.VERSION.SDK_INT != Build.VERSION_CODES.O)
        {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_winner);

        //*****************************************************************************
        //                  Pop Up Orientation
        //*****************************************************************************
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width*.8) , (int) (height*.6) );

        //To cover the whole PopUp, before this the 4 corners outside the borders were not colored
        getWindow().setBackgroundDrawableResource(R.drawable.corners_for_popup);


        //*****************************************************************************
        //                  Data Set up
        //*****************************************************************************
        Bundle extras = getIntent().getExtras();

        assert extras != null;
        String player1 = extras.getString("Player1");
        String player2 = extras.getString("Player2");
        int player1Score = extras.getInt("Player1Score");
        int player2Score = extras.getInt("Player2Score");

        TextView player1TextView = findViewById(R.id.player1Name);
        player1TextView.setText(player1);

        TextView player2TextView = findViewById(R.id.player2Name);
        player2TextView.setText(player2);

        TextView player1ScoreTextView = findViewById(R.id.player1Score);
        player1ScoreTextView.setText(String.valueOf(player1Score));

        TextView player2ScoreTextView = findViewById(R.id.player2Score);
        player2ScoreTextView.setText(String.valueOf(player2Score));

        TextView winnerNameTextView = findViewById(R.id.winnerName);

        TextView loserNameTextView;
        if (player1Score > player2Score)  //Player 1 Winner!!!!!!!!!!!!!!
        {
            winnerNameTextView.setText(player1);

            loserNameTextView = findViewById(R.id.loserName);
            loserNameTextView.setText(player2);
        }
        else //Player 2 Winner!!!!!!!!!!!!!!
        {
            winnerNameTextView.setText(player2);

            loserNameTextView = findViewById(R.id.loserName);
            loserNameTextView.setText(player1);
        }


        Button finish = findViewById(R.id.finish);
        finish.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder dialogBox1 = new AlertDialog.Builder(PopUpWinner.this, R.style.AlertDialogStyle);
                dialogBox1.setTitle("Επιστροφή στο μενού");
                dialogBox1.setMessage("Το παιχνίδι ολοκληρώθηκε. Είστε σίγουροι ότι θέλετε να επιστρέψετε στο μενού?");

                dialogBox1.setPositiveButton("ΟΧΙ", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int i)
                    {
                        dialog.dismiss();
                    }
                });

                dialogBox1.setNegativeButton("ΝΑΙ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i)
                    {
                        returnToMenu(); //Use this to return to Main Activity
                    }
                });

                dialogBox1.create().show();

            }
        } );
    }

    @Override
    public void onBackPressed()
    {
        AlertDialog.Builder dialogBox = new AlertDialog.Builder(PopUpWinner.this, R.style.AlertDialogStyle);
        dialogBox.setTitle("Επιστροφή στο μενού");
        dialogBox.setMessage("Το παιχνίδι ολοκληρώθηκε. Είστε σίγουροι ότι θέλετε να επιστρέψετε στο μενού?");

        dialogBox.setPositiveButton("ΟΧΙ", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int i)
            {
                dialog.dismiss();

            }
        });

        dialogBox.setNegativeButton("ΝΑΙ", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int i)
            {
                returnToMenu(); //Use this to return to Main Activity
            }
        });

        dialogBox.create().show();
    }

    public void returnToMenu()
    {
        Intent returnToMainActivity = new Intent(this, MainActivity.class);
        startActivity(returnToMainActivity);

        //We don't want to return to this activity with the Back Button again, so we call finishAffinity() -> finish both Pop-Up and the Game activity behind it (black screen)
        finishAffinity();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        //overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }


}