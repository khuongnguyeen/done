package com.tools.files.myreader.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.*
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatSpinner
import androidx.recyclerview.widget.DividerItemDecoration

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tools.files.myreader.R
import com.tools.files.myreader.adapter.SelectPDFAdapter
import com.tools.files.myreader.interfaces.ItemSelectClickListener
import com.tools.files.myreader.model.BackList
import com.tools.files.myreader.model.DataListMerge
import com.tools.files.myreader.model.ItemPDFModel
import com.tools.files.myreader.ulti.*

import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tools.files.myreader.ulti.Common
import kotlinx.android.synthetic.main.activity_select_pdf.*
import kotlinx.coroutines.*

import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class SelectPDFActivity : AppCompatActivity(), ItemSelectClickListener {
    override fun onOpenPDF(itemPDFModel: ItemPDFModel) {}

    override fun onSplitClick(itemPDFModel: ItemPDFModel) {
        dialogSplit(itemPDFModel)
    }

    override fun onExtractTextClick(itemPDFModel: ItemPDFModel) {
    }

    override fun onExtractImageClick(itemPDFModel: ItemPDFModel) {
        showDialogExtractImage(itemPDFModel)
    }

    override fun onPDFToImageClick(itemPDFModel: ItemPDFModel) {
        startActivity(
            Intent(
                this,
                ConvertPdf::class.java
            ).putExtra("path", itemPDFModel.path).putExtra("key", ConstantSPKey.KEY_PDF_TO_IMAGE)
        )
//        val f = File(itemPDFModel.path)
       //todo

    }

    override fun onCompressClick(itemPDFModel: ItemPDFModel) {
        showDialogCompressPDF(itemPDFModel)
    }

    override fun onRemovePageClick(itemPDFModel: ItemPDFModel) {
    }

    private fun showDialogCompressPDF(itemPDFModel: ItemPDFModel) {
        val builder = AlertDialog.Builder(this,R.style.CustomDialog)
        builder.setTitle(getString(R.string.compress_lv))
        // add a radio button list
        val mode = arrayOf(
            "Low",
            "Medium",
            "Hight"
        )
        var checkedItem = 2
        var value = 20
        builder.setSingleChoiceItems(mode, checkedItem) { _, which ->
            when (which) {
                0 -> {
                    checkedItem = 0
                    value = 70
                }
                1 -> {
                    checkedItem = 1
                    value = 50
                }
                2 -> {
                    checkedItem = 2
                    value = 20
                }

            }

        }
        builder.setPositiveButton(getString(R.string.compress)
        ) { _, _ ->
            startActivity(
                Intent(
                    this@SelectPDFActivity,
                    ConvertPdf::class.java
                ).putExtra("path", itemPDFModel.path).putExtra(
                    "key",
                    Action.COMPRESS_PDF
                ).putExtra(Action.DL_CP_VALUE, value.toString())
                    .putExtra("value", value.toString())
            )
        }
        builder.setNegativeButton(getString(R.string.cancel)) { _, _ -> }
        val dialog = builder.create()// create and show the alert dialog
        val view=(dialog).window
        view?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun showDialogExtractImage(itemPDFModel: ItemPDFModel) {
        val builder = AlertDialog.Builder(this,R.style.CustomDialog)
        builder.setTitle(getString(R.string.image_quality))
        val mode = arrayOf(
            "Low",
            "Medium",
            "Hight"
        )
        var checkedItem = 1
        var value = 65
        builder.setSingleChoiceItems(mode, checkedItem) { _, which ->
            when (which) {
                0 -> {
                    checkedItem = 0
                    value = 30
                }
                1 -> {
                    checkedItem = 1
                    value = 65
                }
                2 -> {
                    checkedItem = 2
                    value = 100
                }
            }
        }
        builder.setPositiveButton(getString(R.string.extract)) { _, _ ->
            startActivity(
                Intent(
                    this@SelectPDFActivity,
                    ConvertPdf::class.java
                ).putExtra("path", itemPDFModel.path).putExtra("key", Action.EXTRACT_IMG)
                    .putExtra("value", value.toString())
            )
        }
        builder.setNegativeButton(getString(R.string.cancel)) { _, _ -> }


        val dialog = builder.create()
        dialog.setOnShowListener {
            val view=(dialog).window
            view?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        dialog.show()
    }

    private fun dialogSplit(itemPDFModel: ItemPDFModel) {
        var nb = 0
        try {

            val myDocument = PDDocument.load(File(itemPDFModel.path))
            nb = myDocument.numberOfPages
        } catch (e: Exception) {
            Log.d("ConvertPdfTo", "Error " + e.message)
        }
        val builder = AlertDialog.Builder(this,R.style.CustomDialog)
        builder.setTitle("Split PDF")
        val layoutInflater = LayoutInflater.from(this).inflate(R.layout.dialog_split, null)
        val spn = layoutInflater.findViewById<AppCompatSpinner>(R.id.spn)
        val edAt = layoutInflater.findViewById<EditText>(R.id.at)
        val edFrom = layoutInflater.findViewById<EditText>(R.id.from)
        val edTo = layoutInflater.findViewById<EditText>(R.id.to)
        val tvNumPage = layoutInflater.findViewById<TextView>(R.id.tvNumberPagesDialog)
        val sb = StringBuilder()
        sb.append(getString(R.string.tv_number_of_page))
        sb.append(" ")
        sb.append(nb)
        tvNumPage.text = sb.toString()
        builder.setView(layoutInflater)
        var index = 0
        spn.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {
                        index = 0
                        edAt.visibility = View.INVISIBLE
                        edFrom.visibility = View.INVISIBLE
                        edTo.visibility = View.INVISIBLE
                    }
                    1 -> {
                        index = 1
                        edAt.visibility = View.VISIBLE
                        edFrom.visibility = View.INVISIBLE
                        edTo.visibility = View.INVISIBLE
                    }
                    2 -> {
                        index = 2
                        edAt.visibility = View.INVISIBLE
                        edFrom.visibility = View.VISIBLE
                        edTo.visibility = View.VISIBLE
                    }
                }
            }
        }
        builder.setNegativeButton(getString(R.string.cancel), null)
        builder.setPositiveButton(getString(R.string.ok), null)
        val dialog: AlertDialog = builder.create()

        dialog.setOnShowListener(object : DialogInterface.OnShowListener {
            override fun onShow(dl: DialogInterface?) {
                val btnPB: Button = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                val view=(dialog).window
                view?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                btnPB.setOnClickListener(object : View.OnClickListener {
                    override fun onClick(v: View?) {
                        Log.d("qeeeee", "sss")
                        when (index) {
                            0 -> {
                                startActivity(
                                    Intent(
                                        this@SelectPDFActivity,
                                        ConvertPdf::class.java
                                    ).putExtra("path", itemPDFModel.path).putExtra(
                                        "key",
                                        ConstantSPKey.KEY_SPLIT
                                    ).putExtra(Action.ACTION_INTENT, Action.DL_ALL)
                                )
                                dialog.cancel()
                            }
                            1 -> {
                                val vlAt = edAt.text.toString().toInt()
                                if (vlAt <= 0 || vlAt > nb) {
                                    edAt.error = getString(R.string.invalid_value)
                                } else {
                                    startActivity(
                                        Intent(
                                            this@SelectPDFActivity,
                                            ConvertPdf::class.java
                                        ).putExtra("path", itemPDFModel.path).putExtra(
                                            "key",
                                            ConstantSPKey.KEY_SPLIT
                                        ).putExtra(
                                            Action.ACTION_INTENT,
                                            Action.DL_AT
                                        ).putExtra(Action.DL_VALUE_AT, vlAt.toString())
                                    )
                                    dialog.cancel()
                                }
                            }
                            2 -> {
                                try {
                                    val vlFrom = edFrom.text.toString().toInt()
                                    val vlTo = edTo.text.toString().toInt()
                                    if (vlFrom in 1..nb) {
                                        if (vlTo <= 0 || vlTo > nb || vlTo <= vlFrom) {
                                            edTo.error = getString(R.string.invalid_value)
                                            return
                                        } else {
                                            startActivity(
                                                Intent(
                                                    this@SelectPDFActivity,
                                                    ConvertPdf::class.java
                                                ).putExtra("path", itemPDFModel.path)
                                                    .putExtra("key", ConstantSPKey.KEY_SPLIT)
                                                    .putExtra(
                                                        Action.ACTION_INTENT,
                                                        Action.DL_FROM_TO
                                                    )
                                                    .putExtra(
                                                        Action.DL_VALUE_FROM,
                                                        vlFrom.toString()
                                                    )
                                                    .putExtra(Action.DL_VALUE_TO, vlTo.toString())
                                            )
                                            dialog.cancel()
                                        }
                                        return
                                    }
                                    edFrom.error = getString(R.string.invalid_value)
                                    return
                                } catch (e: Exception) {
                                    Log.e("exxxx", "Error " + e.message)
                                }
                            }
                        }
                    }
                })
            }
        })
        dialog.show()
    }


    lateinit var sharedPreferences: SharedPreferences
    var start = 0L
    var gridLayout = false
    var action = ""
    private var mFoodList = mutableListOf<DataListMerge>()
    private var mSearchList = mutableListOf<DataListMerge>()
    lateinit var selectPDFAdapter: SelectPDFAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_pdf)
        start = System.currentTimeMillis()
        getDataIntent()
        sharedPreferences = getSharedPreferences(ConstantSPKey.ACTIVITY_SETTING_KEY, Context.MODE_PRIVATE)
        tb_select_pdf.setTitle(R.string.select_file)
        tb_select_pdf.setNavigationOnClickListener {
            onBackPressed()
        }

        GetFile().execute()
        fcb.setOnClickListener {
            val lst = ArrayList<ItemPDFModel>()
            BackList.instance!!.list.forEach {
                if (it.click == true) {
                    lst.add(it.itemPDFModel!!)
                }
            }
            if (lst.size <= 0) {
                Toast.makeText(
                    this@SelectPDFActivity, "\n" + getString(R.string.you_have_not_selected_the_file_to_merge), Toast.LENGTH_SHORT
                ).show()
            } else {
                startActivity(
                    Intent(
                        this@SelectPDFActivity,
                        OrganizeMergePDFActivity::class.java
                    )
                )
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    inner class GetFile : AsyncTask<Void, Void, ArrayList<DataListMerge>>() {
        override fun doInBackground(vararg p0: Void?): ArrayList<DataListMerge> {
            val fileStorage = FileStorage()
            val file = File(Environment.getExternalStorageDirectory().toString())
            if (Build.VERSION.SDK_INT >= 29) {
                fileStorage.allPdfs(this@SelectPDFActivity).forEach {
                    BackList.instance!!.list.add(DataListMerge(it))
                }
            } else {
                fileStorage.getList(file).forEach {
                    BackList.instance!!.list.add(DataListMerge(it))
                }
            }
            return BackList.instance!!.list
        }

        override fun onPostExecute(result: ArrayList<DataListMerge>?) {
            onPost(result)
        }

    }

    override fun onResume() {
        super.onResume()
        checkFile()

    }

    fun checkFile(){
        CoroutineScope(Dispatchers.Main).launch {
            val fileStorage = FileStorage()
            val file = File(Environment.getExternalStorageDirectory().toString())
            val lst = ArrayList<DataListMerge>()
            if (Build.VERSION.SDK_INT > 29) {
                fileStorage.allPdfs(this@SelectPDFActivity).forEach {
                    lst.add(DataListMerge(it))
                }
            } else {
                fileStorage.getList(file).forEach {
                    lst.add(DataListMerge(it))
                }
            }
            val result: ArrayList<DataListMerge>? = BackList.instance!!.list
            withContext(Dispatchers.Main){
                if (result!!.size!=BackList.instance!!.list.size) {
                    BackList.instance!!.list = result
                    updateRecyclerView(result)
                }
            }
        }
    }

    fun updateRecyclerView(arr: ArrayList<DataListMerge>) {
        var mColor = 0
        recycler_view_select_file.setHasFixedSize(true)
        mColor = 0
        mSearchList.addAll(arr)
        mFoodList.addAll(arr)

        mSearchList.sortWith { row1, row2 ->
            if (Date(File(row1.itemPDFModel!!.path).lastModified()) < Date(File(row2.itemPDFModel!!.path).lastModified())) {
                return@sortWith 1
            }
            if (Date(File(row1.itemPDFModel!!.path).lastModified()) == Date(File(row2.itemPDFModel!!.path).lastModified())) return@sortWith 0
            -1
        }
        selectPDFAdapter = SelectPDFAdapter(this, mSearchList, gridLayout, mColor, action)
        if (!gridLayout) {
            val layoutManager = LinearLayoutManager(this)
            layoutManager.orientation = LinearLayoutManager.VERTICAL
            recycler_view_select_file.layoutManager = layoutManager
            recycler_view_select_file.addItemDecoration(
                DividerItemDecoration(
                    this,
                    DividerItemDecoration.VERTICAL
                )
            )
        } else {
            val layoutManager = GridLayoutManager(this, 3)
            recycler_view_select_file.layoutManager = layoutManager
        }
        recycler_view_select_file.adapter = selectPDFAdapter
        selectPDFAdapter.notifyDataSetChanged()
    }

    private fun onPost(arr: ArrayList<DataListMerge>?) {
        if (tv_loaddataSL.visibility == View.VISIBLE) {
            tv_loaddataSL.visibility = View.INVISIBLE
        }
        updateRecyclerView(arr!!)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return true
    }

    override fun onDestroy() {
        BackList.instance!!.list.clear()
        super.onDestroy()
    }

    @SuppressLint("RestrictedApi")
    fun getDataIntent() {
        try {
            action = intent.getStringExtra(Action.ACTION_INTENT).toString()
        } catch (e: Exception) {
            Log.d("exxx", "Error " + e.message)
        }
        if (action == Action.MERGE_PDF) {
            fcb.visibility = View.VISIBLE
        } else {
            fcb.visibility = View.GONE
        }
    }

}
