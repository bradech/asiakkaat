package helper;

import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Helper {
	
	public String naytaMuuttujaArvoparit(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String paluuStr="";	
        Enumeration<String> parameterNames = req.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            String[] paramValues = req.getParameterValues(paramName);
            for (int i = 0; i < paramValues.length; i++) {                
                paluuStr += paramName + " = " + paramValues[i] + "<br>";                
            }
        }        
        return paluuStr;
    }
}
