import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import javax.swing.table.*;

import dialog.InputFrame;
import favorite.WordItem;
import favorite.WordList;
import history.*;
import record.*;


class FavWordTableModel extends AbstractTableModel {
	private WordList list;
	private String[] columns;

	public FavWordTableModel(WordList list) {
		super();
		this.list = list;
		columns = new String[] {"No.", "Word", "Lang"};
	}

	// Number of column of your table
	public int getColumnCount() {
		return columns.length;
	}

	// Number of row of your table
	public int getRowCount() {
		return list.size();
	}

	// The object to render in a cell
	public Object getValueAt(int row, int col) {
		if (col == 0) return row + 1;
		return list.getValueAt(row, col - 1);
	}

	/**
	 * This method returns true if the cell at the given row and column is
	 * editable. Otherwise, it returns false.
	 */
	public boolean isCellEditable(int row, int col) {
		return false;
	}

	/**
	 * This method is called whenever a value in the table is changed.
	 * 
	 * @param value the new value
	 * @param row the row of the cell that was changed
	 * @param col the column of the cell that was changed
	 */
	public void setValueAt(Object value, int row, int col) {
		list.setValueAt(value, row, col - 1);
        fireTableCellUpdated(row, col);
    }

	// Optional, the name of your column
	public String getColumnName(int col) {
		return columns[col];
	}
}

class HistoryTableModel extends AbstractTableModel {
	private ArrayList<LogRow> data;
	private String[] columns;

	public HistoryTableModel(LogList list, LocalDate fromDate, LocalDate toDate) {
		super();
		columns = new String[] {"No.", "Word", "Frequency", "Lang"};
		data = list.getLogRows(fromDate, toDate);
	}

	public void update(LogList list, LocalDate fromDate, LocalDate toDate) {
		data = list.getLogRows(fromDate, toDate);
		fireTableDataChanged();
	}

	// Number of column of your table
	public int getColumnCount() {
		return columns.length;
	}

	// Number of row of your table
	public int getRowCount() {
		return data.size();
	}

	// The object to render in a cell
	public Object getValueAt(int row, int col) {
		if (col == 0) return row + 1;
		switch (col) {
			case 1: return data.get(row).getWord();
			case 2: return data.get(row).getFrequency();
			case 3: return data.get(row).getLang();
			default: return null;
		}
	}

	/**
	 * This method returns true if the cell at the given row and column is
	 * editable. Otherwise, it returns false.
	 */
	public boolean isCellEditable(int row, int col) {
		return false;
	}

	/**
	 * This method is called whenever a value in the table is changed.
	 * 
	 * @param value the new value
	 * @param row the row of the cell that was changed
	 * @param col the column of the cell that was changed
	 */
	public void setValueAt(Object value, int row, int col) {
        fireTableCellUpdated(row, col);
    }

	// Optional, the name of your column
	public String getColumnName(int col) {
		return columns[col];
	}
}

class MyComboBoxModel extends AbstractListModel<Word> implements ComboBoxModel<Word> {
	private ArrayList<Word> data;
	private Word selectedItem = new Word(null, -1);

	public MyComboBoxModel(ArrayList<Word> data) {
		this.data = data;
	}

	@Override
	public int getSize() {
		return data.size();
	}

	@Override
	public Word getElementAt(int index) {
		return data.get(index);
	}

	@Override
	public void setSelectedItem(Object selected) {
		if (selected instanceof Word) {
			this.selectedItem = (Word) selected;
			fireContentsChanged(selected, -1, -1);
			return;
		}
		if (selected instanceof String) {
			for (Word word : data) {
				if (word.getWord().equals(selected)) {
					this.selectedItem = word;
					fireContentsChanged(selected, -1, -1);
					return;
				}
			}
		}
		this.selectedItem = new Word((String) selected, -1);
		fireContentsChanged(selected, -1, -1);
	}

	@Override
	public Word getSelectedItem() {
		return selectedItem;
	}

