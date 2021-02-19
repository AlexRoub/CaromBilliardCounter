package com.dimitriadism.carombilliardcounter;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class GamesList extends AppCompatActivity {

    ListView myList;
    DatabaseHelper myAppDatabase;
    ArrayList<GameModel> arrayList;
    MyAdapter myAdapter;
    View selectedView;
    int selectedPosition = -1;
    boolean isClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games_list);

        myList = findViewById(R.id.listView);
        myAppDatabase = new DatabaseHelper(this);
        arrayList = new ArrayList<>();

        arrayList = myAppDatabase.getDataForList();
        myAdapter = new MyAdapter(this, arrayList);

        //Load Data in ListView
        myList.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();

        myList.setSmoothScrollbarEnabled(true);

        isClicked = false;

        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int nowClickingPosition, long id) {

                //Unhighlight the row, if it is selected and we press it again
                if ( (nowClickingPosition == selectedPosition) && (!isClicked))
                {
                    view.findViewById(R.id.removeRow).setVisibility(View.INVISIBLE);
                    view.setSelected(false);
                    isClicked = true;
                }
                // A new row is being clicked, lets highlight it!
                else
                {
                    //Enable the row that is now clicked
                    view.findViewById(R.id.removeRow).setVisibility(View.VISIBLE);
                    view.setSelected(true);
                    isClicked = false;
                }

                // Check if a different List row is selected, and of course don't take into consideration the first row that is being clicked! (That's why we don't want the -1 value, which is set in the beginning)
                if( (nowClickingPosition != selectedPosition) && (selectedPosition != -1) )
                {
                    //If a different row is selected, disable the button of the Previous row that was clicked
                    selectedView.findViewById(R.id.removeRow).setVisibility(View.INVISIBLE);
                    selectedView.setSelected(false);
                }

                /* Hold the View (the row that was clicked), and the position of the row in the list,
                   so we can Disable the Button when a different row is being clicked */
                selectedView = view;
                selectedPosition = nowClickingPosition;
            }
        });

    }

    //ImageButton onClick Method -> declared in list_adapter.xml
    @SuppressWarnings({"unused", "RedundantSuppression"}) // For the unused parameter View view
    public void DeleteGame(@NonNull final View view)
    {
        AlertDialog.Builder dialogBox = new AlertDialog.Builder(GamesList.this, R.style.AlertDialogStyle);
        dialogBox.setTitle("Διαγραφή Παιχνιδιού");
        dialogBox.setMessage("Είστε σίγουροι ότι θέλετε να διαγράψετε το συγκεκριμένο παιχνίδι από τη μνήμη?");

        dialogBox.setPositiveButton("ΟΧΙ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i)
            {
                dialog.dismiss();

                /*
                //Unhighlight the selected row, if NO is pressed

                view.findViewById(R.id.removeRow).setVisibility(View.INVISIBLE);
                view.setSelected(false);
                myAdapter.notifyDataSetChanged();
                */
            }
        });

        dialogBox.setNegativeButton("ΝΑΙ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i)
            {
                //Get the Key of the record from the "underlying" object of the clicked View
                GameModel clickedObject = myAdapter.getItem(selectedPosition);
                Integer idSelectedGame = clickedObject.getId();

                //Delete the selected Game from the DB, returns 1 if it is deleted
                if(myAppDatabase.deleteSelectedGame(idSelectedGame) == 1)
                {
                    //Remove the selected row from the Arraylist that holds the Objects
                    arrayList.remove(selectedPosition);

                    //Update the ListView with notifyDataSetChanged()
                    myAdapter.notifyDataSetChanged();

                    //So it can highlight the next row that is clicked
                    selectedPosition = -1;
                }
                else
                {
                    System.out.println("The game was not deleted");
                }
            }
        });

        dialogBox.create().show();

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