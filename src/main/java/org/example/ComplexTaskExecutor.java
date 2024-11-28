package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ComplexTaskExecutor {
    private final ExecutorService executorService;
    private final CyclicBarrier cyclicBarrier;

    public ComplexTaskExecutor(int numberOfTasks) {
        this.executorService = Executors.newFixedThreadPool(numberOfTasks);
        this.cyclicBarrier = new CyclicBarrier(numberOfTasks, () -> {
            System.out.println("All tasks completed. Combining results...");
        });
    }

    public void executeTasks(int numberOfTasks) {
        List<Future<String>> results = new ArrayList<>();
        for (int i = 0; i < numberOfTasks; i++) {
            int taskId = i;
            Future<String> future = executorService.submit(() -> {
                ComplexTask task = new ComplexTask(taskId);
                String result = task.execute();

                try {
                    System.out.println("Task " + taskId + " waiting at barrier...");
                    cyclicBarrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Task " + taskId + " interrupted at barrier.");
                }

                return result;
            });

            results.add(future);
        }

        results.forEach(future -> {
            try {
                System.out.println(future.get());
            } catch (InterruptedException | ExecutionException e) {
                Thread.currentThread().interrupt();
                System.out.println("Error retrieving task result.");
            }
        });

        shutdownExecutor();
    }

    private void shutdownExecutor() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }
}
