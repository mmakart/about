package fintech.controllers;

import fintech.dao.WeatherDao;
import fintech.models.Weather;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
public class WeatherController {
    private final WeatherDao weatherDao;

    @GetMapping("/{region_id}")
    public Double getLastTemperatureInRegion(@PathVariable("region_id") int regionId) {
        boolean areThereWeatherItemsWithGivenRegionId = weatherDao.areThereWeatherItemsWithId(regionId);
        if (areThereWeatherItemsWithGivenRegionId) {
            return weatherDao.getLastTemperatureInRegionByDate(regionId,
                    LocalDate.now());
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No weather items has been found with given region id (" + regionId +
                            ")");
        }
    }

    @PostMapping("/{region_id}")
    public Weather postWeather(@PathVariable("region_id") int regionId,
            @RequestParam("region") String regionName,
            @RequestParam("temperature") double temperature,
            @RequestParam(name = "date_and_time", required = false) String formattedDateTime) {
        Weather weather = null;
        try {
            LocalDateTime dateAndTime = formattedDateTime == null
                    ? LocalDateTime.now()
                    : LocalDateTime.parse(formattedDateTime,
                            DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            weather = new Weather(regionId, regionName, temperature, dateAndTime);
            weatherDao.addWeather(weather);
        } catch (DateTimeParseException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Weather haven't been added due to incorrect time and date format",
                    e);
        }

        return weather;
    }

    @PutMapping("/{region_id}")
    public Weather updateTemperatureOrAddWeatherIfThereAreNoOneAtGivenTime(
            @PathVariable("region_id") int regionId,
            @RequestParam("temperature") double temperature,
            @RequestParam(name = "date_and_time", required = false) String formattedDateTime) {

        boolean areThereWeatherItemsWithGivenRegionId = weatherDao.areThereWeatherItemsWithId(regionId);
        if (!areThereWeatherItemsWithGivenRegionId) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Weather can't be updated or added because there are no such region id (" +
                            regionId +
                            ") and there are no corresponding region name in database");
        }

        Weather newWeather = null;
        try {
            LocalDateTime dateAndTime = formattedDateTime == null
                    ? LocalDateTime.now()
                    : LocalDateTime.parse(formattedDateTime,
                            DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            List<Weather> weatherListAtGivenDateAndTime = weatherDao.getWeatherListByDateAndTime(dateAndTime);

            if (weatherListAtGivenDateAndTime.isEmpty()) {
                String regionName = weatherDao.getRegionNameByRegionId(regionId);
                newWeather = new Weather(regionId, regionName, temperature, dateAndTime);
                weatherDao.addWeather(newWeather);
            } else {
                weatherDao.updateTemperatureByRegionIdAndByDateAndTime(
                        temperature, regionId, dateAndTime);
            }
        } catch (DateTimeParseException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Weather can't be updated or added due to incorrect time and date format",
                    e);
        }

        return newWeather;
    }

    @DeleteMapping("/{region_id}")
    public List<Weather> deleteAllWeatherItemsByRegionId(@PathVariable("region_id") int regionId) {
        return weatherDao.deleteAllWeatherItemsById(regionId);
    }
}
