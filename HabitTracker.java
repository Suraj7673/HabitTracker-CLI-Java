import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;

public class HabitTracker {
    private static final String FILE_PATH = "habits.txt";
    private static Map<String, Set<String>> habits = new HashMap<>();
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        loadHabitsFromFile();

        while (true) {
            System.out.println("\n--- HABIT TRACKER ---");
            System.out.println("1. Add a new habit");
            System.out.println("2. Mark habit done today");
            System.out.println("3. View habits");
            System.out.println("4. Save and Exit");
            System.out.print("Choose: ");
            int choice = sc.nextInt();
            sc.nextLine(); // clear newline

            switch (choice) {
                case 1 -> addHabit();
                case 2 -> markHabitDone();
                case 3 -> viewHabits();
                case 4 -> {
                    saveHabitsToFile();
                    System.out.println("✅ Habits saved. Goodbye!");
                    return;
                }
                default -> System.out.println("❌ Invalid choice. Try again.");
            }
        }
    }

    private static void addHabit() {
        System.out.print("Enter habit name: ");
        String habit = sc.nextLine();
        habits.putIfAbsent(habit, new HashSet<>());
        System.out.println("✅ Habit added!");
    }

    private static void markHabitDone() {
        System.out.print("Enter habit name: ");
        String habit = sc.nextLine();
        if (habits.containsKey(habit)) {
            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            habits.get(habit).add(date);
            System.out.println("✅ Marked as done for today!");
        } else {
            System.out.println("❌ Habit not found.");
        }
    }

    private static void viewHabits() {
        if (habits.isEmpty()) {
            System.out.println("No habits to show.");
            return;
        }

        for (Map.Entry<String, Set<String>> entry : habits.entrySet()) {
            System.out.println("\n📌 " + entry.getKey());
            System.out.println("   Done on: " + entry.getValue());
        }
    }

    private static void saveHabitsToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            for (Map.Entry<String, Set<String>> entry : habits.entrySet()) {
                writer.println(entry.getKey() + ":" + String.join(",", entry.getValue()));
            }
        } catch (IOException e) {
            System.out.println("⚠️ Error saving file.");
        }
    }

    private static void loadHabitsFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                String name = parts[0];
                Set<String> dates = new HashSet<>();
                if (parts.length > 1) {
                    dates.addAll(Arrays.asList(parts[1].split(",")));
                }
                habits.put(name, dates);
            }
        } catch (IOException e) {
            System.out.println("⚠️ Error loading file.");
        }
    }
}
