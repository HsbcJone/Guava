package Functional;

import com.google.common.base.*;
import com.google.common.collect.*;

import javax.annotation.Nullable;
import java.util.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Created by mengxiaopeng on 2017/12/12.
 * matrixOnto.deal.Util.GuavaFunctional
 * 1.Function 接口 输入一个对象，返回一个对象
 * 2.Functions作用:是一个工具类，和集合+Function集合起来
 * 3.List<Obj> ->List<String> or Map<String,Obj>
 * 4.Sets 集合的交并差
 * 5.Maps 工具类是对Map的操作 很多特殊种类的map
 * 6.Ranges 生成开区间和闭区间的Range
 */
public class FunctionsDemo{


    public static void main(String[] args) {
        //1.List<Obj> ->List<String>
        ArrayList<User> users = Lists.newArrayList(new User("Jone", 27, 15000d), new User("Vieech", 30, 10000d), new User("Hopy", 4, 50l));
        List<String> strings = commonTransFunction(users);
        System.out.println("x");
        //2.关于Functions的用法 将List转换为Map
        ImmutableMap<String, User> functionsMap = commonTransFunctionToMap(users);
        Function<String, User> lookUp = Functions.forMap(functionsMap);
        //提供给后面compose的方法
        Predicate<String> compose = Predicates.compose(new Predicate<User>() {
            @Override
            public boolean apply(@Nullable User user) {
                Preconditions.checkNotNull(user);
                if (user.getUserName().equalsIgnoreCase("Jone")) {
                    return true;
                }
                return false;
            }

            @Override
            public boolean equals(@Nullable Object o) {
                return false;
            }
        }, lookUp);
        //流程是  Jone#27#15000.0 查询lookUp等到 User,通过Predicate<User>进行断言，看是否满足
        //common 流程是 输入得到Function的输出，传入Predicate进行断言，返回结果
        boolean jone1 = compose.apply("Jone#27#15000.0");
        assertThat(jone1, equalTo(true));
        //3.List<Obj> ->Map<String,Obj>
        ImmutableMap<String, User> map = commonTransFunctionToMap(users);
        System.out.println("xx");
        //4.Sets 集合的交并差
        HashSet<String> var1 = Sets.newHashSet("2", "5", "55");
        HashSet<String> var2 = Sets.newHashSet("2", "6", "5");
        HashSet<String> var3 = Sets.newHashSet("20", "9", "955");
        //4.1集合之间的不同
        Sets.SetView<String> difference1 = Sets.difference(var1, var2);
        Sets.SetView<String> difference2 = Sets.difference(var2, var1);
        for (String str:difference1){
            System.out.println(str);
        }
        //4.2集合之间的对称差
        Sets.SetView<String> symmetricDifference = Sets.symmetricDifference(var1, var2);
        //4.3 集合之间的交集
        Sets.SetView<String> intersection = Sets.intersection(var1, var3);
        //4.4 集合之间的并集
        Sets.SetView<String> union = Sets.union(var1, var2);
        System.out.println("xx");
        //5.Maps 工具类是对Map的操作
        //5.1 采用ArrayList一键多值 相同的值可以重复
        ArrayListMultimap<String, String> arrayListMultimap = ArrayListMultimap.create();
        arrayListMultimap.put("foo","1");
        arrayListMultimap.put("foo","2");
        arrayListMultimap.put("foo","3");
        arrayListMultimap.put("bar","a");
        arrayListMultimap.put("bar","a");
        arrayListMultimap.put("bar","b");
        for (String key:arrayListMultimap.keySet()){
            System.out.println("key="+key+",value="+arrayListMultimap.get(key));
        }
        //key=bar,value=[a, a, b] //这里可以存在重复的key指
        //key=foo,value=[1, 2, 3]

        //5.2 采用HashTable保存一键多值 相同的值不能重复
        HashMultimap<String, String> hashMultimap = HashMultimap.create();
        hashMultimap.put("foo","1");
        hashMultimap.put("foo","2");
        hashMultimap.put("foo","3");
        hashMultimap.put("bar","a");
        hashMultimap.put("bar","a");
        hashMultimap.put("bar","b");
        System.out.println(hashMultimap);
        //{bar=[a, b], foo=[1, 2, 3]}   其中重复的bar-a bar-a 只保留了一个

        //5.3 多键类Table 理解为Key key2 value的Map
        HashBasedTable<Object, Object, Object> kkvMap = HashBasedTable.create();
        kkvMap.put(1,1,"book");kkvMap.put(1,2,"turkey");kkvMap.put(2,10,"apple");kkvMap.put(2,2,"orange");
        assertThat(kkvMap.get(1,2), equalTo("turkey"));
       assertThat(kkvMap.contains(2,3),equalTo(false));
        assertThat(kkvMap.containsRow(2), equalTo(true));
        boolean book = kkvMap.containsValue("book");
        boolean columnOk = kkvMap.containsColumn(10);
        Map<Object, Object> row = kkvMap.row(1);//得到Map
        Map<Object, Object> column = kkvMap.column(2);//得到comkey为2的Map<key=rowKey,value=value>
        Set<Object> columnKeySet = kkvMap.columnKeySet();
        Set<Object> rowKeySet = kkvMap.rowKeySet();
        //5.4 可以通过value获取Key的HashBiMap
        HashBiMap biMap=HashBiMap.create();
        biMap.put("hello","world");
        //biMap.put("abc","world");//会失败
        biMap.forcePut("abc","world");//强制put
        biMap.put("123","tell");
        biMap.put("123","none");//none会覆盖上面的tell
        //*****键值互换得到新的map
        BiMap inverse = biMap.inverse();
        assertThat(inverse.get("none"), equalTo("123"));
        System.out.println("xx");
        //5.5 不可变集合类 ImmutableListMultMap
        //不可变的集合类，都有一个Builder的内部类，不可以修改添加
        ImmutableListMultimap<Integer, String> immutableListMap = new ImmutableListMultimap.Builder<Integer, String>()
                .put(1, "hello")
                .putAll(2, "abc", "log", "in")
                .putAll(3, "get", "up").build();
        //immutableListMap.put(1,"3"); 报错，不支持重新Put
        System.out.println(immutableListMap.get(2));
        //6 关于Range的定义，开区间和闭区间
//        Range<String> closed = Ranges.closed("a", "z");
//        boolean b = closed.contains("b");
//        Range<Integer> open = Ranges.open(1, 7);
//        boolean contains = open.contains(2);
//        Predicate<User> composeInd = Predicates.compose(open, new Function<User, Integer>() {
//            @Nullable
//            @Override
//            public Integer apply(@Nullable User user) {
//                Preconditions.checkNotNull(user);
//                return user.getAge();
//            }
//        });
//        boolean jone = composeInd.apply(new User("Jone", 2, 15000d));
//        System.out.println(closed);

    }

