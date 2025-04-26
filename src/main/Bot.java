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
        sendCommand("setoption name UCI_LimitStrength value true");

        switch (level.toLowerCase()) {
            case "easy":
                sendCommand("setoption name UCI_Elo value 1350");
                break;
            case "medium":
                sendCommand("setoption name UCI_Elo value 1600");
                break;
            case "hard":
                sendCommand("setoption name UCI_Elo value 1900");
                break;
            case "master":
                sendCommand("setoption name UCI_Elo value 2500");
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
