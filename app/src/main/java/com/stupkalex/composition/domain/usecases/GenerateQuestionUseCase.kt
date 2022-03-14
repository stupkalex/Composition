package com.stupkalex.composition.domain.usecases

import com.stupkalex.composition.domain.entity.Question
import com.stupkalex.composition.domain.repository.GameRepository

class GenerateQuestionUseCase(private val repository: GameRepository) {

    operator fun invoke(maxSumValue: Int): Question{
        return repository.generateQuestion(maxSumValue, COUNT_OF_OPTIONS)
    }

    companion object{
        private const val COUNT_OF_OPTIONS = 6
    }
}