import java.sql.*;
import java.util.Scanner;

public class DebtorManager {
    public void manageDebtors(Connection connection, Scanner scanner) {
        while (true) {
            System.out.println("\nУправление должниками:");
            System.out.println("1. Вывести список должников");
            System.out.println("2. Поиск должника по ID");
            System.out.println("3. Добавить должника");
            System.out.println("4. Удалить должника");
            System.out.println("5. Продлить срок сдачи книги");
            System.out.println("0. Назад");

            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1:
                    printDebtors(connection);
                    break;
                case 2:
                    System.out.println("Введите ID должника:");
                    int id = Integer.parseInt(scanner.nextLine());
                    findDebtorById(connection, id);
                    break;
                case 3:
                    addDebtor(scanner, connection);
                    break;
                case 4:
                    System.out.println("Введите ID должника для удаления:");
                    int debtorId = Integer.parseInt(scanner.nextLine());
                    deleteDebtor(connection, debtorId);
                    break;
                case 5:
                    System.out.println("Введите ID должника для продления срока сдачи книги:");
                    int extendId = Integer.parseInt(scanner.nextLine());
                    extendReturnDate(connection, extendId, scanner);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Неверный ввод. Пожалуйста, введите номер опции.");
                    break;
            }
        }
    }

    private static void printDebtors(Connection connection) {
        String query = "SELECT * FROM debtor";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                System.out.println("ID: " + resultSet.getInt("id") +
                        ", Имя: " + resultSet.getString("name") +
                        ", Фамилия: " + resultSet.getString("surname") +
                        ", Телефон: " + resultSet.getString("phone") +
                        ", Название книги: " + resultSet.getString("book_title") +
                        ", Дата возврата: " + resultSet.getDate("return_date"));
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при выводе списка должников: " + e.getMessage());
        }
    }

    private static void findDebtorById(Connection connection, int id) {
        String query = "SELECT * FROM debtor WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("ID: " + resultSet.getInt("id") +
                            ", Имя: " + resultSet.getString("name") +
                            ", Фамилия: " + resultSet.getString("surname") +
                            ", Телефон: " + resultSet.getString("phone") +
                            ", Название книги: " + resultSet.getString("book_title") +
                            ", Дата возврата: " + resultSet.getDate("return_date"));
                } else {
                    System.out.println("Должник с ID " + id + " не найден.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при поиске должника: " + e.getMessage());
        }
    }

    private static void addDebtor(Scanner scanner, Connection connection) {
        System.out.println("Введите имя должника:");
        String name = scanner.nextLine();
        System.out.println("Введите фамилию должника:");
        String surname = scanner.nextLine();
        System.out.println("Введите телефон должника:");
        String phone = scanner.nextLine();
        System.out.println("Введите название книги:");
        String bookTitle = scanner.nextLine();
        System.out.println("Введите дату возврата (формат ГГГГ-ММ-ДД):");
        String returnDate = scanner.nextLine();

        String query = "INSERT INTO debtor (name, surname, phone, book_title, return_date) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, surname);
            preparedStatement.setString(3, phone);
            preparedStatement.setString(4, bookTitle);
            preparedStatement.setDate(5, Date.valueOf(returnDate));
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Должник успешно добавлен.");
            } else {
                System.out.println("Не удалось добавить должника.");
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при добавлении должника: " + e.getMessage());
        }
    }

    private static void deleteDebtor(Connection connection, int userId) {
        String query = "DELETE FROM debtor WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Должник успешно удален.");
            } else {
                System.out.println("Должник с ID " + userId + " не найден или не может быть удален.");
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при удалении должника: " + e.getMessage());
        }
    }

    private static void extendReturnDate(Connection connection, int debtorId, Scanner scanner) {
        System.out.println("Введите новую дату возврата книги (формат ГГГГ-ММ-ДД):");
        String newReturnDate = scanner.nextLine();

        String query = "UPDATE debtor SET return_date = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setDate(1, Date.valueOf(newReturnDate));
            preparedStatement.setInt(2, debtorId);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Дата возврата обновлена.");
            } else {
                System.out.println("Должник с таким ID не найден.");
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при обновлении даты возврата: " + e.getMessage());
        }
    }
}
