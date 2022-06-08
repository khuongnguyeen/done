package com.tools.files.myreader.activity

import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.tools.files.myreader.R
import com.tools.files.myreader.ulti.Action.IMAGE_URIS
import com.tools.files.myreader.ulti.Common.showDialogSweet

class GalleryActivity : AppCompatActivity() {

    val PICK_IMAGE_MULTIPLE = 1
    val listDataString: ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)
        val intent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(Intent.createChooser(intent, "Select images"), PICK_IMAGE_MULTIPLE)
    }

    private fun getRealPathFromURI(uri: Uri?): String? {
        if (uri == null) return null
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor = applicationContext.contentResolver.query(
            uri,
            projection,
            null,
            null,
            null
        )!!
        if (cursor != null) {
            val columnIndex: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            val result: String = cursor.getString(columnIndex)
            cursor.close()
            return result
        }
        return uri.path
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            if (requestCode == PICK_IMAGE_MULTIPLE) {
                if (resultCode == RESULT_OK) {
                    if (data!!.clipData != null) {
                        val count = data.clipData!!.itemCount
                        for (i in 0 until count) {
                            val a = getRealPathFromURI(data.clipData!!.getItemAt(i).uri)
                            if (a != null) listDataString.add(a)
                        }
                        val intent = Intent(this, OrganizeImagesActivity::class.java)
                        intent.putStringArrayListExtra(IMAGE_URIS, listDataString)
                        startActivity(intent)
                        finish()
                    } else {
                        showDialogSweet(this, SweetAlertDialog.SUCCESS_TYPE, "NULL")
                    }
                } else if (data!!.data != null) {
                    val imagePath = data.data!!.path
                    showDialogSweet(this, SweetAlertDialog.WARNING_TYPE, "$imagePath")
                }
            }
        } catch (e: Exception) {
            finish()
        }

    }
}