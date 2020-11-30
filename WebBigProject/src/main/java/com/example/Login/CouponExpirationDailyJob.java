package com.example.Login;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.beans.Coupon;
import com.example.reposetories.CouponReposetory;

@Component
public class CouponExpirationDailyJob extends Thread {
	
	private boolean quit=false; 
	@Autowired
	private CouponReposetory couponReposetory;
	
	
	public CouponExpirationDailyJob() {	}

	@Override
	public void run() {
		while (quit == false) {
			List<Coupon> coupons = couponReposetory.findAll();
			synchronized (coupons) {
				for (Coupon coupon : coupons) {
					if (coupon.getEndDate().before(new Date(Calendar.getInstance().getTimeInMillis()))) {
						couponReposetory.deleteCouponParchesByCouponId(coupon.getId());
						couponReposetory.deleteById((coupon.getId()));
						System.out.println("coupon end date is over! it was deleted");
					}
				}
			}
			
			try {
				Thread.sleep(86_400_000);
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
			}

		}
	}

	public synchronized void stopJob() {
		this.quit = true;
		interrupt();
	}

}
