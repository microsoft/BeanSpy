/**
 * Copyright (c) Microsoft Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use 
 * this file except in compliance with the License. You may obtain a copy of the 
 * License at http://www.apache.org/licenses/LICENSE-2.0.
 *  
 * THIS CODE IS PROVIDED *AS IS* BASIS, WITHOUT WARRANTIES OR CONDITIONS 
 * OF ANY KIND, EITHER EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION 
 * ANY IMPLIED WARRANTIES OR CONDITIONS OF TITLE, FITNESS FOR A PARTICULAR PURPOSE, 
 * MERCHANTABLITY OR NON-INFRINGEMENT. 
 *
 * See the Apache Version 2.0 License for specific language governing 
 * permissions and limitations under the License.
 */

package com.interopbridges.scx.mbeans;

/**
 * <p>
 * Simple Class that is used by ComplexType MBean. The primary intention of this
 * class is a simple class that contains basic types. This class is used
 * in the unit tests for verifying that classes can be used from
 * within MBeans and can be represented as XML.
 * </p>
 * 
 * @author Geoff Erasmus
 * 
 */

public class ComplexClass 
{
    /**
     * the serialVersionUID property needed for distinguishing this class.
     * It is returned in the Class.toString method to create a unique class instance.
     */
    private static final long serialVersionUID = 999999999999999999L;
    
    /**
     * A primitive private Integer property needed for XML conversion testing
     */
    private Integer   _clsInteger = new Integer(Integer.MAX_VALUE);
    
    /**
     * A primitive private Short property needed for XML conversion testing
     */
    private Short     _clsShort   = new Short(Short.MAX_VALUE);
    
    /**
     * A primitive private Long property needed for XML conversion testing
     */
    private Long      _clsLong    = new Long(Long.MAX_VALUE);
    
    /**
     * A primitive private Boolean property needed for XML conversion testing
     */
    private Boolean   _clsBoolean = new Boolean(true);
    
    /**
     * A primitive private Byte property needed for XML conversion testing
     */
    private Byte      _clsByte    = new Byte(Byte.MAX_VALUE);
    
    /**
     * A primitive private Char property needed for XML conversion testing
     */
    private Character _clsChar    = new Character('z');
    
    /**
     * A primitive private Float property needed for XML conversion testing
     */
    private Float     _clsFloat   = new Float(Float.MAX_VALUE);
    
    /**
     * A primitive private Double property needed for XML conversion testing
     */
    private Double    _clsDouble  = new Double(Double.MAX_VALUE);
    
    /**
     * A primitive private String property needed for XML conversion testing
     */
    private String    _clsString  = new String("Some String");
    
    /**
     * A primitive private inner class property needed for XML conversion testing
     */
    private anonInnerClass _clsClass  = new anonInnerClass();
    
    /**
     * A primitive public int property needed for XML conversion testing
     */
    public int       aInt       = Integer.MAX_VALUE;
    
    /**
     * A primitive public short property needed for XML conversion testing
     */
    public short     aShort     = Short.MAX_VALUE;
    
    /**
     * A primitive public long property needed for XML conversion testing
     */
    public long      aLong      = Long.MAX_VALUE;
    
    /**
     * A primitive public boolean property needed for XML conversion testing
     */
    public boolean   aBoolean   = true;
    
    /**
     * A primitive public byte property needed for XML conversion testing
     */
    public byte      aByte      = Byte.MAX_VALUE;
    
    /**
     * A primitive public char property needed for XML conversion testing
     */
    public char      aChar      = 'x';
    
    /**
     * A primitive public float property needed for XML conversion testing
     */
    public float     aFloat     = Float.MAX_VALUE;
    
    /**
     * A primitive public double property needed for XML conversion testing
     */
    public double    aDouble    = Double.MAX_VALUE;

    /**
     * A reference to a child class needed for XML conversion testing
     */
    private ComplexRecursiveClass childClass = null;
    
    /**
     * Empty Constructor. It is considered to be a best practice to create this
     * default constructor rather than relying on the compiler to auto-generate
     * it.
     */
    public ComplexClass() 
    {
        childClass = new ComplexRecursiveClass(this);
    }

    /**
     * <p>
     * Get Property Method for a child class
     * </p>
     * 
     * @return A child class for XML conversion testing
     */
    public ComplexRecursiveClass getChildClass() 
    {  
        return childClass;    
    }
    
    /**
     * <p>
     * Get Property Method for an Integer type
     * </p>
     * 
     * @return An Integer wrapper class type for XML conversion testing
     */
    public Integer   getInteger()   
    {  
        return _clsInteger;  
    }

