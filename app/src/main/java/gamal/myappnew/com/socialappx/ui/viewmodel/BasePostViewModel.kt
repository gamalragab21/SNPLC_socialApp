package gamal.myappnew.com.socialappx.ui.viewmodel

import android.util.EventLog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gamal.myappnew.com.socialappx.data.entities.Post
import gamal.myappnew.com.socialappx.data.entities.User
import gamal.myappnew.com.socialappx.other.Event
import gamal.myappnew.com.socialappx.other.Resource
import gamal.myappnew.com.socialappx.repositories.MainRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class BasePostViewModel(
    private val repository : MainRepository ,
    private val dispatcher:CoroutineDispatcher=Dispatchers.Main
) :ViewModel() {

    private val _likePostStatus=MutableLiveData<Event<Resource<Boolean>>>()
    val likePostStatus:LiveData<Event<Resource<Boolean>>> =_likePostStatus


    private val _deletePostStatus=MutableLiveData<Event<Resource<Post>>>()
    val deletePostStatus:LiveData<Event<Resource<Post>>> =_deletePostStatus


    private val _likedByUsers=MutableLiveData<Event<Resource<List<User>>>>()
    val likedByUsers:LiveData<Event<Resource<List<User>>>> =_likedByUsers


    abstract val posts:LiveData<Event<Resource<List<Post>>>>

    abstract fun getPost(uid:String="")

    fun getUsers(uids:List<String>) {
     if (uids.isEmpty()) return
        _likedByUsers.postValue(Event(Resource.Loading()))

        viewModelScope.launch(dispatcher) {
            val result=repository.getUsers(uids)
            _likedByUsers.postValue(Event(result))
        }

    }
    fun deletePost(post : Post) {

        _deletePostStatus.postValue(Event(Resource.Loading()))

        viewModelScope.launch(dispatcher) {
            val result=repository.deletingPost(post)
            _deletePostStatus.postValue(Event(result))
        }

    }
    fun toggleForPost(post : Post) {

        _likePostStatus.postValue(Event(Resource.Loading()))

        viewModelScope.launch(dispatcher) {
            val result=repository.toggleLikeForPost(post)
            _likePostStatus.postValue(Event(result))
        }

    }
}