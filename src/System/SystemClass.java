package System;

import CourseObject.*;
import PersonObject.*;
import UserInterface.*;
import Page.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

public class SystemClass {
    private SystemDomain domain;
    private Person currentUser;
    private UserInterface userInterface;

    //Constructor
    public SystemClass(UserInterface u) throws JSONException, IOException {
    	userInterface = u;
		LoginPage login = new LoginPage("Welcome! Please enter your username/password.");
		userInterface.addPage(login);
		userInterface.setCurrentPage(PageType.LOGIN_PAGE);
    	domain = new SystemDomain();
    }

    //Start running the code
	public void run() throws JSONException, IOException {
		while (true) {
			userInterface.display();
			listenUserInterface(userInterface.getSystemMessage());
		}
	}

    //Login with the given user info
    public void login(String[] userInfo) {
    	String username = userInfo[0];
    	String password = userInfo[1];
        boolean userFound = false;
        if (username.charAt(0) == 'o') {
            for (Student student : domain.getStudentCreator().getStudents()) {
                if (("o" + student.getPersonId().getId()).equals(username) &&
                        student.getPassword().getPassword().equals(password)) {
                    setCurrentUser(student);
                    userFound = true;
                    break;
                }
            }
        } else if (username.charAt(0) == 'l') {
            for (Lecturer lecturer : domain.getLecturerCreator().getLecturers()) {
                if (("l" + lecturer.getPersonId().getId()).equals(username) &&
                        lecturer.getPassword().getPassword().equals(password)) {
                    if (lecturer instanceof Advisor advisor) {
                        advisor.findAwaitingStudents();
                    }
                    setCurrentUser(lecturer);
                	lecturer.createSyllabus(lecturer.getGivenCourses());
                    userFound = true;
                    break;
                }
            }
        } if (!userFound) {
        	System.out.println("\u001B[33;1mUsername/Password incorrect.\n\u001B[0m");
        } else {
            userInterface.setPages(domain.getPageCreator().createPages(currentUser));
            userInterface.setCurrentPage(PageType.MAIN_MENU_PAGE);
            System.out.println("\u001B[32;1mLOGIN SUCCESSFUL - WELCOME " + currentUser.getFirstName() + " " + currentUser.getLastName() + "\u001B[0m");
        }
    }

    //Logout from an account
    public void logout() {
    	userInterface.resetPages();
    	LoginPage login = new LoginPage("Welcome! Please enter your username/password.");
		userInterface.addPage(login);
		userInterface.setCurrentPage(PageType.LOGIN_PAGE);
        setCurrentUser(null);
    }

    //Exit the system
    public void exit() throws JSONException, IOException {
        updateStudentJSON();
        updateCourseJSON();
        updateAdvisorJSON();
        updateLecturerJSON();
        System.exit(0);
    }

