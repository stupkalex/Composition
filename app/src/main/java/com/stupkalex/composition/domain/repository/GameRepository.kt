package com.stupkalex.composition.domain.repository

import com.stupkalex.composition.domain.entity.GameSetting
import com.stupkalex.composition.domain.entity.Level
import com.stupkalex.composition.domain.entity.Question

interface GameRepository {

    fun generateQuestion(
        maxSumValue: Int,
        countOfOptions: Int
    ): Question

    fun getGameSetting(level: Level): GameSetting

}