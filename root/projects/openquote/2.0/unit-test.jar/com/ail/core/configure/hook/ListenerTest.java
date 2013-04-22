package com.ail.core.configure.hook;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.ail.core.CoreProxy;
import com.ail.core.configure.hook.Listener;
import com.ail.core.product.ClearProductCacheService.ClearProductCacheCommand;
import com.ail.core.product.ResetProductService.ResetProductCommand;
import com.liferay.portal.ModelListenerException;
import com.liferay.portlet.documentlibrary.NoSuchFileEntryException;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;
import com.liferay.portlet.documentlibrary.model.DLFolder;

public class ListenerTest {
    private Listener sut;
    private CoreProxy mockCoreProxy;
    private ResetProductCommand mockQueuedResetProduct = null;
    private ClearProductCacheCommand mockClearProductCacheCommand = null;

    @SuppressWarnings("unchecked")
    @Before
    public void setupSUT() {
        mockQueuedResetProduct = mock(ResetProductCommand.class);
        mockClearProductCacheCommand = mock(ClearProductCacheCommand.class);

        mockCoreProxy = mock(CoreProxy.class);
        doReturn(mockQueuedResetProduct).when(mockCoreProxy).newCommand(eq("QueuedResetProductCommand"), any(Class.class));
        doReturn(mockClearProductCacheCommand).when(mockCoreProxy).newCommand(eq("QueuedClearProductCacheCommand"), any(Class.class));

        sut = spy(new Listener(mockCoreProxy));
    }

    public DLFileEntry createMockStructure_Product_AIL_TestProduct_TestFile() throws Exception {
        DLFileEntry fileEntry = null;
        DLFolder folder = null;

        fileEntry = mock(DLFileEntry.class);
        doReturn("TestFile").when(fileEntry).getTitle();

        folder = createMockStructure_Product_AIL_TestProduct();
        doReturn(folder).when(fileEntry).getFolder();

        return fileEntry;
    }

    public DLFileEntry createMockStructure_Product_AIL_TestProduct_Registry() throws Exception {
        DLFileEntry fileEntry = null;
        DLFolder folder = null;

        fileEntry = mock(DLFileEntry.class);
        doReturn("Registry.xml").when(fileEntry).getTitle();

        folder = createMockStructure_Product_AIL_TestProduct();
        doReturn(folder).when(fileEntry).getFolder();

        return fileEntry;
    }

    public DLFolder createMockStructure_Product_AIL_TestProduct() throws Exception {
        DLFolder retFolder = null;
        DLFolder folder = null;
        DLFolder parent = null;

        parent = mock(DLFolder.class);
        doReturn("TestProduct").when(parent).getName();
        doReturn(true).when(sut).isFolderAProductRoot(eq(parent));
        doReturn("/Product/AIL/TestProduct").when(parent).getPath();
        folder = parent;
        retFolder = parent;

        parent = mock(DLFolder.class);
        doReturn("AIL").when(parent).getName();
        doReturn(false).when(sut).isFolderAProductRoot(eq(parent));
        doReturn(parent).when(folder).getParentFolder();
        folder = parent;

        parent = mock(DLFolder.class);
        doReturn("Product").when(parent).getName();
        doReturn(false).when(sut).isFolderAProductRoot(eq(parent));
        doReturn(parent).when(folder).getParentFolder();
        folder = parent;

        return retFolder;
    }

    public DLFolder createMockStructure_Product_AIL() throws Exception {
        DLFolder retFolder = null;
        DLFolder folder = null;
        DLFolder parent = null;

        parent = mock(DLFolder.class);
        doReturn("AIL").when(parent).getName();
        doReturn("/Product/AIL").when(parent).getPath();
        doReturn(false).when(sut).isFolderAProductRoot(eq(parent));
        folder = parent;
        retFolder = parent;

        parent = mock(DLFolder.class);
        doReturn(parent).when(folder).getParentFolder();
        doReturn("Product").when(parent).getName();
        doReturn("/Product").when(parent).getPath();
        doReturn(true).when(parent).isRoot();
        doReturn(false).when(sut).isFolderAProductRoot(eq(parent));
        folder = parent;

        return retFolder;
    }

