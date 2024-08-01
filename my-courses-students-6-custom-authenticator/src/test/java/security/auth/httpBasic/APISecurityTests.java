package security.auth.httpBasic;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = HttpBasicApplication.class)
@AutoConfigureMockMvc
public class APISecurityTests {


    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testSecuredEndpointWithAuthenticatedUser() throws Exception {
        mockMvc.perform(get("/api/hello-user")
                        .header(
                                "user",
                                "om"
                        ).header(
                                "roles",
                                "USER"
                        )
                )
                .andExpect(status().isOk());
    }

//    @Test
//    public void testSecuredEndpointWithUnauthenticatedUser() throws Exception {
//        mockMvc.perform(get("/api/hello-user"))
//                .andExpect(status().isUnauthorized());
//    }

    @Test
    public void testUnauthorizedEndpoint() throws Exception {
        mockMvc.perform(get("/api/hello-faculty")
                        .header(
                                "user",
                                "om"
                        ).header(
                                "roles",
                                "USER"
                        )
                )
                .andExpect(status().isForbidden());
    }

    @Test
    public void testAdminEndpointWithAdminRole() throws Exception {
        mockMvc.perform(get("/api/hello-faculty")
                        .header(
                                "user",
                                "professor"
                        ).header(
                                "roles",
                                "FACULTY"
                        )
                )
                .andExpect(status().isOk());
    }

    @Test
    public void testAdminEndpointWithUserRole() throws Exception {
        mockMvc.perform(get("/api/hello-faculty")
                        .header(
                                "user",
                                "om"
                        ).header(
                                "roles",
                                "USER"
                        )
                )
                .andExpect(status().isForbidden());
    }

    @Test
    public void testUserEndpointWithFacultyRole() throws Exception {
        mockMvc.perform(get("/api/hello-user")
                        .header(
                                "user",
                                "professor"
                        ).header(
                                "roles",
                                "FACULTY"
                        )
                )
                .andExpect(status().isOk());
    }


}

