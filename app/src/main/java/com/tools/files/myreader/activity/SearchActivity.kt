package com.tools.files.myreader.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.all.me.io.helpers.utils.FileUtility
import com.tools.files.myreader.R
import com.tools.files.myreader.adapter.RecyclerViewAdapter
import com.tools.files.myreader.base.BaseActivity
import java.io.File
import java.util.*

class SearchActivity : BaseActivity() {
    private var mFoodList = mutableListOf<Any>()
    private var mSearchList = mutableListOf<Any>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        mIoIOnTemClickListener = this
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val textView: TextView = findViewById(R.id.titlt_bar)
        textView.text = getString(R.string.app_name)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        onGettingFile()
    }

    private fun onGettingFile() {

        mFoodList.addAll(FileUtility.mAllFileOffice)
        mSearchList.addAll(FileUtility.mAllFileOffice)
        adapter =
            RecyclerViewAdapter(mSearchList, this@SearchActivity, this@SearchActivity, true)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this@SearchActivity)
        recyclerView.adapter = adapter
        val editTextSearch = findViewById<EditText>(R.id.editTextSearch)
        editTextSearch.addTextChangedListener(textWatcher)
    }

    private val textWatcher: TextWatcher = object : TextWatcher {
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun afterTextChanged(s: Editable) {
            search(s.toString().lowercase(Locale.ROOT))
        }
    }

    private fun search(searchTerm: String) {
        mSearchList.clear()
        for (item in mFoodList) {

            if (item is File) if (item.name!!.lowercase(Locale.ROOT).contains(searchTerm)) {
                mSearchList.add(item)
            }
        }
        adapter!!.notifyDataSetChanged()
    }

}