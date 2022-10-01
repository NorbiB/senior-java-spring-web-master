package hu.ponte.hr.persistence.repository;

import hu.ponte.hr.persistence.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageStoreRepository extends JpaRepository<Image, Long> {
}
