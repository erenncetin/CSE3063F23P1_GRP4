import java.util.ArrayList;

public class Lecturer extends Person{
    private Id lecturerId;
    private ArrayList<Course> givenCourses = new ArrayList<>();

    public Lecturer(String FirstName, String LastName, Id lecturerId,Password password) {
        super(FirstName, LastName,password);
        this.lecturerId = lecturerId;
    }

    public Id getLecturerId() {
        return lecturerId;
    }

    public ArrayList<Course> getGivenCourses() {
        return givenCourses;
    }

    public void setGivenCourses(ArrayList<Course> givenCourses) {
        this.givenCourses = givenCourses;
    }
}