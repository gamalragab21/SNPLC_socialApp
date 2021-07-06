package gamal.myappnew.com.socialappx.repositories

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.scopes.ViewModelScoped
import gamal.myappnew.com.socialappx.data.entities.User
import gamal.myappnew.com.socialappx.other.Constants.USERS
import gamal.myappnew.com.socialappx.other.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@ViewModelScoped
class DefaultAuthRepository  : AuthRepository  {
   private val auth = FirebaseAuth.getInstance()
   private val users = FirebaseFirestore.getInstance().collection(USERS)
    override suspend fun register(
        username : String ,
        email : String ,
        password : String
    ) : Resource<AuthResult> {
        return withContext(Dispatchers.IO) {
            safeCall {
                val result = auth.createUserWithEmailAndPassword(email , password).await()
                val uid = result.user?.uid !!
                val user = User(uid = uid , username = username)
                users.document(uid).set(user).await()
                Resource.Success(result)
            }
        }
    }

    override suspend fun login(email : String , password : String) : Resource<AuthResult> {
        return withContext(Dispatchers.IO) {
            safeCall {
                val result = auth.signInWithEmailAndPassword(email, password).await()
                Resource.Success(result)
            }
        }
    }
}