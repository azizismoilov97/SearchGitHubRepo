package com.example.mygithubrepo.app
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import android.window.OnBackInvokedDispatcher
import androidx.activity.OnBackPressedCallback
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import com.example.mygithubrepo.R
import com.example.mygithubrepo.common.Utils
import com.example.mygithubrepo.common.base.BaseActivity
import com.example.mygithubrepo.databinding.MainActivityBinding


class MainActivity : BaseActivity<MainActivityBinding>() {

    private var doubleBackToExitPressedOnce = false

    private val navController by lazy(LazyThreadSafetyMode.NONE) {findNavController(R.id.nav_host_fragment)}

    override fun getViewBinding(): MainActivityBinding {
        return MainActivityBinding.inflate(layoutInflater)
    }

    override fun doWork() {
       supportActionBar?.hide()

        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

                val currentFragment = navController.currentDestination?.label

                if (currentFragment=="UserListFragment"){

                    if (doubleBackToExitPressedOnce){
                        finish()
                    }

                    doubleBackToExitPressedOnce = true
                    Toast.makeText(this@MainActivity, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

                    Handler(Looper.getMainLooper()).postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
                }else if (currentFragment=="fragment_login"){
                    finish()
                }else{
                    navController.navigate(R.id.action_registerFragment_to_loginFragment)
                }
            }
        })
    }

    override fun setObserver() {

    }

}
