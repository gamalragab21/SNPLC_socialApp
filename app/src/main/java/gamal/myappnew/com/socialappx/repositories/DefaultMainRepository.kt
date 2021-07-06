package gamal.myappnew.com.socialappx.repositories

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.scopes.ViewModelScoped
import gamal.myappnew.com.socialappx.data.entities.Post
import gamal.myappnew.com.socialappx.data.entities.User
import gamal.myappnew.com.socialappx.other.Constants.COMMENTS
import gamal.myappnew.com.socialappx.other.Constants.POSTS
import gamal.myappnew.com.socialappx.other.Constants.USERS
import gamal.myappnew.com.socialappx.other.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Singleton
import kotlin.concurrent.thread

@ViewModelScoped
class DefaultMainRepository : MainRepository {
    private val auth = FirebaseAuth.getInstance()
    private val fireStore = FirebaseFirestore.getInstance()
    private val storage = Firebase.storage
    private val users = fireStore.collection(USERS)
    private val posts = fireStore.collection(POSTS)
    private val comments = fireStore.collection(COMMENTS)
    override suspend fun createPost(imageUri : Uri , text : String) : Resource<Any> =
        withContext(Dispatchers.IO) {
            safeCall {
                val uid = auth.currentUser !!.uid
                val postId = UUID.randomUUID().toString()
                val imageUploadResult = storage.getReference(postId).putFile(imageUri).await()
                val imageURL =
                    imageUploadResult?.metadata?.reference?.downloadUrl?.await().toString()

                val post = Post(
                    id = postId ,
                    authorUid = uid ,
                    text = text ,
                    imageUrl = imageURL ,
                    date = System.currentTimeMillis()
                )
                posts.document(postId).set(post).await()

                Resource.Success(Any())
            }
        }

    override suspend fun getUsers(uids : List<String>) : Resource<List<User>> =
        withContext(Dispatchers.IO) {
            safeCall {
                val userList = users.whereIn("uid" , uids).orderBy("username").get().await()
                    .toObjects(User::class.java)
                Resource.Success(userList)
            }
        }

    override suspend fun getUser(uid : String) : Resource<User> = withContext(Dispatchers.IO) {
        safeCall {
            val user = users.document(uid).get().await().toObject(User::class.java)
                ?: throw  IllegalStateException()
            val currentUid = FirebaseAuth.getInstance().uid !!
            val currentUser = users.document(currentUid).get().await().toObject(User::class.java)
                ?: throw  IllegalStateException()
            user.isFollowing = uid in currentUser.follows
            Resource.Success(user)

        }

    }

    override suspend fun getPostsForFollows() : Resource<List<Post>> = withContext(Dispatchers.IO) {
        safeCall {
            val uid = FirebaseAuth.getInstance().uid !!
            val follows = getUser(uid).data !!.follows
            val allposts = posts.whereIn("authorUid" , follows)
                .orderBy("date" , com.google.firebase.firestore.Query.Direction.DESCENDING)
                .get().await().toObjects(Post::class.java)
                .onEach { post ->
                    val user = getUser(post.authorUid).data !!
                    post.authorProfilePictureUrl = user.profilePictureUrl
                    post.authorUsername = user.username
                    post.isLiked = uid in post.likedBy
                }

            Resource.Success(allposts)
        }
    }

    override suspend fun toggleLikeForPost(post : Post) : Resource<Boolean> = withContext(Dispatchers.IO) {
            safeCall {
                var isLiked = false

                fireStore.runTransaction { transction ->
                    val uid = FirebaseAuth.getInstance().uid !!
                    val postResult = transction.get(posts.document(post.id))
                    val currentLikes = postResult.toObject(Post::class.java)?.likedBy ?: listOf()
                    transction.update(
                        posts.document(post.id) ,
                        "likedBy" ,
                        if (uid in currentLikes) currentLikes - uid else {
                            isLiked = true
                            currentLikes + uid
                        }
                    )
                }.await()
                Resource.Success(isLiked)
            }
        }

    override suspend fun deletingPost(post : Post) : Resource<Post> = withContext(Dispatchers.IO) {
        safeCall {
            posts.document(post.id).delete().await()
            storage.getReferenceFromUrl(post.imageUrl).delete().await()
            Resource.Success(post)
        }

    }

    override suspend fun getPostsForProfile(uid : String) : Resource<List<Post>> = withContext(Dispatchers.IO){
        safeCall {
            val allpostsProfile = posts.whereEqualTo("authorUid",uid)
                .orderBy("date" , com.google.firebase.firestore.Query.Direction.DESCENDING)
                .get().await().toObjects(Post::class.java)
                .onEach { post ->
                    val user = getUser(post.authorUid).data !!
                    post.authorProfilePictureUrl = user.profilePictureUrl
                    post.authorUsername = user.username
                    post.isLiked = uid in post.likedBy
                }

            Resource.Success(allpostsProfile)
        }
    }

    override suspend fun toggleFollowForUser(uid : String) : Resource<Boolean> = withContext(Dispatchers.IO){
        safeCall {
            var isFollowing = false
            fireStore.runTransaction { transaction ->
                val currentUid = auth.uid!!
                val currentUser = transaction.get(users.document(currentUid)).toObject(User::class.java)!!
                isFollowing = uid in currentUser.follows
                val newFollows = if(isFollowing) currentUser.follows - uid else currentUser.follows + uid
                transaction.update(users.document(currentUid), "follows", newFollows)
            }.await()
            Resource.Success(!isFollowing)
        }
    }

    override suspend fun searchUser(query : String) : Resource<List<User>> = withContext(Dispatchers.IO){
        safeCall {
            val userResult=users.whereGreaterThanOrEqualTo("username", query.uppercase(Locale.ROOT))
                .get().await().toObjects(User::class.java)
            Resource.Success(userResult)
        }
    }

}