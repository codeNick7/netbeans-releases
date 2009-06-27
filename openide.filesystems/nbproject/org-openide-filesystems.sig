#Signature file v4.0
#Version 7.21.1

CLSS public java.io.IOException
cons public IOException()
cons public IOException(java.lang.String)
cons public IOException(java.lang.String,java.lang.Throwable)
cons public IOException(java.lang.Throwable)
supr java.lang.Exception
hfds serialVersionUID

CLSS public abstract interface java.io.Serializable

CLSS public abstract interface !annotation java.lang.Deprecated
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
intf java.lang.annotation.Annotation

CLSS public java.lang.Exception
cons public Exception()
cons public Exception(java.lang.String)
cons public Exception(java.lang.String,java.lang.Throwable)
cons public Exception(java.lang.Throwable)
supr java.lang.Throwable
hfds serialVersionUID

CLSS public java.lang.Object
cons public Object()
meth protected java.lang.Object clone() throws java.lang.CloneNotSupportedException
meth protected void finalize() throws java.lang.Throwable
meth public boolean equals(java.lang.Object)
meth public final java.lang.Class<?> getClass()
meth public final void notify()
meth public final void notifyAll()
meth public final void wait() throws java.lang.InterruptedException
meth public final void wait(long) throws java.lang.InterruptedException
meth public final void wait(long,int) throws java.lang.InterruptedException
meth public int hashCode()
meth public java.lang.String toString()

CLSS public java.lang.Throwable
cons public Throwable()
cons public Throwable(java.lang.String)
cons public Throwable(java.lang.String,java.lang.Throwable)
cons public Throwable(java.lang.Throwable)
intf java.io.Serializable
meth public java.lang.StackTraceElement[] getStackTrace()
meth public java.lang.String getLocalizedMessage()
meth public java.lang.String getMessage()
meth public java.lang.String toString()
meth public java.lang.Throwable fillInStackTrace()
meth public java.lang.Throwable getCause()
meth public java.lang.Throwable initCause(java.lang.Throwable)
meth public void printStackTrace()
meth public void printStackTrace(java.io.PrintStream)
meth public void printStackTrace(java.io.PrintWriter)
meth public void setStackTrace(java.lang.StackTraceElement[])
supr java.lang.Object
hfds backtrace,cause,detailMessage,serialVersionUID,stackTrace

CLSS public abstract interface java.lang.annotation.Annotation
meth public abstract boolean equals(java.lang.Object)
meth public abstract int hashCode()
meth public abstract java.lang.Class<? extends java.lang.annotation.Annotation> annotationType()
meth public abstract java.lang.String toString()

CLSS public abstract interface !annotation java.lang.annotation.Documented
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[ANNOTATION_TYPE])
intf java.lang.annotation.Annotation

CLSS public abstract interface !annotation java.lang.annotation.Retention
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[ANNOTATION_TYPE])
intf java.lang.annotation.Annotation
meth public abstract java.lang.annotation.RetentionPolicy value()

CLSS public abstract interface !annotation java.lang.annotation.Target
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[ANNOTATION_TYPE])
intf java.lang.annotation.Annotation
meth public abstract java.lang.annotation.ElementType[] value()

CLSS public abstract interface java.util.EventListener

CLSS public java.util.EventObject
cons public EventObject(java.lang.Object)
fld protected java.lang.Object source
intf java.io.Serializable
meth public java.lang.Object getSource()
meth public java.lang.String toString()
supr java.lang.Object
hfds serialVersionUID

CLSS public abstract javax.annotation.processing.AbstractProcessor
cons protected AbstractProcessor()
fld protected javax.annotation.processing.ProcessingEnvironment processingEnv
intf javax.annotation.processing.Processor
meth protected boolean isInitialized()
meth public abstract boolean process(java.util.Set<? extends javax.lang.model.element.TypeElement>,javax.annotation.processing.RoundEnvironment)
meth public java.lang.Iterable<? extends javax.annotation.processing.Completion> getCompletions(javax.lang.model.element.Element,javax.lang.model.element.AnnotationMirror,javax.lang.model.element.ExecutableElement,java.lang.String)
meth public java.util.Set<java.lang.String> getSupportedAnnotationTypes()
meth public java.util.Set<java.lang.String> getSupportedOptions()
meth public javax.lang.model.SourceVersion getSupportedSourceVersion()
meth public void init(javax.annotation.processing.ProcessingEnvironment)
supr java.lang.Object
hfds initialized

CLSS public abstract interface javax.annotation.processing.Processor
meth public abstract boolean process(java.util.Set<? extends javax.lang.model.element.TypeElement>,javax.annotation.processing.RoundEnvironment)
meth public abstract java.lang.Iterable<? extends javax.annotation.processing.Completion> getCompletions(javax.lang.model.element.Element,javax.lang.model.element.AnnotationMirror,javax.lang.model.element.ExecutableElement,java.lang.String)
meth public abstract java.util.Set<java.lang.String> getSupportedAnnotationTypes()
meth public abstract java.util.Set<java.lang.String> getSupportedOptions()
meth public abstract javax.lang.model.SourceVersion getSupportedSourceVersion()
meth public abstract void init(javax.annotation.processing.ProcessingEnvironment)

CLSS public abstract org.openide.filesystems.AbstractFileSystem
cons public AbstractFileSystem()
fld protected org.openide.filesystems.AbstractFileSystem$Attr attr
fld protected org.openide.filesystems.AbstractFileSystem$Change change
fld protected org.openide.filesystems.AbstractFileSystem$Info info
fld protected org.openide.filesystems.AbstractFileSystem$List list
fld protected org.openide.filesystems.AbstractFileSystem$Transfer transfer
innr public abstract interface static Attr
innr public abstract interface static Change
innr public abstract interface static Info
innr public abstract interface static List
innr public abstract interface static Transfer
meth protected <%0 extends org.openide.filesystems.FileObject> java.lang.ref.Reference<{%%0}> createReference({%%0})
meth protected boolean canRead(java.lang.String)
meth protected boolean canWrite(java.lang.String)
meth protected boolean checkVirtual(java.lang.String)
meth protected final int getRefreshTime()
meth protected final java.lang.ref.Reference<? extends org.openide.filesystems.FileObject> findReference(java.lang.String)
meth protected final java.util.Enumeration<? extends org.openide.filesystems.FileObject> existingFileObjects(org.openide.filesystems.FileObject)
meth protected final org.openide.filesystems.FileObject refreshRoot()
meth protected final void refreshResource(java.lang.String,boolean)
meth protected final void setRefreshTime(int)
meth protected void markImportant(java.lang.String,boolean)
meth public abstract java.lang.String getDisplayName()
meth public org.openide.filesystems.FileObject find(java.lang.String,java.lang.String,java.lang.String)
 anno 0 java.lang.Deprecated()
meth public org.openide.filesystems.FileObject findResource(java.lang.String)
meth public org.openide.filesystems.FileObject getRoot()
meth public org.openide.util.actions.SystemAction[] getActions()
meth public void refresh(boolean)
supr org.openide.filesystems.FileSystem
hfds NO_SYSTEM_ACTIONS,SYSTEM_ACTIONS,lastEnum,refresher,root,serialVersionUID

CLSS public abstract interface static org.openide.filesystems.AbstractFileSystem$Attr
fld public final static long serialVersionUID = 5978845941846736946
 anno 0 java.lang.Deprecated()
