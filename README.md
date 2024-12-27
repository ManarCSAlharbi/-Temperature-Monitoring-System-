# 🌡️ Temperature Monitoring System

A **Java-based client-server application** designed to simulate and process temperature sensor readings. The system allows clients to send temperature readings to the server and request various data analyses like maximum, minimum, average, and recent readings.

## 📑 Table of Contents

1. [Project Overview](#-project-overview)
2. [Project Structure](#-project-structure)
3. [Features](#-features)
4. [How to Run](#-how-to-run)
5. [Requirements](#%EF%B8%8F-requirements)
6. [Usage](#-usage)
7. [Logs](#-logs)
8. [Future Improvements](#-future-improvements)
9. [Contributions](#-contributions)
10. [License](#-license)

## 🧾 Project Overview

This project simulates a network-based temperature monitoring system using Java. It allows clients to send temperature data to a server, which processes the data to provide insights such as maximum, minimum, and average temperatures. The server also maintains detailed logs of interactions, ensuring traceability and transparency.

## 📂 Project Structure

- **Client.java**: Handles the connection to the server, sends temperature readings, and receives processed responses.
- **Server.java**: Processes the incoming temperature readings, logs them, and provides analytical commands.

## ✨ Features

1. 🌡️ **Temperature Simulation**: Client simulates random temperature readings between 15°C and 40°C.
2. 📊 **Data Analysis**:
   - `Max`: Retrieves the maximum recorded temperature.
   - `Min`: Retrieves the minimum recorded temperature.
   - `Average n`: Calculates the average of the last `n` readings.
   - `Recent n`: Lists the most recent `n` readings.
3. 🔄 **Real-time Communication**: Bi-directional communication between client and server.
4. 📝 **Logging**: The server maintains a detailed log of all interactions.

## 🚀 How to Run

1. Clone the repository:

   ```bash
   git clone https://github.com/your-username/temperature-monitoring-system.git
   ```

2. Navigate to the project directory:

   ```bash
   cd temperature-monitoring-system
   ```

3. Compile the Java files:

   ```bash
   javac networks/Client.java networksProject/Server.java
   ```

4. Start the server:

   ```bash
   java networksProject.Server
   ```

5. Start the client in a new terminal:

   ```bash
   java networks.Client
   ```

## 🛠️ Requirements

- Java Development Kit (JDK) 8 or higher
- A terminal or command-line tool

## 💡 Usage

- On the client side, the system will:

  1. Connect to the server.
  2. Send simulated temperature readings every 3 seconds.
  3. Request data analyses (e.g., maximum, minimum, average, recent readings).

- The server processes these requests and responds with the required data while maintaining logs of all interactions.

## 📝 Logs

The server logs all actions to a file named `Log File`, including:

- Client connections and disconnections
- Temperature readings received
- Commands executed and their results

## 🌟 Future Improvements

- Add user authentication for secure access.
- Implement a graphical user interface (GUI) for better user experience.
- Extend functionality to handle multiple sensor types.

## 🤝 Contributions

This project was collaboratively developed by me and my colleague. We worked together to design and implement the system, ensuring its functionality and reliability.

## 📜 License

This project is licensed under the MIT License. See the `LICENSE` file for details.

