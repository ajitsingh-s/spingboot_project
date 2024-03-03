package com.rest.api.server;


import org.springframework.beans.factory.annotation.Value;

import com.act21.demo.bom.Message;
import com.blazesoft.server.base.NdServerException;
import com.blazesoft.server.base.NdServiceException;
import com.blazesoft.server.base.NdServiceSessionException;
import com.blazesoft.server.config.NdServerConfig;
import com.blazesoft.server.deploy.NdStatelessServer;
import com.blazesoft.server.local.NdLocalServerException;


public class Server extends NdStatelessServer {
	
	@Value("${rule.service.name}")
	private String ruleServiceName;
	@Value("${entry.point}")
	private String entryPoint;
	
	public Server(NdServerConfig serverConfig) throws NdLocalServerException {
		super(serverConfig);
	}

	public Message eP_function(Message arg0)
			throws NdServerException, NdServiceException, NdServiceSessionException {
		// Build the argument list
		System.out.println("Rule Service Name -> "+ruleServiceName+", entryPoint -> "+entryPoint);
		Object[] applicationArgs = new Object[1];
		applicationArgs[0] = arg0;

		// Invoke the service and returns its result, if any.
		Message retVal = (Message) invokeService(ruleServiceName, entryPoint, null, applicationArgs);
		System.out.println("entry point called ->" + retVal);
		return retVal;
	}

}
