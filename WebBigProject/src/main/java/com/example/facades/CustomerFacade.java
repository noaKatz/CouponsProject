package com.example.facades;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.example.beans.Category;
import com.example.beans.Coupon;
import com.example.beans.Customer;
import com.example.exceptions.couponAlreadyExistException;
import com.example.exceptions.couponNotFoundException;
import com.example.exceptions.timeOverException;
import com.example.reposetories.CompanyReposetory;
import com.example.reposetories.CouponReposetory;
import com.example.reposetories.CustomerReposetory;

@Service
@Scope("prototype")
public class CustomerFacade extends ClientFacade {

	public CustomerFacade(CompanyReposetory companyReposetory, CouponReposetory couponReposetory,
			CustomerReposetory customerReposetory) {
		super(companyReposetory, couponReposetory, customerReposetory);
	}

//save the customer who entered
	private Customer customer;

	// make a login of customerFacade
	public boolean login(String email, String password) {
		// check if customer exist- if he exist return true
		Customer c = customerReposetory.findCustomerByEmailAndPassword(email, password);
		if (c != null) {
			// save the customer who entered
			customer = c;
			return true;
		} else
			return false;
	}

	// add a purchase of coupon by the customer who entered
	public void purchaseCouponByCustomer(Coupon coupon)
			throws couponAlreadyExistException, couponNotFoundException, timeOverException {
		Date now = new Date(Calendar.getInstance().getTimeInMillis());
		// this coupon can't purchased by the customer more than one time
		customer = customerReposetory.findById(customer.getId()).get();
		for (Coupon c : customer.getCoupons()) {
			if (c.getId() == coupon.getId()) {
				throw new couponAlreadyExistException();
			}
		}
		// we need at least one coupon before the purchase
		if (coupon.getAmount() == 0) {
			throw new couponNotFoundException();
			// the coupon have to be valid
		} else if (coupon.getEndDate().before(now)) {
			throw new timeOverException();
		} else {
			// change the amount of the coupon to be less 1
			// if everything is ok we can make the purchase of the coupon by the customer
			customer.getCoupons().add(coupon);
			customerReposetory.save(customer);
			int a = coupon.getAmount() - 1;
			coupon.setAmount(a);
			couponReposetory.save(coupon);
		}
	}

	// gives back all the customer coupons(who entered)
	public Set<Coupon> getAllCouponsOfCustomer() {
		customer = customerReposetory.findById(customer.getId()).get();
		return customer.getCoupons();
	}

	// gives back all the coupons of this customer by one category
	public List<Coupon> getCouponsOfCustomerByCategory(Category category) {
		customer = customerReposetory.findById(customer.getId()).get();
		// all the customer coupons
		Set<Coupon> all = customer.getCoupons();
		List<Coupon> relevant = new ArrayList<Coupon>();
		for (Coupon coupon : all) {
			// check if the coupon is from the category we looking for
			if (coupon.getCategory().equals(category)) {
				relevant.add(coupon);
			}
		}
		// gives back only the relevant coupons
		return relevant;
	}

	// gives back all the coupons of this customer that there price is less than
	// maxPrice
	public List<Coupon> getCouponsOfCustomerByMaxPrice(double maxPrice) {
		customer = customerReposetory.findById(customer.getId()).get();
		// all the customer coupons
		Set<Coupon> all = customer.getCoupons();
		List<Coupon> relevent = new ArrayList<Coupon>();
		for (Coupon coupon : all) {
			// check if the coupon price is less than the max price
			if (coupon.getPrice() <= maxPrice) {
				relevent.add(coupon);
			}
		}
		// gives back only the relevant coupons
		return relevent;
	}

	// gives back all the coupons
	public List<Coupon> getAllCoupons() {
		return couponReposetory.findAll();
	}

	// gives back all the details of the customer who entered
	public Customer getCustomerDitails() {
		customer = customerReposetory.findById(customer.getId()).get();
		return customer;
	}
}
