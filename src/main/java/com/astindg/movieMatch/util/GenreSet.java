package com.astindg.movieMatch.util;

import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.type.descriptor.sql.VarcharTypeDescriptor;
import org.hibernate.usertype.DynamicParameterizedType;

import java.util.EnumSet;
import java.util.Properties;

public class GenreSet extends AbstractSingleColumnStandardBasicType<EnumSet> implements DynamicParameterizedType {

    public GenreSet() {
        super(VarcharTypeDescriptor.INSTANCE, null);
    }

    @Override
    public String getName() {
        return "genre-set";
    }

    @Override
    public String[] getRegistrationKeys() {
        return new String[] { getName(), "EnumSet", EnumSet.class.getName() };
    }

    @Override
    public void setParameterValues(Properties parameters) {
        setJavaTypeDescriptor(new GenreSetDescriptor());
    }
}
