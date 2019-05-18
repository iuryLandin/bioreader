package principal;

import java.awt.Frame;

import javax.swing.JOptionPane;

import com.digitalpersona.onetouch.DPFPDataPurpose;
import com.digitalpersona.onetouch.DPFPFeatureSet;
import com.digitalpersona.onetouch.DPFPGlobal;
import com.digitalpersona.onetouch.DPFPSample;
import com.digitalpersona.onetouch.DPFPTemplate;
import com.digitalpersona.onetouch.verification.DPFPVerification;
import com.digitalpersona.onetouch.verification.DPFPVerificationResult;

import DAO.BioDAO;

public class AutenticarBio extends CaptureForm {

	private static final long serialVersionUID = -8358819946736993789L;

	private DPFPVerification verificator = DPFPGlobal.getVerificationFactory().createVerification();

	AutenticarBio(Frame owner) {
		super(owner);
	}

	@Override
	protected void init() {
		super.init();
		this.setTitle("LEITOR BIOMÉTRICO");
		updateStatus(0);
	}

	@Override
	protected void process(DPFPSample sample) {
		super.process(sample);

		// Process the sample and create a feature set for the enrollment purpose.
		DPFPFeatureSet features = extractFeatures(sample, DPFPDataPurpose.DATA_PURPOSE_VERIFICATION);

		// Check quality of the sample and start verification if it's good
		if (features != null) {

			// instancia a classe de banco de dados
			BioDAO dao = new BioDAO();
			dao.setListaPessoa();
			
			RegistrarBatida rb = new RegistrarBatida();

			// Passa a digital que é do tipo byte para o tipo template
			DPFPTemplate template = DPFPGlobal.getTemplateFactory().createTemplate();
			// template.deserialize( dao.get() );
			for (int i = 0; i < dao.getListaPessoa().size(); i++) {
				template.deserialize(dao.getListaPessoa().get(i).getDigital());

				// Compara a leitura em tempo real(feature) com o template recuperado do Banco
				DPFPVerificationResult result = verificator.verify(features, template);
				updateStatus(result.getFalseAcceptRate());
				if (result.isVerified()) {
					boolean r = rb.registrar( dao.getListaPessoa().get(i).getId() );
					if(r)
						consoleLog(rb.getData() + " - " + rb.getHora() + " \n" + dao.getListaPessoa().get(i).getNome(), 1);
					else
						consoleLog("NÃO FOI POSSIVEL REGISTRAR BATIDA", 2);
					
				} else {
					consoleLog("USUARIO NAO ENCONTRADO", 2);
				}
			}
		}
	}

	private void updateStatus(int FAR) {
		// Show "False accept rate" value
		setStatus(String.format("False Accept Rate (FAR) = %1$s", FAR));
	}

}
