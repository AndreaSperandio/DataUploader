package view;

import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import control.ExportCore;
import control.ImportCore;
import model.Column;
import model.ExportRow;
import model.ImportedRow;
import view.component.DUButton;
import view.component.DUTextField;
import view.util.DUColor;
import view.util.DULocalizator;
import view.util.DUMessage;
import view.util.DUResource;

public class MainView extends JFrame {
	private static final long serialVersionUID = 846132440578478084L;

	private static final DULocalizator LOC = new DULocalizator(MainView.class);
	private static final String DESKTOP_FOLDER = System.getProperty("user.home") + File.separator + "Desktop";
	private static final String FILE_EXTENSION = ".xlsx";

	private static final int DEFAULT_MAX_COLUMN_VIEW = 4;
	private static final String DEFAULT_ID = "id";

	private final JLabel lblInstruction = new JLabel(MainView.LOC.getRes("lblInstruction"));
	private final JLabel lblDocument = new JLabel(MainView.LOC.getRes("lblDocument"));
	private final DUButton btnDocument = new DUButton(MainView.LOC.getRes("btnLoad"));
	private final JLabel lblDocumentFile = new JLabel();
	private final JLabel lblTable = new JLabel(MainView.LOC.getRes("lblTable"));
	private final DUTextField txtTable = new DUTextField();
	private final JLabel lblId = new JLabel(MainView.LOC.getRes("lblId"));
	private final DUTextField txtId = new DUTextField();
	private final DUButton btnTransform = new DUButton(DUResource.getStartImage());
	private final JLabel lblTransform = new JLabel(MainView.LOC.getRes("lblTransform"));
	private final JLabel lblExportPath = new JLabel(MainView.LOC.getRes("lblExportPath"));
	private final JLabel lblExportPathWrn = new JLabel(MainView.LOC.getRes("lblExportPathWrn"));
	private final DUButton btnExportPath = new DUButton(MainView.LOC.getRes("btnExportPath"));
	private final JLabel lblExportFile = new JLabel();
	private final DUButton btnExport = new DUButton(DUResource.getExportImage());
	private final JLabel lblExport = new JLabel(MainView.LOC.getRes("lblExport"));
	private final JLabel lblAuthor = new JLabel(MainView.LOC.getRes("lblAuthor"));

	private final JPanel pnlColumns = new JPanel();
	private JScrollPane scrollColumns = new JScrollPane(this.pnlColumns);
	private int yScrollColumns;

	private File documentFile = null;
	private File exportFile = null;

	private String sheetName = null;
	private List<ImportedRow> importedRows = null;
	private List<ExportRow> exportRows = null;
	private List<ExportRow> exportQueries = null;
	private final List<ColumnPanel> columnPanels = new ArrayList<>();

	public MainView() {
		this.setup();
		this.init();

		this.columnPanels.add(new ColumnPanel(this));
		this.columnPanels.add(new ColumnPanel(this));
		this.columnPanels.add(new ColumnPanel(this));
		this.columnPanels.add(new ColumnPanel(this));
		this.recalculateColumnPanels();
	}

