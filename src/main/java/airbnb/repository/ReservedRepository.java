package airbnb.repository;

import airbnb.model.ReservedEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * Created by Arianna on 22/9/2017.
 */
@Repository("reservedRepository")
public interface ReservedRepository extends JpaRepository<ReservedEntity,Integer> {
    Set<ReservedEntity> findByApartment(int id);
}
