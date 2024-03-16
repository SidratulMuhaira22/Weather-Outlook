package com.hera.weatheroutlook.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.hera.weatheroutlook.GlideApp
import com.hera.weatheroutlook.R
import com.hera.weatheroutlook.databinding.CardLayoutBinding
import com.hera.weatheroutlook.model.WeatherData
import com.bumptech.glide.request.RequestOptions
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Singleton

@Singleton
class WeatherItemListAdapter :
    ListAdapter<WeatherData, WeatherItemListAdapter.ListViewHolder>(DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            CardLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)
    }

    inner class ListViewHolder(private var binding: CardLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: WeatherData) {
            binding.apply {
                location.text = data.name
                val formatted =
                    SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault()).format(data.dt)
                timestamp.text = itemView.resources.getString(R.string.updated_at, formatted)

                val color =
                    ContextCompat.getColor(itemView.context, R.color.black)
                val circularProgressDrawable = CircularProgressDrawable(itemView.context).apply {
                    setColorSchemeColors(color)
                    strokeWidth = 5f
                    centerRadius = 15f
                    start()
                }

                GlideApp.with(itemView.context)
                    .load(itemView.resources.getString(R.string.icon_html, data.icon))
                    .apply(
                        RequestOptions().dontTransform()
                    )
                    .placeholder(circularProgressDrawable)
                    .into(icon)

                weather.text = data.desc?.replaceFirstChar { it.uppercase() }
                temp.text =
                    itemView.resources.getString(R.string.celcius_degree, data.temp?.toBigDecimal())
                feels.text =
                    itemView.resources.getString(R.string.feels, data.feels?.toBigDecimal())
            }
        }
    }

    companion object {
        private val DIFF_UTIL = object : DiffUtil.ItemCallback<WeatherData>() {
            override fun areItemsTheSame(oldItem: WeatherData, newItem: WeatherData): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: WeatherData,
                newItem: WeatherData
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}