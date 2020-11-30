package com.example.reposetories;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.beans.Category;
import com.example.beans.Coupon;

public interface CouponReposetory extends JpaRepository<Coupon, Integer> {
	//used to find all the company coupons
	List<Coupon> findCouponsByCompanyId(int id);
	//used in add coupon
	boolean existsByTitleAndCompanyId(String title, int id);
	//used in update coupon
	boolean existsByIdAndCompanyId(int id, int companyId);
	//used at get coupons of this company from one category
	List<Coupon> findAllByCategoryAndCompanyId(Category category, int id);
	//used at get coupons of this company until max price
	List<Coupon> findAllByCompanyIdAndPriceLessThan(int id, double maxPrice);
	
	//delete history, delete a coupon
	@Modifying
	@Transactional
	@Query(nativeQuery = true, value="delete from customers_coupons where coupons_id = ?")
	void deleteCouponParchesByCouponId(int couponId);
	
	
	
	//used in the delete company
	@Modifying
	@Transactional
	@Query(nativeQuery = true, value="delete from coupons where company_id = ?")
	void deleteCompanyCoupons(int companyId);
	
	
	
}
