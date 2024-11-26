package org.example.service;
import java.util.Scanner;
import java.util.List;
import java.util.Random;

import org.example.model.*;
import org.example.repo.*;
public class ReadingService {
    private final IRepository<Reading> readingRepo;

    private final IRepository<Student> studentRepo;

    private final IRepository<Teacher> teacherRepo;

    public ReadingService(IRepository<Reading> readingRepo, IRepository<Student> studentRepo, IRepository<Teacher> teacherRepo) {
        this.readingRepo = readingRepo;
        this.studentRepo = studentRepo;
        this.teacherRepo = teacherRepo;
    }

    public Student getStudentById(Integer studentId){
        for (Student student : studentRepo.getAll()) {
            if (student.getId().equals(studentId))
                return student;
        }
        return null;
    }

    public Teacher getTeacherById(Integer teacherId){
        for (Teacher teacher : teacherRepo.getAll()) {
            if (teacher.getId().equals(teacherId))
                return teacher;
        }
        return null;
    }

    public Reading getReadingById(Integer readingId){
        for (Reading reading : readingRepo.getAll()) {
            if (reading.getId().equals(readingId))
                return reading;
        }
        return null;
    }

    public void enroll(Integer studentId, Integer readingCourseId) {
        int alreadyEnrolled=0;

        Student student = getStudentById(studentId);
        Reading course = getReadingById(readingCourseId);

        for (Course course1:student.getCourses()){
            if (course1.getId().equals(readingCourseId))
                alreadyEnrolled=1;
        }

        if (alreadyEnrolled==0){
            studentRepo.delete(studentId);
            readingRepo.delete(readingCourseId);
            if (course.getAvailableSlots() > course.getEnrolledStudents().size()) {
                course.getEnrolledStudents().add(student);
                student.getCourses().add(course);
                readingRepo.create(course);
                studentRepo.create(student);
            }
        }

    }

    public void showEnrolledReadingCourses(Integer studentId){
        Student student=getStudentById(studentId);
        for (Course course:student.getCourses())
            if (course.getCourseName().contains("Reading"))
                System.out.println(course);
    }


