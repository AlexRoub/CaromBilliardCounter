package com.dimitriadism.carombilliardcounter;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class NewGameActivity extends AppCompatActivity {

    String player1;
    String player2;
    Integer winningScore;
    TextView currentScore1textView;
    TextView currentScore2textView;
    EditText addScore1editText;
    EditText addScore2editText;
    private int currentScore1;
    private int currentScore2;
    private Button okAdd1;
    private Button okAdd2;
    private Button okRemove1;
    private Button okRemove2;
    private ImageButton player1TurnArrow;
    private ImageButton player2TurnArrow;
    private int player1turn; //8a paiksoun to rolo tou TRUE kai FALSE ( 1 kai 0 ), gia na kserw sto Resume Last Game poianou seira einai na paiksei
    private int player2turn;
    private ImageButton addOneToAddScore1;
    private ImageButton addOneToAddScore2;
    private ImageView minusOneFromAddScore1;
    private ImageView minusOneFromAddScore2;

    ConstraintLayout myLayout;

    DatabaseHelper myAppDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);

        TextView player2textView; //Local variable instead of Global with the others, so I get ZERO warnings in compiling
        TextView player1textView; //Local variable instead of Global with the others, so I get ZERO warnings in compiling

        myAppDatabase = new DatabaseHelper(this);

        // *****************************************************************************************************************************************
        //                      Get Data from the previous Activity (either Resume or New Game)
        // *****************************************************************************************************************************************

        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {

            String previousActivity = extras.getString("Activity"); //We have to know the Previous Activity!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

            // ***************************************************************************************************************************
            //                     NEW GAME!!! Initialize everything and Insert to DB
            // ***************************************************************************************************************************
            assert previousActivity != null;
            if(previousActivity.equals("NEW"))
            {
                player1 = extras.getString("Player1");
                player2 = extras.getString("Player2");
                winningScore = extras.getInt("WinningScore");

                player1textView = findViewById(R.id.currentPlayer1);
                player1textView.setText(player1);

                player2textView = findViewById(R.id.currentPlayer2);
                player2textView.setText(player2);

                currentScore1textView = findViewById(R.id.currentScore1);
                currentScore1textView.setText("0");

                currentScore2textView = findViewById(R.id.currentScore2);
                currentScore2textView.setText("0");

                final TextView winningScoreTextView = findViewById(R.id.currentWinningScore);
                winningScoreTextView.setText(String.valueOf(winningScore));


                // ****************************************************************************
                //                   Database Setup to insert the New Game
                // ****************************************************************************

                myAppDatabase.insertNewGame( player1, player2, winningScore);


                // ****************************************************************************
                //               Buttons And Views Appearance
                // ****************************************************************************

                okAdd1 = findViewById(R.id.okAdd1);
                okAdd2 = findViewById(R.id.okAdd2);
                okRemove1 = findViewById(R.id.okRemove1);
                okRemove2 = findViewById(R.id.okRemove2);
                player1TurnArrow = findViewById(R.id.player1Turn);
                player2TurnArrow = findViewById(R.id.player2Turn);

                addScore1editText = findViewById(R.id.addScore1);
                addScore2editText = findViewById(R.id.addScore2);
                addOneToAddScore1 = findViewById(R.id.addOneToAddScore1);
                addOneToAddScore2 = findViewById(R.id.addOneToAddScore2);
                minusOneFromAddScore1 = findViewById(R.id.removeOneFromAddScore1);
                minusOneFromAddScore2 = findViewById(R.id.removeOneFromAddScore2);

                okAdd2.setVisibility(View.INVISIBLE);
                okRemove2.setVisibility(View.INVISIBLE);
                addOneToAddScore2.setVisibility(View.INVISIBLE);
                minusOneFromAddScore2.setVisibility(View.INVISIBLE);
                player2TurnArrow.setVisibility(View.INVISIBLE);
                addScore2editText.setVisibility(View.INVISIBLE);
                player1turn = 1;
                player2turn = 0;

                addScore1editText.setText("0");
                addScore1editText.requestFocus();

            } // END OF NEW GAME INITIALIZATION

            // ***************************************************************************************************************************
            //                     RESUME LAST PAUSED GAME!!! Initialize everything with the data we retrieved
            //                                                at the Previous Activity from the DB.
            // ***************************************************************************************************************************
            else if(previousActivity.equals("RESUME"))
            {
                player1 = extras.getString("player1Unfinished");
                currentScore1 = extras.getInt("currentScore1Unfinished");
                player2 = extras.getString("player2Unfinished");
                currentScore2 = extras.getInt("currentScore2Unfinished");
                winningScore = extras.getInt("winningScoreUnfinished");
                player1turn = extras.getInt("player1TurnUnfinished");

                player1textView = findViewById(R.id.currentPlayer1);
                player1textView.setText(player1);

                currentScore1textView = findViewById(R.id.currentScore1);
                currentScore1textView.setText(String.valueOf(currentScore1));

                player2textView = findViewById(R.id.currentPlayer2);
                player2textView.setText(player2);

                currentScore2textView = findViewById(R.id.currentScore2);
                currentScore2textView.setText(String.valueOf(currentScore2));

                final TextView winningScoreTextView = findViewById(R.id.currentWinningScore);
                winningScoreTextView.setText(String.valueOf(winningScore));

                // ****************************************************************************
                //               Buttons And Views Appearance
                // ****************************************************************************

                okAdd1 = findViewById(R.id.okAdd1);
                okAdd2 = findViewById(R.id.okAdd2);
                okRemove1 = findViewById(R.id.okRemove1);
                okRemove2 = findViewById(R.id.okRemove2);
                player1TurnArrow = findViewById(R.id.player1Turn);
                player2TurnArrow = findViewById(R.id.player2Turn);

                addScore1editText = findViewById(R.id.addScore1);
                addScore2editText = findViewById(R.id.addScore2);
                addOneToAddScore1 = findViewById(R.id.addOneToAddScore1);
                addOneToAddScore2 = findViewById(R.id.addOneToAddScore2);
                minusOneFromAddScore1 = findViewById(R.id.removeOneFromAddScore1);
                minusOneFromAddScore2 = findViewById(R.id.removeOneFromAddScore2);

                if(player1turn == 1)
                {
                    okAdd2.setVisibility(View.INVISIBLE);
                    okRemove2.setVisibility(View.INVISIBLE);
                    addOneToAddScore2.setVisibility(View.INVISIBLE);
                    minusOneFromAddScore2.setVisibility(View.INVISIBLE);
                    player2TurnArrow.setVisibility(View.INVISIBLE);
                    addScore2editText.setVisibility(View.INVISIBLE);

                    addScore1editText.setText("0");
                    addScore1editText.setSelection(1);
                    addScore1editText.requestFocus();
                }
                else
                {
                    okAdd1.setVisibility(View.INVISIBLE);
                    okRemove1.setVisibility(View.INVISIBLE);
                    addOneToAddScore1.setVisibility(View.INVISIBLE);
                    minusOneFromAddScore1.setVisibility(View.INVISIBLE);
                    player1TurnArrow.setVisibility(View.INVISIBLE);
                    addScore1editText.setVisibility(View.INVISIBLE);

                    addScore2editText.setText("0");
                    addScore2editText.setSelection(1);
                    addScore2editText.requestFocus();
                }

            }

        }


        // *****************************************************************************************************************************************
        //     Otan o xrhsths pathsei gia na grapsei xeirokinhta to seri tou, ean einai 0 tote to kanei keno gia na mh xreiastei
        //     na svhnei prwta to 0    -> Paikths 1
        // *****************************************************************************************************************************************

        addScore1editText.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (addScore1editText.getText().toString().equals("0"))
                {
                    addScore1editText.setText("");
                }
            }
        });


        // *****************************************************************************************************************************************
        //     Otan o xrhsths pathsei gia na grapsei xeirokinhta to seri tou, ean einai 0 tote to kanei keno gia na mh xreiastei
        //     na svhnei prwta to 0    -> Paikths 2
        // *****************************************************************************************************************************************

        addScore2editText.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (addScore2editText.getText().toString().equals("0"))
                {
                    addScore2editText.setText("");
                }
            }
        });


        // ***********************************************************************************************************************************************************
        //     O xrhsths pataei ena apo ta 2 EditText, ean einai 0 ginetai '', kai twra 8elw otan pathsei kati allo (otidhpote mesa sthn o8onh) na ksanaginei 0
        // ***********************************************************************************************************************************************************

        myLayout = findViewById(R.id.activity_new_game);

        myLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if ( addScore1editText.getText().toString().equals("") )
                {
                    addScore1editText.setText("0");
                    addScore1editText.requestFocus();
                    addScore1editText.setSelection(1);//Cursor right after the 0
                }

                if ( addScore2editText.getText().toString().equals("") )
                {
                    addScore2editText.setText("0");
                    addScore2editText.requestFocus();
                    addScore2editText.setSelection(1);//Cursor right after the 0
                }
            }
        });


        // *****************************************************************************************************************************************
        //                  Pros8esh 1as karampolas sto seri tou Paixth 1
        // *****************************************************************************************************************************************

        addOneToAddScore1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Trexousa timh tou pediou pou kratame to seri
                String addScore1String = addScore1editText.getText().toString();

                String currentScoreToAdd1asString;

                //Tsekaroume ean exei plhktrologhsei kati o xrhsths, h an pataei to + prwta xwris na exei eisagei ari8mo xerata
                if(TextUtils.isEmpty(addScore1String))
                {
                    currentScoreToAdd1asString = String.valueOf(1);
                }
                else //Exei hdh grapsei ari8mo o xrhsths, kai pros8etei kai allh mia me to + koumpi
                {
                    int currentScoreToAdd1 = Integer.parseInt(addScore1String);
                    currentScoreToAdd1asString = String.valueOf(currentScoreToAdd1 + 1);
                }

                if(Integer.parseInt(currentScoreToAdd1asString) > 999)
                {
                    currentScoreToAdd1asString = String.valueOf(999);
                }

                addScore1editText.setText(currentScoreToAdd1asString);

                addScore1editText.setSelection(currentScoreToAdd1asString.length()); // Vazoume ton cursor amesws meta to teleutaio pshfio tou ari8mou pou kratame to seri. ka8ws to seri auksanetai me to pathma tou +
            }
        });


        // *****************************************************************************************************************************************
        //                  Pros8esh 1as karampolas sto seri tou Paixth 2
        // *****************************************************************************************************************************************

        addOneToAddScore2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Trexousa timh tou pediou pou kratame to seri
                String addScore2String = addScore2editText.getText().toString();

                String currentScoreToAdd2asString;

                //Tsekaroume ean exei plhktrologhsei kati o xrhsths, h an pataei to + prwta xwris na exei eisagei ari8mo xerata
                if(TextUtils.isEmpty(addScore2String))
                {
                    currentScoreToAdd2asString = String.valueOf(1);
                }
                else //Exei hdh grapsei ari8mo o xrhsths, kai pros8etei kai allh mia me to + koumpi
                {
                    int currentScoreToAdd2 = Integer.parseInt(addScore2String);
                    currentScoreToAdd2asString = String.valueOf(currentScoreToAdd2 + 1);
                }

                if(Integer.parseInt(currentScoreToAdd2asString) > 999)
                {
                    currentScoreToAdd2asString = String.valueOf(999);
                }

                addScore2editText.setText(currentScoreToAdd2asString);

                addScore2editText.setSelection(currentScoreToAdd2asString.length()); // Vazoume ton cursor amesws meta to teleutaio pshfio tou ari8mou pou kratame to seri. ka8ws to seri auksanetai me to pathma tou +
            }
        });


        // *****************************************************************************************************************************************
        //               Afairesh 1as karampolas apo to seri tou Paixth 1
        // *****************************************************************************************************************************************

        minusOneFromAddScore1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //Trexousa timh tou pediou pou kratame to seri
                String addScore1String = addScore1editText.getText().toString();

                String currentScoreToAdd1asString;

                if(TextUtils.isEmpty(addScore1String))
                {
                    addScore1editText.setText("0");
                }
                else if (addScore1String.equals("0"))
                {
                    currentScoreToAdd1asString = String.valueOf(0);
                    addScore1editText.setText(currentScoreToAdd1asString);
                    addScore1editText.setSelection(currentScoreToAdd1asString.length()); // Vazoume ton cursor amesws meta to teleutaio pshfio tou ari8mou pou kratame to seri. ka8ws to seri auksanetai me to pathma tou -
                }
                else
                {
                    int currentScoreToAdd1 = Integer.parseInt(addScore1String);
                    currentScoreToAdd1asString = String.valueOf(currentScoreToAdd1 - 1);
                    addScore1editText.setText(currentScoreToAdd1asString);
                    addScore1editText.setSelection(currentScoreToAdd1asString.length()); // Vazoume ton cursor amesws meta to teleutaio pshfio tou ari8mou pou kratame to seri. ka8ws to seri auksanetai me to pathma tou +
                }

            }
        });


        // *****************************************************************************************************************************************
        //                  Afairesh 1as karampolas apo to seri tou Paixth 2
        // *****************************************************************************************************************************************

        minusOneFromAddScore2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //Trexousa timh tou pediou pou kratame to seri
                String addScore2String = addScore2editText.getText().toString();

                String currentScoreToAdd2asString;

                if(TextUtils.isEmpty(addScore2String))
                {
                    addScore2editText.setText("0");
                }
                else if (addScore2String.equals("0"))
                {
                    currentScoreToAdd2asString = String.valueOf(0);
                    addScore2editText.setText(currentScoreToAdd2asString);
                    addScore2editText.setSelection(currentScoreToAdd2asString.length()); // Vazoume ton cursor amesws meta to teleutaio pshfio tou ari8mou pou kratame to seri. ka8ws to seri auksanetai me to pathma tou -
                }
                else
                {
                    int currentScoreToAdd2 = Integer.parseInt(addScore2String);
                    currentScoreToAdd2asString = String.valueOf(currentScoreToAdd2 - 1);
                    addScore2editText.setText(currentScoreToAdd2asString);
                    addScore2editText.setSelection(currentScoreToAdd2asString.length()); // Vazoume ton cursor amesws meta to teleutaio pshfio tou ari8mou pou kratame to seri. ka8ws to seri auksanetai me to pathma tou -
                }

            }
        });


        // *****************************************************************************************************************************************
        //                  Klhsh gia Pros8esh tou seri sto 1o skor
        // *****************************************************************************************************************************************

        okAdd1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                int scoreToAdd1 = addToScore1method();
                addScore1editText.setText("0");
                currentScore1 = currentScore1 + scoreToAdd1;


                if (currentScore1 >= winningScore)
                {
                    // *****************************************************************************************************************************************
                    //                              PLAYER 1 WINNER!!!!!!!!!!!!!!!!!!!!!!!  => Anoikse to Pop Up !!!!!
                    //            +  UPDATE DATABASE !
                    // *****************************************************************************************************************************************

                    // ************************************************************************************************
                    //          POPULATE DATABASE TO UPDATE THE FINISHED GAME (IS_FINISHED = 1) -> PLAYER 1 WINNER
                    // ************************************************************************************************
                    Cursor curf = myAppDatabase.checkForUnfinishedGame();
                    curf.moveToFirst();

                    int idUnfinishedIndex = curf.getColumnIndex("ID");
                    int idUnfinished = curf.getInt(idUnfinishedIndex);

                    if(!curf.isClosed())
                        curf.close();

                    myAppDatabase.updateFinishedGame(idUnfinished, currentScore1, currentScore2, player1, currentScore1, player2, currentScore2);

                    // ***************************************************************
                    //   POP UP METHOD -> Open Pop Up for the finished match info!
                    // ***************************************************************
                    popUpWinnerMethod(player1, player2, currentScore1, currentScore2, winningScore);
                }
                else //NO WINNER YET
                {
                    currentScore1textView.setText(String.valueOf(currentScore1));

                    okAdd1.setVisibility(View.INVISIBLE);
                    okAdd2.setVisibility(View.VISIBLE);
                    okRemove1.setVisibility(View.INVISIBLE);
                    okRemove2.setVisibility(View.VISIBLE);
                    addScore1editText.setVisibility(View.INVISIBLE);
                    addScore2editText.setVisibility(View.VISIBLE);
                    addOneToAddScore1.setVisibility(View.INVISIBLE);
                    addOneToAddScore2.setVisibility(View.VISIBLE);
                    minusOneFromAddScore1.setVisibility(View.INVISIBLE);
                    minusOneFromAddScore2.setVisibility(View.VISIBLE);
                    player1TurnArrow.setVisibility(View.INVISIBLE);
                    player2TurnArrow.setVisibility(View.VISIBLE);
                    player1turn = 0;
                    player2turn = 1;

                    addScore2editText.setText("0");
                    addScore2editText.requestFocus();
                    addScore2editText.setSelection(1);//Cursor right after the 0
                }
            }
        });


        // *****************************************************************************************************************************************
        //                  Klhsh gia Pros8esh tou seri sto 2o skor
        // *****************************************************************************************************************************************

        okAdd2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int scoreToAdd2 = addToScore2method();
                addScore2editText.setText("0");
                currentScore2 = currentScore2 + scoreToAdd2;


                if(currentScore2 >= winningScore)
                {
                    // *****************************************************************************************************************************************
                    //                               PLAYER 2 WINNER!!!!!!!!!!!!!!!!!!!!!!!   => Anoikse to Pop Up !!!!!
                    //            +  UPDATE DATABASE!
                    // *****************************************************************************************************************************************

                    // ************************************************************************************************
                    //          POPULATE DATABASE TO UPDATE THE FINISHED GAME (IS_FINISHED = 1) -> PLAYER 2 WINNER
                    // ************************************************************************************************
                    Cursor curf2 = myAppDatabase.checkForUnfinishedGame();
                    curf2.moveToFirst();

                    int idUnfinishedIndex = curf2.getColumnIndex("ID");
                    int idUnfinished = curf2.getInt(idUnfinishedIndex);

                    if(!curf2.isClosed())
                        curf2.close();

                    myAppDatabase.updateFinishedGame(idUnfinished, currentScore1, currentScore2, player2, currentScore2, player1, currentScore1);

                    // ***************************************************************
                    //   POP UP METHOD -> Open Pop Up for the finished match info!
                    // ***************************************************************
                    popUpWinnerMethod(player1, player2, currentScore1, currentScore2, winningScore);

                }
                else // NO WINNER YET
                {
                    currentScore2textView.setText(String.valueOf(currentScore2));

                    okAdd1.setVisibility(View.VISIBLE);
                    okAdd2.setVisibility(View.INVISIBLE);
                    okRemove1.setVisibility(View.VISIBLE);
                    okRemove2.setVisibility(View.INVISIBLE);
                    addScore1editText.setVisibility(View.VISIBLE);
                    addScore2editText.setVisibility(View.INVISIBLE);
                    addOneToAddScore1.setVisibility(View.VISIBLE);
                    addOneToAddScore2.setVisibility(View.INVISIBLE);
                    minusOneFromAddScore1.setVisibility(View.VISIBLE);
                    minusOneFromAddScore2.setVisibility(View.INVISIBLE);
                    player1TurnArrow.setVisibility(View.VISIBLE);
                    player2TurnArrow.setVisibility(View.INVISIBLE);
                    player1turn = 1;
                    player2turn = 0;

                    addScore1editText.setText("0");
                    addScore1editText.requestFocus();
                    addScore1editText.setSelection(1);//Cursor right after the 0
                }


            }
        });


        // *****************************************************************************************************************************************
        //                  Klhsh gia Afairesh tou seri apo to 1o skor
        // *****************************************************************************************************************************************

        okRemove1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int scoreToRemove1 = removeFromScore1method();
                addScore1editText.setText("0");
                if( (currentScore1 - scoreToRemove1) < 0 )
                {
                    Toast.makeText(getApplicationContext(), "Δεν μπορείτε να αφαιρέσετε αυτες τις καραμπόλες!", Toast.LENGTH_SHORT).show();
                    addScore1editText.requestFocus();
                    addScore1editText.setSelection(1);//Cursor right after the 0
                }
                else
                {
                    currentScore1 = currentScore1 - scoreToRemove1 ;
                    currentScore1textView.setText(String.valueOf(currentScore1));

                    okAdd1.setVisibility(View.INVISIBLE);
                    okAdd2.setVisibility(View.VISIBLE);
                    okRemove1.setVisibility(View.INVISIBLE);
                    okRemove2.setVisibility(View.VISIBLE);
                    addScore1editText.setVisibility(View.INVISIBLE);
                    addScore2editText.setVisibility(View.VISIBLE);
                    addOneToAddScore1.setVisibility(View.INVISIBLE);
                    addOneToAddScore2.setVisibility(View.VISIBLE);
                    minusOneFromAddScore1.setVisibility(View.INVISIBLE);
                    minusOneFromAddScore2.setVisibility(View.VISIBLE);
                    player1TurnArrow.setVisibility(View.INVISIBLE);
                    player2TurnArrow.setVisibility(View.VISIBLE);
                    player1turn = 0;
                    player2turn = 1;

                    addScore2editText.setText("0");
                    addScore2editText.requestFocus();
                    addScore2editText.setSelection(1);//Cursor right after the 0
                }
            }
        });


        // *****************************************************************************************************************************************
        //                      Klhsh gia Afairesh tou seri apo to 2o skor
        // *****************************************************************************************************************************************

        okRemove2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int scoreToRemove2 = removeFromScore2method();
                addScore2editText.setText("0");
                if( (currentScore2 - scoreToRemove2) < 0)
                {
                    Toast.makeText(getApplicationContext(), "Δεν μπορείτε να αφαιρέσετε αυτες τις καραμπόλες!", Toast.LENGTH_SHORT).show();
                    addScore2editText.requestFocus();
                    addScore2editText.setSelection(1);//Cursor right after the 0
                }
                else
                {
                    currentScore2 = currentScore2 - scoreToRemove2 ;
                    currentScore2textView.setText(String.valueOf(currentScore2));

                    okAdd1.setVisibility(View.VISIBLE);
                    okAdd2.setVisibility(View.INVISIBLE);
                    okRemove1.setVisibility(View.VISIBLE);
                    okRemove2.setVisibility(View.INVISIBLE);
                    addScore1editText.setVisibility(View.VISIBLE);
                    addScore2editText.setVisibility(View.INVISIBLE);
                    addOneToAddScore1.setVisibility(View.VISIBLE);
                    addOneToAddScore2.setVisibility(View.INVISIBLE);
                    minusOneFromAddScore1.setVisibility(View.VISIBLE);
                    minusOneFromAddScore2.setVisibility(View.INVISIBLE);
                    player1TurnArrow.setVisibility(View.VISIBLE);
                    player2TurnArrow.setVisibility(View.INVISIBLE);
                    player1turn = 1;
                    player2turn = 0;

                    addScore1editText.setText("0");
                    addScore1editText.requestFocus();
                    addScore1editText.setSelection(1);//Cursor right after the 0
                }
            }
        });

    }

    // *****************************************************************************************************************************************
    //                                                         METHODS
    // *****************************************************************************************************************************************

    public int addToScore1method()  //Katoxurwnei PROS8ESH karampoles sto score tou Paixth 1
    {
        EditText editTextAddScore1 = findViewById(R.id.addScore1);
        String addScore1 = editTextAddScore1.getText().toString().trim();

        //Check if Score to add is valid!!!
        if(!TextUtils.isEmpty(editTextAddScore1.getText()))
            return Integer.parseInt(addScore1);
        else
            return 0;
    }


    public int addToScore2method()  //Katoxurwnei PROS8ESH karampoles sto score tou Paixth 2
    {
        EditText editTextAddScore2 = findViewById(R.id.addScore2);
        String addScore2 = editTextAddScore2.getText().toString().trim();

        //Check if Score to add is valid!!!
        if(!TextUtils.isEmpty(editTextAddScore2.getText()))
            return Integer.parseInt(addScore2);
        else
            return 0;
    }


    public int removeFromScore1method()  //Katoxurwnei AFAIRESH karampolas apo to score tou Paixth 1
    {
        EditText editTextAddScore1 = findViewById(R.id.addScore1);
        String removeScore1 = editTextAddScore1.getText().toString().trim();

        //Check if Score to Remove is valid!!!
        if(!TextUtils.isEmpty(editTextAddScore1.getText()))
            return Integer.parseInt(removeScore1);
        else
            return 0;
    }


    public int removeFromScore2method()  //Katoxurwnei AFAIRESH karampoles apo to score tou Paixth 2
    {
        EditText editTextAddScore2 = findViewById(R.id.addScore2);
        String removeScore2 = editTextAddScore2.getText().toString().trim();

        //Check if Score to Remove is valid!!!
        if(!TextUtils.isEmpty(editTextAddScore2.getText()))
            return Integer.parseInt(removeScore2);
        else
            return 0;
    }


    public void popUpWinnerMethod(String player1NameParam, String player2Name2Param, int player1Score1Param, int player2Score2Param, int winningScoreParam)  //Anoigei to Pop Up
    {
        hideAllViews(); //Clear the Background Activity behind Pop-Up, when Pop-Up is about to open

        Intent intentPopup = new Intent(this, PopUpWinner.class);
        //intentPopup.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        intentPopup.putExtra("Player1", player1NameParam);
        intentPopup.putExtra("Player2", player2Name2Param);
        intentPopup.putExtra("Player1Score", player1Score1Param);
        intentPopup.putExtra("Player2Score", player2Score2Param);
        intentPopup.putExtra("WinningScore", winningScoreParam);

        startActivity(intentPopup);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        //We don't call finish now, because when Pop-Up opens, we still want this Activity in the Background (in black), so we call finishAffinity() to finish both Activities inside Pop-Up!!!!
        //finish();
    }


    // ***************************************************************************************
    //        Hide every View from Background Activity when Pop-Up for the winner opens
    // ***************************************************************************************
    public void hideAllViews()
    {
        ConstraintLayout currentActivity = findViewById(R.id.activity_new_game);

        currentActivity.setBackgroundColor(Color.GRAY);

        final int childCount = currentActivity.getChildCount();
        for (int i = 0; i < childCount; i++)
        {
            View v = currentActivity.getChildAt(i);
            v.setVisibility(View.INVISIBLE);
        }
    }


    public void returnToMenu()
    {
        Intent returnToMainActivity = new Intent(this, MainActivity.class);
        startActivity(returnToMainActivity);
        //We don't want to return to this activity with the Back Button again, so we call finish()
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        //overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }


    // ********************************************************
    //          Override for possible Game Loss
    // ********************************************************
    @Override
    public void onPause()
    {
        super.onPause();

        Cursor curp = myAppDatabase.checkForUnfinishedGame();
        curp.moveToFirst();

        if(curp.getCount() > 0 )
        {
            int idUnfinishedIndex = curp.getColumnIndex("ID");
            int idUnfinished = curp.getInt(idUnfinishedIndex);

            if(!curp.isClosed())
                curp.close();

            myAppDatabase.updatePausedGame(idUnfinished, currentScore1, currentScore2, player1turn, player2turn);
        }

        if(!curp.isClosed())
            curp.close();

    }


    // ********************************************************
    //      Override for Game Stop (Back Button pressed)
    // ********************************************************
    @Override
    public void onBackPressed()
    {
        AlertDialog.Builder dialogBox = new AlertDialog.Builder(NewGameActivity.this, R.style.AlertDialogStyle);
        dialogBox.setTitle("Διακοπή Παιχνιδιού");
        dialogBox.setMessage("Είστε σίγουροι ότι θέλετε να διακόψετε το παιχνίδι και να επιστρέψετε στο μενού? Θα έχετε τη δυνατότητα να συνεχίσετε από το σημείο που το διακόψατε.");

        dialogBox.setPositiveButton("ΟΧΙ", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int i)
            {
                dialog.dismiss();
            }
        });

        dialogBox.setNegativeButton("ΝΑΙ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i)
            {
                Cursor curb = myAppDatabase.checkForUnfinishedGame();
                curb.moveToFirst();

                int idUnfinishedIndex = curb.getColumnIndex("ID");
                int idUnfinished = curb.getInt(idUnfinishedIndex);

                if(!curb.isClosed())
                    curb.close();

                boolean isUpdated = myAppDatabase.updatePausedGame(idUnfinished, currentScore1, currentScore2, player1turn, player2turn);
                if(isUpdated)
                {
                    returnToMenu(); //Use this method to return to Main Activity
                }
            }
        });

        dialogBox.create().show();
    }


}