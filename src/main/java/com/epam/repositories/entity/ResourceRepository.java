package com.epam.repositories.entity;

import com.epam.model.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceRepository extends JpaRepository<Resource,Long> {
    boolean existsByPath(String path);
    boolean existsByChecksum(String sum);
}
