package org.example.repo;
import org.example.model.Exam;
import org.example.model.Student;

import java.util.ArrayList;
import java.util.List;

public class ExamRepository implements IRepository<Exam> {
    private List<Exam> exams;
    private static ExamRepository instance;
    public ExamRepository() {
        this.exams=new ArrayList<>();
    }

    @Override
    public List<Exam> getObjects(){
        return exams;
    }

    @Override
    public void save(Exam entity) {
        exams.add(entity);
    }

    @Override
    public void update(Exam entity, Exam examRepl) {
        int index = exams.indexOf(entity);
        if (index != -1) {
            exams.set(index, examRepl);
        }
    }

    public Exam getById(Integer id){
        for (Exam exam : exams) {
            if (exam.getId() == id)
                return exam;
        }
        return null;
    }

    @Override
    public void delete(Exam object) {
        exams.remove(object);
    }

    public static ExamRepository getInstance() {
        if (instance == null) {
            instance = new ExamRepository();
        }
        return instance;
    }
}



