package com.salen;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class FluxTest {

    public static void main(String[] args) {
//        createFlux();
//        operateFlux();
        merge();
    }

    public static Mono<Void> when(Flux<File> files){
        // TODO
//        return files.flatMap(file -> {
//            Mono<Void> copyFile = null;
//            Mono<Void> deleteFile = null;
//            return Mono.when(copyFile, deleteFile);
//        }
//        );
        return null;
    }

    /**
     *  把多个流合并成一个Flux序列，该操作按所有流中的元素实际产生顺序来合并
     */
    public static void merge(){
        Flux<Integer> f1 = Flux.range(0, 5);
        Flux<Integer> f2 = Flux.range(8, 2);
        Flux.merge(f1, f2).subscribe(System.out:: println);
//        0
//        1
//        2
//        3
//        4
//        8
//        9

    }

    public static void operateFlux(){
        //  最大10个一组
        Flux.range(0, 49).buffer(10).subscribe(System.out::println);
//        [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
//        [10, 11, 12, 13, 14, 15, 16, 17, 18, 19]
//        [20, 21, 22, 23, 24, 25, 26, 27, 28, 29]
//        [30, 31, 32, 33, 34, 35, 36, 37, 38, 39]
//        [40, 41, 42, 43, 44, 45, 46, 47, 48]
        System.out.println("---------------------------");


        // 能被4整除的分割  bufferUntil会一直收集数据，直到Predicate返回true
        Flux.range(0, 20).bufferUntil(new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) {
                return integer % 4 == 0;
            }
        }).subscribe(System.out::println);
//        [0]
//        [1, 2, 3, 4]
//        [5, 6, 7, 8]
//        [9, 10, 11, 12]
//        [13, 14, 15, 16]
//        [17, 18, 19]


        System.out.println("---------------------------");

        // bufferwhile  只收集断言为true的数据
        Flux.range(0, 20).bufferWhile(i -> i % 3 == 0 || i % 5 == 0 ).subscribe(System.out::println);
//        [0]
//        [3]
//        [5, 6]
//        [9, 10]
//        [12]
//        [15]
//        [18]

        System.out.println("---------------------------");


        //  将一个Flux分割成多个Flux
        Flux.range(0, 10).window(3).subscribe(new Consumer<Flux<Integer>>() {
            @Override
            public void accept(Flux<Integer> integerFlux) {
                integerFlux.subscribe(System.out::println);
                System.out.println("\t---------------------------");
            }
        });
//                ---------------------------
//        0
//        1
//        2
//                ---------------------------
//        3
//        4
//        5
//                ---------------------------
//        6
//        7
//        8
//                ---------------------------
//        9

    }

    public static void createFlux(){
        Flux.just("Hello", "world").subscribe(System.out:: println);

        Flux.just("Hello", "world").subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) {
                System.out.println(s);
            }
        });

        System.out.println("---------------------------");

        Flux.fromArray(new Integer[]{1, 2, 3}).subscribe(System.out:: println);

        System.out.println("--------------------------");

        Flux.empty().subscribe(System.out:: println);

        System.out.println("--------------------------");

        Flux.range(1, 5).subscribe(System.out:: println);

        System.out.println("--------------------------");


        // Flux.generate  next 方法只能被调用一次，否则会抛出异常： Exceptions$ErrorCallbackNotImplemented: java.lang.IllegalStateException: More than one call to onNext
        Flux.generate(sink -> {
            sink.next("Hello");
            sink.complete();
        }).subscribe(System.out:: println);

        System.out.println("--------------------------");

        // Flux.create  next(Object)可被多次调用
        Flux.create(sink -> {
            for(int i = 0; i < 9; i++){
                sink.next(i);
            }
        }).subscribe(System.out:: println);


    }
}
