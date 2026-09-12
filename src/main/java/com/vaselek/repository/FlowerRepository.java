package com.vaselek.repository;

import com.vaselek.model.Flower;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FlowerRepository extends JpaRepository<Flower, Long> {
    List<Flower> findBySellerId(Long sellerId);
}
