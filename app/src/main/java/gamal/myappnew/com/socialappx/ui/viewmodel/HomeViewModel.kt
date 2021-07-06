package gamal.myappnew.com.socialappx.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gamal.myappnew.com.socialappx.data.entities.Post
import gamal.myappnew.com.socialappx.data.entities.User
import gamal.myappnew.com.socialappx.other.Event
import gamal.myappnew.com.socialappx.other.Resource
import gamal.myappnew.com.socialappx.repositories.MainRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository : MainRepository ,
    private val dispatcher : CoroutineDispatcher =Dispatchers.Main ,
):BasePostViewModel(repository,dispatcher) {
    private val _posts=MutableLiveData<Event<Resource<List<Post>>>>()
    override val posts : LiveData<Event<Resource<List<Post>>>>
        get() = _posts

      override fun getPost(uid : String) {
        _posts.postValue(Event(Resource.Loading()))
        viewModelScope.launch(dispatcher) {
            val result=repository.getPostsForFollows()
            _posts.postValue(Event(result))
        }
    }

    init {
        getPost()
    }
}