intf java.io.Serializable
meth public abstract java.lang.Object readAttribute(java.lang.String,java.lang.String)
meth public abstract java.util.Enumeration<java.lang.String> attributes(java.lang.String)
meth public abstract void deleteAttributes(java.lang.String)
meth public abstract void renameAttributes(java.lang.String,java.lang.String)
meth public abstract void writeAttribute(java.lang.String,java.lang.String,java.lang.Object) throws java.io.IOException

CLSS public abstract interface static org.openide.filesystems.AbstractFileSystem$Change
fld public final static long serialVersionUID = -5841597109944924596
 anno 0 java.lang.Deprecated()
intf java.io.Serializable
meth public abstract void createData(java.lang.String) throws java.io.IOException
meth public abstract void createFolder(java.lang.String) throws java.io.IOException
meth public abstract void delete(java.lang.String) throws java.io.IOException
meth public abstract void rename(java.lang.String,java.lang.String) throws java.io.IOException

CLSS public abstract interface static org.openide.filesystems.AbstractFileSystem$Info
fld public final static long serialVersionUID = -2438286177948307985
 anno 0 java.lang.Deprecated()
intf java.io.Serializable
meth public abstract boolean folder(java.lang.String)
meth public abstract boolean readOnly(java.lang.String)
meth public abstract java.io.InputStream inputStream(java.lang.String) throws java.io.FileNotFoundException
meth public abstract java.io.OutputStream outputStream(java.lang.String) throws java.io.IOException
meth public abstract java.lang.String mimeType(java.lang.String)
meth public abstract java.util.Date lastModified(java.lang.String)
meth public abstract long size(java.lang.String)
meth public abstract void lock(java.lang.String) throws java.io.IOException
meth public abstract void markUnimportant(java.lang.String)
meth public abstract void unlock(java.lang.String)

CLSS public abstract interface static org.openide.filesystems.AbstractFileSystem$List
fld public final static long serialVersionUID = -6242105832891012528
 anno 0 java.lang.Deprecated()
intf java.io.Serializable
meth public abstract java.lang.String[] children(java.lang.String)

CLSS public abstract interface static org.openide.filesystems.AbstractFileSystem$Transfer
fld public final static long serialVersionUID = -8945397853892302838
 anno 0 java.lang.Deprecated()
intf java.io.Serializable
meth public abstract boolean copy(java.lang.String,org.openide.filesystems.AbstractFileSystem$Transfer,java.lang.String) throws java.io.IOException
meth public abstract boolean move(java.lang.String,org.openide.filesystems.AbstractFileSystem$Transfer,java.lang.String) throws java.io.IOException

CLSS public org.openide.filesystems.DefaultAttributes
cons protected DefaultAttributes(org.openide.filesystems.AbstractFileSystem$Info,org.openide.filesystems.AbstractFileSystem$Change,org.openide.filesystems.AbstractFileSystem$List,java.lang.String)
cons public DefaultAttributes(org.openide.filesystems.AbstractFileSystem$Info,org.openide.filesystems.AbstractFileSystem$Change,org.openide.filesystems.AbstractFileSystem$List)
fld public final static java.lang.String ATTR_EXT = "attributes"
 anno 0 java.lang.Deprecated()
fld public final static java.lang.String ATTR_NAME = "filesystem"
 anno 0 java.lang.Deprecated()
fld public final static java.lang.String ATTR_NAME_EXT = "filesystem.attributes"
 anno 0 java.lang.Deprecated()
intf org.openide.filesystems.AbstractFileSystem$Attr
intf org.openide.filesystems.AbstractFileSystem$List
meth public java.lang.Object readAttribute(java.lang.String,java.lang.String)
meth public java.lang.String[] children(java.lang.String)
meth public java.util.Enumeration<java.lang.String> attributes(java.lang.String)
meth public void deleteAttributes(java.lang.String)
meth public void renameAttributes(java.lang.String,java.lang.String)
meth public void writeAttribute(java.lang.String,java.lang.String,java.lang.Object) throws java.io.IOException
supr java.lang.Object
hfds ATTR_NAME_EXT_XML,DTD_PATH,PUBLIC_ID,READONLY_ATTRIBUTES,cache,change,fileName,info,list,serialVersionUID
hcls ElementHandler,InnerParser,Table

CLSS public org.openide.filesystems.EnvironmentNotSupportedException
 anno 0 java.lang.Deprecated()
cons public EnvironmentNotSupportedException(org.openide.filesystems.FileSystem)
cons public EnvironmentNotSupportedException(org.openide.filesystems.FileSystem,java.lang.String)
meth public org.openide.filesystems.FileSystem getFileSystem()
supr java.io.IOException
hfds fs,serialVersionUID

CLSS public org.openide.filesystems.FileAlreadyLockedException
cons public FileAlreadyLockedException()
cons public FileAlreadyLockedException(java.lang.String)
supr java.io.IOException
hfds serialVersionUID

CLSS public org.openide.filesystems.FileAttributeEvent
cons public FileAttributeEvent(org.openide.filesystems.FileObject,java.lang.String,java.lang.Object,java.lang.Object)
cons public FileAttributeEvent(org.openide.filesystems.FileObject,org.openide.filesystems.FileObject,java.lang.String,java.lang.Object,java.lang.Object)
cons public FileAttributeEvent(org.openide.filesystems.FileObject,org.openide.filesystems.FileObject,java.lang.String,java.lang.Object,java.lang.Object,boolean)
meth public java.lang.Object getNewValue()
meth public java.lang.Object getOldValue()
meth public java.lang.String getName()
supr org.openide.filesystems.FileEvent
hfds name,newValue,oldValue,serialVersionUID

CLSS public org.openide.filesystems.FileChangeAdapter
cons public FileChangeAdapter()
intf org.openide.filesystems.FileChangeListener
meth public void fileAttributeChanged(org.openide.filesystems.FileAttributeEvent)
meth public void fileChanged(org.openide.filesystems.FileEvent)
meth public void fileDataCreated(org.openide.filesystems.FileEvent)
meth public void fileDeleted(org.openide.filesystems.FileEvent)
meth public void fileFolderCreated(org.openide.filesystems.FileEvent)
meth public void fileRenamed(org.openide.filesystems.FileRenameEvent)
supr java.lang.Object

CLSS public abstract interface org.openide.filesystems.FileChangeListener
intf java.util.EventListener
meth public abstract void fileAttributeChanged(org.openide.filesystems.FileAttributeEvent)
meth public abstract void fileChanged(org.openide.filesystems.FileEvent)
meth public abstract void fileDataCreated(org.openide.filesystems.FileEvent)
meth public abstract void fileDeleted(org.openide.filesystems.FileEvent)
meth public abstract void fileFolderCreated(org.openide.filesystems.FileEvent)
meth public abstract void fileRenamed(org.openide.filesystems.FileRenameEvent)

