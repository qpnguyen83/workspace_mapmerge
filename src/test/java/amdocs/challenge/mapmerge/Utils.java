package amdocs.challenge.mapmerge;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 *
 * Date: 20/08/2019
 *
 * @author qpnguyen
 */
public class Utils {
  public static <K, V> void assertMapEquals(Map<K, V> expect, Map<K, V> actual) {
    assertEquals("Size not same", expect.size(), actual.size());
    expect.forEach((k, v) -> {
      V vActual = actual.get(k);
      assertNotNull("Key " + k + " is missing", vActual);
      assertEquals("Value of key " + k + " is not same, expect " + v + ", actual " + vActual, v, vActual);
    });
  }
}
