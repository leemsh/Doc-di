package com.dd.server.repository;

import com.dd.server.domain.Medicine;
import com.dd.server.dto.FindByMedicineChartDto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MedicineRepository extends JpaRepository<Medicine, String> {
    Medicine findByItemName(String itemName);
    Medicine findByItemSeq(String itemSeq);

    @Query("SELECT m FROM Medicine m WHERE " +
            "(:name IS NULL OR m.itemName LIKE %:name%) AND " +
            "((:color1 IS NULL OR m.colorClass1 LIKE %:color1%) AND (:color2 IS NULL OR m.colorClass2 LIKE %:color2%) " +
            "OR (:color1 IS NULL OR m.colorClass2 LIKE %:color1%) AND (:color2 IS NULL OR m.colorClass1 LIKE %:color2%)) AND " +
            "(:shape IS NULL OR m.drugShape LIKE %:shape%) AND " +
            "((:txt1 IS NULL OR m.printFront LIKE %:txt1%) AND (:txt2 IS NULL OR m.printBack LIKE %:txt2%) " +
            "OR (:txt1 IS NULL OR m.printBack LIKE %:txt1%) AND (:txt2 IS NULL OR m.printFront LIKE %:txt2%))")


    List<Medicine> findByChartMedicine(
            @Param("name") String name,
            @Param("color1") String color1,
            @Param("color2") String color2,
            @Param("shape") String shape,
            @Param("txt1") String txt1,
            @Param("txt2") String txt2
    );

    @Query("SELECT m FROM Medicine m WHERE (:name IS NULL OR m.itemName LIKE %:name%)")
    List<Medicine> findByMedicineName(@Param("name") String name);
}