	public void setData(ArrayList<Word> data) {
		this.data = data;
		fireContentsChanged(this, 0, getSize());
	}

	public void removeElementAt(int index) {
		data.remove(index);
		this.selectedItem = new Word(null, -1);
		fireContentsChanged(this, index, index + 1);
	}
}

class ItemRenderer extends BasicComboBoxRenderer {
	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {
		super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		if (value != null) {
			Word word = (Word) value;
			setText(word.getWord());
		}

		if (index == -1) {
			Word word = (Word) value;
			setText("" + word.getId());
		}

		return this;
	}
}

public class App extends javax.swing.JFrame implements InputFrame.InputListener {
	public String lang = "en-vi";
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	public File vienFile = new File("Files/Viet_Anh.xml");
	public File enviFile = new File("Files/Anh_Viet.xml");
	public File favFile = new File("Files/favorite.csv");
	public File historyFile = new File("Files/history.csv");

	public MyComboBoxModel wordInputBoxModel;
	public FavWordTableModel favWordTableModel;
	public HistoryTableModel historyTableModel;

	public App() {
		setTitle("Translator");
		recordList = new RecordList();
		wordList = new WordList();
		logList = new LogList();
		try {
			if (lang.equals("en-vi")) {
				recordList.parseXML(enviFile);
			} else {
				recordList.parseXML(vienFile);
			}
			wordList.readCSV(favFile);
			logList.readCSV(historyFile);
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}

		initComponents();

		removeBtn.setEnabled(false);
		markFavBtn.setEnabled(false);

		System.out.println("Init components done");

	}

