package fintech.dao;

import fintech.models.Weather;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import org.springframework.stereotype.Component;

@Component
public class WeatherDao {
    private List<Weather> weatherList = new ArrayList<>();

    {
        weatherList.add(new Weather(52, "Omskaya oblast'", 38.9,
                LocalDateTime.of(2023, 7, 12, 12, 0)));
        weatherList.add(new Weather(52, "Omskaya oblast'", -12.2,
                LocalDateTime.of(2022, 11, 16, 0, 0)));
        weatherList.add(new Weather(52, "Omskaya oblast'", -27.6,
                LocalDateTime.of(2022, 11, 26, 0, 0)));
        weatherList.add(new Weather(50, "Novosibirskaya oblast'", 32.6,
                LocalDateTime.of(2023, 7, 12, 12, 0)));
        weatherList.add(new Weather(50, "Novosibirskaya oblast'", -0.1,
                LocalDateTime.of(2022, 11, 16, 0, 0)));
        weatherList.add(new Weather(50, "Novosibirskaya oblast'", -28.6,
                LocalDateTime.of(2022, 11, 26, 0, 0)));
        weatherList.add(new Weather(25, "Irkutskaya oblast'", 27.8,
                LocalDateTime.of(2023, 7, 12, 12, 0)));
        weatherList.add(new Weather(25, "Irkutskaya oblast'", -0.5,
                LocalDateTime.of(2022, 11, 16, 0, 0)));
        weatherList.add(new Weather(25, "Irkutskaya oblast'", -15.5,
                LocalDateTime.of(2022, 11, 26, 0, 0)));

        weatherList.add(
                new Weather(52, "Omskaya oblast'", 10.0, LocalDateTime.now()));
        weatherList.add(new Weather(52, "Omskaya oblast'", -0.5,
                LocalDateTime.now().minusHours(1L)));
        weatherList.add(
                new Weather(50, "Novosibirskaya oblast'", 15.0, LocalDateTime.now()));
        weatherList.add(new Weather(50, "Novosibirskaya oblast'", 5.0,
                LocalDateTime.now().minusHours(1L)));
    }

    public Double getLastTemperatureInRegionByDate(int regionId, LocalDate date) {
        return weatherList.stream()
                .filter(weather -> weather != null && weather.getRegionId() == regionId &&
                        weather.getDateAndTime().toLocalDate().equals(date))
                .sorted(Comparator.comparing(Weather::getDateAndTime).reversed())
                .map(Weather::getTemperatureInCelsius)
                .findFirst()
                .orElse(null);
    }

    public void addWeather(Weather weather) {
        weatherList.add(weather);
    }

    public List<Weather> getWeatherListByDateAndTime(LocalDateTime dateAndTime) {
        return weatherList.stream()
                .filter(weather -> weather != null &&
                        weather.getDateAndTime().equals(dateAndTime))
                .toList();
    }

    public boolean areThereWeatherItemsWithId(int regionId) {
        return weatherList.stream().anyMatch(
                weather -> weather != null && weather.getRegionId() == regionId);
    }

    public String getRegionNameByRegionId(int regionId) {
        return weatherList.stream()
                .filter(weather -> weather != null && weather.getRegionId() == regionId)
                .map(Weather::getRegionName)
                .findFirst()
                .orElseThrow();
    }

    public void updateTemperatureByRegionIdAndByDateAndTime(
            double newTemperature, int regionId, LocalDateTime dateAndTime) {
        Weather oldWeather = weatherList.stream()
                .filter(weather -> weather != null && weather.getRegionId() == regionId &&
                        weather.getDateAndTime().equals(dateAndTime))
                .sorted(Comparator.comparing(Weather::getDateAndTime).reversed())
                .findFirst()
                .orElseThrow();

        Weather newWeather = new Weather(oldWeather.getRegionId(), oldWeather.getRegionName(),
                newTemperature, oldWeather.getDateAndTime());

        int oldWeatherIndex = weatherList.indexOf(oldWeather);
        weatherList.remove(oldWeatherIndex);
        weatherList.add(newWeather);
    }

    public List<Weather> deleteAllWeatherItemsById(int regionId) {
        Predicate<Weather> predicate = weather -> weather != null && weather.getRegionId() == regionId;

        List<Weather> deleted = weatherList.stream().filter(predicate).toList();
        weatherList.removeIf(predicate);

        return deleted;
    }
}
