package gamal.myappnew.com.socialappx.repositories

import com.google.firebase.auth.AuthResult
import gamal.myappnew.com.socialappx.other.Resource
import javax.inject.Inject


interface AuthRepository {
    suspend fun register(username:String,email:String,password:String):Resource<AuthResult>
    suspend fun login(email:String,password:String):Resource<AuthResult>
}