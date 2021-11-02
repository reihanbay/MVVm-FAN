package com.trytolearn.mvvmfan.view.activity

import android.app.Activity
import android.app.ProgressDialog
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.trytolearn.mvvmfan.R
import com.trytolearn.mvvmfan.adapter.QuotesAdapter
import com.trytolearn.mvvmfan.model.ModelQuotes
import com.trytolearn.mvvmfan.viewmodel.QuotesViewModel
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.toolbar_main.*

class SearchActivity : AppCompatActivity() {

    var page = 0
    lateinit var strName : String
    lateinit var quotesAdapter : QuotesAdapter
    lateinit var quotesVM: QuotesViewModel
    lateinit var progressDialog: ProgressDialog

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            window.statusBarColor = Color.TRANSPARENT
        }

        setSupportActionBar(toolbar_search)
        if (supportActionBar!=null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(false)
        }

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait..")
        progressDialog.setCancelable(false)
        progressDialog.setMessage("Display Quotes")

        imageClear.setOnClickListener{
            searchAnime.text.clear()
            imageClear.visibility = View.GONE
            rvListQuotes.visibility = View.GONE
            imageEmpty.visibility = View.VISIBLE
        }

        searchAnime.setOnEditorActionListener(TextView.OnEditorActionListener { v, id, keyEvent ->
            strName = searchAnime.text.toString()
            if (strName.isEmpty()) {
                Toast.makeText(this@SearchActivity, "Form cannot empty", Toast.LENGTH_SHORT).show()
            } else {
                if(id == EditorInfo.IME_ACTION_SEARCH) {
                    progressDialog.show()
                    quotesVM.setSearchQuotes(strName,page)
                    val inputMethodManager = v.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(v.windowToken,0)
                    imageClear.visibility = View.VISIBLE
                    return@OnEditorActionListener true
                }
            }
            false
        })

        quotesAdapter = QuotesAdapter(this)
        rvListQuotes.layoutManager = LinearLayoutManager(this)
        rvListQuotes.adapter = quotesAdapter
        rvListQuotes.setHasFixedSize(true)

        //method set vm
        quotesVM = ViewModelProvider(this)[QuotesViewModel::class.java]
        quotesVM.getSearchQuotes().observe(this, Observer<ArrayList<ModelQuotes>>{
            imageEmpty.visibility = View.GONE
            rvListQuotes.visibility = View.VISIBLE
            progressDialog.dismiss()
            if (it.size != 0 ){
                quotesAdapter.setQuotesAdapter(it)
            } else {
                Toast.makeText(this, "Quotes Not Found", Toast.LENGTH_SHORT).show()
            }
        })

        nestedScrollView.setOnScrollChangeListener { v: NestedScrollView, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                page++
                progressDialog.show()
                quotesVM.setSearchQuotes(strName,page)
                quotesAdapter.notifyDataSetChanged()
            }
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}