    public DLFolder createMockStructure_Product_AIL_TestProduct_Folder() throws Exception {
        DLFolder retFolder = null;
        DLFolder folder = null;
        DLFolder parent = null;

        parent = mock(DLFolder.class);
        doReturn("Folder").when(parent).getName();
        doReturn("/Product/AIL/TestProduct/Folder").when(parent).getPath();
        doReturn(false).when(sut).isFolderAProductRoot(eq(parent));
        retFolder = parent;
        folder = parent;

        parent = mock(DLFolder.class);
        doReturn("TestProduct").when(parent).getName();
        doReturn("/Product/AIL/TestProduct").when(parent).getPath();
        doReturn(true).when(sut).isFolderAProductRoot(eq(parent));
        doReturn(parent).when(folder).getParentFolder();
        folder = parent;

        parent = mock(DLFolder.class);
        doReturn("AIL").when(parent).getName();
        doReturn("/Product/AIL").when(parent).getPath();
        doReturn(false).when(sut).isFolderAProductRoot(eq(parent));
        doReturn(parent).when(folder).getParentFolder();
        folder = parent;

        parent = mock(DLFolder.class);
        doReturn("Product").when(parent).getName();
        doReturn("/Product").when(parent).getPath();
        doReturn(false).when(sut).isFolderAProductRoot(eq(parent));
        doReturn(parent).when(folder).getParentFolder();
        folder = parent;

        return retFolder;
    }

    @Test
    public void testOnAfterRemoveClearsCache() throws Exception {
        DLFileEntry mockFileEntry = createMockStructure_Product_AIL_TestProduct_TestFile();
        doReturn(true).when(sut).isChanged(mockFileEntry);
        sut.onAfterRemove(mockFileEntry);
        verify(mockClearProductCacheCommand, times(1)).invoke();
        verify(mockQueuedResetProduct, never()).invoke();
    }

    @Test
    public void testOnAfterUpdateClearsCacheAndResetsProduct() throws Exception {
        DLFileEntry mockFileEntry = createMockStructure_Product_AIL_TestProduct_Registry();
        doReturn(true).when(sut).isChanged(mockFileEntry);
        sut.onAfterUpdate(mockFileEntry);
        verify(mockClearProductCacheCommand, times(1)).invoke();
        verify(mockQueuedResetProduct, times(1)).invoke();
    }

    @Test
    public void testOnAfterUpdateForNonRegistryClearsCacheAndResetsProduct() throws Exception {
        DLFileEntry mockFileEntry = createMockStructure_Product_AIL_TestProduct_TestFile();
        doReturn(true).when(sut).isChanged(mockFileEntry);
        sut.onAfterUpdate(mockFileEntry);
        verify(mockClearProductCacheCommand, times(1)).invoke();
        verify(mockQueuedResetProduct, never()).invoke();
    }

    @Test
    public void testOnAfterUpdateForNonProductFilesDoesNothing() throws Exception {
        DLFileEntry mockFileEntry = mock(DLFileEntry.class);
        DLFolder mockDLFolder=mock(DLFolder.class);
        doReturn(mockDLFolder).when(mockFileEntry).getFolder();
        doReturn("My/Folder/Path").when(mockDLFolder).getPath();
        sut.onAfterUpdate(mockFileEntry);
        verify(mockClearProductCacheCommand, never()).invoke();
        verify(mockQueuedResetProduct, never()).invoke();
    }

    @Test
    public void testOnAfterUpdateForUnchangedNonRegistryDoesNotInvokeAnything() throws Exception {
        DLFileEntry mockFileEntry = createMockStructure_Product_AIL_TestProduct_TestFile();
        doReturn(false).when(sut).isChanged(mockFileEntry);
        sut.onAfterUpdate(mockFileEntry);
        verify(mockClearProductCacheCommand, never()).invoke();
        verify(mockQueuedResetProduct, never()).invoke();
    }

    @Test
    public void testOnAfterCreateClearsCacheAndResetsProduct() throws Exception {
        DLFileEntry mockFileEntry = createMockStructure_Product_AIL_TestProduct_Registry();
        doReturn(true).when(sut).isChanged(mockFileEntry);
        sut.onAfterCreate(mockFileEntry);
        verify(mockClearProductCacheCommand, times(1)).invoke();
        verify(mockQueuedResetProduct, times(1)).invoke();
    }

