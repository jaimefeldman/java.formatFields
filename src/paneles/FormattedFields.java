package paneles;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.InputMap;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;

import fecha.FechaMutable;

public class FormattedFields extends JPanel {

	private static final long serialVersionUID = 1L;
	private JFormattedTextField priceTextField;
	private JFormattedTextField dateTextField;
	private Integer period_counter, period_position;
	private Double dValue;
	private DecimalFormat dformat;
	private Boolean isDecimalEnable;
	private FechaMutable mFecha;

	public FormattedFields() {
		
		createPanelBorder();

		// Si esta vairable esta en zero entoces permitira ingresar una vez un separador decimal.
		period_position = 0;
		dValue			= 0d;
		mFecha			= new FechaMutable(LocalDateTime.now());
		
		System.out.println(mFecha.getShortPrettyFormat());
		
		useDecimalSeparator(false);
		
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setCurrencySymbol("$ ");
		dfs.setDecimalSeparator(',');
		dfs.setMonetaryDecimalSeparator(',');
		dfs.setGroupingSeparator('.');
		dformat.setDecimalFormatSymbols(dfs);
		
		priceTextField = new JFormattedTextField(dformat) {

			
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void invalidEdit() {}
		  	
		};
		priceTextField.setName("priceTextField");
		priceTextField.setColumns(15);
		priceTextField.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				if(!priceTextField.getText().isEmpty()) {
					
					//Limpiando el Valor de tipo String Currency a numerico.
					String strValue = priceTextField.getText();
					strValue = strValue.replaceAll("\\$", "");
					strValue = strValue.replace(',',';');
					strValue = strValue.replaceAll("\\.", "");
					
					strValue = strValue.replace(';', '.');
					strValue = strValue.replaceAll("\\s+", "");
					
					dValue   = Double.parseDouble(strValue);
					priceTextField.setValue(dValue);
					period_counter =0;
					period_position =0;
				}
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					
					@Override
					public void run() {
						priceTextField.selectAll();
					}
				});
				
				if(!priceTextField.getText().isEmpty()) {
					priceTextField.setHorizontalAlignment(SwingConstants.LEFT);
				}
			}
		});
		priceTextField.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				if((Character.isDigit(e.getKeyChar()) || e.getKeyChar() == KeyEvent.VK_PERIOD)) {
					if(((e.getKeyChar() == KeyEvent.VK_PERIOD && period_counter == 0) && !priceTextField.getText().contains(","))) {
						period_counter++;
						period_position = (priceTextField.getText().length()+1);
						e.setKeyChar(',');
					}else if(e.getKeyChar() == KeyEvent.VK_PERIOD) {
						e.consume();
					}
				
				}else{
					e.consume();
				}

			}	
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					//Toolkit.getDefaultToolkit().beep();
					if(!priceTextField.getText().isEmpty()) {
						priceTextField.setHorizontalAlignment(SwingConstants.RIGHT);
					}
					dateTextField.requestFocus();
				}else if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
					if(priceTextField.getText().length() == period_position) {
						if(isDecimalEnable) 
							period_counter = 0;
					}
				}

			}
		});
		
		
		MaskFormatter dateMaskFormatter = null; 
		//DateFormat    dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		DateFormat    dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		dateFormat.setLenient(false);

		try {
			dateMaskFormatter = new MaskFormatter("**************************************************");
		} catch (ParseException e) {
			//Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Problema: MaskFormater en la fecha:", e);
			System.err.println("parser error!");
		}
		
		dateTextField = new JFormattedTextField(dateMaskFormatter) {
			
		
			private static final long serialVersionUID = 1L;

			protected void invalidEdit() {}
			//...
		};
		
		//dateTextField.setText(dateFormat.format(mFecha.getLocalDate()));
		//dateTextField.setText(dateFormat.format(new Date()));
		dateTextField.setText(mFecha.getLongPrettyFormat());

		dateTextField.setName("dateTextField");
		dateTextField.setColumns(20);
		dateTextField.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				Date date = null;
				try {

					date = dateFormat.parse(dateTextField.getText());
					dateTextField.setBorder(new JTextField().getBorder());
					
					
					//Convirtiendo el date en un localdate.
					LocalDate localData = LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(date));
					//Convirtiendo la fecha en un calendario.
					Calendar cal = Calendar.getInstance();
					cal.setTime(date);
					//dateMaskFormatter.setValueContainsLiteralCharacters(false);
					mFecha = new FechaMutable(date);
				
					
				} catch (ParseException e1) {
					System.err.println("Fecha: mal formato");
					Toolkit.getDefaultToolkit().beep();
					dateTextField.setText(dateFormat.format(new Date()));
					dateTextField.setBorder(new LineBorder(Color.RED, 2));
					dateTextField.requestFocus();


				}
				//MaskFormatter formatter = new MaskFormatter("(###) ###-****");
				//formatter.setValidCharacters("ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
				//dateTextField = new JFormattedTextField(formatter);
				try {

					dateTextField.setFormatterFactory(new DefaultFormatterFactory(new MaskFormatter("**************************************")));
					dateTextField.setText(mFecha.getLongPrettyFormat());

					
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				dateTextFieldSetColor(Color.BLACK);
				
			}
			
			
			@Override
			public void focusGained(FocusEvent e) {
				try {
					dateTextField.setFormatterFactory(new DefaultFormatterFactory(new MaskFormatter("##/##/####")));
					//dateTextField.setValue(dateFormat.format(new Date()));
					dateTextField.setValue(dateFormat.format(mFecha.getDate()));
					System.out.println("comparando fechas. para encontrar la fecha de hoy.");
					if(mFecha.getISOFormat().equals(FechaMutable.Hoy().getISOFormat())) {
						dateTextFieldSetColor(Color.BLUE);
					}else {
						dateTextFieldSetColor(Color.BLACK);
					}
					
				} catch (ParseException ex) {
					System.err.println("Problema con el parser de fecha!");
				}
			}

		
		
		});
		  
		dateTextField.addKeyListener(new KeyListener() {

			
			@Override
			public void keyTyped(KeyEvent e) {
				if(e.getKeyChar() != KeyEvent.VK_SPACE) {
					dateTextFieldSetColor(Color.BLACK);
				}
			}
			
			
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					priceTextField.requestFocus();
				}
			}
		});
		
		// ReMapeo para la tecla espacio.
		InputMap inputmapDateTextField = dateTextField.getInputMap();
		inputmapDateTextField.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE,0), "print_today");
		
		AbstractAction print_today_action = new AbstractAction() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					dateTextField.setFormatterFactory(new DefaultFormatterFactory(new MaskFormatter("##/##/####")));
					dateTextField.setValue(dateFormat.format(FechaMutable.Hoy().getDate()));
					mFecha = FechaMutable.Hoy();
					dateTextFieldSetColor(Color.BLUE);
				} catch (ParseException ex) {
					// TODO: handle exception
				}
			}
		};
		
		ActionMap am = dateTextField.getActionMap();
		am.put("print_today", print_today_action);
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(priceTextField);
		add(dateTextField);
		
	}

	// Crea los bordes para el panel.
	private void createPanelBorder() {

		Border innerBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		setBorder(innerBorder);
	}

	// Habilita o desbilita el uso del punto decimal.
	private void useDecimalSeparator(Boolean isDecimalEnable, Integer decimalNumber) {

		configureDecimalSeparator(isDecimalEnable, decimalNumber);
	}

	private void useDecimalSeparator(Boolean isDecimalEnable) {
		configureDecimalSeparator(isDecimalEnable, null);
	}

	// Implementacion del uso del punto decimal.
	private void configureDecimalSeparator(Boolean isDecimalEnable, Integer decimalNumber) {

		if (decimalNumber == null)
			decimalNumber = 0;

		if (isDecimalEnable) {
			dformat = new DecimalFormat("$ #,##0.00");
			if (decimalNumber > 0) {
				this.dformat.setMaximumFractionDigits(decimalNumber);
			} else {
				this.dformat.setMaximumFractionDigits(2);
			}
			period_counter = 0;
			this.isDecimalEnable = true;

		} else {
			dformat = new DecimalFormat("$ ###,###");
			dformat.setMaximumFractionDigits(0);
			period_counter = 1;
			this.isDecimalEnable = false;
		}
	}

	private void dateTextFieldSetColor(Color color) {
		
		this.dateTextField.setForeground(color);
	}


}