    public static String[][] appendRow(String[][] originalMatrix, String[] newRow) {
        if (originalMatrix==null||originalMatrix.length==0)
        {
            String[][] newMatrix = new String[1][100];
            newMatrix[0]=newRow;
            return newMatrix;
        }

        int numRows = originalMatrix.length;
        int numCols = originalMatrix[0].length;

        String[][] newMatrix = new String[numRows + 1][numCols];

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                newMatrix[i][j] = originalMatrix[i][j];
            }
        }

        for (int j = 0; j < numCols; j++) {
            newMatrix[numRows][j] = newRow[j];
        }

        return newMatrix;
    }

    public void practiceReading(Integer studentId, Integer courseId){
        System.out.println("\n\nLese den folgenden Text durch und beantworte die Fragen\n\n");

        Student student = getStudentById(studentId);

        Reading course = getReadingById(courseId);

        String[][] exercises=course.getExercises();
        Scanner scanner = new Scanner(System.in);
        String[] exercise;
        int foundCourse=0;
        int mistakeCounter=0;

        for (Course course1:student.getCourses())
            if (course1.getId().equals(courseId))
                foundCourse=1;

        if (foundCourse==1)
        {
            System.out.println(exercises[0][0]);
            System.out.println(exercises[1][0]);
            for (int i=2;i<6;i++)
            {
                exercise=exercises[i];
                System.out.println(exercise[0]+exercise[1]+"\n"+exercise[2]);
                System.out.println("Your answer: ");
                char answer = scanner.nextLine().charAt(0);
                int found=0;

                if (answer=='a' || answer=='b')
                {
                    for (int j=1;j<=2;j++)
                    {
                        if (exercise[j].charAt(0)==answer && exercise[j].charAt(1)=='.')
                            if (exercise[j] == exercise[3])
                            {
                                System.out.println("Correct! " + exercise[3]);
                                found=1;
                                break;
                            }
                    }
                    if (found==0)
                    {
                        System.out.println("Wrong! The right answer was " + exercise[3]);
                        mistakeCounter+=1;
                        if (mistakeCounter==1)
                        {
                            student.setPastMistakes(appendRow(student.getPastMistakes(),exercises[0]));
                            student.setPastMistakes(appendRow(student.getPastMistakes(),exercises[1]));
                            studentRepo.update(student);
                        }
                        student.setPastMistakes(appendRow(student.getPastMistakes(),exercise));
                        studentRepo.update(student);
                    }
                }
                else
                    System.out.println("Invalid choice!");
            }
            System.out.println("\n\n\nPractice complete!\n\n\n");
        }

        if (foundCourse==0)
            System.out.println("\n\n\nYou are not enrolled in this course!");

    }


    public void reviewPastReadingMistakes(Integer studentId){
        Scanner scanner = new Scanner(System.in);

        Student student = getStudentById(studentId);
        String[][] pastMistakes=student.getPastMistakes();
        int numRows = pastMistakes.length;

        for (int i=0;i<numRows;i++){
            if (i==0||i==1)
                System.out.println(pastMistakes[i][0]);
            else{
                String[] exercise = pastMistakes[i];
                System.out.println(exercise[0]+exercise[1]+"\n"+exercise[2]);
                System.out.println("Your answer: ");
                char answer = scanner.nextLine().charAt(0);
                int found=0;

                if (answer=='a' || answer=='b')
                {
                    for (int j=1;j<=2;j++)
                    {
                        if (exercise[j].charAt(0)==answer && exercise[j].charAt(1)=='.')
                            if (exercise[j] == exercise[3])
                            {
                                System.out.println("Correct! " + exercise[3]);
                                found=1;
                                break;
                            }
                    }
                    if (found==0)
                    {
                        System.out.println("Wrong! The right answer was " + exercise[3]);
                    }
                }
                else
                    System.out.println("Invalid choice!");
            }
        }
        System.out.println("\n\n\nReview complete!\n\n\n");
    }

    public void getAvailableCourses() {

        for (Reading reading:readingRepo.getAll())
            System.out.println(reading);
    }
    public List<Student> getAllStudents() {
        return studentRepo.getAll();
    }

    public List<Student> getEnrolledStudents(Integer courseId) {

        Reading course = getReadingById(courseId);
        return course.getEnrolledStudents();
    }

    public void showStudentsEnrolledInReadingCourses(){
        for(Student student:studentRepo.getAll())
            for(Course course:student.getCourses())
                if(course.getCourseName().contains("Reading"))
                {
                    System.out.println(student);
                    break;
                }
    }

    public void removeCourse(Integer courseId, Integer teacherId) {

        Reading course = getReadingById(courseId);
        if (course.getTeacher().getId().equals(teacherId)){
            readingRepo.delete(courseId);
        }
        else{
            System.out.println("You don't have access to this course!");
        }
    }

    public void createOrUpdateReadingCourse(Integer courseId, Integer teacherId, String courseName, Integer maxStudents, Integer exerciseSet){
        int found=0;
        for (Reading course: readingRepo.getAll()){
            if (course.getId().equals(courseId))
            {
                found=1;
                updateReadingCourse(courseId,teacherId,courseName,maxStudents, exerciseSet);
                return;
            }
        }
        if (found==0){
            createReadingCourse(courseId,teacherId,courseName,maxStudents, exerciseSet);
        }
    }

    public void createReadingCourse(Integer courseId, Integer teacherId,String courseName, Integer maxStudents, Integer exerciseSet){

        Teacher teacher=getTeacherById(teacherId);
        Reading r1=new Reading(courseId,courseName,teacher,maxStudents);
        readingRepo.create(r1);
        if(exerciseSet==1)
        {
            String[][] readingExercises = {
                    {"Der Aufbruch\n" + "Franz Kafka","","",""},
                    {"Ich befahl mein Pferd aus dem Stall zu holen. Der Diener verstand mich nicht.\nIch ging selbst in den Stall, sattelte mein Pferd und bestieg es. In der Ferne hörte ich eine Trompete blasen,\nich fragte ihn, was das bedeute. Er wusste nichts und hatte nichts gehört. Beim Tore hielt er mich auf und fragte:\n\"Wohin reitest du, Herr?\" \"Ich weiß es nicht,\" sagte ich, \"nur weg von hier. Immerfort weg von hier, nur so kann ich\nmein Ziel erreichen.\" \"Du kennst also dein Ziel?\" fragte er. \"Ja,\" antwortete ich, \"ich sagte es doch: »Weg-von-hier«,\ndas ist mein Ziel.\" \"Du hast keinen Essvorrat mit,\" sagte er. \"Ich brauche keinen,\" sagte ich, \"die Reise ist so lang,\ndass ich verhungern muss, wenn ich auf dem Weg nichts bekomme. Kein Essvorrat kann mich retten. Es ist ja zum Glück eine\nwahrhaft ungeheure Reise.\"", "", "", ""},
                    {"\n\nDer Diener kann auf alle Fragen des Ich-Erzählers antworten.\n\n", "a. wahr", "b. falsch", "b. falsch"},
                    {"\n\nDer Ich-Erzähler nimmt einen Essvorrat.\n\n", "a. wahr", "b. falsch", "b. falsch"},
                    {"\n\nDer Ich-Erzähler unternimmt eine Reise, deren Dauer undefiniert ist.\n\n", "a. wahr", "b. falsch", "a. wahr"},
                    {"\n\nDie Parabel kann eine Metapher für das Unbekannte des Lebens darstellen.\n\n", "a. wahr", "b. falsch", "a. wahr"},
            };
            r1.setExercises(readingExercises);
        }
        if (exerciseSet==2)
        {
            String[][] readingExercises1 = {
                    {"Der Geier\n" + "Franz Kafka","","",""},
                    {"Es war ein Geier, der hackte in meine Füße. Stiefel und Strümpfe hatte er schon aufgerissen, nun hackte er schon in die Füße selbst.\n" +
                            "Immer schlug er zu, flog dann unruhig mehrmals um mich und setzte dann die Arbeit fort. Es kam ein Herr vorüber, sah ein Weilchen zu und fragte\n" +
                            "dann, warum ich den Geier dulde. »Ich bin ja wehrlos«, sagte ich, »er kam und fing zu hacken an, da wollte ich ihn natürlich wegtreiben, versuchte\n" +
                            "ihn sogar zu würgen, aber ein solches Tier hat große Kräfte, auch wollte er mir schon ins Gesicht springen, da opferte ich lieber die Füße. Nun sind\n" +
                            "sie schon fast zerrissen.« »Daß Sie sich so quälen lassen«, sagte der Herr, »ein Schuß und der Geier ist erledigt.« »Ist das so?« fragte ich, und wollen\n" +
                            "Sie das besorgen?« »Gern«, sagte der Herr, »ich muß nur nach Hause gehn und mein Gewehr holen. Können Sie noch eine halbe Stunde warten?« »Das weiß ich\n" +
                            "nicht«, sagte ich und stand eine Weile starr vor Schmerz, dann sagte ich: »Bitte, versuchen Sie es für jeden Fall.« »Gut«, sagte der Herr, »ich werde\n" +
                            "mich beeilen.« Der Geier hatte während des Gespräches ruhig zugehört und die Blicke zwischen mir und dem Herrn wandern lassen. Jetzt sah ich, daß er\n" +
                            "alles verstanden hatte, er flog auf, weit beugte er sich zurück, um genug Schwung zu bekommen und stieß dann wie ein Speerwerfer den Schnabel durch meinen\n" +
                            "Mund tief in mich. Zurückfallend fühlte ich befreit, wie er in meinem alle Tiefen füllenden, alle Ufer überfließenden Blut unrettbar ertrank.", "", "", ""},
                    {"\n\nDer Ich Erzähler wird von einem Geier angegriffen.\n\n", "a. wahr", "b. falsch", "a. wahr"},
                    {"\n\nDer Herr versucht gleich dem Ich Erzähler zu helfen.\n\n", "a. wahr", "b. falsch", "b. falsch"},
                    {"\n\nDer Ich Erzähler stirbt am Ende.\n\n", "a. wahr", "b. falsch", "a. wahr"},
                    {"\n\nDie Parabel kann bedeuten, dass der Tod in einer verzweifelten Situation eine Befreiung ist.\n\n", "a. wahr", "b. falsch", "a. wahr"},
            };
            r1.setExercises(readingExercises1);
        }
        if(exerciseSet==3)
        {
            String[][] readingExercises2 = {
                    {"Der Steuermann\n" + "Franz Kafka","","",""},
                    {"\"Bin ich nicht Steuermann?\" rief ich. \"du?\" fragte ein dunkler hoch gewachsener Mann und strich sich mit der Hand über die Augen,\n" +
                            "als verscheuche er einen Traum. Ich war am Steuer gestanden in der dunklen Nacht, die schwachbrennende Laterne über meinem Kopf, und nun\n" +
                            "war dieser Mann gekommen und wollte mich beiseiteschieben. Und da ich nicht wich, setzte er mir den Fuß auf die Brust und trat mich\n" +
                            "langsam nieder, während ich noch immer an den Stäben des Steuerrades hing und beim Niederfallen es ganz herumriss. Da aber fasste es der Mann,\n" +
                            "brachte es in Ordnung, mich aber stieß er weg. Doch ich besann mich bald, lief zu der Luke, die in den Mannschaftsraum führte und rief:\n" +
                            "\"Mannschaft! Kameraden! Kommt schnell! Ein Fremder hat mich vom Steuer vertrieben!\" Langsam kamen sie, stiegen auf aus der Schiffstreppe,\n" +
                            "schwankende müde mächtige Gestalten. \"Bin ich der Steuermann?\" fragte ich. Sie nickten, aber Blicke hatten sie nur für den Fremden, im Halbkreis standen\n" +
                            "sie um ihn herum und, als er befehlend sagte: \"Stört mich nicht\", sammelten sie sich, nickten mir zu und zogen wieder die Schiffstreppe hinab.\n" +
                            "Was ist das für Volk! Denken sie auch oder schlurfen sie nur sinnlos über die Erde?", "", "", ""},
                    {"\n\nDie Kameraden sehen den Ich Erzähler als den richtigen Steuermann an.\n\n", "a. wahr", "b. falsch", "b. falsch"},
                    {"\n\nDer Ich Erzähler leistet keinen Gegenstand, vom Fremden ersetzt zu werden.\n\n", "a. wahr", "b. falsch", "b. falsch"},
                    {"\n\nDie Kameraden kämpfen den Fremden ab.\n\n", "a. wahr", "b. falsch", "b. falsch"},
                    {"\n\nDie Parabel kann eine Metapher für die Initiativlosigkeit des einfachen Menschen sein.\n\n", "a. wahr", "b. falsch", "a. wahr"}
            };
            r1.setExercises(readingExercises2);
        }
        if(exerciseSet==4)
        {
            String[][] readingExercises3={
                    {"Gibs auf\n" + "Franz Kafka","","",""},
                    {"Es war sehr früh am Morgen, die Straßen rein und leer, ich ging zum Bahnhof. Als ich eine Turmuhr mit meiner Uhr verglich, sah ich,\n" +
                            "dass es schon viel später war, als ich geglaubt hatte, ich musste mich sehr beeilen, der Schrecken über diese Entdeckung ließ mich im Weg unsicher\n" +
                            "werden, ich kannte mich in dieser Stadt noch nicht sehr gut aus, glücklicherweise war ein Schutzmann in der Nähe, ich lief zu ihm und fragte ihn\n" +
                            "atemlos nach dem Weg. Er lächelte und sagte: \"Von mir willst du den Weg erfahren?\" \"Ja\", sagte ich, \"da ich ihn selbst nicht finden kann.\" \"Gibs auf,\n" +
                            "gibs auf\", sagte er und wandte sich mit einem großen Schwunge ab, so wie Leute, die mit ihrem Lachen allein sein wollen.", "", "", ""},
                    {"\n\nDer Ich Erzähler wandert am Morgen zum Rathaus.\n\n", "a. wahr", "b. falsch", "b. falsch"},
                    {"\n\nDer Schutzmann kann dem Erzähler helfen.\n\n", "a. wahr", "b. falsch", "b. falsch"},
                    {"\n\nDer Schutzmann kennt den Weg nicht.\n\n", "a. wahr", "b. falsch", "a. wahr"},
                    {"\n\nDie Parabel kann eine Metapher für die Kontrollosigkeit des Lebens sein.\n\n", "a. wahr", "b. falsch", "a. wahr"}
            };
            r1.setExercises(readingExercises3);
        }
        if(exerciseSet==5)
        {
            String[][] readingExercises4={
                    {"Kleine Fabel\n" + "Franz Kafka","","",""},
                    {"\"Ach\", sagte die Maus, \"die Welt wird enger mit jedem Tag. Zuerst war sie so breit, dass ich Angst hatte, ich lief weiter und war glücklich,\n" +
                            "dass ich endlich rechts und links in der Ferne Mauern sah, aber diese langen Mauern eilen so schnell aufeinander zu, dass ich schon im letzten\n" +
                            "Zimmer bin, und dort im Winkel steht die Falle, in die ich laufe.\" - \"Du musst nur die Laufrichtung ändern\", sagte die Katze und fraß sie.", "", "", ""},
                    {"\n\nDie Maus wird von der Katze gefressen.\n\n", "a. wahr", "b. falsch", "a. wahr"},
                    {"\n\nDie Umwelt der Maus verengt sich mit jedem Zimmer.\n\n", "a. wahr", "b. falsch", "a. wahr"},
                    {"\n\nDie Parabel kann bedeuten, dass Menschen sich willig das Leben zerstören.\n\n", "a. wahr", "b. falsch", "a. wahr"},
                    {"\n\nDie Parabel kann bedeuten, dass die Komplizierung eine Rettung darstellen kann.\n\n", "a. wahr", "b. falsch", "b. falsch"}
            };
            r1.setExercises(readingExercises4);
        }
        readingRepo.update(r1);

    }

    public void updateReadingCourse(Integer courseId, Integer teacherId,String courseName, Integer maxStudents, Integer exerciseSet){

        Teacher teacher=getTeacherById(teacherId);
        Reading course=getReadingById(courseId);

        course.setCourseName(courseName);
        course.setTeacher(teacher);
        course.setAvailableSlots(maxStudents);
        //Reading r1=new Reading(courseId,courseName,teacher,maxStudents);

        if(exerciseSet==1)
        {
            String[][] readingExercises = {
                    {"Der Aufbruch\n" + "Franz Kafka","","",""},
                    {"Ich befahl mein Pferd aus dem Stall zu holen. Der Diener verstand mich nicht.\nIch ging selbst in den Stall, sattelte mein Pferd und bestieg es. In der Ferne hörte ich eine Trompete blasen,\nich fragte ihn, was das bedeute. Er wusste nichts und hatte nichts gehört. Beim Tore hielt er mich auf und fragte:\n\"Wohin reitest du, Herr?\" \"Ich weiß es nicht,\" sagte ich, \"nur weg von hier. Immerfort weg von hier, nur so kann ich\nmein Ziel erreichen.\" \"Du kennst also dein Ziel?\" fragte er. \"Ja,\" antwortete ich, \"ich sagte es doch: »Weg-von-hier«,\ndas ist mein Ziel.\" \"Du hast keinen Essvorrat mit,\" sagte er. \"Ich brauche keinen,\" sagte ich, \"die Reise ist so lang,\ndass ich verhungern muss, wenn ich auf dem Weg nichts bekomme. Kein Essvorrat kann mich retten. Es ist ja zum Glück eine\nwahrhaft ungeheure Reise.\"", "", "", ""},
                    {"\n\nDer Diener kann auf alle Fragen des Ich-Erzählers antworten.\n\n", "a. wahr", "b. falsch", "b. falsch"},
                    {"\n\nDer Ich-Erzähler nimmt einen Essvorrat.\n\n", "a. wahr", "b. falsch", "b. falsch"},
                    {"\n\nDer Ich-Erzähler unternimmt eine Reise, deren Dauer undefiniert ist.\n\n", "a. wahr", "b. falsch", "a. wahr"},
                    {"\n\nDie Parabel kann eine Metapher für das Unbekannte des Lebens darstellen.\n\n", "a. wahr", "b. falsch", "a. wahr"},
            };
            course.setExercises(readingExercises);
        }
        if (exerciseSet==2)
        {
            String[][] readingExercises1 = {
                    {"Der Geier\n" + "Franz Kafka","","",""},
                    {"Es war ein Geier, der hackte in meine Füße. Stiefel und Strümpfe hatte er schon aufgerissen, nun hackte er schon in die Füße selbst.\n" +
                            "Immer schlug er zu, flog dann unruhig mehrmals um mich und setzte dann die Arbeit fort. Es kam ein Herr vorüber, sah ein Weilchen zu und fragte\n" +
                            "dann, warum ich den Geier dulde. »Ich bin ja wehrlos«, sagte ich, »er kam und fing zu hacken an, da wollte ich ihn natürlich wegtreiben, versuchte\n" +
                            "ihn sogar zu würgen, aber ein solches Tier hat große Kräfte, auch wollte er mir schon ins Gesicht springen, da opferte ich lieber die Füße. Nun sind\n" +
                            "sie schon fast zerrissen.« »Daß Sie sich so quälen lassen«, sagte der Herr, »ein Schuß und der Geier ist erledigt.« »Ist das so?« fragte ich, und wollen\n" +
                            "Sie das besorgen?« »Gern«, sagte der Herr, »ich muß nur nach Hause gehn und mein Gewehr holen. Können Sie noch eine halbe Stunde warten?« »Das weiß ich\n" +
                            "nicht«, sagte ich und stand eine Weile starr vor Schmerz, dann sagte ich: »Bitte, versuchen Sie es für jeden Fall.« »Gut«, sagte der Herr, »ich werde\n" +
                            "mich beeilen.« Der Geier hatte während des Gespräches ruhig zugehört und die Blicke zwischen mir und dem Herrn wandern lassen. Jetzt sah ich, daß er\n" +
                            "alles verstanden hatte, er flog auf, weit beugte er sich zurück, um genug Schwung zu bekommen und stieß dann wie ein Speerwerfer den Schnabel durch meinen\n" +
                            "Mund tief in mich. Zurückfallend fühlte ich befreit, wie er in meinem alle Tiefen füllenden, alle Ufer überfließenden Blut unrettbar ertrank.", "", "", ""},
                    {"\n\nDer Ich Erzähler wird von einem Geier angegriffen.\n\n", "a. wahr", "b. falsch", "a. wahr"},
                    {"\n\nDer Herr versucht gleich dem Ich Erzähler zu helfen.\n\n", "a. wahr", "b. falsch", "b. falsch"},
                    {"\n\nDer Ich Erzähler stirbt am Ende.\n\n", "a. wahr", "b. falsch", "a. wahr"},
                    {"\n\nDie Parabel kann bedeuten, dass der Tod in einer verzweifelten Situation eine Befreiung ist.\n\n", "a. wahr", "b. falsch", "a. wahr"},
            };
            course.setExercises(readingExercises1);
        }
        if(exerciseSet==3)
        {
            String[][] readingExercises2 = {
                    {"Der Steuermann\n" + "Franz Kafka","","",""},
                    {"\"Bin ich nicht Steuermann?\" rief ich. \"du?\" fragte ein dunkler hoch gewachsener Mann und strich sich mit der Hand über die Augen,\n" +
                            "als verscheuche er einen Traum. Ich war am Steuer gestanden in der dunklen Nacht, die schwachbrennende Laterne über meinem Kopf, und nun\n" +
                            "war dieser Mann gekommen und wollte mich beiseiteschieben. Und da ich nicht wich, setzte er mir den Fuß auf die Brust und trat mich\n" +
                            "langsam nieder, während ich noch immer an den Stäben des Steuerrades hing und beim Niederfallen es ganz herumriss. Da aber fasste es der Mann,\n" +
                            "brachte es in Ordnung, mich aber stieß er weg. Doch ich besann mich bald, lief zu der Luke, die in den Mannschaftsraum führte und rief:\n" +
                            "\"Mannschaft! Kameraden! Kommt schnell! Ein Fremder hat mich vom Steuer vertrieben!\" Langsam kamen sie, stiegen auf aus der Schiffstreppe,\n" +
                            "schwankende müde mächtige Gestalten. \"Bin ich der Steuermann?\" fragte ich. Sie nickten, aber Blicke hatten sie nur für den Fremden, im Halbkreis standen\n" +
                            "sie um ihn herum und, als er befehlend sagte: \"Stört mich nicht\", sammelten sie sich, nickten mir zu und zogen wieder die Schiffstreppe hinab.\n" +
                            "Was ist das für Volk! Denken sie auch oder schlurfen sie nur sinnlos über die Erde?", "", "", ""},
                    {"\n\nDie Kameraden sehen den Ich Erzähler als den richtigen Steuermann an.\n\n", "a. wahr", "b. falsch", "b. falsch"},
                    {"\n\nDer Ich Erzähler leistet keinen Gegenstand, vom Fremden ersetzt zu werden.\n\n", "a. wahr", "b. falsch", "b. falsch"},
                    {"\n\nDie Kameraden kämpfen den Fremden ab.\n\n", "a. wahr", "b. falsch", "b. falsch"},
                    {"\n\nDie Parabel kann eine Metapher für die Initiativlosigkeit des einfachen Menschen sein.\n\n", "a. wahr", "b. falsch", "a. wahr"}
            };
            course.setExercises(readingExercises2);
        }
        if(exerciseSet==4)
        {
            String[][] readingExercises3={
                    {"Gibs auf\n" + "Franz Kafka","","",""},
                    {"Es war sehr früh am Morgen, die Straßen rein und leer, ich ging zum Bahnhof. Als ich eine Turmuhr mit meiner Uhr verglich, sah ich,\n" +
                            "dass es schon viel später war, als ich geglaubt hatte, ich musste mich sehr beeilen, der Schrecken über diese Entdeckung ließ mich im Weg unsicher\n" +
                            "werden, ich kannte mich in dieser Stadt noch nicht sehr gut aus, glücklicherweise war ein Schutzmann in der Nähe, ich lief zu ihm und fragte ihn\n" +
                            "atemlos nach dem Weg. Er lächelte und sagte: \"Von mir willst du den Weg erfahren?\" \"Ja\", sagte ich, \"da ich ihn selbst nicht finden kann.\" \"Gibs auf,\n" +
                            "gibs auf\", sagte er und wandte sich mit einem großen Schwunge ab, so wie Leute, die mit ihrem Lachen allein sein wollen.", "", "", ""},
                    {"\n\nDer Ich Erzähler wandert am Morgen zum Rathaus.\n\n", "a. wahr", "b. falsch", "b. falsch"},
                    {"\n\nDer Schutzmann kann dem Erzähler helfen.\n\n", "a. wahr", "b. falsch", "b. falsch"},
                    {"\n\nDer Schutzmann kennt den Weg nicht.\n\n", "a. wahr", "b. falsch", "a. wahr"},
                    {"\n\nDie Parabel kann eine Metapher für die Kontrollosigkeit des Lebens sein.\n\n", "a. wahr", "b. falsch", "a. wahr"}
            };
            course.setExercises(readingExercises3);
        }
        if(exerciseSet==5)
        {
            String[][] readingExercises4={
                    {"Kleine Fabel\n" + "Franz Kafka","","",""},
                    {"\"Ach\", sagte die Maus, \"die Welt wird enger mit jedem Tag. Zuerst war sie so breit, dass ich Angst hatte, ich lief weiter und war glücklich,\n" +
                            "dass ich endlich rechts und links in der Ferne Mauern sah, aber diese langen Mauern eilen so schnell aufeinander zu, dass ich schon im letzten\n" +
                            "Zimmer bin, und dort im Winkel steht die Falle, in die ich laufe.\" - \"Du musst nur die Laufrichtung ändern\", sagte die Katze und fraß sie.", "", "", ""},
                    {"\n\nDie Maus wird von der Katze gefressen.\n\n", "a. wahr", "b. falsch", "a. wahr"},
                    {"\n\nDie Umwelt der Maus verengt sich mit jedem Zimmer.\n\n", "a. wahr", "b. falsch", "a. wahr"},
                    {"\n\nDie Parabel kann bedeuten, dass Menschen sich willig das Leben zerstören.\n\n", "a. wahr", "b. falsch", "a. wahr"},
                    {"\n\nDie Parabel kann bedeuten, dass die Komplizierung eine Rettung darstellen kann.\n\n", "a. wahr", "b. falsch", "b. falsch"}
            };
            course.setExercises(readingExercises4);
        }

        readingRepo.update(course);
    }

    public void viewCourseTaughtByTeacher(Integer teacherId){
        for(Reading course:readingRepo.getAll())
            if (course.getTeacher().getId().equals(teacherId))
                System.out.println(course);
    }

    public void viewMandatoryBooks(Integer studentId, Integer courseId){
        Reading course=getReadingById(courseId);
        for (String book:course.getMandatoryBooks()){
            System.out.println(book);
        }
    }

    public void addMandatoryBook(Integer teacherId, Integer courseId,String book){
        Reading course=getReadingById(courseId);
        if(course.getTeacher().getId().equals(teacherId))
        {
            course.getMandatoryBooks().add(book);
            readingRepo.update(course);
        }
        else System.out.println("You don t have access to this course");
    }



}
