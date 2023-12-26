package PersonObject;

public class Password {
    private String password;

    public Password(String password) {
        this.password = password;
    }
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean compareCurrentPassword(String enteredPassword){
        return enteredPassword.equals(password);
    }

    public boolean checkPasswordCond(String newPassword){
		boolean containsUpperCase = checkUpperCaseCond(newPassword),
        containsLowerCase =checkLowerCaseCond(newPassword),
	    containsNumber = checkNumberCaseCond(newPassword),
        continsSpeacialCharacter=isContainsSpeacialChar(newPassword),
        lenght=lenghtCond(newPassword);
		if (lenght && continsSpeacialCharacter && containsUpperCase && containsLowerCase && containsNumber){
				return true;
			}
         return false;
	}

    public boolean isContainsSpeacialChar(String newPassword){
        
        if(newPassword.contains(".") || newPassword.contains("_")){
            return true;
        }else{
            System.out.println("The password must contain some special character such as '_' , '.'.");
            return false;
        }
    }

    public boolean lenghtCond(String newPassword){
        if(newPassword.length() > 8 && newPassword.length() < 20){
            return true;
        }else{
            System.out.println("The password must be at least 8 and at most 20 characters.");
            return false;
        }
    }

    public boolean checkUpperCaseCond(String newPassword){
        for (char c : newPassword.toCharArray()) {
            if (Character.isUpperCase(c)) {
                return true;
            }
        }
        System.out.println("The password must contain at least one uppercase letter.");
        return false;
    }

    public boolean checkLowerCaseCond(String newPassword){
        for (char c : newPassword.toCharArray()) {
            if (Character.isLowerCase(c)) {
                return true;
            }
        }
        System.out.println("The password must contain at least one lowercase letter.");
        return false;
    }

    public boolean checkNumberCaseCond(String newPassword){
         for (char c : newPassword.toCharArray()) {
            if (Character.isDigit(c)) {
                return true;
            }
        }
            System.out.println("The password must contain at least one number.");
        return false;
    }
    public boolean equals(Object o){
        if(o instanceof Password){
            return ((Password) o).getPassword().equals(this.getPassword());
        }
        return false;
    }
}