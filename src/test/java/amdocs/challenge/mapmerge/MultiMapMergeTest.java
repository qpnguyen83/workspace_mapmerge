package amdocs.challenge.mapmerge;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;

import static amdocs.challenge.mapmerge.Utils.assertMapEquals;

/**
 *
 * Date: 20/08/2019
 * To simplify the test, we test the merge of Map<Integer, Integer> with the aggregate as v+v
 *
 *
 *
 * @author qpnguyen
 */
public class MultiMapMergeTest {
  private static Map<Integer, Integer> getMap(int start, int sizeMap) {
    Map<Integer, Integer> map = new HashMap<>(sizeMap);
    for (int i=0; i< sizeMap;i++,start++) {
      map.put(start, start);
    }

    return map;
  }

  /**
   * Create a list of map with below value
   * sizeList = 3 , sizeMap = 5 -> There are 3 maps, each map has 5 entries
   * Map1: 1->1, 2->2, 3->3, 4->4, 5->5
   * Map2:       2->2, 3->3, 4->4, 5->5, 6->6
   * Map3:             3->3, 4->4, 5->5, 6->6, 7->7
   * @param sizeList
   * @param sizeMap
   * @return
   */
  private static List<Map<Integer, Integer>> getListMap(int sizeList,  int sizeMap) {
    List<Map<Integer, Integer>> list = new ArrayList<>(sizeList);
    for (int i=1; i<= sizeList;i++) {
      list.add(getMap(i, sizeMap));
    }

    return list;
  }

  private static Map<Integer, Integer> getMergedMap(int listSize, int mapSize) {
    int size = listSize + mapSize - 1;
    int min, max;
    if (listSize <= mapSize) {
      min = listSize;
      max = mapSize;
    } else {
      min = mapSize;
      max = listSize;
    }
    Map<Integer, Integer> map = new HashMap<>(mapSize);
    for (int i=1;i<= size;i++) {
      if (i < min) {
        map.put(i, i * i);
      } else if (i <= max) {
        map.put(i, i * min);
      } else {
        map.put(i, i * (size - i + 1));
      }
    }
    return map;
  }

  private static List<Map<Integer, Integer>> maps;
  private static Map<Integer, Integer> expectedMap;

  @BeforeClass
  public static void createData() {
    // Invoke for class loading processing, not effect first calculation
    MapMerge.merge((i1, i2) -> null, Collections.emptyMap(), Collections.emptyMap());
    assertMapEquals(Collections.emptyMap(), Collections.emptyMap());
  }

  @Test
  public void testMergedMap() {
    doTestMergedMaps(100, 100);
    doTestMergedMaps(20000, 100);
    doTestMergedMaps(100, 20000);
    doTestMergedMaps(2000, 2000);
    doTestMergedMaps(100000, 100);
    doTestMergedMaps(100, 100000);
  }

  @SuppressWarnings("Convert2MethodRef")
  private void doTestMergedMaps(int listSize, int mapSize) {
    long start, end;

    List<Map<Integer, Integer>> maps = getListMap(listSize, mapSize);
    Map<Integer, Integer> expectedMap = getMergedMap(listSize, mapSize);

    System.out.println("====== Execution time list=" + listSize + " - map=" + mapSize + " ==============");

    start = System.currentTimeMillis();
    Map<Integer, Integer> merged1Map = MapMerge.mergeSequence((i1, i2) -> i1 + i2, maps);
    end = System.currentTimeMillis();
    assertMapEquals(expectedMap, merged1Map);
    System.out.println("mergeSequence: " +  (end - start));

    start = System.currentTimeMillis();
    Map<Integer, Integer> merged2Map = MapMerge.mergeParallelList((i1, i2) -> i1 + i2, maps);
    end = System.currentTimeMillis();
    assertMapEquals(expectedMap, merged2Map);
    System.out.println("mergeParallelList: " +  (end - start));

    start = System.currentTimeMillis();
    Map<Integer, Integer> merged3Map = MapMerge.mergeParallelMap((i1, i2) -> i1 + i2, maps);
    end = System.currentTimeMillis();
    assertMapEquals(expectedMap, merged3Map);
    System.out.println("mergeParallelMap: " +  (end - start));

    start = System.currentTimeMillis();
    Map<Integer, Integer> merged4Map = MapMerge.mergeParallelListMap((i1, i2) -> i1 + i2, maps);
    end = System.currentTimeMillis();
    assertMapEquals(expectedMap, merged4Map);
    System.out.println("mergeParallelListMap: " +  (end - start));

    start = System.currentTimeMillis();
    Map<Integer, Integer> merged5Map = MapMerge.mergeFlattenAndParallel((i1, i2) -> i1 + i2, maps);
    end = System.currentTimeMillis();
    assertMapEquals(expectedMap, merged5Map);
    System.out.println("mergeFlattenAndParallel:" +  (end - start));

    System.out.println("===========================");
    System.out.println();
  }

}
