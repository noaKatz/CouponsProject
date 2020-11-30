package com.example.facades;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.example.beans.Category;
import com.example.beans.Company;
import com.example.beans.Coupon;
import com.example.exceptions.NotThisCompanyException;
import com.example.exceptions.couponAlreadyExistException;
import com.example.exceptions.couponNotFoundException;
import com.example.exceptions.timeOverException;
import com.example.reposetories.CompanyReposetory;
import com.example.reposetories.CouponReposetory;
import com.example.reposetories.CustomerReposetory;

@Service
@Scope("prototype")
public class CompanyFacade extends ClientFacade {

	public CompanyFacade(CompanyReposetory companyReposetory, CouponReposetory couponReposetory,
			CustomerReposetory customerReposetory) {
		super(companyReposetory, couponReposetory, customerReposetory);
	}
	//save the company who entered
	private Company company;

	// make a login of the companyFacade
	public boolean login(String email, String password) {
		// check if the company exist
		Company c = companyReposetory.findCompanyByEmailAndPassword(email, password);
		if (c != null) {
			//save the company who entered
			company = c;
			return true;
		} else
			return false;
	}

	// add coupon(receipt a coupon with the company)
	public void addCoupon(Coupon coupon)
			throws couponAlreadyExistException, timeOverException, NotThisCompanyException {
		// check that the new coupon belongs to the company who entered
		if (coupon.getCompany().getId() != this.company.getId()) {
			throw new NotThisCompanyException();
		}
		// we can't add a new coupon to this specific company with exist title
		if (couponReposetory.existsByTitleAndCompanyId(coupon.getTitle(), company.getId())) {
			throw new couponAlreadyExistException();
			//we can't add a new coupon if his end date is already over
		} else if (coupon.getEndDate().before(new Date(Calendar.getInstance().getTimeInMillis()))) {
			throw new timeOverException();
			//we can't add a new coupon if his end date is before his start date
		}else if(coupon.getEndDate().before(coupon.getStartDate())) {
			throw new timeOverException();
			// if everything is ok, add the coupon
		} else {
			couponReposetory.save(coupon);
		}
	}

	// update a coupon details
	public void updateCoupon(Coupon coupon)
			throws couponNotFoundException, couponAlreadyExistException, NotThisCompanyException {
		// check that the coupon belongs to the company who entered
		if (coupon.getCompany().getId() != this.company.getId()) {
			throw new NotThisCompanyException();
		}
		// check that this company  already gave a coupon with this id, we can't change the coupon id
		if (couponReposetory.existsByIdAndCompanyId(coupon.getId(), coupon.getCompany().getId())) {
			for (Coupon c : getAllCoupons()) {
				// we can't add a new coupon to this specific company with exist title
				if ((coupon.getTitle().equals(c.getTitle())) && (coupon.getId() != c.getId())) {
					throw new couponAlreadyExistException();
				}
			}
			// if everything is ok, update the coupon
			couponReposetory.save(coupon);
		} else
			throw new couponNotFoundException();
	}

	// delete a coupon(we need first to delete the purchase history of the coupon)
	public void deleteCoupon(int couponId) throws couponNotFoundException, NotThisCompanyException {
		Coupon coupon = couponReposetory.findById(couponId).orElseThrow(couponNotFoundException::new);
		// check that the coupon belongs to the company who entered
		if (coupon.getCompany().getId() != this.company.getId()) {
			throw new NotThisCompanyException();
		}
		// delete the purchase history of this coupon
		couponReposetory.deleteCouponParchesByCouponId(couponId);
		// delete the coupon
		couponReposetory.deleteById(couponId);
	}

	// gives back the coupons of the company who entered(to the companyFacade)
	public List<Coupon> getAllCoupons() {
		return couponReposetory.findCouponsByCompanyId(company.getId());
	}

	// gives back all the coupons of the company who entered ,by specific category
	public List<Coupon> getAllCouponsByCategory(Category category) {
		return couponReposetory.findAllByCategoryAndCompanyId(category, company.getId());
	}
	
	// gives back the coupons of the company who entered, that cost less than
		// maxPrice
	public List<Coupon> getAllCouponsByMaxPrice(double maxPrice) {
		return couponReposetory.findAllByCompanyIdAndPriceLessThan(company.getId(), maxPrice);
	}

	// gives back the details of the company who entered
	public Company getCompanyDetails() {
		return company;
	}
}
