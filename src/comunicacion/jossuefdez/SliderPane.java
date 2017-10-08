package comunicacion.jossuefdez;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SliderPane extends JFrame{

	private static final long serialVersionUID = 1L;
	private JLabel infSlider, valorSlider;
	private JSlider pwmSlider;
	private SerialHandler serialH;
	private String mensajeInicio, mensajeEnvio;
	private double conv;
	private int convToInt;
	
	public static void main(String[] args){
		
		new SliderPane();

	}

	public SliderPane(){
		this.setSize(400, 150);
		this.setLocationRelativeTo(null);
		this.setTitle("Control PWM");
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		
		infSlider = new JLabel("Control PWM");
		panel.add(infSlider);
		
		pwmSlider = new JSlider(0, 255, 0);
		pwmSlider.setMinorTickSpacing(1);
		pwmSlider.setMajorTickSpacing(50);
		pwmSlider.setPaintTicks(true);
		pwmSlider.setPaintLabels(true);
		ListenForSlider lForSlider = new ListenForSlider();
		pwmSlider.addChangeListener(lForSlider);
		panel.add(pwmSlider);
		
		valorSlider = new JLabel("0%");
		panel.add(valorSlider);

		this.add(panel);
		this.setVisible(true);
		
		serialH = new SerialHandler();
		mensajeInicio = serialH.inicializarConexion();
		if(serialH.getErrorInicio()){
			JOptionPane.showMessageDialog(SliderPane.this, mensajeInicio, "Error al iniciar conexión",
					JOptionPane.ERROR_MESSAGE);
		} else{
			/*JOptionPane.showMessageDialog(SliderPane.this, "OK", "Conexión establecida",
					JOptionPane.INFORMATION_MESSAGE);*/
		}
	}
	
	private class ListenForSlider implements ChangeListener{

		@Override
		public void stateChanged(ChangeEvent e) {
			if(e.getSource() == pwmSlider){
				conv = pwmSlider.getValue() / 2.55;
				convToInt = (int) conv;
				valorSlider.setText(Integer.toString(convToInt) + "%");
				mensajeEnvio = serialH.enviarDatos(Integer.toString(pwmSlider.getValue()));
				
				if(serialH.getErrorEnvio()){
					JOptionPane.showMessageDialog(SliderPane.this, mensajeEnvio, "Error al "
							+ "enviar datos", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		
	}
	
}
