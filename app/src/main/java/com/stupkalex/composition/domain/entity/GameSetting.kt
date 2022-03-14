package com.stupkalex.composition.domain.entity

data class GameSetting(
    val maSumValue: Int,
    val minCountOfRightAnswer: Int,
    val minPercentOfRightAnswer: Int,
    val gameTimeInSeconds: Int
)