package networksProject;

import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;

public class Server {
    static List<Float> Temperatures = new ArrayList<>();
    static ServerSocket welcomeSocket;
    static List<Socket> clientSockets = new ArrayList<>();
    static File outputF = new File("Log File");
    static PrintWriter output;
    static int sensorID;

    static {
        try {
            output = new PrintWriter(new FileWriter(outputF, true));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static SimpleDateFormat dateFormat = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]");

    public static void main(String[] argv) throws Exception {
        welcomeSocket = new ServerSocket(6789);
        String timeStamp = dateFormat.format(new Date());
        output.println(timeStamp + " Server is running");
        output.flush();

        while (true) {
            Socket connectionSocket = welcomeSocket.accept();
            clientSockets.add(connectionSocket);
            output.println(dateFormat.format(new Date()) + " Client connected: " + connectionSocket.getInetAddress());
            output.flush();

            Thread clientThread = new Thread(() -> {
                try {
                    handleClient(connectionSocket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            clientThread.start();
        }
    }

    private static void handleClient(Socket clientSocket) throws IOException {
        BufferedReader inFromClient =
                new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        DataOutputStream outToClient =
                new DataOutputStream(clientSocket.getOutputStream());

        try {
            while (true) {
                String input = inFromClient.readLine();

                if (input == null) {
                    // Client has disconnected
                    clientDisconnected(clientSocket);
                    break;
                }

                String[] inputs = input.split(" ");
                String command = inputs[0];
                switch (command) {
                    case "Send":
                        sensorID = Integer.parseInt(inputs[2]);
                        outToClient.writeBytes(addTemp(clientSocket, Float.parseFloat(inputs[1])));
                        break;

                    case "Max":
                        sensorID = Integer.parseInt(inputs[1]);
                        outToClient.writeBytes(findMax(clientSocket));
                        break;

                    case "Min":
                        sensorID = Integer.parseInt(inputs[1]);
                        outToClient.writeBytes(findMin(clientSocket));
                        break;

                    case "Average":
                        sensorID = Integer.parseInt(inputs[2]);
                        outToClient.writeBytes(Average(clientSocket, Integer.parseInt(inputs[1])));
                        break;

                    case "Recent":
                        sensorID = Integer.parseInt(inputs[2]);
                        outToClient.writeBytes(Recents(clientSocket, Integer.parseInt(inputs[1])));
                        break;
                }
            }
        } catch (IOException e) {
            System.out.println("Client " + clientSocket.getInetAddress()+" disconnected");
        } finally {
            // Ensure client disconnection is logged and resources are released
            clientDisconnected(clientSocket);
        }
    }

    private static void clientDisconnected(Socket clientSocket) {
        try {
            String timeStamp = dateFormat.format(new Date());
            output.println(timeStamp + " Client disconnected: " + clientSocket.getInetAddress());
            output.flush();
            clientSockets.remove(clientSocket);
            clientSocket.close();
        } catch (IOException e) {
            System.out.println("Error during client disconnection: " + clientSocket.getInetAddress());
        }
    }

    // Methods ---------------------------------------------------------------------

    public static String addTemp(Socket connectionSocket, float Temp) {
        Temperatures.add(Temp);

        System.out.println("Temperature reading received from sensor " + sensorID);
        output.println(dateFormat.format(new Date()) + " Received temperature: " + Temp + " from " + connectionSocket.getInetAddress());
        return ("A temperature reading of " + Temp + " is received.\n");
    }

    public static String findMax(Socket connectionSocket) {
        output.println(dateFormat.format(new Date()) + " Request max temperature: from " + connectionSocket.getInetAddress());

        float max = Collections.max(Temperatures);
        output.println(dateFormat.format(new Date()) + " Sent max temperature: " + max + " to " + connectionSocket.getInetAddress());
        return ("Max Temperature is " + max + "\n");
    }

    public static String findMin(Socket connectionSocket) {
        output.println(dateFormat.format(new Date()) + " Request min temperature: from " + connectionSocket.getInetAddress());

        float min = Collections.min(Temperatures);
        output.println(dateFormat.format(new Date()) + " Sent min temperature: " + min + " to " + connectionSocket.getInetAddress());
        return ("Min Temperature is " + min + "\n");
    }

    public static String Average(Socket connectionSocket, int n) {
        output.println(dateFormat.format(new Date()) + " Request average temperature: from " + connectionSocket.getInetAddress());

        List<Float> lastNTemps = Temperatures.subList(Math.max(Temperatures.size() - n, 0), Temperatures.size());
        float sum = 0;
        for (float temp : lastNTemps) {
            sum += temp;
        }
        float avg = sum / n;

        if (avg > 30) {
            output.println(dateFormat.format(new Date()) + " Temperature average exceeded threshold: " + avg);
            return ("Temperature average of " + avg + " exceeded the maximum threshold of 30\n");
        }
        output.println(dateFormat.format(new Date()) + " Sent average temperature: " + avg + " to " + connectionSocket.getInetAddress());
        return ("Average Temperature is " + avg + "\n");
    }

    public static String Recents(Socket connectionSocket, int n) {
        output.println(dateFormat.format(new Date()) + " Request recent temperatures: from " + connectionSocket.getInetAddress());
        List<Float> recents = Temperatures.subList(Math.max(Temperatures.size() - n, 0), Temperatures.size());
        output.println(dateFormat.format(new Date()) + " Sent recent temperatures: " + recents + " to " + connectionSocket.getInetAddress());

        return ("Recent Temperature readings are " + recents + "\n");
    }
}
