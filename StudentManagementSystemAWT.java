import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class StudentManagementSystemAWT extends Frame implements ActionListener {

    // GUI Components
    private TextField nameField, rollNumberField, gradeField, searchField;
    private TextArea displayArea;
    private Button addButton, searchButton, removeButton;

    // List to hold students
    private List<Student> students;
    private static final String FILE_NAME = "students.txt";

    public StudentManagementSystemAWT() {
        // Initialize students list and load data from file
        students = new ArrayList<>();
        loadStudents();

        // Set up Frame
        setTitle("Student Management System");
        setSize(600, 400);
        setLayout(new BorderLayout());
        setVisible(true);

        // Panel for input fields
        Panel inputPanel = new Panel(new GridLayout(4, 2, 10, 10));
        inputPanel.add(new Label("Name:"));
        nameField = new TextField();
        inputPanel.add(nameField);
        inputPanel.add(new Label("Roll Number:"));
        rollNumberField = new TextField();
        inputPanel.add(rollNumberField);
        inputPanel.add(new Label("Grade:"));
        gradeField = new TextField();
        inputPanel.add(gradeField);

        // Buttons for actions
        addButton = new Button("Add Student");
        addButton.addActionListener(this);
        inputPanel.add(addButton);

        removeButton = new Button("Remove Student");
        removeButton.addActionListener(this);
        inputPanel.add(removeButton);

        // Panel for search
        Panel searchPanel = new Panel(new FlowLayout());
        searchPanel.add(new Label("Search Roll Number:"));
        searchField = new TextField(15);
        searchPanel.add(searchField);
        searchButton = new Button("Search");
        searchButton.addActionListener(this);
        searchPanel.add(searchButton);

        // Display area
        displayArea = new TextArea();
        displayArea.setEditable(false);

        // Adding components to Frame
        add(inputPanel, BorderLayout.NORTH);
        add(searchPanel, BorderLayout.CENTER);
        add(new ScrollPane().add(displayArea), BorderLayout.SOUTH);

        // Window Listener to handle close button action
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                saveStudents();
                dispose();
            }
        });
    }

    // Load students from file
    private void loadStudents() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                if (details.length == 3) {
                    students.add(new Student(details[0], details[1], details[2]));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading student data.");
        }
    }

    // Save students to file
    private void saveStudents() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Student student : students) {
                writer.write(student.getName() + "," + student.getRollNumber() + "," + student.getGrade());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving student data.");
        }
    }

    // Add student
    private void addStudent(Student student) {
        students.add(student);
        saveStudents();
        updateDisplay();
    }

    // Remove student by roll number
    private void removeStudent(String rollNumber) {
        students.removeIf(student -> student.getRollNumber().equals(rollNumber));
        saveStudents();
        updateDisplay();
    }

    // Search student by roll number
    private Student searchStudent(String rollNumber) {
        for (Student student : students) {
            if (student.getRollNumber().equals(rollNumber)) {
                return student;
            }
        }
        return null;
    }

    // Display all students
    private void updateDisplay() {
        StringBuilder sb = new StringBuilder();
        for (Student student : students) {
            sb.append(student.toString()).append("\n");
        }
        displayArea.setText(sb.toString());
    }

    // Action handler for buttons
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (command.equals("Add Student")) {
            String name = nameField.getText();
            String rollNumber = rollNumberField.getText();
            String grade = gradeField.getText();

            if (name.isEmpty() || rollNumber.isEmpty() || grade.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields must be filled out.");
                return;
            }

            Student student = new Student(name, rollNumber, grade);
            addStudent(student);
            nameField.setText("");
            rollNumberField.setText("");
            gradeField.setText("");
        } else if (command.equals("Search")) {
            String rollNumber = searchField.getText();
            Student student = searchStudent(rollNumber);

            if (student != null) {
                displayArea.setText(student.toString());
            } else {
                displayArea.setText("Student not found.");
            }
        } else if (command.equals("Remove Student")) {
            String rollNumber = searchField.getText();
            if (!rollNumber.isEmpty()) {
                removeStudent(rollNumber);
                searchField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Enter a roll number to remove.");
            }
        }
    }

    public static void main(String[] args) {
        new StudentManagementSystemAWT();
    }
}

// Student class definition
class Student {
    private String name;
    private String rollNumber;
    private String grade;

    // Constructor
    public Student(String name, String rollNumber, String grade) {
        this.name = name;
        this.rollNumber = rollNumber;
        this.grade = grade;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public String getGrade() {
        return grade;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    // To String Method
    @Override
    public String toString() {
        return "Name: " + name + ", Roll Number: " + rollNumber + ", Grade: " + grade;
    }
}
