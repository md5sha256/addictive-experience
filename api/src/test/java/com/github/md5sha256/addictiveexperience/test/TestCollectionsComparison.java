package com.github.md5sha256.addictiveexperience.test;

import com.github.md5sha256.addictiveexperience.api.util.CollectionComparison;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class TestCollectionsComparison {

    @Test
    public void testComparison() {
        final Set<String> set1 = Set.of("abc", "def");
        final Set<String> set2 = Set.of("def", "abc");
        Assertions.assertTrue(CollectionComparison.haveIdenticalElements(set1, set2));
        final Set<String> set3 = Set.of("abc", "def", "hjk");
        Assertions.assertFalse(CollectionComparison.haveIdenticalElements(set1, set3));
        Assertions.assertTrue(CollectionComparison.haveIdenticalElements(Collections.emptySet(), new HashSet<>()));
    }

}
