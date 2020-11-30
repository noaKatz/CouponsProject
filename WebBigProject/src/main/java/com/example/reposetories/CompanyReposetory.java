package com.example.reposetories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.beans.Company;


public interface CompanyReposetory extends JpaRepository<Company, Integer> {

// used in update company
boolean existsByNameAndId(String name, int id);

//used in the company login
Company findCompanyByEmailAndPassword(String email, String password);




}
