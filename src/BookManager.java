import java.sql.*;
import java.util.Scanner;

public class BookManager {
    public void manageBooks(Connection connection, Scanner scanner) throws SQLException {
        System.out.println("Управление книгами:");
        System.out.println("1. Вывести список книг");
        System.out.println("2. Поиск книги по ID");
        System.out.println("3. Добавление книги");
        System.out.println("4. Удаление книги");
        System.out.println("0. Назад");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                print(connection);
                break;
            case 2:
                System.out.println("Введите ID книги:");
                int id = scanner.nextInt();
                find(connection, id);
                break;
            case 3:
                System.out.println("Введите название книги:");
                String name = scanner.nextLine();
                System.out.println("Введите автора книги:");
                String author = scanner.nextLine();
                System.out.println("Доступна ли книга (true/false):");
                boolean availability = scanner.nextBoolean();
                add(connection, name, author, availability);
                break;
            case 4:
                System.out.println("Введите ID книги для удаления:");
                int bookId = scanner.nextInt();
                delete(connection, bookId);
                break;
            case 0:
                
                break;
            default:
                System.out.println("Неверный ввод. Пожалуйста, введите номер опции.");
        }
    }

    private static void print(Connection connection) throws SQLException {
        String query = "SELECT * FROM books";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                System.out.println("ID: " + resultSet.getInt("id") +
                        ", Название: " + resultSet.getString("name") +
                        ", Автор: " + resultSet.getString("author") +
                        ", Доступность: " + resultSet.getBoolean("availability"));
            }
        }
    }

    private static void find(Connection connection, int id) throws SQLException {
        String query = "SELECT * FROM books WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("ID: " + resultSet.getInt("id") +
                            ", Название: " + resultSet.getString("name") +
                            ", Автор: " + resultSet.getString("author") +
                            ", Доступность: " + resultSet.getBoolean("availability"));
                } else {
                    System.out.println("Книга с ID " + id + " не найдена.");
                }
            }
        }
    }

    private static void add(Connection connection, String name, String author, boolean availability) throws SQLException {
        String query = "INSERT INTO books (name, author, availability) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, author);
            preparedStatement.setBoolean(3, availability);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Книга успешно добавлена.");
            } else {
                System.out.println("Не удалось добавить книгу.");
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при добавлении книги: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void delete(Connection connection, int id) throws SQLException {
        String query = "DELETE FROM books WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Книга с ID " + id + " успешно удалена.");
            } else {
                System.out.println("Книга с ID " + id + " не найдена.");
            }
        }
    }
}
