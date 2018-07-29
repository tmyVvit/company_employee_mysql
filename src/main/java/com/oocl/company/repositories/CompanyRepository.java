package com.oocl.company.repositories;

import com.oocl.company.entities.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;


@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    Page<Company> findAll(Pageable page);

    @Modifying(clearAutomatically = true)
    @Query("update Company company set company.name = :name where company.id = :id")
    @Transactional
    int changeNameById(@Param("id") Long id, @Param("name") String name);
}
