package com.example.Web;

import com.example.facades.ClientFacade;

public class OurSession {
private ClientFacade facade;
private long lastAccesed;

public OurSession(ClientFacade facade, long lastAccesed) {
	super();
	this.facade = facade;
	this.lastAccesed = lastAccesed;
}

public ClientFacade getFacade() {
	return facade;
}

public void setFacade(ClientFacade facade) {
	this.facade = facade;
}

public long getLastAccesed() {
	return lastAccesed;
}

public void setLastAccesed(long lastAccesed) {
	this.lastAccesed = lastAccesed;
}



}
