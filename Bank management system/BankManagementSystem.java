import java.sql.*;
import java.util.Scanner;
class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/bankdb";
    private static final String USER = "root";  // Update with your MySQL username
    private static final String PASSWORD = "gauravsaini@1234@1234";  // Update with your MySQL password

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
public class BankManagementSystem {

    public static void createAccount(String name, String accountNumber, double initialBalance) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO accounts (name, account_number, balance) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, name);
            pstmt.setInt(2, Integer.parseInt(accountNumber));
            pstmt.setDouble(3, initialBalance);
            pstmt.executeUpdate();
            System.out.println("Account created successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void deposit(String accountNumber, double amount) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String updateBalanceQuery = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";
            PreparedStatement pstmt = conn.prepareStatement(updateBalanceQuery);
            pstmt.setDouble(1, amount);
            pstmt.setString(2, accountNumber);
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                String insertTransactionQuery = "INSERT INTO transactions (account_number, type, amount) VALUES (?, 'DEPOSIT', ?)";
                pstmt = conn.prepareStatement(insertTransactionQuery);
                pstmt.setString(1, accountNumber);
                pstmt.setDouble(2, amount);
                pstmt.executeUpdate();
                System.out.println("Deposit successful.");
            } else {
                System.out.println("Account not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void withdraw(String accountNumber, double amount) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String checkBalanceQuery = "SELECT balance FROM accounts WHERE account_number = ?";
            PreparedStatement pstmt = conn.prepareStatement(checkBalanceQuery);
            pstmt.setString(1, accountNumber);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                double currentBalance = rs.getDouble("balance");
                if (currentBalance >= amount) {
                    String updateBalanceQuery = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?";
                    pstmt = conn.prepareStatement(updateBalanceQuery);
                    pstmt.setDouble(1, amount);
                    pstmt.setString(2, accountNumber);
                    pstmt.executeUpdate();

                    String insertTransactionQuery = "INSERT INTO transactions (account_number, type, amount) VALUES (?, 'WITHDRAW', ?)";
                    pstmt = conn.prepareStatement(insertTransactionQuery);
                    pstmt.setString(1, accountNumber);
                    pstmt.setDouble(2, amount);
                    pstmt.executeUpdate();
                    System.out.println("Withdrawal successful.");
                } else {
                    System.out.println("Insufficient balance.");
                }
            } else {
                System.out.println("Account not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void checkBalance(String accountNumber) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT balance FROM accounts WHERE account_number = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, accountNumber);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                double balance = rs.getDouble("balance");
                System.out.println("Current balance: ₹" + balance);
            } else {
                System.out.println("Account not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nBank Management System");
            System.out.println("1. Create Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Check Balance");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter account number: ");
                    String accountNumber = scanner.nextLine();
                    System.out.print("Enter initial balance: ");
                    double initialBalance = scanner.nextDouble();
                    createAccount(name, accountNumber, initialBalance);
                    break;
                case 2:
                    System.out.print("Enter account number: ");
                    String accNumberDeposit = scanner.nextLine();
                    System.out.print("Enter amount to deposit: ");
                    double depositAmount = scanner.nextDouble();
                    deposit(accNumberDeposit, depositAmount);
                    break;
                case 3:
                    System.out.print("Enter account number: ");
                    String accNumberWithdraw = scanner.nextLine();
                    System.out.print("Enter amount to withdraw: ");
                    double withdrawAmount = scanner.nextDouble();
                    withdraw(accNumberWithdraw, withdrawAmount);
                    break;
                case 4:
                    System.out.print("Enter account number: ");
                    String accNumberBalance = scanner.nextLine();
                    checkBalance(accNumberBalance);
                    break;
                case 5:
                    System.out.print("Exiting");
                    int i = 0;
                    while(i<5){
                        System.out.print(".");
                        try{
                            Thread.sleep(600);
                        } catch (InterruptedException e) {
                            System.out.println(e.getMessage());
                        }
                        i++;
                    }
                    System.out.println();
                    System.out.println();
                    System.out.println("------Thank you for using the Bank Management System.------");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
