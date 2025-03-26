package org.example;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class MainSimple {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
//        getHello(); // buffer overflow
//        getHello_Improved();
        List<MyRecord> records = getRecords();
//        records.stream().sorted().forEach(System.out::println);
        records.stream().sorted(Comparator.comparing(MyRecord::price)).forEach(System.out::println);

        System.out.println(records.get(0).toString());
        System.out.println(records.get(0).name());
        System.out.println("----");
        printHashSet();
        System.out.println("----");
        printTreeSet();
        System.out.println("----");
        createMultiThreadedProgram();
        System.out.println("----");
        try {
            createThreadPool();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("----");
        dataCorruption();
        System.out.println("----");
        happensBeforeRelationship();
    }

    public static HashSet<String> getHashSet() {
        HashSet<String> set = new HashSet<>();
        set.add("Apple");
        set.add("Banana");
        set.add("Cherry");
        set.add("Date");
        set.add("Elderberry");
        return set;

        // is order guaranteed? // no
    }

    public static void printHashSet() {
        HashSet<String> set = getHashSet();
        set.forEach(System.out::println);
    }

    // create TreeSet

    public static TreeSet<String> getTreeSet() {
        TreeSet<String> set = new TreeSet<>();
        set.add("Elderberry");
        set.add("Cherry");
        set.add("Apple");
        set.add("Banana");

        set.add("Date");

        return set;

        // is order guaranteed? // yes
    }
    public static void printTreeSet() {
        // TreeSet is sorted
        TreeSet<String> set = getTreeSet();
        set.forEach(System.out::println);
    }
    // create LinkedHashSet
    // create HashMap

    public static void getHello() {
        String a="a";
        for (int i=0; i<100000000; i++) {
            a+= a;
        }
        System.out.print(a);
    }

    public static void getHello_Improved() {
        String a="a";
        StringBuilder sb = new StringBuilder(a);
        for (int i=0; i<100000000; i++) {
            sb.append(a);
        }
        System.out.print(sb.toString());
    }

    public static List<MyRecord> getRecords() {
        // bcoz I added Lombak, I can use the builder
        MyRecord.builder().name("Apple").price(3.0).build();
        MyRecord.builder().name("Banana").price(2.0).build();
        MyRecord.builder().name("Cherry").price(4.0).build();
        MyRecord.builder().name("Date").price(1.0).build();
        MyRecord.builder().name("Elderberry").price(5.0).build();

        // this is default way to create a record
        return List.of(new MyRecord("Apple", 3.0),
                new MyRecord("Banana", 2.0),
                new MyRecord("Cherry", 4.0),
                new MyRecord("Date", 1.0),
                new MyRecord("Elderberry"   , 5.0)
        );
    }

    // create multi-threaded program example // why do you need multi-threaded program?
    // to improve performance // to improve responsiveness // to improve resource utilization // to improve simplicity // to improve modularity // to improve scalability   // to improve maintainability
    // how it improves performance? // by running multiple tasks concurrently

    public static void createMultiThreadedProgram() {
        Runnable task = () -> {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Hello, World!");
        };
        Thread thread = new Thread(task);
        thread.start();

        Runnable task2 = () -> {
            System.out.println("Hello, World 2!");
        };
        Thread thread2 = new Thread(task2);
        thread2.start();

        // create a program that uses a thread
        // create a program that uses a thread pool
        // create a program that uses a fork/join pool
        // create a program that uses a completable future
        // create a program that uses a parallel stream
    }


    // create a program that uses a thread pool
    // create a program that uses a fork/join pool
    // create a program that uses a completable future
    // create a program that uses a parallel stream

    /**
     * what is DDD? Domain Driven Design
     * why do you need Record in Java 14?   // immutable data
     * what is the difference between Record and Class? // Record is immutable, Class is mutable // Record is a special kind of class that is introduced in Java 14 // Record is a class that is immutable and has a fixed set of fields //
     * what is the difference between Record and Data?
     * what is the difference between Record and Enum?
     * what is the difference between Record and Interface?
     * what is the difference between Record and Annotation?
     * what is the difference between Record and Lambda?
     * what is stream in java 8?
     * // sequence of elements from a source that supports aggregate operations (filter, map, reduce, find, match, sort, etc)
     * what is the difference between stream and list? // stream is a sequence of elements from a source that supports aggregate operations
     * DDD: what is the difference between Entity and Value Object? // Entity has an identity, Value Object does not have an identity (immutable)
     * DDD: what is the difference between Aggregate and Aggregate Root? // Aggregate is a cluster of associated objects that are treated as a unit for data changes, Aggregate Root is the entry point to an aggregate
     * DDD: what is the difference between Repository and Service? // Repository is a collection of objects, Service is a behavior
     * DDD: what is the difference between Factory and Builder? // Factory is a method that creates objects, Builder is a method that constructs objects
     *
     */

    // create a program that uses a thread pool
    public static void createThreadPool() throws ExecutionException, InterruptedException {
        ExecutorService  executorService = Executors.newFixedThreadPool(2);
        Callable<String> task = () -> {
            return "Hello, World!" + Thread.currentThread().getName();
        };
        Future<String> future = executorService.submit(task);
        try {
            String result = future.get();
            System.out.println(result + Thread.currentThread().getName());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        Callable<Integer> oneToHundreds = () -> {
            int sum = 0;
            for (int i = 1; i <= 100; i++) {
                sum += i;
            }
            System.out.println("sum = " + sum + " " + Thread.currentThread().getName());
            return sum;
        };

        Callable<Integer> hundredToMillion = () -> {
            int sum = 0;
            for (int i = 100; i <= 1000000; i++) {
                sum += i;
            }
            System.out.println("sum = " + sum + " " + Thread.currentThread().getName());
            return sum;
        };

        Future<Integer> future1 = executorService.submit(oneToHundreds);
        Future<Integer> future2 = executorService.submit(hundredToMillion);
        Integer result1 = future1.get();
        Integer result2 = future2.get();
        System.out.println("result1 = " + result1 + " " + Thread.currentThread().getName());
        System.out.println("result2 = " + result2 + " " + Thread.currentThread().getName());
        System.out.println("result1 + result2 = " + (result1 + result2) + " " + Thread.currentThread().getName());
        executorService.shutdown();

        // explain the above code
        // what is ExecutorService? // ExecutorService is an interface that provides a way to manage threads
        // what is Executors? // Executors is a utility class that provides factory methods for creating ExecutorService instances
        // what is newFixedThreadPool? // newFixedThreadPool is a factory method that creates a fixed-size thread pool
        // what is submit? // submit is a method that submits a task for execution and returns a Future representing the pending results of the task
        // what is a task? // a task is a unit of work that can be executed by a thread
        // what is a thread? // a thread is a lightweight process that can run concurrently with other threads



//        Executors.newCachedThreadPool();
        // create a program that uses a thread pool
        // create a program that uses a fork/join pool
        // create a program that uses a completable future
    }
    // create a program that uses a fork/join pool
    // create a program that uses a completable future

    // what is thread local? // ThreadLocal is a class that provides thread-local variables
    // what does it mean to be thread-safe? // Thread-safe means that a class or method can be used by multiple threads without causing data corruption
    // how do you make a class thread-safe? // by using synchronization, locks, or thread-safe data structures
    // what is synchronization? // Synchronization is a mechanism that prevents multiple threads from accessing shared data at the same time
    // what is a lock? // A lock is a synchronization primitive that provides exclusive access to shared data
    // what is a thread-safe data structure? // A thread-safe data structure is a data structure that can be used by multiple threads without causing data corruption
    // show an example of a thread-safe data structure // ConcurrentHashMap // CopyOnWriteArrayList // CopyOnWriteArraySet // ConcurrentLinkedQueue // ConcurrentLinkedDeque // ConcurrentSkipListMap // ConcurrentSkipListSet
    // how does data get corrupted in a multi-threaded program? // Data gets corrupted in a multi-threaded program when multiple threads access shared data without proper synchronization
    // show an example of data corruption in a multi-threaded program //

    private static volatile int count = 0; // what does volatile do? // volatile keyword is used to indicate that a variable's value will be modified by different threads
    // what is the difference between volatile and synchronized? // volatile is used to indicate that a variable's value will be modified by different threads, synchronized is used to prevent multiple threads from accessing shared data at the same time
    // what is the difference between volatile and atomic? // volatile is used to indicate that a variable's value will be modified by different threads, atomic is used to perform atomic operations on variables
    // how does volatile interact with the memory model? // volatile interacts with the memory model by ensuring that changes to a variable are visible to other threads
    // what is the memory model? // The memory model is a set of rules that govern how threads interact with shared memory
    // what is the happens-before relationship? // The happens-before relationship is a rule that defines the ordering of memory operations in a multi-threaded program
    // what is the memory consistency model? // The memory consistency model is a set of rules that define how memory operations are ordered in a multi-threaded program
    // what is the Java Memory Model? // The Java Memory Model is a specification that defines how threads interact with shared memory in the Java programming language
    // what is the difference between the Java Memory Model and the Java Virtual Machine? // The Java Memory Model is a specification that defines how threads interact with shared memory in the Java programming language, the Java Virtual Machine is an implementation of the Java Memory Model
    // explain java memory model
    // The Java Memory Model is a specification that defines how threads interact with shared memory in the Java programming language.
    // It defines the rules for how changes to variables are propagated to other threads and how memory operations are ordered in a multi-threaded program.
    // The Java Memory Model ensures that changes to variables made by one thread are visible to other threads and that memory operations are ordered in a predictable way.
    // how does java memory model make sure that changes to variables are propagated to other threads? // The Java Memory Model ensures that changes to variables made by one thread are visible to other threads by using the happens-before relationship
    // explain happens before relationship with an example
    // example: if thread A writes to a variable and then thread B reads from the same variable, the write operation happens before the read operation
    // show a java program that demonstrates the happens-before relationship

    // The Java Memory Model is an important part of the Java programming language because it provides a consistent and reliable way to write multi-threaded programs.
    // The Java Memory Model is implemented by the Java Virtual Machine, which is responsible for executing Java programs and managing memory operations.
    // The Java Memory Model is designed to provide a high level of performance and scalability for multi-threaded programs, while also ensuring that programs are correct and reliable.
    // The Java Memory Model is a key feature of the Java programming language that makes it possible to write efficient and reliable multi-threaded programs.

    public static void dataCorruption() {

        Runnable task1 = () -> {
            for (int i = 0; i < 10000; i++) {
                count++;
            }
        };

        Runnable task2 = () -> {
            for (int i = 0; i < 10000; i++) {
                count++;
            }
        };
        Thread thread1 = new Thread(task1);
        Thread thread2 = new Thread(task2);
        thread1.start();
        thread2.start();
        try {
            thread1.join(); // what does join do? // waits for the thread to finish
            thread2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Expected count = " + 20000);
        System.out.println("Actual count = " + count);
    }

    // show a java program that demonstrates the happens-before relationship
    public static void happensBeforeRelationship() {
        AtomicInteger value = new AtomicInteger(0);
        Runnable task1 = () -> {
            value.set(1);
        };
        Runnable task2 = () -> {
            int result = value.get();
            System.out.println("Result = " + result);
        };
        Thread thread1 = new Thread(task1);
        Thread thread2 = new Thread(task2);
        thread1.start();
        thread2.start();
        System.out.println("Expected result = 1");
    }
    public static void createCompletableFuture() {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "Hello, World!";
        });
        future.thenAccept(System.out::println);
    }
}
