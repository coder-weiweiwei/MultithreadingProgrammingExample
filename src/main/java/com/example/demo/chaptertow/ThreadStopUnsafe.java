package com.example.demo.chaptertow;

/**
 * 该案例主要用于验证通过stop()关闭线程导致的数据不一致问题
 */
public class ThreadStopUnsafe {
    public static User user = new User();
    public static class User{
        private long id;
        private String name;
        public User(){
            this.id = 0;
            this.name = "0";
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "User{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

    /**
     * 修改属性用户的线程
     */
    public static class ChangeObjectThread extends Thread{
        @Override
        public void run() {
            while (true){
                synchronized (user){
                    long v =  System.currentTimeMillis()/1000;
                    user.setId(v);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    user.setName(v+"");
                }
                Thread.yield();
            }
        }
    }

    /**
     * 正确退出线程的方法
     */
    public static class ChangeObjectThread2 extends Thread{
        // 线程是否继续执行的标志位
        volatile boolean stopme = false;
        public void stopMe(){
            stopme = true;
        }
        @Override
        public void run() {
            while (true){
                if(stopme){
                    System.out.println("exit by stop me");
                    break;
                }
                synchronized (user){
                    long v =  System.currentTimeMillis()/1000;
                    user.setId(v);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    user.setName(v+"");
                }
                Thread.yield();
            }
        }
    }

    /**
     * 读取用户属性线程
     */
    public static class ReadObjectThread extends Thread{
        @Override
        public void run() {
            while (true){
                synchronized (user){
                    // 当用户id的值和用户name的值不一致会进行打印
                    if(!user.name.equals(user.id+"")){
                        System.out.println(user.toString());
                    }
                }
                Thread.yield();
            }
        }
    }

    /**
     * 主线程
     */
    public static void main(String[] args) throws InterruptedException {
        new ReadObjectThread().start();
        while (true){
            // 修改用户线程将通过stop()的方式停止
            Thread t = new ChangeObjectThread();
            t.start();
            t.sleep(150);
            t.stop();
        }
    }

    /**
     * 主函数打印了大量日志，如下
     * User{id=1621846651, name='1621846650'}
     * User{id=1621846651, name='1621846650'}
     * User{id=1621846651, name='1621846650'}
     * id和name不一致。出现该现象就是因为stop()方法的调用导致了数据不一致。
     */

}
