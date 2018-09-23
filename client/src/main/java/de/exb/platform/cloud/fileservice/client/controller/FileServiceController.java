package de.exb.platform.cloud.fileservice.client.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import de.exb.platform.cloud.fileservice.api.constants.serviceConstants;
import de.exb.platform.cloud.fileservice.client.service.FileService;

@RestController
@RequestMapping(serviceConstants.FILES_ROOT)
public class FileServiceController {

	@Autowired
	private FileService fileService;

	@GetMapping(serviceConstants.LIST)
	public ResponseEntity list(@RequestParam("path") String path) {
		fileService.list(path);
		return ResponseEntity.ok().build();
	}

	@PostMapping()
	public ResponseEntity upload(@RequestParam("path") String path, @RequestPart("file") MultipartFile file) {
		fileService.upload(path, file);
		return ResponseEntity.ok().build();
	}

	@GetMapping()
	public ResponseEntity download(@RequestParam("path") String path, HttpServletRequest request ) {
		fileService.download(path, request);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping()
	public ResponseEntity remove(@RequestParam("path") String path) {
		fileService.remove(path);
		return ResponseEntity.ok().build();
	}

}
