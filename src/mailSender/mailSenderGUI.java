package mailSender;

import java.awt.EventQueue;
import java.awt.Frame;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.filechooser.FileNameExtensionFilter;

import javax.swing.JScrollPane;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;

import javax.swing.JPasswordField;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.JLabel;
import java.awt.Color;

public class mailSenderGUI {

	private JFrame frmEmailAutoSender;

	String emisor, password, pathToTemplate, pathToArgumentsCSV, pathToAttachment, subject, nameFrom;
	int templateDoc = 0, csvDoc = 0, attachDoc = 0;
	JTextField textField_email;
	JPasswordField passwordField;
	JScrollPane scrollPane;
	JEditorPane editorPane;

	MailSender mailSender = new MailSender();
	JLabel lblEmail;
	JLabel lblContrasea;
	JButton btnAjustes;
	JTextField textField_asunto;
	JLabel lblAsunto;
	JTextField textField;
	JLabel lblPlantillaCargada;
	JLabel lblCsvCargado;
	JLabel lblAdjuntoCargado;
	JLabel lblEnviandoEsperePor;

	public int selectHTMLTemplate() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Extension", "html");
		fileChooser.setFileFilter(filter);

		int result = fileChooser.showOpenDialog(fileChooser);
		int ret = 0;
		switch (result) {
		case JFileChooser.APPROVE_OPTION:
			File selectedFile = fileChooser.getSelectedFile();
			pathToTemplate = selectedFile.getAbsolutePath();
			if (pathToTemplate != null)
				editorPane.setText(mailSender.loadHTML_Template(pathToTemplate));
			editorPane.repaint();
			lblPlantillaCargada.setVisible(true);
			ret = 1;

			break;
		case JFileChooser.CANCEL_OPTION:
			Frame frame = new Frame();
			JOptionPane.showMessageDialog(frame, "No ha seleccionado ninguna plantilla");
			ret = 0;

		}

