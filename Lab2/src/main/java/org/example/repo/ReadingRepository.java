package org.example.repo;
import org.example.model.Reading;

import java.util.ArrayList;
import java.util.List;

public class ReadingRepository implements IRepository<Reading> {
    private List<Reading> readingCourses;
    private static ReadingRepository instance;
    private ReadingRepository() {
        this.readingCourses=new ArrayList<>();
    }

    @Override
    public List<Reading> getObjects(){
        return readingCourses;
    }

    @Override
    public void save(Reading entity) {
        readingCourses.add(entity);
    }

    @Override
    public void update(Reading entity, Reading ReadingRepl) {
        int index = readingCourses.indexOf(entity);
        if (index != -1) {
            readingCourses.set(index, ReadingRepl);
        }
    }

    @Override
    public void delete(Reading object) {
        readingCourses.remove(object);
    }

    public static ReadingRepository getInstance() {
        if (instance == null) {
            instance = new ReadingRepository();
        }
        return instance;
    }
}
