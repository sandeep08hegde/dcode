import java.util.*;
import java.io.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class StudentDatabase {
    private static List<Student> studentList = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;
        do {
            System.out.println("******Welcome to Student Database*******");
            System.out.println("1. Add Student Data");
            System.out.println("2. List Student Data");
            System.out.println("3. Update Student Data");
            System.out.println("4. Delete Student Data");
            System.out.println("5. Exit");
            System.out.print("Select an option: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addStudent(scanner);
                    break;
                case 2:
                    listStudentData();
                    break;
                case 3:
                    updateStudent(scanner);
                    break;
                case 4:
                    deleteStudent(scanner);
                    break;
                case 5:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 5);
        scanner.close();
    }

    private static void addStudent(Scanner scanner) {
        System.out.println("Enter Student ID: ");
        String studentId = scanner.nextLine();
        if (isDuplicateStudent(studentId)) {
            System.out.println("Student ID already exists. Cannot add duplicate.");
            return;
        }

        System.out.println("Enter Name: ");
        String name = scanner.nextLine();

        System.out.println("Enter Age: ");
        int age = scanner.nextInt();
        if (age < 5 || age > 30) {
            System.out.println("Invalid age. Age must be between 5 and 30.");
            return;
        }

        scanner.nextLine(); // Consume newline
        System.out.println("Enter School Name: ");
        String schoolName = scanner.nextLine();

        System.out.println("Enter Grade: ");
        String grade = scanner.nextLine();

        System.out.println("Enter GPA: ");
        double gpa = scanner.nextDouble();
        if (gpa < 0 || gpa > 4) {
            System.out.println("Invalid GPA. GPA must be between 0 and 4.");
            return;
        }

        scanner.nextLine(); // Consume newline
        System.out.println("Enter Address: ");
        String address = scanner.nextLine();

        System.out.println("Enter Phone Number: ");
        String phoneNumber = scanner.nextLine();
        if (!phoneNumber.matches("\\d{10}")) {
            System.out.println("Invalid phone number. It must be 10 digits.");
            return;
        }

        Student student = new Student(studentId, name, age, schoolName, grade, gpa, address, phoneNumber);
        studentList.add(student);
        saveToExcel();
        System.out.println("Student details added successfully.");
    }

    private static void listStudentData() {
        if (studentList.isEmpty()) {
            System.out.println("No student records found.");
        } else {
            for (Student student : studentList) {
                System.out.println(student);
            }
        }
    }

    private static void updateStudent(Scanner scanner) {
        System.out.println("Enter Student ID to update: ");
        String studentId = scanner.nextLine();
        Student student = findStudentById(studentId);

        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        System.out.println("Choose field to update (Name, Age, School-Name, Grade, GPA, Address, Phone Number): ");
        String fieldToUpdate = scanner.nextLine().toLowerCase();
        System.out.println("Enter new value: ");
        String newValue = scanner.nextLine();

        switch (fieldToUpdate) {
            case "name":
                student.setName(newValue);
                break;
            case "age":
                int newAge = Integer.parseInt(newValue);
                if (newAge < 5 || newAge > 30) {
                    System.out.println("Invalid age. Age must be between 5 and 30.");
                    return;
                }
                student.setAge(newAge);
                break;
            case "school-name":
                student.setSchoolName(newValue);
                break;
            case "grade":
                student.setGrade(newValue);
                break;
            case "gpa":
                double newGpa = Double.parseDouble(newValue);
                if (newGpa < 0 || newGpa > 4) {
                    System.out.println("Invalid GPA. GPA must be between 0 and 4.");
                    return;
                }
                student.setGpa(newGpa);
                break;
            case "address":
                student.setAddress(newValue);
                break;
            case "phone number":
                if (!newValue.matches("\\d{10}")) {
                    System.out.println("Invalid phone number. It must be 10 digits.");
                    return;
                }
                student.setPhoneNumber(newValue);
                break;
            default:
                System.out.println("Invalid field. Update failed.");
                return;
        }

        saveToExcel();
        System.out.println("Update successful.");
    }

    private static void deleteStudent(Scanner scanner) {
        System.out.println("Enter Student ID to delete: ");
        String studentId = scanner.nextLine();
        Student student = findStudentById(studentId);

        if (student != null) {
            studentList.remove(student);
            saveToExcel();
            System.out.println("Student record deleted successfully.");
        } else {
            System.out.println("Student not found.");
        }
    }

    private static boolean isDuplicateStudent(String studentId) {
        return studentList.stream().anyMatch(student -> student.getStudentId().equals(studentId));
    }

    private static Student findStudentById(String studentId) {
        for (Student student : studentList) {
            if (student.getStudentId().equals(studentId)) {
                return student;
            }
        }
        return null;
    }

    private static void saveToExcel() {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Students");

            int rowIndex = 0;
            for (Student student : studentList) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(student.getStudentId());
                row.createCell(1).setCellValue(student.getName());
                row.createCell(2).setCellValue(student.getAge());
                row.createCell(3).setCellValue(student.getSchoolName());
                row.createCell(4).setCellValue(student.getGrade());
                row.createCell(5).setCellValue(student.getGpa());
                row.createCell(6).setCellValue(student.getAddress());
                row.createCell(7).setCellValue(student.getPhoneNumber());
            }

            FileOutputStream fileOut = new FileOutputStream("students.xlsx");
            workbook.write(fileOut);
            fileOut.close();
            System.out.println("Data saved to Excel file successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class Student {
    private String studentId;
    private String name;
    private int age;
    private String schoolName;
    private String grade;
    private double gpa;
    private String address;
    private String phoneNumber;

    public Student(String studentId, String name, int age, String schoolName, String grade, double gpa, String address, String phoneNumber) {
        this.studentId = studentId;
        this.name = name;
        this.age = age;
        this.schoolName = schoolName;
        this.grade = grade;
        this.gpa = gpa;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    // Getters and Setters

    public String getStudentId() {
        return studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public double getGpa() {
        return gpa;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "User-ID: " + studentId + "\nName: " + name + "\nAge: " + age + "\nSchool-Name: " + schoolName +
                "\nGrade: " + grade + "\nGPA: " + gpa + "\nAddress: " + address + "\nPhone Number: " + phoneNumber + "\n";
    }
}



 public static void addStudentData(Scanner scanner) {
        if (loggedInUserId.isEmpty()) {
            System.out.println("No user is logged in. Please log in first.");
            return;
        }

        System.out.println("Logged in as User ID: " + loggedInUserId);




 // Method to load student data from Excel
    private void loadStudentData() {
        students.clear(); // Clear the existing data in case of reloading

        try (FileInputStream fis = new FileInputStream(STUDENT_DATA_FILE);
             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {
            XSSFSheet sheet = workbook.getSheet("Students");

            if (sheet == null) return; // If the sheet does not exist, return early

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header row if there is any

                String studentId = row.getCell(0).getStringCellValue();
                String name = row.getCell(1).getStringCellValue();
                int age = (int) row.getCell(2).getNumericCellValue(); // Convert numeric to int
                String schoolName = row.getCell(3).getStringCellValue();
                String grade = row.getCell(4).getStringCellValue();
                double gpa = row.getCell(5).getNumericCellValue(); // Convert numeric to double
                String address = row.getCell(6).getStringCellValue();
                String phoneNumber = row.getCell(7).getStringCellValue();

                // Add the student object to the list
                students.add(new Student(studentId, name, age, schoolName, grade, gpa, address, phoneNumber));
            }
        } catch (FileNotFoundException e) {
            System.out.println("Student data file not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error reading student data: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error occurred while loading data: " + e.getMessage());
        }
    }

    // Method to save student data to Excel
    private void saveStudentData() {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet("Students");
            int rowNum = 0;
            
            // Create header row
            Row header = sheet.createRow(rowNum++);
            header.createCell(0).setCellValue("Student ID");
            header.createCell(1).setCellValue("Name");
            header.createCell(2).setCellValue("Age");
            header.createCell(3).setCellValue("School Name");
            header.createCell(4).setCellValue("Grade");
            header.createCell(5).setCellValue("GPA");
            header.createCell(6).setCellValue("Address");
            header.createCell(7).setCellValue("Phone Number");

            // Write student data
            for (Student student : students) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(student.getStudentId());
                row.createCell(1).setCellValue(student.getName());
                row.createCell(2).setCellValue(student.getAge());
                row.createCell(3).setCellValue(student.getSchoolName());
                row.createCell(4).setCellValue(student.getGrade());
                row.createCell(5).setCellValue(student.getGpa());
                row.createCell(6).setCellValue(student.getAddress());
                row.createCell(7).setCellValue(student.getPhoneNumber());
            }

            try (FileOutputStream fos = new FileOutputStream(STUDENT_DATA_FILE)) {
                workbook.write(fos);
            }
        } catch (Exception e) {
            System.out.println("Error saving student data: " + e.getMessage());
        }
    }

    // Add Student, Update Student, and Delete Student methods remain the same

    // Inner class Student and other methods remain the same
}
