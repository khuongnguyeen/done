package com.tools.files.myreader.activity

import android.widget.TextView
import android.widget.RelativeLayout
import android.os.Bundle
import android.content.Intent
import android.view.View
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.tools.files.myreader.R
import com.tools.files.myreader.customview.OnSingleClickListener
import com.tools.files.myreader.fragment.CreateNewFileFragment
import com.tools.files.myreader.fragment.CreateTemplateFragment

class CreateFileActivity : FragmentActivity(), View.OnClickListener, DrawerLayout.DrawerListener {
    private var mPosType = 0
    private var imvPopupCreateFilePro: ImageView? = null
    private var imvBack: ImageView? = null
    private var imvPopupCreateFileAds: ImageView? = null
    private var txvPopupCreateFileTemplate: TextView? = null
    private var txvPopupCreateFileNewFile: TextView? = null
    private var fragmentManager: FragmentManager? = null
    private var createNewFileFragment: CreateNewFileFragment? = null
    private var createTemplateFragment: CreateTemplateFragment? = null
    private var rllShareApp: RelativeLayout? = null
    private var cvTemplate: CardView? = null
    private var cvNewFile: CardView? = null
    private var mDrawerLayout: DrawerLayout? = null
    private var isClickDrawer = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_file)
        initView()
        initData()
        dataFromIntent
    }

    private val dataFromIntent: Unit
        private get() {
            mPosType = intent.getIntExtra("CREATE_FILE", 0)
            checkClickCategory(mPosType)
        }

    protected fun initView() {
        fragmentManager = supportFragmentManager
        imvBack = findViewById(R.id.imv_back)
        imvBack!!.setOnClickListener(object :OnSingleClickListener(){
            override fun onSingleClick(v: View?) {
                onBackPressed()
            }
        })
        imvPopupCreateFilePro = findViewById(R.id.imv_activity_create_file__pro)
        imvPopupCreateFileAds = findViewById(R.id.imv_activity_create_file__ads)
        txvPopupCreateFileTemplate = findViewById(R.id.tv_template)
        txvPopupCreateFileNewFile = findViewById(R.id.tv_new_file)
        mDrawerLayout = findViewById(R.id.dl_create_file)
        cvTemplate = findViewById(R.id.cv_template)
        cvNewFile = findViewById(R.id.cv_new_file)
        createNewFileFragment = CreateNewFileFragment()
        createTemplateFragment = CreateTemplateFragment()
        fragmentManager!!
            .beginTransaction()
            .add(R.id.vpg_popup_create_file_pager, createNewFileFragment!!)
            .add(R.id.vpg_popup_create_file_pager, createTemplateFragment!!)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commitAllowingStateLoss()
    }

    protected fun initData() {
        txvPopupCreateFileNewFile!!.setOnClickListener(this)
        txvPopupCreateFileTemplate!!.setOnClickListener(this)
        imvPopupCreateFilePro!!.setOnClickListener(this)
        imvPopupCreateFileAds!!.setOnClickListener(this)
        mDrawerLayout!!.setDrawerListener(this)

    }

    private fun checkClickCategory(pos: Int) {
        when (pos) {
            0 -> {
                fragmentManager!!
                    .beginTransaction()
                    .show(createNewFileFragment!!)
                    .hide(createTemplateFragment!!)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commitAllowingStateLoss()
                cvNewFile!!.setCardBackgroundColor(ContextCompat.getColor(this, R.color.blue))
                txvPopupCreateFileNewFile!!.setTextColor(ContextCompat.getColor(this, R.color.colorWhite))
                cvTemplate!!.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite))
                txvPopupCreateFileTemplate!!.setTextColor(ContextCompat.getColor(this, R.color.colorGray))
            }
            1 -> {
                fragmentManager!!
                    .beginTransaction()
                    .show(createTemplateFragment!!)
                    .hide(createNewFileFragment!!)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commitAllowingStateLoss()
                cvNewFile!!.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite))
                txvPopupCreateFileNewFile!!.setTextColor(ContextCompat.getColor(this, R.color.colorGray))
                cvTemplate!!.setCardBackgroundColor(ContextCompat.getColor(this, R.color.blue))
                txvPopupCreateFileTemplate!!.setTextColor(ContextCompat.getColor(this, R.color.colorWhite))
            }
        }
    }

    override fun onClick(view: View) {
        if (view === txvPopupCreateFileNewFile) {
            checkClickCategory(0)
        } else if (view === txvPopupCreateFileTemplate) {
            checkClickCategory(1)
        } else if (view === rllShareApp) {
            mDrawerLayout!!.closeDrawers()
            val sharingIntent = Intent(Intent.ACTION_SEND)
            sharingIntent.type = "text/plain"
            val shareBody = "Please share app with everyone!"
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here")
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
            startActivity(Intent.createChooser(sharingIntent, "Share via"))
        }
    }

    override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}
    override fun onDrawerOpened(drawerView: View) {
        isClickDrawer = false
    }

    override fun onDrawerClosed(drawerView: View) {
        isClickDrawer = true
    }

    override fun onDrawerStateChanged(newState: Int) {}
}