package gamal.myappnew.com.socialappx.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import gamal.myappnew.com.socialappx.data.entities.Comment
import gamal.myappnew.com.socialappx.other.Event
import gamal.myappnew.com.socialappx.other.Resource
import gamal.myappnew.com.socialappx.repositories.MainRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommentViewModel @Inject constructor(
    private val repository : MainRepository ,
    private val dispatcher : CoroutineDispatcher = Dispatchers.Main
) : ViewModel() {
    private val _createCommentStatus = MutableLiveData<Event<Resource<Comment>>>()
     val createCommentStatus : LiveData<Event<Resource<Comment>>> = _createCommentStatus

    private val _deleteCommentStatus = MutableLiveData<Event<Resource<Comment>>>()
     val deleteCommentStatus : LiveData<Event<Resource<Comment>>> = _deleteCommentStatus

    private val _commentsForPost = MutableLiveData<Event<Resource<List<Comment>>>>()
     val commentForPost : LiveData<Event<Resource<List<Comment>>>> = _commentsForPost

    fun createComment(commentText:String,postId:String){
        if(commentText.isEmpty()) return
        _createCommentStatus.postValue(Event(Resource.Loading()))

        viewModelScope.launch (dispatcher){
            val result=repository.createComment(commentText,postId)
            _createCommentStatus.postValue(Event(result))
        }
    }

    fun deleteComment(comment:Comment){
        _deleteCommentStatus.postValue(Event(Resource.Loading()))

        viewModelScope.launch (dispatcher){
            val result=repository.deleteComment(comment)
            _deleteCommentStatus.postValue(Event(result))
        }
    }

    fun getCommentsForPost(postId:String){
        _commentsForPost.postValue(Event(Resource.Loading()))
        viewModelScope.launch (dispatcher){
            val result=repository.getCommentForPost(postId)
            _commentsForPost.postValue(Event(result))
        }
    }


}