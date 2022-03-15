package com.stupkalex.composition.presentation

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.stupkalex.composition.R
import com.stupkalex.composition.domain.entity.GameResult
import com.stupkalex.composition.domain.entity.GameSetting
import com.stupkalex.composition.domain.entity.Level
import com.stupkalex.composition.domain.entity.Question
import com.stupkalex.composition.domain.usecases.GenerateQuestionUseCase
import com.stupkalex.composition.domain.usecases.GetGameSettingUseCase
import ru.sumin.composition.data.GameRepositoryImpl

class GameFragmentViewModel(application: Application) : AndroidViewModel(application) {
    val context = application

    private lateinit var level: Level
    private lateinit var gameSetting: GameSetting

    private val _formattedTime = MutableLiveData<String>()
    val formattedTime: LiveData<String>
        get() = _formattedTime

    private val _question = MutableLiveData<Question>()
    val question: LiveData<Question>
        get() = _question

    private val _progressAnswer = MutableLiveData<String>()
    val progressAnswer: LiveData<String>
        get() = _progressAnswer

    private val _percentOfRightAnswer = MutableLiveData<Int>()
    val percentOfRightAnswer: LiveData<Int>
        get() = _percentOfRightAnswer

    private val _enoughCount = MutableLiveData<Boolean>()
    val enoughCount: LiveData<Boolean>
        get() = _enoughCount

    private val _enoughPercent = MutableLiveData<Boolean>()
    val enoughPercent: LiveData<Boolean>
        get() = _enoughPercent

    private val _minPercent = MutableLiveData<Int>()
    val minPercent: LiveData<Int>
        get() = _minPercent

    private val _gameResult = MutableLiveData<GameResult>()
    val gameResult: LiveData<GameResult>
        get() = _gameResult

    var countOfRightAnswer = 0
    var countQuestion = 0

    private var timer: CountDownTimer? = null

    private val repository = GameRepositoryImpl

    private val generateQuestionUseCase = GenerateQuestionUseCase(repository)
    private val getGameSettingUseCase = GetGameSettingUseCase(repository)

    fun startGame(level: Level) {
        getGameSetting(level)
        startTimer()
        generateQuestion()
        updateProgress()
    }

    private fun getGameSetting(level: Level) {
        this.level = level
        this.gameSetting = getGameSettingUseCase(level)
        _minPercent.value = gameSetting.minPercentOfRightAnswer
    }

    private fun startTimer() {
        timer = object :
            CountDownTimer(gameSetting.gameTimeInSeconds * MILLIS_IN_SECONDS, MILLIS_IN_SECONDS) {
            override fun onTick(millis: Long) {
                _formattedTime.value = formattedTime(millis)
            }

            override fun onFinish() {
                finishGame()
            }
        }
        timer?.start()
    }

    private fun formattedTime(millis: Long): String {
        val allSeconds = millis / MILLIS_IN_SECONDS
        val minutes = allSeconds / SECONDS_IN_MINUTES
        val seconds = allSeconds - (minutes * SECONDS_IN_MINUTES)
        return String.format("%02d:%02d", minutes, seconds)
    }

    fun generateQuestion() {
        _question.value = generateQuestionUseCase(gameSetting.maxSumValue)
    }

    fun chooseAnswer(number: Int) {
        checkAnswer(number)
        updateProgress()
        generateQuestion()
    }

    private fun updateProgress() {
        val percent = calculatePercentOfRightAnswer()
        _percentOfRightAnswer.value = percent
        _progressAnswer.value = String.format(
            context.resources.getString(R.string.progress_answers),
            countOfRightAnswer,
            gameSetting.minCountOfRightAnswer
        )
        _enoughCount.value = countOfRightAnswer >= gameSetting.minCountOfRightAnswer
        _enoughPercent.value = percent >= gameSetting.minPercentOfRightAnswer
    }

    private fun calculatePercentOfRightAnswer(): Int {
        if (countQuestion == 0) {
            return 0
        }
        return ((countOfRightAnswer / countQuestion.toDouble()) * 100).toInt()
    }

    private fun checkAnswer(number: Int) {
        val rightAnswer = question.value?.rightAnswer
        if (number == rightAnswer) {
            countOfRightAnswer++
        }
        countQuestion++
    }

    private fun finishGame() {
        _gameResult.value = GameResult(
            enoughCount.value == true && enoughPercent.value == true,
            countOfRightAnswer,
            countQuestion,
            gameSetting
        )
    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }

    companion object {
        private const val SECONDS_IN_MINUTES = 60
        private const val MILLIS_IN_SECONDS = 1000L
    }
}