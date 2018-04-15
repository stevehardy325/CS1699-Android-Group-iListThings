package edu.pitt.cs1699.team8;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.R.layout.simple_list_item_1;

public class AddRecipe extends AppCompatActivity {

    ArrayList<Item> items;
    ListView itemView;
    Context addRecContext;
    BackendManager bm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipie);
        items = new ArrayList<>();
        Bundle receivedBundle = getIntent().getExtras();
        if(receivedBundle != null){
            String multiItemString = receivedBundle.getString("multipleItemData");
            try{
                JSONObject multiItemJson = new JSONObject(multiItemString);
                JSONArray itemsJSON = multiItemJson.getJSONArray("Items");
                for (int i = 0; i < itemsJSON.length(); i++) {
                    JSONObject itemJSON = itemsJSON.getJSONObject(i);
                    String name = itemJSON.getString("Name");
                    double price = itemJSON.getDouble("Price");
                    long quan = itemJSON.getLong("Quantity");
                    Item item = new Item(name, price, quan);
                    items.add(item);
                }
            }catch(Exception e){
                Log.v("STUFF",e.toString());
                Log.v("STUFF",multiItemString);
            }
        }
        itemView = findViewById(R.id.itemView);
        itemView.setAdapter(new ArrayAdapter<>(this, R.layout.custom_list_item, items));
        addRecContext = this;
        bm = new BackendManager();
    }

    public void addItemClick(View v) {
        final Dialog dia = new Dialog(this);
        dia.setContentView(R.layout.dialog_add_item);
        dia.show();
        Button getButton = dia.findViewById(R.id.addItemButton);

        final EditText getName = dia.findViewById(R.id.item_input);
        final EditText getPrice = dia.findViewById(R.id.price_input);
        final EditText getQuantity = dia.findViewById(R.id.quantity_input);

        getButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = getName.getText().toString();
                String priceString = getPrice.getText().toString();
                double price = Double.parseDouble(priceString);
                String quanString = getQuantity.getText().toString();
                int quantity = Integer.parseInt(quanString);
                Item a = new Item(name, price, quantity);

                items.add(a);
                itemView.invalidate();
                itemView.setAdapter(new ArrayAdapter<>(addRecContext, R.layout.custom_list_item, items));
                dia.dismiss();
            }
        });

    }

    public void submitClick(View v) {
        for (Item i : items) {
            String name = i.getName();
            double price = i.getPrice();
            long quan = i.getQuantity();
            bm.addItem(name, price, quan);
        }

        finish();
    }

}