import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;
import javax.swing.JOptionPane;

public class GuessingGame {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/numberguessingdb";
    private static final String DB_USER = "root"; // replace with your MySQL username
    private static final String DB_PASSWORD = "036529"; // replace with your MySQL password

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            boolean playAgain = true;

            while (playAgain) {
                playGame(connection);
                int option = JOptionPane.showConfirmDialog(null, "Do you want to play again?", "Play Again", JOptionPane.YES_NO_OPTION);
                playAgain = (option == JOptionPane.YES_OPTION);
            }

        } catch (SQLException e) {
            System.err.println("Error occurred while connecting to the database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void playGame(Connection connection) {
        int randomNumber = new Random().nextInt(100) + 1;
        int attempts = 10;
        int userGuess;
        String name = JOptionPane.showInputDialog(null, "Enter your name:");

        for (int i = 0; i < attempts; i++) {
            userGuess = Integer.parseInt(JOptionPane.showInputDialog(null, "Guess the number between 1 and 100. You have " + (attempts - i) + " attempts left."));

            if (userGuess < randomNumber) {
                JOptionPane.showMessageDialog(null, "Too low! Try again.");
            } else if (userGuess > randomNumber) {
                JOptionPane.showMessageDialog(null, "Too high! Try again.");
            } else {
                JOptionPane.showMessageDialog(null, "Congratulations, " + name + "! You guessed the number " + randomNumber + " in " + (i + 1) + " attempts.");
                try {
                    addHighScore(connection, name, i + 1);
                } catch (SQLException e) {
                    System.err.println("Error occurred while adding high score: " + e.getMessage());
                    e.printStackTrace();
                }
                return;
            }
        }

        JOptionPane.showMessageDialog(null, "Sorry, " + name + "! You didn't guess the number " + randomNumber + " in " + attempts + " attempts.");
    }

    private static void addHighScore(Connection connection, String name, int score) throws SQLException {
        String query = "INSERT INTO highscores (name, score) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, score);
            preparedStatement.executeUpdate();
            connection.commit();
        }
    }
}
