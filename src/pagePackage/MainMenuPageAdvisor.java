package pagePackage;

import systemMessagePackage.FunctionType;
import systemMessagePackage.SystemMessage;

public class MainMenuPageAdvisor extends Page {
	
	public MainMenuPageAdvisor(String content) {
		super(content);
		setType(PageType.MAIN_MENU_PAGE_ADVISOR);
		setName("Main Menu Page Advisor");
	}
	
	@Override
	public SystemMessage runPage() {
		showContent();
		
		switch (takeInput()) {
			case "1":
				return new SystemMessage(FunctionType.CHANGE_PAGE, PageType.MY_STUDENTS, null);
			case "2":
				return new SystemMessage(FunctionType.CHANGE_PAGE, PageType.MY_REQUESTS, null);
			case "3":
				return new SystemMessage(FunctionType.CHANGE_PAGE, PageType.EVALUATE_REQUESTS, null);
			case "4":
				
		}
		
		
		
		return null;
	}

}
