package br.com.devdojo;

import br.com.devdojo.model.Student;
import br.com.devdojo.repository.StudentRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static java.util.Arrays.asList;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class StudentEndpointTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @LocalServerPort
    private int port;
    @MockBean
    private StudentRepository studentRepository;
    @Autowired
    private MockMvc mockMvc;

    @TestConfiguration
    static class Config {
        @Bean
        public RestTemplateBuilder restTemplateBuilder() {
            return new RestTemplateBuilder().basicAuthorization("toyo", "devdojo");
        }
    }

    @Test
    public void listStudentWhenUsernameAndPasswordAreIncorrectShouldReturnStatusCode401() {
        System.out.println(port);
        restTemplate = restTemplate.withBasicAuth("1", "1");
        ResponseEntity<String> response = restTemplate.getForEntity("/v1/protected/students", String.class);
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(401);
    }

    @Test
    public void getStudentByIdWhenUsernameAndPasswordAreIncorrectShouldReturnStatusCode401() {
        System.out.println(port);
        restTemplate = restTemplate.withBasicAuth("1", "1");
        ResponseEntity<String> response = restTemplate.getForEntity("/v1/protected/students/1", String.class);
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(401);
    }

    @Test
    public void listStudentsWhenUsernameAndPasswordAreCorrectShouldReturnStatusCode200() {
        List<Student> students = asList(new Student(1L, "Legolas", "legolas@lotr.com"),
                new Student(2L, "Aragorn", "aragorn@lotr.com"));
        BDDMockito.when(studentRepository.findAll()).thenReturn(students);
        ResponseEntity<String> response = restTemplate.getForEntity("/v1/protected/students", String.class);
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }
    @Test
    public void getStudentByIdWhenUsernameAndPasswordAreCorrectShouldReturnStatusCode200() {
        Student student = new Student(1L, "Legolas", "legolas@lotr.com");
        BDDMockito.when(studentRepository.findOne(student.getId())).thenReturn(student);
        ResponseEntity<Student> response = restTemplate.getForEntity("/v1/protected/students/1", Student.class);
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }
    @Test
    public void getStudentByIdWhenUsernameAndPasswordAreCorrectAndStudentDoesNotExistShouldReturnStatusCode404() {
        ResponseEntity<Student> response = restTemplate.getForEntity("/v1/protected/students/1", Student.class, -1);
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(404);
    }
}
