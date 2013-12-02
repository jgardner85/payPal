package jbg;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

public class SessionStore {
	public static Map<String, HttpSession> map = new HashMap<String, HttpSession>();

}
