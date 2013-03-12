package xmlClasses;
public class TestWrite {

  public static void main(String[] args) {
    StaxWriter configFile = new StaxWriter("kdv_unload", "kdv_unload.txt", "roadSegmentCollection", "roadSegment", " ");
    try {
      configFile.saveConfig();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}