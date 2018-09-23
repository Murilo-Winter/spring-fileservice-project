package de.exb.platform.cloud.fileservice.client.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
import de.exb.platform.cloud.fileservice.client.service.FileServiceException;

@RestController
@RequestMapping(serviceConstants.FILES_ROOT)
public class FileServiceController {

	private static final String SESSION_ID = "1234";

	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

	@Autowired
	private FileService fileService;

	/**
	 * Currently returns a list of paths. Could be adjusted to return in a JSON format.
	 */
	@GetMapping(serviceConstants.LIST)
	public ResponseEntity<List<String>> list(@RequestParam("path") String path) throws FileServiceException {
		logger.info("Listing files for path {}", path);
		List<String> filesList = fileService.list(SESSION_ID, path);
		return ResponseEntity.ok(filesList);
	}

	@PostMapping()
	public ResponseEntity upload(@RequestParam("path") String path, @RequestPart("file") MultipartFile file)
			throws IOException {

		logger.info("Uploading file to path {}", path);
		fileService.save(SESSION_ID, path, file);
		return ResponseEntity.ok("File successfully uplaoded");
	}

	@GetMapping()
	public ResponseEntity<Resource> download(@RequestParam("path") String path, HttpServletRequest request )
			throws IOException {

		logger.info("Download file from path {}", path);
		InputStreamResource resource = fileService.openForReading(SESSION_ID, path);
		MediaType contentType = MediaType.valueOf(request.getServletContext().getMimeType(path));
		long contentLength = fileService.determineLength(path);

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=\"%s\"", path))
				.contentLength(contentLength)
				.contentType(contentType)
				.body(resource);
	}

	@DeleteMapping()
	public ResponseEntity remove(@RequestParam("path") String path, @RequestParam("recursive") boolean recursive)
			throws FileServiceException {

		logger.info("Removing file from path {}", path);
		fileService.delete(SESSION_ID, path, recursive);
		return ResponseEntity.ok("File successfully removed");
	}

}
