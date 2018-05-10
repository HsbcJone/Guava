package Functional;

import com.google.common.base.*;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;


/**
 * Created by mengxiaopeng on 2017/12/12.
 * matrixOnto.deal.Util.GuavaFunctional
 * 1.Predicate接口  输入一个T 返回一个Boolean 一般用于集合过滤等
 * 2.Predicates的用法 结合集合开始过滤。
 * 3.将List<Persion> <=Maps.uniqueIndex=>转换成Map<String,Persion>
 */
public class PredicatesDemo {

    public static void main(String[] args) {
        PredicatesDemo demo = new PredicatesDemo();
        //1.采用正则表达式
        Predicate<Object> objectPredicate = Predicates.alwaysFalse();
        Predicate<CharSequence> charSequencePredicate = Predicates.containsPattern("\\*");
        boolean apply = charSequencePredicate.apply("12*3");
        assertThat(apply, equalTo(true));
        //2.关于Predicates 中具体的方法配合集合进行过滤
        ArrayList<User> users = Lists.newArrayList(new User("Jone", 27, 15000d), new User("Vieech", 30, 10000d), new User("Hopy", 4, 50l));
        List<User> ss = demo.listFilter(users, "Jone", 3, "ors");
        System.out.println(ss);
        //3.compaose 用法
        //3.1.将List转换为Map
        Function function = demo.getNameFunction();
        ImmutableMap<String, User> immutableMap = Maps.uniqueIndex(users, function);
        //3.2.将Map转换成Function
        Function<String, User> lookUp = Functions.forMap(immutableMap);
        //3.3 采用Predicates的compose方法
        Predicate<String> compose = Predicates.compose(demo.filterAge(10), lookUp);
        boolean xx = compose.apply("Jone");

    }

    /**
     * 根据输入的inputs 对name age 做过滤，过滤的模式取决于type(and or模式)
     *
     * @param inputs
     * @param name
     * @param age
     * @param type
     * @return
     */
    public List<User> listFilter(List<User> inputs, String name, int age, String type) {
        Preconditions.checkNotNull(inputs, "input soures can not be null..");
        Predicate<User> predicateName = filteName(name);
        Predicate<User> predicateAge = filterAge(age);
        //将两个Predicate进行并集 合并两个过滤的结果
        Predicate or = Predicates.or(predicateAge, predicateName);
        //将两个Predicate进行交集，先成交集的Predicate然后进行过滤
        Predicate<User> and = Predicates.and(predicateAge, predicateName);
        if ("or".equalsIgnoreCase(type)) {
            and = or;
        }
        Iterable<User> filter = Iterables.filter(inputs, and);
        ArrayList arrayList = Lists.newArrayList(filter);
        return arrayList;
    }

    /**
     * 其中输入时User对象，返回时String对象
     *
     * @return
     */
    private Function getNameFunction() {
        return new Function<User, String>() {
            @Nullable
            @Override
            public String apply(@Nullable User user) {
                Preconditions.checkNotNull(user);
                return user.getUserName();
            }

            @Override
            public boolean equals(@Nullable Object o) {
                return false;
            }
        };
    }

    /**
     * 过滤name
     *
     * @param name
     */
    private Predicate<User> filteName(final String name) {
        return new Predicate<User>() {
            @Override
            public boolean apply(@Nullable User user) {
                Preconditions.checkNotNull(user);
                if (user.getUserName().equalsIgnoreCase(name)) {
                    return true;
                }
                return false;
            }

            @Override
            public boolean equals(@Nullable Object o) {
                return false;
            }
        };
    }

    /**
     * 根据age过滤
     *
     * @param filterAge
     */
    private Predicate<User> filterAge(int filterAge) {
        return new Predicate<User>() {
            @Override
            public boolean apply(@Nullable User user) {
                Preconditions.checkNotNull(user);
                if (user.getAge() > filterAge) {
                    return true;
                }
                return false;
            }

            @Override
            public boolean equals(@Nullable Object o) {
                return false;
            }
        };
    }


    static class User {
        private String userName;
        private int age;
        private double salary;

        public User(String userName, int age, double salary) {
            this.userName = userName;
            this.age = age;
            this.salary = salary;
        }

        public String getUserName() {
            return userName;
        }

        public User setUserName(String userName) {
            this.userName = userName;
            return this;
        }

        public int getAge() {
            return age;
        }

        public User setAge(int age) {
            this.age = age;
            return this;
        }

        public double getSalary() {
            return salary;
        }

        public User setSalary(double salary) {
            this.salary = salary;
            return this;
        }
    }
}
