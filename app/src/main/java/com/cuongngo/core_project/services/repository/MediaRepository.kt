package com.cuongngo.core_project.services.repository

import com.cuongngo.core_project.response.movie_response.GenresMovieResponse
import com.cuongngo.core_project.response.MovieDetailResponse
import com.cuongngo.core_project.response.MovieResponse
import com.cuongngo.core_project.response.MultiMediaResponse
import com.cuongngo.core_project.response.PersonalResponse
import com.cuongngo.core_project.response.movie_response.VideoResponse
import com.cuongngo.core_project.response.tv_response.TvDetailResponse
import com.cuongngo.core_project.services.network.BaseResult
import com.cuongngo.core_project.services.remote.MediaRemoteDataSource
import com.cuongngo.core_project.utils.Constants

class MediaRepository(private val mediaRemoteDataSource: MediaRemoteDataSource) {
    suspend fun getMovieDetail(
        movie_id: String?
    ): BaseResult<MovieDetailResponse> {
        return mediaRemoteDataSource.getMovieDetail(movie_id)
    }

    suspend fun getTvDetail(
        tv_id: String?
    ): BaseResult<TvDetailResponse> {
        return mediaRemoteDataSource.getTvDetail(tv_id)
    }

    suspend fun getUpcoming (): BaseResult<MovieResponse> {
        return mediaRemoteDataSource.getUpcoming()
    }
    suspend fun getGenresMovie(): BaseResult<GenresMovieResponse> {
        return mediaRemoteDataSource.getGenresMovie()
    }
    suspend fun getGenresTV(): BaseResult<GenresMovieResponse> {
        return mediaRemoteDataSource.getGenresTV()
    }

    suspend fun getPopularMovie(
        page: Int?
    ): BaseResult<MovieResponse> {
        return mediaRemoteDataSource.getPopularMovie(page)
    }

    suspend fun searchMedia(
        query: String?,
        page: Int?
    ): BaseResult<MultiMediaResponse> {
        return mediaRemoteDataSource.searchMedia(query, page)
    }

    suspend fun getTrending(
        media_type: String,
        time_window: String,
        page: Int,
    ): BaseResult<MultiMediaResponse> {
        return mediaRemoteDataSource.getTrending(media_type, time_window, page)
    }

    suspend fun getTrendingMovie(
        media_type: String? = Constants.MediaType.MOVIE,
        time_window: String,
        page: Int,
    ): BaseResult<MovieResponse> {
        return mediaRemoteDataSource.getTrendingMovie(media_type, time_window, page)
    }

    suspend fun getPopularPersonal(
        page: Int
    ): BaseResult<PersonalResponse> {
        return mediaRemoteDataSource.getPopularPersonal(page)
    }

    suspend fun getListVideo(
        movie_id: String
    ): BaseResult<VideoResponse> {
        return mediaRemoteDataSource.getListVideo(movie_id)
    }

}