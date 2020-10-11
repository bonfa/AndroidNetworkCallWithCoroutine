package it.fbonfadelli.coroutinetrial

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer

class WeatherActivity : AppCompatActivity() {

    private lateinit var stateName: TextView
    private lateinit var windSpeedContent: TextView
    private lateinit var windDirectionContent: TextView
    private lateinit var windDirectionCompContent: TextView
    private lateinit var detailContainer: View
    private lateinit var errorMessage: TextView
    private lateinit var loader: ProgressBar
    private lateinit var resetViewButton: Button
    private lateinit var reloadButton: Button

    private lateinit var viewModel: WeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindView()

        viewModel = WeatherViewModel.INSTANCE
        viewModel.isLoaderVisible().observe(this, Observer(this::updateLoaderVisibility))
        viewModel.message().observe(this, Observer(this::updateMessage))
        viewModel.isWeatherVisible().observe(this, Observer(this::updateWeatherDetailVisibility))
        viewModel.weather().observe(this, Observer(this::updateWeatherDetail))
    }

    private fun bindView() {
        loader = findViewById(R.id.progress)
        detailContainer = findViewById(R.id.detail)
        stateName = findViewById(R.id.state_name)
        windSpeedContent = findViewById(R.id.wind_speed_content)
        windDirectionContent = findViewById(R.id.wind_direction_content)
        windDirectionCompContent = findViewById(R.id.wind_direction_comp_content)
        errorMessage = findViewById(R.id.no_content_message)
        resetViewButton = findViewById(R.id.reset_view_button)
        reloadButton = findViewById(R.id.reload_button)

        reloadButton.setOnClickListener { viewModel.updateWeather() }
        resetViewButton.setOnClickListener { viewModel.resetView() }
    }

    private fun updateLoaderVisibility(visible: Boolean) {
        this.loader.visibility = if (visible) VISIBLE else GONE
    }

    private fun updateMessage(viewMessage: ViewMessage) {
        this.errorMessage.text = viewMessage.content
        this.errorMessage.visibility = if (viewMessage.visible) VISIBLE else GONE
    }

    private fun updateWeatherDetail(viewModel: ViewWeather) {
        stateName.text = viewModel.weatherStateName
        windSpeedContent.text = viewModel.windSpeed
        windDirectionContent.text = viewModel.windDirection
        windDirectionCompContent.text = viewModel.windDirectionCompass
    }

    private fun updateWeatherDetailVisibility(visible: Boolean) {
        this.detailContainer.visibility = if (visible) VISIBLE else GONE
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateWeather()
    }
}