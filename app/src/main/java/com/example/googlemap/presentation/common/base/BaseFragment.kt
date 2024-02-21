package com.example.googlemap.presentation.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

typealias Inflater<T> = (LayoutInflater, ViewGroup, Boolean) -> T
abstract class BaseFragment<VB: ViewBinding>(private var inflate: Inflater<VB>) : Fragment() {

    private var _binding: VB? = null
    protected val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflate.invoke(inflater, container!!, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
        bindViewActionListener()
        bindObserves()
    }

    abstract fun bind()
    abstract fun bindViewActionListener()
    abstract fun bindObserves()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}