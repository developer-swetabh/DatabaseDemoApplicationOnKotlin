package com.example.swets.mytestdatabaseapplication;

import android.accounts.AccountManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_EMAIL = 1;
    DBHelper dbHelper;
    Model model;
    private ArrayList<Model> modelList;
    private EditText mID, mName, mAddress;
    private RecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        modelList = new ArrayList<>();
        mID = (EditText) findViewById(R.id.editId);
        mName = (EditText) findViewById(R.id.editName);
        mAddress = (EditText) findViewById(R.id.editAddress);
        RecyclerView mRecyclerViewList = (RecyclerView) findViewById(R.id.recyclerViewList);
        dbHelper = new DBHelper(MainActivity.this);
        adapter = new RecyclerViewAdapter(MainActivity.this, modelList);
        mRecyclerViewList.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        mRecyclerViewList.setAdapter(adapter);

        getUsername();


        findViewById(R.id.buttonSaveToDB).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEntry();

            }
        });

        findViewById(R.id.buttonGetFromDB).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                adapter.refreshData();

            }
        });
        findViewById(R.id.button_updateDB).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model = new Model();
                if (TextUtils.isEmpty(mID.getText().toString().trim())) {
                    mID.setError("Enter Id");
                } else if (TextUtils.isEmpty(mName.getText().toString().trim())) {
                    mName.setError("enter name");
                } else if (TextUtils.isEmpty(mAddress.getText().toString().trim())) {
                    mAddress.setError("Enter Address");
                } else {
                    model.setId(Integer.parseInt(mID.getText().toString()));
                    model.setName(mName.getText().toString());
                    model.setAddress(mAddress.getText().toString());
                    dbHelper.updateRow(model, MainActivity.this);
                    mID.setText("");
                    mName.setText("");
                    mAddress.setText("");
                    adapter.refreshData();
                }

            }
        });

        findViewById(R.id.button_delete_row).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(mID.getText().toString())) {
                    if (dbHelper.deleteRow(Integer.parseInt(mID.getText().toString()))) {
                        showSnackbar("Row Deleted succesfully");
                        adapter.refreshData();
                    } else
                        showSnackbar("ID not found");

                } else {
                    mID.setError("Enter some data for deletion");
                }


            }
        });
    }

    public void getUsername() {
        try {
            Intent intent = AccountPicker.newChooseAccountIntent(null, null,
                    new String[]{GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE}, false, null, null, null, null);
            startActivityForResult(intent, REQUEST_CODE_EMAIL);
        } catch (ActivityNotFoundException e) {
            // TODO
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_EMAIL && resultCode == RESULT_OK) {
            String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);

            Log.d("--username", "" + data.getStringExtra(AccountManager.KEY_USERDATA));
        }
    }

    private void addEntry() {
        model = new Model();
        if (TextUtils.isEmpty(mID.getText().toString().trim()))
            mID.setError("Enter Id");
        else if (TextUtils.isEmpty(mName.getText().toString().trim()))
            mName.setError("enter name");
        else if (TextUtils.isEmpty(mAddress.getText().toString().trim()))
            mAddress.setError("Enter Address");
        else {
            model.setId(Integer.parseInt(mID.getText().toString()));
            model.setName(mName.getText().toString());
            model.setAddress(mAddress.getText().toString());
            dbHelper.addRow(model, MainActivity.this);
            adapter.refreshData();
            mID.setText("");
            mName.setText("");
            mAddress.setText("");
        }

    }

    public void showSnackBar(String message, final int id) {
        Snackbar.make(findViewById(R.id.coordinator_layout), message, Snackbar.LENGTH_LONG)
                .setAction("DELETE", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dbHelper.deleteRow(id);
                        adapter.refreshData();
                        Snackbar.make(findViewById(R.id.coordinator_layout), "DATA Deleted!!", Snackbar.LENGTH_SHORT).show();
                    }
                }).show();

    }

    public void showSnackbar(String message) {
        Snackbar.make(findViewById(R.id.coordinator_layout), message, Snackbar.LENGTH_LONG).show();
    }
}
