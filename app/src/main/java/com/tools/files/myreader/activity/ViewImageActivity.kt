package com.tools.files.myreader.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.tools.files.myreader.R
import com.tools.files.myreader.adapter.ViewImageAdapter
import com.tools.files.myreader.ulti.Action
import com.tools.files.myreader.ulti.FileStorage
import kotlinx.android.synthetic.main.activity_view_image.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.File
import java.lang.Exception

class ViewImageActivity : AppCompatActivity() {
    lateinit var viewImageAdapter: ViewImageAdapter
    lateinit var sharedPreferences: SharedPreferences
    lateinit var mList: ArrayList<File>
    var action = ""
    var uri = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_image)
        setSupportActionBar(tb_view_image)
        val mActionBar = supportActionBar
        mActionBar!!.setDisplayHomeAsUpEnabled(true)
        try {
            action = intent.getStringExtra(Action.ACTION_INTENT).toString()
            if (action == Action.PDF_TO_IMG) {
                uri = intent.getStringExtra(Action.IMG_FOLDER).toString()
                val file2 = File(uri)
                mActionBar.title = file2.name
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Error Exception", Toast.LENGTH_LONG).show()
        }
//        GetFile().execute()
        getFile()
    }

    private fun initRecyclerView() {
        rv_view_image.setHasFixedSize(true)
        viewImageAdapter =
            ViewImageAdapter(this, mList)
        val layoutManager = GridLayoutManager(this, 1)
        rv_view_image.layoutManager = layoutManager
        rv_view_image.adapter = viewImageAdapter
        viewImageAdapter.notifyDataSetChanged()
    }

    private fun getFile(){
        val job = CoroutineScope(Dispatchers.IO).async {
            return@async if (action == Action.PDF_TO_IMG) {
                val fileStorage = FileStorage()
                val file = File(uri)
                mList = fileStorage.getListFileIMG(file)
                mList
            } else {
                val fileStorage = FileStorage()
                val file = File(Environment.getExternalStorageDirectory().toString() + "/Pictures/")
                mList = fileStorage.getListFileIMG(file)
                mList
            }
        }

        CoroutineScope(Dispatchers.Main).launch {
            initRecyclerView()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_view, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
            R.id.mn_home -> {
                startActivity(
                    Intent(
                        this,
                        MainActivity::class.java
                    ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                )
            }
        }
        return true
    }

}