		return ret;
	}

	public int selectArgumentsCSV() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Extension", "csv");
		fileChooser.setFileFilter(filter);
		int result = fileChooser.showOpenDialog(fileChooser);
		int ret = 0;
		switch (result) {
		case JFileChooser.APPROVE_OPTION:

			File selectedFile = fileChooser.getSelectedFile();
			pathToArgumentsCSV = selectedFile.getAbsolutePath();
			lblCsvCargado.setVisible(true);
			ret = 1;
			break;
		case JFileChooser.CANCEL_OPTION:
			Frame frame = new Frame();
			JOptionPane.showMessageDialog(frame, "No ha seleccionado ningun csv");
			ret = 0;

		}
		return ret;
	}

	public int selectAttachment() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));

		int result = fileChooser.showOpenDialog(fileChooser);
		int ret = 0;
		switch (result) {
		case JFileChooser.APPROVE_OPTION:
			File selectedFile = fileChooser.getSelectedFile();
			pathToAttachment = selectedFile.getAbsolutePath();
			lblAdjuntoCargado.setVisible(true);
			ret = 1;
			break;
		case JFileChooser.CANCEL_OPTION:
			Frame frame = new Frame();
			JOptionPane.showMessageDialog(frame, "No ha seleccionado ningun adjunto");
			ret = 0;

		}
		return ret;
	}

	public int sendEmail() {
		Frame frame = new Frame();
		String errorMessage = "";

		boolean error = false;

		if ((emisor = textField_email.getText()).isEmpty()) {
			errorMessage += "No ha introducido email \n";
			error = true;
		}
		if ((subject = textField_asunto.getText()).isEmpty()) {
			errorMessage += "No ha introducido asunto \n";
			error = true;
		}
		if ((password = new String(passwordField.getPassword())).isEmpty()) {
			errorMessage += "No ha introducido contraseña \n";
			error = true;
		}
		if ((nameFrom = new String(textField.getText())).isEmpty()) {
			errorMessage += "No ha introducido remitente \n";
			error = true;
		}
		if (csvDoc == 0) {
			errorMessage += "No ha introducido un documento csv \n";
			error = true;
		}
		if (templateDoc == 0) {
			errorMessage += "No ha introducido plantilla \n";
			error = true;
		}
		if (attachDoc == 0) {
			errorMessage += "No ha introducido adjunto \n";
			error = true;
		}
		if (!error) {

			if (!mailSender.sendEmailToAgencies(emisor, password, pathToTemplate, pathToArgumentsCSV, pathToAttachment,
					subject, nameFrom)) {
				JOptionPane.showMessageDialog(frame, "La contraseña es incorrecta");

			} else {
				JOptionPane.showMessageDialog(frame, "La tarea ha acabado satisfactoriamente");

			}

		} else {
			JOptionPane.showMessageDialog(frame, errorMessage);
		}

		return 0;
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					mailSenderGUI window = new mailSenderGUI();
					window.frmEmailAutoSender.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public mailSenderGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmEmailAutoSender = new JFrame();
		frmEmailAutoSender.getContentPane().setBackground(new Color(221, 160, 221));
		frmEmailAutoSender.setTitle("EMAIL AUTO SENDER");
		frmEmailAutoSender.setSize(new Dimension(1200, 600));
		frmEmailAutoSender.setBounds(100, 100, 1200, 600);
		frmEmailAutoSender.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		scrollPane = new JScrollPane();
		lblPlantillaCargada = new JLabel("Plantilla cargada");

		lblCsvCargado = new JLabel("Csv cargado");

		lblAdjuntoCargado = new JLabel("Adjunto cargado");

		lblEnviandoEsperePor = new JLabel("Enviando... Espere por favor");

		lblCsvCargado.setVisible(false);

		lblPlantillaCargada.setVisible(false);

		lblAdjuntoCargado.setVisible(false);

		lblEnviandoEsperePor.setVisible(false);

		JButton btnCargarPlantilla = new JButton("Cargar plantilla");
		btnCargarPlantilla.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				templateDoc = selectHTMLTemplate();
			}
		});

		JButton btnCargarCsv = new JButton("Cargar csv");
		btnCargarCsv.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				csvDoc = selectArgumentsCSV();
			}
		});

		JButton btnCargarAdjunto = new JButton("Cargar adjunto");
		btnCargarAdjunto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				attachDoc = selectAttachment();
			}
		});

		textField_email = new JTextField();
		textField_email.setToolTipText("Correo electronico");
		textField_email.setColumns(10);

		passwordField = new JPasswordField();

		JButton btnEnviar = new JButton("Enviar");
		btnEnviar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				SwingWorker<String, Void> myWorker = new SwingWorker<String, Void>() {
					@Override
					protected String doInBackground() throws Exception {
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								// To update your GUI
								lblEnviandoEsperePor.setVisible(true);
								lblEnviandoEsperePor.repaint();
								btnEnviar.setEnabled(false);
								btnEnviar.repaint();
							}
						});
						sendEmail();
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								// To update your GUI
								lblEnviandoEsperePor.setVisible(false);
								lblEnviandoEsperePor.repaint();
								btnEnviar.setEnabled(true);
								btnEnviar.repaint();
							}
						});
						return null;
					}

				};
				
				myWorker.execute();
				
				
			}
		});
		btnEnviar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});

		lblEmail = new JLabel("Email:");

		lblContrasea = new JLabel("Contraseña:");

		btnAjustes = new JButton("Ajustes");
		btnAjustes.setEnabled(false);

		textField_asunto = new JTextField();
		textField_asunto.setColumns(10);

		lblAsunto = new JLabel("Asunto:");

		JLabel lblNombreDelRemitente = new JLabel("Nombre del remitente:");

		textField = new JTextField();
		textField.setColumns(10);

		GroupLayout groupLayout = new GroupLayout(frmEmailAutoSender.getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup().addContainerGap()
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
										.addComponent(btnEnviar, GroupLayout.DEFAULT_SIZE,
												GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(textField_asunto).addComponent(passwordField)
										.addComponent(btnCargarPlantilla, GroupLayout.DEFAULT_SIZE,
												GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(btnCargarCsv, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addComponent(btnCargarAdjunto, GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
										.addComponent(lblEmail).addComponent(lblContrasea)
										.addComponent(btnAjustes, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addComponent(textField_email).addComponent(lblAsunto)
										.addComponent(lblNombreDelRemitente).addComponent(textField))
								.addComponent(lblPlantillaCargada).addComponent(lblCsvCargado)
								.addComponent(lblAdjuntoCargado).addComponent(lblEnviandoEsperePor))
						.addGap(18).addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
						.addContainerGap()));
		groupLayout
				.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup().addGroup(groupLayout
								.createParallelGroup(
										Alignment.TRAILING)
								.addGroup(groupLayout.createSequentialGroup().addGap(22)
										.addComponent(btnCargarPlantilla).addGap(18).addComponent(btnCargarCsv)
										.addGap(18).addComponent(btnCargarAdjunto).addGap(9).addComponent(lblEmail)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(textField_email, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED).addComponent(lblContrasea)
										.addGap(3)
										.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, 21,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED).addComponent(lblAsunto).addGap(2)
										.addComponent(textField_asunto, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.UNRELATED)
										.addComponent(lblNombreDelRemitente).addGap(5)
										.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addGap(18)
										.addComponent(btnEnviar, GroupLayout.PREFERRED_SIZE, 43,
												GroupLayout.PREFERRED_SIZE)
										.addGap(18).addComponent(lblPlantillaCargada)
										.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(lblCsvCargado)
										.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(lblAdjuntoCargado)
										.addGap(33).addComponent(lblEnviandoEsperePor).addGap(18)
										.addComponent(btnAjustes))
								.addGroup(groupLayout.createSequentialGroup().addContainerGap().addComponent(scrollPane,
										GroupLayout.DEFAULT_SIZE, 547, Short.MAX_VALUE)))
								.addContainerGap()));

		editorPane = new JEditorPane();
		editorPane.setEditable(false);

		editorPane.setText("");
		scrollPane.setViewportView(editorPane);
		frmEmailAutoSender.getContentPane().setLayout(groupLayout);
	}
}
