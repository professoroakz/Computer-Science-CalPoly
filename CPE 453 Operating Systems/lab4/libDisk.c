
/* This functions opens a regular UNIX file and designates the first nBytes of
it as space for the emulated disk. nBytes should be an integral number of the
block size. If nBytes > 0 and there is already a file by the given filename,
that file’s contents may be overwritten. If nBytes is 0, an existing disk is
opened, and should not be overwritten. There is no requirement to maintain
integrity of any file content beyond nBytes.
The return value is -1 on failure or a disk number on success. */
int openDisk(char *filename, int nBytes) {
    return 0;
}

/* readBlock() reads an entire block of BLOCKSIZE bytes from the open disk
(identified by ‘disk’) and copies the result into a local buffer
(must be at least of BLOCKSIZE bytes). The bNum is a logical block number,
which must be translated into a byte offset within the disk. The translation
from logical to physical block is straightforward:
bNum=0 is the very first byte of the file.
bNum=1 is BLOCKSIZE bytes into the disk,
bNum=n is n*BLOCKSIZE bytes into the disk.
On success, it returns 0. -1 or smaller is returned if disk is not available
(hasn’t been opened) or any other failures. You must define your own error code system. */
int readBlock(int disk, int bNum, void *block);

/* writeBlock() takes disk number ‘disk’ and logical block number ‘bNum’ and writes
 the content of the buffer ‘block’ to that location. ‘block’ must be integral
 with BLOCKSIZE. The disk must be open. Just as in readBlock(), writeBlock()
 must translate the logical block bNum to the correct byte position in the file.
 On success, it returns 0. -1 or smaller is returned if disk is not available
 (i.e. hasn’t been opened) or any other failures. You must define your own error code system. */
int writeBlock(int disk, int bNum, void *block);

/* closeDisk() takes a disk number ‘disk’ and makes the disk closed to further I/O;
i.e. any subsequent reads or writes to a closed disk should return an error.
Closing a disk should also close the underlying file, committing any buffered writes. */
void closeDisk(int disk);