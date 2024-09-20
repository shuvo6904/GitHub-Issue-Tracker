package com.example.githubissuetracker.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.githubissuetracker.databinding.FragmentIssueDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IssueDetailsFragment : Fragment() {
    private lateinit var binding: FragmentIssueDetailsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIssueDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initListeners()
    }

    private fun initViews() {

    }

    private fun initListeners() {

    }
}