package org.example.repo;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class FileRepository<T> implements IRepository<T> {
    private final String filePath;
    private static final Map<Class<?>, FileRepository<?>> instances = new HashMap<>();


    public FileRepository(String filePath) {
        this.filePath = filePath;
    }


    @Override
    public void save(T obj) {
        doInFile(data -> data.add(obj));
    }


    public T get(Integer id) {
        return readDataFromFile().get(id);
    }


    @Override
    public void update(T obj, T replace) {
        List<T> objects=readDataFromFile();
        int index= objects.indexOf(obj);
        doInFile(data -> data.set(index,replace));
    }


    @Override
    public void delete(T obj) {
        doInFile(data -> data.remove(obj));
    }

    @Override
    public List<T> getObjects() {
        return readDataFromFile();
    }



    private void doInFile(Consumer<List<T>> function) {
        List<T> data = readDataFromFile();
        function.accept(data);
        writeDataToFile(data);
    }


    private List<T> readDataFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (List<T>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }


    private void writeDataToFile(List<T> data) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static synchronized <T> FileRepository<T> getInstance(Class<T> type, String filePath) {

        if (!instances.containsKey(type)) {
            instances.put(type, new FileRepository<>(filePath));
        }
        return (FileRepository<T>) instances.get(type);
    }
}
