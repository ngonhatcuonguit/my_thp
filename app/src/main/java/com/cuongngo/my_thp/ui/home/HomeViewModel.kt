package com.cuongngo.my_thp.ui.home
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cuongngo.my_thp.R
import com.cuongngo.my_thp.base.viewmodel.BaseViewModel
import com.cuongngo.my_thp.data.database.roomdb.entity.FormEntity
import com.cuongngo.my_thp.data.database.roomdb.entity.FormResponse
import com.cuongngo.my_thp.data.database.roomdb.entity.RequestEntity
import com.cuongngo.my_thp.data.database.roomdb.entity.UserTHPEntity
import com.cuongngo.my_thp.data.database.roomdb.entity.UserTHPResponse
import com.cuongngo.my_thp.response.news.HotNewResponse
import com.cuongngo.my_thp.response.news.News
import com.cuongngo.my_thp.services.network.BaseResult
import com.cuongngo.my_thp.services.repository.FormRepository
import com.cuongngo.my_thp.services.repository.RequestRepository
import com.cuongngo.my_thp.services.repository.UserRepository
import com.cuongngo.my_thp.ui.request_detail.GetRequestStatusResponse
import com.cuongngo.my_thp.ui.request_detail.PushRequestCodeBody
import com.cuongngo.my_thp.ui.request_detail.PushRequestResponse
import com.cuongngo.my_thp.ui.request_detail.RequestBodyPush
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(
    private val userRepository: UserRepository,
    private val formRepository: FormRepository,
    private val requestRepository: RequestRepository
    ) : BaseViewModel() {

    private val _hotNew = MutableLiveData<BaseResult<HotNewResponse>>()
    val hotNew: LiveData<BaseResult<HotNewResponse>> get() = _hotNew

    private val _allForm = MutableLiveData<BaseResult<List<FormEntity>>>()
    val allForm: LiveData<BaseResult<List<FormEntity>>> = _allForm

    private val _formId = MutableLiveData<BaseResult<Long>>()
    val formId: LiveData<BaseResult<Long>> = _formId


    private val _getListUser = MutableLiveData<BaseResult<UserTHPResponse>>()
    val getListUser: LiveData<BaseResult<UserTHPResponse>> get() = _getListUser

    //Local

    private val _getAllUserLocal = MutableLiveData<BaseResult<List<UserTHPEntity>>>()
    val getAllUserLocal: LiveData<BaseResult<List<UserTHPEntity>>>get() = _getAllUserLocal

    private val _insertListUserToLocal = MutableLiveData<BaseResult<Unit>>()
    val insertListUserToLocal: LiveData<BaseResult<Unit>>get() = _insertListUserToLocal

    private val _checkUserTable = MutableLiveData<BaseResult<Int>>()
    val checkUserTable: LiveData<BaseResult<Int>>get() = _checkUserTable

    //-----form remote----
    private val _listFormRemote = MutableLiveData<BaseResult<FormResponse>>()
    val listFormRemote: LiveData<BaseResult<FormResponse>> = _listFormRemote

    private val _listRequestUpLoad = MutableLiveData<BaseResult<List<RequestEntity>>>()
    val listRequestUpLoad: LiveData<BaseResult<List<RequestEntity>>> = _listRequestUpLoad

    private val _allRequest = MutableLiveData<BaseResult<List<RequestEntity>>>()
    val allRequest: LiveData<BaseResult<List<RequestEntity>>> = _allRequest

    private val _upsertListFormToLocal = MutableLiveData<BaseResult<Unit>>()
    val upsertListFormToLocal: LiveData<BaseResult<Unit>> = _upsertListFormToLocal

    private val _checkCountFormRecord = MutableLiveData<BaseResult<Int>>()
    val checkCountFormRecord: LiveData<BaseResult<Int>> = _checkCountFormRecord

    //--------------

    private val _listUploadRequest = MutableLiveData<List<RequestEntity>>()
    val listUploadRequest: LiveData<List<RequestEntity>> = _listUploadRequest

    private val _pushRequest = MutableLiveData<BaseResult<PushRequestResponse>>()
    val pushRequest: LiveData<BaseResult<PushRequestResponse>> get() = _pushRequest

    private val _requestSyncStatus = MutableLiveData<BaseResult<Unit>>()
    val requestSyncStatus: LiveData<BaseResult<Unit>> = _requestSyncStatus

    private val _getRequestStatus = MutableLiveData<BaseResult<GetRequestStatusResponse>>()
    val getRequestStatus: LiveData<BaseResult<GetRequestStatusResponse>> = _getRequestStatus

    private val _updateRequestStatuses = MutableLiveData<BaseResult<Unit>>()
    val updateRequestStatuses: LiveData<BaseResult<Unit>> = _updateRequestStatuses

    fun updateListUploadRequest(requestEntity: RequestEntity, isAdd: Boolean) {
        if(!listUpload.contains(requestEntity) && isAdd){
            listUpload.add(requestEntity)
            this._listUploadRequest.value = listUpload
        }else if(!isAdd){
            listUpload.remove(requestEntity)
            if (listUpload.isNotEmpty()){
                this._listUploadRequest.value = listUpload
            }
        }else{

        }
    }

    var news = News(
        1,
        "",
        "",
        R.drawable.banner_default,
        ""
    )
    var listDefaultHotNews = arrayListOf(
        news.copy(drawableId = R.drawable.banner_default),
        news.copy(drawableId = R.drawable.banner_default_2),
        news.copy(drawableId = R.drawable.banner_default_3),
        news.copy(drawableId = R.drawable.banner_default_4),
    )

    var page: Int = 1
    var keyword: String? = null
    var listUpload: ArrayList<RequestEntity> = arrayListOf()

    init {
//        getHotNew()
//        getAllForm()
//        getListSyncRequest()
    }

    fun insertForm(formEntity: FormEntity) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _formId.postValue(formRepository.insertForm(formEntity))
            }
        }
    }

    fun getHotNew(
    ){
        _hotNew.value = BaseResult.loading(null)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                _hotNew.postValue(userRepository.getHotNew())
            }
        }

    }
    fun getAllForm() {
        _allForm.value = BaseResult.loading(null)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                _allForm.postValue(formRepository.getAllForm())
            }
        }
    }

    fun getListUser(isGetAll: Boolean){
        _getListUser.value = BaseResult.loading(null)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                _getListUser.postValue(
                    userRepository.getListUser(isGetAll)
                )
            }
        }
    }

    fun getListRemoteForm(isGetAll: Boolean) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _listFormRemote.postValue(formRepository.getListForm(isGetAll))
            }
        }
    }

    fun upsertListForm(listForm: List<FormEntity>){
        _upsertListFormToLocal.value = BaseResult.loading(null)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                _upsertListFormToLocal.postValue(formRepository.upsertListForm(listForm))
            }
        }
    }

    fun getCountFormRecord() {
        _checkCountFormRecord.value = BaseResult.loading(null)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                _checkCountFormRecord.postValue(formRepository.getFormCountLocal())
            }
        }
    }

    //Local

    fun getAllUserLocal(){
        _getAllUserLocal.value = BaseResult.loading(null)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                _getAllUserLocal.postValue(userRepository.getAllUserLocal())
            }
        }
    }
    fun searchUsers(keyword: String) {
        _getAllUserLocal.value = BaseResult.loading(null)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _getAllUserLocal.postValue(userRepository.searchUsers(keyword))
            }
        }
    }
    fun getCountUserLocal(){
        _checkUserTable.value = BaseResult.loading(null)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                _checkUserTable.postValue(userRepository.getUserCountLocal())
            }
        }
    }
    fun addListUser(listUser: List<UserTHPEntity>){
        _insertListUserToLocal.value = BaseResult.loading(null)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                _insertListUserToLocal.postValue(userRepository.addListUser(listUser))
            }
        }
    }

    fun getListSyncRequest() {
        viewModelScope.launch {
            _listRequestUpLoad.value = BaseResult.loading(null)
            withContext(Dispatchers.IO) {
                _listRequestUpLoad.postValue(requestRepository.getRequestNeedUpload())
            }
        }
    }
    fun getAllRequest() {
        _allRequest.value = BaseResult.loading(null)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _allRequest.postValue(requestRepository.getAllRequest())
            }
        }
    }


    fun pushRequest(
        requestBodyPush : List<RequestBodyPush>
    ) {
        _pushRequest.value = BaseResult.loading(null)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _pushRequest.postValue(
                    requestRepository.pushRequest(
                        requestBodyPush = requestBodyPush
                    )
                )
            }
        }
    }
    fun getRequestStatus(
            pushRequestCodeBody : PushRequestCodeBody
        ) {
            _getRequestStatus.value = BaseResult.loading(null)
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    _getRequestStatus.postValue(
                        requestRepository.getRequestStatus(
                            pushRequestCodeBody
                        )
                    )
                }
            }
    }

    fun updateRequestStatuses(
            data: List<Pair<String, Int>>
        ) {
            _updateRequestStatuses.value = BaseResult.loading(null)
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    _updateRequestStatuses.postValue(
                        requestRepository.updateRequestStatuses(data)
                    )
                }
            }
    }

    fun updateSyncStatus(requestCode: String, status: Int, is_sync: Boolean) {
        _requestSyncStatus.value = BaseResult.loading(null)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _requestSyncStatus.postValue(
                    requestRepository.updateSyncStatus(
                        requestCode = requestCode,
                        is_sync = is_sync,
                        currentTime = getCurrentTimestamp(),
                        status = status
                    )
                )
            }
        }
    }



}