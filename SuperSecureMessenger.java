import javax.swing.SwingUtilities;

public class SuperSecureMessenger {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UI ui = new UI();
            UIActions actions = new UIActions(ui);

            ui.clearButton.addActionListener(e -> actions.clearAll());
            aliceSend(ui, actions);

            ui.setVisible(true);
        });
    }

    /** Makes Alice's Send button deliver her typed message to Bob's receiver box. */
    private static void aliceSend(UI ui, UIActions actions) {
        ui.aliceSend.addActionListener(e -> {
            String message = actions.takeAliceMessage();
            if (!message.isBlank()) {
                actions.displayInAlice(message);
                actions.displayInMal("Alice: " + message);
                actions.displayInBob("Alice: " + message);
            }
        });
    }
}
