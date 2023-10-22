package fintech.dao.home_task_6;

import fintech.models.home_task_6.Weather;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class WeatherJdbcDao {

    private final DataSource dataSource;

    public Weather save(Weather weather) {
        String insertQuery = "INSERT INTO weather "
                + "(weather_type) VALUES (?)";
        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        insertQuery, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, weather.getType());
            if (statement.executeUpdate() > 0) {
                try (ResultSet ids = statement.getGeneratedKeys()) {
                    if (ids.next()) {
                        weather.setId(ids.getLong(1));
                    } else {
                        throw new SQLException("No weather id generated");
                    }
                }
            }
            return weather;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
