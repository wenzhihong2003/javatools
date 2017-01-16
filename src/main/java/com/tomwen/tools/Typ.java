package com.tomwen.tools;

import java.io.File;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 用于java的类型信息
 * Created by tomwen on 2017/1/16.
 */
public class Typ {
  /* Core */
  public static final Class<Object> object = Object.class;
  public static final Class<String> string = String.class;
  public static final Class<List> list = List.class;
  public static final Class<CharSequence> chars = CharSequence.class;
  public static final Class<Set> set = Set.class;
  public static final Class<Collection> collection = Collection.class;


  public static final Class<Comparable> comparable = Comparable.class;
  /* Wrapper */
  public static final Class<Boolean> bool = Boolean.class;
  public static final Class<Integer> integer = Integer.class;
  public static final Class<Long> longWrapper = Long.class;
  public static final Class<Double> doubleWrapper = Double.class;
  public static final Class<Float> floatWrapper = Float.class;
  public static final Class<Byte> byteWrapper = Byte.class;
  public static final Class<Short> shortWrapper = Short.class;
  public static final Class<BigInteger> bigInteger = BigInteger.class;
  public static final Class<BigDecimal> bigDecimal = BigDecimal.class;

  public static final Class<Number> number = Number.class;


  /* primitive */
  public static final Class<?> flt = float.class;
  public static final Class<?> lng = long.class;
  public static final Class<?> dbl = double.class;
  public static final Class<?> intgr = int.class;
  public static final Class<?> bln = boolean.class;
  public static final Class<?> shrt = short.class;
  public static final Class<?> chr = char.class;
  public static final Class<?> bt = byte.class;


  /* Utility */
  public static final Class<Date> date = Date.class;
  public static final Class<Calendar> calendar = Calendar.class;
  public static final Class<File> file = File.class;
  public static final Class<Path> path = Path.class;


  /* Arry. */
  public static final Class<String[]> stringArray = String[].class;
  public static final Class<int[]> intArray = int[].class;
  public static final Class<byte[]> byteArray = byte[].class;
  public static final Class<short[]> shortArray = short[].class;
  public static final Class<char[]> charArray = char[].class;
  public static final Class<long[]> longArray = long[].class;
  public static final Class<float[]> floatArray = float[].class;
  public static final Class<double[]> doubleArray = double[].class;
  public static final Class<Object[]> objectArray = Object[].class;

  public static boolean doesMapHaveKeyTypeString( Object value ) {
    return getKeyType( (Map<?, ?>) value ) == string;
  }

  public static boolean isBasicType( Object value ) {
    return ( value instanceof Number || value instanceof CharSequence
      || value instanceof Date || value instanceof Calendar || value instanceof Currency
      || value instanceof Boolean );
  }


  public static boolean isPrimitiveOrWrapper( Class<?> theClass ) {
    return ( number.isAssignableFrom( theClass )
      || date.isAssignableFrom( theClass )
      || calendar.isAssignableFrom( theClass )
      || bool.isAssignableFrom( theClass )
      || theClass.isPrimitive() );
  }

  public static boolean isBasicType( Class<?> theClass ) {
    return ( number.isAssignableFrom( theClass )
      || chars.isAssignableFrom( theClass )
      || date.isAssignableFrom( theClass )
      || calendar.isAssignableFrom( theClass )
      || bool.isAssignableFrom( theClass )
      || theClass.isPrimitive() );
  }

  public static boolean isMap( Class<?> thisType ) {
    return isSuperType( thisType, Map.class );
  }

  public static boolean isCharSequence( Class<?> thisType ) {
    return isSuperType( thisType, CharSequence.class );
  }

  public static boolean isCollection( Class<?> thisType ) {
    return isSuperType( thisType, Collection.class );
  }

  public static boolean isList( Class<?> thisType ) {
    return isSuperType( thisType, List.class );
  }

  public static boolean isSet( Class<?> thisType ) {
    return isSuperType( thisType, Set.class );
  }

