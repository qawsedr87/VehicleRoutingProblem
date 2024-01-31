import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Load {
    int id;
    double[] pickup;
    double[] dropoff;

    Load(int id, double[] pickup, double[] dropoff) {
        this.id = id;
        this.pickup = pickup;
        this.dropoff = dropoff;
    }
}

class Driver {
    List<Integer> schedule = new ArrayList<>();
}

public class VRPSolver {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java VRPSolver <path_to_problem_file>");
            System.exit(1);
        }

        String filePath = args[0];
        List<Load> loads = loadProblem(filePath);

        List<Driver> solution = solveVRP(loads);

        printSolution(solution);
    }

    private static List<Load> loadProblem(String filePath) {
        List<Load> loads = new ArrayList<>();

        try (Scanner scanner = new Scanner(new File(filePath))) {
            // skip "loadNumber pickup dropoff"
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }

            int id = 1;
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                String[] parts = line.split(" ");
                double[] pickup = parseCoordinates(parts[1]);
                double[] dropoff = parseCoordinates(parts[2]);
                loads.add(new Load(id++, pickup, dropoff));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return loads;
    }

    private static double[] parseCoordinates(String coordinateString) {
        // remove the parentheses 
        coordinateString = coordinateString.substring(1, coordinateString.length() - 1);
        String[] coordinates = coordinateString.split(",");
        double[] result = new double[2];
        result[0] = Double.parseDouble(coordinates[0]);
        result[1] = Double.parseDouble(coordinates[1]);
        return result;
    }

    private static List<Driver> solveVRP(List<Load> loads) {
        List<Driver> solution = new ArrayList<>();
        Driver currentDriver = new Driver();
        double currentDistance = 0.0;

        for (Load load : loads) {
            // pick up from 0,0 
            double one = Math.sqrt(load.pickup[0] * load.pickup[0] + load.pickup[1] * load.pickup[1]);

            // drop off from pick up point 
            double two_1 = (load.dropoff[0] - load.pickup[0]) * (load.dropoff[0] - load.pickup[0]);
            double two_2 = (load.dropoff[1] - load.pickup[1]) * (load.dropoff[1] - load.pickup[1]);
            double two = Math.sqrt(two_2 + two_1);

            // back to 0,0
            double three = Math.sqrt(load.dropoff[0] * load.dropoff[0] + load.dropoff[1] * load.dropoff[1]);
            
            // print
            // System.out.println(" load: start (0,0) to pickup (" + load.pickup[0] + "," + load.pickup[1] + ") to dropoff (" + load.dropoff[0] + "," + load.dropoff[1] + ")");
            
            double total = one + two + three;
            // System.out.println("=> Total time is " + total + " (" + hr + ")");
            // get the total drive time for this load
            double loadDriveTime = total;

            // check if exceeds the 12 hours limit
            if (currentDistance + loadDriveTime > 12 * 60) {
                // add new driver
                solution.add(currentDriver);
                currentDriver = new Driver();
                currentDistance = 0.0;
            }

            // add the load the current driver's schedule
            currentDriver.schedule.add(load.id);
            currentDistance += loadDriveTime;
        }

        // add driver
        if (!currentDriver.schedule.isEmpty()) {
            solution.add(currentDriver);
        }

        return solution;
    }

    private static void printSolution(List<Driver> solution) {
        for (Driver driver : solution) {
            System.out.println(driver.schedule);
        }
    }
}