	private void initComponents() {
		java.awt.GridBagConstraints gridBagConstraints;
		TabbedPane = new JTabbedPane();
		translatorTab = new JPanel();
		translatorPanel = new JPanel();
		wordInputPanel = new JPanel();
		langSwitchBtn = new JButton();
		wordInputBox = new JComboBox<Word>();
		findBtn = new JButton();
		recordPanel = new JScrollPane();
		recordBox = new JTextArea();
		controlPanel = new JPanel();
		addBtn = new JButton();
		removeBtn = new JButton();
		markFavBtn = new JButton();
		toggleFavBtn = new JToggleButton();
		favoritePanel = new JPanel();
		favWordLabel = new JLabel();
		favWordPanel = new JScrollPane();
		favWordTable = new JTable();
		favCtrlPanel = new JPanel();
		removeFavBtn = new JButton();
		getMeaningBtn = new JButton();
		historyTab = new JPanel();
		dateInputPanel = new JPanel();
		fromLabel = new JLabel();
		fromDateInput = new JFormattedTextField();
		toLabel = new JLabel();
		toDateInput = new JFormattedTextField();
		findHistoryBtn = new JButton();
		historyPanel = new JScrollPane();
		historyTable = new JTable();
		// favFileInput = new JTextField();
		// favFileBtn = new JButton();
		// preferencesTab = new JPanel();
		// fileSettingsPanel = new JPanel();
		// enviDicLabel = new JLabel();
		// enviDicFileInput = new JTextField();
		// enviDicFileBtn = new JButton();
		// vienDicLabel = new JLabel();
		// vienDicFileInput = new JTextField();
		// vienDicFilebtn = new JButton();
		// favLabel = new JLabel();
		// historyLabel = new JLabel();
		// historyFileInput = new JTextField();
		// historyFileBtn = new JButton();
		// aboutPanel = new JPanel();

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setMinimumSize(new java.awt.Dimension(1000, 0));
		setPreferredSize(new java.awt.Dimension(500, 400));

		translatorTab.setLayout(new BoxLayout(translatorTab, BoxLayout.LINE_AXIS));

		translatorPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		translatorPanel.setLayout(new java.awt.BorderLayout(0, 5));

		wordInputPanel.setLayout(new java.awt.GridBagLayout());

		langSwitchBtn.setText("EN->VI");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;

		langSwitchBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				langSwitchBtnActionPerformed(evt);
			}
		});

		wordInputPanel.add(langSwitchBtn, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);

		wordInputBox.setEditable(true);
		wordInputBox.setRenderer(new ItemRenderer());
		wordInputBoxModel = new MyComboBoxModel(recordList.getWordList());
		wordInputBox.setModel(wordInputBoxModel);
		wordInputBox.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				findBtnActionPerformed(evt);
			}
		});
		wordInputPanel.add(wordInputBox, gridBagConstraints);

		findBtn.setText("Find");
		findBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				findBtnActionPerformed(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		wordInputPanel.add(findBtn, gridBagConstraints);

		translatorPanel.add(wordInputPanel, java.awt.BorderLayout.NORTH);

		recordBox.setLineWrap(true);
		recordBox.setWrapStyleWord(true);
		recordBox.setName(""); // NOI18N
		recordBox.setFont(new java.awt.Font("Tahoma", 0, 14));
		recordBox.setMargin(new java.awt.Insets(5, 5, 5, 5));
		recordBox.setEditable(false);
		recordPanel.setViewportView(recordBox);

		translatorPanel.add(recordPanel, java.awt.BorderLayout.CENTER);

		controlPanel.setLayout(new java.awt.GridBagLayout());

		addBtn.setText(" Add new word");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
		addBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				addBtnActionPerformed(evt);
			}
		});
		controlPanel.add(addBtn, gridBagConstraints);

		removeBtn.setText("Remove this word");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;

		removeBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				removeBtnActionPerformed(evt);
			}
		});
		controlPanel.add(removeBtn, gridBagConstraints);

		markFavBtn.setText("Mark as favorite");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);

		markFavBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				markFavBtnActionPerformed(evt);
			}
		});
		controlPanel.add(markFavBtn, gridBagConstraints);

		toggleFavBtn.setText(">>");
		toggleFavBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				toggleFavBtnActionPerformed(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		controlPanel.add(toggleFavBtn, gridBagConstraints);

		translatorPanel.add(controlPanel, java.awt.BorderLayout.SOUTH);

		translatorTab.add(translatorPanel);

		favoritePanel.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 8));
		favoritePanel.setMaximumSize(new java.awt.Dimension(400, 2147483647));
		favoritePanel.setMinimumSize(new java.awt.Dimension(200, 89));
		favoritePanel.setPreferredSize(new java.awt.Dimension(250, 100));
		favoritePanel.setLayout(new java.awt.BorderLayout(0, 5));

		favWordLabel.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
		favWordLabel.setText("Favorite Words");
		favWordLabel.setAlignmentX(0.5F);
		favWordLabel.setBorder(BorderFactory.createEmptyBorder(1, 1, 4, 1));
		favWordLabel.setMinimumSize(new java.awt.Dimension(93, 20));
		favWordLabel.setPreferredSize(new java.awt.Dimension(100, 23));
		favoritePanel.add(favWordLabel, java.awt.BorderLayout.NORTH);

		favWordPanel.setPreferredSize(new java.awt.Dimension(200, 400));
		favWordTableModel = new FavWordTableModel(wordList);
		favWordTable.setModel(favWordTableModel);
		// favWordTable.setPreferredSize(new java.awt.Dimension(200, 80));
		favWordTable.getTableHeader().setReorderingAllowed(false);

		int[] preferredWidth = {10, 80, 10};
		for (int i = 0; i < 3; i++) {
			TableColumn column = favWordTable.getColumnModel().getColumn(i);
			column.setPreferredWidth(preferredWidth[i]);
		}
		favWordTable.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent mouseEvent) {
				if (mouseEvent.getClickCount() == 2 && favWordTable.getSelectedRow() != -1) {
					// your valueChanged overridden method
					getMeaningFromFavorite();
				}
			}
		});
		favWordPanel.setViewportView(favWordTable);

		favoritePanel.add(favWordPanel, java.awt.BorderLayout.CENTER);

		favCtrlPanel.setLayout(new java.awt.GridBagLayout());

		getMeaningBtn.setText("Get meaning");
		getMeaningBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				getMeaningBtnActionPerformed(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
		favCtrlPanel.add(getMeaningBtn, gridBagConstraints);

		removeFavBtn.setText("Remove from favorite");
		removeFavBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				removeFavBtnActionPerformed(evt);
			}
		});
		favCtrlPanel.add(removeFavBtn, new java.awt.GridBagConstraints());

		favoritePanel.add(favCtrlPanel, java.awt.BorderLayout.SOUTH);

		translatorTab.add(favoritePanel);

		TabbedPane.addTab("Translator", translatorTab);

		historyTab.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		historyTab.setLayout(new java.awt.BorderLayout());

		dateInputPanel.setBorder(BorderFactory.createEmptyBorder(1, 1, 5, 1));
		dateInputPanel.setLayout(new java.awt.GridBagLayout());

		fromLabel.setText("From");
		dateInputPanel.add(fromLabel, new java.awt.GridBagConstraints());

		fromDateInput.setColumns(10);
		fromDateInput.setText(LocalDate.now().format(formatter));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
		dateInputPanel.add(fromDateInput, gridBagConstraints);

		toLabel.setText("To");
		dateInputPanel.add(toLabel, new java.awt.GridBagConstraints());

		toDateInput.setColumns(10);
		toDateInput.setText(LocalDate.now().format(formatter));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
		dateInputPanel.add(toDateInput, gridBagConstraints);

		findHistoryBtn.setText("Find");

		findHistoryBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				findHistoryBtnActionPerformed(evt);
			}
		});
		dateInputPanel.add(findHistoryBtn, new java.awt.GridBagConstraints());

		historyTab.add(dateInputPanel, java.awt.BorderLayout.PAGE_START);

		historyTable.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		historyTableModel = new HistoryTableModel(logList, LocalDate.now(), LocalDate.now());
		historyTable.setModel(historyTableModel);
		historyTable.getTableHeader().setReorderingAllowed(false);

		int[] w = {100, 700, 100, 100};
		for (int i = 0; i < 4; i++) {
			TableColumn column = historyTable.getColumnModel().getColumn(i);
			column.setPreferredWidth(w[i]);
		}

		historyPanel.setViewportView(historyTable);

		historyTab.add(historyPanel, java.awt.BorderLayout.CENTER);

		TabbedPane.addTab("History", historyTab);

		// preferencesTab.setLayout(new java.awt.BorderLayout());

		// fileSettingsPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		// fileSettingsPanel.setLayout(new java.awt.GridBagLayout());

		// enviDicLabel.setText("EN-VI Dictionary:");
		// gridBagConstraints = new java.awt.GridBagConstraints();
		// gridBagConstraints.gridx = 0;
		// gridBagConstraints.gridy = 0;
		// gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
		// fileSettingsPanel.add(enviDicLabel, gridBagConstraints);

		// enviDicFileInput.setEditable(false);
		// enviDicFileInput.setColumns(20);
		// gridBagConstraints = new java.awt.GridBagConstraints();
		// gridBagConstraints.gridx = 1;
		// gridBagConstraints.gridy = 0;
		// gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		// gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		// gridBagConstraints.weightx = 1.0;
		// gridBagConstraints.insets = new java.awt.Insets(0, 8, 8, 8);
		// fileSettingsPanel.add(enviDicFileInput, gridBagConstraints);

		// enviDicFileBtn.setText("Change");
		// gridBagConstraints = new java.awt.GridBagConstraints();
		// gridBagConstraints.gridy = 0;
		// gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		// fileSettingsPanel.add(enviDicFileBtn, gridBagConstraints);

		// vienDicLabel.setText("VI-EN Dictionary:");
		// gridBagConstraints = new java.awt.GridBagConstraints();
		// gridBagConstraints.gridx = 0;
		// gridBagConstraints.gridy = 1;
		// gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
		// fileSettingsPanel.add(vienDicLabel, gridBagConstraints);

		// vienDicFileInput.setEditable(false);
		// gridBagConstraints = new java.awt.GridBagConstraints();
		// gridBagConstraints.gridy = 1;
		// gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		// gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		// gridBagConstraints.weightx = 1.0;
		// gridBagConstraints.insets = new java.awt.Insets(0, 8, 8, 8);
		// fileSettingsPanel.add(vienDicFileInput, gridBagConstraints);

		// vienDicFilebtn.setText("Change");
		// gridBagConstraints = new java.awt.GridBagConstraints();
		// gridBagConstraints.gridy = 1;
		// gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		// fileSettingsPanel.add(vienDicFilebtn, gridBagConstraints);

		// favLabel.setText("Favorite:");
		// gridBagConstraints = new java.awt.GridBagConstraints();
		// gridBagConstraints.gridx = 0;
		// gridBagConstraints.gridy = 2;
		// gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
		// fileSettingsPanel.add(favLabel, gridBagConstraints);

		// favFileInput.setEditable(false);
		// favFileInput.setColumns(20);
		// gridBagConstraints = new java.awt.GridBagConstraints();
		// gridBagConstraints.gridx = 1;
		// gridBagConstraints.gridy = 2;
		// gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		// gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		// gridBagConstraints.weightx = 1.0;
		// gridBagConstraints.insets = new java.awt.Insets(0, 8, 8, 8);
		// fileSettingsPanel.add(favFileInput, gridBagConstraints);

		// favFileBtn.setText("Change");
		// gridBagConstraints = new java.awt.GridBagConstraints();
		// gridBagConstraints.gridy = 2;
		// gridBagConstraints.gridheight = 3;
		// gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		// fileSettingsPanel.add(favFileBtn, gridBagConstraints);

		// historyLabel.setText("History:");
		// gridBagConstraints = new java.awt.GridBagConstraints();
		// gridBagConstraints.gridx = 0;
		// gridBagConstraints.gridy = 3;
		// gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
		// fileSettingsPanel.add(historyLabel, gridBagConstraints);

		// historyFileInput.setEditable(false);
		// historyFileInput.setColumns(20);
		// gridBagConstraints = new java.awt.GridBagConstraints();
		// gridBagConstraints.gridx = 1;
		// gridBagConstraints.gridy = 3;
		// gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		// gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		// gridBagConstraints.weightx = 1.0;
		// gridBagConstraints.insets = new java.awt.Insets(0, 8, 8, 8);
		// fileSettingsPanel.add(historyFileInput, gridBagConstraints);

		// historyFileBtn.setText("Change");
		// gridBagConstraints = new java.awt.GridBagConstraints();
		// gridBagConstraints.gridy = 3;
		// gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
		// gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		// fileSettingsPanel.add(historyFileBtn, gridBagConstraints);

		// fileSettingsPanel.setBorder(new CompoundBorder(new TitledBorder("File Settings "), new EmptyBorder(8, 8, 8, 8)));

		// preferencesTab.add(fileSettingsPanel, java.awt.BorderLayout.NORTH);

		// GroupLayout aboutPanelLayout = new GroupLayout(aboutPanel);
		// aboutPanel.setLayout(aboutPanelLayout);
		// aboutPanelLayout.setHorizontalGroup(
		// 		aboutPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
		// 				.addGap(0, 624, Short.MAX_VALUE));
		// aboutPanelLayout.setVerticalGroup(
		// 		aboutPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
		// 				.addGap(0, 233, Short.MAX_VALUE));

		// preferencesTab.add(aboutPanel, java.awt.BorderLayout.CENTER);

		// TabbedPane.addTab("Preferences", preferencesTab);

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(TabbedPane, GroupLayout.DEFAULT_SIZE, 624, Short.MAX_VALUE));
		layout.setVerticalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(TabbedPane, GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE));

		pack();
	}

	protected void findHistoryBtnActionPerformed(ActionEvent evt) {
		try {
			LocalDate fromDate = LocalDate.parse(fromDateInput.getText(), formatter);
			LocalDate toDate = LocalDate.parse(toDateInput.getText(), formatter);
			historyTableModel.update(logList, fromDate, toDate);
		} catch (DateTimeParseException e) {
			JOptionPane.showMessageDialog(this, "Invalid date format. Please use dd/MM/yyyy format.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

	}

	protected void getMeaningBtnActionPerformed(ActionEvent evt) {
		if (favWordTable.getSelectedRow() < 0) {
			return;
		}
		getMeaningFromFavorite();
	}

	protected void removeFavBtnActionPerformed(ActionEvent evt) {
		// get row
		int row = favWordTable.getSelectedRow();
		// delete
		if (row >= 0) {
			wordList.remove(row);
			favWordTableModel.fireTableRowsDeleted(row, row);
			try {
				wordList.writeCSV(favFile);
			} catch (Exception e) {
				System.out.println("Error: " + e.getMessage());
				e.printStackTrace();
				System.exit(1);
			}
		}
	}

	protected void markFavBtnActionPerformed(ActionEvent evt) {
		Word word = (Word) wordInputBox.getSelectedItem();
		int index = word.getId();
		if (index >= 0) {
			WordItem wordItem = new WordItem(word.getWord(), lang.substring(0, 2));
			wordList.add(wordItem);
			favWordTableModel.fireTableRowsInserted(wordList.size() - 1, wordList.size() - 1);
			try {
				wordList.writeCSV(favFile);
			} catch (Exception e) {
				System.out.println("Error: " + e.getMessage());
				e.printStackTrace();
				System.exit(1);
			}
		}
	}

	protected void removeBtnActionPerformed(ActionEvent evt) {
		Word word = (Word) wordInputBox.getSelectedItem();
		
		int index = word.getId();
		if (index >= 0) {
			// remove from list
			recordList.remove(index);
			// remove from combobox
			wordInputBoxModel.removeElementAt(index);
			// write to file
			try {
				recordList.writeXML(lang == "en-vi" ? enviFile : vienFile);
			} catch (Exception e) {
				System.out.println("Error: " + e.getMessage());
				e.printStackTrace();
				System.exit(1);
			}
			// enable buttons
			removeBtn.setEnabled(false);
			markFavBtn.setEnabled(false);
		}
	}

	private void langSwitchBtnActionPerformed(java.awt.event.ActionEvent evt) {
		// switch language
		recordList = new RecordList();
		try {
			if (lang.equals("en-vi")) {
				lang = "vi-en";
				langSwitchBtn.setText("VI->EN");
				recordList.parseXML(vienFile);
			} else {
				lang = "en-vi";
				langSwitchBtn.setText("EN->VI");
				recordList.parseXML(enviFile);
			}
			// update word list
			wordInputBoxModel.setData(recordList.getWordList());
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
	}

	private void toggleFavBtnActionPerformed(java.awt.event.ActionEvent evt) {
		// togle favoritePanel
		if (favoritePanel.isVisible()) {
			favoritePanel.setVisible(false);
			toggleFavBtn.setText("<<");
		} else {
			favoritePanel.setVisible(true);
			toggleFavBtn.setText(">>");
		}
	}

	private void findBtnActionPerformed(java.awt.event.ActionEvent evt) {
		Word word = (Word) wordInputBox.getSelectedItem();
		
		int index = word.getId();
		if (index >= 0) {
			// show result
			recordBox.setText(recordList.get(index).getMeaning());
			// log
			try {
				logList.add(historyFile, new Log(word.getWord(), lang.substring(0, 2)));
			} catch (Exception e) {
				System.out.println("Error: " + e.getMessage());
				e.printStackTrace();
				System.exit(1);
			}
			// enable buttons
			removeBtn.setEnabled(true);
			markFavBtn.setEnabled(true);
		} else {
			// show error
			recordBox.setText("Not found");
			// disable buttons
			removeBtn.setEnabled(false);
			markFavBtn.setEnabled(false);
		}
	}

	private void addBtnActionPerformed(java.awt.event.ActionEvent evt) {
		// popup add dialog
		InputFrame inputFrame = new InputFrame(App.this);
		inputFrame.setVisible(true);
	}

	@Override
	public void onInput(String word, String meaning) {
		// add word to list
		recordList.add(new RecordItem(word, meaning));
		// write to file
		try {
			recordList.writeXML(lang == "en-vi" ? enviFile : vienFile);
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
		// update word list
		wordInputBoxModel.setData(recordList.getWordList());
	}

	private void getMeaningFromFavorite() {
		// get word
		String word = favWordTable.getValueAt(favWordTable.getSelectedRow(), 1).toString();
		if (word == null) {
			return;
		}
		// get meaning
		RecordItem record = getRecordItemByWord(word);
		if (record != null) {
			wordInputBox.setSelectedItem(record.getWord());
			recordBox.setText(record.getMeaning());
		} else {
			wordInputBox.setSelectedItem(word);
			recordBox.setText("Word does not exist or has been deleted.");
		}
	}

	private RecordItem getRecordItemByWord(String word) {
		for (RecordItem record : recordList.getRecordList()) {
			if (record.getWord().equals(word)) {
				return record;
			}
		}
		return null;
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		/* Set the Nimbus look and feel */
		// <editor-fold defaultstate="collapsed" desc=" Look and feel setting code
		// (optional) ">
		/*
		 * If Nimbus (introduced in Java SE 6) is not available, stay with the default
		 * look and feel.
		 * For details see
		 * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
		 */
		try {
			for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Windows".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(App.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(App.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(App.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(App.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new App().setVisible(true);
			}
		});
	}

	// Variables declaration - do not modify
	private RecordList recordList;
	private WordList wordList;
	private LogList logList;

	private JTabbedPane TabbedPane;
	private JButton addBtn;
	private JPanel controlPanel;
	private JPanel dateInputPanel;
	private JPanel favCtrlPanel;
	private JLabel favWordLabel;
	private JScrollPane favWordPanel;
	private JTable favWordTable;
	private JPanel favoritePanel;
	private JButton findBtn;
	private JButton findHistoryBtn;
	private JFormattedTextField fromDateInput;
	private JLabel fromLabel;
	private JScrollPane historyPanel;
	private JPanel historyTab;
	private JTable historyTable;
	private JButton langSwitchBtn;
	private JButton markFavBtn;
	private JTextArea recordBox;
	private JScrollPane recordPanel;
	private JButton removeBtn;
	private JButton removeFavBtn;
	private JButton getMeaningBtn;
	private JFormattedTextField toDateInput;
	private JLabel toLabel;
	private JToggleButton toggleFavBtn;
	private JPanel translatorPanel;
	private JPanel translatorTab;
	private JComboBox<Word> wordInputBox;
	private JPanel wordInputPanel;
	// private JTextField favFileInput;
	// private JPanel preferencesTab;
	// private JButton historyFileBtn;
	// private JTextField historyFileInput;
	// private JTextField vienDicFileInput;
	// private JButton vienDicFilebtn;
	// private JPanel aboutPanel;
	// private JButton enviDicFileBtn;
	// private JTextField enviDicFileInput;
	// private JLabel enviDicLabel;
	// private JLabel favLabel;
	// private JPanel fileSettingsPanel;
	// private JLabel historyLabel;
	// private JLabel vienDicLabel;
	// private JButton favFileBtn;
	// End of variables declaration
}
