package org.example;

public class Main {
    public static void main(String[] args) {
        int numberOfTasks = 5;
        ComplexTaskExecutor taskExecutor = new ComplexTaskExecutor(numberOfTasks);

        Runnable testRunnable = () -> {
            System.out.println(Thread.currentThread().getName() + " started the test.");

            taskExecutor.executeTasks(numberOfTasks);

            System.out.println(Thread.currentThread().getName() + " completed the test.");
        };

        Thread thread1 = new Thread(testRunnable, "TestThread-1");
        Thread thread2 = new Thread(testRunnable, "TestThread-2");

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}