package helpers;

public class ApplicationHelper {
  public static String GetAppVersion(){
    return System.getenv("PAINTING_APP_VERSION");
  }
}
