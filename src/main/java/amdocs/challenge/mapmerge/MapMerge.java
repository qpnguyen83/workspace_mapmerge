package amdocs.challenge.mapmerge;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * Date: 19/08/2019
 *
 * @author qpnguyen
 */
public class MapMerge  {
  public static <K, V>  Map<K, V> merge(Function<V> function, Map<K, V> map1, Map<K, V> map2) {
    Map<K, V> bigMap, smallMap;
    // Determine the big and small map
    if (map1.size() >= map2.size()) {
      bigMap = map1;
      smallMap = map2;
    } else {
      bigMap = map2;
      smallMap = map1;
    }

    // It's not big so sum is ok here;
    int size = bigMap.size() + smallMap.size();
    Map<K, V> mergedMap = new HashMap<>(size);

    // Put big map to the merged map
    mergedMap.putAll(bigMap);
    // For each element in small map, merge them with merging behavior
    smallMap.forEach(((k, v) -> mergedMap.merge(k, v, function::aggregate)));

    return mergedMap;
  }

  // It depends that we should simple sum the size or need to calculate exactly the number of keys
  // Now, just simply do the sum for fast process
  private static <K, V> int calculateSize(List<Map<K, V>> maps) {

    return maps.stream().mapToInt(Map::size).sum();
//    Set<K> keys = new HashSet<>();
//    maps.forEach(map -> keys.addAll(map.keySet()));
//    return keys.size();

  }

  public static <K, V> Map<K, V> mergeSequence(Function<V> function, List<Map<K, V>> maps) {
    if (maps.isEmpty()) {
      return Collections.emptyMap();
    } else if (maps.size() == 1) {
      return maps.get(0);
    }

    int size = calculateSize(maps);

    Map<K, V> mergedMap = new HashMap<>(size);
    maps.forEach(map -> map.forEach(((k, v) -> mergedMap.merge(k, v, function::aggregate))));

    return mergedMap;
  }

  public static <K, V>  Map<K, V> mergeParallelList(Function<V> function, List<Map<K, V>> listMap ) {
    if (listMap.isEmpty()) {
      return Collections.emptyMap();
    } else if (listMap.size() == 1) {
      return listMap.get(0);
    }

    int size = calculateSize(listMap);

    Map<K, V> mergedMap = new ConcurrentHashMap<>(size);
    listMap.parallelStream().forEach(map -> map.forEach((k, v) -> mergedMap.merge(k, v, function::aggregate)));

    return mergedMap;
  }

  public static <K, V>  Map<K, V> mergeParallelMap(Function<V> function, List<Map<K, V>> listMap ) {
    if (listMap.isEmpty()) {
      return Collections.emptyMap();
    } else if (listMap.size() == 1) {
      return listMap.get(0);
    }

    int size = calculateSize(listMap);

    Map<K, V> mergedMap = new ConcurrentHashMap<>(size);
    listMap.forEach(map -> map.entrySet().parallelStream().forEach(entry -> mergedMap.merge(entry.getKey(), entry.getValue(), function::aggregate)));

    return mergedMap;
  }

  public static <K, V>  Map<K, V> mergeParallelListMap(Function<V> function, List<Map<K, V>> listMap ) {
    if (listMap.isEmpty()) {
      return Collections.emptyMap();
    } else if (listMap.size() == 1) {
      return listMap.get(0);
    }

    int size = calculateSize(listMap);

    Map<K, V> mergedMap = new ConcurrentHashMap<>(size);
    listMap.parallelStream().forEach(map -> map.entrySet().parallelStream().forEach(entry -> mergedMap.merge(entry.getKey(), entry.getValue(), function::aggregate)));

    return mergedMap;
  }


  public static <K, V>  Map<K, V> mergeFlattenAndParallel(Function<V> function, List<Map<K, V>> listMap ) {
    if (listMap.isEmpty()) {
      return Collections.emptyMap();
    } else if (listMap.size() == 1) {
      return listMap.get(0);
    }

    int size = calculateSize(listMap);

    List<Map.Entry<K, V>> listEntry = new ArrayList<>(size);
    listMap.forEach(map -> listEntry.addAll(map.entrySet()));

    Map<K, V> mergedMap = new ConcurrentHashMap<>(size);
    listEntry.parallelStream().forEach(entry -> mergedMap.merge(entry.getKey(), entry.getValue(), function::aggregate));

    return mergedMap;
  }

}
