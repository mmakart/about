package fintech.controllers.home_task_5;

import fintech.dao.home_task_5.WeatherHomeTask5JdbcDao;
import fintech.models.home_task_5.jdbc.Weather;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/weather/home_task_5_jdbc")
@RequiredArgsConstructor
public class WeatherHomeTask5JdbcController {

    private final WeatherHomeTask5JdbcDao weatherDao;

    @GetMapping("/{city}")
    public ResponseEntity<List<Weather>> getWeatherInCity(@PathVariable("city") String city) {
        List<Weather> allWeatherInCity = weatherDao.getAllWeatherInCity(city);
        return ResponseEntity.ok(allWeatherInCity);
    }

    @PostMapping("/{city}")
    public ResponseEntity<Weather> addWeather(@PathVariable("city") String city,
            @RequestParam("type") String weatherType) {
        return ResponseEntity.ok(weatherDao.addWeather(city, weatherType));
    }

    @PutMapping("/{city}")
    public ResponseEntity<Weather> updateLastWeatherInCity(@PathVariable("city") String city,
            @RequestParam("type") String weatherType) {
        return ResponseEntity.ok(
                weatherDao.updateLastWeatherInCity(city, weatherType));
    }

    @DeleteMapping("/{city}")
    public ResponseEntity<List<Weather>> removeAllWeatherInCity(@PathVariable("city") String city) {
        return ResponseEntity.ok(weatherDao.removeAllWeatherInCity(city));
    }
}
