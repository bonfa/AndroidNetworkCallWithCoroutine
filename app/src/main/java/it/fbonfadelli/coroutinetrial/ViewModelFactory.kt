import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import it.fbonfadelli.coroutinetrial.WeatherViewModel

class ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {  //todo find a better solution to this generic
        return createWeatherViewModel() as T
    }

    private fun createWeatherViewModel(): WeatherViewModel {
        return WeatherViewModel()
    }
}