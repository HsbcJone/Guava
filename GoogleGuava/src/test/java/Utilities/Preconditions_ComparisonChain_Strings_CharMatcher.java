package Utilities;


import com.google.common.base.CharMatcher;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * ${DESCRIPTION}
 * 1.preconditions 断言:在类似于条件解析时，进行一些重要的验证,验证为空。
 * 2.ComparisonChain 编写比较器
 * 3.Strings 字符串的处理
 * 4.CharMatcher 字符的处理
 * @author mengxp
 * @version 1.0
 * @create 2017-12-11 19:50
 **/
public class Preconditions_ComparisonChain_Strings_CharMatcher {

    //1.***********preconditions*******
    @Test
    public void testPreconditions_NotNull(){
        List<String> var1=null;
        Preconditions.checkNotNull(var1,"Collection should  not  be nullable...",var1);
    }

    @Test
    public void testPreconditions_checkArgument(){
        final String num="132";
        Preconditions.checkArgument(num.equals("133"),"must be equals",num);
    }
    //检查数组的角标越界
    @Test
    public void testPreconditions_checkElementIndex(){
        Preconditions.checkElementIndex(2,3,"max length is=2");
    }

    //3.************Strings********
    @Test
    public void testStringsRepeat(){
        String var="mengxp";
        String repeat = Strings.repeat(var, 2);
        assertThat(repeat,equalTo("mengxpmengxp"));

        String commonPrefix= Strings.commonPrefix("AAASS", "AAWW");
        assertThat(commonPrefix,equalTo("AA"));

        String commonSuffix= Strings.commonSuffix("AAASS", "AAWWSSS");
        assertThat(commonSuffix,equalTo("SS"));

        // padEnd(String string, int minLength, char padChar) 中minLength为结果的字符串的长度=string+(minlength-原stringLength)*padChar
        String padEnd= Strings.padEnd("Jone", 6, 'x');
        String padStart= Strings.padStart("78", 4, '0');

        String empty= Strings.nullToEmpty(null);
        assertThat(empty,equalTo(""));

        String nulll = Strings.emptyToNull("");
        assertThat(nulll,equalTo(null));

        assertTrue(Strings.isNullOrEmpty(""));
    }

    //4.************CharMatcher 字符的处理************
    @Test
    public void testCharMatcher(){
        assertThat(CharMatcher.javaDigit().matches('1'),equalTo(true));
        //获取字符串中某个字符的个数
        assertThat(CharMatcher.is('A').countIn("mengxp is good boy .got AAAAA"),equalTo(5));
        assertThat(CharMatcher.breakingWhitespace().collapseFrom("meng xiao peng",'*'),equalTo("meng*xiao*peng"));
        assertThat(CharMatcher.javaDigit().or(CharMatcher.whitespace()).removeFrom("hello 123 world"),equalTo("helloworld"));
        assertThat(CharMatcher.javaDigit().or(CharMatcher.whitespace()).retainFrom("hello 123 world"),equalTo(" 123 "));
    }

    /**
     * 2.ComparisonChain 编写比较器
     */
    static class ComparisonChainObj implements Comparable<ComparisonChainObj>{
        private int total;
        private int input;
        private int output;
        private String name;

        public ComparisonChainObj(int input, int output, String name) {
            this.input = input;
            this.output = output;
            this.total=input-output;
            this.name = name;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getInput() {
            return input;
        }

        public void setInput(int input) {
            this.input = input;
        }

        public int getOutput() {
            return output;
        }

        public void setOutput(int output) {
            this.output = output;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ComparisonChainObj that = (ComparisonChainObj) o;

            if (total != that.total) return false;
            if (input != that.input) return false;
            if (output != that.output) return false;
            return name.equals(that.name);
        }

        @Override
        public int hashCode() {
            int result = total;
            result = 31 * result + input;
            result = 31 * result + output;
            result = 31 * result + name.hashCode();
            return result;
        }

        //采用ComparisonChain 以total,input,name 三者来排序,按照先后顺序
        @Override
        public int compareTo(ComparisonChainObj o) {
           return ComparisonChain.start().compare(this.total,o.getTotal())
                    .compare(this.input,o.getInput())
                    .compare(this.name,o.getName()).result();
        }

        public static void main(String[] args) {
            ComparisonChainObj var1=new ComparisonChainObj(200,100,"x");
            ComparisonChainObj var2=new ComparisonChainObj(300,200,"c");
            ComparisonChainObj var3=new ComparisonChainObj(300,150,"a");
            ComparisonChainObj var4=new ComparisonChainObj(200,150,"d");
            ArrayList<ComparisonChainObj> objs = Lists.newArrayList(var1, var2, var3, var4);
            Preconditions.checkNotNull(objs);
            TreeSet<ComparisonChainObj> comparisonChainObjs = Sets.newTreeSet(objs);
            System.out.println(comparisonChainObjs);
        }
    }
}
