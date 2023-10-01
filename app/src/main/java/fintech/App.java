package fintech;

import static java.util.stream.Collectors.*;

import fintech.models.Weather;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class App {
    public static void main(String[] args) {
        List<Weather> weatherList = new ArrayList<>();
        fillWeatherList(weatherList);

        Map<String, Double> averageTemperatureByRegion = getAverageTemperatureByRegion(weatherList);
        System.out.println("Average temperature by region:\n" +
                averageTemperatureByRegion + "\n");

        List<String> regionsWhereTemperatureWasMoreThan30 = getRegionsWhereTemperatureWasMoreThan(weatherList, 30.0);
        System.out.println("Regions where temperature was more than 30:\n" +
                regionsWhereTemperatureWasMoreThan30 + "\n");

        Map<Integer, List<Double>> temperatureListById = getTemperatureListById(weatherList);
        System.out.println("Temperatures by id\n" + temperatureListById + "\n");

        Map<Double, List<Weather>> temperatureToWeatherList = getTemperatureToWeatherList(weatherList);
        System.out.println("Weather items by temperature:\n" +
                temperatureToWeatherList + "\n");
    }

    private static void fillWeatherList(List<Weather> weatherList) {
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
    }

    private static Map<String, Double> getAverageTemperatureByRegion(List<Weather> weatherList) {
        return weatherList.stream().collect(
                groupingBy(Weather::getRegionName,
                        averagingDouble(Weather::getTemperatureInCelsius)));
    }

    private static List<String> getRegionsWhereTemperatureWasMoreThan(List<Weather> weatherList,
            double temperature) {
        return weatherList.stream()
                .filter(weather -> weather.getTemperatureInCelsius() > temperature)
                .map(Weather::getRegionName)
                .distinct()
                .toList();
    }

    private static Map<Integer, List<Double>> getTemperatureListById(List<Weather> weatherList) {
        return weatherList.stream().collect(
                groupingBy(Weather::getRegionId,
                        mapping(Weather::getTemperatureInCelsius, toList())));
    }

    private static Map<Double, List<Weather>> getTemperatureToWeatherList(List<Weather> weatherList) {
        return weatherList.stream().collect(
                groupingBy(Weather::getTemperatureInCelsius));
    }
}
