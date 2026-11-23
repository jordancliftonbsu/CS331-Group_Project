import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.*;

/** Helper methods for reading and updating the text boxes of a UI. */
public class UIActions {

    private static final DateTimeFormatter TIMESTAMP =
            DateTimeFormatter.ofPattern("HH:mm");

    private final UI window;

    public UIActions(UI window) {
        this.window = window;
    }

    /** Clears every text box in the window. */
    public void clearAll() {
        runOnEdt(() -> {
            window.aliceView.setText("");
            window.malView.setText("");
            window.bobView.setText("");
            window.publicInfo.setText("");
            window.aliceInput.setText("");
            window.bobInput.setText("");
        });
    }

    public void clearAliceSendBox() {
        runOnEdt(() -> window.aliceInput.setText(""));
    }

    public void clearBobSendBox() {
        runOnEdt(() -> window.bobInput.setText(""));
    }

    /** Returns the text typed in Alice's send box, then clears that box. */
    public String takeAliceMessage() {
        return takeAndClear(window.aliceInput, this::clearAliceSendBox);
    }

    /** Returns the text typed in Bob's send box, then clears that box. */
    public String takeBobMessage() {
        return takeAndClear(window.bobInput, this::clearBobSendBox);
    }

    public void displayInAlice(String text) {
        appendWithTimestamp(window.aliceView, text);
    }

    public void displayInBob(String text) {
        appendWithTimestamp(window.bobView, text);
    }

    public void displayInMal(String text) {
        appendWithTimestamp(window.malView, text);
    }

    /** Appends text (no timestamp) to the Public Info box. */
    public void displayInPublicInfo(String text) {
        runOnEdt(() -> {
            window.publicInfo.append(text + "\n");
            window.publicInfo.setCaretPosition(window.publicInfo.getDocument().getLength());
        });
    }

    /** Takes text out of a given JTextArea box and then clears the box */
    private String takeAndClear(JTextArea box, Runnable clearAction) {
        String[] result = new String[1];
        runAndWait(() -> result[0] = box.getText());
        clearAction.run();
        return result[0];
    }

    /** Appends a timestamp to the provided String and then displays it in the provided JTextArea */
    private void appendWithTimestamp(JTextArea area, String text) {
        String line = "[" + LocalDateTime.now().format(TIMESTAMP) + "] " + text + "\n";
        runOnEdt(() -> {
            area.append(line);
            area.setCaretPosition(area.getDocument().getLength()); // scroll to newest
        });
    }

    /** Runs the task on the Swing event thread (immediately if already on it). */
    private static void runOnEdt(Runnable task) {
        if (SwingUtilities.isEventDispatchThread()) {
            task.run();
        } else {
            SwingUtilities.invokeLater(task);
        }
    }

    /** Like runOnEdt but blocks until the task has finished. */
    private static void runAndWait(Runnable task) {
        if (SwingUtilities.isEventDispatchThread()) {
            task.run();
            return;
        }
        try {
            SwingUtilities.invokeAndWait(task);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
