package com.example.alvarogomez.tfg2018;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Alvaro Gomez on 25/06/2018.
 */

public class MainFooterAdapter extends RecyclerView.Adapter<MainFooterAdapter.ViewHolder> {
    private String[] mDataset;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public Button mTextView;
        public ViewHolder(Button v) {
            super(v);
            mTextView = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MainFooterAdapter() {
        mDataset = setMainFooterButtons();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MainFooterAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        // create a new view
        Button v = (Button) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_button_view, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(mDataset[position]);
        holder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MainFooterButtonsOp mainFooterButtonsOp = new MainFooterButtonsOp();

                switch (position){

                    case 0:
                        mainFooterButtonsOp.indicesButtonOp();
                        break;
                    case 1:
                        mainFooterButtonsOp.prediccionesButtonOp();
                        break;
                    case 2:
                        mainFooterButtonsOp.favoritosButtonOp();
                        break;
                    case 3:
                        mainFooterButtonsOp.noticiasButtonOp();
                        break;

                }



            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }

    public String[] setMainFooterButtons (){

        String[] myMainFooterButtons = new String[4];

        myMainFooterButtons[0] = "Índices";
        myMainFooterButtons[1] = "Predicciones";
        myMainFooterButtons[2] = "Favoritos";
        myMainFooterButtons[3] = "Noticias";

        return myMainFooterButtons;
    }

}
