package com.stupkalex.composition.presentation

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.stupkalex.composition.R
import com.stupkalex.composition.databinding.FragmentGameFinishedBinding
import com.stupkalex.composition.domain.entity.GameResult

class GameFinishedFragment : Fragment() {

    var _binding: FragmentGameFinishedBinding? = null
    val binding: FragmentGameFinishedBinding
        get() = _binding ?: throw RuntimeException("FragmentGameFinishedBinding == null")

    private lateinit var gameResult: GameResult

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArg()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                retryGame()
            }
        }
        launchView()
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        binding.buttonRetry.setOnClickListener {
            retryGame()
        }
    }

    private fun launchView() {
        with(binding) {
            tvRequiredAnswers.text = String.format(
                requireContext().resources.getString(R.string.required_score),
                gameResult.gameSetting.minCountOfRightAnswer
            )
            tvScoreAnswers.text = String.format(
                requireContext().resources.getString(R.string.score_answers),
                gameResult.countOfRightAnswer
            )
            tvRequiredPercentage.text = String.format(
                requireContext().resources.getString(R.string.required_percentage),
                gameResult.gameSetting.minPercentOfRightAnswer
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
        return  if (gameResult.winner){
            requireContext().resources.getDrawable(R.drawable.ic_smile)
        } else {
            requireContext().resources.getDrawable(R.drawable.ic_sad)
        }
    }

    private fun getPercentRightAnswer(): Int{
        if(gameResult.countOfQuestion == 0){
            return 0
        }
        return ((gameResult.countOfRightAnswer / gameResult.countOfQuestion.toDouble()) * 100)
            .toInt()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun parseArg() {
        requireArguments().getParcelable<GameResult>(GAME_RESULT)?.let {
            gameResult = it
        }
    }

    private fun retryGame() {
        requireActivity().supportFragmentManager.popBackStack(
            GameFragment.NAME,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
    }

    companion object {

        const val GAME_RESULT = "game_result"

        fun getInstance(gameResult: GameResult): GameFinishedFragment {
            return GameFinishedFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(GAME_RESULT, gameResult)
                }
            }
        }
    }
}
