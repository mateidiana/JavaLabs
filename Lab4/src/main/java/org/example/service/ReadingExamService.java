package org.example.service;
import java.util.*;

import org.example.model.*;
import org.example.repo.IRepository;

public class ReadingExamService {
    private final IRepository<ReadingExam> readingExamRepo;

    private final IRepository<Student> studentRepo;

    private final IRepository<Teacher> teacherRepo;

    private final IRepository<Question> questionRepo;

    private final IRepository<ExamResult> examResultRepo;

    public ReadingExamService(IRepository<ReadingExam> readingExamRepo, IRepository<Student> studentRepo, IRepository<Teacher> teacherRepo, IRepository<Question> questionRepo, IRepository<ExamResult> examResultRepo) {
        this.readingExamRepo = readingExamRepo;
        this.studentRepo = studentRepo;
        this.teacherRepo = teacherRepo;
        this.questionRepo = questionRepo;
        this.examResultRepo=examResultRepo;
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

    public ReadingExam getReadingExamById(int readingId){
        for (ReadingExam readingExam : readingExamRepo.getAll()) {
            if (readingExam.getId()==readingId)
                return readingExam;
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

    public ExamResult getExamResultById(int resultId){
        for (ExamResult result:examResultRepo.getAll())
        {
            if (result.getId()==resultId)
                return result;
        }
        return null;
    }

    public List<Question> takeReadingExam(int studentId, int examId){
        if (getStudentById(studentId).getReadingCourses().isEmpty())
            return new ArrayList<>();
        else return getReadingExamById(examId).getExercises();
    }

    public String handleAnswer(int studentId, int questionId, String answer){
        Question question=getQuestionById(questionId);
        if (answer.equals(question.getRightAnswer()))
            return "Correct!";
        else
            return "Wrong!";
    }

    public List<ExamResult> showReadingExamResults(int studentId){
        List<ExamResult> allResults=getStudentById(studentId).getResults();
        List<ExamResult> allReadingResults=new ArrayList<>();
        for (ExamResult result:allResults)
            if (getReadingExamById(result.getExam())!=null)
                allReadingResults.add(result);
        return allReadingResults;
    }

    public void addResult(int studentId, int examId, Float result){
        int nextId=examResultRepo.getAll().size();
        ExamResult examResult=new ExamResult(nextId, examId, result, studentId);
        examResultRepo.create(examResult);

        Student student=getStudentById(studentId);
        student.getResults().add(examResult);
        studentRepo.update(student);
    }

    public List<ReadingExam> showAllReadingExams(){
        return readingExamRepo.getAll();
    }

    public List<ReadingExam> examsOfATeacher(int teacherId){
        List<ReadingExam> exams=new ArrayList<>();
        for (ReadingExam exam:readingExamRepo.getAll())
            if (exam.getTeacher()==teacherId)
                exams.add(exam);
        return exams;
    }

    //show results of all students on a reading exam of a teacher
    public List<ExamResult> showAllResultsOfTeacherExam(int teacherId, int examId){
        List<ExamResult> allReadingResults=new ArrayList<>();
        if (getReadingExamById(examId).getTeacher()==teacherId){
            for (Student student:studentRepo.getAll())
                for (ExamResult result:student.getResults())
                    if(result.getExam()==examId)
                        allReadingResults.add(result);
            return allReadingResults;
        }
        else return allReadingResults;

    }

    //delete reading exam
    //create/update reading exam
    //filter students by passing grade

}
