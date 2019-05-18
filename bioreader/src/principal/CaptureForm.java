package principal;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.digitalpersona.onetouch.*;
import com.digitalpersona.onetouch.capture.*;
import com.digitalpersona.onetouch.capture.event.*;
import com.digitalpersona.onetouch.processing.*;

public class CaptureForm extends JDialog {
	private DPFPCapture capturer = DPFPGlobal.getCaptureFactory().createCapture();
	private JLabel picture = new JLabel();
	private JTextField prompt = new JTextField();
	private JTextArea log = new JTextArea();
	private JTextField status = new JTextField("[status line]");

	public CaptureForm(Frame owner) {
		super(owner, true);
		setTitle("Fingerprint Enrollment");

		setLayout(new BorderLayout());
		rootPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		picture.setPreferredSize(new Dimension(240, 280));
		picture.setBorder(BorderFactory.createLoweredBevelBorder());
		prompt.setFont(UIManager.getFont("Panel.font"));
		prompt.setEditable(false);
		prompt.setColumns(40);
		prompt.setMaximumSize(prompt.getPreferredSize());
		prompt.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0), "Mensagem:"),
				BorderFactory.createLoweredBevelBorder()));
		log.setColumns(40);
		log.setEditable(false);
		log.setFont(UIManager.getFont("Panel.font"));
		JScrollPane logpane = new JScrollPane(log);
		logpane.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0), "Status:"),
				BorderFactory.createLoweredBevelBorder()));

		status.setEditable(false);
		status.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		status.setFont(UIManager.getFont("Panel.font"));

		JButton quit = new JButton("Cancelar");
		quit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				System.exit(0);
			}
		});

		JButton cadBiometria = new JButton("Cadastrar");
		cadBiometria.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);

 				
				JPasswordField pf = new JPasswordField();
				int okCxl = JOptionPane.showConfirmDialog(null, pf, "SENHA", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
				
				//verifica se o usuario clicou no botao Ok
				if (okCxl == JOptionPane.OK_OPTION) {
					//pega o valor digitado no campo
				  String senha = new String(pf.getPassword());
				  
				  if(senha.equals("123456")) {
						String cpf = (String) JOptionPane.showInputDialog(null, "INFORME O CPF: ", JOptionPane.PLAIN_MESSAGE);

						Principal p = new Principal();
						p.setUserId(cpf);
						RegistrarBio form = new RegistrarBio(p);
						form.setVisible(true);
						
						
					}else {
						JOptionPane.showMessageDialog(null, "Senha Incorreta", "Senha Inválida", JOptionPane.ERROR_MESSAGE);
						setVisible(true);
					}
				  
				}else {
					setVisible(true);
				}
				
				
						
			}
		});

		JPanel right = new JPanel(new BorderLayout());
		right.setBackground(Color.getColor("control"));
		right.add(prompt, BorderLayout.PAGE_START);
		// right.add(logpane, BorderLayout.CENTER);

		JPanel center = new JPanel(new BorderLayout());
		center.setBackground(Color.getColor("control"));
		center.add(right, BorderLayout.CENTER);
		center.add(picture, BorderLayout.LINE_START);
		center.add(status, BorderLayout.PAGE_END);

		JPanel bottom = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		bottom.setBackground(Color.getColor("control"));
		bottom.add(cadBiometria);
		bottom.add(quit);
		

		setLayout(new BorderLayout());
		add(center, BorderLayout.CENTER);
		add(bottom, BorderLayout.PAGE_END);

		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				init();
				start();
			}

			@Override
			public void componentHidden(ComponentEvent e) {
				stop();
			}

		});

		pack();
		setLocationRelativeTo(null);
	}

	protected void init() {
		capturer.addDataListener(new DPFPDataAdapter() {
			@Override
			public void dataAcquired(final DPFPDataEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {

						setPrompt("Biometria Capturada");
						process(e.getSample());
					}
				});
			}
		});
		capturer.addReaderStatusListener(new DPFPReaderStatusAdapter() {
			@Override
			public void readerConnected(final DPFPReaderStatusEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						setPrompt("Leitor Conectado.");
					}
				});
			}

			@Override
			public void readerDisconnected(final DPFPReaderStatusEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						setPrompt("O leitor foi desconectado!");
					}
				});
			}
		});
		capturer.addSensorListener(new DPFPSensorAdapter() {
			@Override
			public void fingerTouched(final DPFPSensorEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						setPrompt("Leitor tocado.");
					}
				});
			}

			@Override
			public void fingerGone(final DPFPSensorEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						// consoleLog("Dedo removido do leitor.");
					}
				});
			}
		});
		capturer.addImageQualityListener(new DPFPImageQualityAdapter() {
			@Override
			public void onImageQuality(final DPFPImageQualityEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						if (e.getFeedback().equals(DPFPCaptureFeedback.CAPTURE_FEEDBACK_GOOD))
							setPrompt("A qualidade da biometria está boa.");
						else
							setPrompt("A qualidade da biometria está ruim.");
					}
				});
			}
		});
	}

	protected void process(DPFPSample sample) {
		// Draw fingerprint sample image.
		drawPicture(convertSampleToBitmap(sample));
	}

	protected void start() {
		capturer.startCapture();
		setPrompt("Coloque o dedo no leitor para ser escaneado!");
	}

	protected void stop() {
		capturer.stopCapture();
	}

	public void setStatus(String string) {
		status.setText(string);
	}

	public void setPrompt(String string) {
		prompt.setText(string);
	}

	public void consoleLog(String string, int tipo) {
		log.append(string + "\n");
		if (tipo == 1)
			JOptionPane.showMessageDialog(null, string, "  ", JOptionPane.DEFAULT_OPTION);
		if (tipo == 2)
			JOptionPane.showMessageDialog(null, string, "  ", JOptionPane.ERROR_MESSAGE);
	}

	public void drawPicture(Image image) {
		picture.setIcon(
				new ImageIcon(image.getScaledInstance(picture.getWidth(), picture.getHeight(), Image.SCALE_DEFAULT)));
	}

	protected Image convertSampleToBitmap(DPFPSample sample) {
		return DPFPGlobal.getSampleConversionFactory().createImage(sample);
	}

	protected DPFPFeatureSet extractFeatures(DPFPSample sample, DPFPDataPurpose purpose) {
		DPFPFeatureExtraction extractor = DPFPGlobal.getFeatureExtractionFactory().createFeatureExtraction();
		try {
			return extractor.createFeatureSet(sample, purpose);
		} catch (DPFPImageQualityException e) {
			return null;
		}
	}

}
