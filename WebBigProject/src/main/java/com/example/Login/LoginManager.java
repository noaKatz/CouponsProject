package com.example.Login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import com.example.exceptions.loginFailedException;
import com.example.facades.AdminFacade;
import com.example.facades.ClientFacade;
import com.example.facades.CompanyFacade;
import com.example.facades.CustomerFacade;

@Service
public class LoginManager {
	@Autowired
	private ConfigurableApplicationContext ctx;

	public ClientFacade login(String email, String password, ClientType clientType) throws loginFailedException {
		switch (clientType) {
		case Administrator:
			AdminFacade adminFacade=ctx.getBean(AdminFacade.class);
			if(adminFacade.login(email, password))
				return adminFacade;
			break;
		case Company:
			CompanyFacade companyFacade=ctx.getBean(CompanyFacade.class);
			if(companyFacade.login(email, password))
				return companyFacade;
			break;
		case Customer:
			CustomerFacade customerFacade=ctx.getBean(CustomerFacade.class);
			if(customerFacade.login(email, password)) 
				return customerFacade;
	
			break;
	
				
		}
		throw new loginFailedException();
		
	}
}
