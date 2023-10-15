package fintech.controllers.home_task_5;

import fintech.dao.home_task_5.WeatherHomeTask5JpaDao;
import fintech.models.home_task_5.jpa.City;
import fintech.models.home_task_5.jpa.Weather;
import fintech.models.home_task_5.jpa.WeatherType;
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
@RequestMapping("/api/weather/home_task_5_jpa")
@RequiredArgsConstructor
public class WeatherHomeTask5JpaController {

    private final WeatherHomeTask5JpaDao weatherDao;

    @GetMapping("/{city}")
    public ResponseEntity<List<Weather>> getWeatherInCity(@PathVariable("city") String city) {
        List<Weather> allWeatherInCity = weatherDao.findByCity(city);
        return allWeatherInCity.isEmpty() ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(allWeatherInCity);
    }

    @PostMapping("/{city}")
    public ResponseEntity<Weather> addWeather(@PathVariable("city") String cityName,
            @RequestParam("type") String weatherTypeName) {
        Long weatherId = null;
        WeatherType weatherType = new WeatherType(-1, weatherTypeName);
        City city = new City(-1, cityName);
        Weather weather = new Weather(weatherId, city, weatherType);

        return ResponseEntity.ok(weatherDao.save(weather));
    }

    @PutMapping("/{city}")
    public ResponseEntity<Weather> updateLastWeatherInCity(@PathVariable("city") String cityName,
            @RequestParam("type") String weatherTypeName) {
        return ResponseEntity.ok(
                weatherDao.updateAllWeatherInCity(cityName, weatherTypeName));
    }

    @DeleteMapping("/{city}")
    public ResponseEntity<List<Weather>> removeAllWeatherInCity(@PathVariable("city") String city) {
        return ResponseEntity.ok(weatherDao.deleteByCity(city));
    }
}
