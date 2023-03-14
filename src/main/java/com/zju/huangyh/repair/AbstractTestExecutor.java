package com.zju.huangyh.repair;

/**
 * TestExecutor specifies the interface to test the program under repair.
 * <p>
 * Since different programs have different builds, there are different concrete
 * classes (e.g., AntTestExecutor, BashTestExecutor, etc.).
 *
 * @ClassName AbstractTestExecutor
 * @Author huangyaohua
 */
public abstract class AbstractTestExecutor {

    /**
     * Runs the the test cases and returns the status.
     *
     * @return NOT_COMPILED = failed to compile, TESTS_FAILED = failed one or more test cases, TESTS_PASSED = passed all test cases
     * @throws Exception
     */
    public abstract Status runTests() throws Exception;

    /**
     * The possible results from runTests()
     */
    public enum Status {
        FAILED, PASSED, ERROR
    }

}
