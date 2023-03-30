package com.example.mygithubrepo.common.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VB : ViewBinding>  : AppCompatActivity() {

    private var _binding: VB? = null
    val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) { // bu metod child classlarda implement qilinishi kerak emas!
        super.onCreate(savedInstanceState)
        _binding = getViewBinding()
        setContentView(binding.root)
        setObserver()
        doWork()

    }


    abstract fun getViewBinding(): VB // ViewBindingni init qilish uchun
    abstract fun doWork() // asosiy ishlar qilinadigan metod
    abstract fun setObserver() // viewModelni barcha livedatalari shu metodni ichida observe qilishi shart!


}