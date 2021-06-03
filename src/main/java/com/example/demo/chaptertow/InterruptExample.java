package com.example.demo.chaptertow;

/**
 * 线程中断相关处理
 */
public class InterruptExample {

    /**
     * 对线程调用了interrupt()方法，线程会被至上中断状态，但是这个中断并不会发生任何作用
     */
    public static void setInterrupt() throws InterruptedException {
        Thread t1 = new Thread(){
            @Override
            public void run() {
                while (true){
                    Thread.yield();
                }
            }
        };
        t1.start();
        Thread.sleep(2000);
        // 线程被直上中断标志，但是该线程不会有任何改变
        t1.interrupt();
    }

    /**
     * 如果希望线程在中断后退出，需要对中断进行处理
     */
    public static void interruptThread() throws InterruptedException {
        Thread t1 = new Thread(){
            @Override
            public void run() {
                while (true){
                    if(Thread.currentThread().isInterrupted()){
                        System.out.println("Interrupted!");
                        break;
                    }
                    Thread.yield();
                }
            }
        };
        t1.start();
        Thread.sleep(2000);
        // 线程被直上中断标志，但是该线程不会有任何改变
        t1.interrupt();
    }

    /**
     * 通过interruptThreadException捕捉异常来处理中断
     */
    public static void interruptThreadException() throws InterruptedException {
        Thread t1 = new Thread(){
            @Override
            public void run() {
                while (true){
                    if(Thread.currentThread().isInterrupted()){
                        System.out.println("Interrupt");
                        break;
                    }
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        System.out.println("Interruted When Sleep");
                        /**
                         * 设置中断状态
                         * 为什么存在该步骤？
                         *  1.在catch子句中，我们是可以直接退出线程的，但是并没有这样做。因为我们需要对后续进行处理，保证数据的一致性和完整性
                         *  2.在下方再次执行interrupt()，因为代码中需要通过Thread.currentThread().isInterrupted()判断线程是否中断并且停止继续执行
                         *  3.由于中断被捕获，所以会中断标记会被清除，下次循环就无法判断，所以需要再次这是中断标志位。
                         */

                        Thread.currentThread().interrupt();
                    }
                }
            }
        };
        t1.start();
        Thread.sleep(3000);
        t1.interrupt();
    }


    public static void main(String[] args) throws InterruptedException {
//        setInterrupt();
//        interruptThread();
        interruptThreadException();
    }
}
