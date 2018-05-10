package Utilities;

import com.google.common.base.Splitter;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author  mengxp
 * @date   2017-12-10
 * @version 1.0
 */
public class SpliterTest {


    /**
     * 封装成集合 trimResults将结果进行trim  omitEmptyStrings 忽略空值
     */
    @Test
    public void testSplitOnSplit() {
        List<String> result = Splitter.on("|").trimResults().omitEmptyStrings().splitToList("hell0 | world||u|need|java||");
        assertThat(result.size(), equalTo(5));
    }

    /**
     * 将字符串aaabbbcccddd 按照3个长度进行分割
     */
    @Test
    public void testSplitFixlength() {
        List<String> result = Splitter.fixedLength(3).trimResults().omitEmptyStrings().splitToList("aaabbbcccddd");
        assertThat(result.size(), equalTo(4));
        System.out.println(result);
    }

    /**
     * action: 当 Splitter.on("|").limit(3) 逐渐的ccc|ddd会作为一个元素
     * aaa|bbb|ccc|ddd  ==》[aaa, bbb, ccc|ddd]
     */
    @Test
    public void testSplit() {
        List<String> result = Splitter.on("|").limit(3).trimResults().omitEmptyStrings().splitToList("aaa|bbb|ccc|ddd");
        assertThat(result.size(), equalTo(3));
        System.out.println(result);
    }

    /**
     * 支持正则表达式字符串和Pattern
     */
    @Test
    public void testSplitOnPatternString() {
        List<String> result = Splitter.onPattern("\\|").limit(3).trimResults().omitEmptyStrings().splitToList("aaa|bbb|ccc|ddd");
        assertThat(result.size(), equalTo(3));
        System.out.println(result);
    }


    /**
     * 支持pattern的正则表达式。
     */
    @Test
    public void testSplitOnPattern() {
        List<String> result = Splitter.on(Pattern.compile("\\|")).limit(3).trimResults().omitEmptyStrings().splitToList("aaa|bbb|ccc|ddd");
        assertThat(result.size(), equalTo(3));
        System.out.println(result);
    }

    /**
     * SplitToMap
     */
    @Test
    public void testSplitOnToMap(){
        Map<String, String> map = Splitter.on(Pattern.compile("\\|")).trimResults().omitEmptyStrings().withKeyValueSeparator("=").split("key1=V1||||key2=V2");
        assertThat(map.size(), equalTo(2));
        System.out.println(map);
    }
}

