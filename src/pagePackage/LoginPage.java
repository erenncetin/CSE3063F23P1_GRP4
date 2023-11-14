package pagePackage;

import java.util.Scanner;

import helper.ReGSystem;
import userInfoPackage.UserInfo;

public class LoginPage extends Page{
	public UserInfo userInfo;

	public LoginPage(ReGSystem system, String content) {
		super(system, content);
		setType(PageType.LOGIN_PAGE);
		setName("Login Page");
		userInfo = new UserInfo(null, null);
	}


	@Override
	public void runPage() {
		// set user info null because this func may not run one time
		userInfo.reset();
		
		// print the login page content to console
		showContent();
		
		// take password
		System.out.println("Username:");
		userInfo.setUsername(takeInput());
		
		// take password
		System.out.println("Password:");
		userInfo.setPassword(takeInput());
			
				
		if (getSystem().login(userInfo)) {
			getSystem().getUserInterface().setCurrentPage(PageType.MAIN_MENU_PAGE);
		}
		else {
			System.out.println("Your info is wrong! Try again");
		}
	}
}
