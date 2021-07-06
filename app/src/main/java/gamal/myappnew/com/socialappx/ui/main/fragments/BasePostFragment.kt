package gamal.myappnew.com.socialappx.ui.main.fragments


import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestManager
import com.google.firebase.auth.FirebaseAuth
import gamal.myappnew.com.socialappx.R
import gamal.myappnew.com.socialappx.other.EventObserver
import gamal.myappnew.com.socialappx.ui.adapters.PostAdapter
import gamal.myappnew.com.socialappx.ui.adapters.UserAdapter
import gamal.myappnew.com.socialappx.ui.auth.snackbar
import gamal.myappnew.com.socialappx.ui.dialogs.CommentDialogDirections
import gamal.myappnew.com.socialappx.ui.dialogs.DeletePostDialog
import gamal.myappnew.com.socialappx.ui.dialogs.LikedByDialog
import gamal.myappnew.com.socialappx.ui.viewmodel.BasePostViewModel
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

abstract class BasePostFragment(
    layoutId: Int
) : Fragment(layoutId) {

    @Inject
    lateinit var glide: RequestManager

    @Inject
    lateinit var postAdapter: PostAdapter


    protected abstract val postProgressBar: ProgressBar

    protected abstract val basePostViewModel: BasePostViewModel
    protected  var curLikedIndex: Int?=null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToObservers()

        // like post
        postAdapter.setOnLikeClickListener { post, i ->
            curLikedIndex=i
            post.isLiked = !post.isLiked
            basePostViewModel.toggleForPost(post)
        }

        // delete post
        postAdapter.setOnDeletePostClickListener { post ->
            DeletePostDialog().apply {

                setPositiveListener {
                    basePostViewModel.deletePost(post)
                }
            }.show(childFragmentManager, null)
        }

        // liked by post
        postAdapter.setOnLikedByClickListener {post->
            basePostViewModel.getUsers(post.likedBy)
        }

        // user name clicked and image
        postAdapter.setOnUserClickListener { uid->
            if(FirebaseAuth.getInstance().uid!! == uid) {
                requireActivity().bottomNavigationView.selectedItemId = R.id.profileFragment
                return@setOnUserClickListener
            }
            findNavController().navigate(
                OtherProfileFragmentDirections.globalActionOtherFragmentProfile(uid)
            )
        }

        // show comments
        postAdapter.setOnCommentsClickListener {post->
//            findNavController().navigate(
//                R.id.globalActionCommentDialog,
//                Bundle().apply{
//                   putString("postId",post.id)
//                }
//            )

            findNavController().navigate(
                CommentDialogDirections.globalActionCommentDialog(post.id)
            )
        }



    }

    private fun subscribeToObservers() {
        basePostViewModel.likePostStatus.observe(viewLifecycleOwner, EventObserver(
            onError = {
                curLikedIndex?.let { index ->
                    postAdapter.posts[index].isLiking = false
                    postAdapter.notifyItemChanged(index)
                }
                snackbar(it)
            },
            onLoading = {
                curLikedIndex?.let { index ->
                    postAdapter.posts[index].isLiking = true
                    postAdapter.notifyItemChanged(index)
                }
            }
        ) { isLiked ->
            curLikedIndex?.let { index ->
                val uid = FirebaseAuth.getInstance().uid!!
                postAdapter.posts[index].apply {
                    this.isLiked = isLiked
                    isLiking=false
                    if(isLiked) {
                        likedBy += uid
                    } else {
                        likedBy -= uid
                    }
                }
                postAdapter.notifyItemChanged(index)
            }
        })

        basePostViewModel.deletePostStatus.observe(viewLifecycleOwner, EventObserver(
            onError = { snackbar(it) }
        ) { deletedPost ->
            postAdapter.posts -= deletedPost
        })

        basePostViewModel.posts.observe(viewLifecycleOwner, EventObserver(
            onError = {
                postProgressBar.isVisible = false
                snackbar(it)
            },
            onLoading = {
                postProgressBar.isVisible = true
            }
        ) { posts ->
            postProgressBar.isVisible = false
            postAdapter.posts = posts
        })

        basePostViewModel.likedByUsers.observe(viewLifecycleOwner, EventObserver(
            onError = {
                snackbar(it)
            },
            onLoading = {
            }
        ) { users ->
            val userAdapter=UserAdapter(glide)
            userAdapter.users=users
            LikedByDialog(userAdapter)
                .show(childFragmentManager,null)
        })
    }
}