package com.tomwen.tools;

/**
 * 用于java的类型信息
 * Created by tomwen on 2017/1/16.
 */
public class Typ {
  public static boolean isEnum(Class<?> componentType) {
    return componentType.isEnum();
  }
}
