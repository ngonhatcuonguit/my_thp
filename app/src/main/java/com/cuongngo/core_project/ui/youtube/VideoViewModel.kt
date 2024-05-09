package com.cuongngo.core_project.ui.youtube

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cuongngo.core_project.base.viewmodel.BaseViewModel
import com.cuongngo.core_project.response.movie_response.VideoResponse
import com.cuongngo.core_project.services.network.BaseResult
import com.cuongngo.core_project.services.repository.MediaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VideoViewModel(private val mediaRepository: MediaRepository) : BaseViewModel() {

    private val _listVideo = MutableLiveData<BaseResult<VideoResponse>>()
    val listVideo: LiveData<BaseResult<VideoResponse>> get() = _listVideo

    fun getListVideo(movie_id: String){
        _listVideo.value = BaseResult.loading(null)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                _listVideo.postValue(
                    mediaRepository.getListVideo(movie_id)
                )
            }
        }
    }

}