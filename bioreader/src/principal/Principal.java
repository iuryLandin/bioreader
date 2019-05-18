package principal;

import java.awt.Frame;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import com.digitalpersona.onetouch.DPFPTemplate;

import DAO.BioDAO;

public class Principal extends JFrame {

	public static String TEMPLATE_PROPERTY = "template";
	private DPFPTemplate template;

	public String userId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	Principal() {
		setState(Frame.NORMAL);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setTitle("BioReader - Autenticação Biométrica / by: Iury Lanidn");
		setTemplate(null);
		setVisible(false);
	}

	public DPFPTemplate getTemplate() {
		return template;
	}

	public void setTemplate(DPFPTemplate template) {
		DPFPTemplate old = this.template;
		this.template = template;
		firePropertyChange(TEMPLATE_PROPERTY, old, template);
	}

	/**
	 * @param Pegar parametros para saber qual painel chamar
	 * @param       [0]=insert
	 * @param       [0]=auth
	 */
	public static void main(String[] args) {
		Principal p = new Principal();

		AutenticarBio form = new AutenticarBio(p);
		form.setVisible(true);
	}

}