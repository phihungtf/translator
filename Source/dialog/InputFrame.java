package dialog;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InputFrame extends JFrame { 
    
    private JTextField textField;
    private JTextArea textArea;
    private InputListener listener;

	public interface InputListener {
        void onInput(String word, String meaning);
    }
    
	public InputFrame(InputListener listener) {
		super("Input Frame");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
        this.listener = listener;
		// add panel
		JPanel panel = new JPanel();
		// insets
		panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		// add grid bag layout
		panel.setLayout(new GridBagLayout());
		// grid bag layout constraints
		GridBagConstraints gbc = new GridBagConstraints();
	
		// add label
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;
		JLabel label = new JLabel("Word:");
		panel.add(label, gbc);
	
		// add text field
		gbc.gridy = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0.5;
		textField = new JTextField(20);
		panel.add(textField, gbc);
	
		// add more label
		gbc.gridx = 0;
		gbc.gridy = 2;
		JLabel label2 = new JLabel("Meaning:");
		panel.add(label2, gbc);
	
		// add text area
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weighty = 1.0;
		textArea = new JTextArea(10, 20);
		// add scroll bar
		JScrollPane scrollPane = new JScrollPane(textArea);
		panel.add(scrollPane, gbc);

		// add buttons panel
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
		JButton addButton = new JButton("Add");
		addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String word = textField.getText();
                String meaning = textArea.getText();

                // notify the listener of the entered data
                if (listener != null) {
                    listener.onInput(word, meaning);
                }

				// dispose the frame
				dispose();
            }
        });
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// dispose the frame
				dispose();
			}
		});
		buttonsPanel.add(addButton);
		buttonsPanel.add(cancelButton);
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0.0;
		gbc.weighty = 0.0;
		panel.add(buttonsPanel, gbc);
	
		add(panel, BorderLayout.CENTER);
		pack();
	}
}
