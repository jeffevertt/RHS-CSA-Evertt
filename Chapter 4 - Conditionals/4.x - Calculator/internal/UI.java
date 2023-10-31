package internal;

import java.awt.*;
import javax.swing.*;

import java.util.*;

import student.Calculator;

import static constants.Buttons.*;

public class UI {

    private static final Collection<DualButton> SECONDARY_BUTTONS = new ArrayList<>();
    private static final Collection<DualButton> CLEAR_BUTTONS = new ArrayList<>();
    private static JLabel expressionDisplay;
    private static JLabel entryDisplay;
    private static JButton memoryHistoryButton;
    private static JPopupMenu memoryHistoryMenu;

    static void swapSecondaryButtons() {
        SECONDARY_BUTTONS.forEach(DualButton::swapText);
    }

    static void swapClearButton() {
        CLEAR_BUTTONS.forEach(DualButton::swapText);
    }

    static void toggleMemoryHistoryMenu() {
        memoryHistoryMenu.show(memoryHistoryButton, 0, 0);
    }

    static boolean hasMemoryValue() {
        return memoryHistoryMenu.getComponentCount() > 0;
    }

    private static JMenuItem getLatestMemoryMenuItem() {
        synchronized (memoryHistoryMenu.getTreeLock()) {
            if (!hasMemoryValue()) {
                throw new IllegalStateException("No memory value available");
            }
            JMenuItem item = (JMenuItem) memoryHistoryMenu.getComponent(0);
            return item;
        }
    }

    static String getLatestMemoryValue() {
        return getLatestMemoryMenuItem().getText();
    }

    static void replaceLatestMemoryValue(String valueToStore) {
        if (valueToStore == null || valueToStore.isEmpty()) {
            throw new IllegalArgumentException("valueToStore cannot be empty");
        }
        synchronized (memoryHistoryMenu.getTreeLock()) {
            if (!hasMemoryValue()) {
                throw new IllegalStateException("No memory value available");
            }
            getLatestMemoryMenuItem().setText(valueToStore);
        }
    }

    static void storeNewMemoryValue(String valueToStore) {
        if (valueToStore == null || valueToStore.isEmpty()) {
            throw new IllegalArgumentException("valueToStore cannot be empty");
        }
        synchronized (memoryHistoryMenu.getTreeLock()) {
            JMenuItem item = new JMenuItem();
            item.setText(valueToStore);
            memoryHistoryMenu.insert(item, 0);
        }
    }

    static void clearMemoryValues() {
        memoryHistoryMenu.removeAll();
    }

    static String getExpressionText() {
        return expressionDisplay.getText();
    }

    static String getEntryText() {
        return entryDisplay.getText();
    }

    static void setExpressionText(String expressionText) {
        if (expressionText == null) {
            throw new IllegalArgumentException(
                    "ExpressionText cannot be null (\"\" should be the default entry display)");
        }
        expressionDisplay.setText(expressionText);
    }

    static void setEntryText(String entryText) {
        if (entryText == null || entryText.trim().isEmpty()) {
            throw new IllegalArgumentException("Entry text cannot be empty (0 should be the default entry display)");
        }
        entryDisplay.setText(entryText);
    }

