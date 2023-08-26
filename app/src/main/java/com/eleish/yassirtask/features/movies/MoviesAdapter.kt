package com.eleish.yassirtask.features.movies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.eleish.entities.Movie
import com.eleish.yassirtask.databinding.ItemMovieBinding

class MoviesAdapter(private val onItemClicked: (Movie) -> Unit) :
    ListAdapter<Movie, MoviesAdapter.ViewHolder>(DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false).run {
            ViewHolder(this)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = getItem(position)

        with(holder.binding) {
            movieNameTv.text = movie.name
            releaseYearTv.text = movie.releaseYear.toString()
            ratingRb.rating = movie.rating
            moviePosterIv.load(data = movie.posterUrl)

            root.setOnClickListener {
                onItemClicked.invoke(movie)
            }
        }
    }

    class ViewHolder(val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        private val DIFF_UTIL = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem == newItem
            }
        }
    }
}