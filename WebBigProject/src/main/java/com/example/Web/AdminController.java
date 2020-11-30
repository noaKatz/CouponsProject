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

import com.example.beans.Company;
import com.example.beans.Customer;
import com.example.exceptions.companyAlreadyExistException;
import com.example.exceptions.companyNameCantChangeException;
import com.example.exceptions.companyNotFoundException;
import com.example.exceptions.customerAlreadyExistException;
import com.example.exceptions.customerNotFoundException;
import com.example.facades.AdminFacade;
import com.example.facades.ClientFacade;

@RestController
@RequestMapping("admin")
@CrossOrigin(origins = "http://localhost:4200")
public class AdminController {
	@Autowired
	private Map<String, OurSession> sessions;
	
	@PostMapping("/addCompany/{token}")
	public ResponseEntity<?> addCompany( @PathVariable String token, @RequestBody Company company){
		OurSession ourSession=sessions.get(token);
		if(ourSession!=null) {
			ClientFacade facade=ourSession.getFacade();
			if(facade instanceof AdminFacade) {
				AdminFacade admin= (AdminFacade) facade;
				if(System.currentTimeMillis()-ourSession.getLastAccesed()>1000*60*15) {
					sessions.remove(token);
					return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("time out");
				}
					ourSession.setLastAccesed(System.currentTimeMillis());
				try {
					admin.addCompany(company);
					return ResponseEntity.ok(company);
				} catch (companyAlreadyExistException e) {
					return ResponseEntity.badRequest().body(e.getMessage());
				}
					
			}else
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("you are not an admin");
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("error");
		
	}
	
	
	@PutMapping("updateCompany/{token}")
	public ResponseEntity<?> updateCompany( @PathVariable String token, @RequestBody Company company){
		OurSession ourSession=sessions.get(token);
		if(ourSession!=null) {
			ClientFacade facade=ourSession.getFacade();
			if(facade instanceof AdminFacade) {
				AdminFacade admin= (AdminFacade) facade;
				if(System.currentTimeMillis()-ourSession.getLastAccesed()>1000*60*15) {
					sessions.remove(token);
					return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("time out");
				}
					ourSession.setLastAccesed(System.currentTimeMillis());
				try {
					admin.updateCompany(company);
					return ResponseEntity.ok(company);
				} catch (companyNameCantChangeException | companyAlreadyExistException e) {
					return ResponseEntity.badRequest().body(e.getMessage());
				}
			}else
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("you are not an admin");
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("error");
	}
	
	@DeleteMapping("deleteCompany/{token}/{id}")
	public ResponseEntity<?> deleteCompany(@PathVariable String token,@PathVariable int id ){
		OurSession ourSession=sessions.get(token);
		if(ourSession!=null) {
			ClientFacade facade=ourSession.getFacade();
			if(facade instanceof AdminFacade) {
				AdminFacade admin= (AdminFacade) facade;
				if(System.currentTimeMillis()-ourSession.getLastAccesed()>1000*60*15) {
					sessions.remove(token);
					return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("time out");
				}
					ourSession.setLastAccesed(System.currentTimeMillis());
				try {
					admin.deleteCompany(id);
					return ResponseEntity.ok("the comapny deleted succesfully");
				} catch (companyNotFoundException e) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
				}
			
			}else
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("you are not an admin");
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("error");
	}
	
	@GetMapping("allCompanies/{token}")
	public ResponseEntity<?> getAllCompanies(@PathVariable String token){
		OurSession ourSession=sessions.get(token);
		if(ourSession!=null) {
			ClientFacade facade=ourSession.getFacade();
			if(facade instanceof AdminFacade) {
				AdminFacade admin= (AdminFacade) facade;
				if(System.currentTimeMillis()-ourSession.getLastAccesed()>1000*60*15) {
					sessions.remove(token);
					return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("time out");
				}
					ourSession.setLastAccesed(System.currentTimeMillis());
				return ResponseEntity.ok(admin.getAllCompanies());	
			}else
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("you are not an admin");
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("error");
	}
	
	@GetMapping("oneCompany/{token}/{id}")
	public ResponseEntity<?> getOneCompany(@PathVariable String token, @PathVariable int id){
		OurSession ourSession=sessions.get(token);
		if(ourSession!=null) {
			ClientFacade facade=ourSession.getFacade();
			if(facade instanceof AdminFacade) {
				AdminFacade admin= (AdminFacade) facade;
				if(System.currentTimeMillis()-ourSession.getLastAccesed()>1000*60*15) {
					sessions.remove(token);
					return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("time out");
				}
					ourSession.setLastAccesed(System.currentTimeMillis());
				try {
					Company c=admin.getOneCompanyById(id);
					return ResponseEntity.ok(c);
				} catch (companyNotFoundException e) {
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
				}	
			}else
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("you are not an admin");
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("error");
	}
	
	@GetMapping("companyCoupons/{token}/{id}")
	public ResponseEntity<?> companyCoupons(@PathVariable String token,@PathVariable int id){
		OurSession ourSession=sessions.get(token);
		if(ourSession!=null) {
			ClientFacade facade=ourSession.getFacade();
			if(facade instanceof AdminFacade) {
				AdminFacade admin= (AdminFacade) facade;
				if(System.currentTimeMillis()-ourSession.getLastAccesed()>1000*60*15) {
					sessions.remove(token);
					return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("time out");
				}
					ourSession.setLastAccesed(System.currentTimeMillis());
				return ResponseEntity.ok(admin.getCompanyCoupons(id));	
			}else
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("you are not an admin");
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("error");
	}
	
	@PostMapping("addCustomer/{token}")
	public ResponseEntity<?> addCustomer( @PathVariable String token, @RequestBody Customer customer){
		OurSession ourSession=sessions.get(token);
		if(ourSession!=null) {
			ClientFacade facade=ourSession.getFacade();
			if(facade instanceof AdminFacade) {
				AdminFacade admin= (AdminFacade) facade;
				if(System.currentTimeMillis()-ourSession.getLastAccesed()>1000*60*15) {
					sessions.remove(token);
					return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("time out");
				}
					ourSession.setLastAccesed(System.currentTimeMillis());
				try {
				admin.addCustomer(customer);
					return ResponseEntity.ok(customer);
				} catch (customerAlreadyExistException e) {
					return ResponseEntity.badRequest().body(e.getMessage());
				}
			}else
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("you are not an admin");
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("error");	
	}
	
	@PutMapping("updateCustomer/{token}")
	public ResponseEntity<?> updateCustomer( @PathVariable String token, @RequestBody Customer customer){
		OurSession ourSession=sessions.get(token);
		if(ourSession!=null) {
			ClientFacade facade=ourSession.getFacade();
			if(facade instanceof AdminFacade) {
				AdminFacade admin= (AdminFacade) facade;
				if(System.currentTimeMillis()-ourSession.getLastAccesed()>1000*60*15) {
					sessions.remove(token);
					return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("time out");
				}
					ourSession.setLastAccesed(System.currentTimeMillis());
				try {
					admin.updateCustomer(customer);
					return ResponseEntity.ok(customer);
				} catch (customerNotFoundException | customerAlreadyExistException e) {
					return ResponseEntity.badRequest().body(e.getMessage());
				}
			}else
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("you are not an admin");
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("error");
	}

	@DeleteMapping("deleteCustomer/{token}/{id}")
	public ResponseEntity<?> deleteCustomer(@PathVariable String token,@PathVariable int id ){
		OurSession ourSession=sessions.get(token);
		if(ourSession!=null) {
			ClientFacade facade=ourSession.getFacade();
			if(facade instanceof AdminFacade) {
				AdminFacade admin= (AdminFacade) facade;
				if(System.currentTimeMillis()-ourSession.getLastAccesed()>1000*60*15) {
					sessions.remove(token);
					return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("time out");
				}
					ourSession.setLastAccesed(System.currentTimeMillis());
				try {
					admin.deleteCustomer(id);
					return ResponseEntity.ok("the customer deleted succesfully");
				} catch (customerNotFoundException e) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
				}
			
			}else
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("you are not an admin");
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("error");
	}
	
	
	@GetMapping("allCustomers/{token}")
	public ResponseEntity<?> getAllCustomers(@PathVariable String token){
		OurSession ourSession=sessions.get(token);
		if(ourSession!=null) {
			ClientFacade facade=ourSession.getFacade();
			if(facade instanceof AdminFacade) {
				AdminFacade admin= (AdminFacade) facade;
				if(System.currentTimeMillis()-ourSession.getLastAccesed()>1000*60*15) {
					sessions.remove(token);
					return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("time out");
				}
					ourSession.setLastAccesed(System.currentTimeMillis());
				return ResponseEntity.ok(admin.getAllCustomers());	
			}else
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("you are not an admin");
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("error");
	}
	
	@GetMapping("oneCustomer/{token}/{id}")
	public ResponseEntity<?> getOneCustomer(@PathVariable String token, @PathVariable int id){
		OurSession ourSession=sessions.get(token);
		if(ourSession!=null) {
			ClientFacade facade=ourSession.getFacade();
			if(facade instanceof AdminFacade) {
				AdminFacade admin= (AdminFacade) facade;
				if(System.currentTimeMillis()-ourSession.getLastAccesed()>1000*60*15) {
					sessions.remove(token);
					return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("time out");
				}
					ourSession.setLastAccesed(System.currentTimeMillis());
				try {
					Customer c=admin.getOneCustomerById(id);
					return ResponseEntity.ok(c);
				} catch (customerNotFoundException e) {
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
				}	
			}else
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("you are not an admin");
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("error");
	}

}
