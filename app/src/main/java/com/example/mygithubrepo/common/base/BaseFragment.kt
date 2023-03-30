package com.example.mygithubrepo.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<VB : ViewBinding> : Fragment() {
    private var _binding: VB? = null
    val binding get() = _binding!!

    override fun onCreateView( // bu metod child classlarda implement qilinishi kerak emas!
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getViewBinding()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { // bu metod child classlarda implement qilinishi kerak emas!
        super.onViewCreated(view, savedInstanceState)
        setObserver()
        setupViews()
    }

    override fun onDestroyView() {
        super.onDestroyView()
//        _binding = null
    }

    abstract fun getViewBinding(): VB // ViewBindingni init qilish uchun
    abstract fun setObserver() // viewModelni barcha livedatalari shu metodni ichida observe qilishi shart!
    abstract fun setupViews() // asosiy ishlar qilinadigan metod


}