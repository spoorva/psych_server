package common;

import javax.servlet.http.HttpSession;

public class Sessions {
	
	public static boolean isValidGlobalAdminSession(HttpSession session){
		
		if(session != null && session.getAttribute(Constant.ROLE) != null &&
				session.getAttribute(Constant.EMAIL) != null &&
				session.getAttribute(Constant.USER_ID) != null &&
				(session.getAttribute(Constant.ROLE).equals(Constant.GLOBAL_ADMIN))){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean isValidAdminSession(HttpSession session){
		
		if(session != null && session.getAttribute(Constant.ROLE) != null &&
				session.getAttribute(Constant.EMAIL) != null &&
				session.getAttribute(Constant.USER_ID) != null &&
				(session.getAttribute(Constant.ROLE).equals(Constant.GLOBAL_ADMIN) ||
				(session.getAttribute(Constant.ROLE).equals(Constant.LOCAL_ADMIN)))){
			return true;
		}else{
			return false;
		}
	}
}
