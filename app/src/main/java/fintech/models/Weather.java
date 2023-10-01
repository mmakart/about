package fintech.models;

import java.time.LocalDateTime;
import lombok.Value;

@Value
public class Weather {
    int regionId;
    String regionName;
    double temperatureInCelsius;
    LocalDateTime dateAndTime;
}
