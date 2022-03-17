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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
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

    private val args by navArgs<GameFragmentArgs>()

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            GameViewModelFactory(args.level, requireActivity().application)
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        viewModel.gameResult.observe(viewLifecycleOwner) {
            launchGameFinishFragment(it)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }




    private fun launchGameFinishFragment(gameResult: GameResult) {
        findNavController().navigate(
            GameFragmentDirections
                .actionGameFragmentToGameFinishedFragment(gameResult)
        )
    }
}