  public static boolean isSortedSet( Class<?> thisType ) {
    return isSuperType( thisType, SortedSet.class );
  }

  public static boolean isType( Class<?> thisType, Class<?> isThisType ) {
    return isSuperType( thisType, isThisType );
  }

  public static boolean isComparable( Object o ) {
    return o instanceof Comparable;
  }

  public static boolean isComparable( Class<?> type ) {
    return implementsInterface( type, comparable );
  }

  public static boolean isSuperClass( Class<?> type, Class<?> possibleSuperType ) {
    if ( possibleSuperType.isInterface() ) {
      return false;
    } else {
      return possibleSuperType.isAssignableFrom( type );
    }

  }

  public static boolean isSuperType( Class<?> type, Class<?> possibleSuperType ) {
    return possibleSuperType.isAssignableFrom( type );
  }

  public static boolean implementsInterface( Class<?> type, Class<?> interfaceType ) {
    if ( !interfaceType.isInterface() ) {
      return false;
    } else {
      return interfaceType.isAssignableFrom( type );
    }

  }

  public static Class<?> getKeyType( Map<?, ?> value ) {
    if ( value.size() > 0 ) {
      return value.keySet().iterator().next().getClass();
    } else {
      return Object.class;
    }
  }

  public static Class<?> getComponentType( Collection value ) {
    if ( value.size() > 0 ) {
      return value.iterator().next().getClass();
    } else {
      return Object.class;
    }
  }

  public static <T> boolean isAbstract ( Class<T> clazz ) {
    return Modifier.isAbstract ( clazz.getModifiers () );
  }


  private static Set<Class> basicTypeOrCollection = new CopyOnWriteArraySet<>();
  static {
    basicTypeOrCollection.add(int.class);
    basicTypeOrCollection.add(float.class);
    basicTypeOrCollection.add(short.class);
    basicTypeOrCollection.add(char.class);
    basicTypeOrCollection.add(byte.class);
    basicTypeOrCollection.add(double.class);
    basicTypeOrCollection.add(long.class);
    basicTypeOrCollection.add(Long.class);
    basicTypeOrCollection.add(List.class);
    basicTypeOrCollection.add(Set.class);
    basicTypeOrCollection.add(Map.class);
    basicTypeOrCollection.add(String.class);
    basicTypeOrCollection.add(StringBuilder.class);
    basicTypeOrCollection.add(Integer.class);
    basicTypeOrCollection.add(Float.class);
    basicTypeOrCollection.add(Double.class);
    basicTypeOrCollection.add(Short.class);
    basicTypeOrCollection.add(Byte.class);
    basicTypeOrCollection.add(Character.class);
    basicTypeOrCollection.add(BigInteger.class);
    basicTypeOrCollection.add(BigDecimal.class);
    basicTypeOrCollection.add(boolean.class);
    basicTypeOrCollection.add(Boolean.class);
  }

  private static Set<Class> primitiveNumber = new CopyOnWriteArraySet<>();
  static {
    primitiveNumber.add(int.class);
    primitiveNumber.add(float.class);
    primitiveNumber.add(short.class);
    primitiveNumber.add(char.class);
    primitiveNumber.add(byte.class);
    primitiveNumber.add(double.class);
    primitiveNumber.add(long.class);
  }

  public static boolean isBasicTypeOrCollection( Class<?> type ) {
    return basicTypeOrCollection.contains( type );
  }

  public static boolean isPrimitiveNumber(Class<?> arg1) {
    return primitiveNumber.contains(arg1);
  }

  public static boolean isArray(Object obj) {
    if ( obj == null ) return false;
    return obj.getClass().isArray();
  }

  public static boolean isStringArray(Object obj) {
    if ( obj == null ) return false;
    return obj.getClass().isArray() && obj.getClass().getComponentType()==String.class;
  }

  public static boolean isEnum(Class<?> componentType) {

    return componentType.isEnum();
  }
}
