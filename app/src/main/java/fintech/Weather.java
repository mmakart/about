package fintech;

import java.time.LocalDateTime;
import lombok.Value;

@Value
public class Weather {
    int id;
    String regionName;
    double temperatureInCelsius;
    LocalDateTime dateAndTime;
}