CLSS public org.openide.filesystems.FileChooserBuilder
cons public FileChooserBuilder(java.lang.Class)
cons public FileChooserBuilder(java.lang.String)
innr public abstract interface static BadgeProvider
meth public java.io.File showOpenDialog()
meth public java.io.File showSaveDialog()
meth public java.io.File[] showMultiOpenDialog()
meth public javax.swing.JFileChooser createFileChooser()
meth public org.openide.filesystems.FileChooserBuilder forceUseOfDefaultWorkingDirectory(boolean)
meth public org.openide.filesystems.FileChooserBuilder setAccessibleDescription(java.lang.String)
meth public org.openide.filesystems.FileChooserBuilder setApproveText(java.lang.String)
meth public org.openide.filesystems.FileChooserBuilder setBadgeProvider(org.openide.filesystems.FileChooserBuilder$BadgeProvider)
meth public org.openide.filesystems.FileChooserBuilder setControlButtonsAreShown(boolean)
meth public org.openide.filesystems.FileChooserBuilder setDefaultWorkingDirectory(java.io.File)
meth public org.openide.filesystems.FileChooserBuilder setDirectoriesOnly(boolean)
meth public org.openide.filesystems.FileChooserBuilder setFileFilter(javax.swing.filechooser.FileFilter)
meth public org.openide.filesystems.FileChooserBuilder setFileHiding(boolean)
meth public org.openide.filesystems.FileChooserBuilder setFilesOnly(boolean)
meth public org.openide.filesystems.FileChooserBuilder setTitle(java.lang.String)
supr java.lang.Object
hfds DONT_STORE_DIRECTORIES,PREVENT_SYMLINK_TRAVERSAL,aDescription,approveText,badger,controlButtonsShown,dirKey,dirsOnly,failoverDir,fileHiding,filesOnly,filter,force,title
hcls BadgeIconProvider,CustomFileView,IconProvider,MergedIcon,SavedDirFileChooser

CLSS public abstract interface static org.openide.filesystems.FileChooserBuilder$BadgeProvider
meth public abstract int getXOffset()
meth public abstract int getYOffset()
meth public abstract javax.swing.Icon getBadge(java.io.File)

CLSS public org.openide.filesystems.FileEvent
cons public FileEvent(org.openide.filesystems.FileObject)
cons public FileEvent(org.openide.filesystems.FileObject,org.openide.filesystems.FileObject)
cons public FileEvent(org.openide.filesystems.FileObject,org.openide.filesystems.FileObject,boolean)
meth public boolean firedFrom(org.openide.filesystems.FileSystem$AtomicAction)
meth public final boolean isExpected()
meth public final long getTime()
meth public final org.openide.filesystems.FileObject getFile()
meth public java.lang.String toString()
supr java.util.EventObject
hfds atomActionID,expected,file,serialVersionUID,time

CLSS public org.openide.filesystems.FileLock
cons public FileLock()
fld protected java.lang.Throwable lockedBy
fld public final static org.openide.filesystems.FileLock NONE
meth public boolean isValid()
meth public void finalize()
meth public void releaseLock()
supr java.lang.Object
hfds locked

CLSS public abstract org.openide.filesystems.FileObject
cons public FileObject()
intf java.io.Serializable
meth protected void fireFileAttributeChangedEvent(java.util.Enumeration<org.openide.filesystems.FileChangeListener>,org.openide.filesystems.FileAttributeEvent)
meth protected void fireFileChangedEvent(java.util.Enumeration<org.openide.filesystems.FileChangeListener>,org.openide.filesystems.FileEvent)
meth protected void fireFileDataCreatedEvent(java.util.Enumeration<org.openide.filesystems.FileChangeListener>,org.openide.filesystems.FileEvent)
meth protected void fireFileDeletedEvent(java.util.Enumeration<org.openide.filesystems.FileChangeListener>,org.openide.filesystems.FileEvent)
meth protected void fireFileFolderCreatedEvent(java.util.Enumeration<org.openide.filesystems.FileChangeListener>,org.openide.filesystems.FileEvent)
meth protected void fireFileRenamedEvent(java.util.Enumeration<org.openide.filesystems.FileChangeListener>,org.openide.filesystems.FileRenameEvent)
meth public abstract boolean isData()
meth public abstract boolean isFolder()
meth public abstract boolean isReadOnly()
 anno 0 java.lang.Deprecated()
meth public abstract boolean isRoot()
meth public abstract boolean isValid()
meth public abstract java.io.InputStream getInputStream() throws java.io.FileNotFoundException
meth public abstract java.io.OutputStream getOutputStream(org.openide.filesystems.FileLock) throws java.io.IOException
meth public abstract java.lang.Object getAttribute(java.lang.String)
meth public abstract java.lang.String getExt()
meth public abstract java.lang.String getName()
meth public abstract java.util.Date lastModified()
meth public abstract java.util.Enumeration<java.lang.String> getAttributes()
meth public abstract long getSize()
meth public abstract org.openide.filesystems.FileLock lock() throws java.io.IOException
meth public abstract org.openide.filesystems.FileObject createData(java.lang.String,java.lang.String) throws java.io.IOException
meth public abstract org.openide.filesystems.FileObject createFolder(java.lang.String) throws java.io.IOException
meth public abstract org.openide.filesystems.FileObject getFileObject(java.lang.String,java.lang.String)
meth public abstract org.openide.filesystems.FileObject getParent()
meth public abstract org.openide.filesystems.FileObject[] getChildren()
meth public abstract org.openide.filesystems.FileSystem getFileSystem() throws org.openide.filesystems.FileStateInvalidException
meth public abstract void addFileChangeListener(org.openide.filesystems.FileChangeListener)
meth public abstract void delete(org.openide.filesystems.FileLock) throws java.io.IOException
meth public abstract void removeFileChangeListener(org.openide.filesystems.FileChangeListener)
meth public abstract void rename(org.openide.filesystems.FileLock,java.lang.String,java.lang.String) throws java.io.IOException
meth public abstract void setAttribute(java.lang.String,java.lang.Object) throws java.io.IOException
meth public abstract void setImportant(boolean)
 anno 0 java.lang.Deprecated()
meth public boolean canRead()
meth public boolean canWrite()
meth public boolean existsExt(java.lang.String)
meth public boolean isLocked()
meth public boolean isVirtual()
meth public byte[] asBytes() throws java.io.IOException
meth public final boolean hasExt(java.lang.String)
meth public final java.io.OutputStream getOutputStream() throws java.io.IOException
meth public final java.net.URL getURL() throws org.openide.filesystems.FileStateInvalidException
meth public final void delete() throws java.io.IOException
meth public java.lang.String asText() throws java.io.IOException
meth public java.lang.String asText(java.lang.String) throws java.io.IOException
meth public java.lang.String getMIMEType()
meth public java.lang.String getNameExt()
meth public java.lang.String getPackageName(char)
 anno 0 java.lang.Deprecated()
meth public java.lang.String getPackageNameExt(char,char)
 anno 0 java.lang.Deprecated()
meth public java.lang.String getPath()
meth public java.lang.String toString()
meth public java.util.Enumeration<? extends org.openide.filesystems.FileObject> getChildren(boolean)
meth public java.util.Enumeration<? extends org.openide.filesystems.FileObject> getData(boolean)
meth public java.util.Enumeration<? extends org.openide.filesystems.FileObject> getFolders(boolean)
meth public java.util.List<java.lang.String> asLines() throws java.io.IOException
meth public java.util.List<java.lang.String> asLines(java.lang.String) throws java.io.IOException
meth public org.openide.filesystems.FileObject copy(org.openide.filesystems.FileObject,java.lang.String,java.lang.String) throws java.io.IOException
meth public org.openide.filesystems.FileObject createData(java.lang.String) throws java.io.IOException
meth public org.openide.filesystems.FileObject getFileObject(java.lang.String)
meth public org.openide.filesystems.FileObject move(org.openide.filesystems.FileLock,org.openide.filesystems.FileObject,java.lang.String,java.lang.String) throws java.io.IOException
meth public void refresh()
meth public void refresh(boolean)
supr java.lang.Object
hfds serialVersionUID
hcls ED,OnlyFolders,PriorityFileChangeListener

