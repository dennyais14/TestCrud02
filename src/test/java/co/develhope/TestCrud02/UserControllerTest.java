package co.develhope.TestCrud02;

import co.develhope.TestCrud02.controller.UserController;
import co.develhope.TestCrud02.entities.User;
import co.develhope.TestCrud02.repositories.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    UserController userController;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    private User createUserTesting() {
        User user = new User();
        user.setSurname("Rossi");
        user.setEmail("grecm123987@gmail.com");
        return user;
    }

    @Test
    void createUser() throws Exception {
        User user = createUserTesting();

        String userJSON = objectMapper.writeValueAsString(user);

        MvcResult result = this.mvc.perform(post("/user/")
                        .contentType(MediaType.APPLICATION_JSON).content(userJSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        User userResult = objectMapper.readValue(result.getResponse().getContentAsString(), User.class);

        assertThat(userResult.getId()).isNotNull();
        assertThat(userResult.getEmail()).isEqualTo(user.getEmail());
        assertThat(userResult.getSurname()).isEqualTo(user.getSurname());

    }

    @Test
    void getAllUsers() throws Exception {
        User user1 = createUserTesting();
        User user2 = createUserTesting();
        user2.setEmail("ferpqo@gmail.com");
        userRepository.save(user1);
        userRepository.save(user2);

        MvcResult result = this.mvc.perform(get("/user/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        List<User> usersList = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<User>>() {
        });

        assertThat(usersList.size());
        assertThat(usersList.get(0).getEmail()).isEqualTo(user1.getEmail());
        assertThat(usersList.get(1).getEmail()).isEqualTo(user2.getEmail());
    }

    @Test
    void updateUser() throws Exception {
        User user = createUserTesting();

        User updatedUser = new User();
        updatedUser.setSurname("keplero");
        updatedUser.setEmail("rigggrgb123@gmail.com");

        String userJSON = objectMapper.writeValueAsString(updatedUser);

        MvcResult result = this.mvc.perform(put("/user/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        User userResult = objectMapper.readValue(result.getResponse().getContentAsString(), User.class);

        assertThat(userResult.getId()).isEqualTo(user.getId());
        assertThat(userResult.getSurname()).isEqualTo(updatedUser.getSurname());
        assertThat(userResult.getEmail()).isEqualTo(updatedUser.getEmail());
    }

    @Test

    void deleteUser() throws Exception {
        User user = createUserTesting();
        userRepository.save(user);

        this.mvc.perform(delete("/user/" + user.getId()))
                .andExpect(status().isOk());

        assertThat(userRepository.findById(user.getId())).isEmpty();
    }
}
