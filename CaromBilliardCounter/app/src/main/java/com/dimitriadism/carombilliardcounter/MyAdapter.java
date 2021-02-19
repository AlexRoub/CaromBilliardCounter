package com.dimitriadism.carombilliardcounter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {

    final Context context;
    final ArrayList<GameModel> arrayListGames;

    public MyAdapter(Context context, ArrayList<GameModel> arrayListGames)
    {
        this.context = context;
        this.arrayListGames = arrayListGames;
    }

    @Override
    public int getCount()
    {
        return this.arrayListGames.size();
    }

    @Override
    public GameModel getItem(int position)
    {
        return arrayListGames.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @SuppressLint({"InflateParams", "ViewHolder"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.list_adapter, null);

        //This Image Button will be enabled only when a row from the list is selected
        ImageButton removeRow = convertView.findViewById(R.id.removeRow);
        removeRow.setVisibility(View.INVISIBLE);

        TextView dateView = convertView.findViewById(R.id.date_id);
        TextView player1NameView = convertView.findViewById(R.id.player1Name);
        TextView player1ScoreView = convertView.findViewById(R.id.player1Score);
        TextView player2NameView = convertView.findViewById(R.id.player2Name);
        TextView player2ScoreView = convertView.findViewById(R.id.player2Score);

        GameModel gameModel = arrayListGames.get(position);

        dateView.setText(gameModel.getDate());
        player1NameView.setText(gameModel.getPlayer1Name());
        player1ScoreView.setText(String.valueOf(gameModel.getPlayer1Score()));
        player2NameView.setText(gameModel.getPlayer2Name());
        player2ScoreView.setText(String.valueOf(gameModel.getPlayer2Score()));

        return convertView;
    }

    @Override
    public boolean isEnabled(int position)
    {
        return true;
    }

}