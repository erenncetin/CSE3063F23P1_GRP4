// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.

import Draft_Classes.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

public class SystemClass {
    private SystemDomain domain;
    private Person currentUser;

    public SystemClass() throws JSONException, IOException {
        domain = new SystemDomain();
        UserInfo me = new UserInfo("o150120042","nida123");
        login(me);
    }

    public void login(UserInfo userInfo) {
        boolean userFound = false;
        if (userInfo.getUsername().charAt(0) == 'o') {
            ArrayList<Student> students = domain.getStudents();
            for (Student student : students) {
                if (("o" + student.getStudentId().getId()).equals(userInfo.getUsername()) &&
                        student.getPassword().getPassword().equals(userInfo.getPassword())) {
                    setCurrentUser(student);
                    userFound = true;
                    // this.getUserInterface().setCurrentPage(PageType.MAIN_MENU_PAGE_STUDENT);
                }
            }
        } else if (userInfo.getUsername().charAt(0) == 'a') {
            ArrayList<Advisor> advisors = domain.getAdvisors();
            for (Advisor advisor : advisors) {
                if (("o" + advisor.getId().getId()).equals(userInfo.getUsername()) &&
                        advisor.getPassword().getPassword().equals(userInfo.getPassword())) {
                    setCurrentUser(advisor);
                    userFound = true;
                    // this.getUserInterface().setCurrentPage(PageType.MAIN_MENU_PAGE_ADVISOR);
                }
            }
        } if (!userFound) {
            System.out.println("Username/Password incorrect.");
        }
    }

    public void logout() {
        setCurrentUser(null);
    }

    public void exit() throws JSONException, IOException {
        updateStudentJSON();
        updateCourseStudentData();
        System.exit(0);
    }
    public void updateCourseStudentData() throws IOException, JSONException {
        String content = null;
        content = new String(Files.readAllBytes(Path.of("src\\JSON_Files\\courses.json")));
        JSONObject jsonObject = new JSONObject(content);
        JSONArray courseJSON = jsonObject.getJSONArray("courses");

        int j=0;
        for(int i=0; i<courseJSON.length();i++){
            JSONObject curr = courseJSON.getJSONObject(i);
            if(curr.getBoolean("hasSession")){
                JSONArray currCourseJSON = courseJSON.getJSONObject(i).getJSONArray("session");
                int k=0;
                for(k=0;k<currCourseJSON.length();k++){
                    JSONObject currSession = currCourseJSON.getJSONObject(k);
                    currSession.put("studentList",studentToJsonArray(domain.getCourses().get(j).getStudentList()));

                }
                j=j+k;
            }
            else{
                curr.put("studentList",studentToJsonArray(domain.getCourses().get(i).getStudentList()));
                j++;
            }

        }
        Files.write(Paths.get("src\\JSON_Files\\courses.json"),jsonObject.toString(4).getBytes(),StandardOpenOption.TRUNCATE_EXISTING);
    }
    public String[] studentToJsonArray(ArrayList<Student> students){
        String[] studentIds = new String[students.size()];
        for(int i=0; i<students.size();i++){
            studentIds[i] = students.get(i).getStudentId().getId();
        }
        return  studentIds;
    }

    public void updateStudentJSON() throws JSONException, IOException {
        for (int i = 0; i < domain.getStudents().size(); i++) {
            String studentId = domain.getStudents().get(i).getStudentId().getId();
            Path path = Path.of("src\\JSON_Files\\" + studentId + ".json");
            String content = new String(Files.readAllBytes(path));
            JSONObject jsonStudent = new JSONObject(content);
            JSONObject registration = jsonStudent.getJSONObject("registration");
            JSONArray selectedCourses = registration.getJSONArray("selectedcourses");
            JSONArray approvedCourses = registration.getJSONArray("approvedcourses");
            ArrayList<Course> selected = domain.getStudents().get(i).getSelectedCourses();
            ArrayList<Course> approved = domain.getStudents().get(i).getApprovedCourses();
            for (int j = 0; j < selected.size(); j++) {
                selectedCourses.put(j, selected.get(i).getCourseID().getId());
            }
            for (int k = 0; k < approved.size(); k++) {
                approvedCourses.put(k, approved.get(k).getCourseID().getId());
            }
            jsonStudent.put("request", domain.getStudents().get(i).getRequest());
            jsonStudent.put("notification", domain.getStudents().get(i).getNotification());
            Files.write(path, jsonStudent.toString(4).getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
        }
    }

    public SystemDomain getDomain() {
        return domain;
    }

    public Person getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Person currentUser) {
        this.currentUser = currentUser;
    }
}

