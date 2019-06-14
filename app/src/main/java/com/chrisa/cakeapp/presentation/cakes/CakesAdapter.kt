package com.chrisa.cakeapp.presentation.cakes

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chrisa.cakeapp.R
import com.chrisa.cakeapp.data.api.cakes.Cake
import com.chrisa.cakeapp.presentation.utils.load
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable

class CakesAdapter(
    context: Context,
    diffHelper: DiffUtil.ItemCallback<Cake>
) : ListAdapter<Cake, CakeViewHolder>(diffHelper) {

    private val _clickSubject = PublishRelay.create<Cake>()

    val cakeClicked: Observable<Cake>
        get() = _clickSubject

    private val inflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CakeViewHolder {
        val view = inflater.inflate(R.layout.cake_item, parent, false)
        return CakeViewHolder(view, _clickSubject)
    }

    override fun onBindViewHolder(viewHolder: CakeViewHolder, position: Int) {
        viewHolder.bind(getItem(position))
    }
}

class CakeViewHolder(itemView: View, clickSubject: PublishRelay<Cake>) : RecyclerView.ViewHolder(itemView) {

    private val image: ImageView = itemView.findViewById(R.id.cake_image)
    private val name: TextView = itemView.findViewById(R.id.cake_name)
    private lateinit var entry: Cake

    init {
        itemView.setOnClickListener { clickSubject.accept(entry) }
    }

    fun bind(entry: Cake) {
        this.entry = entry
        this.name.text = entry.title
        this.image.load(entry.imageUrl)
    }
}

class CakeDiffItemCallback : DiffUtil.ItemCallback<Cake>() {
    override fun areItemsTheSame(oldItem: Cake, newItem: Cake): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Cake, newItem: Cake): Boolean {
        return oldItem == newItem
    }
}
