package com.stupkalex.composition.presentation

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.stupkalex.composition.R
import com.stupkalex.composition.domain.entity.GameResult

interface onOptionClickListener{
    fun onOptionClick(option: Int)
}

@BindingAdapter("requiredAnswers")
fun bindRequiredAnswers(textView: TextView, count: Int) {
    textView.text = String.format(
        textView.context.getString(R.string.required_score),
        count
    )
}

@BindingAdapter("scoreAnswers")
fun bindScoreAnswers(textView: TextView, count: Int) {
    textView.text = String.format(
        textView.context.getString(R.string.score_answers),
        count
    )
}

@BindingAdapter("requiredPercentage")
fun bindRequiredPercentage(textView: TextView, count: Int) {
    textView.text = String.format(
        textView.context.getString(R.string.required_percentage),
        count
    )
}

@BindingAdapter("scorePercentage")
fun bindScorePercentage(textView: TextView, gameResult: GameResult) {
    textView.text = String.format(
        textView.context.getString(R.string.score_percentage),
        ((gameResult.countOfRightAnswer / gameResult.countOfQuestion.toDouble()) * 100).toInt()
    )
}

@BindingAdapter("drawableSmile")
fun bindDrawableSmile(imageView: ImageView, gameResult: GameResult) {
    imageView.setImageDrawable(
        if (gameResult.winner) {
            imageView.context.getDrawable(R.drawable.ic_smile)
        } else {
            imageView.context.getDrawable(R.drawable.ic_sad)
        }
    )
}

@BindingAdapter("enoughCount")
fun setEnoughCountColor(textView: TextView, state: Boolean){
    val color = getColorByState(textView.context,state)
    textView.setTextColor(color)
}

@BindingAdapter("enoughPercent")
fun setEnoughPercentColor(progressBar: ProgressBar, state: Boolean){
    val color = getColorByState(progressBar.context,state)
    progressBar.progressTintList =
        ColorStateList.valueOf(color)
}

private fun getColorByState(context: Context, state: Boolean): Int {
    val colorResId = if (state) {
        android.R.color.holo_green_light
    } else {
        android.R.color.holo_red_light
    }
    return ContextCompat.getColor(context, colorResId)
}

@BindingAdapter("numberAsText")
fun getNumberAsText(textView: TextView, number: Int){
    textView.text = number.toString()
}

@BindingAdapter("setOnClickByTextView")
fun setOnClickByTextView(textView: TextView, clickListener: onOptionClickListener){
    textView.setOnClickListener {
        clickListener.onOptionClick(textView.text.toString().toInt())
    }
}