    static void create() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        JFrame window = new JFrame();
        window.setTitle("Calculator");
        window.setSize(320, 430);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);
        window.setLayout(new BoxLayout(window.getContentPane(), BoxLayout.Y_AXIS));
        {
            JPanel topBar = new JPanel();
            topBar.setLayout(new BoxLayout(topBar, BoxLayout.X_AXIS));
            JButton modeButton = button(topBar, "\u2261");
            modeButton.setMargin(new Insets(1, 5, 1, 5));
            modeButton.setToolTipText("Choose mode");
            modeButton.setEnabled(false);
            JLabel modeLabel = label(topBar, "Scientific");
            modeLabel.setLabelFor(modeButton);
            modeLabel.setFont(modeLabel.getFont().deriveFont(Font.BOLD, 20));
            topBar.add(Box.createHorizontalGlue());
            JButton historyButton = button(topBar, "\u23F2");
            historyButton.setMargin(new Insets(1, 5, 1, 5));
            historyButton.setToolTipText("history");
            historyButton.setEnabled(false);
            window.add(topBar);
        }
        {
            JPanel displayPanel = new JPanel();
            displayPanel.setLayout(new GridLayout(2, 1));
            displayPanel.setMaximumSize(new Dimension(320, 60));
            expressionDisplay = label(displayPanel, "");
            expressionDisplay.setText("");
            expressionDisplay.setSize(320, 20);
            expressionDisplay.setHorizontalAlignment(SwingConstants.RIGHT);
            entryDisplay = label(displayPanel, "");
            entryDisplay.setText("0");
            entryDisplay.setSize(320, 40);
            entryDisplay.setHorizontalAlignment(SwingConstants.RIGHT);
            entryDisplay.setFont(entryDisplay.getFont().deriveFont(Font.BOLD, 26));
            window.add(displayPanel);
        }
        {
            JPanel memoryBar = new JPanel();
            memoryBar.setMaximumSize(new Dimension(320, 24));
            memoryBar.setLayout(new GridLayout(1, 6));
            memoryButton(memoryBar, MEMORY_CLEAR, "Clear all memory");
            memoryButton(memoryBar, MEMORY_RECALL, "Memory recall");
            memoryButton(memoryBar, MEMORY_ADD, "Memory add");
            memoryButton(memoryBar, MEMORY_SUBTRACT, "Memory subtract");
            memoryButton(memoryBar, MEMORY_STORE, "Memory store");
            memoryHistoryButton = memoryButton(memoryBar, MEMORY_HISTORY, "Memory");
            memoryHistoryButton.setEnabled(false);
            memoryHistoryMenu = new JPopupMenu();
            window.add(memoryBar);
        }
        {
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridLayout(7, 5));

            button(buttonPanel, TOGGLE_SECONDARY_BUTTONS);
            button(buttonPanel, PI);
            button(buttonPanel, E);
            button(buttonPanel, CLEAR_BUTTONS, CLEAR_ALL, CLEAR_ENTRY);
            button(buttonPanel, BACKSPACE);

            button(buttonPanel, SECONDARY_BUTTONS, SQUARED, CUBED);
            button(buttonPanel, MULTIPLICATIVE_INVERSE);
            button(buttonPanel, ABSOLUTE_VALUE);
            button(buttonPanel, EXP).setEnabled(false);
            button(buttonPanel, MODULO);

            button(buttonPanel, SECONDARY_BUTTONS, SQUARE_ROOT, CUBE_ROOT);
            button(buttonPanel, OPEN_PARENTHESIS).setEnabled(false);
            button(buttonPanel, CLOSE_PARENTHESIS).setEnabled(false);
            button(buttonPanel, FACTORIAL);
            button(buttonPanel, DIVIDED_BY);

            button(buttonPanel, SECONDARY_BUTTONS, TO_THE_POWER_OF, NTH_ROOT);
            button(buttonPanel, SEVEN);
            button(buttonPanel, EIGHT);
            button(buttonPanel, NINE);
            button(buttonPanel, MULTIPLIED_BY);

            button(buttonPanel, SECONDARY_BUTTONS, TEN_TO_THE_POWER_OF, TWO_TO_THE_POWER_OF);
            button(buttonPanel, FOUR);
            button(buttonPanel, FIVE);
            button(buttonPanel, SIX);
            button(buttonPanel, MINUS);

            button(buttonPanel, SECONDARY_BUTTONS, LOGARITHM_BASE_TEN, LOGARITHM_BASE_N);
            button(buttonPanel, ONE);
            button(buttonPanel, TWO);
            button(buttonPanel, THREE);
            button(buttonPanel, PLUS);

            button(buttonPanel, SECONDARY_BUTTONS, LOGARITHM_BASE_E, E_TO_THE_POWER_OF);
            button(buttonPanel, FLIP_SIGN);
            button(buttonPanel, ZERO);
            button(buttonPanel, DECIMAL_POINT);
            button(buttonPanel, EQUALS);

            window.add(buttonPanel);
        }
        window.setVisible(true);
    }

    private static JButton memoryButton(JPanel memoryBar, String label, String tooltip) {
        JButton button = button(memoryBar, label);
        button.setToolTipText(tooltip);
        return button;
    }

    private static DualButton button(JPanel panel, Collection<DualButton> labelChangeList, String primaryText,
            String secondaryText) {
        DualButton button = new DualButton();
        button.setText(primaryText);
        button.setOtherText(secondaryText);
        configureAndAdd(panel, button);
        labelChangeList.add(button);
        return button;
    }

    private static JButton button(JPanel panel, String text) {
        JButton button = new JButton();
        button.setText(text);
        configureAndAdd(panel, button);
        return button;
    }

    private static void configureAndAdd(JPanel panel, JButton button) {
        button.setFont(button.getFont().deriveFont(Font.PLAIN, 16));
        button.setMargin(new Insets(1, 1, 1, 1));
        button.addActionListener(e -> Calculator.handleButtonClick(e.getActionCommand()));
        panel.add(button);
    }

    private static JLabel label(JPanel panel, String text) {
        JLabel label = new JLabel();
        label.setText(text);
        label.setFont(label.getFont().deriveFont(Font.PLAIN, 16));
        panel.add(label);
        return label;
    }

}
