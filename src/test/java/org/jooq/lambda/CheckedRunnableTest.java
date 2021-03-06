/**
 * Copyright (c) 2014, Data Geekery GmbH, contact@datageekery.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.jooq.lambda;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Lukas Eder
 */
public class CheckedRunnableTest {

    @Test
    public void testCheckedRunnable() {
        Runnable test = Unchecked.runnable(
            () -> {
                throw new Exception("runnable");
            }
        );

        assertRunnable(test, RuntimeException.class);
    }

    @Test
    public void testCheckedRunnableWithCustomHandler() {
        Runnable test = Unchecked.runnable(
            () -> {
                throw new Exception("runnable");
            },
            e -> {
                throw new IllegalStateException(e);
            }
        );

        assertRunnable(test, IllegalStateException.class);
    }

    private <E extends RuntimeException> void assertRunnable(Runnable test, Class<E> type) {
        assertNotNull(test);
        try {
            test.run();
            fail();
        }
        catch (RuntimeException e) {
            assertException(type, e, "runnable");
        }
    }

    private <E extends RuntimeException> void assertException(Class<E> type, RuntimeException e, String message) {
        assertEquals(type, e.getClass());
        assertEquals(Exception.class, e.getCause().getClass());
        assertEquals(message, e.getCause().getMessage());
    }
}
