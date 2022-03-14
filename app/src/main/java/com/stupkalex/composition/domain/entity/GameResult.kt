package com.stupkalex.composition.domain.entity

data class GameResult(
    val winner: Boolean,
    val countOfRightAnswer: Int,
    val countOfQuestion: Int,
    val gameSetting: GameSetting
)