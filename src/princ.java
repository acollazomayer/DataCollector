import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class princ extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	static CommPortIdentifier portId;
	static Enumeration portList;
	static ArrayList<CommPortIdentifier> ports = new ArrayList<CommPortIdentifier>();
	private SimpleRead reader = new SimpleRead();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {

				////////////////////////// Serial ///////////////////
				portList = CommPortIdentifier.getPortIdentifiers();

				/*
				 * if (portList.hasMoreElements()==false){
				 * System.out.println("false"); }
				 */
				while (portList.hasMoreElements()) {
					// System.out.println("Hola");
					portId = (CommPortIdentifier) portList.nextElement();
					if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
						ports.add(portId);
						// System.out.println(ports.size());
						// System.out.println(portId.getName());
						if (portId.getName().equals("COM1")) {
							// if (portId.getName().equals("/dev/term/a")) {
							// System.out.println("equisde");
							// SimpleRead reader = new SimpleRead();
						}
					}
				}

				/////////////////////////// FRAME ///////////////////
				try {
					princ frame = new princ();
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
					// UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
	}

	/**
	 * Create the frame.
	 */
	public princ() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 285, 235);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		// File Chooser
		JFileChooser fc = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
		fc.setFileFilter(filter);

		JLabel lblDataCollector = new JLabel("Data Collector");
		lblDataCollector.setHorizontalAlignment(SwingConstants.CENTER);
		lblDataCollector.setFont(new Font("Segoe UI Historic", Font.ITALIC, 25));

		JLabel lblSelectPort = new JLabel("     Select Port:");

		JComboBox comboBox = new JComboBox();				
		for (int i = 0; i < ports.size(); i++) {			
			comboBox.addItem(ports.get(i).getName());
		}

		textField = new JTextField();
		textField.setColumns(10);

		JButton btnNewButton = new JButton("Select File");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int returnVal = fc.showOpenDialog(btnNewButton);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					textField.setText(file.getPath());
				} else {
					// JOptionPane.showMessageDialog(fc,"Wrong File");
				}
			}
		});

		JButton btnNewButton_1 = new JButton("Start Read");
		btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				String path = textField.getText();
				File file = new File(path);						
				if (file.exists() && file.getName().toLowerCase().endsWith(".txt")) {					
					PrintStream out;
					try {
						out = new PrintStream(new FileOutputStream(file), true);
						System.setOut(out);	
						
						for (int i = 0; i < ports.size(); i++) {
							if (ports.get(i).getName().equals(comboBox.getSelectedItem().toString())) {
								reader.setPortId(ports.get(i));		
								if(ports.get(i).isCurrentlyOwned()){
									System.out.println("descanza");
								}
								reader.read();
							}
						}						
						
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (PortInUseException e) {
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(contentPane,"Port In Use");
						e.printStackTrace();
					}

				} else {
					JOptionPane.showMessageDialog(fc, "Wrong File!");
				}
			}
		});
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(lblDataCollector, GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
										.addComponent(lblSelectPort, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(btnNewButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
									.addGap(18)
									.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
										.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, 129, GroupLayout.PREFERRED_SIZE)
										.addComponent(textField, GroupLayout.PREFERRED_SIZE, 129, GroupLayout.PREFERRED_SIZE)))))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(58)
							.addComponent(btnNewButton_1, GroupLayout.PREFERRED_SIZE, 141, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(lblDataCollector)
					.addGap(11)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblSelectPort)
						.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnNewButton)
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE))
					.addGap(32)
					.addComponent(btnNewButton_1)
					.addContainerGap(32, Short.MAX_VALUE))
		);
		contentPane.setLayout(gl_contentPane);
	}	
}
