package com.example.tolek.player.Util.jaudiotagger.audio.real;

import com.example.tolek.player.Util.jaudiotagger.audio.generic.GenericTag;
import com.example.tolek.player.Util.jaudiotagger.tag.FieldDataInvalidException;
import com.example.tolek.player.Util.jaudiotagger.tag.FieldKey;
import com.example.tolek.player.Util.jaudiotagger.tag.KeyNotFoundException;
import com.example.tolek.player.Util.jaudiotagger.tag.TagField;

public class RealTag extends GenericTag
{
    public String toString()
    {
        String output = "REAL " + super.toString();
        return output;
    }

    public TagField createCompilationField(boolean value) throws KeyNotFoundException, FieldDataInvalidException
    {
        return createField(FieldKey.IS_COMPILATION,String.valueOf(value));
    }
}
