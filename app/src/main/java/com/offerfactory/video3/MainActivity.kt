package com.offerfactory.video3

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// есть TextView.
// Требуется написать таймер — в TextView будет раз в секунду меняться текст от 5 до 1.
// При достижении нуля открыть SecondActivity
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView = findViewById<TextView>(R.id.textView)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.timerFlow.collect { timerValue ->
                    textView.text = timerValue.toString()
                }
            }
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.stopTimerFlow.collect {
                    launchSecondActivity()
                }
            }
        }

        if (savedInstanceState == null) {
            viewModel.startTimer()
        }
    }

    private fun launchSecondActivity() {
        val intent = Intent(this, SecondActivity::class.java)
        startActivity(intent)
        finish()
    }
}

class MainViewModel : ViewModel() {

    private val timerFlowMutable = MutableStateFlow(5)
    val timerFlow = timerFlowMutable.asStateFlow()

    private val stopTimerFlowMutable = MutableSharedFlow<Unit>()
    val stopTimerFlow = stopTimerFlowMutable.asSharedFlow()

    fun startTimer() {
        viewModelScope.launch {
            for (i in 5 downTo 1) {
                timerFlowMutable.emit(i)
                delay(1000)
            }

            delay(1000)
            stopTimerFlowMutable.emit(Unit)
        }

    }

}
