package com.example.facades;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.example.beans.Company;
import com.example.beans.Coupon;
import com.example.beans.Customer;
import com.example.exceptions.companyAlreadyExistException;
import com.example.exceptions.companyNameCantChangeException;
import com.example.exceptions.companyNotFoundException;
import com.example.exceptions.customerAlreadyExistException;
import com.example.exceptions.customerNotFoundException;
import com.example.reposetories.CompanyReposetory;
import com.example.reposetories.CouponReposetory;
import com.example.reposetories.CustomerReposetory;

@Service
@Scope("prototype")
public class AdminFacade extends ClientFacade {

	
	public AdminFacade(CompanyReposetory companyReposetory, CouponReposetory couponReposetory,
			CustomerReposetory customerReposetory) {
		super(companyReposetory, couponReposetory, customerReposetory);
	}

	//make a login of adminFacade
	public boolean login(String email, String password) {
		// if the email and the password exist return true
		if ((email.equals("admin@admin.com")) && (password.equals("admin"))) {
			return true;
		}
		return false;
	}

	// add a company
	public void addCompany(Company company) throws companyAlreadyExistException {
		List<Company> companies = companyReposetory.findAll();
		for (Company comp : companies) {
			// the new company can't have email or name that already exist
			if ((comp.getEmail().equals(company.getEmail())) || (comp.getName().equals(company.getName())))
				throw new companyAlreadyExistException();
		}
		// if there is no company with this name or that email, add that company
		companyReposetory.save(company);
	}

	
	// update the company details
	public void updateCompany(Company company) throws companyNameCantChangeException, companyAlreadyExistException {
		List<Company> companies = companyReposetory.findAll();
		for (Company comp : companies) {
			// the company can't have email or name that already exist who is not her name
			if (((comp.getEmail().equals(company.getEmail())) || (comp.getName().equals(company.getName())))
					&& (company.getId()!=comp.getId()))
				throw new companyAlreadyExistException();
		}
		// we can't change the name of the company
		if (companyReposetory.existsByNameAndId(company.getName(), company.getId())) {
			// update the company
			companyReposetory.save(company);
		} else
			throw new companyNameCantChangeException();
	}

	
	// delete a company(we need first to delete the purchase history of her coupons and the coupons)
	public void deleteCompany(int id) throws companyNotFoundException {
		List<Coupon> coupons = couponReposetory.findCouponsByCompanyId(id);
		for (Coupon coupon : coupons) {
			// delete the history of coupon purchases
			couponReposetory.deleteCouponParchesByCouponId(coupon.getId());
		}
		// delete the coupon
		couponReposetory.deleteCompanyCoupons(id);
		// after we deleted all the coupons of this company, we can delete the company
		companyReposetory.deleteById(id);
	}

	// gives back all the companies
	public List<Company> getAllCompanies() {
		return companyReposetory.findAll();
	}

	// gives back one company by her id
	public Company getOneCompanyById(int id) throws companyNotFoundException {
		return companyReposetory.findById(id).orElseThrow(companyNotFoundException::new);
	}

	// add a customer
	public void addCustomer(Customer customer) throws customerAlreadyExistException {
		//we have to check that we don't have another customer with that email
		if (customerReposetory.existsCustomerByEmail(customer.getEmail())) {
			throw new customerAlreadyExistException();
		} else {
			// if there isn't any customer with this email, we can add this customer
			customerReposetory.save(customer);
		}
	}

	// update a customer
	public void updateCustomer(Customer customer) throws customerNotFoundException, customerAlreadyExistException {
		// we have to see that we have a customer with the id of the customer we get,
		// because we can't change the id
		if (customerReposetory.existsById(customer.getId())) {
			//email can't be like another customer email
			for (Customer c : getAllCustomers()) {
				if((c.getEmail().equals(customer.getEmail()))&& (customer.getId()!= c.getId())) {
					throw new customerAlreadyExistException();
				}
			}
			// now if everything is ok, update the customer
			customerReposetory.save(customer);
		} else
			throw new customerNotFoundException();
	}

	
	// delete customer(we need first to delete the history of this customer purchases)
	public void deleteCustomer(int id) throws customerNotFoundException {
		Customer cust = getOneCustomerById(id);
		// delete all the purchase history of this customer
		for (Coupon coup : cust.getCoupons()) {
			coup.getCustomers().remove(cust);
			couponReposetory.save(coup);
		}
		// now we can delete the customer
		customerReposetory.deleteById(id);
	}

	
	// gives back all the customers
	public List<Customer> getAllCustomers() {
		return customerReposetory.findAll();
	}

	// gives back one customer by his id
	public Customer getOneCustomerById(int id) throws customerNotFoundException {
		return customerReposetory.findById(id).orElseThrow(customerNotFoundException::new);
	}
	// gives back all the coupons of one company
	public List<Coupon> getCompanyCoupons(int id) {
		return couponReposetory.findCouponsByCompanyId(id);
		
	}
}
