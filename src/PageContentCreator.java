public class PageContentCreator {

    public static String crateMainMenuPageStudentContent() 
	{
		String str="---------MAIN MENU---------\n"+
				    "1) All Courses\n"+
				    "2) Offered Courses\n"+
				    "3) Selected Courses\n"+
				    "4) Log out\n"+
				    "5) Exit";
		return str;
	}

    public static String courseListForContent (ArrayList<Course> courses) 
	{
		String str="";
		for (int i=1; i<=courses.size() ; i++ )
		{	   
			if(courses.get(i-1) instanceof CourseSession)
			{
				CourseSession session = (CourseSession) courses.get(i-1);
				str+= i+" -> "+courses.get(i-1).getCourseID().getId()
						+session.getSessionID().getId()
						+"|"+courses.get(i-1).getCourseName()
						+"|\n"+courses.get(i-1).getLecturer().getName()
						+"|\n"+courses.get(i-1).getQuota()+"\n"
						+"--------------------------------------"+"\n";	
			}
			else {
				str+= i+" -> "+courses.get(i-1).getCourseID().getId()
			+"|"+courses.get(i-1).getCourseName()
			+"|\n"+courses.get(i-1).getLecturer().getName()
			+"|\n"+courses.get(i-1).getQuota()+"\n"
			+"--------------------------------------"+"\n";	
			}
			
		}	
		return str;	
	}
    public static String createAllCoursesPageContent   (ArrayList<Course> courses) {
		String str="=>List of all courses<=\n"+
		"-------------------------------------\n"+
		courseListForContent(courses)+"\n"+
		"Press any key to back to main menu...";	
		return str;
		}

    public static String createSelectableCoursesPageContent  (ArrayList<Course> courses,ArrayList<Course> coursesSelected) {
		String str="=>List of all selectable courses<=\n\n"+
	    "Press number to select course,\nPress q to exit\n"+
		"-------------------------------------\n"
		+courseListForContent(courses)+"\n\n\n\n"+
		"=>List of all selected courses<=\n"+
		courseListForContent(coursesSelected);		
		return str;
	}

    	
	public static String createSelectedCoursesPageContent  (ArrayList<Course> courses) {
		String str="=>List of all selected courses<=\n"+
		"-------------------------------------\n"+
		courseListForContent(courses);	
		return str;	
		}

    public static String createApprovedCoursesPageContent  (ArrayList<Course> courses) {
		String str="=>List of all approved courses<=\n"+
		"-------------------------------------\n"+
		courseListForContent(courses);	
		return str;	
		}

    public static String createMainMenuPageAdvisorContent() 
	{
		String str="---------MAIN MENU---------\n"+
				    "1) Advised Student Information\n"+
				    "2) Evaluate Requests\n"+
				    "3) Log out\n"+
				    "4) Exit\n";
		return str;
	}


    public  static String createMyStudentsPageContent (ArrayList<Student> student)
	{
		String str="All students advised\n";
		for (int i=1; i<=student.size() ; i++ )
		{	   
			str+= i+"->\n StudentID: "+
		    student.get(i-1).getStudentID()
			+"\n Full name: "+		
			student.get(i-1).getFirstName()+" "+
			student.get(i-1).getLastName()
			+"\n Semester: "+
			student.get(i-1).getSemester()+"\n"
			+"--------------------------------------"+"\n";	
		}	
		return str;	
	}

   public static String createEvaluateRequestPageContent (ArrayList<Student> student) //take awaiting student as a parameter
	{	
		String str = "Here is the all student that requested courses.\n";
		
		for(int a=0; a<student.size(); a++)
		{
			str+="\n"+(a+1)+"->"+student.get(a).studentID.getId()+" "+student.get(a).getFirstName()
					+" "+student.get(a).getLastName()
					+" requested "+student.get(a).getSelectedCourses().size()+ " lessons: \n";
					for(int i=0; i<student.get(a).selectedCourses.size(); i++)
					{
						str+= "  *"+ student.get(a).selectedCourses.get(i).getCourseName()
							+"\n";
					}		
					
		}
		str+="\r\n"+ "Select one of student or press 'q' for exit.";
		return str;	
	}
	
    public static String createSelectedStudentsRequesPageContent (Student student)
	{
		String str = "";
		str="All course requests of "+student.getFirstName()+" "+student.getLastName()
		+" number "+student.studentID.getId()
		+"\n"+courseListForContent(student.selectedCourses)+"\n"
		+"Press 'y' to approve, 'n' to reject, 'q' to exit without taking any action.";
		
		return str;
	}

	

}

