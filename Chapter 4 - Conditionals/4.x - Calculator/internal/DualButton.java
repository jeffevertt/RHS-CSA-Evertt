package internal;

import javax.swing.*;

class DualButton extends JButton {

    private String otherText = "";

    public void setOtherText(String otherText) {
        this.otherText = otherText;
    }

    public void swapText() {
        String previousText = getText();
        setText(otherText);
        otherText = previousText;
    }

}
