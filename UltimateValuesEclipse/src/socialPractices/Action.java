package socialPractices;

public class Action {
	private String myAction;
	
	public Action(String action){
		setMyAction(action);
	}
	public Action(int i) {
		setMyAction(Integer.toString(i));
	}
	public Action(boolean accept){
		setMyAction(Boolean.toString(accept));
	}
	
	public String getMyAction() {
		return myAction;
	}
	public boolean getMyActionAsBoolean(){
		return Boolean.parseBoolean(myAction);
	}
	public int getMyActionAsInteger(){
		return Integer.parseInt(myAction);
	}

	public void setMyAction(String myAction) {
		this.myAction = myAction;
	}
	
	public boolean isInteger() {
		String str = myAction;
	    if (str == null) {
	        return false;
	    }
	    int length = str.length();
	    if (length == 0) {
	        return false;
	    }
	    int i = 0;
	    if (str.charAt(0) == '-') {
	        if (length == 1) {
	            return false;
	        }
	        i = 1;
	    }
	    for (; i < length; i++) {
	        char c = str.charAt(i);
	        if (c < '0' || c > '9') {
	            return false;
	        }
	    }
	    return true;
	}
}
