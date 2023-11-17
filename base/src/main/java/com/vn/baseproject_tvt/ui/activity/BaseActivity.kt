package com.vn.baseproject_tvt.ui.activity

import android.app.ActionBar
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.viewbinding.ViewBinding
import com.vn.baseproject_tvt.R
import com.vn.baseproject_tvt.customViews.loading.BaseLoadingView
import com.vn.baseproject_tvt.databinding.ActivityBaseBinding
import com.vn.baseproject_tvt.utils.NetworkUtil
import org.greenrobot.eventbus.EventBus

abstract class BaseActivity : AppCompatActivity() {
    private lateinit var binding: ViewBinding
    private lateinit var rootView: ActivityBaseBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = Color.TRANSPARENT
        }
        rootView = DataBindingUtil.inflate(layoutInflater, R.layout.activity_base, null, false)
        binding = getBindingView()
        rootView.container.addView(binding.root)
        rootView.loading.addView(getLoadingView())
        setContentView(rootView.root)
        initView(savedInstanceState, binding)
        NetworkUtil(this).observe(this) {
            if (!it){
                Toast.makeText(this, resources.getString(R.string.you_are_offline), Toast.LENGTH_SHORT).show()
            }
        }
    }

    abstract fun getBindingView(): ViewBinding

    abstract fun initView(savedInstanceState: Bundle?, binding: ViewBinding)

    open fun getLoadingView(): View {
        return BaseLoadingView(this).apply {
            layoutParams = ConstraintLayout.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT
            )
        }
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

}