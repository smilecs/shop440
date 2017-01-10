package com.shop440;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.shop440.Models.Store;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewStore extends AppCompatActivity {
    Store store;
    @BindView(R.id.store_name) EditText storeName;
    @BindView(R.id.description) EditText description;
    @BindView(R.id.specialisation) EditText specialisation;
    @BindView(R.id.phone) EditText phone;
    @OnClick(R.id.submit) void submit(){
        store.setPhone(phone.getText().toString());
        store.setDescription(description.getText().toString());
        store.setSpecialisation(specialisation.getText().toString());
        store.setName(storeName.getText().toString());
        Intent i = new Intent(NewStore.this, GetLocation.class);
        i.putExtra("data", store);
        startActivity(i);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_store);
        store = new Store();
        ButterKnife.bind(this);

    }
}
