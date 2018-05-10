package Utilities;

import com.google.common.base.Stopwatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * ${DESCRIPTION}
 *
 * @author mengxp
 * @version 1.0
 * @create 2017-12-14 20:43
 **/
public class StopWatcherTest {
    public static final Logger LOGGER =  LogManager.getLogger(StopWatcherTest.class);
    public static void main(String[] args) throws Exception {
        Stopwatch stopwatch=Stopwatch.createStarted();
        TimeUnit.MILLISECONDS.sleep(5);
        Duration elapsed = stopwatch.stop().elapsed();
        //{} 这个符号在log中类似于占位符 和printf很相似
        LOGGER.info("spend [{}],[{}]",elapsed.toString(),"mengxiaopeng");
    }
}
