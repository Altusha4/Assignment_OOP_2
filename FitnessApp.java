import java.util.*;
import java.util.stream.Collectors;

abstract class WorkoutRoutine {
    private String routineName;
    private int durationInMinutes;
    private int caloriesBurned;

    public WorkoutRoutine(String routineName, int durationInMinutes, int caloriesBurned) {
        this.routineName = routineName;
        this.durationInMinutes = durationInMinutes;
        this.caloriesBurned = caloriesBurned;
    }

    public String getRoutineName() {
        return routineName;
    }

    public int getDurationInMinutes() {
        return durationInMinutes;
    }

    public int getCaloriesBurned() {
        return caloriesBurned;
    }

    public abstract String getRoutineType();

    @Override
    public String toString() {
        return String.format("Routine: %-20s | Duration: %3d min | Calories: %4d kcal | Type: %s",
                routineName, durationInMinutes, caloriesBurned, getRoutineType());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        WorkoutRoutine that = (WorkoutRoutine) obj;
        return durationInMinutes == that.durationInMinutes &&
                caloriesBurned == that.caloriesBurned &&
                Objects.equals(routineName, that.routineName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(routineName, durationInMinutes, caloriesBurned);
    }
}

class CardioRoutine extends WorkoutRoutine {
    private int averageHeartRate;

    public CardioRoutine(String routineName, int durationInMinutes, int caloriesBurned, int averageHeartRate) {
        super(routineName, durationInMinutes, caloriesBurned);
        this.averageHeartRate = averageHeartRate;
    }

    public int getAverageHeartRate() {
        return averageHeartRate;
    }

    @Override
    public String getRoutineType() {
        return "Cardio";
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" | Avg Heart Rate: %3d bpm", averageHeartRate);
    }
}

class StrengthRoutine extends WorkoutRoutine {
    private int sets;
    private int repsPerSet;

    public StrengthRoutine(String routineName, int durationInMinutes, int caloriesBurned, int sets, int repsPerSet) {
        super(routineName, durationInMinutes, caloriesBurned);
        this.sets = sets;
        this.repsPerSet = repsPerSet;
    }

    public int getTotalReps() {
        return sets * repsPerSet;
    }

    @Override
    public String getRoutineType() {
        return "Strength";
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" | Sets: %2d | Reps per Set: %2d | Total Reps: %3d",
                sets, repsPerSet, getTotalReps());
    }
}

class User {
    private String name;
    private int age;
    private double weight;
    private List<WorkoutRoutine> routines;

    public User(String name, int age, double weight) {
        this.name = name;
        this.age = age;
        this.weight = weight;
        this.routines = new ArrayList<>();
    }

    public void addRoutine(WorkoutRoutine routine) {
        routines.add(routine);
    }

    public List<WorkoutRoutine> getRoutines() {
        return routines;
    }

    public List<WorkoutRoutine> searchRoutinesByName(String name) {
        return routines.stream()
                .filter(r -> r.getRoutineName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<WorkoutRoutine> filterRoutinesByType(String type) {
        return routines.stream()
                .filter(r -> r.getRoutineType().equalsIgnoreCase(type))
                .collect(Collectors.toList());
    }

    public List<WorkoutRoutine> sortRoutinesByCaloriesBurned(boolean ascending) {
        return routines.stream()
                .sorted(ascending
                        ? Comparator.comparingInt(WorkoutRoutine::getCaloriesBurned)
                        : Comparator.comparingInt(WorkoutRoutine::getCaloriesBurned).reversed())
                .collect(Collectors.toList());
    }
}

public class FitnessApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your name: ");
        String name = scanner.nextLine();

        int age = getValidIntInput(scanner, "Enter your age: ");
        double weight = getValidDoubleInput(scanner, "Enter your weight: ");

        User user = new User(name, age, weight);

        while (true) {
            System.out.println("\nWelcome to FitnessApp!");
            System.out.println("1. Manage Workout Routines");
            System.out.println("2. Search Routines");
            System.out.println("3. Filter Routines by Type");
            System.out.println("4. Sort Routines by Calories Burned");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int choice = getValidIntInput(scanner, "");

            switch (choice) {
                case 1:
                    manageWorkoutRoutines(scanner, user);
                    break;
                case 2:
                    searchRoutines(scanner, user);
                    break;
                case 3:
                    filterRoutines(scanner, user);
                    break;
                case 4:
                    sortRoutines(scanner, user);
                    break;
                case 5:
                    System.out.println("Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void manageWorkoutRoutines(Scanner scanner, User user) {
        System.out.println("\nManage Workout Routines");
        System.out.println("1. Add Cardio Routine");
        System.out.println("2. Add Strength Routine");
        System.out.print("Choose an option: ");

        int type = getValidIntInput(scanner, "");

        System.out.print("Enter routine name: ");
        String routineName = scanner.nextLine();
        int duration = getValidIntInput(scanner, "Enter duration (minutes): ");
        int calories = getValidIntInput(scanner, "Enter calories burned: ");

        switch (type) {
            case 1:
                int heartRate = getValidIntInput(scanner, "Enter average heart rate: ");
                user.addRoutine(new CardioRoutine(routineName, duration, calories, heartRate));
                System.out.println("Cardio Routine added successfully.");
                break;
            case 2:
                int sets = getValidIntInput(scanner, "Enter number of sets: ");
                int reps = getValidIntInput(scanner, "Enter repetitions per set: ");
                user.addRoutine(new StrengthRoutine(routineName, duration, calories, sets, reps));
                System.out.println("Strength Routine added successfully.");
                break;
            default:
                System.out.println("Invalid choice. Routine not added.");
        }
    }

    private static void searchRoutines(Scanner scanner, User user) {
        System.out.print("Enter routine name to search: ");
        String searchName = scanner.nextLine();
        List<WorkoutRoutine> foundRoutines = user.searchRoutinesByName(searchName);

        if (foundRoutines.isEmpty()) {
            System.out.println("No routines found matching the name.");
        } else {
            System.out.println("Found Routines:");
            foundRoutines.forEach(System.out::println);
        }
    }

    private static void filterRoutines(Scanner scanner, User user) {
        System.out.print("Enter type to filter (Cardio/Strength): ");
        String type = scanner.nextLine();
        List<WorkoutRoutine> filteredRoutines = user.filterRoutinesByType(type);

        if (filteredRoutines.isEmpty()) {
            System.out.println("No routines found of type " + type);
        } else {
            System.out.println("Filtered Routines:");
            filteredRoutines.forEach(System.out::println);
        }
    }

    private static void sortRoutines(Scanner scanner, User user) {
        System.out.print("Sort by calories burned (1 for ascending, 2 for descending): ");
        int sortChoice = getValidIntInput(scanner, "");
        boolean ascending = (sortChoice == 1);

        List<WorkoutRoutine> sortedRoutines = user.sortRoutinesByCaloriesBurned(ascending);
        System.out.println("Sorted Routines:");
        sortedRoutines.forEach(System.out::println);
    }

    private static int getValidIntInput(Scanner scanner, String prompt) {
        int value;
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                value = Integer.parseInt(input);
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    }

    private static double getValidDoubleInput(Scanner scanner, String prompt) {
        double value;
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                value = Double.parseDouble(input);
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }
}
