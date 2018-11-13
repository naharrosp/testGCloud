package util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import org.fluentd.logger.FluentLogger;


public class ExceptionUtil {
	  private static FluentLogger ERRORS = FluentLogger.getLogger("myapp");


	  public static void report(Throwable ex) {
	    StringWriter exceptionWriter = new StringWriter();
	    ex.printStackTrace(new PrintWriter(exceptionWriter));
	    Map<String, Object> data = new HashMap<>();
	    data.put("message", exceptionWriter.toString());
	    Map<String,String> serviceContextData = new HashMap<>();
	    serviceContextData.put("service", "myapp");
	    data.put("serviceContext", serviceContextData);
	    // ... add more metadata
	    ERRORS.log("errors", data);
	  }
	}