from Page import Page
from Page import PageType
from System import FunctionType
from System import SystemMessage

class MyRequestPage(Page):

    def __init__(self, content):
        super().__init__(content)
        self.setType(PageType.MyRequestPage)
        self.setName("My Request Page")


    def runPage(self):
        self.showContent()
        self.takeInput()
        return SystemMessage(FunctionType.CHANGE_PAGE, PageType.MAIN_MENU_PAGE, None)
    