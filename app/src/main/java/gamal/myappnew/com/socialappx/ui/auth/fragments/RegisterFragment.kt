package gamal.myappnew.com.socialappx.ui.auth.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import gamal.myappnew.com.socialappx.R
import gamal.myappnew.com.socialappx.app.SocialMediaApplication_HiltComponents
import gamal.myappnew.com.socialappx.other.Event
import gamal.myappnew.com.socialappx.other.EventObserver
import gamal.myappnew.com.socialappx.other.Resource
import gamal.myappnew.com.socialappx.ui.auth.snackbar
import gamal.myappnew.com.socialappx.ui.viewmodel.AuthViewModel
import kotlinx.android.synthetic.main.fragment_register.*
import javax.inject.Inject


@AndroidEntryPoint
class RegisterFragment:Fragment(R.layout.fragment_register) {


     lateinit var viewModel: AuthViewModel
    override fun onViewCreated(view : View , savedInstanceState : Bundle?) {
        super.onViewCreated(view , savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)
        subscribeToObservers()

        tvLogin.setOnClickListener {
            if (findNavController().previousBackStackEntry!=null){
                findNavController().popBackStack()
            }else findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
        }
        btnRegister.setOnClickListener {
            viewModel.register(
                etUsername.text.toString(),
                etEmail.text.toString(),
                etPassword.text.toString(),
                etRepeatPassword.text.toString()
            )
        }


    }

    private fun subscribeToObservers() {
        viewModel.registerStatus.observe(viewLifecycleOwner, EventObserver(
            onError = {
                registerProgressBar.isVisible = false
                snackbar(it)
            } ,
            onLoading = { registerProgressBar.isVisible = true } ,


            ) {
            registerProgressBar.isVisible = false
            snackbar(getString(R.string.success_registration))
        })





    }
}