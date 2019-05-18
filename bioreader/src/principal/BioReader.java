package principal;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import java.awt.BorderLayout;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.JSplitPane;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.JTextField;
import java.awt.Canvas;
import java.awt.TextField;
import java.awt.TextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class BioReader extends JPanel {
	/**
	 * Create the panel.
	 */
	static String mess;

	public BioReader() {
		super();
		setBorder(new LineBorder(new Color(0, 0, 0), 2));
		setLayout(new BorderLayout(0, 0));

		JLabel lblLeitorBiometrico = new JLabel("Leitor Biometrico");
		lblLeitorBiometrico.setHorizontalAlignment(SwingConstants.CENTER);
		lblLeitorBiometrico.setFont(new Font("Tahoma", Font.PLAIN, 22));
		add(lblLeitorBiometrico, BorderLayout.NORTH);

		Canvas canvas = new Canvas();
		add(canvas, BorderLayout.WEST);

		TextArea log = new TextArea();
		add(log, BorderLayout.SOUTH);
		
		JButton btnNewButton = new JButton("New button");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		add(btnNewButton, BorderLayout.EAST);
		log.append(mess);

	}

	 
}
