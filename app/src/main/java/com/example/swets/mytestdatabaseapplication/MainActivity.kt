package com.example.swets.mytestdatabaseapplication

import android.accounts.AccountManager
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.widget.EditText
import com.google.android.gms.auth.GoogleAuthUtil
import com.google.android.gms.common.AccountPicker
import java.util.*

class MainActivity : AppCompatActivity() {
    internal var dbHelper: DBHelper? = null
    internal var model: Model? = null
    private var modelList: ArrayList<Model>? = null
    private var mID: EditText? = null
    private var mName: EditText? = null
    private var mAddress: EditText? = null
    private var adapter: RecyclerViewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        modelList = ArrayList<Model>()
        mID = findViewById(R.id.editId) as EditText
        mName = findViewById(R.id.editName) as EditText
        mAddress = findViewById(R.id.editAddress) as EditText
        val mRecyclerViewList = findViewById(R.id.recyclerViewList) as RecyclerView
        dbHelper = DBHelper(this@MainActivity)
        adapter = RecyclerViewAdapter(this@MainActivity, modelList)
        mRecyclerViewList.layoutManager = LinearLayoutManager(this@MainActivity)
        mRecyclerViewList.adapter = adapter

        getUsername()


        findViewById(R.id.buttonSaveToDB).setOnClickListener { addEntry() }

        findViewById(R.id.buttonGetFromDB).setOnClickListener { adapter!!.refreshData() }
        findViewById(R.id.button_updateDB).setOnClickListener {
            model = Model()
            if (TextUtils.isEmpty(mID!!.text)) {
                mID!!.error = "Enter Id"
            } else if (TextUtils.isEmpty(mName!!.text)) {
                mName!!.error = "enter name"
            } else if (TextUtils.isEmpty(mAddress!!.text)) {
                mAddress!!.error = "Enter Address"
            } else {
                model!!.id = Integer.parseInt(mID!!.text.toString())
                model!!.name = mName!!.text.toString()
                model!!.address = mAddress!!.text.toString()
                dbHelper!!.updateRow(model, this@MainActivity)
                mID!!.setText("")
                mName!!.setText("")
                mAddress!!.setText("")
                adapter!!.refreshData()
            }
        }

        findViewById(R.id.button_delete_row).setOnClickListener {
            if (!TextUtils.isEmpty(mID!!.text.toString())) {
                if (dbHelper!!.deleteRow(Integer.parseInt(mID!!.text.toString()))) {
                    showSnackbar("Row Deleted succesfully")
                    adapter!!.refreshData()
                } else
                    showSnackbar("ID not found")

            } else {
                mID!!.error = "Enter some data for deletion"
            }
        }
    }

    fun getUsername() {
        try {
            val intent = AccountPicker.newChooseAccountIntent(null, null,
                    arrayOf(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE), false, null, null, null, null)
            startActivityForResult(intent, REQUEST_CODE_EMAIL)
        } catch (e: ActivityNotFoundException) {
            // TODO
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_EMAIL && resultCode == Activity.RESULT_OK) {
            val accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME)

            Log.d("--username", "" + data.getStringExtra(AccountManager.KEY_USERDATA))
        }
    }

    private fun addEntry() {
        model = Model()
        if (TextUtils.isEmpty(mID!!.text))
            mID!!.error = "Enter Id"
        else if (TextUtils.isEmpty(mName!!.text))
            mName!!.error = "enter name"
        else if (TextUtils.isEmpty(mAddress!!.text))
            mAddress!!.error = "Enter Address"
        else {
            model!!.id = Integer.parseInt(mID!!.text.toString())
            model!!.name = mName!!.text.toString()
            model!!.address = mAddress!!.text.toString()
            dbHelper!!.addRow(model, this@MainActivity)
            adapter!!.refreshData()
            mID!!.setText("")
            mName!!.setText("")
            mAddress!!.setText("")
        }

    }

    fun showSnackBar(message: String, id: Int) {
        Snackbar.make(findViewById(R.id.coordinator_layout), message, Snackbar.LENGTH_LONG)
                .setAction("DELETE") {
                    dbHelper!!.deleteRow(id)
                    adapter!!.refreshData()
                    Snackbar.make(findViewById(R.id.coordinator_layout), "DATA Deleted!!", Snackbar.LENGTH_SHORT).show()
                }.show()

    }

    fun showSnackbar(message: String) {
        Snackbar.make(findViewById(R.id.coordinator_layout), message, Snackbar.LENGTH_LONG).show()
    }

    companion object {
        private val REQUEST_CODE_EMAIL = 1
    }
}
