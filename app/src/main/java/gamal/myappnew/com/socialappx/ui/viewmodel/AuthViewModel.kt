package gamal.myappnew.com.socialappx.ui.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.util.Patterns
import androidx.lifecycle.*
import com.google.firebase.auth.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import gamal.myappnew.com.socialappx.R
import gamal.myappnew.com.socialappx.other.Constants.MAX_USERNAME_LENGTH
import gamal.myappnew.com.socialappx.other.Constants.MIN_PASSWORD_LENGTH
import gamal.myappnew.com.socialappx.other.Constants.MIN_USERNAME_LENGTH
import gamal.myappnew.com.socialappx.other.Event
import gamal.myappnew.com.socialappx.other.Resource
import gamal.myappnew.com.socialappx.repositories.AuthRepository
import gamal.myappnew.com.socialappx.repositories.DefaultAuthRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
 class AuthViewModel @Inject constructor(
     private val repository: AuthRepository,
     private val applicationContext: Context,
     private val dispatcher: CoroutineDispatcher = Dispatchers.Main
     )    :ViewModel(){
   private val _loginStatus = MutableLiveData<Event<Resource<AuthResult>>>()
   val loginStatus: LiveData<Event<Resource<AuthResult>>> = _loginStatus

   fun login(email: String, password: String) {
      if(email.isEmpty() || password.isEmpty()) {
       //  val error = "applicationContext.getString(R.string.error_input_empty)"
         val error = applicationContext.getString(R.string.error_input_empty)
         _loginStatus.postValue(Event(Resource.Error(error)))
      } else {
         _loginStatus.postValue(Event(Resource.Loading()))
         viewModelScope.launch(dispatcher) {
            val result = repository.login(email, password)
            _loginStatus.postValue(Event(result))
         }
      }
   }

   private val _registerStatus=MutableLiveData<Event<Resource<AuthResult>>>()
   val registerStatus:LiveData<Event<Resource<AuthResult>>> =_registerStatus

   fun register(username:String,email:String,password:String,repeatedpassword:String){
      val error=if (username.isEmpty() || email.isEmpty() || password.isEmpty()){
         applicationContext.getString(R.string.error_input_empty)
      } else if (password!=repeatedpassword){
         applicationContext.getString(R.string.error_incorrectly_repeated_password)
      }else if (username.length<MIN_USERNAME_LENGTH){
          applicationContext.getString(R.string.error_username_too_short,MIN_USERNAME_LENGTH)
      } else if (username.length>MAX_USERNAME_LENGTH){
         applicationContext.getString(R.string.error_username_too_long,MAX_USERNAME_LENGTH)
      } else if (password.length < MIN_PASSWORD_LENGTH){
         applicationContext.getString(R.string.error_password_too_short,MIN_PASSWORD_LENGTH)
      }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
         applicationContext.getString(R.string.error_not_a_valid_email)
      } else null
      error?.let {
         _registerStatus.postValue(Event(Resource.Error(it)))
         return
      }

      _registerStatus.postValue(Event(Resource.Loading()))

      viewModelScope.launch (dispatcher){
         val result=repository.register(username, email, password)
         _registerStatus.postValue(Event(result))

      }

   }
}