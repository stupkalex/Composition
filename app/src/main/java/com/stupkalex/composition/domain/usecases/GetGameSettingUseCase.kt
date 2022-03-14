package com.stupkalex.composition.domain.usecases

import com.stupkalex.composition.domain.entity.GameSetting
import com.stupkalex.composition.domain.entity.Level
import com.stupkalex.composition.domain.repository.GameRepository

class GetGameSettingUseCase(private val repository: GameRepository) {

    operator fun invoke(level: Level) : GameSetting {
        return repository.getGameSetting(level)
    }
}