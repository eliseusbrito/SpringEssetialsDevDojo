package br.com.devdojo.javaClient;

import br.com.devdojo.model.Student;

public class JavaSpringClientTest {
    public static void main(String[] args) {
        Student studentPost = new Student();
        studentPost.setName("Jonh Wick 2");
        studentPost.setEmail("jonh@pencil.com");
        studentPost.setId(15L);
        JavaClientDAO dao = new JavaClientDAO();
//        System.out.println(dao.findById(1));
//        System.out.println(dao.listAll());
//        System.out.println(dao.save(studentPost));
//        dao.update(studentPost);
        dao.delete(14);
    }
}
