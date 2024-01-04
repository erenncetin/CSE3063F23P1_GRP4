from python_development.Page.Page import Page
from python_development.Page.PageType import PageType
from python_development.System.FunctionType import FunctionType
from python_development.System.SystemMessage import SystemMessage

class AllCoursesPage(Page):

    def __init__(self, content):
        super().__init__(content)
        self.setType("ALL_cOURSES_PAGE")
        self.setName("All Courses Page")

    
    def runPage(self):
        self.showContent()
        self.takeInput()
        return SystemMessage("CHANGE_PAGE", "MAIN_MENU_PAGE", None)