CLSS public org.openide.filesystems.FileRenameEvent
cons public FileRenameEvent(org.openide.filesystems.FileObject,java.lang.String,java.lang.String)
cons public FileRenameEvent(org.openide.filesystems.FileObject,org.openide.filesystems.FileObject,java.lang.String,java.lang.String)
cons public FileRenameEvent(org.openide.filesystems.FileObject,org.openide.filesystems.FileObject,java.lang.String,java.lang.String,boolean)
meth public java.lang.String getExt()
meth public java.lang.String getName()
supr org.openide.filesystems.FileEvent
hfds ext,name,serialVersionUID

CLSS public org.openide.filesystems.FileStateInvalidException
cons public FileStateInvalidException()
cons public FileStateInvalidException(java.lang.String)
meth public java.lang.String getFileSystemName()
supr java.io.IOException
hfds fileSystemName,serialVersionUID

CLSS public final org.openide.filesystems.FileStatusEvent
cons public FileStatusEvent(org.openide.filesystems.FileSystem,boolean,boolean)
cons public FileStatusEvent(org.openide.filesystems.FileSystem,java.util.Set<? extends org.openide.filesystems.FileObject>,boolean,boolean)
cons public FileStatusEvent(org.openide.filesystems.FileSystem,org.openide.filesystems.FileObject,boolean,boolean)
meth public boolean hasChanged(org.openide.filesystems.FileObject)
meth public boolean isIconChange()
meth public boolean isNameChange()
meth public org.openide.filesystems.FileSystem getFileSystem()
supr java.util.EventObject
hfds files,icon,name,serialVersionUID

CLSS public abstract interface org.openide.filesystems.FileStatusListener
intf java.util.EventListener
meth public abstract void annotationChanged(org.openide.filesystems.FileStatusEvent)

CLSS public abstract org.openide.filesystems.FileSystem
cons public FileSystem()
fld public final static java.lang.String PROP_DISPLAY_NAME = "displayName"
fld public final static java.lang.String PROP_HIDDEN = "hidden"
 anno 0 java.lang.Deprecated()
fld public final static java.lang.String PROP_READ_ONLY = "readOnly"
fld public final static java.lang.String PROP_ROOT = "root"
fld public final static java.lang.String PROP_SYSTEM_NAME = "systemName"
 anno 0 java.lang.Deprecated()
fld public final static java.lang.String PROP_VALID = "valid"
innr public abstract interface static AtomicAction
innr public abstract interface static HtmlStatus
innr public abstract interface static Status
innr public abstract static Environment
intf java.io.Serializable
meth protected boolean isPersistent()
 anno 0 java.lang.Deprecated()
meth protected final void fireFileStatusChanged(org.openide.filesystems.FileStatusEvent)
meth protected final void firePropertyChange(java.lang.String,java.lang.Object,java.lang.Object)
meth protected final void fireVetoableChange(java.lang.String,java.lang.Object,java.lang.Object) throws java.beans.PropertyVetoException
meth protected final void setCapability(org.openide.filesystems.FileSystemCapability)
 anno 0 java.lang.Deprecated()
meth protected final void setSystemName(java.lang.String) throws java.beans.PropertyVetoException
 anno 0 java.lang.Deprecated()
meth public abstract boolean isReadOnly()
meth public abstract java.lang.String getDisplayName()
meth public abstract org.openide.filesystems.FileObject findResource(java.lang.String)
meth public abstract org.openide.filesystems.FileObject getRoot()
meth public abstract org.openide.util.actions.SystemAction[] getActions()
meth public final boolean isDefault()
meth public final boolean isHidden()
 anno 0 java.lang.Deprecated()
meth public final boolean isValid()
meth public final java.lang.String getSystemName()
 anno 0 java.lang.Deprecated()
meth public final org.openide.filesystems.FileSystemCapability getCapability()
 anno 0 java.lang.Deprecated()
meth public final void addFileChangeListener(org.openide.filesystems.FileChangeListener)
meth public final void addFileStatusListener(org.openide.filesystems.FileStatusListener)
meth public final void addPropertyChangeListener(java.beans.PropertyChangeListener)
meth public final void addVetoableChangeListener(java.beans.VetoableChangeListener)
meth public final void removeFileChangeListener(org.openide.filesystems.FileChangeListener)
meth public final void removeFileStatusListener(org.openide.filesystems.FileStatusListener)
meth public final void removePropertyChangeListener(java.beans.PropertyChangeListener)
meth public final void removeVetoableChangeListener(java.beans.VetoableChangeListener)
meth public final void runAtomicAction(org.openide.filesystems.FileSystem$AtomicAction) throws java.io.IOException
meth public final void setHidden(boolean)
 anno 0 java.lang.Deprecated()
meth public java.lang.String toString()
meth public org.openide.filesystems.FileObject find(java.lang.String,java.lang.String,java.lang.String)
 anno 0 java.lang.Deprecated()
meth public org.openide.filesystems.FileSystem$Status getStatus()
meth public org.openide.util.actions.SystemAction[] getActions(java.util.Set<org.openide.filesystems.FileObject>)
meth public void addNotify()
meth public void prepareEnvironment(org.openide.filesystems.FileSystem$Environment) throws org.openide.filesystems.EnvironmentNotSupportedException
 anno 0 java.lang.Deprecated()
meth public void refresh(boolean)
meth public void removeNotify()
supr java.lang.Object
hfds PROP_CAPABILITIES,STATUS_NONE,assigned,capability,capabilityListener,changeSupport,fclSupport,fileStatusList,hidden,internLock,repository,serialVersionUID,systemName,thrLocal,valid,vetoableChangeList
hcls EventDispatcher,FileStatusDispatcher

CLSS public abstract interface static org.openide.filesystems.FileSystem$AtomicAction
meth public abstract void run() throws java.io.IOException

CLSS public abstract static org.openide.filesystems.FileSystem$Environment
 anno 0 java.lang.Deprecated()
cons public Environment()
meth public void addClassPath(java.lang.String)
 anno 0 java.lang.Deprecated()
supr java.lang.Object

CLSS public abstract interface static org.openide.filesystems.FileSystem$HtmlStatus
intf org.openide.filesystems.FileSystem$Status
meth public abstract java.lang.String annotateNameHtml(java.lang.String,java.util.Set<? extends org.openide.filesystems.FileObject>)

CLSS public abstract interface static org.openide.filesystems.FileSystem$Status
meth public abstract java.awt.Image annotateIcon(java.awt.Image,int,java.util.Set<? extends org.openide.filesystems.FileObject>)
meth public abstract java.lang.String annotateName(java.lang.String,java.util.Set<? extends org.openide.filesystems.FileObject>)

CLSS public org.openide.filesystems.FileSystemCapability
 anno 0 java.lang.Deprecated()
cons public FileSystemCapability()
fld public final static org.openide.filesystems.FileSystemCapability ALL
fld public final static org.openide.filesystems.FileSystemCapability COMPILE
 anno 0 java.lang.Deprecated()
fld public final static org.openide.filesystems.FileSystemCapability DEBUG
 anno 0 java.lang.Deprecated()
fld public final static org.openide.filesystems.FileSystemCapability DOC
 anno 0 java.lang.Deprecated()
fld public final static org.openide.filesystems.FileSystemCapability EXECUTE
 anno 0 java.lang.Deprecated()
innr public static Bean
meth public boolean capableOf(org.openide.filesystems.FileSystemCapability)
meth public final java.util.Enumeration<? extends org.openide.filesystems.FileObject> findAll(java.lang.String,java.lang.String,java.lang.String)
 anno 0 java.lang.Deprecated()
