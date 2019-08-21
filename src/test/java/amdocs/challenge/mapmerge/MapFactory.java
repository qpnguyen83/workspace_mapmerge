package amdocs.challenge.mapmerge;

import java.util.HashMap;
import java.util.Map;

/**
 * Date: 20/08/2019
 *
 * @author qpnguyen
 */
public class MapFactory  <K, V> {
  private Map<K, V> map = new HashMap<>();

  public MapFactory<K, V> put (K k, V v) {
    // Ensure that the map should be created correctly, avoid sloppy code
    if (map.containsKey(k)) {
      throw new RuntimeException("Invalid map creation");
    }
    map.put(k, v);
    return this;
  }

  public Map<K, V> getMap() {
    return map;
  }
}