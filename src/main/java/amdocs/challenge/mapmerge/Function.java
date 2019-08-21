package amdocs.challenge.mapmerge;

/**
 *
 * Date: 19/08/2019
 *
 * @author qpnguyen
 */
public interface Function<V> {
  V aggregate(V v1, V v2);
}
