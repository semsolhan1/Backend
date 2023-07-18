package com.handifarm.recontent.repository;

import com.handifarm.recontent.entity.Recontent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecontentRepository extends JpaRepository<Recontent, String > {

//    Page<Recontent> findByCboardId(String cboardId, Pageable pageable);

//    Page<Recontent> findByCboard_CboardId(String cboardId, Pageable pageable);

        Page<Recontent> findByCboard_CboardId(String cboardId, Pageable pageable);

        Optional<Recontent> findByCboard_CboardIdAndRecontentOrder(String cboardId, int recontentOrder);

//    Optional<Recontent> findByCboard_CboardId(String recontentId);
}
