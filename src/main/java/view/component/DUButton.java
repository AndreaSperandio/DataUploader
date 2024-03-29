package view.component;

import java.awt.Insets;

import javax.swing.Icon;
import javax.swing.JButton;

public class DUButton extends JButton {
	private static final long serialVersionUID = 7532741475194030901L;

	public DUButton(final Icon icon) {
		super(icon);
		this.setup();
	}

	public DUButton(final String name) {
		super(name);
		this.setup();
	}

	public DUButton(final String name, final Icon icon) {
		super(name, icon);
		this.setup();
	}

	private void setup() {
		this.setMargin(new Insets(1, 1, 1, 1));
	}
}