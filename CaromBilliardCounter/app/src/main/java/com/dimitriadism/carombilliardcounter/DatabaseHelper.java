package com.dimitriadism.carombilliardcounter;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "GAMES.db" ;
    private static final String TABLE_NAME = "GAMES_INFO" ;

    private static final String COL_1 = "ID" ;
    private static final String COL_2 = "PLAYER_1" ;
    private static final String COL_3 = "CURRENT_SCORE_1" ;
    private static final String COL_4 = "PLAYER_2" ;
    private static final String COL_5 = "CURRENT_SCORE_2" ;
    private static final String COL_6 = "WINNING_SCORE" ;
    private static final String COL_7 = "WINNER" ;
    private static final String COL_8 = "WINNER_SCORE" ;
    private static final String COL_9 = "LOSER" ;
    private static final String COL_10 = "LOSER_SCORE" ;
    private static final String COL_11 = "PLAYER_1_TURN";
    private static final String COL_12 = "PLAYER_2_TURN";
    private static final String COL_13 = "TIMESTAMP" ;
    private static final String COL_14 = "IS_FINISHED" ;



    DatabaseHelper(@Nullable Context context)
    {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, PLAYER_1 VARCHAR, CURRENT_SCORE_1 INTEGER, PLAYER_2 VARCHAR, CURRENT_SCORE_2 INTEGER, WINNING_SCORE INTEGER, WINNER VARCHAR, WINNER_SCORE INTEGER, LOSER VARCHAR, LOSER_SCORE INTEGER, PLAYER_1_TURN INTEGER, PLAYER_2_TURN INTEGER,TIMESTAMP VARCHAR, IS_FINISHED INTEGER)") ;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME );
        onCreate(db);
    }

    void insertNewGame(String player1, String player2, Integer winningScore)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        Calendar cal = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String dateString = sdf.format(cal.getTime());

        values.put(COL_2, player1);
        values.put(COL_3, 0);
        values.put(COL_4, player2);
        values.put(COL_5, 0);
        values.put(COL_6, winningScore);
        values.put(COL_7, (byte[]) null);
        values.put(COL_8, (Integer) null);
        values.put(COL_9, (byte[]) null);
        values.put(COL_10, (Integer) null);
        values.put(COL_11, 1);
        values.put(COL_12, 0);
        values.put(COL_13, dateString);
        values.put(COL_14, 0);

        db.insert(TABLE_NAME, null, values);
    }


    Cursor checkForUnfinishedGame()
    {
        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL_14 +"= 0", null);
    }


    boolean updatePausedGame(Integer id, Integer currentScore1, Integer currentScore2, Integer player1turn, Integer player2turn)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valuesPaused = new ContentValues();

        Calendar cal = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String dateString = sdf.format(cal.getTime());

        valuesPaused.put(COL_3, currentScore1);
        valuesPaused.put(COL_5, currentScore2);
        valuesPaused.put(COL_11, player1turn);
        valuesPaused.put(COL_12, player2turn);
        valuesPaused.put(COL_13, dateString);

        String idStr = String.valueOf(id);
        long result = db.update(TABLE_NAME, valuesPaused, "ID = ?", new String[] {idStr});
        return result != -1;
    }


    void updateFinishedGame(Integer id, Integer currentScore1, Integer currentScore2, String winner, Integer winnerScore, String loser, Integer loserScore)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valuesFinished = new ContentValues();

        Calendar cal = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String dateString = sdf.format(cal.getTime());

        valuesFinished.put(COL_3, currentScore1);
        valuesFinished.put(COL_5, currentScore2);
        valuesFinished.put(COL_7, winner);
        valuesFinished.put(COL_8, winnerScore);
        valuesFinished.put(COL_9, loser);
        valuesFinished.put(COL_10, loserScore);
        valuesFinished.put(COL_11, -1); //NO ONE'S TURN, GAME IS FINISHED
        valuesFinished.put(COL_12, -1); //NO ONE'S TURN, GAME IS FINISHED
        valuesFinished.put(COL_13, dateString);
        valuesFinished.put(COL_14, 1);

        String idStr = String.valueOf(id); //Gia na valw to WHERE ID = 0, h8ele to id na einai String

        db.update(TABLE_NAME, valuesFinished, "ID = ?", new String[]{idStr});
    }


    Integer deleteUnfinishedGame(Integer id)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        String idStr = String.valueOf(id);
        return db.delete(TABLE_NAME, "ID = ?", new String[] {idStr});
    }

    public ArrayList<GameModel> getDataForList()
    {
        ArrayList<GameModel> arrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        //Get Data Ordered by the key descending (first in the list comes the greatest ID, the newest Game that was completed).
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL_14 +"= 1" + " ORDER BY " + COL_1 + " DESC", null);

        while (cursor.moveToNext())
        {
            int gameId = cursor.getInt(0);
            String date = cursor.getString(12).substring(0,16); //date
            String player1Name = cursor.getString(6); //winner name
            Integer player1Score = cursor.getInt(7); //winner score
            String player2Name = cursor.getString(8); // loser name
            Integer player2Score = cursor.getInt(9); // loser score

            GameModel gameModel = new GameModel(gameId, date, player1Name, player1Score, player2Name, player2Score);
            arrayList.add(gameModel);
        }

        if(!cursor.isClosed())
            cursor.close();

        return arrayList;
    }

    Integer deleteSelectedGame(Integer id)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        String idStr = String.valueOf(id);
        return db.delete(TABLE_NAME, "ID = ?", new String[] {idStr});
    }

    /*Cursor checkForFinishedGame()
    {
        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL_14 +"= 1", null);
    }*/

}