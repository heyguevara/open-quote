package com.ail.configurehook;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.ail.core.CoreProxy;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;
import com.liferay.portlet.documentlibrary.model.DLFolder;

public class ListenerTest {
    private static final String TEST_FOLDER = "/Path/To/The/TestFolder";
    private static final String TEST_FILE = "TestFile";
    private Listener sut;
    private DLFileEntry fileEntry;
    private CoreProxy mockCoreProxy;

    @Before
    public void setupSUT() {
        mockCoreProxy = mock(CoreProxy.class);
        sut = new Listener(mockCoreProxy);
    }

    @Before
    public void setupMocks() throws PortalException, SystemException {
        DLFolder folder = null;

        fileEntry = mock(DLFileEntry.class);
        when(fileEntry.getTitle()).thenReturn(TEST_FILE);

        // convert TEST_FOLDER into a DLFolder hierarchy
        String[] folderNames = TEST_FOLDER.split("/");
        for (int i = folderNames.length - 1; i > 0; i--) {
            DLFolder newFolder = mock(DLFolder.class);
            if (folder == null) {
                when(fileEntry.getFolder()).thenReturn(newFolder);
            } else {
                when(folder.getParentFolder()).thenReturn(newFolder);
            }

            folder = newFolder;

            when(folder.getName()).thenReturn(folderNames[i]);
        }
    }

    @Test
    public void testFileEntry2FullPath() throws Exception {
        assertNull(sut.fileEntry2FullPath(null));
        assertNotNull(sut.fileEntry2FullPath(fileEntry));
        assertEquals(TEST_FOLDER + "/" + TEST_FILE, sut.fileEntry2FullPath(fileEntry));
    }

    @Test
    public void testFullPath2ProductName() {
        assertNull(sut.fullPath2ProductName(null));
        assertNotNull(sut.fullPath2ProductName("/Product/Path"));
        assertEquals("AIL.Test.Product", sut.fullPath2ProductName("/Product/AIL/Test/Product/Registry.xml"));
    }
}
