package com.royalbrothers.stepdefinitions;

import com.royalbrothers.utils.TestBase;
import io.cucumber.java.After;
import io.cucumber.java.Before;

public class Hooks {

    @Before
    public void start() {
        TestBase.init();
    }

    @After
    public void stop() {
        TestBase.tearDown();
    }
}