	private void setup() {
		this.setTitle(MainView.LOC.getRes("title"));
		final Dimension dimension = new Dimension(550, 660);// Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(dimension);
		this.setPreferredSize(dimension);
		// this.setExtendedState(Frame.MAXIMIZED_BOTH);
		this.setIconImages(DUResource.getLogoIcons());
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent e) {
				//if (TMMessage.showConfirmWarnDialog(MainView.this, MainView.LOC.getRes("cnfExit"))) {
				MainView.this.dispose();
				System.exit(0);
				//}
			}
		});
		this.setLayout(null);
		this.pnlColumns.setLayout(null);

		this.add(this.lblInstruction);
		this.add(this.lblDocument);
		this.add(this.btnDocument);
		this.add(this.lblDocumentFile);
		this.add(this.lblTable);
		this.add(this.txtTable);
		this.add(this.lblId);
		this.add(this.txtId);
		this.add(this.btnTransform);
		this.add(this.lblTransform);
		this.add(this.lblExportPath);
		this.add(this.lblExportPathWrn);
		this.add(this.btnExportPath);
		this.add(this.lblExportFile);
		this.add(this.btnExport);
		this.add(this.lblExport);
		this.add(this.lblAuthor);

		final int height = 20;
		final int margin = height + 10;
		final int x = 20;
		int y = 10;
		this.lblInstruction.setBounds(x, y, 560, height * 4 + 10);
		y += 110;
		this.lblDocument.setBounds(x, y, 500, height);
		y += margin;
		this.btnDocument.setBounds(x, y, 100, height);
		this.lblDocumentFile.setBounds(x + 120, y, 400, height);
		y += 40;
		this.lblTable.setBounds(x, y, 60, height);
		this.txtTable.setBounds(80, y, 150, height);
		this.lblId.setBounds(300, y, 20, height);
		this.txtId.setBounds(330, y, 150, height);
		this.yScrollColumns = y + 30;
		y += 190;
		this.btnTransform.setBounds(x, y, 35, height + 10);
		this.lblTransform.setBounds(x + 45, y, 200, height + 10);
		y += 70;
		this.lblExportPath.setBounds(x, y, 100, height);
		this.lblExportPathWrn.setBounds(x + 120, y, 400, height);
		y += margin;
		this.btnExportPath.setBounds(x, y, 100, height);
		this.lblExportFile.setBounds(x + 120, y, 400, height);
		y += 50;
		this.btnExport.setBounds(x, y, 35, height + 10);
		this.lblExport.setBounds(x + 45, y, 200, height + 10);
		y += 50;
		this.lblAuthor.setBounds(dimension.width - 150, y, 120, height);

		this.lblDocument.setToolTipText(MainView.LOC.getRes("lblDocumentToolTip"));
		this.lblTable.setToolTipText(MainView.LOC.getRes("lblTableToolTip"));
		this.lblId.setToolTipText(MainView.LOC.getRes("lblIdToolTip"));
		this.lblTransform.setToolTipText(MainView.LOC.getRes("lblTransformToolTip"));
		this.lblExportPath.setToolTipText(MainView.LOC.getRes("lblExportPathToolTip"));

		this.lblDocumentFile.setForeground(DUColor.LBL_BLUE);
		this.lblExportFile.setForeground(DUColor.LBL_BLUE);
		this.lblExportPathWrn.setForeground(DUColor.LBL_ORANGE);
		this.lblAuthor.setForeground(DUColor.LBL_BLUE);

		this.btnDocument.addActionListener(e -> {
			this.btnFileChooserActionPerformed(this.btnDocument, MainView.LOC.getRes("jfcDocument"),
					this.lblDocumentFile);
			this.updateGraphics();
		});
		this.btnTransform.addActionListener(e -> {
			this.btnMatchActionPerformed();
			this.updateGraphics();
		});
		this.btnExportPath.addActionListener(e -> {
			this.btnFileChooserActionPerformed(this.btnExport, MainView.LOC.getRes("jfcExport"), this.lblExportFile);
			this.updateGraphics();
		});
		this.btnExport.addActionListener(e -> this.btnExportActionPerformed());
	}

	private void init() {
		this.txtId.setText(MainView.DEFAULT_ID);
		this.updateGraphics();
		this.setVisible(true);
	}

	private void updateGraphics() {
		this.txtTable.setEnabled(this.documentFile != null);
		this.txtId.setEnabled(this.documentFile != null);
		for (final ColumnPanel columnPanel : this.columnPanels) {
			columnPanel.setEnabled(this.documentFile != null);
		}
		this.btnTransform.setEnabled(this.documentFile != null);
		this.btnExportPath.setEnabled(this.exportRows != null && !this.exportRows.isEmpty());
		this.btnExport.setEnabled(this.exportFile != null);

		this.lblExportPathWrn.setVisible(MainView.checkFile(this.exportFile, false));
	}

	private void recalculateColumnPanels() {
		this.pnlColumns.removeAll();
		this.remove(this.scrollColumns);

		int y = 0;
		ColumnPanel columnPanel;
		for (final ColumnPanel element : this.columnPanels) {
			columnPanel = element;
			columnPanel.setBounds(0, y, 460, 30);
			this.pnlColumns.add(columnPanel);
			y += 30;
		}

		// Don't move
		this.pnlColumns.setPreferredSize(new Dimension(490, y));
		if (y > MainView.DEFAULT_MAX_COLUMN_VIEW * 30) {
			y = MainView.DEFAULT_MAX_COLUMN_VIEW * 30;
		}

		this.scrollColumns = new JScrollPane(this.pnlColumns);
		this.scrollColumns.setBorder(null);
		this.scrollColumns.getVerticalScrollBar().setUnitIncrement(20); // 2 row at a time
		this.scrollColumns.setBounds(20, this.yScrollColumns, 510, y);
		this.add(this.scrollColumns);
		this.updateGraphics();
		this.repaint();
		this.pack();
	}

	private void btnFileChooserActionPerformed(final DUButton caller, final String title, final JLabel label) {
		final FileNameExtensionFilter filter = new FileNameExtensionFilter("MS Excel Document (2007+)", "xlsx");
		final JFileChooser jfc = new JFileChooser(MainView.DESKTOP_FOLDER);
		jfc.setFileFilter(filter);
		jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		jfc.setMultiSelectionEnabled(false);
		jfc.setAcceptAllFileFilterUsed(false);
		jfc.setDialogTitle(title);

		final int retVaule = jfc.showOpenDialog(caller);
		if (retVaule == JFileChooser.APPROVE_OPTION) {
			File file = jfc.getSelectedFile();
			if (caller == this.btnDocument) {
				this.documentFile = file;
				this.sheetName = null;
				this.importedRows = null;
				this.exportRows = null;
				this.exportQueries = null;
			} else {
				if (!file.getName().endsWith(MainView.FILE_EXTENSION)) {
					file = new File(file.getAbsolutePath() + MainView.FILE_EXTENSION);
				}
				this.exportFile = file;
			}
			label.setText(file.getName());
		} else if (retVaule == JFileChooser.CANCEL_OPTION) {
			if (caller == this.btnDocument) {
				this.documentFile = null;
				this.sheetName = null;
				this.importedRows = null;
				this.exportRows = null;
				this.exportQueries = null;
			} else {
				this.exportFile = null;
			}
			label.setText(null);
		}
	}

	private void btnMatchActionPerformed() {
		if (!this.checkParams()) {
			return;
		}

		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		ImportCore.importRows(this, this.documentFile);
		this.setCursor(Cursor.getDefaultCursor());
		this.updateGraphics();
	}

	private void btnExportActionPerformed() {
		if (!this.checkExportParams()) {
			return;
		}

		if (ExportCore.doExport(this, this.exportFile.getAbsolutePath(),
				this.sheetName != null && !"".equals(this.sheetName) ? this.sheetName : "Foglio 1", this.exportRows,
				"All queries", this.exportQueries)) {
			if (DUMessage.showConfirmDialog(this, MainView.LOC.getRes("cnfExported"))) {
				try {
					Desktop.getDesktop().open(this.exportFile);
				} catch (final IOException e) {
					DUMessage.showErrDialog(this, MainView.LOC.getRes("errInternalError"));
					e.printStackTrace();
				}
			}

			this.lblDocumentFile.setText(null);
			this.lblExportFile.setText(null);
			this.txtTable.setText(null);
			this.txtId.setText(MainView.DEFAULT_ID);
			for (final ColumnPanel columnPanel : this.columnPanels) {
				columnPanel.clear();
			}
			this.documentFile = null;
			this.exportFile = null;
			this.sheetName = null;
			this.importedRows = null;
			this.exportRows = null;
			this.exportQueries = null;
			this.updateGraphics();
		}
	}

	private boolean checkParams() {
		if (!MainView.checkFile(this.documentFile, false)) {
			DUMessage.showErrDialog(this, MainView.LOC.getRes("errDocumentFile"));
			return false;
		}

		if (this.txtTable.isEmpty()) {
			DUMessage.showErrDialog(this, MainView.LOC.getRes("errTable"));
			return false;
		}
		if (this.txtId.isEmpty()) {
			DUMessage.showErrDialog(this, MainView.LOC.getRes("errId"));
			return false;
		}
		for (final ColumnPanel columnPanel : this.columnPanels) {
			if (!columnPanel.checkParams()) {
				DUMessage.showErrDialog(this, MainView.LOC.getRes("errColumnView"));
				return false;
			}
		}

		return true;
	}

	private boolean checkExportParams() {
		if (!MainView.checkFile(this.exportFile, true)) {
			DUMessage.showErrDialog(this, MainView.LOC.getRes("errExportPath"));
			return false;
		}

		return true;
	}

	private static boolean checkFile(final File file, final boolean createIfAbsent) {
		if (file == null || !file.exists() || !file.isFile()) {
			if (createIfAbsent) {
				try {
					file.createNewFile();
					return true;
				} catch (final IOException e) {
					//TRMessage.showErrDialog(this, MainView.LOC.getRes("errInternalError"));
					e.printStackTrace();
				}
			}
			return false;
		}

		return file.getName().endsWith(MainView.FILE_EXTENSION);
	}

	public void notifyImportCompleted(final String _sheetName, final List<ImportedRow> _importedRows) {
		if (_importedRows == null || _importedRows.isEmpty()) {
			DUMessage.showWarnDialog(this, MainView.LOC.getRes("wrnNoRowsFound"));
			return;
		}

		if (_importedRows.get(0).getCells() == null
				|| _importedRows.get(0).getCells().size() < this.columnPanels.size() + 1) {
			DUMessage.showErrDialog(this, MainView.LOC.getRes("errNotEnoughData"));
			return;
		}

		this.sheetName = _sheetName;
		this.importedRows = _importedRows;
		this.importStrings();
	}

	private void importStrings() {
		final List<ExportRow> _exportRows = new ArrayList<>();
		final ExportRow header = new ExportRow();

		final ImportedRow headerRow = this.importedRows.get(0);
		header.addCell(headerRow.getStringAt(0));
		for (int j = 1; j < headerRow.getCells().size(); j++) { //Skip id (pos = 0)
			header.addCell(headerRow.getStringAt(j));

			if (this.columnPanels.size() > j - 1) {
				header.addCell("Query");
			}
		}
		_exportRows.add(header);

		final int posId = 0;
		String id;
		ImportedRow importedRow;
		ExportRow exportRow;
		Column column;
		final String[] perRowQueries = new String[this.columnPanels.size()];
		final List<String> queries = new ArrayList<>();
		for (int i = 1; i < this.importedRows.size(); i++) { //Ignore header
			importedRow = this.importedRows.get(i);
			id = importedRow.getStringAt(posId);
			exportRow = new ExportRow();

			exportRow.addCell(importedRow.getStringAt(posId));
			for (int j = 1; j < importedRow.getCells().size(); j++) { //Skip id (pos = 0)
				if (this.columnPanels.size() > j - 1) {
					column = this.columnPanels.get(j - 1).getColumn();
					perRowQueries[j - 1] = column.getName()
							+ MainView.getUpdateClause(column, importedRow.getStringAt(j));

					final String query = "UPDATE " + this.txtTable.getText() + " SET " + perRowQueries[j - 1]
							+ " WHERE " + this.txtId.getText() + " = " + id + ";";
					exportRow.addCell(importedRow.getStringAt(j));
					exportRow.addCell(query);
				} else {
					exportRow.addCell(importedRow.getStringAt(j));
				}
			}

			_exportRows.add(exportRow);

			String query = "UPDATE " + this.txtTable.getText() + " SET ";
			for (int j = 0; j < this.columnPanels.size(); j++) {
				query += perRowQueries[j] + " AND ";
			}
			query = query.substring(0, query.length() - " AND ".length());
			query += " WHERE " + this.txtId.getText() + " = " + id + ";";
			queries.add(query);
		}

		final List<ExportRow> _exportQueries = new ArrayList<>();
		for (final String query : queries) {
			_exportQueries.add(new ExportRow().addCell(query));
		}

		DUMessage.showInfoDialog(this, MainView.LOC.getRes("infQueryCreated"));
		this.exportRows = _exportRows;
		this.exportQueries = _exportQueries;
	}

	private static String getUpdateClause(final Column column, final String _value) {
		final String value = _value != null ? _value.trim() : _value;
		switch (column.getType()) {
		case STRING:
			return value == null || "".equals(value) ? " = NULL "
					: " = \"" + value.replace("'", "\\\\'").replace("\"", "\\\\\"") + "\" ";

		case BOOL:
			return value == null || "".equals(value.toLowerCase()) || "false".equals(value.toLowerCase())
					|| "no".equals(value.toLowerCase()) || "0".equals(value) ? " = 0 " : " = 1 ";
		case INT:
			return value == null || !MainView.isInteger(value) ? "" : " = " + Integer.parseInt(value);
		case DOUBLE:
			if (value != null) {
				if ((value.contains(",") || value.contains(".")) && MainView.isDouble(value.replaceAll(",", "."))) {
					return " = " + Double.parseDouble(value.replaceAll(",", "."));
				} else if (MainView.isInteger(value)) {
					return " = " + Integer.parseInt(value);
				}
				return "";
			}
			return "";
		default:
			return "";
		}

	}

	public void addColumnPanel(final ColumnPanel prevColumnPanel) {
		final ColumnPanel columnPanel = new ColumnPanel(this);
		this.columnPanels.add(this.columnPanels.indexOf(prevColumnPanel) + 1, columnPanel);
		this.recalculateColumnPanels();
	}

	public void removeColumnPanel(final ColumnPanel columnPanel) {
		if (this.columnPanels.size() == 1) {
			columnPanel.clear();
			return;
		}
		this.columnPanels.remove(columnPanel);
		this.pnlColumns.remove(columnPanel);
		this.recalculateColumnPanels();
	}

	private static boolean isInteger(final String string) {
		if (string == null) {
			return false;
		}

		try {
			Integer.parseInt(string);
			return true;
		} catch (final NumberFormatException e) {
			e.printStackTrace();
			return false;
		}
	}

	private static boolean isDouble(final String string) {
		if (string == null) {
			return false;
		}

		try {
			Double.parseDouble(string);
			return true;
		} catch (final NumberFormatException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static void main(final String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				final MainView frame = new MainView();
				frame.setVisible(true);
			} catch (final Exception e) {
				e.printStackTrace();
			}
		});

	}
}
