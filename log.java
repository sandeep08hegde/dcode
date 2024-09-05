import java.util.*;
import java.io.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Authentication {
    private static Map<String, String> userCredentials = new HashMap<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        loadCredentials();

        do {
            System.out.println("******Welcome to Student Database*******");
            System.out.println("1. Login");
            System.out.println("2. Registration");
            System.out.print("Select an option: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    login(scanner);
                    break;
                case 2:
                    register(scanner);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 1);

        scanner.close();
    }

    private static void login(Scanner scanner) {
        System.out.println("--Welcome to Login Page---");
        System.out.print("Enter UserId: ");
        String userId = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        if (userCredentials.containsKey(userId) && userCredentials.get(userId).equals(password)) {
            System.out.println("Login Successful!");
            StudentDatabase.main(new String[]{});
        } else {
            System.out.println("Login Failed! Invalid UserId or Password.");
        }
    }

    private static void register(Scanner scanner) {
        System.out.println("Enter User-Name: ");
        String userName = scanner.nextLine();
        if (userCredentials.containsKey(userName)) {
            System.out.println("User already exists. Registration unsuccessful.");
            return;
        }

        System.out.println("Enter Password: ");
        String password = scanner.nextLine();

        if (!validatePassword(password)) {
            System.out.println("Invalid password format. Registration unsuccessful.");
            return;
        }

        System.out.println("Re-enter Password: ");
        String confirmPassword = scanner.nextLine();

        if (!password.equals(confirmPassword)) {
            System.out.println("Passwords do not match. Registration unsuccessful.");
            return;
        }

        userCredentials.put(userName, password);
        saveCredentials();
        System.out.println("Registration successful, UserID -> " + userName);
    }

    private static boolean validatePassword(String password) {
        if (password.length() < 8 || password.length() > 12) return false;
        boolean hasUpper = false, hasLower = false, hasDigit = false, hasSpecial = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else if ("@#&%*!".indexOf(c) != -1) hasSpecial = true;
        }
        return hasUpper && hasLower && hasDigit && hasSpecial;
    }

    private static void loadCredentials() {
        try (Workbook workbook = new XSSFWorkbook(new FileInputStream("users.xlsx"))) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                String userName = row.getCell(0).getStringCellValue();
                String password = row.getCell(1).getStringCellValue();
                userCredentials.put(userName, password);
            }
        } catch (IOException e) {
            System.out.println("Error loading user credentials. " + e.getMessage());
        }
    }

    private static void saveCredentials() {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Users");
            int rowIndex = 0;

            for (Map.Entry<String, String> entry : userCredentials.entrySet()) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(entry.getKey());
                row.createCell(1).setCellValue(entry.getValue());
            }

            FileOutputStream fileOut = new FileOutputStream("users.xlsx");
            workbook.write(fileOut);
            fileOut.close();
            System.out.println("User credentials saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
private static boolean validatePassword(String password) {
    // Regex to match the password requirements
    String passwordPattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[@#&%*!]).{8,12}$";

    // Return true if the password matches the regex pattern, otherwise false
    return password.matches(passwordPattern);
}

