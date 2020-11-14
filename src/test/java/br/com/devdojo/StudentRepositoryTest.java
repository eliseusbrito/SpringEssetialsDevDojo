package br.com.devdojo;

import br.com.devdojo.model.Student;
import br.com.devdojo.repository.StudentRepository;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) //Para teste no banco de dados persistido
public class StudentRepositoryTest {
    @Autowired
    private StudentRepository studentRepository;
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void createdShouldPersistData() {
        Student student = new Student("Willian", "willian@devdojo.com.br");
        this.studentRepository.save(student);
        assertThat(student.getId()).isNotNull();
        assertThat(student.getName()).isEqualTo("Willian");
        assertThat(student.getEmail()).isEqualTo("willian@devdojo.com.br");
    }

    @Test
    public void deleteShouldRemoveData() {
        Student student = new Student("Willian", "willian@devdojo.com.br");
        this.studentRepository.save(student);
        studentRepository.delete(student);
        assertThat(studentRepository.findOne(student.getId())).isNull();
    }

    @Test
    public void updateShouldChangeAndPersistData() {
        Student student = new Student("Willian", "willian@devdojo.com.br");
        this.studentRepository.save(student);
        student.setName("Willian222");
        student.setEmail("willian222@devdojo.com.br");
        this.studentRepository.save(student);
        student = this.studentRepository.findOne(student.getId());
        assertThat(student.getName()).isEqualTo("Willian222");
        assertThat(student.getEmail()).isEqualTo("willian222@devdojo.com.br");
    }

    @Test
    public void findByNameIgnoreCaseContainingShouldIgnoreCase() {
        Student student = new Student("Willian", "willian@devdojo.com.br");
        Student student2 = new Student("willian", "willian222@devdojo.com.br");
        this.studentRepository.save(student);
        this.studentRepository.save(student2);
        List<Student> studentList = studentRepository.findByNameIgnoreCaseContaining("willian");
        assertThat(studentList.size()).isEqualTo(2);
    }

    @Test
    public void createWhenNameIsNullThrowConstraintViolationException() {
        thrown.expect(ConstraintViolationException.class);
        thrown.expectMessage("O campo nome do estudante é obrigatório");
        this.studentRepository.save(new Student());
    }

    @Test
    public void createWhenEmailIsNullThrowConstraintViolationException() {
        thrown.expect(ConstraintViolationException.class);
        Student student = new Student();
        student.setName("Willian");
        this.studentRepository.save(new Student());
    }

    @Test
    public void createWhenEmailIsNotValidThrowConstraintViolationException() {
        thrown.expect(ConstraintViolationException.class);
        thrown.expectMessage("Digite um email válido");
        Student student = new Student();
        student.setName("Willian");
        student.setEmail("Willian");
        this.studentRepository.save(student);
    }

}
