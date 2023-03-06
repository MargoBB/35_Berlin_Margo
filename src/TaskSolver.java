import java.util.ArrayList;
import java.util.List;

public class TaskSolver {
    
    private List<Task> tasks;
    
    public TaskSolver() {
        this.tasks = new ArrayList<>();
    }
    
    public void addTask(Task task) {
        this.tasks.add(task);
    }
    
    public void solveTasks() {
        for (Task task : tasks) {
            task.solve();
        }
    }
    
    public static void main(String[] args) {
        TaskSolver solver = new TaskSolver();
        
        // add tasks
        Task task1 = new Task1();
        Task task2 = new Task2();
        solver.addTask(task1);
        solver.addTask(task2);
        
        // solve tasks
        solver.solveTasks();
    }
}

interface Task {
    void solve();
}

class Task1 implements Task {
    public void solve() {
        System.out.println("Task 1 solved.");
    }
}

class Task2 implements Task {
    public void solve() {
        System.out.println("Task 2 solved.");
    }
}