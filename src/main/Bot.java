package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;

public class Bot {
    private Process engine;
    private BufferedReader reader;
    private BufferedWriter writer;

    public boolean startEngine(String pathToEngine) throws IOException {
        engine = Runtime.getRuntime().exec(pathToEngine);
        reader = new BufferedReader(new InputStreamReader(engine.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(engine.getOutputStream()));
        return sendCommand("uci");

    }

    public boolean sendCommand(String command) throws IOException {
        writer.write(command + "\n");
        writer.flush();
        return true;
    }

    public void setDifficulty(String level) throws IOException {
        switch (level.toLowerCase()) {
            case "easy":
                sendCommand("setoption name Skill Level value 0");
                sendCommand("setoption name Skill Level Maximum Error value 700");
                sendCommand("setoption name Skill Level Probability value 50");
                break;
            case "medium":
                sendCommand("setoption name Skill Level value 8");
                sendCommand("setoption name Skill Level Maximum Error value 100");
                sendCommand("setoption name Skill Level Probability value 20");
                break;
            case "hard":
                sendCommand("setoption name Skill Level value 15");
                sendCommand("setoption name Skill Level Maximum Error value 30");
                sendCommand("setoption name Skill Level Probability value 10");
                break;
            case "master":
                sendCommand("setoption name Skill Level value 20");
                break;
        }
    }

    public String getBestMoveFromStartpos(List<String> moves, int moveTimeMs) throws IOException {
        StringBuilder command = new StringBuilder("position startpos");
        if (moves != null && !moves.isEmpty()) {
            command.append(" moves");
            for (String move : moves) {
                command.append(" ").append(move);
            }
        }

        sendCommand(command.toString());
        sendCommand("go movetime " + moveTimeMs);

        String line;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("bestmove")) {
                return line.split(" ")[1];
            }
        }
        return null;
    }

    public void stopEngine() throws IOException {
        sendCommand("quit");
        writer.close();
        reader.close();
        engine.destroy();
    }
}
