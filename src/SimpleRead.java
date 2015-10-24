import java.io.*;
import java.util.*;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEventListener;
import gnu.io.*;

public class SimpleRead implements Runnable, SerialPortEventListener {
	static CommPortIdentifier portId;
	static Enumeration portList;
	static ArrayList<String> reads;

	InputStream inputStream;
	SerialPort serialPort;
	Thread readThread;
	

	////// GETTERS Y SETTERS//////

	public SerialPort getSerialPort() {
		return serialPort;
	}

	public void setSerialPort(SerialPort serialPort) {
		this.serialPort = serialPort;
	}

	public static CommPortIdentifier getPortId() {
		return portId;
	}

	public static void setPortId(CommPortIdentifier portId) {
		SimpleRead.portId = portId;}	

	public SimpleRead() {

	}

	public void read() throws PortInUseException{
		
			serialPort = (SerialPort) portId.open("SimpleReadApp", 2000);
		
		try {
			inputStream = serialPort.getInputStream();
		} catch (IOException e) {
			System.out.println(e);			
		}
		try {
			serialPort.addEventListener(this);
		} catch (TooManyListenersException e) {
			System.out.println(e);
		}
		serialPort.notifyOnDataAvailable(true);
		try {
			serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
		} catch (UnsupportedCommOperationException e) {
			System.out.println(e);
		}
		readThread = new Thread(this);
		readThread.start();

	}

	public void run() {
		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			System.out.println(e);
		}
	}

	public void serialEvent(SerialPortEvent event) {
		switch (event.getEventType()) {
		case SerialPortEvent.BI:
		case SerialPortEvent.OE:
		case SerialPortEvent.FE:
		case SerialPortEvent.PE:
		case SerialPortEvent.CD:
		case SerialPortEvent.CTS:
		case SerialPortEvent.DSR:
		case SerialPortEvent.RI:
		case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
			break;
		case SerialPortEvent.DATA_AVAILABLE:
			byte[] buf = new byte[20];

			int len;

			try {
				len = inputStream.read(buf, 0, buf.length);

				String read = new String(buf, 0, len, "us-ascii");

				System.out.print(read);
				if (len != buf.length) {
					throw new RuntimeException("the stream is closed and i failed to read enough data");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}