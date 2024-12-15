package org.example;
import org.example.repo.*;
import org.example.service.*;
import org.example.model.*;
public class Main {
    public static void main(String[] args) {
        IRepository<Student> studentRepo=new InMemoryRepository<>();
        Student s1=new Student("Student1",1);
        Student s2=new Student("Student2",2);
        Student s3=new Student("Student3",3);
        studentRepo.create(s1);
        studentRepo.create(s2);
        studentRepo.create(s3);
        StudentService studentService=new StudentService(studentRepo);
        for (Student student:studentRepo.getAll())
        {
            System.out.println(student.getName());
        }

        Student studentRepl=new Student("Studentrepl",1); //replaces student with id 1 with studentRepl

        studentRepo.update(studentRepl);

        for (Student student:studentRepo.getAll())
        {
            System.out.println(student.getName());
        }
        Student student=studentService.getStudentById(1);
        System.out.println(student.getName());
    }
}
