package lt.paulius.maps.repositories;

import lt.paulius.maps.models.CityDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends JpaRepository<CityDAO, Long> {
}
