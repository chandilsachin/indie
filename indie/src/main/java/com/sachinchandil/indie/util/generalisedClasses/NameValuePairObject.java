package com.sachinchandil.indie.util.generalisedClasses;


public class NameValuePairObject
{

    private String Name;
    private String Value;

    public NameValuePairObject(String Name, String Value)
    {
        this.Name = Name;
        this.Value = Value;
    }

    public String getName()
    {
        return Name;
    }

    void setName(String Name)
    {
        this.Name = Name;
    }

    public String getValue()
    {
        return Value;
    }

    void setValue(String Value)
    {
        this.Value = Value;
    }


}
