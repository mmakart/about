package fintech;

import static java.util.stream.Collectors.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class App {
    public static void main(String[] args) {
        List<Weather> weatherList = new ArrayList<>();

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

        Map<String, Double> averageTemperatureByRegion = weatherList.stream().collect(
                groupingBy(Weather::getRegionName,
                        averagingDouble(Weather::getTemperatureInCelsius)));
        System.out.println("Average temperature by region:\n" +
                averageTemperatureByRegion + "\n");

        List<String> regionsWhereTemperatureWasMoreThan30 = weatherList.stream()
                .filter(weather -> weather.getTemperatureInCelsius() > 30.0)
                .map(Weather::getRegionName)
                .distinct()
                .toList();
        System.out.println("Regions where temperature was more than 30:\n" +
                regionsWhereTemperatureWasMoreThan30 + "\n");

        Map<Integer, List<Double>> temperatureListById = weatherList.stream().collect(
                groupingBy(Weather::getId,
                        mapping(Weather::getTemperatureInCelsius, toList())));
        System.out.println("Temperatures by id\n" + temperatureListById + "\n");

        Map<Double, List<Weather>> temperatureToWeatherList = weatherList.stream().collect(
                groupingBy(Weather::getTemperatureInCelsius));
        System.out.println("Weather items by temperature:\n" +
                temperatureToWeatherList + "\n");
    }
}
