package me.therimuru.RestAuth;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

public class TestStatusLogger implements TestWatcher {
    @Override
    public void testSuccessful(ExtensionContext context) {
        System.out.println("\033[0;32mTest successed: " + context.getDisplayName() + "\033[0m");
    }
}
