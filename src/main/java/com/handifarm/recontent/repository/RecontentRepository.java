package com.handifarm.recontent.repository;

import com.handifarm.recontent.entity.Recontent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecontentRepository extends JpaRepository<Recontent, String > {

    Page<Recontent> findByCboardId(String cboardId, Pageable pageable);

}
