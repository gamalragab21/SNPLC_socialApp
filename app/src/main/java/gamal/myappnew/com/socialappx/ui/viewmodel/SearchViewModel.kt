package gamal.myappnew.com.socialappx.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gamal.myappnew.com.socialappx.data.entities.User
import gamal.myappnew.com.socialappx.other.Event
import gamal.myappnew.com.socialappx.other.Resource
import gamal.myappnew.com.socialappx.repositories.MainRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: MainRepository ,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main
): ViewModel() {

    private val _searchResults = MutableLiveData<Event<Resource<List<User>>>>()
    val searchResults: LiveData<Event<Resource<List<User>>>> = _searchResults

    fun searchUser(query: String) {
        if(query.isEmpty()) return

        _searchResults.postValue(Event(Resource.Loading()))
        viewModelScope.launch(dispatcher) {
            val result = repository.searchUser(query)

            _searchResults.postValue(Event(result))
        }
    }
}