    @Test
    public void testOnBeforeUpdateRecognisesProductPath() throws Exception {
        DLFileEntry mockFileEntry = mock(DLFileEntry.class);
        doReturn("/Product/SomeProductName").when(sut).fileEntry2FullPath(mockFileEntry);
        sut.onBeforeUpdate(mockFileEntry);
        verify(sut, times(1)).recordChange(eq(mockFileEntry));
    }

    @Test
    public void testOnBeforeUpdateRecognisesNoneProductPath() throws Exception {
        DLFileEntry mockFileEntry = mock(DLFileEntry.class);
        doReturn("/NoneProduct/SomeProductName").when(sut).fileEntry2FullPath(mockFileEntry);
        sut.onBeforeUpdate(mockFileEntry);
        verify(sut, never()).recordChange(eq(mockFileEntry));
    }

    @Test(expected = NullPointerException.class)
    public void testFolderIsAProductFolderError() throws Exception {
        DLFolder mockFolderEntry = mock(DLFolder.class);
        doReturn(1234L).when(mockFolderEntry).getGroupId();
        doReturn(2345L).when(mockFolderEntry).getFolderId();
        doThrow(new NullPointerException()).when(sut).getFileEntry(eq(1234L), eq(2345L), eq("Registry.xml"));
        sut.isFolderAProductRoot(mockFolderEntry);
    }

    @Test
    public void testFolderIsAProductFolderFileNotFound() throws Exception {
        DLFolder mockFolderEntry = mock(DLFolder.class);
        doReturn(1234L).when(mockFolderEntry).getGroupId();
        doReturn(2345L).when(mockFolderEntry).getFolderId();
        doThrow(new NoSuchFileEntryException()).when(sut).getFileEntry(eq(1234L), eq(2345L), eq("Registry.xml"));
        assertFalse(sut.isFolderAProductRoot(mockFolderEntry));
    }

    @Test
    public void testFolderIsAProductFolder() throws Exception {
        DLFolder mockFolderEntry = mock(DLFolder.class);
        DLFileEntry mockFileEntry=mock(DLFileEntry.class);

        doReturn(1234L).when(mockFolderEntry).getGroupId();
        doReturn(2345L).when(mockFolderEntry).getFolderId();
        doReturn(mockFileEntry).when(sut).getFileEntry(eq(1234L), eq(2345L), eq("Registry.xml"));
        assertTrue(sut.isFolderAProductRoot(mockFolderEntry));
    }

    @Test
    public void testFileEntry2FullPathNullSafety() throws Exception {
        assertNull(sut.fileEntry2FullPath(null));
    }

    @Test
    public void testFileEntry2FullPathHappyPath() throws Exception {
        DLFileEntry fileEntry = createMockStructure_Product_AIL_TestProduct_TestFile();
        assertNotNull(sut.fileEntry2FullPath(fileEntry));
        assertEquals("/Product/AIL/TestProduct/TestFile", sut.fileEntry2FullPath(fileEntry));
    }

    @Test(expected = ModelListenerException.class)
    public void testFileEntry2FullPathException() throws Exception {
        DLFileEntry fileEntry = mock(DLFileEntry.class);
        doThrow(new NullPointerException()).when(fileEntry).getTitle();
        sut.fileEntry2FullPath(fileEntry);
    }

    @Test
    public void testFileEntry2ProductName() throws Exception {
        assertNull(sut.fileEntry2ProductName(null));

        DLFolder folder1 = createMockStructure_Product_AIL_TestProduct();
        assertEquals("AIL.TestProduct", sut.fileEntry2ProductName(folder1));

        DLFolder folder2 = createMockStructure_Product_AIL_TestProduct_Folder();
        assertEquals("AIL.TestProduct", sut.fileEntry2ProductName(folder2));
    }

    @Test(expected = IllegalStateException.class)
    public void testFileEntry2ProductNameOutsideOfTree() throws Exception {
        DLFolder folder1 = createMockStructure_Product_AIL();
        sut.fileEntry2ProductName(folder1);
    }

}
