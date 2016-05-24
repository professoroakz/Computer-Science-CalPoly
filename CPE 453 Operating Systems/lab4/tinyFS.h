#ifndef TINYFS_H
#define TINYFS_H
#include <time.h>
#include <stdint.h>
/* The default size of the disk and file system block */
#define BLOCKSIZE 256
#define DEFAULT_DISK_SIZE 10240
#define DEFAULT_DISK_NAME “tinyFSDisk”
typedef int file_descriptor;

typedef struct block_info {
    uint8_t type, magic_number, block_number;
} block_info;

typedef struct free_block {
    struct block_info info;
    struct free_block *next;
} free_block;

typedef struct super_block {
    struct inode *root_inode;
    struct block_info info;

    struct super_block *next;
    struct free_block *free_blocks;

    uint8_t num_free_blocks;
    uint8_t *filename;

} super_block;

typedef struct file_extent {
    block_info info;
    struct file_extent *next;
    uint8_t inode_block_number;
    char data[BLOCKSIZE - 6];
} file_extent;

typedef struct inode {
    struct block_info info;
    struct file_extent *file_extent;
    struct inode *next;

    uint8_t filename;
    uint8_t size;
    uint8_t data;
    uint8_t io_flags;

    time_t created;
    time_t accessed;
    time_t modified;

    uint16_t file_descriptor;
    uintptr_t file_pointer;
} inode;

/* Makes a blank TinyFS file system of size nBytes on the file specified by ‘filename’. This function should use the emulated disk library to open the specified file, and upon success, format the file to be mountable. This includes initializing all data to 0x00, setting magic numbers, initializing and writing the superblock and inodes, etc. Must return a specified success/error code. */
int tfs_mkfs(char *filename, int nBytes);

/* tfs_mount(char *filename) “mounts” a TinyFS file system located within ‘filename’. tfs_unmount(void) “unmounts” the currently mounted file system. As part of the mount operation, tfs_mount should verify the file system is the correct type. Only one file system may be mounted at a time. Use tfs_unmount to cleanly unmount the currently mounted file system. Must return a specified success/error code. */
int tfs_mount(char *filename);

int tfs_unmount(void);

/* Opens a file for reading and writing on the currently mounted file system. Creates a dynamic resource table entry for the file, and returns a file descriptor (integer) that can be used to reference this file while the filesystem is mounted. */
file_descriptor tfs_openFile(char *name);

/* Closes the file, de-allocates all system/disk resources, and removes table entry */
int tfs_closeFile(file_descriptor FD);

/* Writes buffer ‘buffer’ of size ‘size’, which represents an entire file’s content, to the file system. Sets the file pointer to 0 (the start of file) when done. Returns success/error codes. */
int tfs_writeFile(file_descriptor FD, char *buffer, int size);

/* deletes a file and marks its blocks as free on disk. */
int tfs_deleteFile(file_descriptor FD);

/* reads one byte from the file and copies it to buffer, using the current file pointer location and incrementing it by one upon success. If the file pointer is already at the end of the file then tfs_readByte() should return an error and not increment the file pointer. */
int tfs_readByte(file_descriptor FD, char *buffer);

/* change the file pointer location to offset (absolute). Returns success/error codes.*/
int tfs_seek(file_descriptor FD, int offset);

#endif