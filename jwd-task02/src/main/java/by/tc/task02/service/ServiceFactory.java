package by.tc.task02.service;

import by.tc.task02.service.impl.DomServiceImpl;

public final class ServiceFactory {
	private static final ServiceFactory instance = new ServiceFactory();

	private final DomService DomService = new DomServiceImpl();
	
	private ServiceFactory() {}

	public DomService getDomService() {

		return DomService;
	}

	public static ServiceFactory getInstance() {
		return instance;
	}

}