meth public final org.openide.filesystems.FileObject find(java.lang.String,java.lang.String,java.lang.String)
 anno 0 java.lang.Deprecated()
meth public java.util.Enumeration<? extends org.openide.filesystems.FileObject> findAllResources(java.lang.String)
 anno 0 java.lang.Deprecated()
meth public java.util.Enumeration<? extends org.openide.filesystems.FileSystem> fileSystems()
 anno 0 java.lang.Deprecated()
meth public org.openide.filesystems.FileObject findResource(java.lang.String)
 anno 0 java.lang.Deprecated()
meth public void addPropertyChangeListener(java.beans.PropertyChangeListener)
meth public void removePropertyChangeListener(java.beans.PropertyChangeListener)
supr java.lang.Object

CLSS public static org.openide.filesystems.FileSystemCapability$Bean
 anno 0 java.lang.Deprecated()
cons public Bean()
intf java.io.Serializable
meth public boolean capableOf(org.openide.filesystems.FileSystemCapability)
meth public boolean getCompile()
 anno 0 java.lang.Deprecated()
meth public boolean getDebug()
 anno 0 java.lang.Deprecated()
meth public boolean getDoc()
 anno 0 java.lang.Deprecated()
meth public boolean getExecute()
 anno 0 java.lang.Deprecated()
meth public void addPropertyChangeListener(java.beans.PropertyChangeListener)
meth public void removePropertyChangeListener(java.beans.PropertyChangeListener)
meth public void setCompile(boolean)
 anno 0 java.lang.Deprecated()
meth public void setDebug(boolean)
 anno 0 java.lang.Deprecated()
meth public void setDoc(boolean)
 anno 0 java.lang.Deprecated()
meth public void setExecute(boolean)
 anno 0 java.lang.Deprecated()
supr org.openide.filesystems.FileSystemCapability
hfds compilation,debug,doc,execution,serialVersionUID,supp

CLSS public final org.openide.filesystems.FileUtil
meth public !varargs static java.lang.String getMIMEType(org.openide.filesystems.FileObject,java.lang.String[])
meth public !varargs static void refreshFor(java.io.File[])
meth public final static void runAtomicAction(java.lang.Runnable)
meth public final static void runAtomicAction(org.openide.filesystems.FileSystem$AtomicAction) throws java.io.IOException
meth public static boolean affectsOrder(org.openide.filesystems.FileAttributeEvent)
meth public static boolean isArchiveFile(java.net.URL)
meth public static boolean isArchiveFile(org.openide.filesystems.FileObject)
meth public static boolean isParentOf(org.openide.filesystems.FileObject,org.openide.filesystems.FileObject)
meth public static java.io.File archiveOrDirForURL(java.net.URL)
meth public static java.io.File normalizeFile(java.io.File)
meth public static java.io.File toFile(org.openide.filesystems.FileObject)
meth public static java.lang.String findFreeFileName(org.openide.filesystems.FileObject,java.lang.String,java.lang.String)
meth public static java.lang.String findFreeFolderName(org.openide.filesystems.FileObject,java.lang.String)
meth public static java.lang.String getExtension(java.lang.String)
meth public static java.lang.String getFileDisplayName(org.openide.filesystems.FileObject)
meth public static java.lang.String getMIMEType(java.lang.String)
 anno 0 java.lang.Deprecated()
meth public static java.lang.String getMIMEType(org.openide.filesystems.FileObject)
meth public static java.lang.String getRelativePath(org.openide.filesystems.FileObject,org.openide.filesystems.FileObject)
meth public static java.net.URL getArchiveFile(java.net.URL)
meth public static java.net.URL getArchiveRoot(java.net.URL)
meth public static java.net.URL urlForArchiveOrDir(java.io.File)
meth public static java.net.URLStreamHandler nbfsURLStreamHandler()
meth public static java.util.List<java.lang.String> getMIMETypeExtensions(java.lang.String)
meth public static java.util.List<org.openide.filesystems.FileObject> getOrder(java.util.Collection<org.openide.filesystems.FileObject>,boolean)
meth public static org.openide.filesystems.FileChangeListener weakFileChangeListener(org.openide.filesystems.FileChangeListener,java.lang.Object)
meth public static org.openide.filesystems.FileObject copyFile(org.openide.filesystems.FileObject,org.openide.filesystems.FileObject,java.lang.String) throws java.io.IOException
meth public static org.openide.filesystems.FileObject copyFile(org.openide.filesystems.FileObject,org.openide.filesystems.FileObject,java.lang.String,java.lang.String) throws java.io.IOException
meth public static org.openide.filesystems.FileObject createData(java.io.File) throws java.io.IOException
meth public static org.openide.filesystems.FileObject createData(org.openide.filesystems.FileObject,java.lang.String) throws java.io.IOException
meth public static org.openide.filesystems.FileObject createFolder(java.io.File) throws java.io.IOException
meth public static org.openide.filesystems.FileObject createFolder(org.openide.filesystems.FileObject,java.lang.String) throws java.io.IOException
meth public static org.openide.filesystems.FileObject findBrother(org.openide.filesystems.FileObject,java.lang.String)
meth public static org.openide.filesystems.FileObject getArchiveFile(org.openide.filesystems.FileObject)
meth public static org.openide.filesystems.FileObject getArchiveRoot(org.openide.filesystems.FileObject)
meth public static org.openide.filesystems.FileObject getConfigFile(java.lang.String)
meth public static org.openide.filesystems.FileObject getConfigRoot()
meth public static org.openide.filesystems.FileObject moveFile(org.openide.filesystems.FileObject,org.openide.filesystems.FileObject,java.lang.String) throws java.io.IOException
meth public static org.openide.filesystems.FileObject toFileObject(java.io.File)
meth public static org.openide.filesystems.FileObject[] fromFile(java.io.File)
 anno 0 java.lang.Deprecated()
meth public static org.openide.filesystems.FileStatusListener weakFileStatusListener(org.openide.filesystems.FileStatusListener,java.lang.Object)
meth public static org.openide.filesystems.FileSystem createMemoryFileSystem()
meth public static void addFileChangeListener(org.openide.filesystems.FileChangeListener)
meth public static void addFileChangeListener(org.openide.filesystems.FileChangeListener,java.io.File)
meth public static void copy(java.io.InputStream,java.io.OutputStream) throws java.io.IOException
meth public static void copyAttributes(org.openide.filesystems.FileObject,org.openide.filesystems.FileObject) throws java.io.IOException
meth public static void extractJar(org.openide.filesystems.FileObject,java.io.InputStream) throws java.io.IOException
 anno 0 java.lang.Deprecated()
meth public static void preventFileChooserSymlinkTraversal(javax.swing.JFileChooser,java.io.File)
meth public static void refreshAll()
meth public static void removeFileChangeListener(org.openide.filesystems.FileChangeListener)
meth public static void removeFileChangeListener(org.openide.filesystems.FileChangeListener,java.io.File)
meth public static void setMIMEType(java.lang.String,java.lang.String)
meth public static void setOrder(java.util.List<org.openide.filesystems.FileObject>) throws java.io.IOException
supr java.lang.Object
hfds LOG,ZIP_HEADER_1,ZIP_HEADER_2,archiveFileCache,diskFileSystem,holders,transientAttributes
hcls Holder,NonCanonicalizingFile,NonCanonicalizingFileSystemView

CLSS public org.openide.filesystems.JarFileSystem
cons public JarFileSystem()
cons public JarFileSystem(org.openide.filesystems.FileSystemCapability)
 anno 0 java.lang.Deprecated()
