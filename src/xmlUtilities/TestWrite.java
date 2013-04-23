package xmlUtilities;
public class TestWrite {

  public static void main(String[] args) {
    StaxWriter configFile = new StaxWriter("kdv_unload_References", "kdv_unload.txt", "RSC", "RS", " ");
    try {
      configFile.saveConfig();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}