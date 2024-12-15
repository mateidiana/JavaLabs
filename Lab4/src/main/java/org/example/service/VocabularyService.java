package org.example.service;

import org.example.model.*;
import org.example.repo.IRepository;

import java.util.List;
import java.util.ArrayList;

public class VocabularyService {
    private final IRepository<Vocabulary> vocabRepo;
    private final IRepository<Student> studentRepo;
    private final IRepository<Teacher> teacherRepo;

    private final IRepository<Word> wordRepo;


    public VocabularyService(IRepository<Vocabulary> vocabRepo, IRepository<Student> studentRepo, IRepository<Teacher> teacherRepo, IRepository<Word> wordRepo) {
        this.vocabRepo = vocabRepo;
        this.studentRepo = studentRepo;
        this.teacherRepo = teacherRepo;
        this.wordRepo = wordRepo;
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

    public Vocabulary getVocabById(int vocabId){
        for (Vocabulary vocab : vocabRepo.getAll()) {
            if (vocab.getId()==vocabId)
                return vocab;
        }
        return null;
    }

    public Word getWordById(int wordId){
        for (Word word : wordRepo.getAll()) {
            if (word.getId()==wordId)
                return word;
        }
        return null;
    }

    public void enroll(int studentId, int vocabCourseId) {
        int alreadyEnrolled=0;

        Student student = getStudentById(studentId);
        Vocabulary course = getVocabById(vocabCourseId);

        for (Vocabulary vocab:student.getVocabCourses()){
            if (vocab.getId()==vocabCourseId)
            {
                alreadyEnrolled=1;
                break;
            }

        }

        if (alreadyEnrolled==0){
            studentRepo.delete(studentId);
            vocabRepo.delete(vocabCourseId);
            if (course.getAvailableSlots() > course.getEnrolledStudents().size()) {
                course.getEnrolledStudents().add(student);
                student.getVocabCourses().add(course);
                vocabRepo.create(course);
                studentRepo.create(student);
            }
        }

    }

    public List<Word> practiceVocab(int studentId, int courseId){
        int isEnrolled=0;
        Vocabulary course=getVocabById(courseId);
        Student student=getStudentById(studentId);
        List<Word> questions=new ArrayList<>();

        for (Vocabulary vocab: student.getVocabCourses())
            if (vocab.getId()==courseId)
            {
                isEnrolled=1;
                break;
            }
        if (isEnrolled==0)
            return new ArrayList<>();
        else{
            return course.getWords();
        }
    }

    public String handleAnswer(int studentId, int wordId, String answer){
        Word word=getWordById(wordId);
        if (answer.equals(word.getMeaning()))
            return "Correct!";
        else{
            Student student=getStudentById(studentId);
            List<Word> pastMistakes=student.getPastVocabMistakes();
            pastMistakes.add(word);
            student.setPastVocabMistakes(pastMistakes);
            studentRepo.update(student);
            return "Wrong!";
        }
    }


    public List<Word> reviewPastVocabMistakes(int studentId){
        Student student= getStudentById(studentId);
        return student.getPastVocabMistakes();
    }

    public List<Vocabulary> showEnrolledVocabCourses(int studentId){
        Student student=getStudentById(studentId);
        return student.getVocabCourses();
    }

    public List<Vocabulary> getAvailableVocabCourses(){
        return vocabRepo.getAll();
    }

    public List<Student> getAllStudents() {
        return studentRepo.getAll();
    }

    public List<Student> getEnrolledStudents(int courseId) {
        Vocabulary course = getVocabById(courseId);
        return course.getEnrolledStudents();
    }

    public List<Student> showStudentsEnrolledInVocabCourses(){
        List<Student> studentList=new ArrayList<>();
        for(Student student:studentRepo.getAll())
            if (!student.getVocabCourses().isEmpty())
                studentList.add(student);
        return studentList;

    }

    public boolean removeCourse(int courseId, int teacherId) {
        Vocabulary course = getVocabById(courseId);
        if (course.getTeacher()==teacherId){
            vocabRepo.delete(courseId);
            return true;
        }
        else
            return false;
    }

    public void createOrUpdateVocabCourse(int courseId, int teacherId, String courseName, Integer maxStudents, int exerciseSet){
        int found=0;
        for (Vocabulary course: vocabRepo.getAll()){
            if (course.getId()==courseId)
            {
                found=1;
                updateVocabCourse(courseId,teacherId,courseName,maxStudents, exerciseSet);
                return;
            }
        }
        if (found==0){
            createVocabCourse(courseId,teacherId,courseName,maxStudents, exerciseSet);
        }
    }

    public void createVocabCourse(int courseId, int teacherId, String courseName, Integer maxStudents, int exerciseSet){
        Vocabulary v1=new Vocabulary(courseId,courseName,teacherId,maxStudents);
        vocabRepo.create(v1);
        if(exerciseSet==1)
        {
            int nextId=wordRepo.getAll().size();
            Word w1=new Word(nextId,"tree","Baum");
            wordRepo.create(w1);
            Word w2=new Word(nextId+1,"flower","Blume");
            wordRepo.create(w2);
            Word w3=new Word(nextId+2,"house","Haus");
            wordRepo.create(w3);

            w1.setVocabId(courseId);
            w2.setVocabId(courseId);
            w3.setVocabId(courseId);
            wordRepo.update(w1);
            wordRepo.update(w2);
            wordRepo.update(w3);
            List<Word> words=new ArrayList<>();
            words.add(w1);
            words.add(w2);
            words.add(w3);
            v1.setWords(words);

        }

        vocabRepo.update(v1);
    }

    public void updateVocabCourse(int courseId, int teacherId,String courseName, Integer maxStudents, int exerciseSet){
        Vocabulary course=getVocabById(courseId);

        course.setCourseName(courseName);
        course.setTeacher(teacherId);
        course.setAvailableSlots(maxStudents);

        if(exerciseSet==1)
        {
            int nextId=wordRepo.getAll().size();
            Word w1=new Word(nextId,"tree","Baum");
            wordRepo.create(w1);
            Word w2=new Word(nextId+1,"flower","Blume");
            wordRepo.create(w2);
            Word w3=new Word(nextId+2,"house","Haus");
            wordRepo.create(w3);

            w1.setVocabId(courseId);
            w2.setVocabId(courseId);
            w3.setVocabId(courseId);
            wordRepo.update(w1);
            wordRepo.update(w2);
            wordRepo.update(w3);
            List<Word> words=new ArrayList<>();
            words.add(w1);
            words.add(w2);
            words.add(w3);
            course.setWords(words);

        }
        vocabRepo.update(course);
    }

    public List<Vocabulary> viewVocabularyCoursesTaughtByTeacher(int teacherId){
        List<Vocabulary> taughtCourses=new ArrayList<>();
        for(Vocabulary course:vocabRepo.getAll())
            if (course.getTeacher()==teacherId)
                taughtCourses.add(course);
        return taughtCourses;

    }


}
