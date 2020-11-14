package br.com.devdojo.javaClient;

import br.com.devdojo.model.Student;

public class JavaSpringClientTest {
    public static void main(String[] args) {
        Student studentPost = new Student();
        studentPost.setName("Jonh Wick");
        studentPost.setEmail("jonh@pencil.com");
        JavaClientDAO dao = new JavaClientDAO();
        System.out.println(dao.findById(1));
        System.out.println(dao.listAll());
        System.out.println(dao.save(studentPost));
    }
}
