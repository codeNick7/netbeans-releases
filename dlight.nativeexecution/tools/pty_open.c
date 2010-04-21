/*
 * File:   ptysatelite.c
 * Author: ak119685
 *
 */

#if !defined (__SVR4) || !defined (__sun)
#define _XOPEN_SOURCE 600
#define _BSD_SOURCE
#include <termios.h>
#endif

#if !defined __APPLE__ && !defined __CYGWIN__
#include <stropts.h>
#include <sys/stream.h>
#include <sys/termios.h>
#else
#include <sys/select.h>
#include <sys/ioctl.h>
#endif

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>
#include <poll.h>

static int ptm_open(void) {
    int masterfd;

    if ((masterfd = posix_openpt(O_RDWR)) == -1) {
        return -1;
    }

    if (grantpt(masterfd) == -1 || unlockpt(masterfd) == -1) {
        close(masterfd);
        return -1;
    }

    return masterfd;
}

static int pts_open(int masterfd) {
    int slavefd;
    char* name;

    if ((name = ptsname(masterfd)) == NULL) {
        close(masterfd);
        return -1;
    }

    if ((slavefd = open(name, O_RDWR)) == -1) {
        close(masterfd);
        return -1;
    }

#if defined (__SVR4) && defined (__sun)

    if (ioctl(slavefd, I_PUSH, "ptem") == -1) {
        close(masterfd);
        close(slavefd);
        return -1;
    }

    if (ioctl(slavefd, I_PUSH, "ldterm") == -1) {
        close(masterfd);
        close(slavefd);
        return -1;
    }

    if (ioctl(slavefd, I_PUSH, "ttcompat") == -1) {
        close(masterfd);
        close(slavefd);
        return -1;
    }
#else
    /*
        struct termios termios_p;
        tcgetattr(slavefd, &termios_p);
        cfmakeraw(&termios_p);
        tcsetattr(slavefd, TCSANOW, &termios_p);
     */
#endif

    return slavefd;
}

#ifdef __APPLE__

static void loop(int master_fd) {
    ssize_t n;
    char buf[BUFSIZ];
    int select_result;
    fd_set read_set;

    for (;;) {
        FD_ZERO(&read_set);
        FD_SET(STDIN_FILENO, &read_set);
        FD_SET(master_fd, &read_set);
        select_result = select(master_fd + 1, &read_set, NULL, NULL, NULL);

        if (select_result == -1) {
            printf("ERROR: poll failed\n");
            exit(1);
        }

        if (FD_ISSET(STDIN_FILENO, &read_set)) {
            if ((n = read(STDIN_FILENO, buf, BUFSIZ)) == -1) {
                printf("ERROR: read from stdin failed\n");
                exit(1);
            }

            if (n == 0) {
                break;
            }

            if (write(master_fd, buf, n) == -1) {
                printf("ERROR: write to master failed\n");
                exit(1);
            }
        }

        if (FD_ISSET(master_fd, &read_set)) {
            if ((n = read(master_fd, buf, BUFSIZ)) == -1) {
                printf("ERROR: read from master failed\n");
                exit(1);
            }

            if (n == 0) {
                break;
            }

            if (write(STDOUT_FILENO, buf, n) == -1) {
                printf("ERROR: write to stdout failed\n");
                exit(1);
            }
        }
    }
}

#else

static void loop(int master_fd) {
    ssize_t n;
    char buf[BUFSIZ];
    struct pollfd fds[2];
    char control_buf [BUFSIZ];
    char data_buf [BUFSIZ];
    int flags;
    struct strbuf control;
    struct strbuf data;
    struct iocblk *ioc;
    struct termios *term;
    unsigned char msg_type;


    fds[0].fd = STDIN_FILENO;
    fds[0].events = POLLIN;
    fds[0].revents = 0;
    fds[1].fd = master_fd;
    fds[1].events = POLLIN;
    fds[1].revents = 0;


    control.buf = control_buf;
    control.maxlen = BUFSIZ;
    data.buf = data_buf;
    data.maxlen = BUFSIZ;


    int poll_result;

    for (;;) {
        poll_result = poll((struct pollfd*) & fds, 2, /*INFTIM*/ -1);

        if (poll_result == -1) {
            printf("ERROR: poll failed\n");
            exit(1);
        }

        if (fds[0].revents & POLLHUP || fds[1].revents & POLLHUP) {
            break;
        }

        if (fds[0].revents & POLLIN) {
            if ((n = read(STDIN_FILENO, buf, BUFSIZ)) == -1) {
                printf("ERROR: read from stdin failed\n");
                exit(1);
            }

            if (n == 0) {
                break;
            }

            if (write(master_fd, buf, n) == -1) {
                printf("ERROR: write to master failed\n");
                exit(1);
            }
        }

        if (fds[1].revents & POLLIN) {
            if ((n = getmsg(master_fd, &control, &data, &flags)) == -1) {
                printf("ERROR: getmsg from master failed\n");
                exit(1);
            }

            msg_type = control.buf[0];

            switch (msg_type) {
                case M_DATA:
                    if (write(STDOUT_FILENO, data.buf, data.len) == -1) {
                        printf("ERROR: write to stdout failed\n");
                        exit(1);
                    }
                case M_IOCTL:
                    ioc = (struct iocblk*) &data.buf[0];
                    switch (ioc->ioc_cmd) {
                        case TCSBRK:
                            goto out;
                    }
            }


        }
    }
out:
    ;
}
#endif

int main(int argc, char** argv) {
    int master_fd;
    int slave_fd;
    char* name;

    if ((master_fd = ptm_open()) == -1) {
        printf("ERROR: ptm_open() failed\n");
        exit(1);
    }

    if ((name = ptsname(master_fd)) == NULL) {
        close(master_fd);
        return -1;
    }

    // open slave to setup line discipline...
    if ((slave_fd = pts_open(master_fd)) == -1) {
        printf("ERROR: cannot open PTY slave\n");
        exit(1);
    }

    printf("PID: %d\n", getpid());
    printf("TTY: %s\n\n", name);
    fflush(stdout);

#if defined _XOPEN_STREAMS && _XOPEN_STREAMS != -1
    ioctl(master_fd, I_PUSH, "pckt");
#endif
    
    loop(master_fd);

    return (EXIT_SUCCESS);
}

#ifdef __CYGWIN__
//added for compatibility with cygwin 1.5

int posix_openpt(int flags) {
    return open("/dev/ptmx", flags);
}
#endif
