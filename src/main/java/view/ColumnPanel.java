package view;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

import model.Column;
import view.component.DUButton;
import view.component.DUComboBox;
import view.component.DUComboBoxItem;
import view.component.DUTextField;
import view.util.DULocalizator;
import view.util.DUResource;

public class ColumnPanel extends JPanel {
	private static final long serialVersionUID = 846132445578478084L;

	private static final DULocalizator LOC = new DULocalizator(ColumnPanel.class);

	private final JLabel lblType = new JLabel(ColumnPanel.LOC.getRes("lblType"));
	private final DUComboBox<DUComboBoxItem<Column.Type>, Column.Type> cmbType = new DUComboBox<>();
	private final JLabel lblName = new JLabel(ColumnPanel.LOC.getRes("lblName"));
	private final DUTextField txtName = new DUTextField();
	private final DUButton btnAdd = new DUButton(DUResource.getAddImage());
	private final DUButton btnDelete = new DUButton(DUResource.getDeleteImage());

	private final MainView parent;

	public ColumnPanel(final MainView parent) {
		this.parent = parent;

		this.setup();
		this.init();
	}

	private void setup() {
		final Dimension dimension = new Dimension(500, 30);
		this.setSize(dimension);
		this.setPreferredSize(dimension);
		this.setLayout(null);

		this.add(this.lblType);
		this.add(this.cmbType);
		this.add(this.lblName);
		this.add(this.txtName);
		this.add(this.btnAdd);
		this.add(this.btnDelete);

		final int height = 20;
		final int x = 0;
		final int y = 10;
		this.lblType.setBounds(x, y, 40, height);
		this.cmbType.setBounds(x + 40, y, 100, height);
		this.lblName.setBounds(x + 170, y, 100, height);
		this.txtName.setBounds(x + 270, y, 130, height);
		this.btnAdd.setBounds(x + 410, y, 20, 20);
		this.btnDelete.setBounds(x + 440, y, 20, 20);

		this.lblType.setToolTipText(ColumnPanel.LOC.getRes("lblTypeToolTip"));
		this.lblName.setToolTipText(ColumnPanel.LOC.getRes("lblNameToolTip"));

		this.btnAdd.addActionListener(e -> this.btnAddActionPerformed());
		this.btnDelete.addActionListener(e -> this.btnDeleteActionPerformed());
	}

	private void init() {
		this.cmbType.addItems(Column.Type.getColumnComboItems());
		this.cmbType.setSelectedIndex(0);
	}

	private void btnAddActionPerformed() {
		this.parent.addColumnPanel(this);
	}

	private void btnDeleteActionPerformed() {
		this.parent.removeColumnPanel(this);
	}

	public boolean checkParams() {
		if (this.txtName.isEmpty()) {
			return false;
		}
		return true;
	}

	public Column getColumn() {
		return new Column(this.cmbType.getSelectedItemKey(), this.txtName.getText());
	}

	public void clear() {
		this.cmbType.setSelectedIndex(0);
		this.txtName.clear();
	}

	@Override
	public void setEnabled(final boolean enabled) {
		this.cmbType.setEnabled(enabled);
		this.txtName.setEnabled(enabled);
		this.btnAdd.setEnabled(enabled);
		this.btnDelete.setEnabled(enabled);
	}
}
