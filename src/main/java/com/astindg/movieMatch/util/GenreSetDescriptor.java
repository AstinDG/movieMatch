package com.astindg.movieMatch.util;

import com.astindg.movieMatch.model.Genre;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.AbstractTypeDescriptor;

import java.util.*;
import java.util.stream.Collectors;

public class GenreSetDescriptor extends AbstractTypeDescriptor<EnumSet> {
    private static final String SEPARATOR = ",";

    protected GenreSetDescriptor() {
        super(EnumSet.class);
    }

    @Override
    public String toString(EnumSet value) {
        StringJoiner joiner = new StringJoiner(SEPARATOR);
        for(Object v : value){
            joiner.add(v.toString());
        }
        return joiner.toString();
    }

    @Override
    public EnumSet fromString(String string) {
        if (StringUtils.isEmpty(string))
            return null;

        List<Enum> list = Arrays.stream(StringUtils.split(string, SEPARATOR))
                .map(Genre::valueOf)
                .collect(Collectors.toList());

        if (list.isEmpty())
            return null;

        return EnumSet.copyOf(list);
    }

    @Override
    public int extractHashCode(EnumSet value) {
        return Objects.hashCode(value);
    }

    @Override
    public <X> X unwrap(EnumSet value, Class<X> type, WrapperOptions options) {
        if (value == null)
            return null;

        if (EnumSet.class.isAssignableFrom(type))
            return (X) value;

        if (String.class.isAssignableFrom(type))
            return (X) toString(value);

        throw unknownUnwrap(type);
    }

    @Override
    public <X> EnumSet wrap(X value, WrapperOptions options) {
        if (value == null)
            return null;

        if (value instanceof EnumSet)
            return (EnumSet) value;

        if (value instanceof String)
            return fromString((String) value);

        throw unknownWrap(value.getClass());
    }
}
