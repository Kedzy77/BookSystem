import java.sql.*;
import java.util.Scanner;

public class UserManager {
    public void manageUsers(Connection connection, Scanner scanner) {
        System.out.println("\nУправление пользователями:");
        System.out.println("1. Вывести список пользователей");
        System.out.println("2. Поиск пользователя по ID");
        System.out.println("3. Добавление пользователя");
        System.out.println("4. Удаление пользователя");
        System.out.println("0. Назад");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                printUsers(connection);
                break;
            case 2:
                System.out.println("Введите ID пользователя:");
                int id = scanner.nextInt();
                findUser(connection, id);
                break;
            case 3:
                System.out.println("Введите имя пользователя:");
                String name = scanner.nextLine();
                System.out.println("Введите фамилию пользователя:");
                String surname = scanner.nextLine();
                System.out.println("Введите телефон пользователя:");
                String phone = scanner.nextLine();
                addUser(connection, name, surname, phone);
                break;
            case 4:
                System.out.println("Введите ID пользователя для удаления:");
                int userId = scanner.nextInt();
                deleteUser(connection, userId);
                break;
            case 0:
                break;
            default:
                System.out.println("Неверный ввод. Пожалуйста, введите номер опции.");
                break;
        }
    }

    private static void printUsers(Connection connection) {
        String query = "SELECT * FROM users";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                System.out.println("ID: " + resultSet.getInt("id") +
                        ", Имя: " + resultSet.getString("name") +
                        ", Фамилия: " + resultSet.getString("surname") +
                        ", Телефон: " + resultSet.getString("phone"));
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при получении списка пользователей: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void findUser(Connection connection, int id) {
        String query = "SELECT * FROM users WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("ID: " + resultSet.getInt("id") +
                            ", Имя: " + resultSet.getString("name") +
                            ", Фамилия: " + resultSet.getString("surname") +
                            ", Телефон: " + resultSet.getString("phone"));
                } else {
                    System.out.println("Пользователь с ID " + id + " не найден.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при поиске пользователя: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void addUser(Connection connection, String name, String surname, String phone) {
        String query = "INSERT INTO users (name, surname, phone) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, surname);
            preparedStatement.setString(3, phone);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Пользователь успешно добавлен.");
            } else {
                System.out.println("Не удалось добавить пользователя.");
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при добавлении пользователя: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void deleteUser(Connection connection, int userId) {
        String query = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Пользователь успешно удален.");
            } else {
                System.out.println("Пользователь с ID " + userId + " не найден или не может быть удален.");
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при удалении пользователя: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
