package fintech.models.home_task_5.jdbc;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Weather {
    private Long id;
    private City city;
    private WeatherType weatherType;
}
