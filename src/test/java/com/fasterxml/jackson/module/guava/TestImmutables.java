package com.fasterxml.jackson.module.guava;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;

/**
 * Unit tests for verifying that various immutable types
 * (like {@link ImmutableList}, {@link ImmutableMap} and {@link ImmutableSet})
 * work as expected.
 * 
 * @author tsaloranta
 */
public class TestImmutables extends BaseTest
{
    /*
    /**********************************************************************
    /* Unit tests for verifying handling in absence of module registration
    /**********************************************************************
     */
    
    /**
     * Immutable types can actually be serialized as regular collections, without
     * problems.
     */
    public void testWithoutSerializers() throws Exception
    {                
        final ObjectMapper mapper = new ObjectMapper();
        
        final List<Integer> list = ImmutableList.of(1, 2, 3);
        assertEquals("[1,2,3]", mapper.writeValueAsString(list));

        final Set<String> set = ImmutableSet.of("abc", "def");        
        assertEquals("[\"abc\",\"def\"]", mapper.writeValueAsString(set));

        final Map<String,Integer> map = ImmutableMap.of("a", 1, "b", 2);        
        assertEquals("{\"a\":1,\"b\":2}", mapper.writeValueAsString(map));
    }

    /**
     * Deserialization will fail, however.
     */
    public void testWithoutDeserializers() throws Exception
    {
        final ObjectMapper mapper = new ObjectMapper();

        try {
            mapper.readValue("[1,2,3]", new TypeReference<ImmutableList<Integer>>() { });
            fail("Expected failure for missing deserializer");
        } catch (JsonMappingException e) {
            verifyException(e, "can not find a deserializer");
        }

        try {
            mapper.readValue("[1,2,3]", new TypeReference<ImmutableSet<Integer>>() { });
            fail("Expected failure for missing deserializer");
        } catch (JsonMappingException e) {
            verifyException(e, "can not find a deserializer");
        }

        try {
            mapper.readValue("[1,2,3]", new TypeReference<ImmutableSortedSet<Integer>>() { });
            fail("Expected failure for missing deserializer");
        } catch (JsonMappingException e) {
            verifyException(e, "can not find a deserializer");
        }
        
        try {
            mapper.readValue("{\"a\":true,\"b\":false}", new TypeReference<ImmutableMap<Integer,Boolean>>() { });
            fail("Expected failure for missing deserializer");
        } catch (JsonMappingException e) {
            verifyException(e, "can not find a deserializer");
        }
    }
        
    /*
    /**********************************************************************
    /* Unit tests for actual registered module
    /**********************************************************************
     */

    public void testImmutableList() throws Exception
    {
        List<Integer> list = mapperWithModule().readValue("[1,2,3]", new TypeReference<ImmutableList<Integer>>() { });        
        assertEquals(3, list.size());
        assertEquals(Integer.valueOf(1), list.get(0));
        assertEquals(Integer.valueOf(2), list.get(1));
        assertEquals(Integer.valueOf(3), list.get(2));
    }

    public void testImmutableSet() throws Exception
    {
        Set<Integer> set = mapperWithModule().readValue("[3,7,8]", new TypeReference<ImmutableSet<Integer>>() { });
        assertEquals(3, set.size());
        Iterator<Integer> it = set.iterator();
        assertEquals(Integer.valueOf(3), it.next());
        assertEquals(Integer.valueOf(7), it.next());
        assertEquals(Integer.valueOf(8), it.next());
    }

    public void testImmutableSortedSet() throws Exception
    {
        SortedSet<Integer> set = mapperWithModule().readValue("[5,1,2]", new TypeReference<ImmutableSortedSet<Integer>>() { });
        assertEquals(3, set.size());
        Iterator<Integer> it = set.iterator();
        assertEquals(Integer.valueOf(1), it.next());
        assertEquals(Integer.valueOf(2), it.next());
        assertEquals(Integer.valueOf(5), it.next());
    }
    
    public void testImmutableMap() throws Exception
    {
        Map<Integer,Boolean> map = mapperWithModule().readValue("{\"12\":true,\"4\":false}", new TypeReference<ImmutableMap<Integer,Boolean>>() { });
        assertEquals(2, map.size());
        assertEquals(Boolean.TRUE, map.get(Integer.valueOf(12)));
        assertEquals(Boolean.FALSE, map.get(Integer.valueOf(4)));
    }
    
}
