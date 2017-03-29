package controls;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import paneles.FormattedFields;

public class appWindowFormatControl extends JFrame {

	private static final long serialVersionUID = 1L;

	public appWindowFormatControl() {

		setTitle("format controls");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Border outerBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		Border innerBorder = BorderFactory.createEtchedBorder();
		Border mainBorder = BorderFactory.createCompoundBorder(outerBorder, innerBorder);

		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBorder(mainBorder);

		mainPanel.add(new FormattedFields(), BorderLayout.CENTER);

		//Probando date con tiempo.
		//Date date = new Date();
		//DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		//sdf.setTimeZone(TimeZone.getTimeZone("GMT")); tambien llamado (UTC) universal time corrdianted.
		//System.out.println(sdf.format(date));
		//LocalDateTime ldt = LocalDateTime.parse(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(date));
		//System.out.println(ldt.format(DateTimeFormatter.ofPattern("'Hoy' EEEE dd 'de' MMMM 'del' yyyy 'a las:' HH:mm 'hrs.'")));
		//System.out.println("Fecha recibida de un date es : " + fecha.getLongPrettyFormat());
		//System.out.println("fecha: " + fecha.getLongPrettyFormatWihtTime());
		//System.out.println("fecha devolviendo un date: " + fecha.getDate());
		
		
		add(mainPanel);
		setLocationRelativeTo(null);
		pack();

	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				appWindowFormatControl app = new appWindowFormatControl();
				app.setVisible(true);
			}
		});
	}
}
