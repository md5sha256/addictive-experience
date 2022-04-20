package io.github.md5sha256.addictiveexperience;

import io.github.md5sha256.addictiveexperience.api.util.DataKey;
import io.leangen.geantyref.TypeToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestDataKey {

    @Test
    public void testKeyInit() {
        Assertions
                .assertThrows(NullPointerException.class, () -> DataKey.of(null, (Class<?>) null));
        Assertions.assertThrows(NullPointerException.class, () -> DataKey.of(null, String.class));
        Assertions.assertThrows(NullPointerException.class,
                                () -> DataKey.of(null, TypeToken.get(String.class)));
        Assertions
                .assertThrows(NullPointerException.class, () -> DataKey.of("key", (Class<?>) null));
        Assertions.assertThrows(NullPointerException.class,
                                () -> DataKey.of("key", (TypeToken<?>) null));
    }

    @Test
    public void testKeyName() {
        final String expectedName = "name";
        final DataKey<String> key = DataKey.of("name", String.class);
        Assertions.assertEquals(expectedName, key.name());
        final String expectedToString = String.format("DataKeyImpl{name='%s', type=%s}", expectedName, String.class.getName());
        Assertions.assertEquals(expectedToString, key.toString());
    }

    @Test
    public void testKeyType() {
        final Class<String> stringClass = String.class;
        final TypeToken<String> typeToken = TypeToken.get(String.class);
        final DataKey<String> keyWithClass = DataKey.of("name", stringClass);
        Assertions.assertEquals(stringClass, keyWithClass.type().getType());
        final DataKey<String> keyWithTypeToken = DataKey.of("name", typeToken);
        Assertions.assertEquals(typeToken, keyWithTypeToken.type());
        Assertions.assertEquals(keyWithClass, keyWithTypeToken);
        Assertions.assertEquals(keyWithClass.hashCode(), keyWithTypeToken.hashCode());
    }

}
