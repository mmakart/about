package fintech.dao.home_task_5;

import fintech.models.home_task_5.jpa.Weather;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherHomeTask5JpaDao extends JpaRepository<Weather, Long> {
    public static final String UPDATE_WEATHER_QUERY = "UPDATE weather w"
            + "SET w.weather_type_id = (SELECT id FROM weather_type WHERE name = ?1)"
            + "WHERE w.city_id = (SELECT id FROM city WHERE name = ?2)";

    public static final String SELECT_WEATHER_BY_CITY_NAME_QUERY = "SELECT * "
            + "FROM weather "
            + "WHERE city_id = "
            + "(SELECT id FROM city WHERE name = ?1)";

    public static final String DELETE_ALL_WEATHER_IN_CITY_QUERY = "DELETE FROM weather "
            + "WHERE city_id = "
            + "(SELECT id FROM city WHERE name = ?1)";

    @Query(value = SELECT_WEATHER_BY_CITY_NAME_QUERY, nativeQuery = true)
    List<Weather> findByCity(String city);

    <T extends Weather> T save(T weather);

    @Modifying
    @Query(value = UPDATE_WEATHER_QUERY, nativeQuery = true)
    Weather updateAllWeatherInCity(String city, String weatherType);

    @Modifying
    @Query(value = DELETE_ALL_WEATHER_IN_CITY_QUERY, nativeQuery = true)
    List<Weather> deleteByCity(String city);
}