innr public static Impl
meth protected <%0 extends org.openide.filesystems.FileObject> java.lang.ref.Reference<{%%0}> createReference({%%0})
meth protected boolean folder(java.lang.String)
meth protected boolean readOnly(java.lang.String)
meth protected java.io.InputStream inputStream(java.lang.String) throws java.io.FileNotFoundException
meth protected java.io.OutputStream outputStream(java.lang.String) throws java.io.IOException
meth protected java.lang.Object readAttribute(java.lang.String,java.lang.String)
meth protected java.lang.String mimeType(java.lang.String)
meth protected java.lang.String[] children(java.lang.String)
meth protected java.util.Date lastModified(java.lang.String)
meth protected java.util.Enumeration<java.lang.String> attributes(java.lang.String)
meth protected long size(java.lang.String)
meth protected void createData(java.lang.String) throws java.io.IOException
meth protected void createFolder(java.lang.String) throws java.io.IOException
meth protected void delete(java.lang.String) throws java.io.IOException
meth protected void deleteAttributes(java.lang.String)
meth protected void finalize() throws java.lang.Throwable
meth protected void lock(java.lang.String) throws java.io.IOException
meth protected void markUnimportant(java.lang.String)
meth protected void rename(java.lang.String,java.lang.String) throws java.io.IOException
meth protected void renameAttributes(java.lang.String,java.lang.String)
meth protected void unlock(java.lang.String)
meth protected void writeAttribute(java.lang.String,java.lang.String,java.lang.Object) throws java.io.IOException
meth public boolean isReadOnly()
meth public java.io.File getJarFile()
meth public java.lang.String getDisplayName()
meth public java.util.jar.Manifest getManifest()
meth public void prepareEnvironment(org.openide.filesystems.FileSystem$Environment)
 anno 0 java.lang.Deprecated()
meth public void removeNotify()
meth public void setJarFile(java.io.File) throws java.beans.PropertyVetoException,java.io.IOException
supr org.openide.filesystems.AbstractFileSystem
hfds LOGGER,MEM_STREAM_SIZE,REFRESH_TIME,aliveCount,checkTime,closeSync,closeTask,fcl,foRoot,jar,lastModification,manifest,req,root,serialVersionUID,softCache,strongCache,watcherTask
hcls Cache,Ref

CLSS public static org.openide.filesystems.JarFileSystem$Impl
cons public Impl(org.openide.filesystems.JarFileSystem)
intf org.openide.filesystems.AbstractFileSystem$Attr
intf org.openide.filesystems.AbstractFileSystem$Change
intf org.openide.filesystems.AbstractFileSystem$Info
intf org.openide.filesystems.AbstractFileSystem$List
meth public boolean folder(java.lang.String)
meth public boolean readOnly(java.lang.String)
meth public java.io.InputStream inputStream(java.lang.String) throws java.io.FileNotFoundException
meth public java.io.OutputStream outputStream(java.lang.String) throws java.io.IOException
meth public java.lang.Object readAttribute(java.lang.String,java.lang.String)
meth public java.lang.String mimeType(java.lang.String)
meth public java.lang.String[] children(java.lang.String)
meth public java.util.Date lastModified(java.lang.String)
meth public java.util.Enumeration<java.lang.String> attributes(java.lang.String)
meth public long size(java.lang.String)
meth public void createData(java.lang.String) throws java.io.IOException
meth public void createFolder(java.lang.String) throws java.io.IOException
meth public void delete(java.lang.String) throws java.io.IOException
meth public void deleteAttributes(java.lang.String)
meth public void lock(java.lang.String) throws java.io.IOException
meth public void markUnimportant(java.lang.String)
meth public void rename(java.lang.String,java.lang.String) throws java.io.IOException
meth public void renameAttributes(java.lang.String,java.lang.String)
meth public void unlock(java.lang.String)
meth public void writeAttribute(java.lang.String,java.lang.String,java.lang.Object) throws java.io.IOException
supr java.lang.Object
hfds fs,serialVersionUID

CLSS public org.openide.filesystems.LocalFileSystem
cons public LocalFileSystem()
cons public LocalFileSystem(org.openide.filesystems.FileSystemCapability)
 anno 0 java.lang.Deprecated()
innr public static Impl
meth protected boolean folder(java.lang.String)
meth protected boolean readOnly(java.lang.String)
meth protected java.io.InputStream inputStream(java.lang.String) throws java.io.FileNotFoundException
meth protected java.io.OutputStream outputStream(java.lang.String) throws java.io.IOException
meth protected java.lang.String computeSystemName(java.io.File)
meth protected java.lang.String mimeType(java.lang.String)
meth protected java.lang.String[] children(java.lang.String)
meth protected java.util.Date lastModified(java.lang.String)
meth protected long size(java.lang.String)
meth protected void createData(java.lang.String) throws java.io.IOException
meth protected void createFolder(java.lang.String) throws java.io.IOException
meth protected void delete(java.lang.String) throws java.io.IOException
meth protected void lock(java.lang.String) throws java.io.IOException
meth protected void markUnimportant(java.lang.String)
meth protected void rename(java.lang.String,java.lang.String) throws java.io.IOException
meth protected void unlock(java.lang.String)
meth public boolean isReadOnly()
meth public java.io.File getRootDirectory()
meth public java.lang.String getDisplayName()
meth public void prepareEnvironment(org.openide.filesystems.FileSystem$Environment)
 anno 0 java.lang.Deprecated()
meth public void setReadOnly(boolean)
meth public void setRootDirectory(java.io.File) throws java.beans.PropertyVetoException,java.io.IOException
supr org.openide.filesystems.AbstractFileSystem
hfds FAILURE,NOT_EXISTS,REFRESH_TIME,SUCCESS,readOnly,rootFile,serialVersionUID
hcls InnerAttrs

CLSS public static org.openide.filesystems.LocalFileSystem$Impl
cons public Impl(org.openide.filesystems.LocalFileSystem)
intf org.openide.filesystems.AbstractFileSystem$Change
intf org.openide.filesystems.AbstractFileSystem$Info
intf org.openide.filesystems.AbstractFileSystem$List
meth public boolean folder(java.lang.String)
meth public boolean readOnly(java.lang.String)
meth public java.io.InputStream inputStream(java.lang.String) throws java.io.FileNotFoundException
meth public java.io.OutputStream outputStream(java.lang.String) throws java.io.IOException
meth public java.lang.String mimeType(java.lang.String)
meth public java.lang.String[] children(java.lang.String)
meth public java.util.Date lastModified(java.lang.String)
meth public long size(java.lang.String)
meth public void createData(java.lang.String) throws java.io.IOException
meth public void createFolder(java.lang.String) throws java.io.IOException
meth public void delete(java.lang.String) throws java.io.IOException
meth public void lock(java.lang.String) throws java.io.IOException
meth public void markUnimportant(java.lang.String)
meth public void rename(java.lang.String,java.lang.String) throws java.io.IOException
meth public void unlock(java.lang.String)
supr java.lang.Object
hfds fs,serialVersionUID

CLSS public abstract org.openide.filesystems.MIMEResolver
cons public !varargs MIMEResolver(java.lang.String[])
cons public MIMEResolver()
 anno 0 java.lang.Deprecated()
meth public abstract java.lang.String findMIMEType(org.openide.filesystems.FileObject)
supr java.lang.Object
hfds resolvableMIMETypes

