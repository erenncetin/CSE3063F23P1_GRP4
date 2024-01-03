from Page import Page
from Page import PageType
from System import FunctionType
from System import SystemMessage

class EvaluateRequestsPage(Page):
    
    numberOfRequest = None
    
    def __init__(self, content):
        super().__init__(content)
        self.setType(PageType.EvaluateRequestsPage)
        self.setName("Evaluate Request Page")

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
                    return SystemMessage(FunctionType.SELECET_STUDENT, PageType.SELECTED_STUDENT_REQUEST_PAGE, selection)
            except:
                if(inputFromPage.lower() == "q"):
                    return SystemMessage(FunctionType.CHANGE_PAGE, PageType.MAIN_MENU_PAGE, None)
                else:
                    print("Wrong Input")

    def getNumberOfRequest(self):
        return self.numberOfRequest

    def setNumberOfRequest(self, numberOfRequest):
        self.numberOfRequest = numberOfRequest     
                