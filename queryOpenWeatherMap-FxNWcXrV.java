import java.io.IOException;
import java.net.MalformedURLException;
import net.aksingh.owmjapis.CurrentWeather;
import net.aksingh.owmjapis.OpenWeatherMap;
import org.json.JSONException;

public class WeatherExample {

    public static void main(String[] args)
            throws IOException, MalformedURLException, JSONException {

        // declaring object of "OpenWeatherMap" class
        OpenWeatherMap owm = new OpenWeatherMap("ea950a82a1470f29811e9fe0592d6afa");

        // getting current weather data for the "London" city
        CurrentWeather cwd = owm.currentWeatherByCityName("Southwick");

        //printing city name from the retrieved data
        System.out.println("City: " + cwd.getCityName());

        // printing the max./min. temperature
        System.out.println("Temperature: " + cwd.getMainInstance().getMaxTemperature()
                            + "/" + cwd.getMainInstance().getMinTemperature() + "\'F");
    }
}