package com.example.Web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.beans.Category;
import com.example.beans.Coupon;
import com.example.exceptions.couponAlreadyExistException;
import com.example.exceptions.couponNotFoundException;
import com.example.exceptions.timeOverException;
import com.example.facades.ClientFacade;
import com.example.facades.CustomerFacade;

@RestController
@RequestMapping("customer")
@CrossOrigin(origins = "http://localhost:4200")
public class CustomerController {
	@Autowired
	private Map<String, OurSession> sessions;

	@PostMapping("buyCoupon/{token}")
	public ResponseEntity<?> purchaseCoupon(@PathVariable String token, @RequestBody Coupon coupon) {
		OurSession ourSession = sessions.get(token);
		if (ourSession != null) {
			ClientFacade facade = ourSession.getFacade();
			if (facade instanceof CustomerFacade) {
				CustomerFacade customer = (CustomerFacade) facade;
				if (System.currentTimeMillis() - ourSession.getLastAccesed() > 1000 * 60 * 15) {
					sessions.remove(token);
					return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("time out");
				}
				ourSession.setLastAccesed(System.currentTimeMillis());
				try {
					customer.purchaseCouponByCustomer(coupon);
					return ResponseEntity.ok(coupon);
				} catch (couponAlreadyExistException | timeOverException | couponNotFoundException e) {
					return ResponseEntity.badRequest().body(e.getMessage());
				}

			} else
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("you are not a customer");
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("error");
	}

	@GetMapping("customerCoupons/{token}")
	public ResponseEntity<?> getCustomerCoupons(@PathVariable String token) {
		OurSession ourSession = sessions.get(token);
		if (ourSession != null) {
			ClientFacade facade = ourSession.getFacade();
			if (facade instanceof CustomerFacade) {
				CustomerFacade customer = (CustomerFacade) facade;
				if (System.currentTimeMillis() - ourSession.getLastAccesed() > 1000 * 60 * 15) {
					sessions.remove(token);
					return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("time out");
				}
				ourSession.setLastAccesed(System.currentTimeMillis());
				return ResponseEntity.ok(customer.getAllCouponsOfCustomer());
			} else
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("you are not a customer");
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("error");
	}

	@GetMapping("customerCouponsByCategory/{token}/{category}")
	public ResponseEntity<?> getCustomerCouponsByCategory(@PathVariable String token, @PathVariable Category category) {
		OurSession ourSession = sessions.get(token);
		if (ourSession != null) {
			ClientFacade facade = ourSession.getFacade();
			if (facade instanceof CustomerFacade) {
				CustomerFacade customer = (CustomerFacade) facade;
				if (System.currentTimeMillis() - ourSession.getLastAccesed() > 1000 * 60 * 15) {
					sessions.remove(token);
					return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("time out");
				}
				ourSession.setLastAccesed(System.currentTimeMillis());
				return ResponseEntity.ok(customer.getCouponsOfCustomerByCategory(category));
			} else
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("you are not a customer");
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("error");
	}

	@GetMapping("customerCouponsByMaxPrice/{token}/{max}")
	public ResponseEntity<?> getCustomerCouponsByMaxPrice(@PathVariable String token, @PathVariable double max) {
		OurSession ourSession = sessions.get(token);
		if (ourSession != null) {
			ClientFacade facade = ourSession.getFacade();
			if (facade instanceof CustomerFacade) {
				CustomerFacade customer = (CustomerFacade) facade;
				if (System.currentTimeMillis() - ourSession.getLastAccesed() > 1000 * 60 * 15) {
					sessions.remove(token);
					return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("time out");
				}
				ourSession.setLastAccesed(System.currentTimeMillis());
				return ResponseEntity.ok(customer.getCouponsOfCustomerByMaxPrice(max));
			} else
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("you are not a customer");
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("error");
	}

	@GetMapping("allCoupons/{token}")
	public ResponseEntity<?> getAllCoupons(@PathVariable String token) {
		OurSession ourSession = sessions.get(token);
		if (ourSession != null) {
			ClientFacade facade = ourSession.getFacade();
			if (facade instanceof CustomerFacade) {
				CustomerFacade customer = (CustomerFacade) facade;
				if (System.currentTimeMillis() - ourSession.getLastAccesed() > 1000 * 60 * 15) {
					sessions.remove(token);
					return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("time out");
				}
				ourSession.setLastAccesed(System.currentTimeMillis());
				return ResponseEntity.ok(customer.getAllCoupons());
			} else
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("you are not a customer");
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("error");
	}

	@GetMapping("customerInfo/{token}")
	public ResponseEntity<?> getCustomerInfo(@PathVariable String token) {
		OurSession ourSession = sessions.get(token);
		if (ourSession != null) {
			ClientFacade facade = ourSession.getFacade();
			if (facade instanceof CustomerFacade) {
				CustomerFacade customer = (CustomerFacade) facade;
				if (System.currentTimeMillis() - ourSession.getLastAccesed() > 1000 * 60 * 15) {
					sessions.remove(token);
					return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("time out");
				}
				ourSession.setLastAccesed(System.currentTimeMillis());
				return ResponseEntity.ok(customer.getCustomerDitails());
			} else
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("you are not a customer");
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("error");
	}

}
