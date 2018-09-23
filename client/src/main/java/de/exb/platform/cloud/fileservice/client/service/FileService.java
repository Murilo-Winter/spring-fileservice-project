package de.exb.platform.cloud.fileservice.client.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import de.exb.platform.cloud.fileservice.api.constants.serviceConstants;

/**
 * This class would have the call to the fileservice-server endpoints, retrieving the required data
 * and doing something with it, or performing the required operation.
 * A fallback could be set in place, so that it can handle instabilities(for instance, using Hystrix).
 * The tests for this class would consist in more or less what was done in the fileservice-server.
 * However, in this case, we could have both server and client running, and checking that the integration succeeds.
 *
 * A Service Discovery service (like Eureka) could be used to manage multiple instances of the fileservice-server,
 * effectively balancing the load between them.
 *
 */
@Service
public class FileService {

	@Autowired
	private RestTemplateBuilder restTemplatebuilder;

	public HttpStatus list(String path) {
		RestTemplate restTemplate = restTemplatebuilder.build();
		return restTemplate.getForEntity("http://localhost:8080" + serviceConstants.FILES_ROOT + serviceConstants.LIST + serviceConstants.PATH_PARAM + path, String.class).getStatusCode();
	}

	public void upload(String path, MultipartFile file) {

	}

	public void download(String path, HttpServletRequest request) {

	}

	public void remove(String path) {

	}

}
