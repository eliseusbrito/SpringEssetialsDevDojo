package br.com.devdojo.javaClient;

import br.com.devdojo.model.PageableResponse;
import br.com.devdojo.model.Student;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class JavaSpringClientTest {
    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplateBuilder()
                .rootUri("http://localhost:8080/v1/protected/students")
                .basicAuthorization("toyo", "devdojo").build();
        RestTemplate restTemplateAdmin = new RestTemplateBuilder()
                .rootUri("http://localhost:8080/v1/admin/students")
                .basicAuthorization("toyo", "devdojo").build();
        Student student = restTemplate.getForObject("/{id}", Student.class, 1);
        System.out.println(student);
//        ResponseEntity<Student> forEntity = restTemplate.getForEntity("/{id}", Student.class, 1);
//        System.out.println(forEntity.getBody());
//        Student[] students = restTemplate.getForObject("/", Student[].class, 1);
//        System.out.println(Arrays.toString(students));
//        ResponseEntity<List<Student>> exchange = restTemplate.exchange("/", HttpMethod.GET, null,
//                new ParameterizedTypeReference<List<Student>>() {
//                });
//        System.out.println(exchange.getBody());
        ResponseEntity<PageableResponse<Student>> exchange = restTemplate.exchange("/?sort=id,desc&sort=name,asc", HttpMethod.GET, null,
                new ParameterizedTypeReference<PageableResponse<Student>>() {
                });
        System.out.println(exchange);
        Student studentPost = new Student();
        studentPost.setName("Jonh Wick");
        studentPost.setEmail("jonh@pencil.com");
        ResponseEntity<Student> exchangePost = restTemplateAdmin.exchange("/",
                HttpMethod.POST, new HttpEntity<>(studentPost, createJSONHeader()), Student.class);
        restTemplateAdmin.postForEntity("/",studentPost,Student.class);
        Student studentPostForObject = restTemplateAdmin.postForObject("/", studentPost, Student.class);
        ResponseEntity<Student> studentResponseEntity = restTemplateAdmin.postForEntity("/", studentPost, Student.class);
        System.out.println(exchangePost);
        System.out.println(studentPostForObject);
        System.out.println(studentResponseEntity);
    }

    private static HttpHeaders createJSONHeader(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

}
