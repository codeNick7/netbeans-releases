#
# Gererated Makefile - do not edit!
#
# Edit the Makefile in the project folder instead (../Makefile). Each target
# has a -pre and a -post target defined where you can add custumized code.
#
# This makefile implements configuration specific macros and targets.


# Environment
MKDIR=mkdir
CP=cp
CCADMIN=CCadmin
RANLIB=ranlib
CC=cc
CCC=CC
CXX=CC
FC=f77

# Include project Makefile
include Makefile

# Object Directory
OBJECTDIR=build/Debug_x64/Sun12-Solaris-Sparc

# Object Files
OBJECTFILES= \
	${OBJECTDIR}/_ext/home/dl198383/tmp/nbi/engine/native/jnilib/solaris-sparc/../.common/src/CommonUtils.o \
	${OBJECTDIR}/_ext/home/dl198383/tmp/nbi/engine/native/jnilib/solaris-sparc/../.unix/src/jni_UnixNativeUtils.o

# C Compiler Flags
CFLAGS=-m64 -fPIC -shared -D_FILE_OFFSET_BITS=64 -D_LARGEFILE_SOURCE -xO2 -Xa

# CC Compiler Flags
CCFLAGS=-m64 -fPIC -shared -D_FILE_OFFSET_BITS=64 -D_LARGEFILE_SOURCE -xO2 -Xa
CXXFLAGS=-m64 -fPIC -shared -D_FILE_OFFSET_BITS=64 -D_LARGEFILE_SOURCE -xO2 -Xa

# Fortran Compiler Flags
FFLAGS=

# Link Libraries and Options
LDLIBSOPTIONS=

# Build Targets
.build-conf: ${BUILD_SUBPROJECTS} dist/solaris-sparcv9.so

dist/solaris-sparcv9.so: ${OBJECTFILES}
	${MKDIR} -p dist
	${LINK.c} -G -o dist/solaris-sparcv9.so -s -Kpic -h solaris-sparcv9.so ${OBJECTFILES} ${LDLIBSOPTIONS} 

${OBJECTDIR}/_ext/home/dl198383/tmp/nbi/engine/native/jnilib/solaris-sparc/../.common/src/CommonUtils.o: ../.common/src/CommonUtils.c 
	${MKDIR} -p ${OBJECTDIR}/_ext/home/dl198383/tmp/nbi/engine/native/jnilib/solaris-sparc/../.common/src
	$(COMPILE.c) -s -I/usr/java/include -I/usr/java/include/solaris -o ${OBJECTDIR}/_ext/home/dl198383/tmp/nbi/engine/native/jnilib/solaris-sparc/../.common/src/CommonUtils.o ../.common/src/CommonUtils.c

${OBJECTDIR}/_ext/home/dl198383/tmp/nbi/engine/native/jnilib/solaris-sparc/../.unix/src/jni_UnixNativeUtils.o: ../.unix/src/jni_UnixNativeUtils.c 
	${MKDIR} -p ${OBJECTDIR}/_ext/home/dl198383/tmp/nbi/engine/native/jnilib/solaris-sparc/../.unix/src
	$(COMPILE.c) -s -I/usr/java/include -I/usr/java/include/solaris -o ${OBJECTDIR}/_ext/home/dl198383/tmp/nbi/engine/native/jnilib/solaris-sparc/../.unix/src/jni_UnixNativeUtils.o ../.unix/src/jni_UnixNativeUtils.c

# Subprojects
.build-subprojects:

# Clean Targets
.clean-conf:
	${RM} -r build/Debug_x64
	${RM} dist/solaris-sparcv9.so

# Subprojects
.clean-subprojects:

# Enable dependency checking
.KEEP_STATE:
.KEEP_STATE_FILE:.make.state.${CONF}
