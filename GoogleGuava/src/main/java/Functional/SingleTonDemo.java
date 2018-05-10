package Functional;

/**
 * Created by mengxiaopeng on 2017/12/12.
 * matrixOnto.deal.Util.GuavaFunctional
 * 枚举直接实现单列模式 简洁 而且
 */
public enum SingleTonDemo {

    INSTANCE;

     SingleTonDemo() {
        System.out.println("SingTon Con...Times");
    }

    public void om(){
        System.out.println("Om ways..");
    }

    public static void main(String[] args) {
        SingleTonDemo.INSTANCE.om();
        SingleTonDemo.INSTANCE.om();
        SingleTonDemo.INSTANCE.om();

    }
}
