package com.example.facades;


import com.example.reposetories.CompanyReposetory;
import com.example.reposetories.CouponReposetory;
import com.example.reposetories.CustomerReposetory;

public abstract class ClientFacade {

	protected CompanyReposetory companyReposetory;
	protected CouponReposetory couponReposetory;
	protected CustomerReposetory customerReposetory;
	
	public ClientFacade(CompanyReposetory companyReposetory, CouponReposetory couponReposetory,
			CustomerReposetory customerReposetory) {
		super();
		this.companyReposetory = companyReposetory;
		this.couponReposetory = couponReposetory;
		this.customerReposetory = customerReposetory;
	} 
	
	public abstract boolean login(String email, String password);

}
