package com.tomwen.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.Random;
import java.util.Scanner;

/**
 * 关于jvm的一些信息
 * 这个类还演示使用enum做为单例的设计模式
 * Created by tomwen on 2017/1/16.
 */
public enum Jvm {
  ;

  public static Logger logger = LoggerFactory.getLogger(Jvm.class);
  private static final boolean IS64BIT = is64Bit0();
  private static final int PROCESS_ID = getProcessId0();
  private static final String OS = System.getProperty("os.name").toLowerCase();
  public static final int PID_BITS = Maths.intLog2(getPidMax());
  public static String TMP = System.getProperty("java.io.tmpdir");

  public static boolean is64Bit() {
    return IS64BIT;
  }

  private static boolean is64Bit0() {
    String systemProp;
    systemProp = System.getProperty("com.ibm.vm.bitmode");
    if (systemProp != null) {
      return "64".equals(systemProp);
    }
    systemProp = System.getProperty("sun.arch.data.model");
    if (systemProp != null) {
      return "64".equals(systemProp);
    }
    systemProp = System.getProperty("java.vm.version");
    return systemProp != null && systemProp.contains("_64");
  }

  public static int getProcessId() {
    return PROCESS_ID;
  }

  private static int getProcessId0() {
    String pid = null;
    final File self = new File("/proc/self");
    try {
      if (self.exists()) {
        pid = self.getCanonicalFile().getName();
      }
    } catch (IOException ignored) {
    }

    if (pid == null) {
      pid = ManagementFactory.getRuntimeMXBean().getName().split("@", 0)[0];
    }

    if (pid == null) {
      int rpid = new Random().nextInt(1 << 16);
      logger.warn("Unable to determine PID, picked a random number=" + rpid);

      return rpid;

    } else {
      return Integer.parseInt(pid);
    }
  }

  /**
   * This may or may not be the OS thread id, but should be unique across processes
   *
   * @return a unique tid of up to 48 bits.
   */
  public static long getUniqueTid() {
    return getUniqueTid(Thread.currentThread());
  }

  public static long getUniqueTid(Thread thread) {
    // Assume 48 bit for 16 to 24-bit process id and 16 million threads from the start.
    return ((long) getProcessId() << 24) | thread.getId();
  }

  public static boolean isWindows() {
    return OS.startsWith("win");
  }

  public static boolean isWindows10() {
    return OS.startsWith("win") && OS.endsWith("10");
  }

  public static boolean isMacOSX() {
    return OS.contains("mac");
  }

  public static boolean isLinux() {
    return OS.startsWith("linux");
  }

  public static boolean isUnix() {
    return OS.contains("nix") ||
      OS.contains("nux") ||
      OS.contains("aix") ||
      OS.contains("bsd") ||
      OS.contains("sun") ||
      OS.contains("hpux");
  }

  public static boolean isSolaris() {
    return OS.startsWith("sun");
  }

  public static long getPidMax() {
    if (isLinux()) {
      File file = new File("/proc/sys/kernel/pid_max");
      if (file.canRead()) {
        Scanner scanner = null;
        try{
          scanner = new Scanner(file);
          return Maths.nextPower2(scanner.nextLong(), 1 << 16);
        } catch (FileNotFoundException e) {
          logger.warn("", e);
        }finally {
          if(scanner != null)
            scanner.close();
        }
      }
    } else if (isMacOSX()) {
      return 1L << 24;
    }

    // the default.
    return isWindows10() ? 1L << 24 : 1L << 16;
  }

  private static String convertStreamToString(java.io.InputStream is) {
    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
    return s.hasNext() ? s.next() : "";
  }

  /**
   * 返回windows 空闲内存有多少bytes
   */
  public static long freePhysicalMemoryOnWindowsInBytes() throws IOException {
    if (!isWindows()) {
      throw new IllegalStateException("Method freePhysicalMemoryOnWindowsInBytes() should " +
        "be called only on windows. Use Jvm.isWindows() to check the OS.");
    }

    Process pr = Runtime.getRuntime().exec("wmic OS get FreePhysicalMemory /Value");
    try {
      int result = pr.waitFor();
      String output = convertStreamToString(pr.getInputStream());
      if (result != 0) {
        String errorOutput = convertStreamToString(pr.getErrorStream());
        throw new IOException("Couldn't get free physical memory on windows. " +
          "Command \"wmic OS get FreePhysicalMemory /Value\" exited with " +
          result + " code, output: \"" + output + "\", error output: \"" +
          errorOutput + "\"");
      }
      String[] parts = output.trim().split("=");
      if (parts.length != 2) {
        throw new IOException("Couldn't get free physical memory on windows. " +
          "Command \"wmic OS get FreePhysicalMemory /Value\" output has unexpected " +
          "format: \"" + output + "\"");
      }
      try {
        return (Long.parseLong(parts[1])) * 1024L;
      } catch (NumberFormatException e) {
        throw new IOException("Couldn't get free physical memory on windows. " +
          "Command \"wmic OS get FreePhysicalMemory /Value\" output has unexpected " +
          "format: \"" + output + "\"", e);
      }
    } catch (InterruptedException e) {
      throw new IOException(e);
    }
  }

  public static void checkInterrupted() {
    if (Thread.currentThread().isInterrupted()) throw new InterruptedRuntimeException();
  }

  public static void trimStackTrace(StringBuilder sb, StackTraceElement... stes) {
    int first = trimFirst(stes);
    int last = trimLast(first, stes);
    for (int i = first; i <= last; i++)
      sb.append("\n\tat ").append(stes[i]);
  }

  private static int trimFirst(StackTraceElement[] stes) {
    int first = 0;
    for (; first < stes.length; first++)
      if (!isInternal(stes[first].getClassName()))
        break;
    if (first > 0) first--;
    if (first > 0) first--;
    return first;
  }

  private static int trimLast(int first, StackTraceElement[] stes) {
    int last = stes.length - 1;
    for (; first < last; last--)
      if (!isInternal(stes[last].getClassName()))
        break;
    if (last < stes.length - 1) last++;
    return last;
  }

  public static boolean isInternal(String className) {
    return className.startsWith("jdk.") || className.startsWith("sun.") || className.startsWith("java.");
  }






}
