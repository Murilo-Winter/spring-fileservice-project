package de.exb.platform.cloud.fileservice.client.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileServiceImpl implements FileService {

	@Override
	public InputStreamResource openForReading(final String sessionId, final String path)
			throws FileServiceException {

		try {
			FileInputStream inputStream = new FileInputStream(new File(path));
			return new InputStreamResource(inputStream);
		}
		catch (final FileNotFoundException e) {
			throw new FileServiceException("Could not open file for reading", e);
		}
	}

	@Override
	public List<String> list(final String sessionId, final String path) throws FileServiceException {
		if(!exists(sessionId, path)) {
			throw new FileServiceException("Requested path does not exist");
		}

		final List<String> files = new ArrayList<>();
		for (final File file : (new File(path).listFiles())) {
			files.add(file.getAbsolutePath());
		}
		return files;
	}

	@Override
	public void delete(final String sessionId, final String path, final boolean recursive)
			throws FileServiceException {

		if(!exists(sessionId, path)) {
			throw new FileServiceException("Requested path does not exist");
		}

		final File f = new File(path);
		if (recursive) {
			final Path filePath = Paths.get(f.getAbsolutePath());
			try {
				Files.walkFileTree(filePath, new SimpleFileVisitor<Path>() {

					@Override
					public FileVisitResult visitFile(final Path aFile, final BasicFileAttributes aAttrs)
							throws IOException {
						Files.delete(aFile);
						return FileVisitResult.CONTINUE;
					}

					@Override
					public FileVisitResult postVisitDirectory(final Path aDir, final IOException aExc)
							throws IOException {
						Files.delete(aDir);
						return FileVisitResult.CONTINUE;
					}
				});
			}
			catch (final IOException e) {
				throw new FileServiceException("Cannot delete entries", e);
			}
		}
		else {
			if (!f.delete()) {
				throw new FileServiceException("Cannot delete entry");
			}
		}
	}

	@Override
	public boolean exists(final String sessionId, final String path) {
		return new File(path).exists();
	}

	@Override
	public void save(String sessionId, String path, MultipartFile file) throws IOException {
		if (file == null) {
			throw new FileServiceException("No file was provided");
		}
		if(!exists(sessionId, path)) {
			throw new FileServiceException("Requested path does not exist");
		}

		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		Path filePath = Paths.get(path);
		Path locationPath = filePath.resolve(fileName);
		Files.copy(file.getInputStream(), locationPath, StandardCopyOption.REPLACE_EXISTING);
	}

	@Override
	public long determineLength(String path) {
		Path filePath = Paths.get(path);
		return filePath.toFile().length();
	}

}
