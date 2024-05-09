package com.cuongngo.core_project.ui.media.list_move

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cuongngo.core_project.App
import com.cuongngo.core_project.R
import com.cuongngo.core_project.databinding.ItemHorizontalMediaBinding
import com.cuongngo.core_project.response.MultiMedia
import com.cuongngo.core_project.response.MultiMediaResponse
import com.cuongngo.core_project.utils.Constants

class MovieHorizontalAdapter(
    listMedia: ArrayList<MultiMedia>,
    private val onItemClick: ((MultiMedia) -> Unit)? = null
) : RecyclerView.Adapter<MovieHorizontalAdapter.MovieHorizontalViewHolder>() {

    private val listMedia = listMedia

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHorizontalViewHolder {
        return MovieHorizontalViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_horizontal_media,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MovieHorizontalViewHolder, position: Int) {
        val binding = holder.mediaHorizontalMovieBinding
        var media: MultiMedia? = if (listMedia[position].media_type == Constants.MediaType.TV) {
            listMedia[position].known_for?.firstOrNull()
        } else listMedia[position]

        binding.multiMedia = media

        binding.root.setOnClickListener {
            onItemClick?.invoke(media ?: return@setOnClickListener)
        }
        val genreID = media?.genre_ids?.firstOrNull()
        App.getGenres().genres?.forEach {
            if (it.id == genreID) {
                binding.tvGenre.text = it.name.toString()
            }
        }
    }

    override fun getItemCount(): Int {
        return listMedia.size
    }

    fun submitListMovie(mediaResponse: MultiMediaResponse?) {
        if (mediaResponse?.page == 1) {
            this.listMedia.clear()
        }
        this.listMedia.addAll(mediaResponse?.results.orEmpty())
        notifyDataSetChanged()
    }

    class MovieHorizontalViewHolder(
        val mediaHorizontalMovieBinding: ItemHorizontalMediaBinding
    ) : RecyclerView.ViewHolder(mediaHorizontalMovieBinding.root)

}