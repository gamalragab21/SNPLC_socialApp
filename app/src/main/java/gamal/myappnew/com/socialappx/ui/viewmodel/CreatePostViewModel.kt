package gamal.myappnew.com.socialappx.ui.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gamal.myappnew.com.socialappx.R
import gamal.myappnew.com.socialappx.other.Event
import gamal.myappnew.com.socialappx.other.Resource
import gamal.myappnew.com.socialappx.repositories.MainRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val repository: MainRepository ,
    private val applicationContext: Context ,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main
) : ViewModel() {

    private val _createPostStatus = MutableLiveData<Event<Resource<Any>>>()
    val createPostStatus: LiveData<Event<Resource<Any>>> = _createPostStatus

    private val _currentImageUri = MutableLiveData<Uri>()
    val currentImageUri: LiveData<Uri> = _currentImageUri

    fun setCurrentImageUri(uri : Uri){
        _currentImageUri.postValue(uri)
    }

    fun createPost(imageUri: Uri, text: String) {
        if(text.isEmpty()) {
            val error = applicationContext.getString(R.string.error_input_empty)
            _createPostStatus.postValue(Event(Resource.Error(error)))
        } else {
            _createPostStatus.postValue(Event(Resource.Loading()))
            viewModelScope.launch(dispatcher) {
                val result = repository.createPost(imageUri, text)
                _createPostStatus.postValue(Event(result))
            }
        }
    }
}