package com.rest.api.controller;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.act21.demo.bom.Message;
import com.blazesoft.server.base.NdServerException;
import com.blazesoft.server.base.NdServerId;
import com.blazesoft.server.base.NdServiceException;
import com.blazesoft.server.base.NdServiceSessionException;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.rest.api.server.Server;

@RestController
public class SpringRestController {

	@Autowired
	private Server server;

	@GetMapping("/test")
	public String test() {
		System.out.println("Test successfully");
		return "SUCCESS";
	}

	@GetMapping("/hello")
	public NdServerId hello() {
		return server.getServerId();
	}

	@RequestMapping(value = { "/xmlReq" }, method = { RequestMethod.POST }, produces = { "application/xml" })
	public String xmlReq(@RequestBody String msg) throws JsonMappingException, JsonProcessingException,
			NdServiceSessionException, NdServiceException, NdServerException {
		ObjectMapper mapper = new ObjectMapper();
		// mapper.setSerializationInclusion(Include.NON_NULL);
		JsonNode jsonNode = mapper.readTree(msg);
		String xmlString = new XmlMapper().writeValueAsString(jsonNode);
		return xmlString;
	}

//	@PostMapping("/run")
	@RequestMapping(value = { "/run" }, method = { RequestMethod.POST }, produces = { "application/json" })
	public String executeBRE(@RequestBody String msg) throws JsonMappingException, JsonProcessingException,
			NdServiceSessionException, NdServiceException, NdServerException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		Message message = mapper.readValue(msg, Message.class);
		Message outMsg = server.eP_function(message);
		ObjectMapper mapper1 = new ObjectMapper();
		String retMsg = mapper1.writeValueAsString(outMsg);
		return retMsg;
	}

	@RequestMapping(value = { "/runJsonXML" }, method = { RequestMethod.POST }, produces = { "application/xml" })
	public String executeBREJsonXML(@RequestBody String msg) throws JsonMappingException, JsonProcessingException,
			NdServiceSessionException, NdServiceException, NdServerException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		Message message = mapper.readValue(msg, Message.class);
		Message outMsg = server.eP_function(message);
		ObjectMapper jsonMapper = new ObjectMapper();
		JsonNode jsonNode = jsonMapper.valueToTree(outMsg);
		String retMsg = new XmlMapper().writeValueAsString(jsonNode);
		return retMsg;
	}

	@RequestMapping(value = { "/runXML" }, method = { RequestMethod.POST }, produces = { "application/xml" })
	public String executeBREXML(@RequestBody String msg) throws JsonMappingException, JsonProcessingException,
			NdServiceSessionException, NdServiceException, NdServerException, FileNotFoundException {
		try {
			Message message = new Message();
			JacksonXmlModule xmlModule = new JacksonXmlModule();
			xmlModule.setDefaultUseWrapper(false);
			ObjectMapper xmlMapper = new XmlMapper(xmlModule);
			xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			xmlMapper.setDateFormat(df);
			FileReader fr = new FileReader(new File("./Request/Request.xml"));

			message = xmlMapper.readValue(msg, Message.class);
			Message outMsg = server.eP_function(message);
			ObjectMapper mapper = new ObjectMapper();
			JsonNode jsonNode = mapper.valueToTree(outMsg);
			String retMsg = new XmlMapper().writeValueAsString(jsonNode);

			fr.close();
			return retMsg;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}
}
