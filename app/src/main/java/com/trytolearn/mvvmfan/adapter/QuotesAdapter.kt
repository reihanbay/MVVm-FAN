package com.trytolearn.mvvmfan.adapter

import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.trytolearn.mvvmfan.R
import com.trytolearn.mvvmfan.model.ModelQuotes
import com.trytolearn.mvvmfan.view.fragment.TranslateFragment
import kotlinx.android.synthetic.main.list_item_quotes.view.*

class QuotesAdapter(val context: Context) : RecyclerView.Adapter<QuotesAdapter.QuotesViewHolder>() {

    private val modelQuotesList = ArrayList<ModelQuotes>()

    fun setQuotesAdapter(items: ArrayList<ModelQuotes>){
        modelQuotesList.clear()
        modelQuotesList.addAll(items)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuotesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_quotes, parent, false)
        return QuotesViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuotesViewHolder, position: Int) {
        val modelQuotes = modelQuotesList[position]

        holder.titleAnime.text = modelQuotes.anime
        holder.tvCharacterAnime.text = "Character : " + modelQuotes.character
        holder.tvQuotes.text = "“" + modelQuotes.quote + "”"

        holder.imageCopy.setOnClickListener { view: View? ->
            val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboardManager.text = modelQuotes.quote
            val snackBar = view?.let { Snackbar.make(it, "Quotes copied to clipboard", Snackbar.LENGTH_INDEFINITE) }
            snackBar?.setActionTextColor(ContextCompat.getColor(context, R.color.purple_700))
            snackBar?.setAction("DISMISS") { v: View? ->
                snackBar?.dismiss()
            }
            snackBar?.show()
        }

        holder.imageTranslate.setOnClickListener {
            val fragment: Fragment = TranslateFragment()
            val bundle = Bundle()
            bundle.putString("Quotes", modelQuotes.quote)
            bundle.putString("CharacterAnime", modelQuotes.character)
            fragment.arguments = bundle
            val fragmentManager = (context as AppCompatActivity).supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frameContainer, fragment)
            fragmentTransaction.commit()
        }
    }

    override fun getItemCount(): Int = modelQuotesList.size
    inner class QuotesViewHolder(item : View) : RecyclerView.ViewHolder(item) {
        var titleAnime : TextView
        var tvCharacterAnime : TextView
        var tvQuotes: TextView
        var imageCopy : ImageView
        var imageTranslate : ImageView

        init {
            titleAnime = itemView.tvTitleAnime
            tvCharacterAnime = itemView.tvCharacterAnime
            tvQuotes = itemView.tvQuotes
            imageCopy = itemView.imageCopy
            imageTranslate = itemView.imageTranslate
        }
    }
}