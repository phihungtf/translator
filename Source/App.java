import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import javax.swing.JList;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import javax.swing.table.AbstractTableModel;

import dialog.InputFrame;
import favorite.WordItem;
import favorite.WordList;
import record.*;


class FavWordTableModel extends AbstractTableModel {
	private WordList list;
	private String[] columns;
	public boolean isUpdated = false;

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
		this.isUpdated = true;
    }

	// Optional, the name of your column
	public String getColumnName(int col) {
		return columns[col];
	}

	public void addRow(WordItem word) {
		list.add(word);
		fireTableRowsInserted(list.size() - 1, list.size() - 1);
	}

	public void removeRow(int index) {
		list.remove(index);
		fireTableRowsDeleted(index, index);
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
		System.out.println("Type: " + selected.getClass().getName());
		if (selected instanceof Word) {
			this.selectedItem = (Word) selected;
		} else if (selected instanceof String) {
			for (Word word : data) {
				if (word.getWord().equals(selected)) {
					this.selectedItem = word;
					return;
				}
			}
			this.selectedItem = new Word((String) selected, -1);
		}
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
	public File vienFile = new File("Files/Viet_Anh.xml");
	public File enviFile = new File("Files/test.xml");

	public MyComboBoxModel wordInputBoxModel;
	public FavWordTableModel favWordTableModel;

	public App() {
		recordList = new RecordList();
		try {
			if (lang.equals("en-vi")) {
				recordList.parseXML(enviFile);
			} else {
				recordList.parseXML(vienFile);
			}
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}

		initComponents();

		removeBtn.setEnabled(false);
		markFavBtn.setEnabled(false);

		System.out.println("Init components done");
		// recordBox.setText(curRecordList.toString());

	}

	private void initComponents() {
		java.awt.GridBagConstraints gridBagConstraints;
		TabbedPane = new javax.swing.JTabbedPane();
		translatorTab = new javax.swing.JPanel();
		translatorPanel = new javax.swing.JPanel();
		wordInputPanel = new javax.swing.JPanel();
		langSwitchBtn = new javax.swing.JButton();
		// wordInput = new javax.swing.JTextField();
		wordInputBox = new javax.swing.JComboBox<Word>();
		findBtn = new javax.swing.JButton();
		recordPanel = new javax.swing.JScrollPane();
		recordBox = new javax.swing.JTextArea();
		controlPanel = new javax.swing.JPanel();
		addBtn = new javax.swing.JButton();
		removeBtn = new javax.swing.JButton();
		markFavBtn = new javax.swing.JButton();
		toggleFavBtn = new javax.swing.JToggleButton();
		favoritePanel = new javax.swing.JPanel();
		favWordLabel = new javax.swing.JLabel();
		favWordPanel = new javax.swing.JScrollPane();
		favWordTable = new javax.swing.JTable();
		favCtrlPanel = new javax.swing.JPanel();
		removeFavBtn = new javax.swing.JButton();
		historyTab = new javax.swing.JPanel();
		dateInputPanel = new javax.swing.JPanel();
		fromLabel = new javax.swing.JLabel();
		fromDateInput = new javax.swing.JFormattedTextField();
		toLabel = new javax.swing.JLabel();
		toDateInput = new javax.swing.JFormattedTextField();
		findHistoryBtn = new javax.swing.JButton();
		historyPanel = new javax.swing.JScrollPane();
		historyTable = new javax.swing.JTable();
		preferencesTab = new javax.swing.JPanel();
		filesSettingPanel = new javax.swing.JPanel();
		enviDicLabel = new javax.swing.JLabel();
		enviDicFileInput = new javax.swing.JTextField();
		enviDicFileBtn = new javax.swing.JButton();
		vienDicLabel = new javax.swing.JLabel();
		vienDicFileInput = new javax.swing.JTextField();
		vienDicFilebtn = new javax.swing.JButton();
		favLabel = new javax.swing.JLabel();
		favFileInput = new javax.swing.JTextField();
		favFileBtn = new javax.swing.JButton();
		historyLabel = new javax.swing.JLabel();
		historyFileInput = new javax.swing.JTextField();
		historyFileBtn = new javax.swing.JButton();
		aboutPanel = new javax.swing.JPanel();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setMinimumSize(new java.awt.Dimension(1000, 0));
		setPreferredSize(new java.awt.Dimension(500, 400));

		translatorTab.setLayout(new javax.swing.BoxLayout(translatorTab, javax.swing.BoxLayout.LINE_AXIS));

		translatorPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 8, 8, 8));
		translatorPanel.setLayout(new java.awt.BorderLayout(0, 5));

		wordInputPanel.setLayout(new java.awt.GridBagLayout());

		langSwitchBtn.setText("EN→VI");
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

		favoritePanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 0, 8, 8));
		favoritePanel.setMaximumSize(new java.awt.Dimension(400, 2147483647));
		favoritePanel.setMinimumSize(new java.awt.Dimension(200, 89));
		favoritePanel.setPreferredSize(new java.awt.Dimension(250, 100));
		favoritePanel.setLayout(new java.awt.BorderLayout(0, 5));

		favWordLabel.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
		favWordLabel.setText("Favorite Words");
		favWordLabel.setAlignmentX(0.5F);
		favWordLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 4, 1));
		favWordLabel.setMinimumSize(new java.awt.Dimension(93, 20));
		favWordLabel.setPreferredSize(new java.awt.Dimension(100, 23));
		favoritePanel.add(favWordLabel, java.awt.BorderLayout.NORTH);

		favWordPanel.setPreferredSize(new java.awt.Dimension(200, 400));
		favWordTableModel = new FavWordTableModel(new WordList());
		favWordTable.setModel(favWordTableModel);
		// favWordTable.setPreferredSize(new java.awt.Dimension(200, 80));
		favWordTable.getTableHeader().setReorderingAllowed(false);
		favWordPanel.setViewportView(favWordTable);

		favoritePanel.add(favWordPanel, java.awt.BorderLayout.CENTER);

		favCtrlPanel.setLayout(new java.awt.GridBagLayout());

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

		historyTab.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 8, 8, 8));
		historyTab.setLayout(new java.awt.BorderLayout());

		dateInputPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 5, 1));
		dateInputPanel.setLayout(new java.awt.GridBagLayout());

		fromLabel.setText("From");
		dateInputPanel.add(fromLabel, new java.awt.GridBagConstraints());

		fromDateInput.setColumns(10);
		fromDateInput.setFormatterFactory(
				new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter()));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
		dateInputPanel.add(fromDateInput, gridBagConstraints);

		toLabel.setText("To");
		dateInputPanel.add(toLabel, new java.awt.GridBagConstraints());

		toDateInput.setColumns(10);
		toDateInput.setFormatterFactory(
				new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter()));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
		dateInputPanel.add(toDateInput, gridBagConstraints);

		findHistoryBtn.setText("Find");
		dateInputPanel.add(findHistoryBtn, new java.awt.GridBagConstraints());

		historyTab.add(dateInputPanel, java.awt.BorderLayout.PAGE_START);

		historyTable.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 8, 8, 8));
		historyTable.setModel(new javax.swing.table.DefaultTableModel(
			new Object[][] {},
			new String[] {
				"No.", "Word", "Frequency", "Lang"
			}) {
			boolean[] canEdit = new boolean[] {
				false, false, false, false
			};

			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit[columnIndex];
			}
		});
		historyPanel.setViewportView(historyTable);

		historyTab.add(historyPanel, java.awt.BorderLayout.CENTER);

		TabbedPane.addTab("History", historyTab);

		preferencesTab.setLayout(new java.awt.BorderLayout());

		filesSettingPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 8, 8, 8));
		filesSettingPanel.setLayout(new java.awt.GridBagLayout());

		enviDicLabel.setText("EN-VI Dictionary:");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
		filesSettingPanel.add(enviDicLabel, gridBagConstraints);

		enviDicFileInput.setEditable(false);
		enviDicFileInput.setColumns(20);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 8, 8, 8);
		filesSettingPanel.add(enviDicFileInput, gridBagConstraints);

		enviDicFileBtn.setText("Change");
		enviDicFileBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				enviDicFileBtnActionPerformed(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		filesSettingPanel.add(enviDicFileBtn, gridBagConstraints);

		vienDicLabel.setText("VI-EN Dictionary:");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
		filesSettingPanel.add(vienDicLabel, gridBagConstraints);

		vienDicFileInput.setEditable(false);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 8, 8, 8);
		filesSettingPanel.add(vienDicFileInput, gridBagConstraints);

		vienDicFilebtn.setText("Change");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		filesSettingPanel.add(vienDicFilebtn, gridBagConstraints);

		favLabel.setText("Favorite:");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
		filesSettingPanel.add(favLabel, gridBagConstraints);

		favFileInput.setEditable(false);
		favFileInput.setColumns(20);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 8, 8, 8);
		filesSettingPanel.add(favFileInput, gridBagConstraints);

		favFileBtn.setText("Change");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridy = 2;
		gridBagConstraints.gridheight = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		filesSettingPanel.add(favFileBtn, gridBagConstraints);

		historyLabel.setText("History:");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
		filesSettingPanel.add(historyLabel, gridBagConstraints);

		historyFileInput.setEditable(false);
		historyFileInput.setColumns(20);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 8, 8, 8);
		filesSettingPanel.add(historyFileInput, gridBagConstraints);

		historyFileBtn.setText("Change");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridy = 3;
		gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		filesSettingPanel.add(historyFileBtn, gridBagConstraints);

		preferencesTab.add(filesSettingPanel, java.awt.BorderLayout.NORTH);

		javax.swing.GroupLayout aboutPanelLayout = new javax.swing.GroupLayout(aboutPanel);
		aboutPanel.setLayout(aboutPanelLayout);
		aboutPanelLayout.setHorizontalGroup(
				aboutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGap(0, 624, Short.MAX_VALUE));
		aboutPanelLayout.setVerticalGroup(
				aboutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGap(0, 233, Short.MAX_VALUE));

		preferencesTab.add(aboutPanel, java.awt.BorderLayout.CENTER);

		TabbedPane.addTab("Preferences", preferencesTab);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addComponent(TabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 624, Short.MAX_VALUE));
		layout.setVerticalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addComponent(TabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE));

		pack();
	}

	protected void removeFavBtnActionPerformed(ActionEvent evt) {
		// get row
		int row = favWordTable.getSelectedRow();
		// delete
		if (row >= 0) {
			favWordTableModel.removeRow(row);
		}
	}

	protected void markFavBtnActionPerformed(ActionEvent evt) {
		Word word = (Word) wordInputBox.getSelectedItem();
		int index = word.getId();
		if (index >= 0) {
			favWordTableModel.addRow(new WordItem(word.getWord(), lang.substring(0, 2)));
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
				langSwitchBtn.setText("VI-EN");
				recordList.parseXML(vienFile);
			} else {
				lang = "en-vi";
				langSwitchBtn.setText("EN-VI");
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

	private void enviDicFileBtnActionPerformed(java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
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
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Windows".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(App.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(App.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(App.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
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

	private javax.swing.JTabbedPane TabbedPane;
	private javax.swing.JPanel aboutPanel;
	private javax.swing.JButton addBtn;
	private javax.swing.JPanel controlPanel;
	private javax.swing.JPanel dateInputPanel;
	private javax.swing.JButton enviDicFileBtn;
	private javax.swing.JTextField enviDicFileInput;
	private javax.swing.JLabel enviDicLabel;
	private javax.swing.JPanel favCtrlPanel;
	private javax.swing.JButton favFileBtn;
	private javax.swing.JTextField favFileInput;
	private javax.swing.JLabel favLabel;
	private javax.swing.JLabel favWordLabel;
	private javax.swing.JScrollPane favWordPanel;
	private javax.swing.JTable favWordTable;
	private javax.swing.JPanel favoritePanel;
	private javax.swing.JPanel filesSettingPanel;
	private javax.swing.JButton findBtn;
	private javax.swing.JButton findHistoryBtn;
	private javax.swing.JFormattedTextField fromDateInput;
	private javax.swing.JLabel fromLabel;
	private javax.swing.JButton historyFileBtn;
	private javax.swing.JTextField historyFileInput;
	private javax.swing.JLabel historyLabel;
	private javax.swing.JScrollPane historyPanel;
	private javax.swing.JPanel historyTab;
	private javax.swing.JTable historyTable;
	private javax.swing.JButton langSwitchBtn;
	private javax.swing.JButton markFavBtn;
	private javax.swing.JPanel preferencesTab;
	private javax.swing.JTextArea recordBox;
	private javax.swing.JScrollPane recordPanel;
	private javax.swing.JButton removeBtn;
	private javax.swing.JButton removeFavBtn;
	private javax.swing.JFormattedTextField toDateInput;
	private javax.swing.JLabel toLabel;
	private javax.swing.JToggleButton toggleFavBtn;
	private javax.swing.JPanel translatorPanel;
	private javax.swing.JPanel translatorTab;
	private javax.swing.JTextField vienDicFileInput;
	private javax.swing.JButton vienDicFilebtn;
	private javax.swing.JLabel vienDicLabel;
	// private javax.swing.JTextField wordInput;
	private javax.swing.JComboBox<Word> wordInputBox;
	private javax.swing.JPanel wordInputPanel;
	// End of variables declaration
}