    //Update course quota after selections in JSON files
    private void updateCourseJSON() {
        try {
            Path path = Path.of("src\\JSON_Files\\courses.json");
            String content = new String(Files.readAllBytes(path));
            JSONObject jsonObject = new JSONObject(content);
            JSONArray courseJSON = jsonObject.getJSONArray("courses");
            for (int i = 0; i < courseJSON.length(); i++) {
                JSONObject currentCourse = courseJSON.getJSONObject(i);
                currentCourse.put("quota", domain.getCourseCreator().getCourses().get(i).getQuota());
                currentCourse.put("studentList", studentToJsonArray(domain.getCourseCreator().getCourses().get(i).getStudentList()));
            }
            Files.write(path, jsonObject.toString(4).getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
        }
        catch (IOException | JSONException ignored){
            System.err.println("An error occurred while writing data to the courses JSON file.");
            System.exit(0);
        }
    }

    private void updateLecturerJSON() throws JSONException, IOException{
        try {
            Path path = Path.of("src\\JSON_Files\\lecturers.json");
            String content = new String(Files.readAllBytes(path));
            JSONObject jsonObject = new JSONObject(content);
            JSONArray lecturerJSON = jsonObject.getJSONArray("lecturers");
            for (int i = 0; i < domain.getLecturerCreator().getLecturers().size(); i++) {
                if (!(domain.getLecturerCreator().getLecturers().get(i) instanceof Advisor)) {
                    JSONObject currentLecturer = lecturerJSON.getJSONObject(i);
                    currentLecturer.put("password", domain.getLecturerCreator().getLecturers().get(i).getPassword().getPassword());
                }
            }
            Files.write(path, jsonObject.toString(4).getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
        }
        catch (IOException | JSONException ignored){
            System.err.println("An error occurred while writing data to the lecturers JSON file.");
            System.exit(0);
        }
    }
    private void updateAdvisorJSON() throws JSONException, IOException{
        try {
            Path path = Path.of("src\\JSON_Files\\advisors.json");
            String content = new String(Files.readAllBytes(path));
            JSONObject jsonObject = new JSONObject(content);
            JSONArray advisorJSON = jsonObject.getJSONArray("advisors");
            for (int i = 0; i < domain.getAdvisorCreator().getAdvisors().size(); i++) {
                JSONObject currentAdvisor = advisorJSON.getJSONObject(i);
                currentAdvisor.put("password", domain.getAdvisorCreator().getAdvisors().get(i).getPassword().getPassword());
            }
            Files.write(path, jsonObject.toString(4).getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
        }
        catch (IOException | JSONException ignored){
            System.err.println("An error occurred while writing data to the advisors JSON file.");
            System.exit(0);
        }
    }

    //Update student infos in JSON files
    private void updateStudentJSON() {
        for (int i = 0; i < domain.getStudentCreator().getStudents().size(); i++) {
            try {
                String studentId = domain.getStudentCreator().getStudents().get(i).getPersonId().getId();
                Path path = Path.of("src\\JSON_Files\\Students\\" + studentId + ".json");
                String content = new String(Files.readAllBytes(path));
                JSONObject jsonStudent = new JSONObject(content);
                JSONObject registration = jsonStudent.getJSONObject("registration");
                ArrayList<Course> selected = domain.getStudentCreator().getStudents().get(i).getSelectedCourses();
                ArrayList<Course> approved = domain.getStudentCreator().getStudents().get(i).getApprovedCourses();
                registration.put("selectedCourses", transcriptCourses(selected));
                registration.put("approvedCourses", transcriptCourses(approved));
                jsonStudent.put("password", domain.getStudentCreator().getStudents().get(i).getPassword().getPassword());
                jsonStudent.put("request", domain.getStudentCreator().getStudents().get(i).getRequest());
                jsonStudent.put("readNotification", domain.getStudentCreator().getStudents().get(i).getReadNotifications().toArray(new String[0]));
                jsonStudent.put("unreadNotification", domain.getStudentCreator().getStudents().get(i).getUnreadNotifications().toArray(new String[0]));
                Files.write(path, jsonStudent.toString(4).getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
            } catch (JSONException | IOException exception) {
                System.err.println("\nSYSTEM UPDATE FAILS.");
                System.exit(0);
            }
        }
    }

    //Create an array for JSON files update
    private String[] studentToJsonArray(ArrayList<Student> students){
        String[] studentIds = new String[students.size()];
        for(int i=0; i<students.size();i++){
            studentIds[i] = students.get(i).getPersonId().getId();
        }
        return  studentIds;
    }

    //Create an array for JSON files update
    private String[] transcriptCourses(ArrayList<Course> transcriptCoursesList) {
        String[] transcriptCoursesAr = new String[transcriptCoursesList.size()];
        for (int i = 0; i < transcriptCoursesList.size(); i++) {
            if (transcriptCoursesList.get(i) instanceof CourseSession crsSession) {
                transcriptCoursesAr[i] = crsSession.getCourseId().getId() + "." + crsSession.getSessionId().getId();
            } else {
                transcriptCoursesAr[i] = transcriptCoursesList.get(i).getCourseId().getId();
            }
        } return transcriptCoursesAr;
    }

    //PAGE MERGE WITH SYSTEM PROCESSES
    public void listenUserInterface(SystemMessage sm) throws JSONException, IOException {
        FunctionType functionType = sm.getFunctionType();
        if (functionType == FunctionType.LOGIN) {
        	String userInfo[] = (String[]) sm.getInput();
        	this.login(userInfo);
        }
        else if (functionType == FunctionType.LOGOUT) {
	        System.out.println("\u001B[31;1mLOGOUT SUCCESSFUL - GOODBYE "+ currentUser.getFirstName() + " " + currentUser.getLastName() + "\u001B[0m");
			this.logout();
	        this.userInterface.setCurrentPage(PageType.LOGIN_PAGE);
        }
        else if (functionType == FunctionType.EXIT) {
            try {
    	        System.out.println("\u001B[31;1mSYSTEM EXITING\u001B[0m");
            	Thread.sleep(500);
    	        System.out.println("\u001B[31;1mSYSTEM EXITING.\u001B[0m");
            	Thread.sleep(500);
    	        System.out.println("\u001B[31;1mSYSTEM EXITING..\u001B[0m");
            	Thread.sleep(500);
    	        System.out.println("\u001B[31;1mSYSTEM EXITING...\u001B[0m");
            }
            catch (Exception e){
            }
            finally {
                exit();
            }
        }
        else if (functionType == FunctionType.CHANGE_PAGE) {
            this.userInterface.setCurrentPage(sm.getNextPageType());
        }
        else if (functionType == FunctionType.SELECT_COURSE ) {
			Student student = (Student) this.getCurrentUser(); 
			String courseName = student.getSelectableCourses().get((Integer)sm.getInput() - 1).getCourseName();
			if (student.addSelectedCourse((Integer)sm.getInput())) {
                System.out.println("\u001B[32;1mCourse Addition Is Succesful - " + courseName + "\u001B[0m");
			}
			else {
    	        System.out.println("\u001B[33;1mCourse Addition Is Not Succesful - " + courseName + "\u001B[0m");
			}

            // handling of selecteable course data
            SelectableCoursesPage selectableCoursePage = (SelectableCoursesPage) this.userInterface.selectPage(PageType.SELECTABLE_COURSES_PAGE);
			selectableCoursePage.setContent(domain.getPageCreator().createSelectableCoursesPageContent(student.getSelectableCourses(), student.getMarks()));
			selectableCoursePage.setNumberOfSelectableCourses(student.getSelectableCourses().size());

            // handling selected course data
            SelectedCoursesPage selectedCoursePage = (SelectedCoursesPage) this.userInterface.selectPage(PageType.SELECTED_COURSES_PAGE);
			selectedCoursePage.setContent(domain.getPageCreator().createSelectedCoursesPageContent(student.getSelectedCourses()));
			selectedCoursePage.setNumberOfDropableCourses(student.getSelectedCourses().size());
			
			// handling syllabus page
			SyllabusPage syllabus = (SyllabusPage) this.userInterface.selectPage(PageType.SYLLABUS_PAGE);
			syllabus.setContent(domain.getPageCreator().createSyllabusPageContent(student.getSyllabus()));

			this.userInterface.setCurrentPage(sm.getNextPageType());
        }
        else if (functionType == FunctionType.DROP_COURSE ) {
            Student student = (Student) this.getCurrentUser();
			String courseName = student.getSelectedCourses().get((Integer)sm.getInput() - 1).getCourseName();

            student.dropCourse((Integer)sm.getInput());
            System.out.println("\u001B[32;1mCourse Dropping Is Succesful - " + courseName + "\u001B[0m");

            // handling selected course data
            SelectedCoursesPage selectedCoursePage = (SelectedCoursesPage) this.userInterface.selectPage(PageType.SELECTED_COURSES_PAGE);
			selectedCoursePage.setContent(domain.getPageCreator().createSelectedCoursesPageContent(student.getSelectedCourses()));
			selectedCoursePage.setNumberOfDropableCourses(student.getSelectedCourses().size());

            // handling of selecteable course data
            SelectableCoursesPage selectableCoursePage = (SelectableCoursesPage) this.userInterface.selectPage(PageType.SELECTABLE_COURSES_PAGE);
			selectableCoursePage.setContent(domain.getPageCreator().createSelectableCoursesPageContent(student.getSelectableCourses(), student.getMarks()));
			selectableCoursePage.setNumberOfSelectableCourses(student.getSelectableCourses().size());
			
			// handling syllabus page
			SyllabusPage syllabus = (SyllabusPage) this.userInterface.selectPage(PageType.SYLLABUS_PAGE);
			syllabus.setContent(domain.getPageCreator().createSyllabusPageContent(student.getSyllabus()));
						
            this.userInterface.setCurrentPage(sm.getNextPageType());
        }
        else if (functionType == FunctionType.SEND_APPROVE ) {
            Student student = (Student) this.getCurrentUser();
            if (student.getRequest().equals("false")) {
                student.sendToApproval();
                System.out.println("\u001B[32;1mYou have successfully sent your course selection list for advisor approval!\u001B[0m");
            }
            else {
                System.out.println("\u001B[33;1mYou have already successfully sent your course selection list for advisor approval!\u001B[0m");
            }
            this.userInterface.setCurrentPage(sm.getNextPageType());
        }
        else if (functionType == FunctionType.SELECET_STUDENT ) {
			Advisor advisor = (Advisor)this.getCurrentUser();
			advisor.selectStudent((Integer)sm.getInput());
			
			// handling selected request 
			SelectedStudentRequestPage s = (SelectedStudentRequestPage) this.userInterface.selectPage(PageType.SELECTED_STUDENT_REQUEST_PAGE);
            s.setContent(domain.getPageCreator().createSelectedStudentsRequestPageContent(advisor.getSelectStudent()));
			this.userInterface.setCurrentPage(sm.getNextPageType());
        }
        else if (functionType == FunctionType.APPROVE_REQUEST ) {
			Advisor advisor = (Advisor)this.getCurrentUser();
			String selectedStudentFullName = advisor.getSelectStudent().getFirstName() + " " + advisor.getSelectStudent().getLastName();
 			advisor.sendNotification((String) sm.getInput(), "A");
 			advisor.approve();

 			System.out.println("\u001B[32;1mRequest Has Been Approved - " + selectedStudentFullName + "'s Request\u001B[0m");

            // handling selected student request
            SelectedStudentRequestPage selectedStdudentRequesPage = (SelectedStudentRequestPage) this.userInterface.selectPage(PageType.SELECTED_STUDENT_REQUEST_PAGE);
			selectedStdudentRequesPage.setContent(domain.getPageCreator().createSelectedStudentsRequestPageContent(advisor.getSelectStudent()));

            // handling evaluate request
            EvaluateRequestsPage evaluateRequestPage = (EvaluateRequestsPage) this.userInterface.selectPage(PageType.EVALUATE_REQUESTS_PAGE);
			evaluateRequestPage.setContent(domain.getPageCreator().createEvaluateRequestPageContent(advisor.getAwaitingStudents()));
			evaluateRequestPage.setNumberOfRequest(advisor.getAwaitingStudents().size());
            this.userInterface.setCurrentPage(sm.getNextPageType());
        }
        else if (functionType == FunctionType.DISAPPREOVE_REQUEST ) {
			Advisor advisor = (Advisor)this.getCurrentUser();
			String selectedStudentFullName = advisor.getSelectStudent().getFirstName() + " " + advisor.getSelectStudent().getLastName();
 			advisor.sendNotification((String) sm.getInput(), "Dmnefbjfafk");
			advisor.disapprove();

 			System.out.println("\u001B[33;1mRequest Has Been Disapproved - " + selectedStudentFullName + "'s Request\u001B[0m");

            // handling selected student request
            SelectedStudentRequestPage selectedStdudentRequesPage = (SelectedStudentRequestPage) this.userInterface.selectPage(PageType.SELECTED_STUDENT_REQUEST_PAGE);
			selectedStdudentRequesPage.setContent(domain.getPageCreator().createSelectedStudentsRequestPageContent(advisor.getSelectStudent()));

            // handling evaluate request
            EvaluateRequestsPage evaluateRequestPage = (EvaluateRequestsPage) this.userInterface.selectPage(PageType.EVALUATE_REQUESTS_PAGE);
			evaluateRequestPage.setContent(domain.getPageCreator().createEvaluateRequestPageContent(advisor.getAwaitingStudents()));
			evaluateRequestPage.setNumberOfRequest(advisor.getAwaitingStudents().size());

            this.userInterface.setCurrentPage(sm.getNextPageType());
        }
        else if (functionType == FunctionType.SELECT_MY_COURSE ) {
			Lecturer lecturer = (Lecturer)this.getCurrentUser();
			lecturer.selectCourse((Integer)sm.getInput());
			
			// handling selected my course
			SelectedMyCoursePage selectedMyCourse = (SelectedMyCoursePage) this.userInterface.selectPage(PageType.SELECTED_MY_COURSE_PAGE);
			selectedMyCourse.setContent(domain.getPageCreator().createSelectedMyCoursePage(lecturer.getSelectedCourse()));
			
			this.userInterface.setCurrentPage(sm.getNextPageType());			
        }
        else if (functionType == FunctionType.CHANGE_PASSWORD ) {
        	String passwords[] = (String[]) sm.getInput();
        	
        	// Check current and new password and change it if it is okay
        	if (currentUser.getPassword().compareCurrentPassword(passwords[0])) {
        		if (currentUser.getPassword().checkPasswordCond(passwords[1])) {
        			currentUser.getPassword().setPassword(passwords[1]);
                    System.out.println("\u001B[32;1mPassword Change Successful\u001B[0m");
        		}
        		else {
                	System.out.println("\u001B[33;1mYour new password must obey the rules!\u001B[0m");
        		}
        	}
        	else {
            	System.out.println("\u001B[33;1mYour current password incorrect!\u001B[0m");
        	}

        	this.userInterface.setCurrentPage(sm.getNextPageType());
        }
        else if (functionType == FunctionType.READ_NOTIFICATIONS ) {
            Student student = (Student) this.getCurrentUser();
            student.clearUnreadNotification();
            MainMenuPageStudent main = (MainMenuPageStudent) this.userInterface.selectPage(PageType.MAIN_MENU_PAGE);
            main.setContent(domain.getPageCreator().createMainMenuPageStudentContent(student.getUnreadNotifications().size()));
            
            this.userInterface.setCurrentPage(sm.getNextPageType());
        }

    }

    public Person getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Person currentUser) {
        this.currentUser = currentUser;
    }
}