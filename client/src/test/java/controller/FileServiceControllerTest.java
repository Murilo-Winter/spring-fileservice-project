package controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import de.exb.platform.cloud.fileservice.api.constants.serviceConstants;
import de.exb.platform.cloud.fileservice.client.FileServiceApplication;


@RunWith(SpringRunner.class)
@SpringBootTest(classes=FileServiceApplication.class)
@AutoConfigureMockMvc
public class FileServiceControllerTest {

	@Autowired
	private MockMvc mockMvc;

	/**
	 * Sample test that tests the integration between client/server.
	 * As the server is started in port 8080, and client in 8090, both applications can run at the same time.
	 * Note: the server must be running for this test to succeed;
	 */
	@Test
	public void listFilesShouldRetrieveContent() throws Exception {
		MvcResult result = mockMvc
				.perform(get(serviceConstants.FILES_ROOT + serviceConstants.LIST + serviceConstants.PATH_PARAM + "/"))
				.andExpect(status().isOk()).andReturn();
	}

}
