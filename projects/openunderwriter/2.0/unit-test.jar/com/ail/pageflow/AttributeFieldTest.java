package com.ail.pageflow;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.ail.core.Attribute;

public class AttributeFieldTest {

    private AttributeField sut;
    @Mock
    private Attribute attribute;
    
    @Before
    public void setup() {
        initMocks(this);
        sut =new AttributeField();
    }
    
    @Test
    public void testSlaveIsBlankForNonMaster() {
        doReturn(false).when(attribute).isChoiceMasterType();
        assertEquals("", sut.getSlavesBinding(attribute));
    }
    
    @Test
    public void testSlaveIsDerivedForMaster() {
        sut.setBinding("/attribute[id='master']");
        doReturn("master").when(attribute).getId();
        doReturn(true).when(attribute).isChoiceMasterType();
        doReturn("slave").when(attribute).getChoiceSlave();
        
        assertEquals("/attribute[id='slave']", sut.getSlavesBinding(attribute));
    }

    @Test
    public void testMasterIsBlankForNonSlave() {
        doReturn(false).when(attribute).isChoiceSlaveType();
        assertEquals("", sut.getMastersBinding(attribute));
    }
    
    @Test
    public void testMasterIsDerivedForSlave() {
        sut.setBinding("/attribute[id='slave']");
        doReturn("slave").when(attribute).getId();
        doReturn(true).when(attribute).isChoiceSlaveType();
        doReturn("master").when(attribute).getChoiceMaster();
        
        assertEquals("/attribute[id='master']", sut.getMastersBinding(attribute));
    }

}