CLSS public org.openide.filesystems.MultiFileSystem
cons protected MultiFileSystem()
cons public MultiFileSystem(org.openide.filesystems.FileSystem[])
meth protected !varargs final void setDelegates(org.openide.filesystems.FileSystem[])
meth protected final org.openide.filesystems.FileSystem findSystem(org.openide.filesystems.FileObject)
meth protected final org.openide.filesystems.FileSystem[] getDelegates()
meth protected final void hideResource(java.lang.String,boolean) throws java.io.IOException
meth protected final void setPropagateMasks(boolean)
meth protected java.util.Set<? extends org.openide.filesystems.FileSystem> createLocksOn(java.lang.String) throws java.io.IOException
meth protected org.openide.filesystems.FileObject findResourceOn(org.openide.filesystems.FileSystem,java.lang.String)
meth protected org.openide.filesystems.FileSystem createWritableOn(java.lang.String) throws java.io.IOException
meth protected org.openide.filesystems.FileSystem createWritableOnForRename(java.lang.String,java.lang.String) throws java.io.IOException
meth protected static java.util.Enumeration<java.lang.String> hiddenFiles(org.openide.filesystems.FileObject,boolean)
meth protected void markUnimportant(org.openide.filesystems.FileObject)
meth protected void notifyMigration(org.openide.filesystems.FileObject)
meth public boolean isReadOnly()
meth public final boolean getPropagateMasks()
meth public java.lang.String getDisplayName()
meth public org.openide.filesystems.FileObject find(java.lang.String,java.lang.String,java.lang.String)
 anno 0 java.lang.Deprecated()
meth public org.openide.filesystems.FileObject findResource(java.lang.String)
meth public org.openide.filesystems.FileObject getRoot()
meth public org.openide.util.actions.SystemAction[] getActions()
meth public org.openide.util.actions.SystemAction[] getActions(java.util.Set<org.openide.filesystems.FileObject>)
meth public void addNotify()
meth public void prepareEnvironment(org.openide.filesystems.FileSystem$Environment) throws org.openide.filesystems.EnvironmentNotSupportedException
 anno 0 java.lang.Deprecated()
meth public void refresh(boolean)
meth public void removeNotify()
supr org.openide.filesystems.FileSystem
hfds MASK,WRITE_SYSTEM_INDEX,propagateMasks,root,serialVersionUID,systems

CLSS public org.openide.filesystems.Repository
cons public Repository(org.openide.filesystems.FileSystem)
intf java.io.Serializable
meth public final java.util.Enumeration<? extends org.openide.filesystems.FileObject> findAll(java.lang.String,java.lang.String,java.lang.String)
 anno 0 java.lang.Deprecated()
meth public final java.util.Enumeration<? extends org.openide.filesystems.FileObject> findAllResources(java.lang.String)
 anno 0 java.lang.Deprecated()
meth public final java.util.Enumeration<? extends org.openide.filesystems.FileSystem> fileSystems()
 anno 0 java.lang.Deprecated()
meth public final java.util.Enumeration<? extends org.openide.filesystems.FileSystem> getFileSystems()
 anno 0 java.lang.Deprecated()
meth public final org.openide.filesystems.FileObject find(java.lang.String,java.lang.String,java.lang.String)
 anno 0 java.lang.Deprecated()
meth public final org.openide.filesystems.FileObject findResource(java.lang.String)
 anno 0 java.lang.Deprecated()
meth public final org.openide.filesystems.FileSystem findFileSystem(java.lang.String)
 anno 0 java.lang.Deprecated()
meth public final org.openide.filesystems.FileSystem getDefaultFileSystem()
 anno 0 java.lang.Deprecated()
meth public final org.openide.filesystems.FileSystem[] toArray()
 anno 0 java.lang.Deprecated()
meth public final void addFileChangeListener(org.openide.filesystems.FileChangeListener)
 anno 0 java.lang.Deprecated()
meth public final void addFileSystem(org.openide.filesystems.FileSystem)
 anno 0 java.lang.Deprecated()
meth public final void addRepositoryListener(org.openide.filesystems.RepositoryListener)
 anno 0 java.lang.Deprecated()
meth public final void readExternal(java.io.ObjectInput) throws java.io.IOException,java.lang.ClassNotFoundException
 anno 0 java.lang.Deprecated()
meth public final void removeFileChangeListener(org.openide.filesystems.FileChangeListener)
 anno 0 java.lang.Deprecated()
meth public final void removeFileSystem(org.openide.filesystems.FileSystem)
 anno 0 java.lang.Deprecated()
meth public final void removeRepositoryListener(org.openide.filesystems.RepositoryListener)
 anno 0 java.lang.Deprecated()
meth public final void reorder(int[])
 anno 0 java.lang.Deprecated()
meth public final void writeExternal(java.io.ObjectOutput) throws java.io.IOException
 anno 0 java.lang.Deprecated()
meth public static org.openide.filesystems.Repository getDefault()
supr java.lang.Object
hfds fclSupport,fileSystems,fileSystemsClone,listeners,names,propListener,serialVersionUID,system,vetoListener
hcls Replacer

CLSS public org.openide.filesystems.RepositoryAdapter
cons public RepositoryAdapter()
intf org.openide.filesystems.RepositoryListener
meth public void fileSystemAdded(org.openide.filesystems.RepositoryEvent)
meth public void fileSystemPoolReordered(org.openide.filesystems.RepositoryReorderedEvent)
meth public void fileSystemRemoved(org.openide.filesystems.RepositoryEvent)
supr java.lang.Object

CLSS public org.openide.filesystems.RepositoryEvent
cons public RepositoryEvent(org.openide.filesystems.Repository,org.openide.filesystems.FileSystem,boolean)
meth public boolean isAdded()
meth public org.openide.filesystems.FileSystem getFileSystem()
meth public org.openide.filesystems.Repository getRepository()
supr java.util.EventObject
hfds add,fileSystem,serialVersionUID

CLSS public abstract interface org.openide.filesystems.RepositoryListener
intf java.util.EventListener
meth public abstract void fileSystemAdded(org.openide.filesystems.RepositoryEvent)
meth public abstract void fileSystemPoolReordered(org.openide.filesystems.RepositoryReorderedEvent)
meth public abstract void fileSystemRemoved(org.openide.filesystems.RepositoryEvent)

CLSS public org.openide.filesystems.RepositoryReorderedEvent
cons public RepositoryReorderedEvent(org.openide.filesystems.Repository,int[])
meth public int[] getPermutation()
meth public org.openide.filesystems.Repository getRepository()
supr java.util.EventObject
hfds perm,serialVersionUID

CLSS public abstract org.openide.filesystems.URLMapper
cons public URLMapper()
fld public final static int EXTERNAL = 1
fld public final static int INTERNAL = 0
fld public final static int NETWORK = 2
meth public abstract java.net.URL getURL(org.openide.filesystems.FileObject,int)
meth public abstract org.openide.filesystems.FileObject[] getFileObjects(java.net.URL)
meth public static java.net.URL findURL(org.openide.filesystems.FileObject,int)
meth public static org.openide.filesystems.FileObject findFileObject(java.net.URL)
meth public static org.openide.filesystems.FileObject[] findFileObjects(java.net.URL)
 anno 0 java.lang.Deprecated()
supr java.lang.Object
hfds CACHE_JUST_COMPUTING,cache,result,threadCache
hcls DefaultURLMapper

CLSS public final org.openide.filesystems.XMLFileSystem
cons public XMLFileSystem()
cons public XMLFileSystem(java.lang.String) throws org.xml.sax.SAXException
cons public XMLFileSystem(java.net.URL) throws org.xml.sax.SAXException
cons public XMLFileSystem(org.openide.filesystems.FileSystemCapability)
 anno 0 java.lang.Deprecated()
