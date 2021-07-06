package gamal.myappnew.com.socialappx.ui.main.fragments

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestManager
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import dagger.hilt.android.AndroidEntryPoint
import gamal.myappnew.com.socialappx.R
import gamal.myappnew.com.socialappx.other.EventObserver
import gamal.myappnew.com.socialappx.ui.auth.snackbar
import gamal.myappnew.com.socialappx.ui.viewmodel.CreatePostViewModel
import gamal.myappnew.com.socialappx.utils.slideUpViews
import kotlinx.android.synthetic.main.fragment_create_post.*
import kotlinx.android.synthetic.main.fragment_login.*
import javax.inject.Inject


@AndroidEntryPoint
class CreatePostFragment : Fragment(R.layout.fragment_create_post) {

    private var currentImageUri:Uri?=null
   private lateinit var  permissionlistener : PermissionListener
    @Inject
    lateinit var glide:RequestManager

    private val createPostViewModel: CreatePostViewModel by viewModels()

    private val cropactivityResultContract = object : ActivityResultContract<String , Uri?>() {
        override fun createIntent(context : Context , input : String?) : Intent {
            return CropImage.activity().setAspectRatio(16 , 9)
                .setGuidelines(CropImageView.Guidelines.ON)
                .getIntent(requireContext())
        }

        override fun parseResult(resultCode : Int , intent : Intent?) : Uri? {
           return CropImage.getActivityResult(intent)?.uri
        }

    }

    private lateinit var cropContent:ActivityResultLauncher<String>


    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)

        cropContent=registerForActivityResult(cropactivityResultContract){
            it?.let {
                createPostViewModel.setCurrentImageUri(it)
            }
        }
    }

    override fun onViewCreated(view : View , savedInstanceState : Bundle?) {
        super.onViewCreated(view , savedInstanceState)


        subscribeToObservers()
        btnPost.setOnClickListener {
            currentImageUri?.let { uri ->
                createPostViewModel.createPost(uri,etPostDescription.text.toString())
            }?: snackbar(getString(R.string.error_no_image_chosen))
        }

        btnSetPostImage.setOnClickListener {
           openGallery()

        }
        ivPostImage.setOnClickListener {
            openGallery()
        }

        slideUpViews(requireContext(), ivPostImage, btnSetPostImage, tilPostText, btnPost)


    }

    private fun openGallery() {
         permissionlistener  = object : PermissionListener {
            override fun onPermissionGranted() {
                cropContent.launch("image/*")
            }

            override fun onPermissionDenied(deniedPermissions : List<String>) {
               snackbar(getString(R.string.permission_gallery_denied))
            }
        }
        TedPermission.with(requireContext())
            .setPermissionListener(permissionlistener)
            .setDeniedMessage(getString(R.string.message_gallery_permission_denied))
            .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, )
            .check();
    }

    private fun subscribeToObservers() {
        createPostViewModel.currentImageUri.observe(viewLifecycleOwner){
            currentImageUri=it
            btnSetPostImage.isVisible=false
            glide.load(currentImageUri).into(ivPostImage)
        }

        createPostViewModel.createPostStatus.observe(viewLifecycleOwner , EventObserver(
            onError = {
                createPostProgressBar.isVisible = false
                snackbar(it)
            } ,
            onLoading = { createPostProgressBar.isVisible = true }
        ) {
            createPostProgressBar.isVisible = false
            snackbar(requireActivity().getString(R.string.success_posts))
            findNavController().popBackStack()
        })
    }

}