    /**
     * 将List<Obj> <--> 转换成 List<String>
     * @return
     */
    private List<String> transFunctionOne() {
        ArrayList<User> users = Lists.newArrayList(new User("Jone", 27, 15000d), new User("Vieech", 30, 10000d), new User("Hopy", 4, 50l));
        Function<User,String> function = new Function<User, String>() {
            @Nullable
            @Override
            public String apply(@Nullable User user) {
                Preconditions.checkNotNull(user);
                return Joiner.on("#").join(user.getUserName(),user.getAge(),user.getSalary());
            }
            @Override
            public boolean equals(@Nullable Object o) {
                return false;
            }
        };
        FluentIterable<String> transform = FluentIterable.from(users).transform(function);
        ArrayList<String> strings = Lists.newArrayList(transform);
        return strings;
    }

    /**
     * 比较抽象的一个通用的解析方法
     * CommonVo 所有的不同的类型的类型都继承
     * 抽取出返回的通用的List
     * 适用的场景是:通用的将List<VO> 转换成List<String>
     * @param inputs
     * @param <I>
     * @return
     */
    private static <I> List<String> commonTransFunction(List<I> inputs ) {
        Function<I,String> function = new Function<I, String>() {
            @Nullable
            @Override
            public String apply(@Nullable I input) {
                Preconditions.checkNotNull(input);
                //在这里也可以采用switch的方式，通过判断不同的类型的input来进行特殊的返回值的包装
                if (input instanceof CommonVo){
                    CommonVo var1 = (CommonVo) input;
                    return Joiner.on("#").join(var1.getUserName(),var1.getAge(),var1.getSalary());
                }
                return "";
            }
            @Override
            public boolean equals(@Nullable Object o) {
                return false;
            }
        };
        FluentIterable<String> transform = FluentIterable.from(inputs).transform(function);
        ArrayList<String> strings = Lists.newArrayList(transform);
        return strings;
    }


    /**
     * 比较抽象的一个通用的解析方法
     * CommonVo 所有的不同的类型的类型都继承
     * 抽取出返回的通用的Map
     * 适用的场景是:通用的将List<VO> 转换成Map<String,Vo>
     * @param inputs
     * @param <I>
     * @return
     */
    private static <I> ImmutableMap<String,I> commonTransFunctionToMap(List<I> inputs ) {
        Function<I,String> function = new Function<I, String>() {
            @Nullable
            @Override
            public String apply(@Nullable I input) {
                Preconditions.checkNotNull(input);
                //在这里也可以采用switch的方式，通过判断不同的类型的input来进行特殊的返回值的包装
                if (input instanceof CommonVo){
                    CommonVo var1 = (CommonVo) input;
                    return Joiner.on("#").join(var1.getUserName(),var1.getAge(),var1.getSalary());
                }
                return "";
            }
            @Override
            public boolean equals(@Nullable Object o) {
                return false;
            }
        };
        ImmutableMap<String, I> stringIImmutableMap = Maps.uniqueIndex(inputs, function);
        return stringIImmutableMap;
    }

    static abstract class CommonVo{
         protected String userName;
         protected int age;
         protected double salary;

         public CommonVo(String userName, int age, double salary) {
             this.userName = userName;
             this.age = age;
             this.salary = salary;
         }

         public String getUserName() {
             return userName;
         }

         public CommonVo setUserName(String userName) {
             this.userName = userName;
             return this;
         }

         public int getAge() {
             return age;
         }

         public CommonVo setAge(int age) {
             this.age = age;
             return this;
         }

         public double getSalary() {
             return salary;
         }

         public CommonVo setSalary(double salary) {
             this.salary = salary;
             return this;
         }

     }


   static class User extends CommonVo {

        public User(String userName, int age, double salary) {
            super(userName, age, salary);
        }
    }
}
