package comunicacion.jossuefdez;

import java.io.OutputStream;
import java.util.Arrays;
import java.util.Enumeration;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

public class SerialHandler {

	private static final String PUERTO = "COM3";
	private static final int TIMEOUT = 2000;
	private static final int BAUDIOS = 115200;
	
	protected OutputStream output = null;
	protected SerialPort serialPort;
	
	private boolean errorInicio, errorEnvio;

	public SerialHandler(){
		errorInicio = false;
		errorEnvio = false;
	}
	
	//Funciones para mandar datos, errores e inicializar comunicación
	public String inicializarConexion(){
		
		CommPortIdentifier puertoID = null;
		Enumeration<?> puertoEnum = CommPortIdentifier.getPortIdentifiers();
		
		while(puertoEnum.hasMoreElements()){
			CommPortIdentifier actualPuerto = (CommPortIdentifier) puertoEnum.nextElement();
			if(PUERTO.equals(actualPuerto.getName())){
				puertoID = actualPuerto;
				break;
			}
		}
		
		if(puertoID == null){
			errorInicio = true;
			return "No se ha localizado el puerto" + PUERTO + "\n";
		}
		
		try{
			serialPort = (SerialPort) puertoID.open(this.getClass().getName(), TIMEOUT);
			//Parametros del puerto serie
			serialPort.setSerialPortParams(BAUDIOS, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, 
					SerialPort.PARITY_NONE);
			output = serialPort.getOutputStream();
			errorInicio = false;
			return "OK";
		} catch(Exception e){
			errorInicio = true;
			return e.getMessage();
		}
	}

	public String enviarDatos(String datos){
		try{
			output.write(datos.getBytes());
			
			System.out.println("Valor enviado: " + datos);
			errorEnvio = false;
			return "OK";
		} catch(Exception e){
			errorEnvio = true;
			return "No se han podido enviar los datos";
		}
	}

	public boolean getErrorInicio(){
		return errorInicio;
	}
	
	public boolean getErrorEnvio(){
		return errorEnvio;
	}
}
