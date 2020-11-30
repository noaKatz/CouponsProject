package com.example.Web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.beans.Category;
import com.example.beans.Coupon;
import com.example.exceptions.NotThisCompanyException;
import com.example.exceptions.couponAlreadyExistException;
import com.example.exceptions.couponNotFoundException;
import com.example.exceptions.timeOverException;
import com.example.facades.ClientFacade;
import com.example.facades.CompanyFacade;


@RestController
@RequestMapping("company")
@CrossOrigin(origins = "http://localhost:4200")
public class CompanyController {
	
	@Autowired
	private Map<String, OurSession> sessions;
	
	@PostMapping("addCoupon/{token}")
	public ResponseEntity<?> addCoupon( @PathVariable String token, @RequestBody Coupon coupon){
		OurSession ourSession=sessions.get(token);
		if(ourSession!=null) {
			ClientFacade facade=ourSession.getFacade();
			if(facade instanceof CompanyFacade) {
				CompanyFacade company= (CompanyFacade) facade;
				if(System.currentTimeMillis()-ourSession.getLastAccesed()>1000*60*15) {
					sessions.remove(token);
					return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("time out");
				}
					ourSession.setLastAccesed(System.currentTimeMillis());
				try {
					company.addCoupon(coupon);
					return ResponseEntity.ok(coupon);
				} catch (couponAlreadyExistException | timeOverException | NotThisCompanyException e) {
					return ResponseEntity.badRequest().body(e.getMessage());
				}
			
			}else
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("you are not a comapny");
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("error");	
	}
	
	@PutMapping("updateCoupon/{token}")
	public ResponseEntity<?> updateCoupon( @PathVariable String token, @RequestBody Coupon coupon){
		OurSession ourSession=sessions.get(token);
		if(ourSession!=null) {
			ClientFacade facade=ourSession.getFacade();
			if(facade instanceof CompanyFacade) {
				CompanyFacade company= (CompanyFacade) facade;
				if(System.currentTimeMillis()-ourSession.getLastAccesed()>1000*60*15) {
					sessions.remove(token);
					return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("time out");
				}
					ourSession.setLastAccesed(System.currentTimeMillis());
				try {
					company.updateCoupon(coupon);
					return ResponseEntity.ok(coupon);
				} catch (couponNotFoundException | couponAlreadyExistException | NotThisCompanyException e) {
					return ResponseEntity.badRequest().body(e.getMessage());
				}	
			}else
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("you are not a comapny");
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("error");	
	}
	
	@DeleteMapping("deleteCoupon/{token}/{id}")
	public ResponseEntity<?> deleteCoupon( @PathVariable String token, @PathVariable int id){
		OurSession ourSession=sessions.get(token);
		if(ourSession!=null) {
			ClientFacade facade=ourSession.getFacade();
			if(facade instanceof CompanyFacade) {
				CompanyFacade company= (CompanyFacade) facade;
				if(System.currentTimeMillis()-ourSession.getLastAccesed()>1000*60*15) {
					sessions.remove(token);
					return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("time out");
				}
					ourSession.setLastAccesed(System.currentTimeMillis());
				try {
					company.deleteCoupon(id);
					return ResponseEntity.ok("the coupon deleted succesfully");
				} catch (couponNotFoundException | NotThisCompanyException e) {
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
				}
			}else
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("you are not a comapny");
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("error");	
	}
	
	@GetMapping("companyCoupons/{token}")
	public ResponseEntity<?> getCompanyCoupons( @PathVariable String token){
		OurSession ourSession=sessions.get(token);
		if(ourSession!=null) {
			ClientFacade facade=ourSession.getFacade();
			if(facade instanceof CompanyFacade) {
				CompanyFacade company= (CompanyFacade) facade;
				
				if(System.currentTimeMillis()-ourSession.getLastAccesed()>1000*60*15) {
					sessions.remove(token);
					return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("time out");
				}
					ourSession.setLastAccesed(System.currentTimeMillis());
				return ResponseEntity.ok(company.getAllCoupons());
			}else
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("you are not a comapny");
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("error");
	}
	
	@GetMapping("companyCouponsByCategory/{token}/{category}")
	public ResponseEntity<?> getCompanyCouponsByCategory( @PathVariable String token,@PathVariable Category category){
		OurSession ourSession=sessions.get(token);
		if(ourSession!=null) {
			ClientFacade facade=ourSession.getFacade();
			if(facade instanceof CompanyFacade) {
				CompanyFacade company= (CompanyFacade) facade;
				if(System.currentTimeMillis()-ourSession.getLastAccesed()>1000*60*15) {
					sessions.remove(token);
					return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("time out");
				}
					ourSession.setLastAccesed(System.currentTimeMillis());
				return ResponseEntity.ok(company.getAllCouponsByCategory(category));
			}else
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("you are not a comapny");
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("error");
	}
	
	@GetMapping("companyCouponsByMaxPrice/{token}/{max}")
	public ResponseEntity<?> getCompanyCouponsByMaxPrice( @PathVariable String token, @PathVariable double max){
		OurSession ourSession=sessions.get(token);
		if(ourSession!=null) {
			ClientFacade facade=ourSession.getFacade();
			if(facade instanceof CompanyFacade) {
				CompanyFacade company= (CompanyFacade) facade;
				if(System.currentTimeMillis()-ourSession.getLastAccesed()>1000*60*15) {
					sessions.remove(token);
					return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("time out");
				}
					ourSession.setLastAccesed(System.currentTimeMillis());
				return ResponseEntity.ok(company.getAllCouponsByMaxPrice(max));
			}else
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("you are not a comapny");
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("error");
	}
	
	@GetMapping("companyInfo/{token}")
	public ResponseEntity<?> getCompanyInfo(@PathVariable String token){
		OurSession ourSession=sessions.get(token);
		if(ourSession!=null) {
			ClientFacade facade=ourSession.getFacade();
			if(facade instanceof CompanyFacade) {
				CompanyFacade company= (CompanyFacade) facade;
				if(System.currentTimeMillis()-ourSession.getLastAccesed()>1000*60*15) {
					sessions.remove(token);
					return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("time out");
				}
					ourSession.setLastAccesed(System.currentTimeMillis());
				return ResponseEntity.ok(company.getCompanyDetails());
			}else
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("you are not a comapny");
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("error");
	}

}
