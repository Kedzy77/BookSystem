import java.sql.*;
import java.util.Scanner;

public class Main {
    private static final String url = "jdbc:postgresql://localhost/postgres";
    private static final String user = "postgres";
    private static final String password = "1234";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            while (running) {
                System.out.println("Выберите опцию:");
                System.out.println("1. Управление книгами");
                System.out.println("2. Управление пользователями");
                System.out.println("3. Список должников");
                System.out.println("0. Выход");

                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        new BookManager().manageBooks(connection, scanner);
                        break;
                    case 2:
                        new UserManager().manageUsers(connection, scanner);
                        break;
                    case 3:
                        new DebtorManager().manageDebtors(connection, scanner);
                        break;
                    case 0:
                        running = false;
                        break;
                    default:
                        System.out.println("Неверный ввод. Пожалуйста, введите номер опции.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
