package com.viettel.im.ws.unittest;

import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.junit.Test;

/**
 * Created by hoangnt14 on 12/15/2015.
 */
public class JunitTest {
    @Test
    public void testValidate_objectNull() {
        boolean ok = DataUtil.isNullObject(null);
        TestCase.assertEquals(true, ok);

    }
}
