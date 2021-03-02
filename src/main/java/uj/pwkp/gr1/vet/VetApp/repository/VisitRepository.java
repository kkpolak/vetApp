package uj.pwkp.gr1.vet.VetApp.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uj.pwkp.gr1.vet.VetApp.entity.Visit;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Integer> {

  @Query("select v from visits v where (:timeFrom >= v.startTime and :timeFrom < (v.startTime + v.duration)) or (:timeTo > v.startTime and :timeTo <= (v.startTime + v.duration))")
  List<Visit> overlaps(LocalDateTime timeFrom, LocalDateTime timeTo);
}