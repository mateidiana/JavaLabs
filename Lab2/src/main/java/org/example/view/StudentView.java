package org.example.view;
import java.util.Scanner;
import java.util.stream.IntStream;
import org.example.controller.ReadingController;
import org.example.service.ReadingService;
import org.example.model.Reading;
import org.example.model.Student;
import org.example.model.Teacher;
import org.example.repo.StudentRepository;
import org.example.repo.ReadingRepository;

public class StudentView {
    private ReadingController readingController;
    public StudentView(ReadingController readingController){
        this.readingController=readingController;
    }

    public void start(){
        Scanner scanner = new Scanner(System.in);
        boolean continueLoop = true;

        while (continueLoop) {
            System.out.print("Select an option:\n\n1. View students\n2. View reading courses\n3. View students enrolled in reading courses\n4. Delete course\n5. Enroll student in a reading course\n0. Exit\n");

            String option = scanner.nextLine();

            switch (option) {
                case "0":
                    continueLoop = false;
                    break;
                case "1":
                    readingController.viewStudents();
                    break;
                case "2":
                    readingController.viewCourses();
                    break;
                case "3":
                    readingController.viewEnrolled(readCourseId(scanner));
                    break;
                case "4":
                    readingController.deleteCourse(readCourseId(scanner));
                    break;
                case "5":
                    readingController.enrollStudent(readStudentId(scanner), readCourseId(scanner));
                    break;
                default:
            }
        }
    }

    private static int readStudentId(Scanner scanner) {
        System.out.println("Enter student ID: ");
        return Integer.parseInt(scanner.nextLine());
    }

    private static int readCourseId(Scanner scanner) {
        System.out.print("Enter course ID: ");
        return Integer.parseInt(scanner.nextLine());
    }

//    public static void main(String[] args) {
//        ReadingRepository readingRepo = createInMemoryCourseRepository();
//        StudentRepository studentRepo = createInMemoryStudentRepository();
//
//        ReadingService readingService = new ReadingService(readingRepo, studentRepo);
//        ReadingController readingController = new ReadingController(readingService);
//
//        StudentView studentView = new StudentView(readingController);
//        studentView.start();
//    }

//    private static StudentRepository createInMemoryStudentRepository() {
//        StudentRepository studentRepo = new StudentRepository();
//        IntStream.range(1, 6).forEach(i -> studentRepo.save(new Student("Student" + i, i)));
//        return studentRepo;
//    }
//
//    private static ReadingRepository createInMemoryCourseRepository() {
//        ReadingRepository readingRepo = new ReadingRepository();
//        readingRepo.save(new Reading(1, "Reading1", new Teacher("Teacher1", 1), 25));
//        readingRepo.save(new Reading(2, "Reading2", new Teacher("Teacher2", 2), 25));
//        readingRepo.save(new Reading(3, "Reading3", new Teacher("Teacher3", 3), 25));
//        readingRepo.save(new Reading(4, "Reading4", new Teacher("Teacher4", 4), 25));
//        readingRepo.save(new Reading(5, "Reading5", new Teacher("Teacher5", 5), 25));
//        readingRepo.getObjects().forEach(System.out::println);
//        return readingRepo;
//    }
}
