package com.sachinchandil.indie.util.generalisedClasses;

public class AlertButton
{
    String text = null;
    int onClick = -1;

    public AlertButton(String text, int onClick)
    {
        this.text = text;
        if (onClick != -1)
            this.onClick = onClick;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public int getOnClick()
    {
        return onClick;
    }

    public void setOnClick(int onClick)
    {
        this.onClick = onClick;
    }

}