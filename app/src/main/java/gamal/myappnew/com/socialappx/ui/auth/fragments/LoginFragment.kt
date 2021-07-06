package gamal.myappnew.com.socialappx.ui.auth.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import gamal.myappnew.com.socialappx.R
import gamal.myappnew.com.socialappx.app.SocialMediaApplication_HiltComponents
import gamal.myappnew.com.socialappx.other.EventObserver
import gamal.myappnew.com.socialappx.ui.auth.snackbar
import gamal.myappnew.com.socialappx.ui.main.MainActivity
import gamal.myappnew.com.socialappx.ui.viewmodel.AuthViewModel
import kotlinx.android.synthetic.main.fragment_login.*
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment: Fragment(R.layout.fragment_login) {

     private lateinit var  viewModel: AuthViewModel


    override fun onViewCreated(view : View , savedInstanceState : Bundle?) {
        super.onViewCreated(view , savedInstanceState)

       viewModel = ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)
        subscribeToObservers()

        btnLogin.setOnClickListener {
            viewModel.login(
                etEmail.text.toString(),
                etPassword.text.toString()
            )
        }
      tvRegisterNewAccount.setOnClickListener {
          if (findNavController().previousBackStackEntry!=null){
              findNavController().popBackStack()
          }else findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
      }
   }

    private fun subscribeToObservers() {
        viewModel.loginStatus.observe(viewLifecycleOwner, EventObserver(
            onError = {
                loginProgressBar.isVisible = false
                snackbar(it)
            } ,
            onLoading = { loginProgressBar.isVisible = true }
        ) {
            loginProgressBar.isVisible = false
            Intent(requireContext() , MainActivity::class.java).also {
                startActivity(it)
                requireActivity().finish()
            }
        })
    }
}