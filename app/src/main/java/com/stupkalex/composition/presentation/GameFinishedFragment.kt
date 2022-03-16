package com.stupkalex.composition.presentation

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.stupkalex.composition.R
import com.stupkalex.composition.databinding.FragmentGameFinishedBinding
import com.stupkalex.composition.domain.entity.GameResult

class GameFinishedFragment : Fragment() {

    var _binding: FragmentGameFinishedBinding? = null
    val binding: FragmentGameFinishedBinding
        get() = _binding ?: throw RuntimeException("FragmentGameFinishedBinding == null")


    private val args by navArgs<GameFinishedFragmentArgs>()


      override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        launchView()
        binding.buttonRetry.setOnClickListener {
            retryGame()
        }
    }

    private fun launchView() {
        with(binding) {
            tvRequiredAnswers.text = String.format(
                requireContext().resources.getString(R.string.required_score),
                args.gameResult.gameSetting.minCountOfRightAnswer
            )
            tvScoreAnswers.text = String.format(
                requireContext().resources.getString(R.string.score_answers),
                args.gameResult.countOfRightAnswer
            )
            tvRequiredPercentage.text = String.format(
                requireContext().resources.getString(R.string.required_percentage),
                args.gameResult.gameSetting.minPercentOfRightAnswer
            )
            tvScorePercentage.text = String.format(
                requireContext().resources.getString(R.string.score_percentage),
                getPercentRightAnswer()
            )
            emojiResult.setImageDrawable(
                setSmileByWinner()
            )
        }
    }

    private fun setSmileByWinner(): Drawable? {
        return if (args.gameResult.winner) {
            requireContext().resources.getDrawable(R.drawable.ic_smile)
        } else {
            requireContext().resources.getDrawable(R.drawable.ic_sad)
        }
    }

    private fun getPercentRightAnswer(): Int {
        if (args.gameResult.countOfQuestion == 0) {
            return 0
        }
        return ((args.gameResult.countOfRightAnswer / args.gameResult.countOfQuestion.toDouble()) * 100)
            .toInt()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun retryGame() {
        findNavController().popBackStack()
    }
}
