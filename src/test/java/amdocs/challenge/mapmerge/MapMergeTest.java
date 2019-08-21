package amdocs.challenge.mapmerge;

import org.junit.Test;

import java.util.Map;

import static amdocs.challenge.mapmerge.Utils.assertMapEquals;

/**
 * Date: 20/08/2019
 *
 * @author qpnguyen
 */
public class MapMergeTest {

  @Test
  public void testSimpleMergeMapStringInteger() {
    Map<String, Integer> map1 = new MapFactory<String, Integer>().put("key1", 20).put("key2", 30).getMap();
    Map<String, Integer> map2 = new MapFactory<String, Integer>().put("key3", 40).put("key1", 50).getMap();

    Map<String, Integer> merge1Expect = new MapFactory<String, Integer>().put("key1", 140).put("key2", 30).put("key3", 40).getMap();
    Map<String, Integer> merge1Actual = MapMerge.merge((i1, i2) -> 2 * (i1 + i2), map1, map2);
    assertMapEquals(merge1Expect, merge1Actual);

    Map<String, Integer> merge2Expect = new MapFactory<String, Integer>().put("key1", 1000).put("key2", 30).put("key3", 40).getMap();
    Map<String, Integer> merge2Actual = MapMerge.merge((i1, i2) -> i1 * i2, map1, map2);
    assertMapEquals(merge2Expect, merge2Expect);
  }

  @Test
  public void testSimpleMergeMapIntegerString() {
    Map<Integer, String> map1 = new MapFactory<Integer, String>().put(10, "10").put(11, "11").getMap();
    Map<Integer, String> map2 = new MapFactory<Integer, String>().put(12, "12").put(10, "100").getMap();

    Map<Integer, String> merge1Expect = new MapFactory<Integer, String>().put(10, "10-100").put(11, "11").put(12, "12").getMap();
    Map<Integer, String> merge1Actual = MapMerge.merge((i1, i2) -> i1 + "-"+ i2, map1, map2);
    assertMapEquals(merge1Expect, merge1Actual);

    Map<Integer, String> merge2Expect = new MapFactory<Integer, String>().put(10, "2-10100").put(11, "11").put(12, "12").getMap();
    Map<Integer, String> merge2Actual = MapMerge.merge((i1, i2) -> "2-" + i1 + i2, map1, map2);
    assertMapEquals(merge2Expect, merge2Actual);
  }
}