    /**
     * <p>
     * Get Property Method for a Short type
     * </p>
     * 
     * @return A Short wrapper class type for XML conversion testing
     */
    public Short     getShort()     
    {  
        return _clsShort;    
    }

    /**
     * <p>
     * Get Property Method for a Long type
     * </p>
     * 
     * @return A Long wrapper class type for XML conversion testing
     */
    public Long      getLong()      
    {  
        return _clsLong;         
    }

    /**
     * <p>
     * Get Property Method for a Boolean type
     * </p>
     * 
     * @return A Boolean wrapper class type for XML conversion testing
     */
    public Boolean   getBoolean()   
    {  
        return _clsBoolean;  
    }

    /**
     * <p>
     * Get Property Method for a Byte type
     * </p>
     * 
     * @return A Byte wrapper class type for XML conversion testing
     */
    public Byte      getByte()      
    {  
        return _clsByte;         
    }

    /**
     * <p>
     * Get Property Method for a Character type
     * </p>
     * 
     * @return A Character wrapper class type for XML conversion testing
     */
    public Character getCharacter() 
    {  
        return _clsChar;      
    }

    /**
     * <p>
     * Get Property Method for a Float type
     * </p>
     * 
     * @return A Float wrapper class type for XML conversion testing
     */
    public Float     getFloat()     
    {  
        return _clsFloat;    
    }

    /**
     * <p>
     * Get Property Method for a Double type
     * </p>
     * 
     * @return A Double wrapper class type for XML conversion testing
     */
    public Double    getDouble()    
    {  
        return _clsDouble;   
    }

    /**
     * <p>
     * Get Property Method for a String type
     * </p>
     * 
     * @return A String class type for XML conversion testing
     */
    public String    getString()    
    {  
        return _clsString;       
    }

    /**
     * <p>
     * Get Property Method for a Inner Class
     * </p>
     * 
     * @return A InnerClass for XML conversion testing
     */
    public anonInnerClass  getMyClass() 
    {  
        return _clsClass;    
    }
    

    /**
     * <p>
     * Get Property Method for a int type
     * </p>
     * 
     * @return An int type for XML conversion testing
     */
    public int       get_int()      
    {  
        return aInt;         
    }

    /**
     * <p>
     * Get Property Method for a short type
     * </p>
     * 
     * @return A short type for XML conversion testing
     */
    public short     get_short()    
    {  
        return aShort;   
    }

    /**
     * <p>
     * Get Property Method for a long type
     * </p>
     * 
     * @return A long type for XML conversion testing
     */
    public long      get_long()     
    {  
        return aLong;     
    }

    /**
     * <p>
     * Get Property Method for a boolean type
     * </p>
     * 
     * @return A boolean type for XML conversion testing
     */
    public boolean   get_boolean()  
    {  
        return aBoolean;     
    }

    /**
     * <p>
     * Get Property Method for an byte type
     * </p>
     * 
     * @return A byte type for XML conversion testing
     */
    public byte      get_byte()     
    {  
        return aByte;     
    }

    /**
     * <p>
     * Get Property Method for an char type
     * </p>
     * 
     * @return A char type for XML conversion testing
     */
    public char      get_character()
    {  
        return aChar;     
    }

    /**
     * <p>
     * Get Property Method for an Float type
     * </p>
     * 
     * @return A float type for XML conversion testing
     */
    public float     get_float()    
    {  
        return aFloat;   
    }

    /**
     * <p>
     * Get Property Method for an double type
     * </p>
     * 
     * @return A double type for XML conversion testing
     */
    public double    get_double()   
    {  
        return aDouble;  
    }
    
    
/**
 * <p>
 * Simple inner class that is used by ComplexClass class. The primary intention of this
 * class is a simple class that is contained within a more complex class. This class is used
 * in the unit tests for verifying that classes can be used from
 * within MBeans and can be represented as XML.
 * As this specific class is an inner class it will not be transformed into XML.
 * </p>
 * 
*/  
    class anonInnerClass
    {
        /**
         * A primitive Integer property needed for XML conversion testing
         */
        protected Integer icInteger = Integer.MAX_VALUE;
        
        /**
         * Empty Constructor. It is considered to be a best practice to create this
         * default constructor rather than relying on the compiler to auto-generate
         * it.
         */
        public anonInnerClass() 
        {
        }
        
        /**
         * <p>
         * Get Property Method for an Integer type
         * </p>
         * 
         * @return An Integer type for XML conversion testing
         */
        public Integer getInteger() 
        { 
            return icInteger; 
        }
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    public String toString()    
    {
       StringBuilder result = new StringBuilder();
        result.append("serialVersionUID ").append(serialVersionUID);
        return result.toString();

    }
}
