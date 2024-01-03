from Page import Page
from Page import PageType
from System import FunctionType
from System import SystemMessage

class MyCoursesPage(Page):

    numberOfCourses = None

    def __init__(self, content):
        super().__init__(content)
        self.setType(PageType.MyCoursesPage)
        self.setName("My Courses Page")


    def runPage(self):
        self.showContent()
        inputFromPage = None
        while(True):
            inputFromPage = self.takeInput()
            try:
                selection = int(inputFromPage)
                if (selection > self.numberOfRequest or selection < 0):
                    print("Enter a valid number")
                else:
                    return SystemMessage(FunctionType.SELECT_MY_COURSE, PageType.SELECTED_MY_COURSE_PAGE, selection)
            except:
                if(inputFromPage.lower() == "q"):
                    return SystemMessage(FunctionType.CHANGE_PAGE, PageType.MAIN_MENU_PAGE, None)
                else:
                    print("Wrong Input")


    def getNumberOfCourses(self):
        return self.numberOfCourses
    
    def setNumberOfCourses(self, numberOfCourses):
        self.numberOfCourses = numberOfCourses