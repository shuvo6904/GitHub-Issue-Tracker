package com.example.githubissuetracker.ui.github_issue_details

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.githubissuetracker.utils.Constants
import com.example.githubissuetracker.R
import com.example.githubissuetracker.databinding.ActivityGitHubIssueDetailsBinding
import com.example.githubissuetracker.network.model.github_issue.Item
import com.example.githubissuetracker.utils.formatToDate
import com.example.githubissuetracker.utils.getParcelableExtraCompat
import com.example.githubissuetracker.utils.loadImage
import io.noties.markwon.Markwon

class GitHubIssueDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGitHubIssueDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGitHubIssueDetailsBinding.inflate(layoutInflater)
        //enableEdgeToEdge()
        setContentView(binding.root)
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/

        initViews()
        initListeners()
    }

    private fun initListeners() {
        binding.backBtn.setOnClickListener {
            finish()
        }
    }

    private fun initViews() {
        binding.apply {

            toolbarTitle.text = getString(R.string.issue_details)

            val gitHubIssue: Item? = intent.getParcelableExtraCompat(Constants.GITHUB_ISSUES_ITEM)

            gitHubIssue?.let { issue ->
                title.text = issue.title ?: ""
                profileImage.loadImage(issue.user?.avatarUrl, R.drawable.ic_profile)
                name.text = issue.user?.login ?: ""
                binding.state.text = getString(R.string.issue_state, issue.state ?: "")
                binding.commentsCount.text = getString(R.string.issue_comments, issue.comments ?: 0)
                binding.createdAt.text = getString(R.string.issue_created_at, issue.createdAt?.formatToDate() ?: "")
                Markwon.create(this@GitHubIssueDetailsActivity).setMarkdown(body, issue.body ?: "")
            }
        }
    }
}