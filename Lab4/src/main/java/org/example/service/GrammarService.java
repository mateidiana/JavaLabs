package org.example.service;

import org.example.model.*;
import org.example.model.Exceptions.ValidationException;
import org.example.repo.IRepository;

import java.util.List;
import java.util.ArrayList;


public class GrammarService {
    private final IRepository<Grammar> grammarRepo;
    private final IRepository<Student> studentRepo;
    private final IRepository<Teacher> teacherRepo;

    private final IRepository<Question> questionRepo;

    public GrammarService(IRepository<Grammar> grammarRepo, IRepository<Student> studentRepo, IRepository<Teacher> teacherRepo, IRepository<Question> questionRepo) {
        this.grammarRepo = grammarRepo;
        this.studentRepo = studentRepo;
        this.teacherRepo = teacherRepo;
        this.questionRepo = questionRepo;
    }

    public Student getStudentById(int studentId){
        for (Student student : studentRepo.getAll()) {
            if (student.getId()==studentId)
                return student;
        }
        return null;
    }

    public Teacher getTeacherById(int teacherId){
        for (Teacher teacher : teacherRepo.getAll()) {
            if (teacher.getId()==teacherId)
                return teacher;
        }
        return null;
    }

    public Grammar getGrammarById(int grammarId){
        for (Grammar grammar : grammarRepo.getAll()) {
            if (grammar.getId()==grammarId)
                return grammar;
        }
        return null;
    }

    public Question getQuestionById(int questionId){
        for (Question question : questionRepo.getAll()) {
            if (question.getId()==questionId)
                return question;
        }
        return null;
    }

    public void enroll(int studentId, int grammarCourseId) {
        int alreadyEnrolled=0;

        Student student = getStudentById(studentId);
        Grammar course = getGrammarById(grammarCourseId);

        for (Grammar grammar:student.getGrammarCourses()){
            if (grammar.getId()==grammarCourseId)
            {
                alreadyEnrolled=1;
                break;
            }

        }

        if (alreadyEnrolled==0){
            studentRepo.delete(studentId);
            grammarRepo.delete(grammarCourseId);
            if (course.getAvailableSlots() > course.getEnrolledStudents().size()) {
                course.getEnrolledStudents().add(student);
                student.getGrammarCourses().add(course);
                grammarRepo.create(course);
                studentRepo.create(student);
            }
        }

    }

    public List<Question> practiceGrammar(int studentId, int courseId){
        int isEnrolled=0;
        Grammar course=getGrammarById(courseId);
        Student student=getStudentById(studentId);
        List<Question> questions=new ArrayList<>();

        for (Grammar grammar: student.getGrammarCourses())
            if (grammar.getId()==courseId)
            {
                isEnrolled=1;
                break;
            }
        if (isEnrolled==0)
            return new ArrayList<>();
        else{
            return course.getExercises();
        }
    }

    public String handleAnswer(int studentId, int questionId, String answer){
        Question question=getQuestionById(questionId);
        if (answer.equals(question.getRightAnswer()))
            return "Correct!";
        else{
            Student student=getStudentById(studentId);
            List<Question> pastMistakes=student.getPastGrammarMistakes();
            pastMistakes.add(question);
            student.setPastGrammarMistakes(pastMistakes);
            studentRepo.update(student);
            return "Wrong!";
        }
    }


    public List<Question> reviewPastGrammarMistakes(int studentId){
        Student student= getStudentById(studentId);
        return student.getPastGrammarMistakes();
    }

    public List<Grammar> showEnrolledGrammarCourses(int studentId){
        Student student=getStudentById(studentId);
        return student.getGrammarCourses();
    }

    public List<Grammar> getAvailableGrammarCourses(){
        return grammarRepo.getAll();
    }

    public List<Student> getAllStudents() {
        return studentRepo.getAll();
    }

    public List<Student> getEnrolledStudents(int courseId) {
        Grammar course = getGrammarById(courseId);
        return course.getEnrolledStudents();
    }

