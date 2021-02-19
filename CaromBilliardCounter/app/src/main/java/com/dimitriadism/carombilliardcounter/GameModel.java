package com.dimitriadism.carombilliardcounter;

public class GameModel {

    final Integer id;
    final String date;
    final String player1Name;
    final String player2Name;
    final Integer player1Score;
    final Integer player2Score;

    public GameModel(Integer id, String date, String player1Name, Integer player1Score, String player2Name, Integer player2Score)
    {
        this.id = id;
        this.date = date;
        this.player1Name = player1Name;
        this.player1Score = player1Score;
        this.player2Name = player2Name;
        this.player2Score = player2Score;
    }


    public Integer getId() {
        return id;
    }

    /*public void setId(Integer id) {
        this.id = id;
    }*/

    public String getDate() {
        return date;
    }

    /*public void setDate(String date) {
        this.date = date;
    }*/

    public String getPlayer1Name() {
        return player1Name;
    }

    /*public void setPlayer1Name(String player1Name) {

        this.player1Name = player1Name;
    }*/

    public String getPlayer2Name() {
        return player2Name;
    }

    /*public void setPlayer2Name(String player2Name) {
        this.player2Name = player2Name;
    }*/

    public Integer getPlayer1Score() {
        return player1Score;
    }

    /*public void setPlayer1Score(Integer player1Score) {
        this.player1Score = player1Score;
    }*/

    public Integer getPlayer2Score() {
        return player2Score;
    }

    /*public void setPlayer2Score(Integer player2Score) {
        this.player2Score = player2Score;
    }*/
}
