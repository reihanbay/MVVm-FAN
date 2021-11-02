package com.trytolearn.mvvmfan.view.activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.trytolearn.mvvmfan.R
import com.trytolearn.mvvmfan.adapter.QuotesAdapter
import com.trytolearn.mvvmfan.model.ModelQuotes
import com.trytolearn.mvvmfan.viewmodel.QuotesViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var quotesVM : QuotesViewModel
    private lateinit var quotesAdapter : QuotesAdapter
    private lateinit var progressDialog : ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        }
        if (Build.VERSION.SDK_INT >= 21 ) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            window.statusBarColor = Color.TRANSPARENT
        }

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait..")
        progressDialog.setCancelable(false)
        progressDialog.setMessage("Display Quotes")


        //Set Data adapter
        quotesAdapter = QuotesAdapter(this)
        rvRandowQuotes.layoutManager = LinearLayoutManager(this)
        rvRandowQuotes.adapter = quotesAdapter
        rvRandowQuotes.setHasFixedSize(true)

        //Set ViewModel
        quotesVM = ViewModelProvider(this).get(QuotesViewModel::class.java)
        quotesVM.setRandomQuotes()
        progressDialog.show()
        quotesVM.getRandomQuotes().observe(this) { modelQuotes: ArrayList<ModelQuotes> ->

            if (modelQuotes.size != 0) {
                quotesAdapter.setQuotesAdapter(modelQuotes)
            } else {
                Toast.makeText(this, "Quotes Not Found!", Toast.LENGTH_LONG).show()
            }
            progressDialog.dismiss()
        }

        //open search activity
        fabSearch.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

        getGreetingText()
    }

    private fun getGreetingText() {
        val calendar = Calendar.getInstance()
        val timeOfDay = calendar[Calendar.HOUR_OF_DAY]
        if (timeOfDay >= 0 && timeOfDay < 12) {
            tvGreetings.text = "Good Morning"
        }
        when(timeOfDay) {
            in 0..11 -> tvGreetings.text = "Good Morning"
            in 12..14 -> tvGreetings.text = "Good Afternoon"
            in 15..17 -> tvGreetings.text = "Good Evening"
            in 18..24 -> tvGreetings.text = "Good Night"
        }
    }
    companion object {
        fun setWindowFlag(activity: Activity, bits: Int, on: Boolean) {
            val window = activity.window
            val layoutParams = window.attributes
            if (on) {
                layoutParams.flags = layoutParams.flags or bits
            } else {
                layoutParams.flags = layoutParams.flags and bits.inv()
            }
            window.attributes = layoutParams
        }
    }
}