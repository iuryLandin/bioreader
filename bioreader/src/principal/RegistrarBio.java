package principal;

import java.awt.Frame;

import javax.swing.JOptionPane;

import com.digitalpersona.onetouch.DPFPDataPurpose;
import com.digitalpersona.onetouch.DPFPFeatureSet;
import com.digitalpersona.onetouch.DPFPGlobal;
import com.digitalpersona.onetouch.DPFPSample;
import com.digitalpersona.onetouch.processing.DPFPEnrollment;
import com.digitalpersona.onetouch.processing.DPFPImageQualityException;

import DAO.BioDAO;

public class RegistrarBio extends CaptureForm {
	private DPFPEnrollment enroller = DPFPGlobal.getEnrollmentFactory().createEnrollment();

	static Frame f;

	RegistrarBio(Frame owner) {
		super(owner);
		f = owner;
	}

	@Override
	protected void init() {
		super.init();
		this.setTitle("CADASTRAR BIOMETRIA");
		updateStatus();
	}

	@Override
	protected void process(DPFPSample sample) {
		super.process(sample);
		// Cria a feature
		DPFPFeatureSet features = extractFeatures(sample, DPFPDataPurpose.DATA_PURPOSE_ENROLLMENT);

		
		if (features != null)
			try {
				consoleLog(" ", 0);// da uma quebrada na linha para separar os blocos
				enroller.addFeatures(features); // incrementa no contador
			} catch (DPFPImageQualityException ex) {
			} finally {
				updateStatus();

				// Check if template has been created.
				switch (enroller.getTemplateStatus()) {
				case TEMPLATE_STATUS_READY: // Informa que a leitura foi concluida
					stop();
					((Principal) getOwner()).setTemplate(enroller.getTemplate()); // biometria armazenada na memoria
					setPrompt("Biometria recebida!");

					// salvar dados no banco
					byte[] biometria = enroller.getTemplate().serialize(); // converte o template de biometria em bytes

					// chama o metodo para atualizar o cadastro do usuario
					BioDAO dao = new BioDAO();
					if (dao.update(((Principal) getOwner()).getUserId(), biometria)) { // se for atualizado (retorno==true)
						JOptionPane.showMessageDialog(null, "SUCESSO \n Biometria Cadastrada!"); // SUCESSO
						this.setVisible(false); // fecha a janela
						
						Principal p = new Principal();
						AutenticarBio form = new AutenticarBio(p);
						form.setVisible(true);
					}else {
						JOptionPane.showMessageDialog(null, "ERRO \n Não foi possível cadastrar a biometria!"); // ERRO
					}

					
					//System.exit(0); // fecha o programa
					break;

				case TEMPLATE_STATUS_FAILED: // Informa o erro na leitura
					enroller.clear();
					stop();
					updateStatus();
					((Principal) getOwner()).setTemplate(null);
					JOptionPane.showMessageDialog(RegistrarBio.this,
							"Não foi possível cadastrar a biometria, Tente novamente", "Cadastrar Biometria",
							JOptionPane.ERROR_MESSAGE);
					start();
					break;
				}
			}
	}

	private void updateStatus() {
		// Mostrar o contador de vezes do dedo
		setStatus(String.format("Coloque o dedo no leitor por mais %1$s vez(es)", enroller.getFeaturesNeeded()));
	}
}
