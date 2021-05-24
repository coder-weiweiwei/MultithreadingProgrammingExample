package com.example.demo.chapterone;

/**
 *  1.如果具有一个静态全局变量 int i，两个线程同时对它赋值，这两个线程之间是不会相互干扰的。这就是原子性的特点：不可被中断
 *  2.如果全局变量不是int而是long，结果可能就不会那么幸运。因为long有64位，然而系统如果是32位，此时线程之间的结果会相互干扰。
 */
public class MultiThreadLong {
    // 一个全局静态变量
    public static long t = 0;

    /**
     * 一个修改t值的线程
      */
    public static class ChangeT implements Runnable{
        private long to;
        public ChangeT(long to){
            this.to = to;
        }
        @Override
        public void run() {
            // 将传入的值复制给全局变量t
            while (true){
                MultiThreadLong.t = to;
                Thread.yield();
            }
        }
    }

    /**
     * 一个读取t值的想成
     */
    public static class ReadT implements Runnable{

        @Override
        public void run() {
            while (true){
                long temp = MultiThreadLong.t;
                // 当 t 不为以下条件中的值会进行输出
                if(temp != 111L && temp != -999L && temp != 333L && temp != -444L){
                    System.out.println(temp);
                }
            }
        }
    }

    /**
     * 主线程
     */
    public static void main(String[] args) {
        new Thread(new ChangeT(111L)).start();
        new Thread(new ChangeT(-999L)).start();
        new Thread(new ChangeT(333L)).start();
        new Thread(new ChangeT(-444L)).start();
        new Thread(new ReadT()).start();
        /**
         * 1.由于我们给的值都是符合ReadT类run方法中指定条件的值，所以正常条件下是不会出现打印的。
         * 2.但是在32位JVM中，ReaT则可能会读到数据。因为32为重long类型数据的读和写都不具有原子性。所以即有可能出现写乱或者读串位的情况
         */
    }
}
