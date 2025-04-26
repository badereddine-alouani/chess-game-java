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
        System.out.println("Starting chess engine...");
        engine = Runtime.getRuntime().exec(pathToEngine);
        reader = new BufferedReader(new InputStreamReader(engine.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(engine.getOutputStream()));

        sendCommand("uci");

        String line;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("uciok")) {
                break;
            }
        }

        sendCommand("isready");
        while ((line = reader.readLine()) != null) {
            if (line.equals("readyok")) {
                break;
            }
        }

        sendCommand("ucinewgame");

        return true;
    }

    public boolean sendCommand(String command) throws IOException {
        writer.write(command + "\n");
        writer.flush();
        return true;
    }

    public void setDifficulty(String level) throws IOException {
        // Disable Elo limitation (we'll use other constraints)
        sendCommand("setoption name UCI_LimitStrength value false");

        switch (level.toLowerCase()) {
            case "beginner":
                sendCommand("setoption name Skill Level value 0");
                sendCommand("setoption name UCI_MaxDepth value 2");
                sendCommand("setoption name Hash value 16");
                break;
            case "easy":
                sendCommand("setoption name Skill Level value 5");
                sendCommand("setoption name UCI_MaxDepth value 3");
                sendCommand("setoption name Hash value 64");
                break;
            case "medium":
                sendCommand("setoption name Skill Level value 10");
                sendCommand("setoption name UCI_MaxDepth value 6");
                sendCommand("setoption name Hash value 128");
                break;
            case "hard":
                sendCommand("setoption name Skill Level value 15");
                sendCommand("setoption name UCI_MaxDepth value 10");
                sendCommand("setoption name Hash value 512");
                break;
            case "master":
                sendCommand("setoption name Skill Level value 20");
                sendCommand("setoption name Hash value 2048");
                sendCommand("setoption name Threads value 4");
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
