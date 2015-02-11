/*
 * Copyright Mirake Ltd. All Rights reserved.
 */

package fi.mirake;

/**
 *
 * @author Teemu
 */
public class BluetoothTester {
  // Check if Bluetooth API optional package (JSR 82) is present.
  public static boolean hasBluetoothAPI ()
  {
    try
      {
        Class.forName ("javax.bluetooth.LocalDevice");
        return true;
      }
    catch (Exception ex)
      {
        return false;
      }
  }
}
