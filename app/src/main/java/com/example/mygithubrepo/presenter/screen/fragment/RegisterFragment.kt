package com.example.mygithubrepo.presenter.screen.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.mygithubrepo.R
import com.example.mygithubrepo.common.Utils
import com.example.mygithubrepo.common.base.BaseFragment
import com.example.mygithubrepo.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.jakewharton.rxbinding2.widget.RxTextView

class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {

    private val navController by lazy(LazyThreadSafetyMode.NONE) {findNavController()}

    private lateinit var auth: FirebaseAuth

    override fun getViewBinding(): FragmentRegisterBinding = FragmentRegisterBinding.inflate(layoutInflater)

    override fun setObserver() {

    }


    private fun createAccount(email: String, password: String) {
        // [START create_user_with_email]
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)

                    navController.navigate(R.id.action_registerFragment_to_loginFragment)

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    val a = task.exception.toString().split(" ").toTypedArray()
                    var newString=""
                    var i=1
                    while (i<a.size){
                        newString+= a[i]+" "
                        i++

                    }
                    Toast.makeText(requireContext(), newString,
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
                binding.progressBar.visibility = View.GONE
            }
        // [END create_user_with_email]
    }

    private fun register(){
        binding.btnRegister.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            createAccount(binding.etEmail.text.toString().trim(), binding.etPassword.text.toString().trim())
        }
    }

    override fun setupViews() {

        if(!Utils.isNetworkAvailable(requireContext())) Utils.showNetworkError(requireContext())

        auth = Firebase.auth

        register()


        binding.tvHavenAccount.setOnClickListener {

            navController.navigate(R.id.action_registerFragment_to_loginFragment)
        }


        // Email validation

        val emailStream = RxTextView.textChanges(binding.etEmail)
            .skipInitialValue()
            .map { email ->
                !Patterns.EMAIL_ADDRESS.matcher(email).matches()
            }
        emailStream.subscribe{
            showEmailValidAlert(it)
        }


        //Password Validation

        val passwordStream= RxTextView.textChanges(binding.etPassword)
            .skipInitialValue()
            .map { password ->
                password.length < 8
            }

        passwordStream.subscribe{
            showTextMinimalAlert(it,"Password")
        }

        //Confirm Password Validation
        val passwordConfirmStream=io.reactivex.Observable.merge(
            RxTextView.textChanges(binding.etPassword)
                .skipInitialValue()
                .map { password ->
                    password.toString() !=binding.etConfirmPassword.text.toString()
                },
            RxTextView.textChanges(binding.etConfirmPassword)
                .skipInitialValue()
                .map { confirmPassword ->
                    confirmPassword.toString() !=binding.etPassword.text.toString()
                })
        passwordConfirmStream.subscribe{
            showPasswordConfirmAlert(it)
        }

        //ButtonEnable true or false
        val invalidFieldStream =io.reactivex.Observable.combineLatest(
            emailStream,
            passwordStream,
            passwordConfirmStream
        ) {  emailInvalid: Boolean,
            passwordInvalid: Boolean, passwordConfirmInvalid: Boolean ->
             !emailInvalid  && !passwordInvalid
                    && !passwordConfirmInvalid
        }

        invalidFieldStream.subscribe{
                isValid->
            if (isValid){
                binding.btnRegister.isEnabled=true
                binding.btnRegister.backgroundTintList= ContextCompat.getColorStateList(requireActivity().applicationContext, R.color.primary_color)
            }else{
                binding.btnRegister.isEnabled=true
                binding.btnRegister.backgroundTintList=
                    ContextCompat.getColorStateList(requireActivity().applicationContext, android.R.color.darker_gray)

            }
        }

    }

    private fun updateUI(user: FirebaseUser?) {

    }

    private fun showTextMinimalAlert(isNotValid: Boolean, text:String){
        if(text=="Password"){
            binding.etPassword.error=if (isNotValid) "$text 8 ta belgidan ko'p bo'lishi kerak" else null
        }
    }

    private fun showEmailValidAlert(isNotValid: Boolean){
        binding.etEmail.error=if (isNotValid) "Email kiriting" else null
    }

    private fun showPasswordConfirmAlert(isNotValid: Boolean){
        binding.etConfirmPassword.error=if (isNotValid) "Parol bir xil emas" else null
    }

    companion object {
        private const val TAG = "EmailPassword"
    }

}