innr public static Impl
meth protected <%0 extends org.openide.filesystems.FileObject> java.lang.ref.Reference<{%%0}> createReference({%%0})
meth public boolean isReadOnly()
meth public java.lang.String getDisplayName()
meth public java.net.URL getXmlUrl()
meth public java.net.URL[] getXmlUrls()
meth public void addNotify()
meth public void removeNotify()
meth public void setXmlUrl(java.net.URL) throws java.beans.PropertyVetoException,java.io.IOException
meth public void setXmlUrl(java.net.URL,boolean) throws java.beans.PropertyVetoException,java.io.IOException
meth public void setXmlUrls(java.net.URL[]) throws java.beans.PropertyVetoException,java.io.IOException
supr org.openide.filesystems.AbstractFileSystem
hfds DTD_MAP,rootRef,serialVersionUID,urlsToXml
hcls FileObjRef,Handler,ResourceElem

CLSS public static org.openide.filesystems.XMLFileSystem$Impl
cons public Impl(org.openide.filesystems.XMLFileSystem)
intf org.openide.filesystems.AbstractFileSystem$Attr
intf org.openide.filesystems.AbstractFileSystem$Change
intf org.openide.filesystems.AbstractFileSystem$Info
intf org.openide.filesystems.AbstractFileSystem$List
meth public boolean folder(java.lang.String)
meth public boolean readOnly(java.lang.String)
meth public java.io.InputStream inputStream(java.lang.String) throws java.io.FileNotFoundException
meth public java.io.OutputStream outputStream(java.lang.String) throws java.io.IOException
meth public java.lang.Object readAttribute(java.lang.String,java.lang.String)
meth public java.lang.String mimeType(java.lang.String)
meth public java.lang.String[] children(java.lang.String)
meth public java.util.Date lastModified(java.lang.String)
meth public java.util.Enumeration<java.lang.String> attributes(java.lang.String)
meth public long size(java.lang.String)
meth public void createData(java.lang.String) throws java.io.IOException
meth public void createFolder(java.lang.String) throws java.io.IOException
meth public void delete(java.lang.String) throws java.io.IOException
meth public void deleteAttributes(java.lang.String)
meth public void lock(java.lang.String) throws java.io.IOException
meth public void markUnimportant(java.lang.String)
meth public void rename(java.lang.String,java.lang.String) throws java.io.IOException
meth public void renameAttributes(java.lang.String,java.lang.String)
meth public void unlock(java.lang.String)
meth public void writeAttribute(java.lang.String,java.lang.String,java.lang.Object) throws java.io.IOException
supr java.lang.Object
hfds fs,serialVersionUID

CLSS public final org.openide.filesystems.annotations.LayerBuilder
innr public final File
meth public org.openide.filesystems.annotations.LayerBuilder$File file(java.lang.String)
meth public org.openide.filesystems.annotations.LayerBuilder$File instanceFile(java.lang.String,java.lang.String,java.lang.Class) throws org.openide.filesystems.annotations.LayerGenerationException
meth public org.openide.filesystems.annotations.LayerBuilder$File shadowFile(java.lang.String,java.lang.String,java.lang.String)
supr java.lang.Object
hfds doc,originatingElement,processingEnv,unwrittenFiles

CLSS public final org.openide.filesystems.annotations.LayerBuilder$File
meth public java.lang.String getPath()
meth public org.openide.filesystems.annotations.LayerBuilder write()
meth public org.openide.filesystems.annotations.LayerBuilder$File boolvalue(java.lang.String,boolean)
meth public org.openide.filesystems.annotations.LayerBuilder$File bundlevalue(java.lang.String,java.lang.String) throws org.openide.filesystems.annotations.LayerGenerationException
meth public org.openide.filesystems.annotations.LayerBuilder$File bundlevalue(java.lang.String,java.lang.String,java.lang.String)
meth public org.openide.filesystems.annotations.LayerBuilder$File bytevalue(java.lang.String,byte)
meth public org.openide.filesystems.annotations.LayerBuilder$File charvalue(java.lang.String,char)
meth public org.openide.filesystems.annotations.LayerBuilder$File contents(java.lang.String)
meth public org.openide.filesystems.annotations.LayerBuilder$File doublevalue(java.lang.String,double)
meth public org.openide.filesystems.annotations.LayerBuilder$File floatvalue(java.lang.String,float)
meth public org.openide.filesystems.annotations.LayerBuilder$File instanceAttribute(java.lang.String,java.lang.Class) throws org.openide.filesystems.annotations.LayerGenerationException
meth public org.openide.filesystems.annotations.LayerBuilder$File intvalue(java.lang.String,int)
meth public org.openide.filesystems.annotations.LayerBuilder$File longvalue(java.lang.String,long)
meth public org.openide.filesystems.annotations.LayerBuilder$File methodvalue(java.lang.String,java.lang.String,java.lang.String)
meth public org.openide.filesystems.annotations.LayerBuilder$File newvalue(java.lang.String,java.lang.String)
meth public org.openide.filesystems.annotations.LayerBuilder$File position(int)
meth public org.openide.filesystems.annotations.LayerBuilder$File serialvalue(java.lang.String,byte[])
meth public org.openide.filesystems.annotations.LayerBuilder$File shortvalue(java.lang.String,short)
meth public org.openide.filesystems.annotations.LayerBuilder$File stringvalue(java.lang.String,java.lang.String)
meth public org.openide.filesystems.annotations.LayerBuilder$File url(java.lang.String)
meth public org.openide.filesystems.annotations.LayerBuilder$File urlvalue(java.lang.String,java.lang.String) throws org.openide.filesystems.annotations.LayerGenerationException
meth public org.openide.filesystems.annotations.LayerBuilder$File urlvalue(java.lang.String,java.net.URI) throws org.openide.filesystems.annotations.LayerGenerationException
supr java.lang.Object
hfds attrs,contents,path,url

CLSS public abstract org.openide.filesystems.annotations.LayerGeneratingProcessor
cons protected LayerGeneratingProcessor()
meth protected !varargs final org.openide.filesystems.annotations.LayerBuilder layer(javax.lang.model.element.Element[])
meth protected abstract boolean handleProcess(java.util.Set<? extends javax.lang.model.element.TypeElement>,javax.annotation.processing.RoundEnvironment) throws org.openide.filesystems.annotations.LayerGenerationException
meth public final boolean process(java.util.Set<? extends javax.lang.model.element.TypeElement>,javax.annotation.processing.RoundEnvironment)
supr javax.annotation.processing.AbstractProcessor
hfds ENTITY_RESOLVER,ERROR_HANDLER,GENERATED_LAYER,LOCAL_DTD_RESOURCE,NETWORK_DTD_URL,PUBLIC_DTD_ID,createdBuilders,generatedLayerByProcessor,originatingElementsByProcessor

CLSS public org.openide.filesystems.annotations.LayerGenerationException
cons public LayerGenerationException(java.lang.String)
cons public LayerGenerationException(java.lang.String,javax.lang.model.element.Element)
cons public LayerGenerationException(java.lang.String,javax.lang.model.element.Element,javax.lang.model.element.AnnotationMirror)
cons public LayerGenerationException(java.lang.String,javax.lang.model.element.Element,javax.lang.model.element.AnnotationMirror,javax.lang.model.element.AnnotationValue)
supr java.lang.Exception
hfds erroneousAnnotation,erroneousAnnotationValue,erroneousElement

