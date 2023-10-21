package fintech.dao.home_task_5;

import fintech.models.home_task_5.jdbc.City;
import fintech.models.home_task_5.jdbc.Weather;
import fintech.models.home_task_5.jdbc.WeatherType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class WeatherHomeTask5JdbcDao {

    private final DataSource dataSource;

    public WeatherHomeTask5JdbcDao(@Qualifier("jdbcDataSource") DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Weather> getAllWeatherInCity(String cityName) {
        try (Connection connection = dataSource.getConnection()) {
            String query = "SELECT city.id, weather.id, weather.weather_type_id, weather_type.name FROM weather "
                    + "JOIN city ON weather.city_id = city.id "
                    + "JOIN weather_type ON weather.weather_type_id = weather_type.id "
                    + "WHERE city.name = ? ";

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, cityName);

                try (ResultSet rs = statement.executeQuery()) {
                    List<Weather> result = new ArrayList<>();
                    while (rs.next()) {
                        WeatherType weatherType = new WeatherType(rs.getInt("weather_type.id"),
                                rs.getString("weather_type.name"));
                        City city = new City(rs.getInt("city.id"), cityName);
                        Weather weather = new Weather(rs.getLong("weather.id"), city, weatherType);
                        result.add(weather);
                    }
                    return result;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public Weather addWeather(String cityName, String weatherTypeName) {
        try (Connection connection = dataSource.getConnection()) {
            int cityId = tryInsertCity(cityName, connection);
            int weatherTypeId = tryInsertWeatherType(weatherTypeName, connection);
            long weatherId = insertWeather(connection, cityId, weatherTypeId);

            WeatherType weatherType = new WeatherType(weatherTypeId, weatherTypeName);
            City city = new City(cityId, cityName);
            Weather result = new Weather(weatherId, city, weatherType);
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private long insertWeather(Connection connection, int cityId,
            int weatherTypeId) throws SQLException {
        String weatherQuery = "INSERT INTO weather (city_id, weather_type_id) VALUES (?, ?)";
        long weatherId;

        try (PreparedStatement weatherStatement = connection.prepareStatement(
                weatherQuery, Statement.RETURN_GENERATED_KEYS)) {
            weatherStatement.setInt(1, cityId);
            weatherStatement.setInt(2, weatherTypeId);
            weatherStatement.executeUpdate();

            try (ResultSet ids = weatherStatement.getGeneratedKeys()) {
                if (ids.next()) {
                    weatherId = ids.getLong(1);
                } else {
                    throw new SQLException("No weather id generated");
                }
            }
        }
        return weatherId;
    }

    private int tryInsertWeatherType(String weatherTypeName,
            Connection connection) throws SQLException {
        String findWeatherTypeQuery = "SELECT id FROM weather_type WHERE name = ?";

        try (PreparedStatement findWeatherTypeStatement = connection.prepareStatement(findWeatherTypeQuery)) {
            findWeatherTypeStatement.setString(1, weatherTypeName);
            try (ResultSet rs = findWeatherTypeStatement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }

        String weatherTypeQuery = "INSERT INTO weather_type (name) VALUES (?)";
        int weatherTypeId;

        try (PreparedStatement weatherTypeStatement = connection.prepareStatement(
                weatherTypeQuery, Statement.RETURN_GENERATED_KEYS)) {
            weatherTypeStatement.setString(1, weatherTypeName);
            weatherTypeStatement.executeUpdate();

            try (ResultSet ids = weatherTypeStatement.getGeneratedKeys()) {
                if (ids.next()) {
                    weatherTypeId = ids.getInt(1);
                } else {
                    throw new SQLException("No weather type id generated");
                }
            }
        }
        return weatherTypeId;
    }

    private int tryInsertCity(String cityName, Connection connection)
            throws SQLException {
        String findCityQuery = "SELECT id FROM city WHERE name = ?";

        try (PreparedStatement findCityStatement = connection.prepareStatement(findCityQuery)) {
            findCityStatement.setString(1, cityName);
            try (ResultSet rs = findCityStatement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }

        String cityQuery = "INSERT INTO city (name) VALUES (?)";
        int cityId;

        try (PreparedStatement cityStatement = connection.prepareStatement(
                cityQuery, Statement.RETURN_GENERATED_KEYS)) {
            cityStatement.setString(1, cityName);
            cityStatement.executeUpdate();

            try (ResultSet ids = cityStatement.getGeneratedKeys()) {
                if (ids.next()) {
                    cityId = ids.getInt(1);
                } else {
                    throw new SQLException("No city id generated");
                }
            }
            return cityId;
        }
    }

    public Weather updateLastWeatherInCity(String cityName,
            String weatherTypeName) {
        try (Connection connection = dataSource.getConnection()) {
            int cityId = tryFindCity(cityName, connection);
            int weatherTypeId = tryInsertWeatherType(weatherTypeName, connection);
            long affectedWeatherId = findLastWeatherIdInCity(connection, cityId);

            String updateLastWeatherInCityQuery = "UPDATE weather SET weather_type_id = ? WHERE id = ?";
            try (PreparedStatement updateLastWeatherInCityStatement = connection
                    .prepareStatement(updateLastWeatherInCityQuery)) {
                updateLastWeatherInCityStatement.setInt(1, weatherTypeId);
                updateLastWeatherInCityStatement.setLong(2, affectedWeatherId);
                updateLastWeatherInCityStatement.executeUpdate();
            }

            WeatherType weatherType = new WeatherType(weatherTypeId, weatherTypeName);
            City city = new City(cityId, cityName);
            Weather result = new Weather(affectedWeatherId, city, weatherType);
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private long findLastWeatherIdInCity(Connection connection, int cityId)
            throws SQLException {
        String findLastWeatherInCityQuery = "SELECT id FROM weather WHERE city_id = ? ORDER BY id DESC LIMIT 1";
        long affectedWeatherId;
        try (PreparedStatement findLastWeatherInCityStatement = connection
                .prepareStatement(findLastWeatherInCityQuery)) {
            findLastWeatherInCityStatement.setInt(1, cityId);
            try (ResultSet rs = findLastWeatherInCityStatement.executeQuery()) {
                if (rs.next()) {
                    affectedWeatherId = rs.getLong(1);
                } else {
                    throw new SQLException("Weather with such city id (" + cityId +
                            ") not found");
                }
            }
        }
        return affectedWeatherId;
    }

    private int tryFindCity(String cityName, Connection connection)
            throws SQLException {
        String findCityQuery = "SELECT id FROM city WHERE name = ?";
        int cityId;
        try (PreparedStatement findCityStatement = connection.prepareStatement(findCityQuery)) {
            findCityStatement.setString(1, cityName);
            try (ResultSet rs = findCityStatement.executeQuery()) {
                if (rs.next()) {
                    cityId = rs.getInt(1);
                } else {
                    throw new SQLException("No city with such name (" + cityName +
                            ") found");
                }
            }
        }
        return cityId;
    }

    public List<Weather> removeAllWeatherInCity(String cityName) {
        try (Connection connection = dataSource.getConnection()) {
            List<Weather> toDelete = findWeatherToDelete(cityName, connection);

            String removeAllWeatherInCityQuery = "DELETE FROM weather "
                    + "WHERE city_id = (SELECT id FROM city WHERE name = ?)";
            try (PreparedStatement removeAllWeatherInCityStatement = connection
                    .prepareStatement(removeAllWeatherInCityQuery)) {
                removeAllWeatherInCityStatement.setString(1, cityName);
                int deletedCount = removeAllWeatherInCityStatement.executeUpdate();
                if (deletedCount >= 1) {
                    return toDelete;
                }
                return Collections.emptyList();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    private List<Weather> findWeatherToDelete(String cityName,
            Connection connection)
            throws SQLException {
        String findDeletedWeatherQuery = "SELECT weather.id, city.id, "
                + "weather_type.id, weather_type.name "
                + "FROM city "
                + "JOIN weather ON city.id = weather.city_id "
                + "JOIN weather_type ON weather.weather_type_id = weather_type.id "
                + "WHERE city.name = ?";
        try (PreparedStatement findDeletedWeatherStatement = connection.prepareStatement(findDeletedWeatherQuery)) {
            findDeletedWeatherStatement.setString(1, cityName);
            try (ResultSet rs = findDeletedWeatherStatement.executeQuery()) {
                List<Weather> result = new ArrayList<>();
                while (rs.next()) {
                    long weatherId = rs.getLong("weather.id");
                    int weatherTypeId = rs.getInt("weather_type.id");
                    String weatherTypeName = rs.getString("weather_type.name");
                    int cityId = rs.getInt("city.id");

                    WeatherType weatherType = new WeatherType(weatherTypeId, weatherTypeName);
                    City city = new City(cityId, cityName);
                    Weather weather = new Weather(weatherId, city, weatherType);
                    result.add(weather);
                }
                return result;
            }
        }
    }
}
