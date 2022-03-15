package com.stupkalex.composition.presentation

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.stupkalex.composition.R
import com.stupkalex.composition.databinding.FragmentGameBinding
import com.stupkalex.composition.domain.entity.GameResult
import com.stupkalex.composition.domain.entity.Level
import com.stupkalex.composition.domain.entity.Question
import java.lang.RuntimeException


class GameFragment : Fragment() {

    var _binding: FragmentGameBinding? = null
    val binding: FragmentGameBinding
        get() = _binding ?: throw RuntimeException("FragmentGameBinding == null")

    private lateinit var level: Level

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(
                requireActivity().application
            )
        )[GameFragmentViewModel::class.java]
    }

    private val tvOptions by lazy {
        mutableListOf<TextView>().apply {
            add(binding.tvOption1)
            add(binding.tvOption2)
            add(binding.tvOption3)
            add(binding.tvOption4)
            add(binding.tvOption5)
            add(binding.tvOption6)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parsArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.startGame(level)
        setupObservers()
        setupSetOnClickOptions()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupSetOnClickOptions() {
        for (tvOptions in tvOptions) {
            tvOptions.setOnClickListener {
                viewModel.chooseAnswer(tvOptions.text.toString().toInt())
            }
        }
    }

    private fun setupObservers() {
        with(viewModel) {
            formattedTime.observe(viewLifecycleOwner) {
                binding.tvTimer.text = it
            }
            progressAnswer.observe(viewLifecycleOwner) {
                binding.tvAnswersProgress.text = it.toString()
            }
            question.observe(viewLifecycleOwner) {
                binding.tvSum.text = it.sum.toString()
                binding.tvLeftNumber.text = it.visibleNumber.toString()
                for (i in 0 until tvOptions.size) {
                    tvOptions[i].text = it.options[i].toString()
                }
            }
            percentOfRightAnswer.observe(viewLifecycleOwner) {
                binding.progressBar.setProgress(it, true)
            }
            enoughCount.observe(viewLifecycleOwner) {
                val color = getColorByState(it)
                binding.tvAnswersProgress.setTextColor(color)
            }
            enoughPercent.observe(viewLifecycleOwner) {
                val color = getColorByState(it)
                binding.progressBar.progressTintList =
                    ColorStateList.valueOf(color)
            }
            minPercent.observe(viewLifecycleOwner) {
                binding.progressBar.secondaryProgress = it
            }
            gameResult.observe(viewLifecycleOwner) {
                launchGameFinishFragment(it)
            }
        }
    }

    private fun getColorByState(state: Boolean): Int {
        val colorResId = if (state) {
            android.R.color.holo_green_light
        } else {
            android.R.color.holo_red_light
        }
        return ContextCompat.getColor(requireContext(), colorResId)
    }

    private fun parsArgs() {
        requireArguments().getParcelable<Level>(LEVEL_KEY)?.let {
            level = it
        }
    }

    private fun launchGameFinishFragment(gameResult: GameResult) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, GameFinishedFragment.getInstance(gameResult))
            .addToBackStack(null)
            .commit()
    }

    companion object {

        private const val LEVEL_KEY = "level"
        const val NAME = "game_fragment"

        fun getInstance(level: Level): GameFragment {
            return GameFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(LEVEL_KEY, level)
                }
            }
        }
    }
}
