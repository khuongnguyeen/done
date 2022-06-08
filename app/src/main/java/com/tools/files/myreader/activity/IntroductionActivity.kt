package com.tools.files.myreader.activity

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.widget.TextView
import com.tools.files.myreader.R
import com.tools.files.myreader.base.App.Companion.checkOne
import com.tools.files.myreader.ocr.general.PermissionUtils
import com.tools.files.myreader.ulti.SharedPreferencesUtility
import com.tools.files.myreader.ulti.Common.pushEventAnalytics
import com.tools.files.myreader.ulti.Common.saveData

class IntroductionActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        if (SharedPreferencesUtility.getCheckPermission(this)) {
            launchMainScreen(this@IntroductionActivity)
        }

        PermissionUtils.isPermission(PERMISSION_EXTERNAL, this)
    }

    private fun launchMainScreen(activity: Activity) {
        SharedPreferencesUtility.setCheckPermission(activity, true)
    }

    override fun onBackPressed() {}

    private fun startMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PERMISSION_EXTERNAL) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    if (!checkOne) pushEventAnalytics("tutorial_complete")
                    saveData(this, "checkOne", true)
                    startMain()
                } else {
                    try {
                        val dialog = Dialog(this)
                        dialog.requestWindowFeature(1)
                        dialog.setContentView(R.layout.dialog_explain_permision)
                        dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
                        dialog.setCanceledOnTouchOutside(false)
                        dialog.setCancelable(false)
                        (dialog.findViewById(R.id.txtOk) as TextView).setOnClickListener {
                            PermissionUtils.isPermission(PERMISSION_EXTERNAL, this)
                            dialog.dismiss()
                        }
                        (dialog.findViewById(R.id.txtClose) as TextView).setOnClickListener {
                            finishAffinity()
                        }
                        dialog.show()
                    } catch (unused: Exception) {
                        finishAffinity()
                    }

                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_EXTERNAL) {
            if (grantResults.isNotEmpty() && permissions[0] == Manifest.permission.WRITE_EXTERNAL_STORAGE) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    GlobalScope.launch {
                    if (!checkOne) pushEventAnalytics("tutorial_complete")
                    saveData(this, "checkOne", true)
                    startMain()
//                    }
                } else {
//                    PermissionUtils.isPermission(PERMISSION_EXTERNAL, this@IntroductionActivity)
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    companion object {
        const val PERMISSION_EXTERNAL = 123
    }
}