    public List<Student> showStudentsEnrolledInGrammarCourses(){
        List<Student> studentList=new ArrayList<>();
        for(Student student:studentRepo.getAll())
            if (!student.getGrammarCourses().isEmpty())
                studentList.add(student);
        return studentList;

    }

    public boolean removeCourse(int courseId, int teacherId) {
        Grammar course = getGrammarById(courseId);
        if (course.getTeacher()==teacherId){
            grammarRepo.delete(courseId);
            return true;
        }
        else
            return false;
    }

    public void createOrUpdateGrammarCourse(int courseId, int teacherId, String courseName, Integer maxStudents, int exerciseSet){
        int found=0;
        for (Grammar course: grammarRepo.getAll()){
            if (course.getId()==courseId)
            {
                found=1;
                updateGrammarCourse(courseId,teacherId,courseName,maxStudents, exerciseSet);
                return;
            }
        }
        if (found==0){
            createGrammarCourse(courseId,teacherId,courseName,maxStudents, exerciseSet);
        }
    }

    public void createGrammarCourse(int courseId, int teacherId, String courseName, Integer maxStudents, int exerciseSet){
        Grammar g1=new Grammar(courseId,courseName,teacherId,maxStudents);
        grammarRepo.create(g1);
        if(exerciseSet==1)
        {
            int nextId=questionRepo.getAll().size();
            Question q1=new Question(nextId,"Du (brauchen) _ Hilfe.","brauchst");
            questionRepo.create(q1);
            Question q2=new Question(nextId+1,"Ich bin _ Hause.","zu");
            questionRepo.create(q2);
            Question q3=new Question(nextId+2,"Er trägt _.","bei");
            questionRepo.create(q3);

            q1.setReadingId(courseId);
            q2.setReadingId(courseId);
            q3.setReadingId(courseId);
            questionRepo.update(q1);
            questionRepo.update(q2);
            questionRepo.update(q3);
            List<Question> questions=new ArrayList<>();
            questions.add(q1);
            questions.add(q2);
            questions.add(q3);
            g1.setExercises(questions);

        }

        grammarRepo.update(g1);
    }

    public void updateGrammarCourse(int courseId, int teacherId,String courseName, Integer maxStudents, int exerciseSet){
        Grammar course=getGrammarById(courseId);

        course.setCourseName(courseName);
        course.setTeacher(teacherId);
        course.setAvailableSlots(maxStudents);

        if(exerciseSet==1)
        {
            int nextId=questionRepo.getAll().size();
            Question q1=new Question(nextId,"Du (brauchen) _ Hilfe.","brauchst");
            questionRepo.create(q1);
            Question q2=new Question(nextId+1,"Ich bin _ Hause.","zu");
            questionRepo.create(q2);
            Question q3=new Question(nextId+2,"Er trägt _.","bei");
            questionRepo.create(q3);

            q1.setReadingId(courseId);
            q2.setReadingId(courseId);
            q3.setReadingId(courseId);
            questionRepo.update(q1);
            questionRepo.update(q2);
            questionRepo.update(q3);
            List<Question> questions=new ArrayList<>();
            questions.add(q1);
            questions.add(q2);
            questions.add(q3);
            course.setExercises(questions);

        }
        grammarRepo.update(course);
    }

    public List<Grammar> viewGrammarCoursesTaughtByTeacher(int teacherId){
        List<Grammar> taughtCourses=new ArrayList<>();
        for(Grammar course:grammarRepo.getAll())
            if (course.getTeacher()==teacherId)
                taughtCourses.add(course);
        return taughtCourses;

    }


    public void idDataCheck(int id){
        if (id<1)
            throw new ValidationException("Id cannot be less than 1!");
    }

    public void stringDataCheck(String string){
        if (string.isEmpty())
            throw new ValidationException("Name cannot be an empty string!");
    }

    public void intDataCheck(int number){
        if (number<1)
            throw new ValidationException("Number cannot be null!");
    }

}
