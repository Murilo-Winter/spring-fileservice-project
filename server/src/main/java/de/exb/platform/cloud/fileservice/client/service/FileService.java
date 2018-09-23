package de.exb.platform.cloud.fileservice.client.service;

import java.io.IOException;
import java.util.List;
import javax.validation.constraints.NotNull;

import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

	public InputStreamResource openForReading(@NotNull final String aSessionId, @NotNull final String aPath)
			throws FileServiceException;

	@NotNull
	public List<String> list(@NotNull final String aSessionId, @NotNull final String aPath)
			throws FileServiceException;

	public void delete(@NotNull final String aSessionId, @NotNull final String aPath, final boolean aRecursive)
			throws FileServiceException;

	public boolean exists(@NotNull final String aSessionId, @NotNull final String aPath)
			throws FileServiceException;

	public void save(@NotNull final String aSessionId, @NotNull String aPath, @NotNull final MultipartFile file)
			throws IOException;

	long determineLength(String path);
}
