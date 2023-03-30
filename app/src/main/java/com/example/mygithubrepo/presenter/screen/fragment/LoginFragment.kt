package com.example.mygithubrepo.presenter.screen.fragment


import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.mygithubrepo.R
import com.example.mygithubrepo.common.Utils
import com.example.mygithubrepo.common.base.BaseFragment
import com.example.mygithubrepo.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.schedulers.Schedulers

class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    private val navController by lazy(LazyThreadSafetyMode.NONE) {findNavController()}

    private lateinit var auth: FirebaseAuth

    override fun getViewBinding(): FragmentLoginBinding  = FragmentLoginBinding.inflate(layoutInflater)

    override fun setObserver() {

    }


    override fun setupViews() {

        if(!Utils.isNetworkAvailable(requireContext())) Utils.showNetworkError(requireContext())

        auth = Firebase.auth



        binding.tvHaventAccount.setOnClickListener {

            navController.navigate(R.id.action_loginFragment_to_registerFragment)

        }

        login()


        // Email validation

        val emailStream = RxTextView.textChanges(binding.atEmail)
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
                password.isEmpty()
            }

        passwordStream
            .subscribe{
            showTextMinimalAlert(it,"Password")
        }

        //ButtonEnable true or false
        val invalidFieldStream =io.reactivex.Observable.combineLatest(
            emailStream,
            passwordStream,
        ) { usernameInvalid: Boolean,
            passwordInvalid: Boolean ->
            !usernameInvalid && !passwordInvalid

        }

        invalidFieldStream.subscribe{
                isValid->
            if (isValid){
                binding.btnLogin.isEnabled=true
                binding.btnLogin.backgroundTintList= ContextCompat.getColorStateList(context!!,
                    R.color.primary_color
                )
            }else{
                binding.btnLogin.isEnabled=true
                binding.btnLogin.backgroundTintList=
                    ContextCompat.getColorStateList(context!!, android.R.color.darker_gray)
            }
        }
    }

    private fun showEmailValidAlert(isNotValid: Boolean){
        binding.atEmail.error=if (isNotValid) "Email kiriting" else null
    }

    private fun showTextMinimalAlert(isNotValid: Boolean, text:String){
        if(text=="Password"){
            binding.etPassword.error=if (isNotValid) "$text mavjud emas" else null
        }
    }

    private fun signIn(email: String, password: String) {
        // [START sign_in_with_email]
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
                    val user = auth.currentUser
                    updateUI(user)

                    navController.navigate(R.id.action_loginFragment_to_listFragment)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    val a = task.exception.toString().split(" ").toTypedArray()
                    var newString=""
                    var i=1
                    while (i<a.size){
                        newString+= a[i]+" "
                        i++

                    }
                    Toast.makeText(requireContext(),
                        newString,
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
                binding.progressBar.visibility = View.GONE
            }
        // [END sign_in_with_email]
    }


    private fun updateUI(user: FirebaseUser?) {

    }


    private fun login(){
        binding.btnLogin.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            signIn(binding.atEmail.text.toString().trim(), binding.etPassword.text.toString().trim())
        }


    }

    companion object {
        private const val TAG = "EmailPassword"
    }


}