package org.app.mss.persistences.repositories;

import org.app.mss.persistences.entities.Data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IDataRepository extends JpaRepository<Data, Long> {

    Optional<List<Data>> findAllBySensor(String sensor);

}