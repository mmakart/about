package fintech.dao.home_task_6;

import fintech.models.home_task_6.Weather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherJpaDao extends JpaRepository<Weather, Long> {
}
