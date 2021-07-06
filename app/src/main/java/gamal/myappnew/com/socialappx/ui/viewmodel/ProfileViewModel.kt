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
class ProfileViewModel @Inject constructor(
   private val repository : MainRepository,
    private val dispatcher : CoroutineDispatcher=Dispatchers.Main
) :BasePostViewModel(repository,dispatcher){

    private val _posts= MutableLiveData<Event<Resource<List<Post>>>>()
    override val posts : LiveData<Event<Resource<List<Post>>>>
        get() = _posts

    private val _profileMeta=  MutableLiveData<Event<Resource<User>>>()
     val profileMeta:LiveData<Event<Resource<User>>> =_profileMeta

    private val _followsStatus=  MutableLiveData<Event<Resource<Boolean>>>()
     val followsStatus:LiveData<Event<Resource<Boolean>>> =_followsStatus

    override fun getPost(uid : String) {
        _posts.postValue(Event(Resource.Loading()))
        viewModelScope.launch(dispatcher) {
            val result=repository.getPostsForProfile(uid)
            _posts.postValue(Event(result))
        }
    }

    fun toggleFollowForUser(uid : String){
        _followsStatus.postValue(Event((Resource.Loading())))
        viewModelScope.launch(dispatcher) {
            val result=repository.toggleFollowForUser(uid)
            _followsStatus.postValue(Event(result))
        }
    }

    fun loadingProfile(uid : String){
        _profileMeta.postValue(Event((Resource.Loading())))
        viewModelScope.launch(dispatcher) {
            val result=repository.getUser(uid)
            _profileMeta.postValue(Event(result))
        }
        getPost(uid)
    }


}