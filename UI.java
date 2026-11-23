import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.*;

public class UI extends JFrame {

    final JTextArea aliceView = readOnlyArea();
    final JTextArea malView = readOnlyArea();
    final JTextArea bobView = readOnlyArea();
    final JTextArea publicInfo = readOnlyArea();
    final PlaceholderTextArea aliceInput = new PlaceholderTextArea("Type your message");
    final PlaceholderTextArea bobInput = new PlaceholderTextArea("Type your message");
    final JButton aliceSend = new JButton("Send");
    final JButton bobSend = new JButton("Send");
    final JButton clearButton = new JButton("Clear");

    public UI() {
        super("Super Secure Messenger");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new GridLayout(1, 3, 10, 0));
        root.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        root.add(column("Alice's View", aliceView, inputPanel(aliceInput, aliceSend)));
        root.add(column("Mal's View", malView, publicInfoPanel()));
        root.add(column("Bob's View", bobView, inputPanel(bobInput, bobSend)));

        JPanel content = new JPanel(new BorderLayout());
        content.add(root, BorderLayout.CENTER);
        content.add(controlsPanel(), BorderLayout.SOUTH);
        setContentPane(content);
    }

    /** Builds a column: label + receiver box (70%) over a bottom box (30%). */
    private JPanel column(String title, JTextArea receiver, JComponent bottom) {
        JPanel top = new JPanel(new BorderLayout(0, 4));
        top.add(new JLabel(title, SwingConstants.CENTER), BorderLayout.NORTH);
        top.add(new JScrollPane(receiver), BorderLayout.CENTER);

        JPanel col = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;

        c.gridy = 0;
        c.weighty = 0.7;
        col.add(top, c);

        c.gridy = 1;
        c.weighty = 0.3;
        c.insets = new Insets(10, 0, 0, 0);
        col.add(bottom, c);
        return col;
    }

    private JPanel inputPanel(PlaceholderTextArea input, JButton send) {
        input.setLineWrap(true);
        input.setWrapStyleWord(true);
        JPanel p = new JPanel(new BorderLayout(0, 4));
        p.add(new JScrollPane(input), BorderLayout.CENTER);
        p.add(send, BorderLayout.SOUTH);
        return p;
    }

    private JPanel controlsPanel() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        p.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
        p.add(new JLabel("Controls"));
        p.add(clearButton);
        return p;
    }

    private JPanel publicInfoPanel() {
        JPanel p = new JPanel(new BorderLayout(0, 4));
        p.add(new JLabel("Public Info", SwingConstants.CENTER), BorderLayout.NORTH);
        p.add(new JScrollPane(publicInfo), BorderLayout.CENTER);
        return p;
    }

    private static JTextArea readOnlyArea() {
        JTextArea a = new JTextArea();
        a.setEditable(false);
        a.setLineWrap(true);
        a.setWrapStyleWord(true);
        return a;
    }

    /** Text area that shows grayed-out hint text while empty. */
    static class PlaceholderTextArea extends JTextArea {
        private final String placeholder;

        PlaceholderTextArea(String placeholder) {
            this.placeholder = placeholder;
            addFocusListener(new FocusAdapter() {
                @Override public void focusGained(FocusEvent e) { repaint(); }
                @Override public void focusLost(FocusEvent e) { repaint(); }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (getText().isEmpty()) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2.setColor(Color.GRAY);
                Insets in = getInsets();
                g2.drawString(placeholder, in.left, in.top + g2.getFontMetrics().getAscent());
                g2.dispose();
            }
        }
    }

}
