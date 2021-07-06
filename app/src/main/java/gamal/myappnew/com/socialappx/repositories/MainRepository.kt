package gamal.myappnew.com.socialappx.repositories

import android.net.Uri
import gamal.myappnew.com.socialappx.data.entities.Comment
import gamal.myappnew.com.socialappx.data.entities.Post
import gamal.myappnew.com.socialappx.data.entities.User
import gamal.myappnew.com.socialappx.other.Resource

interface MainRepository {
    suspend fun createPost(imageUri:Uri,text:String): Resource<Any>
    suspend fun getUsers(uids:List<String>):Resource<List<User>>
    suspend fun getUser(uid:String):Resource<User>
    suspend fun getPostsForFollows():Resource<List<Post>>
    suspend fun toggleLikeForPost(post : Post):Resource<Boolean>
    suspend fun deletingPost(post : Post):Resource<Post>
    suspend fun getPostsForProfile(uid:String=""):Resource<List<Post>>
    suspend fun toggleFollowForUser(uid:String):Resource<Boolean>
    suspend fun searchUser(query:String):Resource<List<User>>
    suspend fun createComment(commentText:String,postId:String):Resource<Comment>
    suspend fun deleteComment(comment:Comment):Resource<Comment>
    suspend fun getCommentForPost(postId:String):Resource<List<Comment>>
}