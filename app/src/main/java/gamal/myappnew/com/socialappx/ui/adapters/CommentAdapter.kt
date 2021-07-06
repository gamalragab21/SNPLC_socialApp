package gamal.myappnew.com.socialappx.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.google.firebase.auth.FirebaseAuth
import gamal.myappnew.com.socialappx.R
import gamal.myappnew.com.socialappx.data.entities.Comment
import gamal.myappnew.com.socialappx.data.entities.User
import kotlinx.android.synthetic.main.item_comment.view.*
import kotlinx.android.synthetic.main.item_post.view.*
import kotlinx.android.synthetic.main.item_user.view.*
import javax.inject.Inject

class CommentAdapter @Inject constructor(
    private val glide : RequestManager
) : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {


    var comments : List<Comment>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    private val diffCallback = object : DiffUtil.ItemCallback<Comment>() {
        override fun areContentsTheSame(oldItem : Comment , newItem : Comment) : Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areItemsTheSame(oldItem : Comment , newItem : Comment) : Boolean {
            return oldItem.commentId== newItem.commentId
        }
    }
    private val differ = AsyncListDiffer(this , diffCallback)

    class CommentViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val tvCommentUsername: TextView = itemView.tvCommentUsername
        val tvComment: TextView = itemView.tvComment
        val ibDeleteComment: ImageButton = itemView.ibDeleteComment
        val ivCommentUserProfilePicture: ImageView = itemView.ivCommentUserProfilePicture
    }

    override fun onCreateViewHolder(parent : ViewGroup , viewType : Int) : CommentViewHolder {
        return CommentViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_comment ,
                parent ,
                false
            )
        )
    }

    override fun getItemCount() : Int {
        return comments.size
    }


    override fun onBindViewHolder(holder : CommentViewHolder , position : Int) {
        val comment = comments[position]
        holder.apply {
            ibDeleteComment.isVisible = comment.uid == FirebaseAuth.getInstance().uid!!

            glide.load(comment.profilePictureUri).into(ivCommentUserProfilePicture)
            tvCommentUsername.text = comment.username
            tvComment.text = comment.comment

            ibDeleteComment.setOnClickListener {
                onDeleteClickListener?.let { click ->
                    click(comment)
                }
            }
            tvCommentUsername.setOnClickListener {
                onUserClickListener?.let { click ->
                    click(comment)
                }
            }
            }


        }


    private var onDeleteClickListener: ((Comment) -> Unit)? = null

    fun setOnDeleteClickListener(listener: (Comment) -> Unit) {
        onDeleteClickListener = listener
    }

    private var onUserClickListener: ((Comment) -> Unit)? = null

    fun setOnUserClickListener(listener: (Comment) -> Unit) {
        onUserClickListener = listener
    }

}
