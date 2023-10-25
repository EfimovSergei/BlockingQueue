package org.example;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


public class Main {
    public static BlockingQueue<String> blockingQueueA = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> blockingQueueB = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> blockingQueueC = new ArrayBlockingQueue<>(100);
    public static int countA;
    public static int countB;
    public static int countC;

    public static void main(String[] args) throws InterruptedException {
        List<Thread> threads = new ArrayList<>(4);
        Thread stringPutter = new Thread(() -> {
            for (int i = 0; i < 10_000; i++) {
                String string = generateText("abc", 100_000);
                Random random = new Random();
                int a = random.nextInt(3) + 1;
                switch (a) {
                    case 1:
                        try {
                            blockingQueueA.put(string);
                        } catch (InterruptedException e) {
                            return;
                        }
                        break;
                    case 2:
                        try {
                            blockingQueueB.put(string);
                        } catch (InterruptedException e) {
                            return;
                        }
                        break;
                    case 3:
                        try {
                            blockingQueueC.put(string);
                        } catch (InterruptedException e) {
                            return;
                        }
                        break;
                }
            }
        });
        stringPutter.start();
        Thread threadA = new Thread(() -> {
            try {
                int a = blockingQueueA.take().split("a", -1).length - 1;
                if (a > countA) {
                    countA = a;
                }

            } catch (InterruptedException e) {
                return;
            }
        });
        Thread threadB = new Thread(() -> {
            try {
                int b = blockingQueueB.take().split("b", -1).length - 1;
                if (b > countB) {
                    countB = b;
                }

            } catch (InterruptedException e) {
                return;
            }
        });
        Thread threadC = new Thread(() -> {
            try {
                int c = blockingQueueC.take().split("c", -1).length - 1;
                if (c > countC) {
                    countC = c;
                }

            } catch (InterruptedException e) {
                return;
            }
        });
        threads.add(threadA);
        threads.add(threadB);
        threads.add(threadC);
        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }
        System.out.println(countA + " - a" + "\n" +
                countB + " - b" + "\n" +
                countC + " - c" + "\n");
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}