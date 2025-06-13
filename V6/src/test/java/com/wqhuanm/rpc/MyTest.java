package com.wqhuanm.rpc;

import com.wqhuanm.rpc.pojo.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

public class MyTest
{

    @ParameterizedTest
    @ValueSource(longs = {233,666})
    public void test() throws Exception{

    }

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService();
    }

    // --- 使用 @MethodSource 提供 User 和 long 类型的参数 ---
    @ParameterizedTest
    @MethodSource("provideAuthCookieTestData")
    @DisplayName("测试getAuthCookie方法，包含有效和无效的用户和时间")
    void testGetAuthCookie(User user, long time, String expectedResult) {
        String actualCookie = authService.getAuthCookie(user, time);
        assertEquals(expectedResult, actualCookie,
                () -> "测试失败：User=" + user + ", Time=" + time);
    }

    // 静态方法，提供测试数据
    // 必须返回 Stream<Arguments>，Arguments.of() 用于包装多个参数
    private static Stream<Arguments> provideAuthCookieTestData() {
        return Stream.of(
                // 有效用户和时间
                Arguments.of(new User("testUser", "pass123"), 3600L, "auth_cookie_testUser_3600"),
                Arguments.of(new User("anotherUser", "xyz"), 1800L, "auth_cookie_anotherUser_1800"),

                // 无效时间
                Arguments.of(new User("validUser", "abc"), 0L, "ERROR_INVALID_TIME"),
                Arguments.of(new User("validUser", "abc"), -100L, "ERROR_INVALID_TIME"),

                // 无效用户 - null user对象
                Arguments.of(null, 3600L, "ERROR_INVALID_USER"),
                // 无效用户 - null username
                Arguments.of(new User(null, "pass"), 3600L, "ERROR_INVALID_USER"),
                // 无效用户 - empty username
                Arguments.of(new User("", "pass"), 3600L, "ERROR_INVALID_USER")
        );
    }

    // --- 结合 @NullSource 测试 null user 参数 (如果业务逻辑允许) ---
    @ParameterizedTest
    @NullSource // 提供一个 null 参数给 user
    @ValueSource(longs = {1000L, 5000L}) // 提供 time 参数
    @DisplayName("测试getAuthCookie方法，当user为null时")
    // 注意：这里需要两个参数，但 @NullSource 只提供第一个，所以需要结合其他方式
    // 更常见的做法是将所有参数都放在 @MethodSource 中
    // 这个例子只是为了演示 @NullSource 的用法，在实际多参数场景下，@MethodSource 更合适
    void testGetAuthCookieWithNullUser(User user, long time) {
        // 在这里，user 确实是 null
        // time 来自 @ValueSource
        String actualCookie = authService.getAuthCookie(user, time);
        assertEquals("ERROR_INVALID_USER", actualCookie);
    }


    // --- 另一个 @MethodSource 例子，如果测试场景更聚焦于某一类输入 ---
    @ParameterizedTest
    @MethodSource("provideValidUsers")
    @ValueSource(longs = {1000L, 2000L})
    @DisplayName("测试getAuthCookie方法，使用有效用户和不同时间")
    void testGetAuthCookieWithValidUsers(User user, long time) {
        String expectedCookie = "auth_cookie_" + user.getUsername() + "_" + time;
        String actualCookie = authService.getAuthCookie(user, time);
        assertEquals(expectedCookie, actualCookie);
    }

    private static Stream<User> provideValidUsers() {
        return Stream.of(
                new User("userA", "pwdA"),
                new User("userB", "pwdB")
        );
    }
    // 注意：这里的 @ValueSource 只能提供 time 参数。如果 user 和 time 都来自 MethodSource，需要像上面的 provideAuthCookieTestData 一样包装 Arguments
    // JUnit 5 的参数解析器会自动尝试匹配参数来源。如果多个来源，会尝试组合，但有时会很复杂。
    // 最清晰的方式还是一个 @MethodSource 提供所有参数。
}
