package controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import de.exb.platform.cloud.fileservice.client.FileServiceApplication;
import de.exb.platform.cloud.fileservice.api.constants.serviceConstants;

/**
 * Additional tests could include error cases (invalid path, null file and so on) and security
 * For testing purposes, the storage path is defined inside the controller folder.
 * Note that FileServiceImpl could also be tested individually, but this test covers most of it's
 * functionality, already (except for corner cases)
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes=FileServiceApplication.class)
@AutoConfigureMockMvc
public class FileServiceControllerTest {

	private static final String FILE1 = "Sample1.txt";

	private static final String FILE2 = "Sample2.txt";

	private static final String FILE3 = "Sample3.txt";

	private static final String STORAGE_PATH = Paths.get("")
			.toAbsolutePath().toString() + "\\src\\test\\java\\resources\\";

	@Autowired
	private MockMvc mockMvc;

	@BeforeClass
	public static void classSetup() {
		createFile(FILE1, "File Content 1");
		createFile(FILE2, "File Content 2");
		removeFile(FILE3);
	}

	@AfterClass
	public static void classTeardown() {
		removeFile(FILE1);
		removeFile(FILE2);
		removeFile(FILE3);
	}

	@Test
	@Ignore
	public void unauthorizedTest() throws Exception {
		this.mockMvc
				.perform(get("/"))
				.andExpect(status().isUnauthorized());
	}

	@Test
	public void listFilesShouldRetrieveContent() throws Exception {
		MvcResult result = mockMvc
				.perform(get(serviceConstants.FILES_ROOT + serviceConstants.LIST + serviceConstants.PATH_PARAM + STORAGE_PATH))
				.andExpect(status().isOk()).andReturn();
		Assert.assertNotEquals(0, result.getResponse().getContentAsString().length());
	}

	@Test
	public void uploadFileShouldCreateNewFile() throws Exception {
		MockMultipartFile multipartFile = new MockMultipartFile("file", FILE3, "text/plain", "File Content 3".getBytes());

		this.mockMvc
				.perform(multipart(serviceConstants.FILES_ROOT + serviceConstants.PATH_PARAM + STORAGE_PATH)
						.file(multipartFile))
				.andExpect(status().isOk());
		Assert.assertTrue(fileExists(STORAGE_PATH + FILE3));
	}

	@Test
	public void downloadShouldRetrieveFile() throws Exception {
		MvcResult result = mockMvc
				.perform(get(serviceConstants.FILES_ROOT + serviceConstants.PATH_PARAM + STORAGE_PATH + FILE1).contentType(MediaType.TEXT_PLAIN))
				.andExpect(status().isOk()).andReturn();
		Assert.assertNotEquals(0, result.getResponse().getContentAsString().length());
	}

	@Test
	public void deleteShouldRemoveFile() throws Exception {
		mockMvc.perform(delete(serviceConstants.FILES_ROOT + serviceConstants.PATH_PARAM + STORAGE_PATH + FILE2 + serviceConstants.RECURSIVE_PARAM + "true")
				.contentType(MediaType.TEXT_PLAIN)).andExpect(status().isOk());
		Assert.assertFalse(fileExists(STORAGE_PATH + FILE2));
	}

	private static void createFile(String filename, String content) {
		try {
			List<String> lines = Arrays.asList(content);
			Path file = Paths.get(STORAGE_PATH, filename);
			Files.write(file, lines, Charset.forName("UTF-8"));
		}
		catch (IOException e) {
		}
	}

	private static void removeFile(String filename) {
		try {
			Path file = Paths.get(STORAGE_PATH, filename);
			Files.delete(file);
		}
		catch (IOException e) {
		}
	}

	private static boolean fileExists(String path) {
		File f = new File(path);
		return (f.exists() && !f.isDirectory());
	}

}
