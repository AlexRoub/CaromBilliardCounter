package com.dimitriadism.carombilliardcounter;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper myAppDatabase ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myAppDatabase = new DatabaseHelper(this);

        Button newGame = findViewById(R.id.newGame);
        newGame.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openNewGame();
            }
        } );

        Button resumeLastGame = findViewById(R.id.resumeLastGame);
        resumeLastGame.setOnClickListener(new View.OnClickListener()
        {
           @Override
           public void onClick(View v)
           {
               openResumeLastGame();
           }
        });

        Button gamesList = findViewById(R.id.gamesList);
        gamesList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                openGamesList();
            }
        });

    }


    // *****************************************************************************************************************************************
    //                                                         METHODS
    // *****************************************************************************************************************************************

    public void openNewGame()
    {
        //Check in DB for Unfinished Game (is_finished = 0)
        final Cursor cur = myAppDatabase.checkForUnfinishedGame();
        cur.moveToFirst();

        final Intent intentNames = new Intent(this, NamesActivity.class);

        if (cur.getCount() > 0)
        {
            AlertDialog.Builder dialogBox1 = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogStyle);
            dialogBox1.setTitle("Νέο Παιχνίδι");
            dialogBox1.setMessage("Βρέθηκε παιχνίδι που είχε διακοπεί. Είστε σίγουροι ότι θέλετε να το διαγράψετε και να ξεκινήσετε καινούριο?");

            dialogBox1.setPositiveButton("ΟΧΙ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i)
                {
                    dialog.dismiss();

                    if(!cur.isClosed())
                        cur.close();
                }
            });

            dialogBox1.setNegativeButton("ΝΑΙ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i)
                {
                    int idUnfinishedIndex = cur.getColumnIndex("ID");
                    int idUnfinished = cur.getInt(idUnfinishedIndex);

                    if(!cur.isClosed())
                        cur.close();

                    Integer deletedGame = myAppDatabase.deleteUnfinishedGame(idUnfinished);
                    if(deletedGame == 1)
                    {
                        startActivity(intentNames);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                        finish();
                    }
                }
            });

            dialogBox1.create().show();

        }
        else //No Unfinished Game in DB, go to the next Activity to fill the players' names and the winning score
        {
            startActivity(intentNames);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            finish();
        }

    }


    public void openResumeLastGame()
    {
        //Check in DB for Unfinished Game (is_finished = 0)
        final Cursor res = myAppDatabase.checkForUnfinishedGame();
        res.moveToFirst();

        //There is an unfinished game in DB
        if (res.getCount() > 0)
        {
            AlertDialog.Builder dialogBox2 = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogStyle);
            dialogBox2.setTitle("Επαναφορά Παιχνιδιού");
            dialogBox2.setMessage("Βρέθηκε παιχνίδι που είχε διακοπεί. Είστε σίγουροι ότι θέλετε να το συνεχίσετε?");

            dialogBox2.setPositiveButton("ΟΧΙ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i)
                {
                    dialog.dismiss();

                    if(!res.isClosed())
                        res.close();
                }
            });

            dialogBox2.setNegativeButton("ΝΑΙ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i)
                {
                    //GET THE INFORMATION WE NEED FROM THE DATABASE ABOUT THE UNFINISHED GAME, SO WE PASS IT TO THE GAME ACTIVITY

                    int idUnfinishedIndex = res.getColumnIndex("ID");
                    int idUnfinished = res.getInt(idUnfinishedIndex);

                    int player1UnfinishedIndex = res.getColumnIndex("PLAYER_1");
                    String player1Unfinished = res.getString(player1UnfinishedIndex);

                    int currentScore1UnfinishedIndex = res.getColumnIndex("CURRENT_SCORE_1");
                    int currentScore1Unfinished = res.getInt(currentScore1UnfinishedIndex);

                    int player2UnfinishedIndex = res.getColumnIndex("PLAYER_2");
                    String player2Unfinished = res.getString(player2UnfinishedIndex);

                    int currentScore2UnfinishedIndex = res.getColumnIndex("CURRENT_SCORE_2");
                    int currentScore2Unfinished = res.getInt(currentScore2UnfinishedIndex);

                    int winningScoreUnfinishedIndex = res.getColumnIndex("WINNING_SCORE");
                    int winningScoreUnfinished = res.getInt(winningScoreUnfinishedIndex);

                    int player1TurnUnfinishedIndex = res.getColumnIndex("PLAYER_1_TURN");
                    int player1TurnUnfinished = res.getInt(player1TurnUnfinishedIndex);

                    int dateUnfinishedIndex = res.getColumnIndex("TIMESTAMP");
                    String dateUnfinished = res.getString(dateUnfinishedIndex);

                    if(!res.isClosed())
                        res.close();

                    //******************************************************************************************************
                    //                              Go to the Game Activity
                    // *****************************************************************************************************
                    openGameResume(idUnfinished, player1Unfinished, currentScore1Unfinished, player2Unfinished, currentScore2Unfinished, winningScoreUnfinished, player1TurnUnfinished, dateUnfinished);
                }
            });

            dialogBox2.create().show();

        }
        else //No Unfinished Game in DB, let the User know he can't resume any.
        {
            Toast.makeText(this, "Δεν υπάρχει ανεκπλήρωτο παιχνίδι για να συνεχίσετε!", Toast.LENGTH_LONG).show();

            if(!res.isClosed())
                res.close();
        }

    }


    public void openGamesList()
    {
        Intent intentList = new Intent(this, GamesList.class);
        startActivity(intentList); // Go to List Activity
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        finish();

        /*final Cursor cursor = myAppDatabase.checkForFinishedGame();
        cursor.moveToFirst();

        if (cursor.getCount() > 0) // There are finished games, the List will open
        {
            if(!cursor.isClosed())
                cursor.close();

            Intent intentList = new Intent(this, GamesList.class);
            startActivity(intentList); // Go to List Activity
            finish();
        }
        else // NO finished game, throw Message for the User!!!
        {
            if(!cursor.isClosed())
                cursor.close();

            Toast.makeText(this, "Η λίστα είναι άδεια. Δεν υπάρχει ολοκληρωμένο παιχνίδι!", Toast.LENGTH_LONG).show();
        }*/
    }


    public void openGameResume(int idUnfinished, String player1Unfinished, int currentScore1Unfinished, String player2Unfinished, int currentScore2Unfinished, int winningScoreUnfinished, int player1TurnUnfinished, String dateUnfinished)
    {
        Intent intentGame = new Intent(this, NewGameActivity.class);

        intentGame.putExtra("idUnfinished", idUnfinished);
        intentGame.putExtra("player1Unfinished", player1Unfinished);
        intentGame.putExtra("currentScore1Unfinished", currentScore1Unfinished);
        intentGame.putExtra("player2Unfinished", player2Unfinished);
        intentGame.putExtra("currentScore2Unfinished", currentScore2Unfinished);
        intentGame.putExtra("winningScoreUnfinished", winningScoreUnfinished);
        intentGame.putExtra("player1TurnUnfinished", player1TurnUnfinished);
        intentGame.putExtra("dateUnfinished", dateUnfinished);

        intentGame.putExtra("Activity", "RESUME"); //We want to know from which activity we came into the Game Activity. Was it New Game or Resume ?

        startActivity(intentGame);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        finish();
    }

}