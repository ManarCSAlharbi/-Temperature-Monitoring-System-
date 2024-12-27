package networks;

import java.io.*;
import java.net.*;
import java.util.*;

public class Client {

	public static final int SERVER_PORT = 12345; // Replace with the actual server port
	public static final String SERVER_ADDRESS = "127.0.0.1"; // Replace with the actual server address
	public static final int SENSOR_ID = 1; // Simulated sensor ID
	public static DataOutputStream out;
	public static BufferedReader inFromServer;
	public static BufferedReader inFromUser;
	private static Timer timer;
	private static int readingsCount = 0;

	public static void main(String[] args) throws Exception {
		// Connect to the server
		Socket socket = new Socket("localhost", 6789);
		out = new DataOutputStream(socket.getOutputStream());
		inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream())); // in from server
		inFromUser = new BufferedReader(new InputStreamReader(System.in)); // in from user

		System.out.println("Connected to the server.");

		// Start the process
		boolean continueSending = true;
		while (continueSending) {
			startSendingReadings();
			listenForCommands();
		}
		socket.close();
	}

	private static void startSendingReadings() {
		readingsCount = 0; // Reset the count
		timer = new Timer(true);

		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				if (readingsCount < 20) {
					// Simulate a random temperature reading between 15 and 40 degrees Celsius using
					// random function
					double temperature = 15 + Math.random() * 25;
					sendCommand("Send " + temperature);
					receiveResponse();
					readingsCount++;
				}
			}
		}, 0, 3000); // Send temperature readings every 3 seconds

		// Wait for the readings to complete
		try {
			Thread.sleep(20 * 3000); // Wait for 20 readings to complete
		} catch (InterruptedException e) {
			System.out.println("Error while waiting for readings to complete.");
		}
	}

	// to send list of command to server and receive Response
	private static void listenForCommands() {
		int average, recent;

		System.out.println("\nCommands: Average n, Max, Min, Recent n ");
		sendCommand("Max \n");
		receiveResponse();

		sendCommand("Min \n");
		receiveResponse();

		average = 1 + (int) (Math.random() * 20);
		sendCommand("Average " + average);
		receiveResponse();

		recent = 1 + (int) (Math.random() * 20);
		sendCommand("Recent " + recent);
		receiveResponse();
	}

	// to send command to server
	private static void sendCommand(String command) {
		try {
			out.writeBytes(command + "  " + SENSOR_ID + "\n");// to server
			System.out.println("Sent to server: " + command);
		} catch (IOException e) {
			System.out.println("Error sending command to server.");
		}
	}

	// to receive Response from server
	private static void receiveResponse() {
		try {
			String response = inFromServer.readLine(); // in from server
			if (response != null) {
				System.out.println("Server response: " + response);
			}
		} catch (IOException e) {
			System.out.println("Error reading response from server.");
		}
	}

}
