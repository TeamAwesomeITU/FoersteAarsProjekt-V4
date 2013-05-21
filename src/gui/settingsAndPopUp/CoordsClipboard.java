package gui.settingsAndPopUp;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

public final class CoordsClipboard implements ClipboardOwner {
   /**
   * Is implemented from the clipboardowner interface. We do not use it
   */
   public void lostOwnership(Clipboard aClipboard, Transferable aContents) {
   }

  /**
   * Copies the coordinates to the clipboard.
  */
  public void setClipboardContents(String coords){
    StringSelection stringSelection = new StringSelection(coords);
    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    clipboard.setContents(stringSelection, this);
  }
} 