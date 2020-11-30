package com.example.reposetories;



import org.springframework.data.jpa.repository.JpaRepository;

import com.example.beans.Customer;

public interface CustomerReposetory extends JpaRepository<Customer, Integer> {

	//used in add customer
	boolean existsCustomerByEmail(String email);

	//used in customer login
	Customer findCustomerByEmailAndPassword(String email, String password);

}
