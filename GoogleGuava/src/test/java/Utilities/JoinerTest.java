package Utilities;

import com.google.common.base.Joiner;
import com.google.common.io.Files;
import com.google.common.primitives.ImmutableIntArray;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.ImmutableMap.of;
import static java.util.stream.Collectors.joining;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;


/**
 *{@link List}
 *
 * @author  mengxp
 * @date   2017-12-10
 * @version 1.0
 *
 */
public class JoinerTest {

    private final List<String> stringList=Arrays.asList(
            "Google","Guava","Java","Scala","Kafka"
    );
    private final String[] stringArray= new String[]{"Jone","JK","Love"};

    private final List<String> stringListWithNullValue=Arrays.asList(
            "Google","Guava","Java","Scala",null
    );
    private final String targetFileName="H:\\project\\GoogleGuava\\src\\main\\joiner_file.txt";
    private final Map<String,String>  stringMap= of("key1","value1","key2","value2");

    @Test
    public void testJoinOnJoin(){
        //String join = Joiner.on("#").join(stringListWithNullValue);
        String join = Joiner.on("#").join(stringList);
        assertThat(join,equalTo("Google#Guava#Java#Scala#Kafka"));
    }
    @Test
    public void testJoinOnJoinArray(){
        //String join = Joiner.on("#").join(stringListWithNullValue);
        String join = Joiner.on("#").join(stringArray);
        assertThat(join,equalTo("Google#Guava#Java#Scala#Kafka"));
    }

    @Test
    public void testJoinOnJoinWithNullValue(){
        String join = Joiner.on("#").skipNulls().join(stringListWithNullValue);
        assertThat(join,equalTo("Google#Guava#Java#Scala"));
    }

    @Test
    public void testJoinOnJoinWithDefaultValue(){
        String join = Joiner.on("#").useForNull("MMMM").join(stringListWithNullValue);
        assertThat(join,equalTo("Google#Guava#Java#Scala#MMMM"));
    }

    @Test
    public void testJoinOnAppendTo_StringBuffer(){
        final StringBuilder builder=new StringBuilder();
        StringBuilder resultbuild = Joiner.on("#").useForNull("MMMM").appendTo(builder, stringListWithNullValue);
        assertThat(resultbuild,sameInstance(builder));
        assertThat(resultbuild.toString(),equalTo("Google#Guava#Java#Scala#MMMM"));
    }

    @Test
    public void testJoinOnAppendTo_Writer(){
     try(FileWriter fileWriter=new FileWriter(new File(targetFileName))){
         Joiner.on("#").useForNull("MMMM").appendTo(fileWriter,stringListWithNullValue);
         assertThat(Files.isFile().test(new File(targetFileName)),equalTo(true));
     } catch (IOException e) {
         fail();
     }
    }


    //采用JDK1.8的来实现
    //Predicate Consumer Function都是java 8的新接口
    //Steam中Supplier：供应商
    //Stream：高特性的流迭代器
    //String::contact
    //Intermediate:中间的(流的过滤等，每个操作会返回新的流)    Terminal:末端终点（执行完这类操作后 stream不能被使用了）   Short-circuiting:短回路的
    @Test
    public void testJoinByStream(){
        String collect = stringListWithNullValue.stream().filter(item -> item != null&&!item.isEmpty()).collect(joining("#"));
        assertThat(collect,equalTo("Google#Guava#Java#Scala#"));
    }

    @Test
    public void testJoinByStreamUserDefalutValue(){
        String collect = stringListWithNullValue.stream()
                .map(item -> item != null&&!item.isEmpty()? item:"DEFALUT")
                .collect(joining("#"));
        assertThat(collect,equalTo("Google#Guava#Java#Scala#"));
    }

    //其中this::defalutValue 代表这方法的引用
    @Test
    public void testJoinByStreamUserDefalutValueUseOtherDisplay(){
        String collect = stringListWithNullValue.stream()
                .map(this::defalutValue)
                .collect(joining("#"));
        assertThat(collect,equalTo("Google#Guava#Java#Scala#"));
    }

    private String defalutValue(final String item){
        return item != null&&!item.isEmpty()? item:"DEFALUT";
    }


    //对map的操作
    @Test
    public void testJoinerToMap(){
        String join = Joiner.on(",").withKeyValueSeparator("=").join(stringMap);
        assertThat("xx",equalTo(join));
    }
}
